package org.thingsboard.edgetest.services.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import org.thingsboard.edgetest.services.action.ActionService;

import javax.annotation.PostConstruct;

@Service
public class ApplicationService {

    @Value("${action}")
    private String action;

    private ActionService actionService;


    @PostConstruct
    private void construct() {
        actionService = new AnnotationConfigApplicationContext("org.thingsboard.edgetest.services.action." + action).getBean(action + "Service", ActionService.class);
    }

    public void run() {
        actionService.start();
    }

}
