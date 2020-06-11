package org.thingsboard.edgetest.black.box.cases.emulation;

import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.thingsboard.edgetest.black.box.ws.DeviceWSClient;
import org.thingsboard.edgetest.clients.http.HTTPClient;
import org.thingsboard.edgetest.clients.mqtt.MQTTClient;
import org.thingsboard.edgetest.black.box.util.Generator;
import org.thingsboard.rest.client.RestClient;
import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.kv.Aggregation;
import org.thingsboard.server.common.data.kv.TsKvEntry;
import org.thingsboard.server.common.data.page.TimePageLink;

import javax.annotation.PostConstruct;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
public class Emulator {

    private HTTPClient httpClient;
    private MQTTClient mqttClient;
    private RestClient restClientCloud;
    private RestClient restClientEdge;

    private Device deviceOnEdge;
    private Device deviceOnCloud;

    private DeviceWSClient wsClientCloud;
    private DeviceWSClient wsClientEdge;

    @PostConstruct
    public void initClients() throws Exception { //
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

    @Test
    public void testHttpCloud() {
        String token = restClientCloud.getDeviceCredentialsByDeviceId(deviceOnCloud.getId()).get().getCredentialsId();
        httpClient.init("http://localhost:8080", token);
        String content = Generator.generateRandomContent();
        log.info("Http client is going to push: " + content);
        httpClient.publish(content);
    }



    @Test
    public void testMqttCloud() {
        String token = restClientCloud.getDeviceCredentialsByDeviceId(deviceOnCloud.getId()).get().getCredentialsId();
        mqttClient.init("http://localhost:8080", token);
        String content = Generator.generateRandomContent();
        log.info("Mqtt client is going to push: " + content);
        mqttClient.publish(content);
    }

    @Test
    public void testHttpEdge() {
        String token = restClientEdge.getDeviceCredentialsByDeviceId(deviceOnEdge.getId()).get().getCredentialsId();
        httpClient.init("http://localhost:19090", token);
        String content = Generator.generateRandomContent();
        log.info("Http client is going to push: " + content);
        httpClient.publish(content);
    }


    @Test
    public void testMqttEdge() {
        String token = restClientEdge.getDeviceCredentialsByDeviceId(deviceOnEdge.getId()).get().getCredentialsId();
        MQTTClient.setMqttPort("11883");
        mqttClient.init("http://localhost:19090", token);
        String content = Generator.generateRandomContent();
        log.info("Mqtt client is going to push: " + content);
        mqttClient.publish(content);
    }

    @Ignore
    @Test
    public void compare() throws Exception {
        Thread.sleep(5000);
        //System.out.println(wsClientCloud.getMessages());
        //System.out.println(wsClientEdge.getMessages());
        List<String> keys = restClientCloud.getTimeseriesKeys(deviceOnCloud.getId());
        List<TsKvEntry> tsKvEntries = restClientCloud.getTimeseries(deviceOnCloud.getId(), keys, 0L, Aggregation.NONE, new TimePageLink(100, System.currentTimeMillis()-5000, System.currentTimeMillis()));
        System.out.println(tsKvEntries);
    }

}
