package org.thingsboard.edgetest.clients;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("http")
@Scope("prototype")
public class HTTPClient extends Client{

    private final static String PROTOCOL = "http";
    private final static String API = "/api/";

    private HttpClient httpClient;
    private HttpPost request;

    @Override
    public void init(String hostname, String token) {
        httpClient = HttpClientBuilder.create().build();
        request = new HttpPost(PROTOCOL + hostname + API + V1 + token + TELEMETRY);
        connected = true;
        System.out.println("Client connected to server.");
    }

    @Override
    public void publish(String content) {
        try {
            isConnected();
            request.setEntity(new StringEntity(content, ContentType.APPLICATION_JSON));
            httpClient.execute(request);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
