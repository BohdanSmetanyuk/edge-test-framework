package org.thingsboard.edgetest.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.thingsboard.edgetest.data.TelemetryProfile;
import org.thingsboard.rest.client.RestClient;
import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.edge.Edge;
import org.thingsboard.server.common.data.id.DeviceId;
import org.thingsboard.server.common.data.id.EdgeId;
import org.thingsboard.server.common.data.page.TextPageLink;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EntitySolver {

    private RestClient restClient;
    private JsonSolver jsonSolver;
    private ObjectMapper mapper;

    public EntitySolver(RestClient restClient, String solutionDir) {
        this.restClient = restClient;
        mapper = new ObjectMapper();
        jsonSolver = new JsonSolver(solutionDir, mapper);
    }

    public void installDevices() throws IOException {
        for (JsonNode deviceNode : jsonSolver.getDevicesAsJsonNode()) {
            Device device = new Device();
            device.setName(mapper.treeToValue(deviceNode.get("name"), String.class));
            device.setType(mapper.treeToValue(deviceNode.get("type"), String.class));
            device.setLabel(mapper.treeToValue(deviceNode.get("label"), String.class));
            restClient.saveDevice(device);
        }
    }

    public void installEdges() throws IOException{
        for (JsonNode edgeNode: jsonSolver.getEdgesAsJsonNode()) {
            Edge edge = new Edge();
            edge.setName(mapper.treeToValue(edgeNode.get("name"), String.class));
            edge.setType(mapper.treeToValue(edgeNode.get("type"), String.class));
            edge.setLabel(mapper.treeToValue(edgeNode.get("label"), String.class));
            edge.setType(mapper.treeToValue(edgeNode.get("routingKey"), String.class));
            edge.setLabel(mapper.treeToValue(edgeNode.get("secret"), String.class));
            restClient.saveEdge(edge);
        }
    }

    public void assignDevicesToEdges() throws IOException{
        for (JsonNode edgeNode : jsonSolver.getEdgesAsJsonNode()) {
            String edgeName = mapper.treeToValue(edgeNode.get("name"), String.class);
            for(JsonNode deviceNode: jsonSolver.getDevicesAsJsonNode()) {
                if(edgeName.equals(mapper.treeToValue(deviceNode.get("edge"), String.class))) {
                    EdgeId edgeId = restClient.getTenantEdge(edgeName).get().getId();
                    DeviceId deviceId = restClient.getTenantDevice(mapper.treeToValue(deviceNode.get("name"), String.class)).get().getId();
                    restClient.assignDeviceToEdge(edgeId, deviceId);
                }
            }
        }
    }

    public List<TelemetryProfile> initTelemetryProfiles(){
        List<TelemetryProfile> telemetryProfileList = new ArrayList<>();
        try {
            for (JsonNode deviceNode : jsonSolver.getDevicesAsJsonNode()) {
                String deviceName = mapper.treeToValue(deviceNode.get("name"), String.class);
                String profile = mapper.treeToValue(deviceNode.get("profile"), String.class);
                boolean telemetryFound = false;
                for (JsonNode telemetryNode : jsonSolver.getTelemetryAsJsonNode()) {
                    if(profile.equals(mapper.treeToValue(telemetryNode.get("profile"), String.class))) {
                        telemetryFound = true;
                        int publishFrequencyInMillis = mapper.treeToValue(telemetryNode.get("publishFrequencyInMillis"), int.class);
                        JsonNode keysAndValuesNode = mapper.treeToValue(telemetryNode.get("telemetry"), JsonNode.class);
                        TelemetryProfile telemetryProfile = TelemetryProfile.getTelemetryProfile(deviceName, restClient, profile, publishFrequencyInMillis, keysAndValuesNode);
                        telemetryProfileList.add(telemetryProfile);
                    }
                }
                if(!telemetryFound) {
                    throw new RuntimeException("No telemetry found for profile: " + profile + "\nDevice name: " + deviceName);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return telemetryProfileList;
    }

    public void deleteDevices(List<DeviceId> deviceIds) {
        for(DeviceId deviceId: deviceIds) {
            restClient.unassignDeviceFromEdge(deviceId);
            restClient.deleteDevice(deviceId);
        }
    }

    public void deleteEdges(List<EdgeId> edgeIds) {
        for(EdgeId edgeId: edgeIds) {
            restClient.deleteEdge(edgeId);
        }
    }

    public List<String> getEdgeTypes() throws IOException{
        List<String> edgeTypes = new ArrayList<>();
        for (JsonNode edgeNode: jsonSolver.getEdgesAsJsonNode()) {
            edgeTypes.add(mapper.treeToValue(edgeNode.get("type"), String.class));
        }
        return edgeTypes;
    }

    public List<String> getDeviceTypes() throws IOException{
        List<String> deviceTypes = new ArrayList<>();
        for (JsonNode deviceNode: jsonSolver.getDevicesAsJsonNode()) {
            deviceTypes.add(mapper.treeToValue(deviceNode.get("type"), String.class));
        }
        return deviceTypes;
    }

    public List<Edge> getTenantEdges(String edgeType, int edgeTypesSize) { //
        return restClient.getTenantEdges(edgeType, new TextPageLink(edgeTypesSize)).getData();
    }

    public List<Device> getEdgeDevices(Edge edge, String deviceType, int deviceTypesSize) { //
        return restClient.getEdgeDevices(edge.getId(), deviceType, new TextPageLink(deviceTypesSize)).getData();
    }

}
