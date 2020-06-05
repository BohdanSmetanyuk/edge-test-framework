package org.thingsboard.edgetest.black.box.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class CloudParams {

    @Value("${cloud.host}")
    private String host;
    @Value("${cloud.username}")
    private String username;
    @Value("${cloud.password}")
    private String password;
}
