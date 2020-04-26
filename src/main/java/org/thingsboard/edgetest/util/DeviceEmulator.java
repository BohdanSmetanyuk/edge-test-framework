package org.thingsboard.edgetest.util;

import org.thingsboard.edgetest.clients.Client;
import org.thingsboard.edgetest.data.TelemetryProfile;
import org.thingsboard.rest.client.RestClient;
import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.id.DeviceId;
import org.thingsboard.server.common.data.kv.Aggregation;
import org.thingsboard.server.common.data.kv.TsKvEntry;
import org.thingsboard.server.common.data.page.TimePageLink;

import java.util.ArrayList;
import java.util.List;

public class DeviceEmulator extends Thread {  // target

    private TelemetryProfile tp;
    private Client client;

    private static RestClient restClientCloud;
    private static RestClient restClientEdge;
    private static long emulationTime;

    private List<String> deviceTelemetry;
    private List<String> cloudTelemetry;
    private List<String> edgeTelemetry;

    private Long startTs;
    private Long endTs;
    private int limit;

    public DeviceEmulator(TelemetryProfile tp, Client client) {
        super(tp.getDeviceDetails().getDeviceName() + " emulator");
        this.tp = tp;
        this.client = client;

        deviceTelemetry = new ArrayList<>();
        cloudTelemetry = new ArrayList<>();
        edgeTelemetry = new ArrayList<>();

        client.init(tp.getDeviceDetails().getAccessToken());
    }

    public void run() {

        pushTelemetry();
        compareTelemetry();
    }

    private void pushTelemetry() {

        limit = 0;
        startTs = System.currentTimeMillis();

        long breakTime = System.currentTimeMillis()+emulationTime;

        while(System.currentTimeMillis()<breakTime) {
            String content = tp.generateContent();
            client.publish(content);
            deviceTelemetry.add(Converter.convertContentToSimpleString(content));
            limit++;
            try {
                Thread.sleep(tp.getPublishFrequencyInMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        client.disconnect();

        endTs = System.currentTimeMillis();
    }

    private List<String> getCloudTimeseries(DeviceId deviceId, List<String> keys) {
        TimePageLink timePageLink = new TimePageLink(limit, startTs, endTs);
        List<TsKvEntry> timeseries = restClientCloud.getTimeseries(deviceId, keys, 0L, Aggregation.NONE, timePageLink);
        return Converter.convertTsKvEntryListToSimpleStringList(timeseries, keys.size());
    }

    private List<String> getEdgeTimeseries(String deviceName, List<String> keys) {
        TimePageLink timePageLink = new TimePageLink(limit, startTs, endTs);
        Device device = restClientEdge.getTenantDevice(deviceName).get();
        List<TsKvEntry> timeseries = restClientEdge.getTimeseries(device.getId(), keys, 0L, Aggregation.NONE, timePageLink);
        return Converter.convertTsKvEntryListToSimpleStringList(timeseries, keys.size());
    }

    private void compareTelemetry() {

        cloudTelemetry = getCloudTimeseries(tp.getDeviceDetails().getDeviceId(), tp.getTelemetryKeys());
        edgeTelemetry = getEdgeTimeseries(tp.getDeviceDetails().getDeviceName(), tp.getTelemetryKeys());
        System.out.println("Device telemetry");
        System.out.println(deviceTelemetry);
        System.out.println("Cloud telemetry");
        System.out.println(cloudTelemetry);
        System.out.println("Edge telemetry");
        System.out.println(edgeTelemetry);
        System.out.println(deviceTelemetry.equals(cloudTelemetry) && cloudTelemetry.equals(edgeTelemetry));
    }

    static public void setEmulator(RestClient restClientCloud, RestClient restClientEdge, long emulationTime) {
        DeviceEmulator.restClientCloud = restClientCloud;
        DeviceEmulator.restClientEdge = restClientEdge;
        DeviceEmulator.emulationTime = emulationTime;
    }

}
