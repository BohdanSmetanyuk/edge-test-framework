package org.thingsboard.edgetest.data.host.edge;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thingsboard.edgetest.data.host.HostDetails;
import org.thingsboard.rest.client.RestClient;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component("edge")
@NoArgsConstructor
public class EdgeDetails extends HostDetails {

    @Value("${edge.host.name}")
    private String edgeHostname;
    @Value("${edge.user.username}")
    private String edgeUsername;
    @Value("${edge.user.password}")
    private String edgePassword;

    @PostConstruct
    private void construct() {
        restClient = new RestClient("http" + edgeHostname);
        logger.info("Rest client connected to edge on " + "http" + edgeHostname);
        restClient.login(edgeUsername, edgePassword);
        logger.info("Rest client successfully login with username: " + edgeUsername + " and password: " + edgePassword);
    }

    @Override
    public String getHostName() {
        return edgeHostname;
    }

    @PreDestroy
    private void destroy() {
        restClient.logout();
        logger.info("Rest client successfully logout");
        restClient.close();
        logger.info("Rest client successfully disconnected from edge");
    }

}
