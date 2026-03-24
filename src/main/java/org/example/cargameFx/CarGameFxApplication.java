package org.example.cargameFx;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.cargameFx.fxComponents.FXController;
import org.example.cargameFx.fxComponents.Gui;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Objects;

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
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm()
        );
        gui.initUI(scene);
        primaryStage.setScene(scene);
        primaryStage.setWidth(1000);
        primaryStage.setHeight(800);
        primaryStage.show();

        gui.start();
    }

}
