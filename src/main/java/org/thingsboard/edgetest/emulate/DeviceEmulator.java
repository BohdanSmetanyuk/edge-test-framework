package org.thingsboard.edgetest.emulate;

import org.thingsboard.edgetest.clients.Client;
import org.thingsboard.edgetest.data.TelemetryProfile;
import org.thingsboard.rest.client.RestClient;
import org.thingsboard.server.common.data.kv.Aggregation;
import org.thingsboard.server.common.data.page.TimePageLink;

import java.util.ArrayList;
import java.util.List;

// implements Runnable
public class DeviceEmulator extends Thread {  // upgrade later

    private TelemetryProfile tp;
    private Client client;
    private RestClient restClient;

    private long emulationTime;

    private List<String> deviceTelemetry;
    private List<String> cloudTelemetry;
    //private List<String> edgeTelemetry;

    private Long startTs;
    private Long endTs;

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

        startTs = System.currentTimeMillis();

        long breakTime = System.currentTimeMillis()+emulationTime;

        while(System.currentTimeMillis()<breakTime) {
            String content = tp.generateContent();
            client.publish(content);
            deviceTelemetry.add(tp.convertContentToSimpleString(content));
            try {
                Thread.sleep(tp.getPublishFrequencyInMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        client.disconnect();

        endTs = System.currentTimeMillis();
    }

    private void compareTelemetry() { // upgrade

        TimePageLink timePageLink = new TimePageLink(100, startTs, endTs);
        cloudTelemetry = tp.convertTsKvEntryListToSimpleStringList(restClient.getTimeseries(tp.getDeviceDetails().getDeviceId(), tp.getTelemetryKeys(), 0L, Aggregation.NONE, timePageLink));
        System.out.println("Device telemetry");
        System.out.println(deviceTelemetry);
        System.out.println("Cloud telemetry");
        System.out.println(cloudTelemetry);
        System.out.println(deviceTelemetry.equals(cloudTelemetry));
    }
}
