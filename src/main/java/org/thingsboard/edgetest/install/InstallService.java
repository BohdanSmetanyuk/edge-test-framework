package org.thingsboard.edgetest.install;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.thingsboard.edgetest.RunInstallApplication;
import org.thingsboard.edgetest.services.Service;
import org.thingsboard.edgetest.solutions.Solution;
import org.thingsboard.rest.client.RestClient;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class InstallService extends Service {

    @Value("${user.username}")
    private String username;
    @Value("${user.password}")
    private String password;

    final String PROTOCOL = "http";

    private RestClient restClient;

    @PostConstruct
    private void login() {
        restClient = new RestClient(PROTOCOL + hostname);
        restClient.login(username, password);
    }

    @PreDestroy
    private void logout() {
        restClient.logout();
    }

    public void install() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RunInstallApplication.class);
        Solution solution = context.getBean(solutionName, Solution.class);
        solution.install(restClient);
    }
}
