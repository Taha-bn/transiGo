
package com.transigo.controller;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField passwordVisibleField;

    @FXML
    private Button togglePasswordButton;

    @FXML
    private Label messageLabel;

    @FXML
    private VBox loginCard;

    @FXML
    private VBox heroContent;

    // Initialisation automatique de l'interface
    @FXML
    public void initialize() {

        // Synchroniser les deux champs du mot de passe
        passwordVisibleField.textProperty()
                .bindBidirectional(passwordField.textProperty());

        // Animation d'apparition
        FadeTransition heroFade =
                new FadeTransition(Duration.millis(650), heroContent);

        heroFade.setFromValue(0);
        heroFade.setToValue(1);
        heroFade.play();

        loginCard.setOpacity(0);
        loginCard.setTranslateY(18);

        FadeTransition cardFade =
                new FadeTransition(Duration.millis(500), loginCard);

        cardFade.setFromValue(0);
        cardFade.setToValue(1);

        TranslateTransition cardSlide =
                new TranslateTransition(Duration.millis(500), loginCard);

        cardSlide.setFromY(18);
        cardSlide.setToY(0);

        new ParallelTransition(cardFade, cardSlide).play();
    }

    // Afficher / masquer le mot de passe
    @FXML
    private void togglePassword() {

        boolean show = !passwordVisibleField.isVisible();

        passwordField.setVisible(!show);
        passwordField.setManaged(!show);

        passwordVisibleField.setVisible(show);
        passwordVisibleField.setManaged(show);

        if (show) {
            passwordVisibleField.requestFocus();
            passwordVisibleField.positionCaret(
                    passwordVisibleField.getText().length()
            );
            togglePasswordButton.setText("Masquer");
        } else {
            passwordField.requestFocus();
            passwordField.positionCaret(
                    passwordField.getText().length()
            );
            togglePasswordButton.setText("Afficher");
        }
    }

    // Action du bouton Se connecter
    @FXML
    private void handleLogin() {

        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showMessage(
                    "Veuillez remplir tous les champs.",
                    "message-error"
            );
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showMessage(
                    "Veuillez saisir une adresse e-mail valide.",
                    "message-error"
            );
            return;
        }

        // Authentification temporaire :
        // La vérification PostgreSQL sera ajoutée ultérieurement.
        showMessage(
                "Formulaire rempli ! Connexion à venir.",
                "message-success"
        );
    }

    // Afficher un message avec son style
    private void showMessage(String text, String style) {

        messageLabel.setText(text);
        messageLabel.getStyleClass().setAll("message", style);

        FadeTransition fade =
                new FadeTransition(Duration.millis(220), messageLabel);

        fade.setFromValue(0);
        fade.setToValue(1);
        fade.playFromStart();
    }
}
