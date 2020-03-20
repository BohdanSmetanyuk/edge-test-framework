package org.thingsboard.edgetest.solutions;

import org.springframework.stereotype.Component;
import org.thingsboard.edgetest.clients.Client;
import org.thingsboard.rest.client.RestClient;


// second test solution (no need yet)
@Component
public class TestSolution2 implements Solution {
    @Override
    public void install(RestClient restClient) { System.out.println("install 2"); }

    @Override
    public void emulate(Client client, String hostname) {
        System.out.println("emulate 2");
    }
}
