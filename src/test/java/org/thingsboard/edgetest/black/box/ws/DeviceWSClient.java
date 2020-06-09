package org.thingsboard.edgetest.black.box.ws;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.thingsboard.server.common.data.EntityType;
import org.thingsboard.server.common.data.id.DeviceId;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.Random;

//latest ts
//check response
//only device

@Slf4j
public class DeviceWSClient extends WebSocketClient {

    @Getter
    private String latestMessage;

    public DeviceWSClient(String host, String jwt, DeviceId deviceId) throws URISyntaxException {
        super(new URI("ws://" + host + "/api/ws/plugins/telemetry?token=" + jwt));
        connect();
        send(getSubscriptionCommands(deviceId));
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("WebSocket client is opened");
    }

    @Override
    public void onMessage(String s) {
        try {
            String response = readResponse(s);
            latestMessage = response;
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        log.info("WebSocket client is closed with code [{}]", i);
    }

    @Override
    public void onError(Exception e) {
        log.error(e.getMessage());
    }

    private String getSubscriptionCommands(DeviceId deviceId) {
        JsonObject wsRequest = new JsonObject();
        JsonArray cmd = new JsonArray();
        JsonObject cmdsObject = new JsonObject();
        cmdsObject.addProperty("entityType", EntityType.DEVICE.name());
        cmdsObject.addProperty("entityId", deviceId.toString());
        cmdsObject.addProperty("scope", "LATEST_TELEMETRY");
        cmdsObject.addProperty("cmdId", new Random().nextInt(100));
        cmd.add(cmdsObject);
        wsRequest.add("tsSubCmds", cmd);
        return wsRequest.toString();
    }

    public String readResponse(String message) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(message);
        JsonNode data = mapper.treeToValue(jsonNode.get("data"), JsonNode.class);
        Iterator<String> iterator = data.fieldNames();
        StringBuilder content = new StringBuilder("{");
        while (iterator.hasNext()) {
            String key = iterator.next();
            int value = mapper.treeToValue(data.get(key).get(0).get(1), int.class);
            content.append("'").append(key).append("'").append(":").append(value);
            if(iterator.hasNext()) {
                content.append(",");
            }
        }
        content.append("}");
        return content.toString();
    }


}
