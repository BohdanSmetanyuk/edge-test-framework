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

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@RunWith(SpringRunner.class)
public class EntityCreator {

    private RestClient restClient;

    @PostConstruct
    public void init() {
        restClient = new RestClient("http://localhost:8080");
        restClient.login("tenant@thingsboard.org", "tenant");
    }

    @Test
    public void deviceCreationTest() {
        Device device = new Device();
        device.setName("Test Device");
        device.setType("test_device");
        restClient.saveDevice(device);
    }

    @Test
    public void assetCreationTest() {
        Asset asset = new Asset();
        asset.setName("Test Asset");
        asset.setType("test_asset");
        restClient.saveAsset(asset);
        assertThat(asset.getName()).isEqualTo("Test Asset"); //
    }

    @Test
    public void edgeCreationTest() {
        Edge edge = new Edge();
        edge.setName("Test Edge");
        edge.setType("test_edge");
        edge.setSecret("secret1234");
        edge.setRoutingKey("routing567");
        restClient.saveEdge(edge);
    }

}
