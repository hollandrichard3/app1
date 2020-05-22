package com.lessons.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller("com.lessons.controllers.WelcomeController")
public class WelcomeController {

    private static final Logger logger = LoggerFactory.getLogger(WelcomeController.class);

    /**********************************************************************
     * showDefaultPage()
     ***********************************************************************/
    @RequestMapping("/")
    public String showDefaultPage()
    {
        logger.debug("showDefaultPage() started");

        // Forward the user to the main page
        return "forward:/app.html";
    }

    /*************************************************************************
     * REST endpoint /api/time
     *
     * @return a plain-old string with the system time (not JSON)
     *************************************************************************/
    @RequestMapping(value = "/api/time", method = RequestMethod.GET,
            produces = "application/json")
    public ResponseEntity<?> getDateTime() {
        logger.debug("getDateTime() started.");

        // Get the date/time
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String dateTime = dateFormat.format(date);

        // Return the date/time string as plain-text
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.TEXT_PLAIN)
                .body(dateTime);
    }

}
