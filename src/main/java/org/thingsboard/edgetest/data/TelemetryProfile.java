package org.thingsboard.edgetest.data;

import org.thingsboard.server.common.data.id.DeviceId;
import org.thingsboard.server.common.data.kv.TsKvEntry;

import java.util.*;

public class TelemetryProfile {

    private String deviceName;
    private String profile;
    private int publishFrequencyInMillis;
    private Map<String, HashMap<String, Integer>> keysAndValues;

    public TelemetryProfile(String deviceName, String profile) {
        this.deviceName = deviceName;
        this.profile = profile;
        keysAndValues = new HashMap<>();
    }

    public String generateContent() {  // upgrade later
        StringBuilder content = new StringBuilder("{");
        Iterator<String> iterator = keysAndValues.keySet().iterator();
        while (iterator.hasNext()) {
            content.append("'");
            String key = iterator.next();
            content.append(key);
            content.append("'");
            content.append(":");
            content.append(generateRandomValue(keysAndValues.get(key).get("minValue"), keysAndValues.get(key).get("maxValue")));
            if(iterator.hasNext()) {
                content.append(",");
            }
        }
        content.append("}");
        return content.toString();
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

    private int generateRandomValue(int minValue, int maxValue) {
        return (int)(minValue + Math.random()*(maxValue-minValue));
    }

    public String getDeviceName() {
        return deviceName;
    }

    public String getProfile() {
        return profile;
    }

    public int getPublishFrequencyInMillis() {
        return publishFrequencyInMillis;
    }

    public void setPublishFrequencyInMillis(int publishFrequencyInMillis) {
        this.publishFrequencyInMillis = publishFrequencyInMillis;
    }

    public List<String> getTelemetryKeys() {
        return new ArrayList<>(keysAndValues.keySet());
    }

    public void putKeysAndValues(String key, HashMap minMaxValues) {
        keysAndValues.put(key, minMaxValues);
    }
}
