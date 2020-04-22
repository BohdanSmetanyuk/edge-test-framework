package org.thingsboard.edgetest;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.thingsboard.edgetest.services.application.ApplicationService;

public class RunApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("org.thingsboard.edgetest.services.application");
        ApplicationService applicationService = context.getBean("applicationService", ApplicationService.class);
        applicationService.run();
    }
}
