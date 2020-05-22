package com.lessons.services;

import com.lessons.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private static final Logger logger = LoggerFactory.getLogger(ReportService.class);

    private static final int CREATE_REVTYPE = 0;
    private static final int UPDATE_REVTYPE = 1;
    private static final int DELETE_REVTYPE = 2;

    @Resource
    private DataSource dataSource;

    public ReportService(){
        logger.debug("ReportService Constructor");
    }

    @PostConstruct
    public void init(){
        logger.debug("ReportService PostConstruct");
    }

    /**
     * Service method to get all reports
     * 1. Construct the SQL to select id, display_name from reports
     * 2. Create a JdbcTemplate object
     * 3. Create a row mapper
     * 4. Use your jdbcTemplate object and row mapper to run sql & get a list of DTO objects
     * 5. Return the list of DTO Objects
     * @return
     */
    public List<GetReportDTO> getAllReports(Integer pageSize, Integer startingRecord) {

        // PostGres starts records from 0.
        Integer offset = startingRecord - 1;

        // Construct the SQL to get these columns of data
        String sql = 	"select id, version, description, display_name, reviewed, reference_source, priority, " +
                        "created_date, last_modified_date, is_custom_report, reserved, reserved_by " +
                        "from reports " +
                        "order by id " +
                        "limit ?" +
                        " offset ?";

        // Use the rowMapper to convert the results into a list of ReportDTO objects
        BeanPropertyRowMapper rowMapper = new BeanPropertyRowMapper(GetReportDTO.class);

        // Create a JdbcTemplate object
        JdbcTemplate jt = new JdbcTemplate(this.dataSource);

        // Get a connection from the connection pool
        // Run the SQL
        // Convert the results into a list of GetReportDTO objects
        // Return the connection to the connection pool
        List<GetReportDTO> listOfReports = jt.query(sql, rowMapper, pageSize, offset);

        return listOfReports;
    }

    /**
     *Adds reports to the reports and reports_aud tables
     *
     * @param addReportDTO
     */
    public void addReport(AddReportDTO addReportDTO) {

        logger.debug("addReport() started");

        NamedParameterJdbcTemplate npjt = new NamedParameterJdbcTemplate(this.dataSource);

        Integer nextId = getNextId();

        // Construct the SQL to get these columns of data
        String sql = "Insert into reports(id, description, display_name, priority, created_date, is_custom_report, reference_source) " +
                "values( :id, :description, :display_name, :priority, now(), :is_custom_report, :reference_source) " +
                "returning *";

        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", nextId);
        paramMap.put("description", addReportDTO.getDescription());
        paramMap.put("display_name", addReportDTO.getDisplayName());
        paramMap.put("priority", addReportDTO.getPriority());
        paramMap.put("is_custom_report", addReportDTO.getIsCustomReport());
        paramMap.put("reference_source", addReportDTO.getReferenceSource());

        // Get a connection from the connection pool
        // Run the SQL
        // Convert the results into a list of GetReportDTO objects
        // Return the connection to the connection pool
        Map<String, Object> newReportMap = npjt.queryForMap(sql, paramMap);

        if(newReportMap == null){
            throw new RuntimeException("Records failed to be inserted.");
        }

        Integer transId = getNextTransactionId();

        sql = "Insert into reports_aud(id, description, display_name, priority, " +
                "is_custom_report, reference_source, username, timestamp, rev, rev_type) values( :id, :description, :display_name, :priority, " +
                ":is_custom_report, :reference_source, :username, now(), :rev, :revType) " +
                "returning *";

        newReportMap.put("username", "BOGUS_USER");
        newReportMap.put("rev", transId);
        newReportMap.put("revType", CREATE_REVTYPE);

        // Get a connection from the connection pool
        // Run the SQL
        // Convert the results into a list of GetReportDTO objects
        // Return the connection to the connection pool
        newReportMap = npjt.queryForMap(sql, newReportMap);

        if(newReportMap == null){
            throw new RuntimeException("Records failed to be inserted.");
        }

    }

    /**
     * @return next ID from the database sequence
     */
    public Integer getNextId(){
        String sqlNextVal = "Select nextval('seq_table_ids')";

        // Create a JdbcTemplate object
        JdbcTemplate jtemp = new JdbcTemplate(this.dataSource);

        return jtemp.queryForObject(sqlNextVal, Integer.class);
    }

    /**
     * @return next Transaction ID from the database sequence
     */
    public Integer getNextTransactionId(){
        String sqlNextVal = "Select nextval('seq_transaction_ids')";

        // Create a JdbcTemplate object
        JdbcTemplate jtemp = new JdbcTemplate(this.dataSource);

        return jtemp.queryForObject(sqlNextVal, Integer.class);
    }

    /**
     * Adds a report using a transaction
     *
     * @param addReportDTO
     */
    public void addReportWithTransaction(final AddReportDTO addReportDTO){
        TransactionTemplate tt = new TransactionTemplate();
        tt.setTransactionManager(new DataSourceTransactionManager(dataSource));

        // this transaction will throw a TransactionTimedOutException after 60 seconds
        // (causing the transaction to rollback)
        tt.setTimeout(60);

        tt.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {

                addReport(addReportDTO);

            }
        });

        logger.debug("Transaction finished.");
    }

    public boolean doesReportExist(Integer reportId){
        String sql = "Select count(*) from reports where id = ?";

        // Create a JdbcTemplate object
        JdbcTemplate jtemp = new JdbcTemplate(this.dataSource);

        boolean exists = jtemp.queryForObject(sql, Integer.class, reportId) > 0 ? true : false;

        return exists;
    }

    public void deleteReport(Integer reportId){
        String sql = "Delete from reports where id = ?";

        // Create a JdbcTemplate object
        JdbcTemplate jtemp = new JdbcTemplate(this.dataSource);

        // Executing sql statement #1
        jtemp.update(sql, reportId);

        Integer transId = getNextTransactionId();

        sql = "Insert into reports_aud(id, username, timestamp, rev, rev_type) values( :id, :username, now(), :rev, :revType) ";

        Map<String, Object> reportMap = new HashMap<>();
        reportMap.put("id", reportId);
        reportMap.put("username", "BOGUS_USER");
        reportMap.put("rev", transId);
        reportMap.put("revType", DELETE_REVTYPE);

        NamedParameterJdbcTemplate npjt = new NamedParameterJdbcTemplate(this.dataSource);

        // Executing sql statement #2
        Integer rowsInserted = npjt.update(sql, reportMap);

        if(rowsInserted != 1){
            throw new RuntimeException("Records failed to be inserted.");
        }

    }

    public void deleteReportWithTransaction(final Integer reportId){
        TransactionTemplate tt = new TransactionTemplate();
        tt.setTransactionManager(new DataSourceTransactionManager(dataSource));

        // this transaction will throw a TransactionTimedOutException after 60 seconds
        // (causing the transaction to rollback)
        tt.setTimeout(60);

        tt.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {

                deleteReport(reportId);

            }
        });

        logger.debug("Transaction finished.");
    }

    public void editReport(EditReportDTO editReportDTO){
        String sql = "Update reports set version = :version, display_name = :display_name, last_modified_date = now(), " +
                "description = :description, priority = :priority, is_custom_report = :is_custom_report, reference_source = :reference_source where id = :id " +
                "returning *";

        // Update version#
        int newVersion = editReportDTO.getVersion() + 1;

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", editReportDTO.getId());
        paramMap.put("version", newVersion);
        paramMap.put("display_name", editReportDTO.getDisplayName());
        paramMap.put("description", editReportDTO.getDescription());
        paramMap.put("priority", editReportDTO.getPriority());
        paramMap.put("is_custom_report", editReportDTO.getIsCustomReport());
        paramMap.put("reference_source", editReportDTO.getReferenceSource());

        // Create a JdbcTemplate object
        NamedParameterJdbcTemplate npjt = new NamedParameterJdbcTemplate(this.dataSource);

        // Executing sql statement #1
        paramMap = npjt.queryForMap(sql, paramMap);

        Integer transId = getNextTransactionId();

        sql = "Insert into reports_aud(id, username, version, display_name, description, reviewed, priority, created_date, last_modified_date, is_custom_report, reference_source, reserved, reserved_by, timestamp, rev, rev_type) " +
                "values( :id, :username, :version, :display_name, :description, :reviewed, :priority, :created_date, :last_modified_date, :is_custom_report, :reference_source, :reserved, :reserved_by, now(), :rev, :revType) ";

        paramMap.put("username", "BOGUS_USER");
        paramMap.put("rev", transId);
        paramMap.put("revType", UPDATE_REVTYPE);

        // Executing sql statement #2
        Integer rowsInserted = npjt.update(sql, paramMap);

        if(rowsInserted != 1){
            throw new RuntimeException("Records failed to be inserted.");
        }
    }

    public void editReportWithTransaction(final EditReportDTO editReportDTO){
        TransactionTemplate tt = new TransactionTemplate();
        tt.setTransactionManager(new DataSourceTransactionManager(dataSource));

        // this transaction will throw a TransactionTimedOutException after 60 seconds
        // (causing the transaction to rollback)
        tt.setTimeout(60);

        tt.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {

                editReport(editReportDTO);

            }
        });

        logger.debug("Transaction finished.");
    }

    /**
     * @return whether versions match.
     */
    public boolean isVersionMatch(Integer version, Integer reportId){
        String sql = "Select version from reports where id = ?";

        // Create a JdbcTemplate object
        JdbcTemplate jtemp = new JdbcTemplate(this.dataSource);

        int currentVersion = jtemp.queryForObject(sql, Integer.class, reportId);

        return version == currentVersion;
    }

    /**
     * @return user and last modified date.
     */
    public List<GetLastModifiedReportDTO> getLastModifiedBy(Integer reportId){
        String sql = "Select username, last_modified_date from reports_aud " +
                "where id = ? and last_modified_date is not null " +
                "order by last_modified_date DESC";

        // Use the rowMapper to convert the results into a list of ReportDTO objects
        BeanPropertyRowMapper rowMapper = new BeanPropertyRowMapper(GetLastModifiedReportDTO.class);

        // Create a JdbcTemplate object
        JdbcTemplate jt = new JdbcTemplate(this.dataSource);

        // Get a connection from the connection pool
        // Run the SQL
        // Convert the results into a list of GetReportDTO objects
        // Return the connection to the connection pool
        List<GetLastModifiedReportDTO> listOfReports = jt.query(sql, rowMapper, reportId);

        return listOfReports;
    }

}
