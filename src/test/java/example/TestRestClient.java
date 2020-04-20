package example;

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
        TimePageLink timePageLink = new TimePageLink(100, 1587061477994L, 1587061482256L);
        System.out.println(client.getTimeseries(device.getId(), keys, 0L, Aggregation.NONE, timePageLink));

    }

}//keys=temperature,humidity&startTs=0&endTs=1587061482256&limit=100&agg=NONE"