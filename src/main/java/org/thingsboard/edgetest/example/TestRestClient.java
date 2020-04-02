package org.thingsboard.edgetest.example;

import org.thingsboard.rest.client.RestClient;
import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.kv.Aggregation;
import org.thingsboard.server.common.data.page.TimePageLink;
import java.util.List;


public class TestRestClient {

    public static void main(String[] args) {

        String url = "http://localhost:8080";

        String username = "tenant@thingsboard.org";
        String password = "tenant";

        RestClient client = new RestClient(url);
        client.login(username, password);

        Device device = client.getTenantDevice("Thermometer").get();

        // only last ts
        List<String> keys = client.getTimeseriesKeys(device.getId());
        System.out.println(client.getTimeseries(device.getId(), keys, 20000000000000000L, Aggregation.NONE, new TimePageLink(1000000000)));
    }

}
