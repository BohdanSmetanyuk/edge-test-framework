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
import org.thingsboard.edgetest.black.box.config.EntityParams;
import org.thingsboard.rest.client.RestClient;
import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.asset.Asset;
import org.thingsboard.server.common.data.edge.Edge;

import java.util.List;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {CloudParams.class, EntityParams.class})
public class CreateEntities {

    private RestClient restClient;
    private List<Device> devices;
    private List<Edge> edges;
    private List<Asset> assets;

    @Autowired
    public void setCloudParams(CloudParams cloudParams, EntityParams entityParams) {
        restClient = new RestClient(cloudParams.getHost());
        restClient.login(cloudParams.getUsername(), cloudParams.getPassword());
        devices = entityParams.getDevices();
        edges = entityParams.getEdges();
        assets = entityParams.getAssets();
    }

    @Test
    public void deviceCreationTest() {
        for(Device device: devices) {
            restClient.saveDevice(device);
        }
    }

    @Ignore
    @Test
    public void assetCreationTest() {
        for(Asset asset: assets) {
            restClient.saveAsset(asset);
        }
    }

    @Test
    public void edgeCreationTest() {
        for(Edge edge: edges) {
            restClient.saveEdge(edge);
        }
    }

}
