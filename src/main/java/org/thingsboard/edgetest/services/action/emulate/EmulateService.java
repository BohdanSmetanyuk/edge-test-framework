package org.thingsboard.edgetest.services.action.emulate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thingsboard.edgetest.clients.Client;
import org.thingsboard.edgetest.services.action.ActionService;
import org.thingsboard.edgetest.util.DeviceEmulator;
import org.thingsboard.rest.client.RestClient;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Service
public class EmulateService extends ActionService {

    @Value("${telemetry.send.protocol}")
    private String telemetrySendProtocol;

    @Value("${emulation.time}")
    private long emulationTime;

    @Value("${edge.host.name}")
    private String edgeHostname;
    @Value("${edge.user.username}")
    private String edgeUsername;
    @Value("${edge.user.password}")
    private String edgePassword;

    private RestClient restClientEdge;

    @PostConstruct
    private void initEdgeRestClient() {
        restClientEdge = new RestClient("http" + edgeHostname);
        logger.info("Rest client connected to edge on " + "http" + edgeHostname);
        restClientEdge.login(edgeUsername, edgePassword);
        logger.info("Rest client successfully login with username: " + edgeUsername + " and password: " + edgePassword);
    }

    public void start() {
        Client.setClientHostname(cloudHostname);
        logger.info("Emulation target: " + "http" + cloudHostname);
        logger.info("Emulation time: " + emulationTime + " , telemetry send protocol: " + telemetrySendProtocol);
        DeviceEmulator.setEmulator(restClientCloud, restClientEdge, emulationTime);
        solution.emulate(telemetrySendProtocol);
    }

    @PreDestroy
    private void logout() {
        restClientEdge.logout();
        logger.info("Rest client successfully logout");
        restClientEdge.close();
        logger.info("Rest client successfully disconnected from edge");
    }
}
