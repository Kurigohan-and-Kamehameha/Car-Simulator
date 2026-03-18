package org.example.cargameFx.fxComponents;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.HashSet;
import java.util.Set;

public class InputHandler implements EventHandler<KeyEvent> {

    private final Set<KeyCode> pressedKeys = new HashSet<>();
    private final FXController fxController;

    public InputHandler(FXController fxController) {
        this.fxController = fxController;
    }

    public Set<KeyCode> getPressedKeys() {
        return pressedKeys;
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
            pressedKeys.add(event.getCode());
        } else if (event.getEventType() == KeyEvent.KEY_RELEASED) {
            pressedKeys.remove(event.getCode());
        }

        updateDirection();
    }

    private void updateDirection() {
        int dx = 0, dy = 0;
        if (pressedKeys.contains(KeyCode.UP)) dy -= 1;
        if (pressedKeys.contains(KeyCode.DOWN)) dy += 1;
        if (pressedKeys.contains(KeyCode.LEFT)) dx -= 1;
        if (pressedKeys.contains(KeyCode.RIGHT)) dx += 1;

        if (dx != 0 || dy != 0) {
            fxController.setDirection(dx, dy); // ggf. Richtung + Speed in Controller
        } else {
            fxController.stop();
        }
    }

    public boolean isPressed(KeyCode code) {
        return pressedKeys.contains(code);
    }
}
