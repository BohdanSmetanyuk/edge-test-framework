package org.thingsboard.edgetest.solutions;

import org.springframework.stereotype.Component;
import org.thingsboard.edgetest.clients.Client;
import org.thingsboard.rest.client.RestClient;
import org.thingsboard.server.common.data.Device;


// second test solution
@Component
public class TestSolution2 implements Solution {
    @Override
    public void install(RestClient restClient) {

        System.out.println("install 2");
        Device device = new Device();
        device.setName("Thermometer 2");
        device.setType("thermometer");
        device = restClient.saveDevice(device);
        System.out.println(restClient.getDeviceCredentialsByDeviceId(device.getId()));
    }

    @Override
    public void emulate(Client client, String hostname) {
        System.out.println("emulate 2");
    }
}
