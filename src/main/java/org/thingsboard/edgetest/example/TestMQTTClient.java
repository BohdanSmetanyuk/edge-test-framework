package org.thingsboard.edgetest.example;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class TestMQTTClient {

    public static void main(String[] args) {

        final String TOPIC  = "v1/devices/me/telemetry";
        String content      = "{\"Temp\":20,\"Humi\":70}";
        String broker       = "tcp://127.0.0.1:1883";
        String clientId     = "ThingsboardTestMQTTClient";

        // PROBLEMS WITH TELEMETRY

        try {
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(1);
            connOpts.setUserName("BWDTqlOGwPFnwrHey2P8");
            MqttClient sampleClient = new MqttClient(broker, clientId,  new MemoryPersistence());
            sampleClient.connect(connOpts);
            // MqttMessage message = new MqttMessage(content.getBytes());
            while(true) {
                Thread.sleep(2000);
                sampleClient.publish(TOPIC, content.getBytes(), 0, false);
                System.out.println("Message published ");
                System.out.println("Please check data in telemetry of your device on thingsboard");
            }

        } catch(MqttException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
