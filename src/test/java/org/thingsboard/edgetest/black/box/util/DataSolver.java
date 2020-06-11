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

    public File getDockerFile(String folder) {
        return new File(getDockerFilePath(folder));
    }

    public JsonNode getRuleChainAsJsonNode() throws IOException {
        return mapper.readTree(Files.readAllBytes(getRuleChainFilePath()));
    }

    private String getDataDir() {
        return Paths.get("src", "test", "data").toFile().getAbsolutePath();
    }

    private String getDockerFilePath(String folder) {
        return Paths.get(getDataDir(), "docker", folder, "docker-compose.yml").toString();
    }

    private Path getRuleChainFilePath() {
        return Paths.get(getDataDir(),"json", "rulechain", "root_rule_chain.json");
    }
}
