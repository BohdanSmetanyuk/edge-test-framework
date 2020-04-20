package example;

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
    // getCustomerEdges // test
    // saveEdge +
    // getEdgeTypes +
    // deleteEdge +
    // getEdgeById +
    // setRootRuleChain
    // findByQuery  // test
    // getEdgesByIds +
    // getEdges
    // getTenantEdge +
    // getTenantEdges +

    // *** device-controller ***
    // unassignDeviceFromEdge +
    // assignDeviceToEdge +
    // getEdgeDevices +

    // *** asset-controller ***
    // unassignAssetFromEdge  // test
    // assignAssetToEdge  // test
    // getEdgeAssets  // test
}
