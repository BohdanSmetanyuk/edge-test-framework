package org.thingsboard.edgetest.solution;

import org.springframework.stereotype.Component;
import org.thingsboard.edgetest.util.DeviceEmulator;
import org.thingsboard.edgetest.data.common.TelemetryProfile;
import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.edge.Edge;
import org.thingsboard.server.common.data.id.DeviceId;
import org.thingsboard.server.common.data.id.EdgeId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component("cloud-solution")
public class CloudSolution extends BaseSolution implements Solution {

    @Override
    String getSolutionDir() {
        return "cloud_solution";
    }

    @Override
    public void install() {
        try {
            entitySolver.installDevices();
            entitySolver.installEdges();
            entitySolver.assignDevicesToEdges();
        } catch (IOException ex) {
            logger.error(ex.getMessage());
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
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }
        entitySolver.unassignDevicesFromEdges(devicesDelete);
        entitySolver.deleteDevices(devicesDelete);
        entitySolver.deleteEdges(edgesDelete);
    }

    @Override
    public void emulate() {
        List<DeviceEmulator> deviceEmulators = new ArrayList<>();
        for (TelemetryProfile tp:  entitySolver.initTelemetryProfiles()) {
            DeviceEmulator deviceEmulator = new DeviceEmulator(tp);
            deviceEmulators.add(deviceEmulator);
            deviceEmulator.start();
        }
        try {
            for(DeviceEmulator deviceEmulator: deviceEmulators) {
                deviceEmulator.join();
            }
        } catch (InterruptedException ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

}
