package org.thingsboard.edgetest;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.thingsboard.edgetest.install.UninstallService;

@Configuration
@ComponentScan({"org.thingsboard.edgetest.solutions", "org.thingsboard.edgetest.install"})
public class RunUninstallApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RunInstallApplication.class);
        UninstallService installService = context.getBean("uninstallService", UninstallService.class);
        installService.uninstall();
    }

}