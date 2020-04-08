package org.thingsboard.edgetest.exceptions;

public class ClientNotConnectedException extends RuntimeException {

    public ClientNotConnectedException() {
        super("Client uninialized!\nCall init method.");
    }
}
