package example;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class TestMQTTClient {

    public static void main(String[] args) {

        final String TOPIC  = "v1/devices/me/telemetry";
        long time = System.currentTimeMillis();
        String content      = "{'ts':" + time + ", 'values':{'humidity':100, ' temperature':100}}";
        String broker       = "tcp://localhost:1883";
        String clientId     = "ThingsboardTestMQTTClient";
        try {
            System.out.println(content);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setKeepAliveInterval(1);
            connOpts.setUserName("OoLTDrQzJ6R5mvT0cinY");
            MqttClient sampleClient = new MqttClient(broker, clientId,  new MemoryPersistence());
            sampleClient.connect(connOpts);
            // MqttMessage message = new MqttMessage(content.getBytes());
            sampleClient.publish(TOPIC, content.getBytes(), 0, false);
            System.out.println("Message published ");
            System.out.println("Please check data in telemetry of your device on thingsboard");
            sampleClient.disconnect();
        } catch(MqttException  ex) {
            ex.printStackTrace();
        }
    }
}
