package org.thingsboard.edgetest.services.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thingsboard.edgetest.configuration.ApplicationConfig;
import org.thingsboard.edgetest.services.action.ActionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PostConstruct;

@Service
public class ApplicationService {

    private static final Logger logger = LogManager.getLogger(ApplicationService.class);

    @Autowired
    ApplicationConfig applicationConfig;

    private String action;
    private ActionService actionService;

    @PostConstruct
    private void construct() {
        logger.info("Starting edge-test-framework");
    }

    public void run() {
        applicationConfig.getDescription();
        action = applicationConfig.getValue("action");
        logger.info("Preparing " + action + " application");
        actionService = applicationConfig.getActionService();
        logger.info("Starting " + action + " application");
        actionService.init(applicationConfig);
        actionService.start();
        logger.info("Successfully finished " + action + " application");
        logger.info("Edge-test-framework successfully finished");
    }

}
