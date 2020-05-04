package org.thingsboard.edgetest.data.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TelemetryProfile {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Getter
    private DeviceDetails deviceDetails;
    private String profile;
    @Getter
    private int publishFrequencyInMillis;
    private Map<String, HashMap<String, Integer>> telemetry;

    private TelemetryProfile(DeviceDetails deviceDetails, String profile, int publishFrequencyInMillis, Map<String, HashMap<String, Integer>> telemetry) {
        this.deviceDetails = deviceDetails;
        this.profile = profile;
        this.publishFrequencyInMillis = publishFrequencyInMillis;
        this.telemetry = telemetry;
    }

    public static TelemetryProfile getTelemetryProfile(DeviceDetails deviceDetails, String profile, int publishFrequencyInMillis, JsonNode keysAndValuesNode) throws IOException {
        Map<String, HashMap<String, Integer>> keysAndValues = new HashMap<>();
        for (JsonNode keyAndValuesNode: keysAndValuesNode) {
            HashMap<String, Integer> minMaxValues = new HashMap<>();
            String key = mapper.treeToValue(keyAndValuesNode.get("key"), String.class);
            int maxValue = mapper.treeToValue(keyAndValuesNode.get("maxValue"), int.class);
            int minValue = mapper.treeToValue(keyAndValuesNode.get("minValue"), int.class);
            minMaxValues.put("minValue", minValue);
            minMaxValues.put("maxValue", maxValue);
            keysAndValues.put(key, minMaxValues);
        }
        return new TelemetryProfile(deviceDetails, profile, publishFrequencyInMillis, keysAndValues);
    }

    public String generateContent() {
        Iterator<String> iterator = telemetry.keySet().iterator();
        Random random = new Random();
        StringBuilder content = new StringBuilder("{");
        while (iterator.hasNext()) {
            String key = iterator.next();
            int value = random.nextInt(telemetry.get(key).get("maxValue")+1)+telemetry.get(key).get("minValue");
            content.append("'").append(key).append("'").append(":").append(value);
            if(iterator.hasNext()) {
                content.append(",");
            }
        }
        content.append("}");
        return content.toString();
    }

    public List<String> getTelemetryKeys() {
        return new ArrayList<>(telemetry.keySet());
    }
}
