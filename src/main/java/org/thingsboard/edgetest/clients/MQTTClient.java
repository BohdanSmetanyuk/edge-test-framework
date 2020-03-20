package org.thingsboard.edgetest.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("mqtt")
public class MQTTClient extends Client{

    final String PROTOCOL = "tcp";
    @Value("${mqtt.port}")
    private String port;

    @Override
    public void init(String hostname, String token) {

    }

    @Override
    public void publish(String content) {

    }

    // test method
    @Override
    public String getProtocol() {
        return PROTOCOL + "\nPort: " + port;
    }
}
