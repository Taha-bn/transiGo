
package com.transigo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        // Charger l'interface Login
        URL fxmlLocation = Main.class.getResource(
                "/com/transigo/view/LoginView.fxml"
        );

        if (fxmlLocation == null) {
            throw new IOException(
                    "Fichier LoginView.fxml introuvable !"
            );
        }

        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Parent root = loader.load();

        // Créer la Scene
        Scene scene = new Scene(root, 1200, 780);

        // Charger le fichier CSS
        URL cssLocation = Main.class.getResource(
                "/com/transigo/css/login.css"
        );

        if (cssLocation == null) {
            throw new IOException(
                    "Fichier login.css introuvable !"
            );
        }

        scene.getStylesheets().add(cssLocation.toExternalForm());

        // Configurer la fenêtre
        stage.setTitle("TransiGo — Logistics & Transport");
        stage.setMinWidth(1000);
        stage.setMinHeight(650);
        stage.setScene(scene);
        stage.setResizable(true);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
