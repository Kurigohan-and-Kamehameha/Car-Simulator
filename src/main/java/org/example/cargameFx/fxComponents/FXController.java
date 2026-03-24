package org.example.cargameFx.fxComponents;

import org.example.cargameFx.enums.EngineType;
import org.example.cargameFx.Servicelayer;
import org.example.cargameFx.enums.State;
import org.example.cargameFx.graph.Graph;
import org.example.cargameFx.observer.ColorEngineView;
import org.example.cargameFx.observer.PositionView;
import org.example.cargameFx.observer.StateView;
import org.example.cargameFx.observer.StorageView;
import org.springframework.stereotype.Controller;

@Controller
public class FXController {
    private final Servicelayer servicelayer;
    private final ColorEngineView viewColEng;
    private final PositionView viewPos;
    private final StateView viewState;
    private final StorageView storageView;
    private final Graph graph;

    public FXController(Servicelayer servicelayer, ColorEngineView viewColEng, PositionView viewPos, StateView viewState, StorageView storageView, Graph graph){
        this.servicelayer = servicelayer;
        this.viewColEng = viewColEng;
        this.viewPos = viewPos;
        this.viewState = viewState;
        this.storageView = storageView;
        this.graph = graph;
    }

    public void stop() {
        servicelayer.stop();
    }

    public void setDirection(String id){
        servicelayer.setDirection(id);
    }

    public void setDirection(int dx, int dy) {
        servicelayer.setDirection(dx, dy);
    }

    public void setColor(String color, Runnable afterUpdate){
        servicelayer.setColor(color, afterUpdate);
    }

    public void setEngine(EngineType engineType, Runnable afterUpdate){
        servicelayer.setEngine(engineType, afterUpdate);
    }

    public double getPosX(){
        return viewPos.getPositionX();
    }

    public double getPosY(){
        return viewPos.getPositionY();
    }

    public String getColor(){
        return viewColEng.getColor();
    }

    public EngineType getEngine(){
        return viewColEng.getEngineType();
    }

    public double getPower(){
        return storageView.getPower();
    }

    public State getState(){
        return viewState.getState();
    }

    public Graph getGraph(){
        return this.graph;
    }

}
