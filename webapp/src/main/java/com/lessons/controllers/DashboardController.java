package com.lessons.controllers;


import com.lessons.models.EditIndicatorDTO;
import com.lessons.models.ReportDTO;
import com.lessons.models.SearchDTO;
import com.lessons.models.SearchResultDTO;
import com.lessons.services.DashboardService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller("com.lessons.controllers.DashboardController")
public class DashboardController {
    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Resource
    private DashboardService dashboardService;


    /*************************************************************************
     * REST endpoint /api/dashboard/time
     *
     * @return a plain-old string with the database time (not JSON)
     *************************************************************************/
    @RequestMapping(value = "/api/dashboard/time", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<?> getDateTime() {
        logger.debug("getDashboardDetails() started.");

        // Get the date/time from the database
        String sDateTime = dashboardService.getDatabaseTime();

        // Return the date/time string as plain-text
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body(sDateTime);
    }

    /*
     * /api/getStuff?id=some_long_string
     */
    @RequestMapping(value = "/api/getStuff", method = RequestMethod.GET, produces = "application/json")
    public  ResponseEntity<?>  getStuff(@RequestParam String id) {
        logger.debug("id={}", id);

        String returnValue = "id=" + id;

        // Return the string as plain-text
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body(returnValue);
    }

    /*
     * /api/getStuff2?id=some_long_string
     */
    @RequestMapping(value = "/api/getStuff2", method = RequestMethod.GET, produces = "application/json")
    public  ResponseEntity<?>  getStuff2(@RequestParam(name="id") String stuffId) {
        logger.debug("stuffId={}", stuffId);

        String returnValue = "stuffId=" + stuffId;

        // Return the string as plain-text
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body(returnValue);
    }

    /*
     * /api/getStuff3?id=7
     */
    @RequestMapping(value = "/api/getStuff3", method = RequestMethod.GET, produces = "application/json")
    public  ResponseEntity<?>  getStuff3(@RequestParam(name="id") Integer id,
                                         @RequestParam(name="optional", required=false) String optional) {
        logger.debug("id={}  optional={}", id, optional);

        String returnValue = "id=" + id + " optional=" + optional;

        // Return the string as plain-text
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body(returnValue);
    }

    /*
     * /api/getStuff4?id=7
     * /api/getStuff4?optional=something&id=55
     */
    @RequestMapping(value = "/api/getStuff4", method = RequestMethod.GET, produces = "application/json")
    public  ResponseEntity<?>  getStuff4(@RequestParam(name="id") Integer id,
                                         @RequestParam(name="optional", defaultValue="not_set") String optional) {
        logger.debug("id={}  optional={}", id, optional);

        String returnValue = "id=" + id + " optional=" + optional;

        // Return the string as plain-text
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body(returnValue);
    }

    /*
     * /api/getStuff5?param1=this&param2=that&param3=othre_thing
     */
    @RequestMapping(value = "/api/getStuff5", method = RequestMethod.GET, produces = "application/json")
    public  ResponseEntity<?>  getStuff5(@RequestParam Map<String,String> allParams) {
        logger.debug("allParams={}", allParams.entrySet() );

        // Return the map of parameters back as JSON
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allParams);
    }

    /*
     * /api/getStuff6?ids=1,2,3,4,something
     */
    @RequestMapping(value = "/api/getStuff6", method = RequestMethod.GET, produces = "application/json")
    public  ResponseEntity<?>  getStuff6(@RequestParam List<String> ids) {
        logger.debug("ids={}", ids );

        // Return the map of parameters back as JSON
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ids);
    }

    /*
     * /api/getStuff7/{reportId}
     */
    @RequestMapping(value = "/api/getStuff7/{reportId}", method = RequestMethod.GET, produces = "application/json")
    public  ResponseEntity<?>  getStuff7(@PathVariable Integer reportId) {
        logger.debug("reportId={}", reportId );

        // Return the map of parameters back as JSON
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(reportId);
    }

    /*
     * /api/search REST end point
     * {
     *     indexName: "reports",
     *     rawQuery:  "1.2.3.4"
     * }
     *
     * NOTE:
     *   1) This is a POST call
     *   2) Using @RequestBody to tell the mapping to pull the DTO from the body
     */
    @RequestMapping(value = "/api/search", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> runSearch(@RequestBody SearchDTO searchDto) {
        logger.debug("searchDto={}", searchDto);

        // Create a new ArrayList (that will hold SearchResultDTO objects)
        ArrayList<SearchResultDTO> listOfResults = new ArrayList<>();

        // Create some objects
        SearchResultDTO result1 = new SearchResultDTO();
        result1.setId(1);
        result1.setDisplayName("Report 1");

        SearchResultDTO result2 = new SearchResultDTO();
        result2.setId(2);
        result2.setDisplayName("Report 2");
        Timestamp now = new Timestamp(System.currentTimeMillis());
        result2.setCreatedDate(now);

        SearchResultDTO result3 = new SearchResultDTO();
        result3.setId(3);
        result3.setDisplayName("Report 3");

        // Add the objects to the list
        listOfResults.add(result1);
        listOfResults.add(result2);
        listOfResults.add(result3);

        // Return the list of results back to the caller
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(listOfResults);
    }

//    @RequestMapping(value = "/api/reports/edit", method = RequestMethod.POST, produces = "application/json")
//    public ResponseEntity<?> editReport(@RequestBody ReportDTO reportDto) {
//        logger.debug("editReport started.");
//
//        if (reportDto.getName() == null){
//            return ResponseEntity
//                    .status(HttpStatus.BAD_REQUEST)
//                    .contentType(MediaType.TEXT_PLAIN)
//                    .body("Report name is missing");
//        }
//
//        String returnValue = "hi Mom!!!";
//
//        // Return the list of results back to the caller
//        return ResponseEntity
//                .status(HttpStatus.OK)
//                .contentType(MediaType.TEXT_PLAIN)
//                .body(returnValue);
//    }

    @RequestMapping(value = "/api/indicators/edit", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<?> editIndicator(@Valid @RequestBody EditIndicatorDTO editIndicatorDTO) {
        logger.debug("editIndicator started.");

//        if (editIndicatorDTO.getId() == null){
//            return ResponseEntity
//                    .status(HttpStatus.BAD_REQUEST)
//                    .contentType(MediaType.TEXT_PLAIN)
//                    .body("Report id is missing");
//        }
//        if (editIndicatorDTO.getValue() == null){
//            return ResponseEntity
//                    .status(HttpStatus.BAD_REQUEST)
//                    .contentType(MediaType.TEXT_PLAIN)
//                    .body("Report value is missing");
//        }
//        if (editIndicatorDTO.getIndicatorType() == null){
//            return ResponseEntity
//                    .status(HttpStatus.BAD_REQUEST)
//                    .contentType(MediaType.TEXT_PLAIN)
//                    .body("Report indicator type is missing");
//        }

        // Return the list of results back to the caller
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(editIndicatorDTO);
    }

}