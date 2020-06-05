package org.thingsboard.edgetest.black.box.cases.entity;

import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thingsboard.edgetest.black.box.config.CloudParams;
import org.thingsboard.rest.client.RestClient;
import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.asset.Asset;
import org.thingsboard.server.common.data.edge.Edge;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {CloudParams.class})
public class DeleteEntities {

    private RestClient restClient;

    @Autowired
    public void setParams(CloudParams cloudParams) {
        restClient = new RestClient(cloudParams.getHost());
        restClient.login(cloudParams.getUsername(), cloudParams.getPassword());
    }

    @Test
    public void deviceDeleteTest() {
        Device device = restClient.getTenantDevice("Test device").get();
        restClient.deleteDevice(device.getId());
    }

    @Ignore
    @Test
    public void assetDeleteTest() {
        Asset asset = restClient.getTenantAsset("Test asset").get();
        restClient.deleteAsset(asset.getId());
    }

    @Test
    public void edgeDeleteTest() {
        Edge edge = restClient.getTenantEdge("Test edge").get();
        restClient.deleteEdge(edge.getId());
    }
}
