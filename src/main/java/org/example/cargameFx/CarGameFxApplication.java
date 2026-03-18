package org.example.cargameFx;

import jakarta.annotation.PostConstruct;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.example.cargameFx.fxComponents.FXController;
import org.example.cargameFx.fxComponents.Gui;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CarGameFxApplication extends Application {

    private ConfigurableApplicationContext context;
    private FXController fxController;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void init() {
        context = SpringApplication.run(CarGameFxApplication.class);
    }

    @Override
    public void stop() {
        context.close();
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Gui gui = context.getBean(Gui.class);
        Group root = new Group(gui.getRoot());
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.setWidth(500);
        primaryStage.setHeight(500);
        primaryStage.show();

        gui.initUI(scene);
        gui.start();

    }
}
