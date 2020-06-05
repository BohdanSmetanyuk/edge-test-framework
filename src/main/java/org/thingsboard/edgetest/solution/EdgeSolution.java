package org.thingsboard.edgetest.solution;

import org.springframework.stereotype.Component;
import org.thingsboard.edgetest.data.common.TelemetryProfile;
import org.thingsboard.edgetest.util.DeviceEmulator;
import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.id.DeviceId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component("edge-solution")
public class EdgeSolution extends BaseSolution implements Solution {

    @Override
    String getSolutionDir() {
        return "edge_solution";
    }

    @Override
    public void install() {
        try {
            entitySolver.installDevices();
        } catch (IOException ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }
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

    @Override
    public void uninstall() {
        List<DeviceId> devicesDelete = new ArrayList<>();
        try {
            List<String> deviceTypes = entitySolver.getDeviceTypes();
            for (String deviceType : deviceTypes) {
                for(Device device: entitySolver.getTenantDevices(deviceType, deviceTypes.size())) {
                    devicesDelete.add(device.getId());
                }
            }
        } catch (IOException ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }
        entitySolver.deleteDevices(devicesDelete);
    }
}
