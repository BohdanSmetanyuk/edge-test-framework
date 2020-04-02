package org.thingsboard.edgetest.example;

import org.thingsboard.rest.client.RestClient;

public class TestFeatures {

    static public void main(String[] args) {
        String url = "http://localhost:8080";
        String username = "tenant@thingsboard.org";
        String password = "tenant";

        RestClient client = new RestClient(url);
        client.login(username, password);
    }

    // *** edge-controller ***
    // unassignEdgeFromCustomer +
    // assignEdgeToPublicCustomer
    // assignEdgeToCustomer +
    // getCustomerEdges
    // saveEdge +
    // getEdgeTypes + /api/edges -> /api/edge/types
    // deleteEdge +
    // getEdgeById +
    // setRootRuleChain
    // findByQuery
    // getEdgesByIds +
    // getEdges
    // getTenantEdge +
    // getTenantEdges +

    // *** device-controller ***
    // unassignDeviceFromEdge +
    // assignDeviceToEdge +
    // getEdgeDevices +
}
