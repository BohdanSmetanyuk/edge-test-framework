package org.thingsboard.edgetest.clients;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

abstract public class Client {

    final static protected String TELEMETRY = "/telemetry";
    final static protected String V1 = "v1/";

    protected static final Logger logger = LogManager.getLogger(Client.class);

    protected static String hostname;

    protected boolean connected = false;

    abstract public void init(String token);

    abstract public void publish(String content);

    abstract public void disconnect();

    protected void isConnected() throws RuntimeException {
        if(!connected) {
            throw new RuntimeException("Client uninialized!\nCall init method.");
        }
    }

    public static void setClientHostname(String hostname) {
        Client.hostname = hostname;
    }

}
