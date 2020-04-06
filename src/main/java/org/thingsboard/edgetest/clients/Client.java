package org.thingsboard.edgetest.clients;

abstract public class Client {

    final static String TELEMETRY = "/telemetry";
    final static String V1 = "v1/";

    //private boolean inited // if false throw ClientNotInitException

    abstract public void init(String hostname, String token);

    abstract public void publish(String content);

    public void disconnect() {
        System.out.println("Disconnect client");
    }

}
