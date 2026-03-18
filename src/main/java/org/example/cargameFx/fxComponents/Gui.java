package org.example.cargameFx.fxComponents;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import org.springframework.stereotype.Component;

@Component
public class Gui extends AnimationTimer {

    private final BorderPane root;
    private final FXController fxController;

    private final Circle circle;

    public Gui(FXController fxController){
        this.fxController = fxController;
        this.root = new BorderPane();

        circle = new Circle();
        circle.setFill(Paint.valueOf(fxController.getColor()));
        circle.setRadius(10);
        circle.setLayoutX(fxController.getPosX());
        circle.setLayoutY(fxController.getPosY());

        ColorPicker colorPicker = new ColorPicker();
        colorPicker.setFocusTraversable(false);
        colorPicker.setValue(Color.valueOf(fxController.getColor()));
        colorPicker.setOnAction(event -> {
            Color selectedColor = colorPicker.getValue();
            fxController.setColor(selectedColor.toString(), () ->
                    Platform.runLater(() -> circle.setFill(Paint.valueOf(fxController.getColor())))
            );
        });

        root.setTop(colorPicker);
    }

    public void initUI(Scene scene) {
        InputHandler inputHandler = new InputHandler(fxController);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, inputHandler);
        scene.addEventHandler(KeyEvent.KEY_RELEASED, inputHandler);

        Pane pane = new Pane();
        pane.getChildren().add(circle);

        root.setCenter(pane);
    }

    @Override
    public void handle(long now) {
        circle.setLayoutX(fxController.getPosX());
        circle.setLayoutY(fxController.getPosY());

    }

    public BorderPane getRoot(){
        return root;
    }

}
