package org.thingsboard.edgetest.black.box.cases.entity;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.thingsboard.rest.client.RestClient;
import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.asset.Asset;
import org.thingsboard.server.common.data.edge.Edge;

import javax.annotation.PostConstruct;


@Slf4j
@RunWith(SpringRunner.class)
public class EntityDeleter {

    private RestClient restClient;

    @PostConstruct
    public void init() {
        restClient = new RestClient("http://localhost:8080");
        restClient.login("tenant@thingsboard.org", "tenant");
    }

    @Test
    public void deviceDeleteTest() {
        Device device = restClient.getTenantDevice("Test Device").get();
        restClient.deleteDevice(device.getId());
    }

    @Test
    public void assetDeleteTest() {
        Asset asset = restClient.getTenantAsset("Test Asset").get();
        restClient.deleteAsset(asset.getId());
    }

    @Test
    public void edgeDeleteTest() {
        Edge edge = restClient.getTenantEdge("Test Edge").get();
        restClient.deleteEdge(edge.getId());
    }
}
