package org.thingsboard.edgetest.black.box.cases.emulation;

import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.thingsboard.edgetest.black.box.util.Converter;
import org.thingsboard.edgetest.black.box.ws.DeviceWSClient;
import org.thingsboard.edgetest.clients.Client;
import org.thingsboard.edgetest.clients.http.HTTPClient;
import org.thingsboard.edgetest.clients.mqtt.MQTTClient;
import org.thingsboard.edgetest.black.box.util.Generator;
import org.thingsboard.rest.client.RestClient;
import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.kv.Aggregation;
import org.thingsboard.server.common.data.kv.TsKvEntry;
import org.thingsboard.server.common.data.page.TimePageLink;

import javax.annotation.PostConstruct;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@TestPropertySource(locations = {"classpath:application.properties"})
public class Emulator {

    private HTTPClient httpClient;
    private MQTTClient mqttClient;
    private RestClient restClientCloud;
    private RestClient restClientEdge;

    private Device deviceOnEdge;
    private Device deviceOnCloud;

    private DeviceWSClient wsClientCloud;
    private DeviceWSClient wsClientEdge;

    @Value("${messages.amount}")
    private int messagesAmount;

    @PostConstruct
    public void initClients() throws Exception {
        restClientCloud = new RestClient("http://localhost:8080");
        restClientCloud.login("tenant@thingsboard.org", "tenant");
        restClientEdge = new RestClient("http://localhost:19090");
        restClientEdge.login("tenant@thingsboard.org", "tenant");
        httpClient = new HTTPClient();
        mqttClient = new MQTTClient();
        deviceOnCloud = restClientCloud.getTenantDevice("Test Device").get();
        deviceOnEdge = restClientEdge.getTenantDevice("Test Device").get();
        wsClientCloud = new DeviceWSClient("localhost:8080", restClientCloud.getToken(), deviceOnCloud.getId());
        wsClientEdge = new DeviceWSClient("localhost:19090", restClientEdge.getToken(), deviceOnEdge.getId());
    }

    @Ignore
    @Test
    public void testHttpCloud() throws Exception {
        String token = restClientCloud.getDeviceCredentialsByDeviceId(deviceOnCloud.getId()).get().getCredentialsId();
        httpClient.init("http://localhost:8080", token);
        String content = Generator.generateContent();
        log.info("Http client is going to push: " + content);
        httpClient.publish(content);
        compareLatestMessages();
    }


    @Ignore
    @Test
    public void testMqttCloud() throws Exception {
        String token = restClientCloud.getDeviceCredentialsByDeviceId(deviceOnCloud.getId()).get().getCredentialsId();
        mqttClient.init("http://localhost:8080", token);
        String content = Generator.generateContent();
        log.info("Mqtt client is going to push: " + content);
        mqttClient.publish(content);
        compareLatestMessages();
    }

    @Test
    public void testHttpEdge() throws Exception {
        String token = restClientEdge.getDeviceCredentialsByDeviceId(deviceOnEdge.getId()).get().getCredentialsId();
        httpClient.init("http://localhost:19090", token);
        emulate(httpClient);
    }


    @Test
    public void testMqttEdge() throws Exception {
        String token = restClientEdge.getDeviceCredentialsByDeviceId(deviceOnEdge.getId()).get().getCredentialsId();
        MQTTClient.setMqttPort("11883");
        mqttClient.init("http://localhost:19090", token);
        emulate(mqttClient);
    }

    @Test
    public void generalComparison() throws Exception {
        Thread.sleep(1000);
        List<String> keys = Arrays.asList("key1", "key2", "key3");
        List<TsKvEntry> cloudTimeseries = restClientCloud.getTimeseries(deviceOnCloud.getId(), keys, 0L, Aggregation.NONE, new TimePageLink(100, 0L, System.currentTimeMillis()));
        List<TsKvEntry> edgeTimeseries = restClientCloud.getTimeseries(deviceOnEdge.getId(), keys, 0L, Aggregation.NONE, new TimePageLink(100, 0L, System.currentTimeMillis()));
        List<String> cloudTimeseriesAsListOfStrings = Converter.convertTsKvEntryListToSimpleStringList(cloudTimeseries, keys.size());
        List<String> edgeTimeseriesAsListOfStrings = Converter.convertTsKvEntryListToSimpleStringList(edgeTimeseries, keys.size());
        log.info("Cloud timeseries: " + cloudTimeseriesAsListOfStrings);
        log.info("Edge timeseries: " + edgeTimeseriesAsListOfStrings);
        assertThat(cloudTimeseriesAsListOfStrings).isEqualTo(edgeTimeseriesAsListOfStrings);
    }

    // @AfterClass
    private void compareLatestMessages() throws InterruptedException {
        Thread.sleep(1000);
        String latestTelemetryOnEdge = wsClientEdge.getLatestMessage();
        String latestTelemetryOnCloud = wsClientCloud.getLatestMessage();
        log.info("Latest telemetry on edge: " + latestTelemetryOnEdge);
        log.info("Latest telemetry on cloud: " + latestTelemetryOnCloud);
        assertThat(latestTelemetryOnEdge).isEqualTo(latestTelemetryOnCloud);
    }

    private void emulate(Client client) throws InterruptedException {
        for (int i=1; i<=messagesAmount; i++) {
            String content = Generator.generateContent();
            log.info("Http client is going to push: " + content);
            client.publish(content);
            compareLatestMessages();
        }
    }

}
