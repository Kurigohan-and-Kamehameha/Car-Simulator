package org.example.cargame.persistence;

import jakarta.transaction.Transactional;
import org.example.cargame.components.*;
import org.example.cargame.entity.EntityId;
import org.example.cargame.enums.EngineType;
import org.example.cargame.enums.MessageType;
import org.example.cargame.graph.Edge;
import org.example.cargame.graph.Graph;
import org.example.cargame.graph.Node;
import org.example.cargame.persistence.entitys.*;
import org.example.cargame.snapshot.EnergyStorageSnapshot;
import org.example.cargame.snapshot.PathSnapshot;
import org.example.cargame.snapshot.PositionSnapshot;
import org.example.cargame.snapshot.SpeedSnapshot;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class PersistenceLayerDataBase {
    private final Graph graph;
    private final StateComponentRepository stateRepo;
    private final SpeedComponentRepository speedRepo;
    private final PositionComponentRepository positionRepo;
    private final ColorComponentRepository colorRepo;
    private final EngineComponentRepository engineRepo;
    private final PathComponentRepository pathRepo;
    private final MessageComponentRepository messageRepo;
    private final EnergyStorageComponentRepository energyRepo;

    public PersistenceLayerDataBase(Graph graph, StateComponentRepository stateRepo, SpeedComponentRepository speedRepo,
                                    PositionComponentRepository positionRepo, ColorComponentRepository colorRepo,
                                    EngineComponentRepository engineRepo, PathComponentRepository pathRepo,
                                    MessageComponentRepository messageRepo, EnergyStorageComponentRepository energyRepo) {
        this.graph = graph;
        this.stateRepo = stateRepo;
        this.speedRepo = speedRepo;
        this.positionRepo = positionRepo;
        this.colorRepo = colorRepo;
        this.engineRepo = engineRepo;
        this.pathRepo = pathRepo;
        this.messageRepo = messageRepo;
        this.energyRepo = energyRepo;
    }

    @Transactional
    public void save(String folderPath, LoadedGameData data){
        data.states.forEach((id, state) -> {
            StateComponentEntity entity = new StateComponentEntity();
            entity.setId((long) id.getId());
            entity.setState(state);
            stateRepo.save(entity);
        });

        data.speeds.forEach((id, snap) -> {
            SpeedComponentEntity entity = new SpeedComponentEntity();
            entity.setId((long) id.getId());
            entity.setSpeed(snap.speed());
            speedRepo.save(entity);
        });

        data.positions.forEach((id, snap) -> {
            PositionComponentEntity entity = new PositionComponentEntity();
            entity.setId((long) id.getId());
            entity.setX(snap.x());
            entity.setY(snap.y());
            entity.setEdgeProgress(snap.edgeProgress());
            if(snap.currentEdge() != null) {
                entity.setCurrentEdgeFromId(snap.currentEdge().getFrom().getId());
                entity.setCurrentEdgeToId(snap.currentEdge().getTo().getId());
            } else if(snap.currentNode() != null) {
                entity.setCurrentNodeId(snap.currentNode().getId());
            }
            positionRepo.save(entity);
        });

        data.colors.forEach((id, color) -> {
            ColorComponentEntity entity = new ColorComponentEntity();
            entity.setId((long) id.getId());
            entity.setColor(color);
            colorRepo.save(entity);
        });

        data.engines.forEach((id, type) -> {
            EngineComponentEntity entity = new EngineComponentEntity();
            entity.setId((long) id.getId());
            entity.setEngineType(type.toString());
            engineRepo.save(entity);
        });

        data.paths.forEach((id, snap) -> {
            if (snap == null) {
                snap = new PathSnapshot(new ArrayList<>(), 0);
            }

            PathComponentEntity entity = new PathComponentEntity();
            entity.setId((long) id.getId());
            entity.setEdgeIdsFrom(
                    snap.path().stream().map(e -> e.getFrom().getId()).toList()
            );
            entity.setEdgeIdsTo(
                    snap.path().stream().map(e -> e.getTo().getId()).toList()
            );
            entity.setCurrentEdgeIndex(snap.getCurrentEdgeIndex());

            pathRepo.save(entity);
        });

        data.messages.forEach((id, msgMap) -> {
            MessageComponentEntity entity = new MessageComponentEntity();
            entity.setId((long) id.getId());
            Map<MessageType, String> filtered = new HashMap<>();
            msgMap.forEach((type, msg) -> {
                if (type == MessageType.WARNING) {
                    filtered.put(type, msg);
                }
            });
            entity.setMessages(filtered);
            messageRepo.save(entity);
        });

        data.storage.forEach((id, snap) -> {
            EnergyStorageComponentEntity entity = new EnergyStorageComponentEntity();
            entity.setId((long) id.getId());
            entity.setCapacity(snap.capacity());
            entity.setPower(snap.power());
            energyRepo.save(entity);
        });
    }

    @Transactional
    public LoadedGameData load(String folderPath) {
        if (!(stateRepo.count() > 0)) {
            return null;
        }
        LoadedGameData data = new LoadedGameData();

        speedRepo.findAll().forEach(entity -> {
            EntityId id = new EntityId(entity.getId().intValue());
            data.speeds.put(id, new SpeedSnapshot(entity.getSpeed()));
        });

        positionRepo.findAll().forEach(entity -> {
            EntityId id = new EntityId(entity.getId().intValue());

            Node from = graph.getNodeById(entity.getCurrentEdgeFromId());
            Node to = graph.getNodeById(entity.getCurrentEdgeToId());

            PositionSnapshot snapshot;

            if (from != null && to != null) {
                snapshot = new PositionSnapshot(
                        null,
                        new Edge(from, to),
                        entity.getEdgeProgress(),
                        entity.getX(),
                        entity.getY()
                );
            } else {
                snapshot = new PositionSnapshot(
                        graph.getNodeById(entity.getCurrentNodeId()),
                        null,
                        0,
                        entity.getX(),
                        entity.getY()
                );
            }

            data.positions.put(id, snapshot);
        });

        colorRepo.findAll().forEach(entity -> {
            EntityId id = new EntityId(entity.getId().intValue());
            data.colors.put(id, entity.getColor());
        });

        engineRepo.findAll().forEach(entity -> {
            EntityId id = new EntityId(entity.getId().intValue());
            data.engines.put(id, EngineType.fromDisplayName(entity.getEngineType()));
        });

        pathRepo.findAll().forEach(entity -> {
            EntityId id = new EntityId(entity.getId().intValue());

            if (entity.getEdgeIdsFrom() != null && entity.getEdgeIdsTo() != null) {
                List<String> edgeIdsFromCopy = new ArrayList<>(entity.getEdgeIdsFrom());
                List<String> edgeIdsToCopy = new ArrayList<>(entity.getEdgeIdsTo());

                List<Edge> path = new ArrayList<>();
                for (int i = 0; i < edgeIdsFromCopy.size(); i++) {
                    Node from = graph.getNodeById(edgeIdsFromCopy.get(i));
                    Node to = graph.getNodeById(edgeIdsToCopy.get(i));
                    path.add(new Edge(from, to));
                }
                data.paths.put(id, new PathSnapshot(path, entity.getCurrentEdgeIndex()));
            }
        });

        messageRepo.findAll().forEach(entity -> {
            EntityId id = new EntityId(entity.getId().intValue());
            Map<MessageType, String> messagesCopy = new HashMap<>(entity.getMessages());
            messagesCopy.putIfAbsent(MessageType.ALERT, "");
            data.messages.put(id, messagesCopy);
        });

        energyRepo.findAll().forEach(entity -> {
            EntityId id = new EntityId(entity.getId().intValue());
            data.storage.put(id, new EnergyStorageSnapshot(entity.getPower(), entity.getCapacity()));
        });

        stateRepo.findAll().forEach(entity -> {
            EntityId id = new EntityId(entity.getId().intValue());
            data.states.put(id, entity.getState());
        });

        return data;
    }
}
