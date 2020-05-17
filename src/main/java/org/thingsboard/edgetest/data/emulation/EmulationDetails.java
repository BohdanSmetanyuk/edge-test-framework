package org.thingsboard.edgetest.data.emulation;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.thingsboard.edgetest.clients.Client;

public class EmulationDetails {

    @Getter
    private String telemetrySendProtocol;
    @Getter
    private long emulationTime;

    private AnnotationConfigApplicationContext context;

    @Setter
    @Getter
    private String mqttPort;

    public EmulationDetails(String telemetrySendProtocol, long emulationTime) {
        this.telemetrySendProtocol = telemetrySendProtocol;
        this.emulationTime = emulationTime;
        context = new AnnotationConfigApplicationContext("org.thingsboard.edgetest.clients." + telemetrySendProtocol);
    }

    public Client getClient() {
        return context.getBean(telemetrySendProtocol, Client.class);
    }
}
