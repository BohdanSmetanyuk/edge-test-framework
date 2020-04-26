package org.thingsboard.edgetest.solution;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.thingsboard.edgetest.util.DeviceEmulator;
import org.thingsboard.edgetest.data.TelemetryProfile;
import org.thingsboard.edgetest.clients.Client;
import org.thingsboard.edgetest.util.EntitySolver;
import org.thingsboard.rest.client.RestClient;
import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.edge.Edge;
import org.thingsboard.server.common.data.id.DeviceId;
import org.thingsboard.server.common.data.id.EdgeId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component("edge-test-solution")
public class EdgeTestSolution implements Solution {

    private EntitySolver entitySolver;

    @Override
    public void initSolution(RestClient restClient) {
        entitySolver = new EntitySolver(restClient, "edge_test_solution");
    }

    @Override
    public void install() {
        try {
            entitySolver.installDevices();
            entitySolver.installEdges();
            entitySolver.assignDevicesToEdges();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void uninstall() {

        List<EdgeId> edgesDelete = new ArrayList<>();
        List<DeviceId> devicesDelete = new ArrayList<>();
        try {
            List<String> edgeTypes = entitySolver.getEdgeTypes();
            List<String> deviceTypes = entitySolver.getDeviceTypes();
            for(String edgeType: edgeTypes) {
                for (Edge edge : entitySolver.getTenantEdges(edgeType, edgeTypes.size())) {
                    edgesDelete.add(edge.getId());
                    for (String deviceType : deviceTypes) {
                        for(Device device: entitySolver.getEdgeDevices(edge, deviceType, deviceTypes.size())) {
                            devicesDelete.add(device.getId());
                        }
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        entitySolver.deleteDevices(devicesDelete);
        entitySolver.deleteEdges(edgesDelete);
    }

    @Override
    public void emulate(String telemetrySendProtocol) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("org.thingsboard.edgetest.clients." + telemetrySendProtocol);
        for (TelemetryProfile tp:  entitySolver.initTelemetryProfiles()) {
            Client client = context.getBean(telemetrySendProtocol, Client.class);
            new DeviceEmulator(tp, client).start();
        }
    }

}
