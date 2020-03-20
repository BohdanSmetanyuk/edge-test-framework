package org.thingsboard.edgetest.solutions;

import org.springframework.stereotype.Component;
import org.thingsboard.edgetest.clients.Client;
import org.thingsboard.rest.client.RestClient;
import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.security.DeviceCredentials;


// will be renamed to "edge-test-solution"
@Component
public class TestSolution implements Solution{

    //private static final String TEST_SOLUTION_DIR = "test_solution";

    @Override
    public void install(RestClient restClient) {

        System.out.println("install 1");

        Device device = new Device();
        device.setName("Thermometer 1");
        device.setType("thermometer");
        device = restClient.saveDevice(device);

        //
        DeviceCredentials dc = restClient.getDeviceCredentialsByDeviceId(device.getId()).get();
        System.out.println("ACCESS_TOKEN: " + dc.getCredentialsId());
        //
    }

    @Override
    public void emulate(Client client, String hostname) {
        System.out.println(client.getProtocol());

        //
        String token = "pe55uwQcGxg2X3hvAFI5";   // token from id
        String content = "{\"Temp\":20,\"Humi\":70}";  // content from method, that generates random values
        //

        client.init(hostname, token);
        client.publish(content);

    }
}
