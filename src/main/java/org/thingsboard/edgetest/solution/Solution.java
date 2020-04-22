package org.thingsboard.edgetest.solution;

import org.thingsboard.rest.client.RestClient;

public interface Solution {

    void install();

    void emulate(String telemetrySendProtocol);

    void uninstall();

    void initSolution(RestClient restClient);

}
