package org.thingsboard.edgetest.services.action;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.thingsboard.edgetest.data.host.HostDetails;
import org.thingsboard.edgetest.solution.Solution;

import javax.annotation.PostConstruct;

abstract public class ActionService {

    @Value("${solution.name}")
    protected String solutionName;
    @Value("${target}")
    protected String target;

    protected static final Logger logger = LogManager.getLogger(ActionService.class);

    protected Solution solution;

    @PostConstruct
    private void construct() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("org.thingsboard.edgetest.solution");
        logger.info("Preparing to initialize solution " + solutionName);
        solution = context.getBean(solutionName, Solution.class);
    }

    abstract public void start();
}
