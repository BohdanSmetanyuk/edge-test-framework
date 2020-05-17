package org.thingsboard.edgetest.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.thingsboard.edgetest.clients.mqtt.MQTTClient;
import org.thingsboard.edgetest.data.comparison.ComparisonDetails;
import org.thingsboard.edgetest.data.emulation.EmulationDetails;
import org.thingsboard.edgetest.data.host.HostDetails;
import org.thingsboard.edgetest.data.host.cloud.CloudDetails;
import org.thingsboard.edgetest.data.host.edge.EdgeDetails;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ApplicationConfig implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${general}")
    private String general;

    @Value("${additional}")
    private String additional;

    private static final Logger logger = LogManager.getLogger(ApplicationConfig.class);

    private Map<String, String> params;
    private String description;

    @PostConstruct
    private void construct() {
        params = new HashMap<>();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("Initializing configuration");
        initConfig(general);
        initConfig(additional);
        //viewConfigurationParams(); // for debug
        logger.info("Configuration successfully initialized");
    }

    private void initConfig(String filename) {
        String[] keyAndValue;
        try {
            File file = new File(filename);
            logger.info("Reading " + file.getName());
            FileReader fileReader = new FileReader(file.getAbsolutePath());
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                if(line.isEmpty() || line.contains("#")) {
                    line = bufferedReader.readLine();
                    continue;
                } else if (!line.contains("=")) {
                    throw new RuntimeException("Illegal configuration file\nPlease rewrite your .conf file in next form: key=value...");
                } else {
                    keyAndValue = line.split("=");
                    if (keyAndValue[0].equals("description")) {
                        description = keyAndValue[1];
                    } else {
                        params.put(keyAndValue[0], keyAndValue[1]);
                    }
                    line = bufferedReader.readLine();
                }
            }
        } catch (IOException ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void viewConfigurationParams() {
        logger.info("Configuration params:\n" + params);
    }

    public void getDescription() {
        if(description==null) {
            logger.info("No description");
        } else {
            logger.info("Description:\n" + description);
        }
    }

    public String getValue(String key) throws RuntimeException{
        String value = params.get(key);
        if (value == null) {
            throw new RuntimeException("Unrecognized param: " + key);
        }
        return value;
    }

    public HostDetails getTargetDetails(String target) {
        if(target.equals("cloud")) {
            return getCloudDetails();
        } else if(target.equals("edge")) {
            return getEdgeDetails();
        } else {
            throw new RuntimeException("Unrecognized target: " + target);
        }
    }

    public CloudDetails getCloudDetails() {
        return new CloudDetails(params.get("cloud.host.name"), params.get("cloud.user.username"), params.get("cloud.user.password"));
    }

    public EdgeDetails getEdgeDetails () {
        return new EdgeDetails(params.get("edge.host.name"), params.get("edge.user.username"), params.get("edge.user.password"));
    }

    public EmulationDetails getEmulationDetails() {
        EmulationDetails emulationDetails = new EmulationDetails(params.get("telemetry.send.protocol"), Long.parseLong(params.get("emulation.time")));
        if (params.containsKey("mqtt.port")) {
            emulationDetails.setMqttPort(params.get("mqtt.port"));
        }
        if(emulationDetails.getTelemetrySendProtocol().equals("mqtt") && emulationDetails.getMqttPort()!=null) {
            if(MQTTClient.getMqttPort()==null) {
                MQTTClient.setMqttPort(emulationDetails.getMqttPort());
            }
        }
        return emulationDetails;
    }

    public ComparisonDetails getComparisonDetails() {
        int attempts = params.get("attempts") == null ? 1 : Integer.parseInt(params.get("attempts"));
        long delay = params.get("delay") == null ? 0L : Long.parseLong(params.get("delay"));
        return new ComparisonDetails(attempts, delay);
    }
}