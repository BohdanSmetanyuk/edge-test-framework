package org.thingsboard.edgetest.data.host.cloud;

import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.thingsboard.edgetest.data.host.HostDetails;
import org.thingsboard.rest.client.RestClient;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component("cloud")
@NoArgsConstructor
public class CloudDetails extends HostDetails {

    @Value("${cloud.host.name}")
    private String cloudHostname;
    @Value("${cloud.user.username}")
    private String cloudUsername;
    @Value("${cloud.user.password}")
    private String cloudPassword;

    @PostConstruct
    private void construct() {
        restClient = new RestClient("http" + cloudHostname);
        logger.info("Rest client connected to cloud on " + "http" + cloudHostname);
        restClient.login(cloudUsername, cloudPassword);
        logger.info("Rest client successfully login with username: " + cloudUsername + " and password: " + cloudPassword);
    }

    @Override
    public String getHostName() {
        return cloudHostname;
    }

    @PreDestroy
    private void destroy() {
        restClient.logout();
        logger.info("Rest client successfully logout");
        restClient.close();
        logger.info("Rest client successfully disconnected from cloud");
    }

}
