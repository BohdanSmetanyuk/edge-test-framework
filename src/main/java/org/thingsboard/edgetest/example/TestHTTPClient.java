package org.thingsboard.edgetest.example;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.entity.ContentType;

import java.io.IOException;

public class TestHTTPClient {

    public static void main(String[] args){

        HttpClient httpClient = HttpClientBuilder.create().build();
        try {
            HttpPost request = new HttpPost("http://demo.thingsboard.io/api/v1/pe55uwQcGxg2X3hvAFI5/telemetry"); // http, https
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
