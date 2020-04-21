package org.thingsboard.edgetest;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.thingsboard.edgetest.services.emulate.EmulateService;

@Configuration
@ComponentScan({"org.thingsboard.edgetest.solutions", "org.thingsboard.edgetest.services.emulate", "org.thingsboard.edgetest.clients"})
public class RunEmulatorApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RunEmulatorApplication.class);
        EmulateService emulateService = context.getBean("emulateService", EmulateService.class);
        emulateService.emulate();
    }
}
