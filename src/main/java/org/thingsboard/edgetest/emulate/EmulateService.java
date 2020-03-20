package org.thingsboard.edgetest.emulate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.thingsboard.edgetest.RunEmulatorApplication;
import org.thingsboard.edgetest.clients.Client;
import org.thingsboard.edgetest.services.Service;
import org.thingsboard.edgetest.solutions.Solution;

@Component
public class EmulateService extends Service {

    @Value("${telemetry.send.protocol}")
    private String telemetrySendProtocol;

    private Client client;

    public void emulate() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RunEmulatorApplication.class);
        Solution solution = context.getBean(solutionName, Solution.class);
        client = context.getBean(telemetrySendProtocol, Client.class);
        solution.emulate(client);
    }
}
