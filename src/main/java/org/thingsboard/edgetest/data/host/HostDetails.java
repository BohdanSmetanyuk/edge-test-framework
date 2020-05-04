package org.thingsboard.edgetest.data.host;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thingsboard.rest.client.RestClient;

abstract public class HostDetails {

    protected static final Logger logger = LogManager.getLogger(HostDetails.class);

    protected RestClient restClient;

    public RestClient getRestClient() {
        return restClient;
    }

    abstract public String getHostName();
}
