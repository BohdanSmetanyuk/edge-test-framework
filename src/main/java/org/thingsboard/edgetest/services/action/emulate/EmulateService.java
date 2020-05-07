package org.thingsboard.edgetest.services.action.emulate;

import org.springframework.stereotype.Service;
import org.thingsboard.edgetest.configuration.ApplicationConfig;
import org.thingsboard.edgetest.data.emulation.EmulationDetails;
import org.thingsboard.edgetest.data.host.cloud.CloudDetails;
import org.thingsboard.edgetest.data.host.edge.EdgeDetails;
import org.thingsboard.edgetest.services.action.ActionService;
import org.thingsboard.edgetest.util.DeviceEmulator;
import org.thingsboard.rest.client.RestClient;

@Service
public class EmulateService extends ActionService {

    private CloudDetails cloudHost;
    private EdgeDetails edgeHost;
    private EmulationDetails emulationDetails;

    @Override
    public void init(ApplicationConfig applicationConfig) {
        super.init(applicationConfig);
        cloudHost = applicationConfig.getCloudDetails();
        edgeHost = applicationConfig.getEdgeDetails();
        emulationDetails = applicationConfig.getEmulationDetails();
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
        logger.info("Emulation time: " + emulationDetails.getEmulationTime() + " , telemetry send protocol: " + emulationDetails.getTelemetrySendProtocol());
        DeviceEmulator.setEmulator(cloudHost.getRestClient(), edgeHost.getRestClient(), targetHostName, emulationDetails);
        solution.initSolution(targetRestClient);
        logger.info("Solution " + solutionName + " initialized");
        inited = true;
    }

    public void start() {
        solution.emulate();
    }
}
