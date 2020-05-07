package org.thingsboard.edgetest.services.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import org.thingsboard.edgetest.configuration.ApplicationConfig;
import org.thingsboard.edgetest.services.action.ActionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class ApplicationService {

    private static final Logger logger = LogManager.getLogger(ApplicationService.class);

    @Autowired
    ApplicationConfig applicationConfig;

    private String action;
    private ActionService actionService;

    public void run() {
        action = applicationConfig.getValue("action");
        logger.info("Preparing " + action + " application");
        actionService = new AnnotationConfigApplicationContext("org.thingsboard.edgetest.services.action." + action).getBean(action + "Service", ActionService.class);
        logger.info("Starting " + action + " application");
        actionService.init(applicationConfig);
        actionService.start();
        logger.info("Successfully finished " + action + " application");
    }

}
