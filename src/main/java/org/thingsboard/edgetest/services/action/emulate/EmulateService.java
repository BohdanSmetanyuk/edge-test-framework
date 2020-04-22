package org.thingsboard.edgetest.services.action.emulate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thingsboard.edgetest.clients.Client;
import org.thingsboard.edgetest.services.action.ActionService;
import org.thingsboard.edgetest.util.DeviceEmulator;

@Service
public class EmulateService extends ActionService {

    @Value("${telemetry.send.protocol}")
    private String telemetrySendProtocol;

    @Value("${emulation.time}")
    private long emulationTime;

    public void start() {
        Client.setClientHostname(hostname);
        DeviceEmulator.setEmulator(restClient, emulationTime);
        solution.emulate(telemetrySendProtocol);
    }
}
