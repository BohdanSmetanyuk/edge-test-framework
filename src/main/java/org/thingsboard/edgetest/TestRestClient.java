package org.thingsboard.edgetest;

import org.springframework.web.client.HttpClientErrorException;
import org.thingsboard.rest.client.RestClient;
import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.asset.Asset;
import org.thingsboard.server.common.data.relation.EntityRelation;


public class TestRestClient {

    public static void main(String[] args) {

        String url = "http://localhost:8080";

        String username = "tenant@thingsboard.org";
        String password = "tenant";

        RestClient client = new RestClient(url);
        client.login(username, password);

        try {
            Asset asset = new Asset();
            asset.setName("Building 1");
            asset.setType("building");
            asset = client.saveAsset(asset);

            Device device = new Device();
            device.setName("Thermometer 1");
            device.setType("thermometer");
            device = client.saveDevice(device);

            EntityRelation relation = new EntityRelation();
            relation.setFrom(asset.getId());
            relation.setTo(device.getId());
            relation.setType("Contains");
            client.saveRelation(relation);
        } catch (HttpClientErrorException ex) {
            ex.getStackTrace();
        }
    }
}
