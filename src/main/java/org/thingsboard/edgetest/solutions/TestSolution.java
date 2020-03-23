package org.thingsboard.edgetest.solutions;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.thingsboard.edgetest.data.TelemetryProfile;
import org.thingsboard.edgetest.clients.Client;
import org.thingsboard.rest.client.RestClient;
import org.thingsboard.server.common.data.Device;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


// will be renamed to "edge-test-solution"
@Component
public class TestSolution extends Solution{

    private static final String TEST_SOLUTION_DIR = "test_solution";

    @Override
    public void install(RestClient restClient) {
        installDevices(restClient);
    }

    @Override
    void installDevices(RestClient restClient) {
        try {
            for (JsonNode deviceNode : getDevicesAsJsonNode()) {
                Device device = new Device();
                device.setName(mapper.treeToValue(deviceNode.get("name"), String.class));
                device.setType(mapper.treeToValue(deviceNode.get("type"), String.class));
                device.setLabel(mapper.treeToValue(deviceNode.get("label"), String.class));
                restClient.saveDevice(device);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void emulate(RestClient restClient, Client client, String hostname) {  // upgrade later

        List<TelemetryProfile> telemetryProfileList = initTelemetryProfiles();

        for (TelemetryProfile tp: telemetryProfileList) {
            Device device = restClient.getTenantDevice(tp.getDeviceName()).get();
            String token = restClient.getDeviceCredentialsByDeviceId(device.getId()).get().getCredentialsId();

            client.init(hostname, token);


            // here must be some concurrency
            while(true) {
                String content = tp.generateContent();
                client.publish(content);
                try {
                    Thread.sleep(tp.getPublishFrequencyInMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    List<TelemetryProfile> initTelemetryProfiles() {
        List<TelemetryProfile> telemetryProfileList = new ArrayList<>();
        try {
            for (JsonNode deviceNode : getDevicesAsJsonNode()) {
                String deviceName = mapper.treeToValue(deviceNode.get("name"), String.class);
                String profile = mapper.treeToValue(deviceNode.get("profile"), String.class);
                TelemetryProfile telemetryProfile = new TelemetryProfile(deviceName, profile);
                for (JsonNode telemetryNode : getTelemetryAsJsonNode()) {
                    if(telemetryProfile.getProfile().equals(mapper.treeToValue(telemetryNode.get("profile"), String.class))) {
                        telemetryProfile.setPublishFrequencyInMillis(mapper.treeToValue(telemetryNode.get("publishFrequencyInMillis"), int.class));
                        for (JsonNode keyAndValues: getKeysAndValuesAsJsonNode(telemetryNode)) {
                            HashMap<String, Integer> minMaxValues = new HashMap<>();
                            String key = mapper.treeToValue(keyAndValues.get("key"), String.class);
                            int maxValue = mapper.treeToValue(keyAndValues.get("maxValue"), int.class);
                            int minValue = mapper.treeToValue(keyAndValues.get("minValue"), int.class);
                            minMaxValues.put("minValue", minValue);
                            minMaxValues.put("maxValue", maxValue);
                            telemetryProfile.putKeysAndValues(key, minMaxValues);
                        }
                        telemetryProfileList.add(telemetryProfile);
                    }
                    else {
                        System.out.println("No telemetry found for profile: " + telemetryProfile.getProfile() + "\nDevice name:" + telemetryProfile.getDeviceName());  // Exception
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return telemetryProfileList;
    }

    @Override
    String getSolutionName() {
        return TEST_SOLUTION_DIR;
    }

}
