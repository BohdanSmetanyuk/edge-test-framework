package org.thingsboard.edgetest.clients.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.thingsboard.edgetest.clients.Client;


// working better than http client (sometimes 1 sec break) + mqtt protocol is "lighter" than http
@Component("mqtt")
@Scope("prototype")
public class MQTTClient extends Client {

    private final static String PROTOCOL = "tcp";
    private final static String DEVICES_ME = "devices/me";

    private static String port;

    private String topic;
    private MqttClient mqttClient;

    @Override
    public void init(String hostname, String token) {

        topic = V1 + DEVICES_ME + TELEMETRY;
        if(hostname.contains("localhost")) {
            hostname = hostname.substring(0, 12);
        }
        if(port==null) {
            setMqttPort("1883"); // standart mqtt port
        }
        String broker = PROTOCOL + hostname + ":" + port;

        try {
            mqttClient = new MqttClient(broker, "MQTTClient", new MemoryPersistence());   // string value

            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setCleanSession(true);   // true or false
            mqttConnectOptions.setKeepAliveInterval(1);   // int value
            mqttConnectOptions.setUserName(token);

            mqttClient.connect(mqttConnectOptions);
        } catch (MqttException ex) {
            ex.printStackTrace();
        }
        connected = true;
        logger.info("MQTT client connected to target. MQTT port: " + port);
    }

    public static void setMqttPort(String port) {
        MQTTClient.port = port;
    }

    public static String getMqttPort() {
        return port;
    }

    @Override
    public void publish(String content) {
        try {
            isConnected();
            mqttClient.publish(topic,  new MqttMessage(content.getBytes()));
        } catch (MqttException ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    @Override
    public void disconnect() {
        try {
            mqttClient.disconnect();
        } catch (MqttException ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }
        logger.info("MQTT client disconnected from target");
    }
}
