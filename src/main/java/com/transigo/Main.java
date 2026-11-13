package com.transigo;

import com.transigo.util.SceneNavigator;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        SceneNavigator.showLogin(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
