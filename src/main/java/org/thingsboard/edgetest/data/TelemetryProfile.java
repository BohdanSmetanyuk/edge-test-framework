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
import java.util.Random;


// Think about static methods !!!
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

    public List<String> convertTsKvEntryListToSimpleStringList(List<TsKvEntry> tsKvEntryList) { // upgrade
        List<String> simpleStringList = new ArrayList<>();

        int numberOfKeys = getTelemetryKeys().size(); // here ?
        int sizeOfTsKvEntrySublist = tsKvEntryList.size()/numberOfKeys;

        for (int i = sizeOfTsKvEntrySublist-1; i >= 0; i--) {
            String ss = "";
            for(int j=0; j<numberOfKeys; j++) {
                TsKvEntry tsKvEntry = tsKvEntryList.get(i+j*sizeOfTsKvEntrySublist);
                ss += tsKvEntry.getKey() + tsKvEntry.getValueAsString();
            }
            simpleStringList.add(ss);
        }
        return simpleStringList;
    }

    public String convertContentToSimpleString(String content) {
        return content.replaceAll("\\W", "");
    }

    public DeviceDetails getDeviceDetails() {
        return deviceDetails;
    }

    public int getPublishFrequencyInMillis() {
        return publishFrequencyInMillis;
    }

    public List<String> getTelemetryKeys() {
        return new ArrayList<>(telemetry.keySet());
    }
}
