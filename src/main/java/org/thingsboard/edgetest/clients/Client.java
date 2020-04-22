package org.thingsboard.edgetest.clients;

abstract public class Client {

    final static protected String TELEMETRY = "/telemetry";
    final static protected String V1 = "v1/";

    protected static String hostname;

    protected boolean connected = false;

    abstract public void init(String token);

    abstract public void publish(String content);

    public void disconnect() {
        System.out.println("Disconnect client from server.");
    }

    protected void isConnected() throws RuntimeException {
        if(!connected) {
            throw new RuntimeException("Client uninialized!\nCall init method.");
        }
    }

    public static void setClientHostname(String hostname) {
        Client.hostname = hostname;
    }

}
