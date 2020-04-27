package org.thingsboard.edgetest.clients.http;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.thingsboard.edgetest.clients.Client;

import java.io.IOException;

@Component("http")
@Scope("prototype")
public class HTTPClient extends Client {

    private final static String PROTOCOL = "http";
    private final static String API = "/api/";

    private HttpClient httpClient;
    private HttpPost request;

    @Override
    public void init(String token) {
        httpClient = HttpClientBuilder.create().build();
        request = new HttpPost(PROTOCOL + hostname + API + V1 + token + TELEMETRY);
        connected = true;
        logger.info("HTTP client connected to target");
    }

    @Override
    public void publish(String content) {
        try {
            isConnected();
            request.setEntity(new StringEntity(content, ContentType.APPLICATION_JSON));
            httpClient.execute(request);
        } catch (IOException ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        logger.info("HTTP client disconnected from target");
    }
}
