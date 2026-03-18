package org.example.cargameFx;

import org.example.cargameFx.entity.EntityId;
import org.example.cargameFx.enums.State;
import org.example.cargameFx.snapshot.PositionSnapshot;

public class PhysicsEngine {

    private final Model model;

    public PhysicsEngine(Model model){
        this.model = model;
    }

    public void update(){
        for (EntityId id : model.getAllEntities()) {
            if (State.DRIVE == model.getStates().get(id).state) {
                PositionSnapshot oldSnap = model.getPositions().get(id).getSnapshot();

                PositionSnapshot newSnap = new PositionSnapshot(
                        oldSnap.x + model.getVelocities().get(id).getSnapshot().vx,
                        oldSnap.y + model.getVelocities().get(id).getSnapshot().vy
                );
                model.getPositions().get(id).setSnapshot(newSnap);
            }
        }
    }

    public void notifyObservers(){
        for (EntityId id : model.getAllEntities()) {
            if (State.DRIVE == model.getStates().get(id).state) {
                model.getPositions().get(id).notifyObservers();
            }
        }
    }
}
