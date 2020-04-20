package example;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.entity.ContentType;

import java.io.IOException;

public class TestHTTPClient {

    public static void main(String[] args){

        // PROBLEMS WITH TELEMETRY

        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost("http://127.0.0.1:8080/api/v1/ZURUYILN0T4GiANnMmwQ/telemetry"); // http, https
            while(true) {
                Thread.sleep(2000);
                StringEntity params = new StringEntity("{\"Humi\":71,\"Temp\":21}", ContentType.APPLICATION_JSON);
                request.setEntity(params);
                HttpResponse response = httpClient.execute(request);
                System.out.println(response.getStatusLine().getStatusCode());
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
