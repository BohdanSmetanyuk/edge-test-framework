package org.thingsboard.edgetest.data.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.thingsboard.server.common.data.id.DeviceId;

@Getter
@AllArgsConstructor
public class DeviceDetails {

    private String deviceName;
    private DeviceId deviceId;
    private String accessToken;

    public String getDeviceName() {
        return deviceName;
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
