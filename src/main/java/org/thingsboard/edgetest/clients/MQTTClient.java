package org.thingsboard.edgetest.clients;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


// working better than http client (sometimes 1 sec break) + mqtt protocol is "lighter" than http
@Component("mqtt")
public class MQTTClient extends Client{

    private final static String PROTOCOL = "tcp";
    private final static String DEVICES_ME = "devices/me";

    private String topic;
    private MqttClient mqttClient;

    @Value("${mqtt.port}")
    private String port;

    @Override
    public void init(String hostname, String token) {

        topic = V1 + DEVICES_ME + TELEMETRY;
        if(hostname.contains("localhost")) {
            hostname = hostname.substring(0, 12);
        }
        String broker = PROTOCOL + hostname + ":" + port;

        try {
            mqttClient = new MqttClient(broker, "EdgeTestMQTTClient", new MemoryPersistence());   // string value

            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setCleanSession(true);   // true or false
            mqttConnectOptions.setKeepAliveInterval(1);   // int value
            mqttConnectOptions.setUserName(token);

            mqttClient.connect(mqttConnectOptions); // disconnect !!!
        } catch (MqttException ex) {  // Exceptions
            ex.printStackTrace();
        }

    }

    @Override
    public void publish(String content) {
        try {
            mqttClient.publish(topic,  new MqttMessage(content.getBytes()));
        } catch (MqttException ex) {  // Exceptions
            ex.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        super.disconnect();
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
