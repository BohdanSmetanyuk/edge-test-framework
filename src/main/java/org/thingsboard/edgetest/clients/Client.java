package org.thingsboard.edgetest.clients;


abstract public class Client {

    final String TELEMETRY = "/telemetry";
    final String V1 = "v1/";

    //private boolean inited // if false throw ClientNotInitException

    abstract public void init(String hostname, String token);

    abstract public void publish(String content);

    // test method
    abstract public String getProtocol();
}
