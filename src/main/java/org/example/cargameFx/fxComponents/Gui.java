package org.example.cargameFx.fxComponents;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import org.example.cargameFx.enums.EngineType;
import org.example.cargameFx.enums.NodeType;
import org.example.cargameFx.enums.State;
import org.example.cargameFx.graph.Graph;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Gui extends AnimationTimer {

    private final BorderPane root;
    private final FXController fxController;

    private final ComboBox<String> comboBox;
    private final ColorPicker colorPicker;
    private final Circle circle;
    private final Label powerLabel;
//    ImageView imageView;
    private boolean lastWorkshopState = false;

    public Gui(FXController fxController){
        this.fxController = fxController;
        this.root = new BorderPane();
/*
        Image gif = new Image(getClass().getResource("/gif/piggy.gif").toExternalForm());
        imageView = new ImageView(gif);
        imageView.setFitWidth(50);
        imageView.setPreserveRatio(true);
        imageView.setTranslateX(fxController.getPosX());
        imageView.setTranslateY(fxController.getPosY()-10);
*/
        circle = new Circle();
        circle.setFill(Paint.valueOf(fxController.getColor()));
        circle.setRadius(16);
        this.circle.setLayoutX(fxController.getPosX());
        this.circle.setLayoutY(fxController.getPosY());

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
        engineLabel.setText("Active Engine: ");
        engineLabel.getStyleClass().add("label");
        Label storageLabel = new Label();
        storageLabel.setText("Power: ");
        storageLabel.getStyleClass().add("label");

        Label engineTypeLabel = new Label();
        engineTypeLabel.setText(fxController.getEngine().toString());
        engineTypeLabel.getStyleClass().add("engineType-label");

        comboBox = new ComboBox<>();
        comboBox.setFocusTraversable(false);
        comboBox.getItems().addAll(EngineType.FUEL.toString(), EngineType.ELECTRIC.toString());
        comboBox.setValue(fxController.getEngine().toString());
        comboBox.setOnAction(event -> {
            String selectedEngine = comboBox.getValue();
            fxController.setEngine(EngineType.fromDisplayName(selectedEngine), () ->
                    Platform.runLater(() -> engineTypeLabel.setText(fxController.getEngine().toString()))
            );
        });

        powerLabel = new Label();
        powerLabel.setText(String.valueOf(fxController.getPower()));
        powerLabel.getStyleClass().add("power-label");

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setAlignment(Pos.CENTER_LEFT);

        hBox.getChildren().addAll(colorPicker, comboBox, engineLabel, engineTypeLabel, storageLabel, powerLabel);
        root.setTop(hBox);
    }

    public void initUI(Scene scene) {
        //InputKeyHandler inputHandler = new InputKeyHandler(fxController);
        //scene.addEventHandler(KeyEvent.KEY_PRESSED, inputHandler);
        //scene.addEventHandler(KeyEvent.KEY_RELEASED, inputHandler);

        Graph graph = fxController.getGraph();
        List<Circle> circleNodes = new ArrayList<>();
        graph.getNodes().forEach(node -> {
            Circle circleNode = new Circle(node.getX(), node.getY(), 20);
            circleNode.setOnMouseClicked(new InputClickHandler(node.getId(), fxController));
            circleNode.setOnMouseEntered(e -> circleNode.setCursor(Cursor.HAND));
            circleNode.setOnMouseExited(e -> circleNode.setCursor(Cursor.DEFAULT));
            if (node.getType() == NodeType.WORKSHOP) {
                circleNode.setFill(Paint.valueOf("#00FF00"));
            }
            if (node.getType() == NodeType.GASSTATION) {
                circleNode.setFill(Paint.valueOf("#FFFF00"));
            }
            circleNodes.add(circleNode);
        });

        List<Line> lines = new ArrayList<>();
        graph.getEdges().forEach(edge -> {
            Line line = new Line(edge.getFrom().getX(), edge.getFrom().getY(), edge.getTo().getX(), edge.getTo().getY());
            line.setStrokeWidth(2);
            lines.add(line);
        });

        Pane pane = new Pane();
        pane.getChildren().addAll(lines);
        pane.getChildren().addAll(circleNodes);
        pane.getChildren().add(circle);

        root.setCenter(pane);
    }

    @Override
    public void handle(long now) {
        circle.setLayoutX(fxController.getPosX());
        circle.setLayoutY(fxController.getPosY());
/*
        imageView.setTranslateX(fxController.getPosX());
        imageView.setTranslateY(fxController.getPosY()-10);
        imageView.setTranslateX(imageView.getTranslateX() - imageView.getBoundsInLocal().getWidth() / 2);
        imageView.setTranslateY(imageView.getTranslateY() - imageView.getBoundsInLocal().getHeight() / 2);
*/
        powerLabel.setText(String.valueOf(fxController.getPower()));

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
