package org.thingsboard.edgetest.services.action.emulate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import org.thingsboard.edgetest.data.host.HostDetails;
import org.thingsboard.edgetest.services.action.ActionService;
import org.thingsboard.edgetest.util.DeviceEmulator;
import org.thingsboard.rest.client.RestClient;

import javax.annotation.PostConstruct;

@Service
public class EmulateService extends ActionService {

    @Value("${telemetry.send.protocol}")
    private String telemetrySendProtocol;

    @Value("${emulation.time}")
    private long emulationTime;

    @PostConstruct
    private void construct() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("org.thingsboard.edgetest.data.host");
        HostDetails cloudHost = context.getBean("cloud", HostDetails.class);
        HostDetails edgeHost = context.getBean("edge", HostDetails.class);
        String targetHostName;
        RestClient targetRestClient;
        if (target.equals("cloud")) {
            targetHostName = cloudHost.getHostName();
            targetRestClient = cloudHost.getRestClient();
        } else if(target.equals("edge")) {
            targetHostName = edgeHost.getHostName();
            targetRestClient = edgeHost.getRestClient();
        } else {
            throw new RuntimeException("Unrecognized target: " + target);
        }
        logger.info("Emulation target: " + "http" + targetHostName);
        logger.info("Emulation time: " + emulationTime + " , telemetry send protocol: " + telemetrySendProtocol);
        DeviceEmulator.setEmulator(cloudHost.getRestClient(), edgeHost.getRestClient(), targetHostName, emulationTime);
        solution.initSolution(targetRestClient);
        logger.info("Solution " + solutionName + " initialized");
    }

    public void start() {
        solution.emulate(telemetrySendProtocol);
    }
}
