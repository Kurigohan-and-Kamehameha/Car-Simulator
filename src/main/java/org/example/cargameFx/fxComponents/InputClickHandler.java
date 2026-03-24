package org.example.cargameFx.fxComponents;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class InputClickHandler implements EventHandler<MouseEvent> {
    private final String id;
    private final FXController fxController;

    public InputClickHandler(String id, FXController fxController) {
        this.id = id;
        this.fxController = fxController;
    }

    @Override
    public void handle(MouseEvent event) {
        this.fxController.setDirection(this.id);
    }

}
