package org.thingsboard.edgetest.black.box.cases.entity;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.thingsboard.rest.client.RestClient;
import org.thingsboard.server.common.data.id.DeviceId;
import org.thingsboard.server.common.data.id.EdgeId;

import javax.annotation.PostConstruct;

@Slf4j
@RunWith(SpringRunner.class)
public class RelationBuilder {

    private RestClient restClient;

    @PostConstruct
    public void init() {
        restClient = new RestClient("http://localhost:8080");
        restClient.login("tenant@thingsboard.org", "tenant");
    }

    @Test
    public void assignDevicesToEdge() {
        DeviceId deviceId = restClient.getTenantDevice("Test Device").get().getId();
        EdgeId edgeId = restClient.getTenantEdge("Test Edge").get().getId();
        restClient.assignDeviceToEdge(edgeId, deviceId);
    }

}
