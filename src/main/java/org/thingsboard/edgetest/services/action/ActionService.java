package org.thingsboard.edgetest.services.action;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.thingsboard.edgetest.solution.Solution;
import org.thingsboard.rest.client.RestClient;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

abstract public class ActionService {

    @Value("${solution.name}")
    private String solutionName;

    @Value("${cloud.host.name}")
    protected String cloudHostname;

    @Value("${cloud.user.username}")
    private String cloudUsername;
    @Value("${cloud.user.password}")
    private String cloudPassword;

    protected static final Logger logger = LogManager.getLogger(ActionService.class);

    protected Solution solution;
    protected RestClient restClientCloud;

    @PostConstruct
    private void construct() {
        restClientCloud = new RestClient("http" + cloudHostname);
        logger.info("Rest client connected to cloud on " + "http" + cloudHostname);
        restClientCloud.login(cloudUsername, cloudPassword);
        logger.info("Rest client successfully login with username: " + cloudUsername + " and password: " + cloudPassword);
        logger.info("Preparing to initialize solution " + solutionName);
        solution =  new AnnotationConfigApplicationContext("org.thingsboard.edgetest.solution").getBean(solutionName, Solution.class);
        solution.initSolution(restClientCloud);
        logger.info("Solution " + solutionName + " initialized");
    }

    abstract public void start();

    @PreDestroy
    private void destroy() {
        restClientCloud.logout();
        logger.info("Rest client successfully logout");
        restClientCloud.close();
        logger.info("Rest client successfully disconnected from cloud");
    }
}
