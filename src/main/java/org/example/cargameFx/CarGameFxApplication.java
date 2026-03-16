package org.example.cargameFx;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.cargameFx.fxComponents.Gui;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CarGameFxApplication extends Application {

    public static void main(String[] args) {
        SpringApplication.run(CarGameFxApplication.class, args);
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Gui gui = new Gui();

        Group root = new Group();

        primaryStage.setScene(new Scene(root));
        primaryStage.setMinWidth(200);
        primaryStage.setMinHeight(200);
        primaryStage.show();
    }
}
