package org.example.cargame.fxComponents;

import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;

import static javafx.scene.input.KeyCode.T;

public class Gui {

    private BorderPane borderPane = new BorderPane();

    public Gui(){
        ColorPicker colorButton = new ColorPicker();
        //ComboBox<T> comboBox = new ComboBox<>(T);


    }

    public BorderPane getUi(){
        return borderPane;
    }
}
