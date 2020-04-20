package org.thingsboard.edgetest.solutions;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.context.annotation.AnnotationConfigApplicationContext; ///
import org.springframework.stereotype.Component;
import org.thingsboard.edgetest.RunEmulatorApplication;  ///
import org.thingsboard.edgetest.emulate.DeviceEmulator;
import org.thingsboard.edgetest.data.TelemetryProfile;
import org.thingsboard.edgetest.clients.Client;
import org.thingsboard.edgetest.exceptions.NoTelemetryProfilesFoundException;
import org.thingsboard.rest.client.RestClient;
import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.edge.Edge;
import org.thingsboard.server.common.data.id.DeviceId;
import org.thingsboard.server.common.data.id.EdgeId;
import org.thingsboard.server.common.data.page.TextPageLink;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component("edge-test-solution")
public class EdgeTestSolution extends Solution{

    private static final String TEST_SOLUTION_DIR = "edge_test_solution";

    @Override
    public void install(RestClient restClient) {
        try {
            installDevices(restClient);
            //installEdges(restClient);
            //assignDevicesToEdges(restClient);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    void installDevices(RestClient restClient) throws IOException {
        for (JsonNode deviceNode : getDevicesAsJsonNode()) {
            Device device = new Device();
            device.setName(mapper.treeToValue(deviceNode.get("name"), String.class));
            device.setType(mapper.treeToValue(deviceNode.get("type"), String.class));
            device.setLabel(mapper.treeToValue(deviceNode.get("label"), String.class));
            restClient.saveDevice(device);
        }
    }

    @Override
    void installEdges(RestClient restClient) throws IOException {
        for (JsonNode edgeNode: getEdgesAsJsonNode()) {
            Edge edge = new Edge();
            edge.setName(mapper.treeToValue(edgeNode.get("name"), String.class));
            edge.setType(mapper.treeToValue(edgeNode.get("type"), String.class));
            edge.setLabel(mapper.treeToValue(edgeNode.get("label"), String.class));
            restClient.saveEdge(edge);
        }
    }

    @Override
    void assignDevicesToEdges(RestClient restClient) throws IOException {
        for (JsonNode edgeNode : getEdgesAsJsonNode()) {
            String edgeName = mapper.treeToValue(edgeNode.get("name"), String.class);
            for(JsonNode deviceNode: getDevicesAsJsonNode()) {
                if(edgeName.equals(mapper.treeToValue(deviceNode.get("edge"), String.class))) {
                    EdgeId edgeId = restClient.getTenantEdge(edgeName).get().getId();
                    DeviceId deviceId = restClient.getTenantDevice(mapper.treeToValue(deviceNode.get("name"), String.class)).get().getId();
                    restClient.assignDeviceToEdge(edgeId, deviceId);
                }
            }
        }
    }

    @Override
    public void uninstall(RestClient restClient) {  // upgrade later

        List<EdgeId> edgesDelete = new ArrayList<>();
        List<DeviceId> devicesDelete = new ArrayList<>();
        try {
            List<String> edgeTypes = getEdgeTypes();
            List<String> deviceTypes = getDeviceTypes();
            for(String edgeType: edgeTypes) {
                for (Edge edge : restClient.getTenantEdges(edgeType, new TextPageLink(edgeTypes.size())).getData()) {
                    edgesDelete.add(edge.getId());
                    for (String deviceType : deviceTypes) {
                        for(Device device: restClient.getEdgeDevices(edge.getId(), deviceType, new TextPageLink(deviceTypes.size())).getData()) {
                            devicesDelete.add(device.getId());
                        }
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        deleteDevices(devicesDelete, restClient);
        deleteEdges(edgesDelete, restClient);
    }


    private void deleteDevices(List<DeviceId> deviceIds, RestClient restClient) {
        for(DeviceId deviceId: deviceIds) {
            restClient.unassignDeviceFromEdge(deviceId);
            restClient.deleteDevice(deviceId);
        }
    }

    private void deleteEdges(List<EdgeId> edgeIds, RestClient restClient) {
        for(EdgeId edgeId: edgeIds) {
            restClient.deleteEdge(edgeId);
        }
    }

    private List<String> getEdgeTypes() throws IOException{
        List<String> edgeTypes = new ArrayList<>();
        for (JsonNode edgeNode: getEdgesAsJsonNode()) {
            edgeTypes.add(mapper.treeToValue(edgeNode.get("type"), String.class));
        }
        return edgeTypes;
    }

    private List<String> getDeviceTypes() throws IOException{
        List<String> deviceTypes = new ArrayList<>();
        for (JsonNode deviceNode: getDevicesAsJsonNode()) {
            deviceTypes.add(mapper.treeToValue(deviceNode.get("type"), String.class));
        }
        return deviceTypes;
    }

    @Override
    public void emulate(RestClient restClient, String hostname, String telemetrySendProtocol, long emulationTime) {  // upgrade later !!!
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RunEmulatorApplication.class);
        for (TelemetryProfile tp:  initTelemetryProfiles(restClient)) {
            // ThreadPoolExecutor, ExecutorService, ScheduledExecutorService, Future
            Client client = context.getBean(telemetrySendProtocol, Client.class);
            new DeviceEmulator(tp, client, restClient, hostname, emulationTime).start();   // emulationTime here technically
        }
    }

    @Override
    List<TelemetryProfile> initTelemetryProfiles(RestClient restClient) {
        List<TelemetryProfile> telemetryProfileList = new ArrayList<>();
        try {
            for (JsonNode deviceNode : getDevicesAsJsonNode()) {
                String deviceName = mapper.treeToValue(deviceNode.get("name"), String.class);
                String profile = mapper.treeToValue(deviceNode.get("profile"), String.class);
                boolean telemetryFound = false;
                for (JsonNode telemetryNode : getTelemetryAsJsonNode()) {
                    if(profile.equals(mapper.treeToValue(telemetryNode.get("profile"), String.class))) {
                        telemetryFound = true;
                        int publishFrequencyInMillis = mapper.treeToValue(telemetryNode.get("publishFrequencyInMillis"), int.class);
                        TelemetryProfile telemetryProfile = TelemetryProfile.getTelemetryProfile(deviceName, restClient, profile, publishFrequencyInMillis, telemetryNode);
                        telemetryProfileList.add(telemetryProfile);
                    }
                }
                if(!telemetryFound) {
                    throw new NoTelemetryProfilesFoundException(profile, deviceName);
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
