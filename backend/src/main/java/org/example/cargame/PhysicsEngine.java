package org.example.cargame;

import org.example.cargame.entity.EntityId;
import org.example.cargame.enums.NodeType;
import org.example.cargame.enums.State;
import org.example.cargame.graph.Edge;
import org.example.cargame.graph.Node;
import org.example.cargame.snapshot.EnergyStorageSnapshot;
import org.example.cargame.snapshot.PathSnapshot;
import org.example.cargame.snapshot.PositionSnapshot;
import org.example.cargame.snapshot.SpeedSnapshot;
import org.example.cargame.enums.MessageType;

import java.util.List;

public class PhysicsEngine {

    private final CarModel model;
    private static final double DELTA_TIME = 0.016;

    public PhysicsEngine(CarModel model) {
        this.model = model;
    }

    public void update() {
        for (EntityId id : model.getAllEntities()) {
            if (model.getStates().get(id).get() != State.DRIVE) {
                continue;
            }
            PositionSnapshot currentPosSnap = model.getPositions().get(id).getSnapshot();
            PathSnapshot pathSnap = model.getPaths().get(id).getSnapshot();
            if (pathSnap == null || pathSnap.path().isEmpty())
                continue;

            int currentIndex = pathSnap.getCurrentEdgeIndex();
            List<Edge> path = pathSnap.path();

            Edge edge;
            double progress;

            if (currentPosSnap.currentEdge() != null) {
                edge = currentPosSnap.currentEdge();
                progress = currentPosSnap.edgeProgress();
            } else {
                edge = path.get(currentIndex);
                progress = 0.0;
            }

            SpeedSnapshot currentSpeedSnap = model.getSpeeds().get(id).getSnapshot();
            double speed = currentSpeedSnap.speed();
            double potentialDelta = speed / edge.getWeight() * DELTA_TIME;

            double remainingOnEdge = 1.0 - progress;
            double actualDelta = Math.min(potentialDelta, remainingOnEdge);
            double newProgress = progress + actualDelta;

            double distance = actualDelta * edge.getWeight();
            EnergyStorageSnapshot currentStorageSnap = model.getStorage().get(id).getSnapshot();
            double newPower = Math.max(0, currentStorageSnap.power() - distance);
            model.getStorage().get(id).setSnapshot(new EnergyStorageSnapshot(newPower, currentStorageSnap.capacity()));

            if (newProgress >= 1.0) {
                currentIndex++;
                if (currentIndex < path.size()) {
                    edge = path.get(currentIndex);
                    if(edge.getFrom().getType().equals(NodeType.GASSTATION)) {
                        EnergyStorageSnapshot snap = model.getStorage().get(id).getSnapshot();
                        model.getStorage().get(id).setSnapshot(new EnergyStorageSnapshot(snap.capacity(), snap.capacity()));
                    }
                    newProgress = 0.0;

                    pathSnap = new PathSnapshot(path, currentIndex);
                    model.getPaths().get(id).setSnapshot(pathSnap);

                } else {
                    Node target = edge.getTo();
                    model.getPositions().get(id).setSnapshot(new PositionSnapshot(target));

                    switch (target.getType()) {
                        case WORKSHOP -> {
                            model.getStates().get(id).set(State.WAIT_AT_WORKSHOP);
                            model.getMessages().get(id).setMessage(MessageType.WARNING, "");
                        }
                        case INTERSECTION -> model.getStates().get(id).set(State.WAIT_AT_INTERSECTION);
                        case GASSTATION -> {
                            model.getStates().get(id).set(State.WAIT_AT_GASSTATION);
                            model.getStorage().get(id).setSnapshot(new EnergyStorageSnapshot(
                                    currentStorageSnap.capacity(), currentStorageSnap.capacity()));
                        }
                    }
                    continue;
                }
            }
            double x = toX(newProgress, edge);
            double y = toY(newProgress, edge);
            model.getPositions().get(id).setSnapshot(new PositionSnapshot(null, edge, newProgress, x, y));
        }
    }

    private double toX(double edgeProgress, Edge edge) {
        return edge.getFrom().getX() +
                (edge.getTo().getX() - edge.getFrom().getX()) * edgeProgress;
    }

    private double toY(double edgeProgress, Edge edge) {
        return edge.getFrom().getY() +
                (edge.getTo().getY() - edge.getFrom().getY()) * edgeProgress;
    }

    public void notifyObservers() {

        for (EntityId id : model.getAllEntities()) {
            model.getPositions().get(id).notifyObservers(id);
            model.getStates().get(id).notifyObservers(id);
            model.getSpeeds().get(id).notifyObservers(id);
            model.getMessages().get(id).notifyObservers(id);
            model.getStorage().get(id).notifyObservers(id);
        }
    }
}
