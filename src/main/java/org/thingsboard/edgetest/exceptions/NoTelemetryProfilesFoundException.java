package org.thingsboard.edgetest.exceptions;

public class NoTelemetryProfilesFoundException extends RuntimeException {

    public NoTelemetryProfilesFoundException(String profile, String deviceName) {
        super("No telemetry found for profile: " + profile + "\nDevice name: " + deviceName);
    }
}
