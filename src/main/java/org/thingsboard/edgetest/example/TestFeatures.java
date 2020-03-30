package org.thingsboard.edgetest.example;

import org.thingsboard.rest.client.RestClient;
import org.thingsboard.server.common.data.edge.Edge;

public class TestFeatures {

    static public void main(String[] args) {
        String url = "http://localhost:8080";
        String username = "tenant@thingsboard.org";
        String password = "tenant";

        RestClient client = new RestClient(url);
        client.login(username, password);

        Edge edge = new Edge();

        edge.setName("Edge 1");
        edge.setType("simple_test_edge");

        // ?
        edge.setSecret("1234");
        edge.setRoutingKey("qwer");

        client.saveEdge(edge);

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
    // getTenantEdges

    // *** device-controller ***
    // unassignDeviceFromEdge +
    // assignDeviceToEdge +
}
