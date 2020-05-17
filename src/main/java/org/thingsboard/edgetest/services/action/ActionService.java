package org.thingsboard.edgetest.services.action;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thingsboard.edgetest.configuration.ApplicationConfig;
import org.thingsboard.edgetest.solution.Solution;


abstract public class ActionService {

    protected String solutionName;
    protected String target;

    protected static final Logger logger = LogManager.getLogger(ActionService.class);

    protected Solution solution;

    protected boolean inited = false;

    public void init(ApplicationConfig applicationConfig) {
        solutionName = applicationConfig.getValue("solution.name");
        target = applicationConfig.getValue("target");
        logger.info("Preparing to initialize solution " + solutionName);
        solution = applicationConfig.getSolution();
    }

    protected void isInited() throws RuntimeException {
        if(!inited) {
            throw new RuntimeException("Action service uninialized!\nCall init method.");
        }
    }

    abstract public void start();
}
