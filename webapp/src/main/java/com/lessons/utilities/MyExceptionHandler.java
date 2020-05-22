package com.lessons.utilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class MyExceptionHandler
{
    private static final Logger logger = LoggerFactory.getLogger(MyExceptionHandler.class);

    @Value("${development.mode}")
    private Boolean developmentMode;

    public MyExceptionHandler(){
        logger.debug("Inside MyExceptionHandler constructor");
    }

    @PostConstruct
    public void init(){
        logger.debug("Inside MyExceptionHandler PostConstruct");
    }

    /************************************************************************
     * handleException()
     *
     * NOTE: 	Undertow will *NOT* catch MaxUploadSizeExceededException
     *      	Tomcat will catch MaxUploadSizeExceededException
     *
     * This is the Global Exception Handler
     ************************************************************************/
    @ExceptionHandler({RuntimeException.class, Exception.class} )
    public ResponseEntity<?> handleException(Exception aException)
    {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // Log the error *and* stack trace
        if (null != request) {
            logger.error("Exception raised from call to " + request.getRequestURI(), aException);
        } else {
            logger.error("Exception raised from null request.", aException);
        }

        // Get the simple name of this exception (as a string)
        String exceptionSimpleName = aException.getClass().getSimpleName();

        // Set the returned status code based on the exception string
        HttpStatus returnedStatuscode;
        switch(exceptionSimpleName) {
            case "MaxUploadSizeExceededException":
                // The user attempted to upload a file that is too big
                returnedStatuscode = HttpStatus.PAYLOAD_TOO_LARGE;
                break;
            default:
                returnedStatuscode = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        // Return a ResponseEntity with media type as text_plain so that the front-end does not convert this to a JSON map
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);

        if (developmentMode) {
            // I am in developer mode so send the *real* error message to the front-end
            return new ResponseEntity<>(aException.toString(), headers, returnedStatuscode);
        }
        else {
            // I am in production mode so send a *generic* error message to the front-end
            return new ResponseEntity<>("Something went wrong. Please contact an administrator.", headers, returnedStatuscode);
        }
    }



}
