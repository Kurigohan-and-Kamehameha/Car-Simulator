package org.example.cargameFx.fxComponents;

import javafx.scene.control.ColorPicker;
import javafx.scene.layout.BorderPane;

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
