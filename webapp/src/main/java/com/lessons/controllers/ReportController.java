package com.lessons.controllers;

import com.lessons.models.EditReportDTO;
import com.lessons.models.GetLastModifiedReportDTO;
import com.lessons.models.GetReportDTO;
import com.lessons.models.AddReportDTO;
import com.lessons.services.ReportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Controller("com.lessons.controllers.ReportController")
public class ReportController {
    private static final Logger logger = LoggerFactory.getLogger(ReportController.class);

    @Resource
    private ReportService reportService;
    @Value("${get.reports.default.pagesize:50}")
    private Integer defaultPageSize;

    @Value("${get.indicators.default.pagesize:}")
    private Integer indicatorDefaultPageSize;

    @Value("${my.string.list:}")
    private List<String> stringList;

    @Value("${max.upload.size.bytes}")
    private long maxUploadSizeInBytes;

    public ReportController(){
        logger.debug("Inside constructor.");
    }

    @PostConstruct
    public void init(){
        logger.debug("InsidepPost constructor.");
    }

    @RequestMapping(value = "/api/reports/add", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> editIndicator(@Valid @RequestBody AddReportDTO addReportDTO) {
        logger.debug("Report started.");

//        if(!addReportDTO.isValidDto()){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                    .contentType(MediaType.TEXT_PLAIN)
//                    .body("Missing required parameter(s).");
//        }

        reportService.addReportWithTransaction(addReportDTO);

        // Return the list of results back to the caller
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("");
    }

    @RequestMapping(value = "/api/reports/all", method = RequestMethod.GET, produces = "application/json")
    public  ResponseEntity<?>  getStuff3(@RequestParam(name="page_size", required=false) Integer pageSize,
                                         @RequestParam(name="starting_record", required=false) Integer startingRecord) {
        logger.debug("id={}  optional={}", pageSize, startingRecord);

        if(pageSize == null){
            pageSize = 20;
        }
        if(startingRecord == null){
            startingRecord = 1;
        }

        if(pageSize < 1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Cannot have page size less than 1");
        }

        if(startingRecord < 1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Cannot have starting record of less than 1");
        }

        List<GetReportDTO> getReportDTOList = reportService.getAllReports(pageSize, startingRecord);

        // Return the string as plain-text
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(getReportDTOList);
    }

    /*
    * Download Report REST Endpoint
    * GET /api/reports/download
     */
    @RequestMapping(value = "/api/reports/download", method = RequestMethod.GET)
    public ResponseEntity<?> downloadReport(@RequestParam(name="id") Integer reportId) {
        logger.debug("downloadReport started. ReportId=" + reportId);

        // Create a string (that will hold the downloaded textfile contents)
        String fileContents = "Report is here and it's a really long report...";

        // Set the default file name (that the browser will save as...)
        String fileName = "something.txt";

        // Create an HttpHeaders object (this holds your list of headers)
        HttpHeaders headers = new HttpHeaders();

        // Set a header with the default name to save this file as
        // -- So, the browser will do a Save As...
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentLength(fileContents.getBytes(Charset.defaultCharset()).length);

        // Return the ResponseEntity object with the special headers
        // -- This will cause the browser to do a Save File As... operation
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(fileContents.getBytes(Charset.defaultCharset()));
    }

    @RequestMapping(value = "/api/reports/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> downloadReportId(@PathVariable("id") Integer reportId) {
        logger.debug("downloadReport started. ReportId=" + reportId);

        // Create a string (that will hold the downloaded textfile contents)
        String fileContents = "Report is here and it's a really long report...";

        // Set the default file name (that the browser will save as...)
        String fileName = "something.txt";

        // Create an HttpHeaders object (this holds your list of headers)
        HttpHeaders headers = new HttpHeaders();

        // Set a header with the default name to save this file as
        // -- So, the browser will do a Save As...
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentLength(fileContents.getBytes(Charset.defaultCharset()).length);

        // Return the ResponseEntity object with the special headers
        // -- This will cause the browser to do a Save File As... operation
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(fileContents.getBytes(Charset.defaultCharset()));
    }

    @RequestMapping(value = "/api/reports/download2", method = RequestMethod.GET)
    public ResponseEntity<?> uploadReport() throws Exception {
        logger.debug("uploadReport started.");

        // Create a string (that will hold the uploaded textfile contents)
        String content = "";
        content = new String ( Files.readAllBytes( Paths.get("/home/rholland/Desktop/stuff1.txt") ) );

        // Return the ResponseEntity object with the special headers
        // -- This will cause the browser to do a Save File As... operation
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body(content);
    }

    @RequestMapping(value = "/api/reports/upload", method = RequestMethod.POST)
    public ResponseEntity<?> uploadFileWithParams(
                                                  @RequestParam(value = "file", required = true) MultipartFile aMultipartFile,
                                                  @RequestParam(name="id") Integer reportId)
    {
        logger.debug("uploadFileWithParams() started. ");

        String uploadedFilename = aMultipartFile.getOriginalFilename();
        long uploadedFileSize = aMultipartFile.getSize();

        logger.debug("ReportID is {}", reportId );
        logger.debug("Submitted file name is {}", uploadedFilename );
        logger.debug("Submitted file is {} bytes",uploadedFileSize );

        if(uploadedFileSize == 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Cannot upload an empty file");
        }

        if (uploadedFileSize > this.maxUploadSizeInBytes) {
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("You uploaded a file that exceeds the upload limit of " +
                            maxUploadSizeInBytes + " bytes.");
        }


        // Return a message back to the front-end
        String returnedMessage = "You uploaded the file called" + uploadedFilename + " with a size of " + uploadedFileSize + " bytes";

        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body(returnedMessage);

    }

    @RequestMapping(value = "/api/reports/delete/{reportId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteReport(@PathVariable("reportId") Integer reportId) {
        logger.debug("deleteReport started. ReportId=" + reportId);

        if(reportId < 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Report ID must be positive.");
        }

        if(!reportService.doesReportExist(reportId)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Report ID does not exist.");
        }

        reportService.deleteReportWithTransaction(reportId);

        // Return the ResponseEntity object with the special headers
        // -- This will cause the browser to do a Save File As... operation
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("");
    }

    @RequestMapping(value = "/api/reports/edit", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> editReport(@Valid @RequestBody EditReportDTO editReportDTO) {
        logger.debug("Edit Report started.");

        int reportId = editReportDTO.getId();

        if(!reportService.doesReportExist(reportId)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Report ID does not exist.");
        }
        if(!reportService.isVersionMatch(editReportDTO.getVersion(), reportId)){
            List<GetLastModifiedReportDTO> lastModifiedByList = reportService.getLastModifiedBy(reportId);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(lastModifiedByList);
        }

        reportService.editReportWithTransaction(editReportDTO);

        // Return the list of results back to the caller
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body("");
    }

}