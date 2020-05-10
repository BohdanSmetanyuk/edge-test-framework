package org.thingsboard.edgetest.data.host.edge;

import lombok.NoArgsConstructor;
import org.thingsboard.edgetest.data.host.HostDetails;
import org.thingsboard.rest.client.RestClient;

import javax.annotation.PreDestroy;

@NoArgsConstructor
public class EdgeDetails extends HostDetails {

    private String edgeHostname;
    private String edgeUsername;
    private String edgePassword;

    public EdgeDetails(String edgeHostname, String edgeUsername, String edgePassword) {
        this.edgeHostname = edgeHostname;
        this.edgeUsername = edgeUsername;
        this.edgePassword = edgePassword;
        initRestClient();
    }

    @Override
    public void init(HostDetails edgeDetails) {
        this.edgeHostname = edgeDetails.getHostName();
        this.edgeUsername = edgeDetails.getUserUsername();
        this.edgePassword = edgeDetails.getUserPassword();
        initRestClient();
    }

    @Override
    protected void initRestClient() {
        restClient = new RestClient(edgeHostname);
        logger.info("Rest client connected to edge on " + edgeHostname);
        restClient.login(edgeUsername, edgePassword);
        logger.info("Rest client successfully login with username: " + edgeUsername + " and password: " + edgePassword);
    }

    @Override
    public String getHostName() {
        return edgeHostname;
    }

    @Override
    public String getUserUsername() {
        return edgeUsername;
    }

    @Override
    public String getUserPassword() {
        return edgePassword;
    }

    @PreDestroy
    private void destroy() {
        restClient.logout();
        logger.info("Rest client successfully logout");
        restClient.close();
        logger.info("Rest client successfully disconnected from edge");
    }
}
