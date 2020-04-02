package org.thingsboard.edgetest.services;

import org.springframework.beans.factory.annotation.Value;
import org.thingsboard.rest.client.RestClient;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

abstract public class Service {
    @Value("${solution.name}")
    protected String solutionName;
    @Value("${host.name}")
    protected String hostname;

    @Value("${user.username}")
    private String username;
    @Value("${user.password}")
    private String password;

    private final static String REST_CLIENT_PROTOCOL = "http";

    protected RestClient restClient;

    @PostConstruct
    private void login() {
        restClient = new RestClient(REST_CLIENT_PROTOCOL + hostname);
        restClient.login(username, password);
    }

    @PreDestroy
    private void logout() {
        restClient.logout();
    }
}
