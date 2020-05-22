package com.lessons.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class ElasticSearchService {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchService.class);

    @Value("${es.enabled}")
    private Boolean esEnabled;

    @Value("${es.hostname:}")
    private String esHostName;

    @Value("${es.port:}")
    private Integer esPort;

    public ElasticSearchService(){
        logger.debug("Inside Constructor");
    }

    @PostConstruct
    public void init(){
        logger.debug("Inside Post Constructor");
        if(esEnabled == false){
            logger.debug("ElasticSearch is disabled.");
        } else {
            if (esHostName == null || "".equals(esHostName)){
                throw new RuntimeException("es.hostname is required");
            } else if (esPort == null){
                throw new RuntimeException("es.port is required");
            }
        }
    }
}
