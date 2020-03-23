package org.thingsboard.edgetest.solutions;

import org.springframework.stereotype.Component;
import org.thingsboard.edgetest.data.TelemetryProfile;
import org.thingsboard.edgetest.clients.Client;
import org.thingsboard.rest.client.RestClient;

import java.util.List;


// second test solution (no need yet)
@Component
public class TestSolution2 extends Solution {
    @Override
    public void install(RestClient restClient) { System.out.println("install 2"); }

    @Override
    void installDevices(RestClient restClient) {

    }

    @Override
    public void emulate(RestClient restClient, Client client, String hostname) {
        System.out.println("emulate 2");
    }

    @Override
    String getSolutionName() {
        return null;
    }

    @Override
    List<TelemetryProfile> initTelemetryProfiles() {
        return null;
    }
}
