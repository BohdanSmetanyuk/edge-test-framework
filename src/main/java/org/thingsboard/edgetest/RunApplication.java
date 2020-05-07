package org.thingsboard.edgetest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.thingsboard.edgetest.services.application.ApplicationService;

@ComponentScan({"org.thingsboard.edgetest.services.application", "org.thingsboard.edgetest.configuration"})
public class RunApplication implements CommandLineRunner {

    @Autowired
    ApplicationService applicationService;

    public static void main(String[] args) {
        SpringApplication.run(RunApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        applicationService.run();
    }
}
