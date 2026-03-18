package org.example.cargameFx.fxComponents;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import org.example.cargameFx.enums.EngineType;
import org.example.cargameFx.enums.State;
import org.springframework.stereotype.Component;

@Component
public class Gui extends AnimationTimer {

    private final BorderPane root;
    private final FXController fxController;

    private final ComboBox<String> comboBox;
    private final ColorPicker colorPicker;
    private final Circle circle;

    private boolean lastWorkshopState = false;

    public Gui(FXController fxController){
        this.fxController = fxController;
        this.root = new BorderPane();

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER_LEFT);

        circle = new Circle();
        circle.setFill(Paint.valueOf(fxController.getColor()));
        circle.setRadius(10);
        circle.setLayoutX(fxController.getPosX());
        circle.setLayoutY(fxController.getPosY());

        colorPicker = new ColorPicker();
        colorPicker.setFocusTraversable(false);
        colorPicker.setValue(Color.valueOf(fxController.getColor()));
        colorPicker.setOnAction(event -> {
            Color selectedColor = colorPicker.getValue();
            fxController.setColor(selectedColor.toString(), () ->
                    Platform.runLater(() -> circle.setFill(Paint.valueOf(fxController.getColor())))
            );
        });

        Label engineLabel = new Label();
        engineLabel.setText(fxController.getEngine().toString());
        engineLabel.getStyleClass().add("engine-label");

        comboBox = new ComboBox<>();
        comboBox.setFocusTraversable(false);
        comboBox.getItems().addAll(EngineType.FUEL.toString(), EngineType.ELECTRIC.toString());
        comboBox.setValue(fxController.getEngine().toString());
        comboBox.setOnAction(event -> {
            String selectedEngine = comboBox.getValue();
            fxController.setEngine(EngineType.fromDisplayName(selectedEngine), () ->
                    Platform.runLater(() -> engineLabel.setText(fxController.getEngine().toString()))
            );
        });

        hBox.getChildren().addAll(colorPicker, comboBox, engineLabel);
        root.setTop(hBox);
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

        boolean isWorkshop = fxController.getState() == State.WAIT_AT_WORKSHOP;

        if (isWorkshop != lastWorkshopState) {
            comboBox.setDisable(!isWorkshop);
            colorPicker.setDisable(!isWorkshop);
            lastWorkshopState = isWorkshop;
        }

    }

    public BorderPane getRoot(){
        return root;
    }

}
