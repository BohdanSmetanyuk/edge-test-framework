package org.thingsboard.edgetest.black.box.cases.entity;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thingsboard.edgetest.black.box.config.CloudParams;
import org.thingsboard.rest.client.RestClient;
import org.thingsboard.server.common.data.id.DeviceId;
import org.thingsboard.server.common.data.id.EdgeId;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {CloudParams.class})
public class BuildRelations {

    private RestClient restClient;

    @Autowired
    public void setCloudParams(CloudParams params) {
        restClient = new RestClient(params.getHost());
        restClient.login(params.getUsername(), params.getPassword());
    }

    @Test
    public void assignDevicesToEdge() {
        DeviceId deviceId = restClient.getTenantDevice("Test device").get().getId();
        EdgeId edgeId = restClient.getTenantEdge("Test edge").get().getId();
        restClient.assignDeviceToEdge(edgeId, deviceId);
    }

}
