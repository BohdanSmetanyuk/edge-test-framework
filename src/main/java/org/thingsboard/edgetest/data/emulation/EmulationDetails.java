package org.thingsboard.edgetest.data.emulation;

import lombok.Getter;
import lombok.Setter;

@Getter
public class EmulationDetails {

    private String telemetrySendProtocol;
    private long emulationTime;

    @Setter
    private String mqttPort;

    public EmulationDetails(String telemetrySendProtocol, long emulationTime) {
        this.telemetrySendProtocol = telemetrySendProtocol;
        this.emulationTime = emulationTime;
    }
}
