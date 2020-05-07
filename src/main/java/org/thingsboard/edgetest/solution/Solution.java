package org.thingsboard.edgetest.solution;

import org.thingsboard.rest.client.RestClient;

public interface Solution {

    void install();

    void emulate();

    void uninstall();

    void initSolution(RestClient restClient);

}
