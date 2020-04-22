package org.thingsboard.edgetest.data;

import org.thingsboard.rest.client.RestClient;
import org.thingsboard.server.common.data.id.DeviceId;


public class DeviceDetails {

    private String deviceName;
    private DeviceId deviceId;
    private String accessToken;

    public DeviceDetails(String deviceName, RestClient restClient) {
        this.deviceName = deviceName;
        this.deviceId = restClient.getTenantDevice(this.deviceName).get().getId();
        this.accessToken = restClient.getDeviceCredentialsByDeviceId(this.deviceId).get().getCredentialsId();
    }

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
