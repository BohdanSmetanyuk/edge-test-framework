package org.thingsboard.edgetest.black.box.cases.emulation;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thingsboard.edgetest.black.box.util.Converter;
import org.thingsboard.edgetest.black.box.ws.DeviceWSClient;
import org.thingsboard.edgetest.clients.http.HTTPClient;
import org.thingsboard.edgetest.clients.mqtt.MQTTClient;
import org.thingsboard.edgetest.black.box.config.CloudParams;
import org.thingsboard.edgetest.black.box.util.Generator;
import org.thingsboard.rest.client.RestClient;
import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.kv.TsKvEntry;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {CloudParams.class})
public class TestEmulation {

    private HTTPClient httpClient;
    private MQTTClient mqttClient;
    private RestClient restClient;

    private String hostName;

    private String currentContent;

    @Autowired
    public void setCloudParams(CloudParams params) {
        httpClient = new HTTPClient();
        mqttClient = new MQTTClient();
        hostName = params.getHost();
        restClient = new RestClient(hostName);
        restClient.login(params.getUsername(), params.getPassword());
    }

    @Test
    public void testHttp() throws Exception{
        log.info("Testing http");
        Device device = restClient.getTenantDevice("Test device").get();
        String token = restClient.getDeviceCredentialsByDeviceId(device.getId()).get().getCredentialsId();
        httpClient.init(hostName, token);
        String content = Generator.generateRandomContent();
        log.info("Http client is going to push: " + content);
        httpClient.publish(content);

        DeviceWSClient wsClient = new DeviceWSClient("localhost:8080", restClient.getToken(), device.getId());

        Thread.sleep(500);
        log.info("Latest timeseries is: " + wsClient.getLatestMessage());

        List<String> keys = restClient.getTimeseriesKeys(device.getId());
        List<TsKvEntry> latestTimeseries = restClient.getLatestTimeseries(device.getId(), keys);
        log.info("Latest timeseries is: " + Converter.tsKvEntryToString(latestTimeseries));
    }



    @Test
    public void testMqtt() {
        log.info("Testing mqtt");
        Device device = restClient.getTenantDevice("Test device").get();
        assertThat(device.getName()).isNotNull();
        String token = restClient.getDeviceCredentialsByDeviceId(device.getId()).get().getCredentialsId();
        mqttClient.init(hostName, token);
        String content = Generator.generateRandomContent();
        log.info("Mqtt client is going to push: " + content);
        mqttClient.publish(content);
        List<String> keys = restClient.getTimeseriesKeys(device.getId());
        List<TsKvEntry> latestTimeseries = restClient.getLatestTimeseries(device.getId(), keys);
        log.info("Latest timeseries is: " + Converter.tsKvEntryToString(latestTimeseries));
    }
}
