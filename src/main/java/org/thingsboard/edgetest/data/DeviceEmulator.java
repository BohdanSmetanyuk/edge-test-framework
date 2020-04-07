package org.thingsboard.edgetest.data;

import org.thingsboard.edgetest.clients.Client;
import org.thingsboard.rest.client.RestClient;

import java.util.ArrayList;
import java.util.List;

public class DeviceEmulator extends Thread {  // upgrade later !!!

    private TelemetryProfile tp;
    private Client client;
    private RestClient restClient;

    // it's technically
    private long emulationTime;


    public DeviceEmulator(TelemetryProfile tp, Client client, RestClient restClient, String hostname, long emulationTime) {  // emulationTime here technically
        this.tp = tp;
        this.client = client;
        this.restClient = restClient;

        // it's technically
        this.emulationTime = emulationTime;

        client.init(hostname, tp.getDeviceDetails().getAccessToken());
    }

    public void run() { // upgrade later !!!
        List<String> deviceTelemetry = new ArrayList<>();
        List<String> cloudTelemetry = new ArrayList<>();

        // it's technically
        long breakTime = System.currentTimeMillis()+emulationTime;  // to env.

        while(System.currentTimeMillis()<breakTime) { // it's technically
            String content = tp.generateContent();
            client.publish(content);
            deviceTelemetry.add(tp.convertContentToSimpleString(content));

            // it's technically, no need will be when restClient.getTimeseries will working
            // sometimes 1 sec delay
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            cloudTelemetry.add(tp.convertTsKvEntryListToSimpleString(restClient.getLatestTimeseries(tp.getDeviceDetails().getDeviceId(), tp.getTelemetryKeys())));

            try {
                Thread.sleep(tp.getPublishFrequencyInMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        client.disconnect();
        System.out.println(deviceTelemetry);
        System.out.println(cloudTelemetry);
        System.out.println(deviceTelemetry.equals(cloudTelemetry));
    }
}
