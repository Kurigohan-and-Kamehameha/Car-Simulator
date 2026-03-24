package org.example.cargameFx;

import org.example.cargameFx.entity.EntityId;
import org.example.cargameFx.enums.NodeType;
import org.example.cargameFx.enums.State;
import org.example.cargameFx.graph.Edge;
import org.example.cargameFx.graph.Node;
import org.example.cargameFx.snapshot.EnergyStorageSnapshot;
import org.example.cargameFx.snapshot.PathSnapshot;
import org.example.cargameFx.snapshot.PositionSnapshot;
import org.example.cargameFx.snapshot.SpeedSnapshot;
import org.example.cargameFx.enums.MessageType;

import java.util.List;

public class PhysicsEngine {

    private final Model model;
    private static final double DELTA_TIME = 0.016;

    public PhysicsEngine(Model model) {
        this.model = model;
    }

    public void update() {
        for (EntityId id : model.getAllEntities()) {
            if (model.getStates().get(id).get() != State.DRIVE)
                continue;

            PositionSnapshot currentPosSnap = model.getPositions().get(id).getSnapshot();

            PathSnapshot pathSnap = model.getPaths().get(id).getSnapshot();
            if (pathSnap == null || pathSnap.path().isEmpty())
                continue;
            List<Edge> path = pathSnap.path();

            Edge edge = (currentPosSnap.currentEdge() != null) ? currentPosSnap.currentEdge() : path.getFirst();
            double progress = (currentPosSnap.currentEdge() != null) ? currentPosSnap.edgeProgress() : 0.0;

            SpeedSnapshot currentSpeedSnap = model.getSpeeds().get(id).getSnapshot();
            double speed = currentSpeedSnap.getSpeed();
            double potentialDelta = speed / edge.getWeight() * DELTA_TIME;

            double remainingOnEdge = 1.0 - progress;
            double actualDelta = Math.min(potentialDelta, remainingOnEdge);
            double newProgress = progress + actualDelta;

            double distance = actualDelta * edge.getWeight();

            EnergyStorageSnapshot currentStorageSnap = model.getStorage().get(id).getSnapshot();
            double newPower = Math.max(0, currentStorageSnap.power() - distance);
            model.getStorage().get(id).setSnapshot(new EnergyStorageSnapshot(newPower, currentStorageSnap.capacity()));

            int index = path.indexOf(edge);

            if (newProgress >= 1.0) {
                if (index + 1 < path.size()) {
                    edge = path.get(index + 1);
                    newProgress = 0.0;
                } else {
                    Node target = edge.getTo();

                    model.getPositions().get(id).setSnapshot(new PositionSnapshot(target));
                    switch (target.getType()) {
                        case NodeType.WORKSHOP:
                            model.getStates().get(id).set(State.WAIT_AT_WORKSHOP);
                            model.getMessages().get(id).setMessage(MessageType.WARNING, "");
                            break;
                        case NodeType.INTERSECTION:
                            model.getStates().get(id).set(State.WAIT_AT_INTERSECTION);
                            break;
                        case NodeType.GASSTATION:
                            model.getStates().get(id).set(State.WAIT_AT_GASSTATION);
                            model.getStorage().get(id).setSnapshot(new EnergyStorageSnapshot(
                                    currentStorageSnap.capacity(), currentStorageSnap.capacity()));
                            break;
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
            model.getPositions().get(id).notifyObservers();
            model.getStates().get(id).notifyObservers();
            model.getStorage().get(id).notifyObservers();
            model.getSpeeds().get(id).notifyObservers();
            model.getMessages().get(id).notifyObservers();
        }
    }
}
