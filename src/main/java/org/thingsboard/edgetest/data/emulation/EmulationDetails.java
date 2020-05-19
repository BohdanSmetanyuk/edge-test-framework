package org.thingsboard.edgetest.data.emulation;

import lombok.Getter;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.thingsboard.edgetest.clients.Client;

public class EmulationDetails {

    @Getter
    private String telemetrySendProtocol;
    @Getter
    private EmulationMode mode;

    private AnnotationConfigApplicationContext context;

    @Getter
    private long emulationTime;
    @Getter
    private int messageAmount;

    public EmulationDetails(String telemetrySendProtocol) {
        this.telemetrySendProtocol = telemetrySendProtocol;
        context = new AnnotationConfigApplicationContext("org.thingsboard.edgetest.clients." + telemetrySendProtocol);
    }

    public void setEmulationTime(long emulationTime) {
        this.emulationTime = emulationTime;
        this.mode = EmulationMode.BY_EMULATION_TIME;
    }

    public void setMessageAmount(int messageAmount) {
        this.messageAmount = messageAmount;
        this.mode = EmulationMode.BY_MESSAGE_AMOUNT;
    }

    public Client getClient() {
        return context.getBean(telemetrySendProtocol, Client.class);
    }
}
