package org.thingsboard.edgetest.util;

import org.thingsboard.edgetest.clients.Client;
import org.thingsboard.edgetest.data.TelemetryProfile;
import org.thingsboard.edgetest.util.Converter;
import org.thingsboard.rest.client.RestClient;
import org.thingsboard.server.common.data.id.DeviceId;
import org.thingsboard.server.common.data.kv.Aggregation;
import org.thingsboard.server.common.data.kv.TsKvEntry;
import org.thingsboard.server.common.data.page.TimePageLink;

import java.util.ArrayList;
import java.util.List;

// implements Runnable
public class DeviceEmulator extends Thread {  // upgrade

    private TelemetryProfile tp;
    private Client client;
    private RestClient restClient;

    private long emulationTime;

    private List<String> deviceTelemetry;
    private List<String> cloudTelemetry;
    //private List<String> edgeTelemetry;

    private Long startTs;
    private Long endTs;
    private int limit;

    public DeviceEmulator(TelemetryProfile tp, Client client, RestClient restClient, String hostname, long emulationTime) {  // emulationTime here?
        super(tp.getDeviceDetails().getDeviceName() + " emulator");
        this.tp = tp;
        this.client = client;
        this.restClient = restClient;
        this.emulationTime = emulationTime;

        deviceTelemetry = new ArrayList<>();
        cloudTelemetry = new ArrayList<>();
        //edgeTelemetry = new ArrayList<>();

        client.init(hostname, tp.getDeviceDetails().getAccessToken());
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

    private List<String> getTimeseries(DeviceId deviceId, List<String> keys) {
        TimePageLink timePageLink = new TimePageLink(limit, startTs, endTs);
        List<TsKvEntry> timeseries = restClient.getTimeseries(deviceId, keys, 0L, Aggregation.NONE, timePageLink);
        return Converter.convertTsKvEntryListToSimpleStringList(timeseries, keys.size());
    }

    private void compareTelemetry() { // upgrade

        cloudTelemetry = getTimeseries(tp.getDeviceDetails().getDeviceId(), tp.getTelemetryKeys());
        System.out.println("Device telemetry");
        System.out.println(deviceTelemetry);
        System.out.println("Cloud telemetry");
        System.out.println(cloudTelemetry);
        System.out.println(deviceTelemetry.equals(cloudTelemetry));
    }
}
