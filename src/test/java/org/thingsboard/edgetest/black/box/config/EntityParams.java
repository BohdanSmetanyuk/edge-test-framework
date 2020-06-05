package org.thingsboard.edgetest.black.box.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thingsboard.edgetest.black.box.util.DataSolver;
import org.thingsboard.edgetest.black.box.common.EntityTypes;
import org.thingsboard.server.common.data.Device;
import org.thingsboard.server.common.data.HasName;
import org.thingsboard.server.common.data.asset.Asset;
import org.thingsboard.server.common.data.edge.Edge;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Getter
@Setter
public class EntityParams {

    private ObjectMapper om;
    private DataSolver ds;
    private List<Edge> edges;
    private List<Device> devices;
    private List<Asset> assets;
    private Map<HasName, HasName> relations;

    @PostConstruct
    public void init() throws Exception {
        om = new ObjectMapper();
        ds = new DataSolver();
        edges = new ArrayList<>();
        devices = new ArrayList<>();
        assets = new ArrayList<>();
        relations = new HashMap<>();
        readDevices();
        readEdges();
        readRelations();
    }

    private void readEdges() throws Exception {
        for(JsonNode edgeInJson: ds.getEdgesAsJsonNode()) {
            Edge edge = new Edge();
            edge.setName(om.treeToValue(edgeInJson.get("name"), String.class));
            edge.setType(om.treeToValue(edgeInJson.get("type"), String.class));
            edge.setLabel(om.treeToValue(edgeInJson.get("label"), String.class));
            edge.setRoutingKey(om.treeToValue(edgeInJson.get("routingKey"), String.class));
            edge.setSecret(om.treeToValue(edgeInJson.get("secret"), String.class));
            edges.add(edge);
        }
        log.info(edges.toString());
    }

    private void readDevices() throws Exception {
        for(JsonNode deviceInJson: ds.getDevicesAsJsonNode()) {
            Device device = new Device();
            device.setName(om.treeToValue(deviceInJson.get("name"), String.class));
            device.setType(om.treeToValue(deviceInJson.get("type"), String.class));
            device.setLabel(om.treeToValue(deviceInJson.get("label"), String.class));
            devices.add(device);
        }
        log.info(devices.toString());
    }

    private void readRelations() throws Exception {
        for(JsonNode relationInJson: ds.getRelationsAsJsonNode()) {
            String fromName = om.treeToValue(relationInJson.get("name"), String.class);
            EntityTypes fromType = om.treeToValue(relationInJson.get("type"), EntityTypes.class);
            JsonNode to = om.treeToValue(relationInJson.get("to"), JsonNode.class);
            String toName = om.treeToValue(to.get("name"), String.class);
            EntityTypes toType = om.treeToValue(to.get("type"), EntityTypes.class);
            HasName fromEntity = getEntity(fromName, fromType);
            HasName toEntity = getEntity(toName, toType);
            relations.put(fromEntity, toEntity);
        }
        log.info(relations.toString());
    }

    private HasName getEntity(String name, EntityTypes type) {
        HasName entity;
        switch (type) {
            case DEVICE:
                entity = findEntity(devices, name);
                break;
            case EDGE:
                entity = findEntity(edges, name);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        return entity;
    }
    
    private HasName findEntity(List<? extends HasName> entitiesList, String entityName) {
        return entitiesList.stream().filter(entity -> entityName.equals(entity.getName())).findFirst().orElse(null);
    }

}
