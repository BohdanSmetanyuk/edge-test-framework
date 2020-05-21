package org.thingsboard.edgetest.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.thingsboard.edgetest.clients.Client;
import org.thingsboard.edgetest.data.common.TelemetryProfile;
import org.thingsboard.edgetest.data.comparison.ComparisonDetails;
import org.thingsboard.edgetest.data.emulation.EmulationDetails;
import org.thingsboard.rest.client.RestClient;
import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.kv.Aggregation;
import org.thingsboard.server.common.data.kv.TsKvEntry;
import org.thingsboard.server.common.data.page.TimePageLink;

import java.util.ArrayList;
import java.util.List;

public class DeviceEmulator extends Thread {

    private static final Logger logger = LogManager.getLogger(DeviceEmulator.class);

    private TelemetryProfile tp;

    private static RestClient restClientCloud;
    private static RestClient restClientEdge;
    private static String targetHostName;
    private static EmulationDetails emulationDetails;
    private static ComparisonDetails comparisonDetails;

    private List<String> deviceTelemetry;
    private List<String> cloudTelemetry;
    private List<String> edgeTelemetry;

    private Long startTs;
    private Long endTs;
    private int messagesSent;

    public DeviceEmulator(TelemetryProfile tp) {
        super(tp.getDeviceDetails().getDeviceName() + " emulator");
        this.tp = tp;

        deviceTelemetry = new ArrayList<>();
        cloudTelemetry = new ArrayList<>();
        edgeTelemetry = new ArrayList<>();
    }

    public void run() {
        pushTelemetry();
        compareTelemetry();
    }

    private void pushTelemetry() {
        Client client = emulationDetails.getClient();
        client.init(targetHostName, tp.getDeviceDetails().getAccessToken());
        logger.info("Starting push telemetry");
        messagesSent = 0;
        startTs = System.currentTimeMillis();

        switch (emulationDetails.getMode()) {
            case BY_EMULATION_TIME:
                pushTelemetryByTime(client);
                break;
            case BY_MESSAGE_AMOUNT:
                pushTelemetryByMessageAmount(client);
                break;
            default:
                throw new RuntimeException("Unrecognized emulation mode");
        }

        client.disconnect();
        logger.info("Messages pushed: " + messagesSent);
        endTs = System.currentTimeMillis();
        logger.info("All messages pushed successfully");
    }

    private void pushTelemetryByTime(Client client) {
        logger.info("Pushing telemetry by emulation time");
        long breakTime = System.currentTimeMillis()+emulationDetails.getEmulationTime();
        while(System.currentTimeMillis()<breakTime) {
            String content = tp.generateContent();
            client.publish(content);
            deviceTelemetry.add(Converter.convertContentToSimpleString(content));
            messagesSent++;
            try {
                Thread.sleep(tp.getPublishFrequencyInMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void pushTelemetryByMessageAmount(Client client) {
        logger.info("Pushing telemetry by message amount");
        int messageAmount = emulationDetails.getMessageAmount();
        logger.info("Going to push: " + messageAmount + " messages");
        while(messagesSent < messageAmount) {
            String content = tp.generateContent(System.currentTimeMillis());
            client.publish(content);
            deviceTelemetry.add(Converter.convertContentWithTsToSimpleString(content));
            messagesSent++;

            try {
                Thread.sleep(10); // one hundred messages per second
                if(messagesSent%100==0) {   // than wait 100 millis
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    private List<String> getTimeseries(RestClient restClient, String deviceName, List<String> keys) {
        TimePageLink timePageLink = new TimePageLink(messagesSent, startTs, endTs);
        Device device = restClient.getTenantDevice(deviceName).get();
        List<TsKvEntry> timeseries = restClient.getTimeseries(device.getId(), keys, 0L, Aggregation.NONE, timePageLink);
        return Converter.convertTsKvEntryListToSimpleStringList(timeseries, keys.size());
    }

    private void compareTelemetry() {
        logger.info("Starting compare telemetry");
        logger.info("Attempts: " + comparisonDetails.getAttempts() + " , delay: " + comparisonDetails.getDelay());
        int attempts = comparisonDetails.getAttempts();
        for(int i=1; i<=attempts; i++) {
            logger.info("Attempt #" + i);
            cloudTelemetry = getTimeseries(restClientCloud, tp.getDeviceDetails().getDeviceName(), tp.getTelemetryKeys());
            edgeTelemetry = getTimeseries(restClientEdge, tp.getDeviceDetails().getDeviceName(), tp.getTelemetryKeys());
            boolean isTelemetryEquals = deviceTelemetry.equals(cloudTelemetry) && cloudTelemetry.equals(edgeTelemetry); // it's transitive
            if (isTelemetryEquals) {
                logger.info(this.getName() + ": all telemetry equals");
                if (i < attempts) {
                    logger.info((attempts - i) + " attempts will be passed");
                }
                break;
            } else {
                logger.info(this.getName() + ": telemetry not equals");
                endTs = System.currentTimeMillis();
                if (i < attempts) {
                    try {
                        Thread.sleep(comparisonDetails.getDelay());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    showComparisonFailureDetails(); // for debug
                }
            }
        }
    }

    private void showComparisonFailureDetails() { //
        logger.info("Data size: " + "device: " + deviceTelemetry.size() + " cloud: " + cloudTelemetry.size() + " edge: "  + edgeTelemetry.size());
        logger.info("First value: " + "device: " + deviceTelemetry.get(0) + " cloud: " + cloudTelemetry.get(0) + " edge: "  + edgeTelemetry.get(0));
        logger.info("Last value: " + "device: " + deviceTelemetry.get(deviceTelemetry.size()-1) + " cloud: " + cloudTelemetry.get(cloudTelemetry.size()-1) + " edge: "  + edgeTelemetry.get(edgeTelemetry.size()-1));
    }

    static public void setEmulator(RestClient restClientCloud, RestClient restClientEdge, String targetHostName, EmulationDetails emulationDetails, ComparisonDetails comparisonDetails) {
        DeviceEmulator.restClientCloud = restClientCloud;
        DeviceEmulator.restClientEdge = restClientEdge;
        DeviceEmulator.targetHostName = targetHostName;
        DeviceEmulator.emulationDetails = emulationDetails;
        DeviceEmulator.comparisonDetails = comparisonDetails;
    }

}
