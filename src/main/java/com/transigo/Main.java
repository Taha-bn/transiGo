
package com.transigo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        // Partie gauche : identité TransiGo
        VBox gauche = new VBox(20);
        gauche.setAlignment(Pos.CENTER);
        gauche.setPadding(new Insets(40));
        gauche.setPrefWidth(350);
        gauche.setStyle("-fx-background-color: #142D4E;");

        Label logo = new Label("TransiGo");
        logo.setFont(Font.font("Arial", 34));
        logo.setTextFill(Color.WHITE);

        Label slogan = new Label(
                "Transport intelligent,\ngestion simplifiée."
        );
        slogan.setFont(Font.font("Arial", 17));
        slogan.setTextFill(Color.WHITE);
        slogan.setAlignment(Pos.CENTER);

        gauche.getChildren().addAll(logo, slogan);

        // Partie droite : formulaire de connexion
        VBox droite = new VBox(15);
        droite.setAlignment(Pos.CENTER);
        droite.setPadding(new Insets(45));
        droite.setPrefWidth(450);
        droite.setStyle("-fx-background-color: white;");

        Label titre = new Label("Bienvenue sur TransiGo");
        titre.setFont(Font.font("Arial", 25));
        titre.setStyle("-fx-font-weight: bold;");

        Label description = new Label(
                "Connectez-vous à votre espace professionnel"
        );

        TextField email = new TextField();
        email.setPromptText("Email professionnel");
        email.setPrefHeight(42);

        PasswordField password = new PasswordField();
        password.setPromptText("Mot de passe");
        password.setPrefHeight(42);

        Button connexion = new Button("Se connecter");
        connexion.setMaxWidth(Double.MAX_VALUE);
        connexion.setPrefHeight(45);
        connexion.setStyle(
                "-fx-background-color: #F28C28;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-cursor: hand;"
        );

        Label message = new Label();

        connexion.setOnAction(event -> {
            if (email.getText().isBlank()
                    || password.getText().isBlank()) {

                message.setText("Veuillez remplir tous les champs.");
                message.setStyle("-fx-text-fill: red;");

            } else {
                message.setText(
                        "Formulaire rempli ! Connexion à venir."
                );
                message.setStyle("-fx-text-fill: green;");
            }
        });

        droite.getChildren().addAll(
                titre, description,
                email, password,
                connexion, message
        );

        // Organisation générale
        HBox root = new HBox(gauche, droite);

        Scene scene = new Scene(root, 800, 500);

        stage.setTitle("TransiGo - Connexion");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
