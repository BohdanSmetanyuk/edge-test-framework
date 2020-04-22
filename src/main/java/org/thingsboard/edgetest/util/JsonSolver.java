package org.thingsboard.edgetest.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonSolver {

    private ObjectMapper mapper;

    private String solutionDir;

    public JsonSolver(String solutionDir) {
        this.solutionDir = solutionDir;
        mapper = new ObjectMapper();
    }

    public JsonSolver(String solutionDir, ObjectMapper mapper) {
        this.solutionDir = solutionDir;
        this.mapper = mapper;
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

    private String getDataDir() {
        return Paths.get("src", "main", "data").toFile().getAbsolutePath();
    }

    private Path getEntitiesDir() {
        return Paths.get(getDataDir(), "json", solutionDir, "entities");
    }

    private Path getTelemetryDir() {
        return Paths.get(getDataDir(), "json", solutionDir, "telemetry");
    }

    private JsonNode readFileContentToJsonNode(Path path, String fileName) throws IOException {
        return mapper.readTree(Files.readAllBytes(path.resolve(fileName)));
    }

}
