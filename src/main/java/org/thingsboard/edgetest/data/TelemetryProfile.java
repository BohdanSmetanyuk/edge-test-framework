package org.thingsboard.edgetest.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.thingsboard.rest.client.RestClient;
import org.thingsboard.server.common.data.kv.TsKvEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TelemetryProfile {

    private static final ObjectMapper mapper = new ObjectMapper();

    private DeviceDetails deviceDetails;
    private String profile;
    private int publishFrequencyInMillis;
    private Map<String, HashMap<String, Integer>> telemetry;

    private TelemetryProfile(DeviceDetails deviceDetails, String profile, int publishFrequencyInMillis, Map<String, HashMap<String, Integer>> telemetry) {
        this.deviceDetails = deviceDetails;
        this.profile = profile;
        this.publishFrequencyInMillis = publishFrequencyInMillis;
        this.telemetry = telemetry;
    }

    public static TelemetryProfile getTelemetryProfile(String deviceName, RestClient restClient, String profile, int publishFrequencyInMillis, JsonNode telemetryNode) throws IOException {
        Map<String, HashMap<String, Integer>> keysAndValues = new HashMap<>();
        for (JsonNode keyAndValues: getKeysAndValuesAsJsonNode(telemetryNode)) {
            HashMap<String, Integer> minMaxValues = new HashMap<>();
            String key = mapper.treeToValue(keyAndValues.get("key"), String.class);
            int maxValue = mapper.treeToValue(keyAndValues.get("maxValue"), int.class);
            int minValue = mapper.treeToValue(keyAndValues.get("minValue"), int.class);
            minMaxValues.put("minValue", minValue);
            minMaxValues.put("maxValue", maxValue);
            keysAndValues.put(key, minMaxValues);
        }
        return new TelemetryProfile(new DeviceDetails(deviceName, restClient), profile, publishFrequencyInMillis, keysAndValues);
    }

    public static JsonNode getKeysAndValuesAsJsonNode(JsonNode telemetryNode) throws IOException {
        return mapper.treeToValue(telemetryNode.get("telemetry"), JsonNode.class);
    }

    public String generateContent() {  // upgrade later
        StringBuilder content = new StringBuilder("{");
        Iterator<String> iterator = telemetry.keySet().iterator();
        while (iterator.hasNext()) {
            content.append("'");
            String key = iterator.next();
            content.append(key);
            content.append("'");
            content.append(":");
            content.append(generateRandomValue(telemetry.get(key).get("minValue"), telemetry.get(key).get("maxValue")));
            if(iterator.hasNext()) {
                content.append(",");
            }
        }
        content.append("}");
        return content.toString();
    }

    private int generateRandomValue(int minValue, int maxValue) {
        return (int)(minValue + Math.random()*(maxValue-minValue));
    }

    public String convertTsKvEntryListToSimpleString(List<TsKvEntry> tsKvEntryList) { // upgrade later
        StringBuilder simpleString = new StringBuilder();
        for(TsKvEntry tsKvEntry: tsKvEntryList) {
            simpleString.append(tsKvEntry.getKey());
            simpleString.append(tsKvEntry.getValue());
        }
        return simpleString.toString();
    }

    public String convertContentToSimpleString(String content) {
        return content.replaceAll("\\W", "");
    }

    public DeviceDetails getDeviceDetails() {
        return deviceDetails;
    }

    public String getProfile() {
        return profile;
    }

    public int getPublishFrequencyInMillis() {
        return publishFrequencyInMillis;
    }

    public List<String> getTelemetryKeys() {
        return new ArrayList<>(telemetry.keySet());
    }
}
