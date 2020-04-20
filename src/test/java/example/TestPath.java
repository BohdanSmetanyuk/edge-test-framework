package example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestPath {

    static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {

        JsonNode jsonNode = readFileContentToJsonNode(getDevicesDir(), "devices.json");
        for(JsonNode node: jsonNode) {
            System.out.println(mapper.treeToValue(node.get("name"), String.class));
        }
    }

    private static String getDataDir() {
        return Paths.get("src\\main\\data").toFile().getAbsolutePath();
    }

    private static Path getDevicesDir() {
        return Paths.get(getDataDir(), "json", getSolutionName(), "entities");
    }

    private static String getSolutionName() {
        return "test_solution";
    }

    protected static JsonNode readFileContentToJsonNode(Path path, String fileName) throws IOException {
        return mapper.readTree(Files.readAllBytes(path.resolve(fileName)));
    }

}
