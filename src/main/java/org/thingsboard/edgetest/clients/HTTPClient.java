package org.thingsboard.edgetest.clients;

import org.springframework.stereotype.Component;

@Component("http")
public class HTTPClient implements Client{

    final String PROTOCOL = "http";

    @Override
    public String getProtocol() {
        return PROTOCOL;
    }
}
