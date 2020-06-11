package org.thingsboard.edgetest.black.box.cases.entity;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.thingsboard.rest.client.RestClient;
import org.thingsboard.server.common.data.id.DeviceId;

import javax.annotation.PostConstruct;

@Slf4j
@RunWith(SpringRunner.class)
public class RelationDestroyer {

    private RestClient restClient;

    @PostConstruct
    public void init() {
        restClient = new RestClient("http://localhost:8080");
        restClient.login("tenant@thingsboard.org", "tenant");
    }

    @Test
    public void unassignDeviceFromEdge() {
        DeviceId deviceId = restClient.getTenantDevice("Test Device").get().getId();
        restClient.unassignDeviceFromEdge(deviceId);
    }

}
