package org.thingsboard.edgetest.black.box.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class EdgeParams {

    @Value("${edge.host}")
    private String host;
    @Value("${edge.username}")
    private String username;
    @Value("${edge.password}")
    private String password;
}
