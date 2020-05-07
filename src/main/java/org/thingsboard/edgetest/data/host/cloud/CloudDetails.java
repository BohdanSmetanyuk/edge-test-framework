package org.thingsboard.edgetest.data.host.cloud;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.thingsboard.edgetest.data.host.HostDetails;
import org.thingsboard.rest.client.RestClient;

import javax.annotation.PreDestroy;

@Component("cloud")
@NoArgsConstructor
public class CloudDetails extends HostDetails {

    private String cloudHostname;
    private String cloudUsername;
    private String cloudPassword;

    public CloudDetails(String cloudHostname, String cloudUsername, String cloudPassword) {
        this.cloudHostname = cloudHostname;
        this.cloudUsername = cloudUsername;
        this.cloudPassword = cloudPassword;
        initRestClient();
    }

    @Override
    public void init(HostDetails cloudDetails) {
        this.cloudHostname = cloudDetails.getHostName();
        this.cloudUsername = cloudDetails.getUserUsername();
        this.cloudPassword = cloudDetails.getUserPassword();
        initRestClient();
    }

    @Override
    protected void initRestClient() {
        restClient = new RestClient("http" + cloudHostname);
        logger.info("Rest client connected to cloud on " + "http" + cloudHostname);
        restClient.login(cloudUsername, cloudPassword);
        logger.info("Rest client successfully login with username: " + cloudUsername + " and password: " + cloudPassword);
    }

    @Override
    public String getHostName() {
        return cloudHostname;
    }

    @Override
    public String getUserUsername() {
        return cloudUsername;
    }

    @Override
    public String getUserPassword() {
        return cloudPassword;
    }

    @PreDestroy
    private void destroy() {
        restClient.logout();
        logger.info("Rest client successfully logout");
        restClient.close();
        logger.info("Rest client successfully disconnected from cloud");
    }
}
