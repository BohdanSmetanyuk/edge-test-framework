package org.thingsboard.edgetest.solutions;

import org.springframework.stereotype.Component;
import org.thingsboard.edgetest.data.TelemetryProfile;
import org.thingsboard.rest.client.RestClient;

import java.io.IOException;
import java.util.List;


@Component("simple-devices-solution")
public class SimpleDevicesSolution extends Solution {  // ???

    @Override
    public void install(RestClient restClient) { System.out.println("install 2"); }

    @Override
    void installDevices(RestClient restClient) throws IOException {

    }

    @Override
    void installEdges(RestClient restClient) throws IOException {
	
    }

    @Override
    void assignDevicesToEdges(RestClient restClient) throws IOException {

    }

    @Override
    public void emulate(RestClient restClient, String hostname, String telemetrySendProtocol, long emulationTime) {

    }

    @Override
    String getSolutionName() {
        return null;
    }

    @Override
    public void uninstall(RestClient restClient) {

    }

    @Override
    List<TelemetryProfile> initTelemetryProfiles(RestClient restClient) {
        return null;
    }
}
