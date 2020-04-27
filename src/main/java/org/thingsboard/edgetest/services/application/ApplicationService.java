package org.thingsboard.edgetest.services.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import org.thingsboard.edgetest.services.action.ActionService;
import javax.annotation.PostConstruct;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class ApplicationService {

    private static final Logger logger = LogManager.getLogger(ApplicationService.class);

    @Value("${action}")
    private String action;

    private ActionService actionService;


    @PostConstruct
    private void construct() {
        logger.info("Preparing " + action + " application");
        actionService = new AnnotationConfigApplicationContext("org.thingsboard.edgetest.services.action." + action).getBean(action + "Service", ActionService.class);
    }

    public void run() {
        logger.info("Starting " + action + " application");
        actionService.start();
        logger.info("Successfully finished " + action + " application");
    }

}
