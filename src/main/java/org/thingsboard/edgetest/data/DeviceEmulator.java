package org.thingsboard.edgetest.data;

import org.thingsboard.edgetest.clients.Client;
import org.thingsboard.rest.client.RestClient;
import org.thingsboard.server.common.data.id.DeviceId;

import java.util.ArrayList;
import java.util.List;

public class DeviceEmulator extends Thread {  // upgrade later !!!

    private TelemetryProfile tp;
    private Client client;
    private RestClient restClient;

    private DeviceId deviceId;

    public DeviceEmulator(TelemetryProfile tp, Client client, RestClient restClient, DeviceId deviceId, String accessToken, String hostname) {
        super(tp.getDeviceName());
        this.tp = tp;
        this.client = client;
        this.restClient = restClient;
        this.deviceId = deviceId;
        client.init(hostname, accessToken);
    }

    public void run() { // upgrade later !!!
        List<String> deviceTelemetry = new ArrayList<>();
        List<String> cloudTelemetry = new ArrayList<>();

        // it's technically
        long breakTime = System.currentTimeMillis()+10000L;  // to env.

        while(System.currentTimeMillis()<breakTime) {
            String content = tp.generateContent();
            client.publish(content);
            deviceTelemetry.add(tp.convertContentToSimpleString(content));

            // it's technically, no need will be when restClient.getTimeseries will working
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            cloudTelemetry.add(tp.convertTsKvEntryListToSimpleString(restClient.getLatestTimeseries(deviceId, tp.getTelemetryKeys())));
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
