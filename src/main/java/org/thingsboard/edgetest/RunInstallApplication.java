package org.thingsboard.edgetest;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.thingsboard.edgetest.install.InstallService;

// for future updates

@Configuration
@ComponentScan({"org.thingsboard.edgetest.solutions", "org.thingsboard.edgetest.install"})
public class RunInstallApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RunInstallApplication.class);
        InstallService installService = context.getBean("installService", InstallService.class);
        installService.install();
    }

}
