package org.thingsboard.edgetest.black.box.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DataSolver {

    private ObjectMapper mapper;

    public DataSolver() {
        mapper = new ObjectMapper();
    }


    public JsonNode getDevicesAsJsonNode() throws IOException {
        return readFileContentToJsonNode(getEntitiesDir(), "devices.json");
    }

    public JsonNode getEdgesAsJsonNode() throws IOException {
        return readFileContentToJsonNode(getEntitiesDir(), "edges.json");
    }

    public JsonNode getTelemetryAsJsonNode() throws IOException {
        return readFileContentToJsonNode(getTelemetryDir(), "telemetry.json");
    }

    public JsonNode getRelationsAsJsonNode() throws IOException {
        return readFileContentToJsonNode(getRelationsDir(), "relations.json");
    }

    public File getDockerFile(String folder) {
        return new File(getDockerFolderPath(folder));
    }

    private String getDataDir() {
        return Paths.get("src", "test", "data").toFile().getAbsolutePath();
    }

    private Path getEntitiesDir() {
        return Paths.get(getDataDir(), "json", "entities");
    }

    private Path getTelemetryDir() {
        return Paths.get(getDataDir(), "json", "telemetry");
    }

    private Path getRelationsDir() {
        return Paths.get(getDataDir(), "json", "relations");
    }

    private String getDockerFolderPath(String folder) {
        return Paths.get(getDataDir(), "docker", folder, "docker-compose.yml").toString();
    }

    private JsonNode readFileContentToJsonNode(Path path, String fileName) throws IOException {
        return mapper.readTree(Files.readAllBytes(path.resolve(fileName)));
    }
}
