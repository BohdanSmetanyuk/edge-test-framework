package org.thingsboard.edgetest.solutions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.thingsboard.edgetest.data.TelemetryProfile;
import org.thingsboard.rest.client.RestClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

// Think about abstract and non abstract methods

abstract public class Solution {

    ObjectMapper mapper = new ObjectMapper();

    abstract public void install(RestClient restClient);

    abstract void installDevices(RestClient restClient) throws IOException;

    abstract void installEdges(RestClient restClient) throws IOException;

    abstract void assignDevicesToEdges(RestClient restClient) throws IOException;

    abstract public void emulate(RestClient restClient, String hostname, String telemetrySendProtocol, long emulationTime);

    abstract String getSolutionName();

    abstract public void uninstall(RestClient restClient);

    abstract List<TelemetryProfile> initTelemetryProfiles(RestClient restClient);

    String getDataDir() {
        return Paths.get("src/main/data").toFile().getAbsolutePath(); // "src\\main\\data" on windows
    }

    Path getEntitiesDir() {
        return Paths.get(getDataDir(), "json", getSolutionName(), "entities");
    }

    Path getTelemetryDir() {
        return Paths.get(getDataDir(), "json", getSolutionName(), "telemetry");
    }

    JsonNode readFileContentToJsonNode(Path path, String fileName) throws IOException {
        return mapper.readTree(Files.readAllBytes(path.resolve(fileName)));
    }

    JsonNode getDevicesAsJsonNode() throws IOException {
        return readFileContentToJsonNode(getEntitiesDir(), "devices.json");
    }

    JsonNode getEdgesAsJsonNode() throws IOException {
        return readFileContentToJsonNode(getEntitiesDir(), "edges.json");
    }

    JsonNode getTelemetryAsJsonNode() throws IOException {
        return readFileContentToJsonNode(getTelemetryDir(), "telemetry.json");
    }

}
