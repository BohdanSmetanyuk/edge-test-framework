package org.thingsboard.edgetest.services.action;

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

    protected Solution solution;
    protected RestClient restClientCloud;

    @PostConstruct
    private void construct() {
        restClientCloud = new RestClient("http" + cloudHostname);
        restClientCloud.login(cloudUsername, cloudPassword);
        solution =  new AnnotationConfigApplicationContext("org.thingsboard.edgetest.solution").getBean(solutionName, Solution.class);
        solution.initSolution(restClientCloud);
    }

    abstract public void start();

    @PreDestroy
    private void destroy() {
        restClientCloud.logout();
    }
}
