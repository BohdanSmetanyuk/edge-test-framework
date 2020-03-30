package org.thingsboard.edgetest.solutions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.thingsboard.edgetest.data.TelemetryProfile;
import org.thingsboard.edgetest.clients.Client;
import org.thingsboard.rest.client.RestClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

abstract public class Solution {

    ObjectMapper mapper = new ObjectMapper();

    abstract public void install(RestClient restClient);

    abstract void installDevices(RestClient restClient);  // for the moment abstract

    abstract void installEdges(RestClient restClient); // for the moment abstract

    abstract public void emulate(RestClient restClient, Client client, String hostname);

    abstract String getSolutionName();

    abstract List<TelemetryProfile> initTelemetryProfiles();

    String getDataDir() {
        return Paths.get("src\\main\\data").toFile().getAbsolutePath();
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

    JsonNode getKeysAndValuesAsJsonNode(JsonNode telemetryNode) throws IOException {
        return mapper.treeToValue(telemetryNode.get("telemetry"), JsonNode.class);
    }

}
