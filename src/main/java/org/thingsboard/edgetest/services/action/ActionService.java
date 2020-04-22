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
    @Value("${host.name}")
    protected String hostname;

    @Value("${user.username}")
    private String username;
    @Value("${user.password}")
    private String password;

    private final static String REST_CLIENT_PROTOCOL = "http";

    protected Solution solution;
    protected RestClient restClient;

    @PostConstruct
    private void construct() {
        restClient = new RestClient(REST_CLIENT_PROTOCOL + hostname);
        restClient.login(username, password);
        solution =  new AnnotationConfigApplicationContext("org.thingsboard.edgetest.solution").getBean(solutionName, Solution.class);
        solution.initSolution(restClient);
    }

    abstract public void start();

    @PreDestroy
    private void destroy() {
        restClient.logout();
    }
}
