package org.thingsboard.edgetest.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("mqtt")
public class MQTTClient implements Client{

    final String PROTOCOL = "tcp";
    @Value("${mqtt.port}")
    private String port;

    @Override
    public String getProtocol() {
        return PROTOCOL;
    }
}
