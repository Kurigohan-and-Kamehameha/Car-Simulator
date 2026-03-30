package org.example.cargame.persistence;

import org.example.cargame.CarModel;
import org.example.cargame.components.*;
import org.example.cargame.entity.EntityId;
import org.example.cargame.enums.EngineType;
import org.example.cargame.factories.EngineFactory;
import org.example.cargame.graph.Edge;
import org.example.cargame.graph.Graph;
import org.example.cargame.graph.Node;
import org.example.cargame.snapshot.PathSnapshot;
import org.example.cargame.snapshot.PositionSnapshot;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.*;

@Service
public class PersistenceLayerJson {
    private final CarModel model;
    private final ObjectMapper mapper = new ObjectMapper();
    private final EngineFactory engineFactory;
    private final Graph graph;
    private static final Logger logger = LoggerFactory.getLogger(PersistenceLayerJson.class);

    public PersistenceLayerJson(CarModel model, EngineFactory engineFactory, Graph graph) {
        this.model = model;
        this.engineFactory = engineFactory;
        this.graph = graph;
    }

    public void save(String folderPath){
        try {
            File file = new File(folderPath);
            SaveGame saveGame = new SaveGame();
            Map<Integer, EntityData> entities = new HashMap<>();

            for (EntityId id : model.getAllEntities()) {
                EntityData data = new EntityData();

                if (model.getStorage().containsKey(id)) {
                    data.energy = model.getStorage().get(id).getSnapshot();
                }
                if (model.getColors().containsKey(id)) {
                    data.color = model.getColors().get(id).getColor();
                }
                if (model.getEngines().containsKey(id)) {
                    data.engineType =
                            model.getEngines().get(id).getActiveEngine().getType().toString();
                }
                if (model.getSpeeds().containsKey(id)) {
                    data.speed = model.getSpeeds().get(id).getSnapshot();
                }
                if (model.getStates().containsKey(id)) {
                    data.state = model.getStates().get(id).get();
                }
                if (model.getMessages().containsKey(id)) {
                    data.messages = model.getMessages().get(id).getMessages();
                }

                if (model.getPaths().containsKey(id) && model.getPaths().get(id).getSnapshot() != null) {
                    PathSaveData pathSaveData = new PathSaveData();
                    List<Edge> path = model.getPaths().get(id).getSnapshot().path();
                    List<String> fromNodes = new ArrayList<>();
                    List<String> toNodes = new ArrayList<>();
                    for (Edge edge : path) {
                        fromNodes.add(edge.getFrom().getId());
                        toNodes.add(edge.getTo().getId());
                    }
                    pathSaveData.fromNodes = fromNodes;
                    pathSaveData.toNodes = toNodes;

                    PositionSnapshot posSnap = model.getPositions().get(id).getSnapshot();
                    if (posSnap.currentEdge() != null) {
                        pathSaveData.currentEdgeIndex = path.indexOf(posSnap.currentEdge());
                    } else {
                        pathSaveData.currentEdgeIndex = 0;
                    }
                    data.path = pathSaveData;
                }

                PositionSnapshot snap = model.getPositions().get(id).getSnapshot();
                PositionSaveData posData = new PositionSaveData();
                if (snap.currentNode() != null) {
                    posData.currentNodeId = snap.currentNode().getId();
                }
                if (snap.currentEdge() != null) {
                    posData.currentEdgeFromId = snap.currentEdge().getFrom().getId();
                    posData.currentEdgeToId = snap.currentEdge().getTo().getId();
                }
                posData.edgeProgress = snap.edgeProgress();
                posData.x = snap.x();
                posData.y = snap.y();
                data.position = posData;


                entities.put(id.getId(), data);
            }

            saveGame.entities = entities;
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, saveGame);
            System.out.println("Game saved!");
        } catch (Exception e) {
            logger.error("Failed to save game {}", folderPath, e);
        }
    }

    public void load(String folderPath){
        try {
            File file = new File(folderPath);
            if (!file.exists()) {
                System.out.println("Save file not found: " + file.getAbsolutePath());
                return;
            }

            SaveGame saveGame = mapper.readValue(file, SaveGame.class);

            model.clear();

            int maxId = 0;

            for (Map.Entry<Integer, EntityData> entry : saveGame.entities.entrySet()) {
                int rawId = entry.getKey();
                EntityId id = new EntityId(rawId);
                EntityData data = entry.getValue();

                maxId = Math.max(maxId, rawId);

                if (data.energy != null) {
                    EnergyStorageComponent comp = new EnergyStorageComponent();
                    comp.setSnapshot(data.energy);
                    model.getStorage().put(id, comp);
                }
                if (data.speed != null) {
                    SpeedComponent comp = new SpeedComponent();
                    comp.setSnapshot(data.speed);
                    model.getSpeeds().put(id, comp);
                }
                if (data.state != null) {
                    StateComponent comp = new StateComponent();
                    comp.set(data.state);
                    model.getStates().put(id, comp);
                }
                if (data.messages != null) {
                    MessageComponent comp = new MessageComponent();
                    comp.setMessages(data.messages);
                    model.getMessages().put(id, comp);
                }
                if (data.color != null) {
                    ColorComponent comp = new ColorComponent();
                    comp.setColor(data.color);
                    model.getColors().put(id, comp);
                }
                if (data.engineType != null) {
                    EngineComponent comp = engineFactory.create();
                    comp.setEngine(Objects.requireNonNull(EngineType.fromDisplayName(data.engineType)));
                    model.getEngines().put(id, comp);
                }

                if (data.path != null) {
                    PathSaveData pathSaveData = data.path;
                    List<Edge> path = new ArrayList<>();
                    for (int i = 0; i < pathSaveData.fromNodes.size(); i++) {
                        Node from = graph.getNodeById(pathSaveData.fromNodes.get(i));
                        Node to = graph.getNodeById(pathSaveData.toNodes.get(i));
                        Edge pathEdge = new Edge(from, to);
                        path.add(pathEdge);
                    }
                    PathSnapshot pathSnap = new PathSnapshot(path, pathSaveData.currentEdgeIndex);
                    PathComponent comp = new PathComponent();
                    comp.setSnapshot(pathSnap);
                    model.getPaths().put(id, comp);
                }

                Edge edge = null;
                PositionSaveData pos = data.position;
                Node node = graph.getNodeById(pos.currentNodeId);
                if (pos.currentEdgeFromId != null && pos.currentEdgeToId != null) {
                    Node from = graph.getNodeById(pos.currentEdgeFromId);
                    Node to = graph.getNodeById(pos.currentEdgeToId);
                    edge = new Edge(from, to);
                }
                PositionComponent compPos = getPositionComponent(edge, pos, node);
                model.getPositions().put(id, compPos);
            }

            model.resetNextId(maxId + 1);
            System.out.println("Game loaded!");
        } catch (Exception e) {
            logger.error("Failed to load save file from {}", folderPath, e);
        }
    }

    private static @NonNull PositionComponent getPositionComponent(Edge edge, PositionSaveData pos, Node node) {
        PositionSnapshot snapshotPos;
        if (edge != null) {
            snapshotPos = new PositionSnapshot(
                    null,       // currentNode
                    edge,
                    pos.edgeProgress,
                    pos.x,
                    pos.y
            );
        } else if (node != null) {
            snapshotPos = new PositionSnapshot(
                    node,
                    null,
                    pos.edgeProgress,
                    pos.x,
                    pos.y
            );
        } else {
            snapshotPos = new PositionSnapshot(null, null, 0, 0, 0);
        }
        PositionComponent compPos = new PositionComponent();
        compPos.setSnapshot(snapshotPos);
        return compPos;
    }
}
