package org.thingsboard.edgetest.clients;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


// working better than http client (sometimes 1 sec break) + mqtt protocol is "lighter" than http
@Component("mqtt")
public class MQTTClient extends Client{

    final String PROTOCOL = "tcp";
    final String DEVICES_ME = "devices/me";

    private String topic;
    private String broker;

    private boolean isSessionCleaned = true; // true or false
    private int keepAliveInterval = 60; // int value
    private int qos = 0; // 0, 1 or 2
    private boolean retained = false; // true or false

    private String clientID = "EdgeTestMQTTClient";

    private MqttClient mqttClient;
    private MqttConnectOptions mqttConnectOptions;

    @Value("${mqtt.port}")
    private String port;

    @Override
    public void init(String hostname, String token) {

        topic = V1 + DEVICES_ME + TELEMETRY;
        broker = PROTOCOL + hostname + ":" + port;

        try {
            mqttClient = new MqttClient(broker, clientID, new MemoryPersistence());

            mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setCleanSession(isSessionCleaned);
            mqttConnectOptions.setKeepAliveInterval(keepAliveInterval);
            mqttConnectOptions.setUserName(token);

            mqttClient.connect(mqttConnectOptions); // disconnect !!!
        } catch (MqttException ex) {  // Exceptions
            ex.printStackTrace();
        }

    }

    @Override
    public void publish(String content) {

        try {
            mqttClient.publish(topic, content.getBytes(), qos, retained);
        } catch (MqttException ex) {  // Exceptions
            ex.printStackTrace();
        }
    }

    // test method
    @Override
    public String getProtocol() {
        return PROTOCOL + "\nPort: " + port;
    }
}
