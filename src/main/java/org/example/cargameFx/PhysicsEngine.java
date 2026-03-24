package org.example.cargameFx;

import org.example.cargameFx.entity.EntityId;
import org.example.cargameFx.enums.NodeType;
import org.example.cargameFx.enums.State;
import org.example.cargameFx.graph.Edge;
import org.example.cargameFx.graph.Node;
import org.example.cargameFx.snapshot.EnergyStorageSnapshot;
import org.example.cargameFx.snapshot.PathSnapshot;
import org.example.cargameFx.snapshot.PositionSnapshot;
import org.example.cargameFx.snapshot.PositionSnapshotDriveFree;

import java.util.List;

public class PhysicsEngine {

    private final Model model;
    private boolean updateNeeded = false;
    private static final double DELTA_TIME = 0.016;

    public PhysicsEngine(Model model){
        this.model = model;
    }

    public void update() {
        for (EntityId id : model.getAllEntities()) {
            if (model.getStates().get(id).get() != State.DRIVE) continue;

            PositionSnapshot currentPosSnap = model.getPositions().get(id).getSnapshot();

            PathSnapshot pathSnap = model.getPaths().get(id).getSnapshot();
            if (pathSnap == null || pathSnap.path().isEmpty()) continue;
            List<Edge> path = pathSnap.path();

            Edge edge = (currentPosSnap.currentEdge() != null) ? currentPosSnap.currentEdge() : path.getFirst();
            double progress = (currentPosSnap.currentEdge() != null) ? currentPosSnap.edgeProgress() : 0.0;

            double speed = model.getSpeeds().get(id).getSpeed();
            double delta = speed / edge.getWeight() * DELTA_TIME;
            double newProgress = progress + delta;

            double distance = delta * edge.getWeight();

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
                    switch (target.getType()){
                        case NodeType.WORKSHOP: model.getStates().get(id).set(State.WAIT_AT_WORKSHOP); break;
                        case NodeType.INTERSECTION: model.getStates().get(id).set(State.WAIT_AT_INTERSECTION); break;
                        case NodeType.GASSTATION:
                            model.getStates().get(id).set(State.WAIT_AT_GASSTATION);
                            model.getStorage().get(id).setSnapshot(new EnergyStorageSnapshot(currentStorageSnap.capacity(), currentStorageSnap.capacity())); break;
                    }
                    continue;
                }
            }
            updateNeeded = true;
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

    public void updateDriveFree(){
        for (EntityId id : model.getAllEntities()) {
            if (State.DRIVE == model.getStates().get(id).get()) {
                PositionSnapshotDriveFree oldSnap = model.getPositionsDF().get(id).getSnapshot();

                PositionSnapshotDriveFree newSnap = new PositionSnapshotDriveFree(
                        oldSnap.x + model.getVelocities().get(id).getSnapshot().vx,
                        oldSnap.y + model.getVelocities().get(id).getSnapshot().vy
                );
                model.getPositionsDF().get(id).setSnapshot(newSnap);
            }
        }
    }


    public void notifyObservers(){
        for (EntityId id : model.getAllEntities()) {
            model.getPositions().get(id).notifyObservers();
            model.getStates().get(id).notifyObservers();
            model.getStorage().get(id).notifyObservers();
            //model.getPositionsDF().get(id).notifyObservers();

        }
    }
}
