package org.thingsboard.edgetest.clients;

import org.thingsboard.edgetest.exceptions.ClientNotConnectedException;

abstract public class Client {

    final static String TELEMETRY = "/telemetry";
    final static String V1 = "v1/";

    boolean connected = false;

    abstract public void init(String hostname, String token);

    abstract public void publish(String content);

    public void disconnect() {
        System.out.println("Disconnect client from server.");
    }

    void isConnected() throws ClientNotConnectedException {
        if(!connected) {
            throw new ClientNotConnectedException();
        }
    }

}
