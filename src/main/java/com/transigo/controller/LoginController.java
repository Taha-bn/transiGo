package com.transigo.controller;

import com.transigo.model.Utilisateur;
import com.transigo.service.AuthResult;
import com.transigo.service.AuthService;
import com.transigo.util.SceneNavigator;

import javafx.animation.FadeTransition;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginController {

    private static final String MESSAGE_ERROR = "message-error";

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
    private Button loginButton;

    @FXML
    private VBox loginCard;

    @FXML
    private VBox heroContent;

    @FXML
    private HBox adminCard;

    @FXML
    private HBox managerCard;

    @FXML
    private Label adminCheck;

    @FXML
    private Label managerCheck;

    private final AuthService authService = new AuthService();

    private final Map<Label, Animation> checkAnimations = new HashMap<>();

    /**
     * Choix visuel courant. Il ne sert jamais à autoriser l'accès : il est
     * seulement comparé au rôle réel renvoyé par AuthService.
     */
    private AccountType selectedAccountType;

    @FXML
    public void initialize() {

        // Synchroniser les deux champs du mot de passe
        passwordVisibleField.textProperty()
                .bindBidirectional(passwordField.textProperty());

        setupRoleCard(adminCard, AccountType.ADMINISTRATEUR);
        setupRoleCard(managerCard, AccountType.GESTIONNAIRE);

        resetCheck(adminCheck);
        resetCheck(managerCheck);

        // Nettoyer l'ancien message dès que l'utilisateur corrige sa saisie
        emailField.textProperty().addListener(
                (observable, oldValue, newValue) -> clearMessage()
        );
        passwordField.textProperty().addListener(
                (observable, oldValue, newValue) -> clearMessage()
        );

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

    private void setupRoleCard(HBox card, AccountType accountType) {
        card.setUserData(accountType);
        card.setAccessibleRole(AccessibleRole.RADIO_BUTTON);
        card.setAccessibleText(
                accountType.getTitle()
                        + " — "
                        + accountType.getDescription()
        );
    }

    // ------------------------------------------------------------------
    // Sélection du type de compte (logique purement visuelle)
    // ------------------------------------------------------------------

    @FXML
    private void handleCardClicked(MouseEvent event) {
        AccountType accountType = accountTypeOf(event.getSource());

        if (accountType != null) {
            selectAccountType(accountType);
        }
    }

    @FXML
    private void handleCardKeyPressed(KeyEvent event) {
        if (event.getCode() != KeyCode.ENTER
                && event.getCode() != KeyCode.SPACE) {
            return;
        }

        AccountType accountType = accountTypeOf(event.getSource());

        if (accountType != null) {
            selectAccountType(accountType);
            event.consume();
        }
    }

    @FXML
    private void handleCardMouseEntered(MouseEvent event) {
        if (event.getSource() instanceof Node card) {
            animateCardScale(card, 1.015);
        }
    }

    @FXML
    private void handleCardMouseExited(MouseEvent event) {
        if (event.getSource() instanceof Node card) {
            animateCardScale(card, 1.0);
        }
    }

    private AccountType accountTypeOf(Object source) {
        if (source instanceof HBox card
                && card.getUserData() instanceof AccountType accountType) {
            return accountType;
        }

        return null;
    }

    private void selectAccountType(AccountType accountType) {
        if (selectedAccountType == accountType) {
            return;
        }

        selectedAccountType = accountType;

        updateRoleCard(
                adminCard,
                adminCheck,
                accountType == AccountType.ADMINISTRATEUR
        );
        updateRoleCard(
                managerCard,
                managerCheck,
                accountType == AccountType.GESTIONNAIRE
        );

        clearMessage();
    }

    private void updateRoleCard(HBox card, Label check, boolean selected) {
        card.getStyleClass().removeAll("selected", "unselected");
        card.getStyleClass().add(selected ? "selected" : "unselected");

        animateCardOpacity(card, selected ? 1.0 : 0.76);

        if (selected) {
            showCheck(check);
        } else {
            hideCheck(check);
        }
    }

    private void resetCheck(Label check) {
        stopCheckAnimation(check);
        check.setVisible(false);
        check.setOpacity(0);
        check.setScaleX(0.5);
        check.setScaleY(0.5);
    }

    private void showCheck(Label check) {
        stopCheckAnimation(check);

        check.setVisible(true);
        check.setOpacity(0);
        check.setScaleX(0.5);
        check.setScaleY(0.5);

        FadeTransition fade =
                new FadeTransition(Duration.millis(180), check);
        fade.setFromValue(0);
        fade.setToValue(1);

        ScaleTransition scale =
                new ScaleTransition(Duration.millis(180), check);
        scale.setFromX(0.5);
        scale.setFromY(0.5);
        scale.setToX(1);
        scale.setToY(1);
        scale.setInterpolator(Interpolator.EASE_BOTH);

        ParallelTransition animation = new ParallelTransition(fade, scale);
        checkAnimations.put(check, animation);
        animation.play();
    }

    private void hideCheck(Label check) {
        stopCheckAnimation(check);

        if (!check.isVisible()) {
            return;
        }

        FadeTransition fade =
                new FadeTransition(Duration.millis(130), check);
        fade.setToValue(0);
        fade.setOnFinished(event -> {
            checkAnimations.remove(check);
            check.setVisible(false);
        });

        checkAnimations.put(check, fade);
        fade.play();
    }

    private void stopCheckAnimation(Label check) {
        Animation animation = checkAnimations.remove(check);

        if (animation != null) {
            animation.stop();
        }
    }

    private void animateCardOpacity(Node card, double targetOpacity) {
        FadeTransition fade =
                new FadeTransition(Duration.millis(180), card);
        fade.setToValue(targetOpacity);
        fade.setInterpolator(Interpolator.EASE_BOTH);
        fade.play();
    }

    private void animateCardScale(Node card, double targetScale) {
        ScaleTransition scale =
                new ScaleTransition(Duration.millis(150), card);
        scale.setToX(targetScale);
        scale.setToY(targetScale);
        scale.setInterpolator(Interpolator.EASE_BOTH);
        scale.play();
    }

    // ------------------------------------------------------------------
    // Authentification
    // ------------------------------------------------------------------

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
                    MESSAGE_ERROR
            );
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showMessage(
                    "Veuillez saisir une adresse e-mail valide.",
                    MESSAGE_ERROR
            );
            return;
        }

        if (selectedAccountType == null) {
            showMessage(
                    "Veuillez sélectionner un type de compte.",
                    MESSAGE_ERROR
            );
            return;
        }

        setLoading(true);

        try {
            AuthResult result = authService.authenticate(email, password);

            switch (result.status()) {
                case SUCCESS -> handleAuthenticatedUser(result.utilisateur());

                case INVALID_CREDENTIALS -> showMessage(
                        "E-mail ou mot de passe incorrect.",
                        MESSAGE_ERROR
                );

                case INACTIVE_ACCOUNT -> showMessage(
                        "Ce compte est désactivé. Contactez un administrateur.",
                        MESSAGE_ERROR
                );

                case SERVER_ERROR -> showMessage(
                        "Connexion à la base de données impossible. "
                                + "Vérifiez que le serveur est démarré.",
                        MESSAGE_ERROR
                );
            }
        } finally {
            setLoading(false);
        }
    }

    private void handleAuthenticatedUser(Utilisateur utilisateur) {

        // Le rôle réel vient de la base. On refuse le tableau de bord si le
        // choix visuel ne correspond pas à ce rôle.
        if (!selectedAccountType.matches(utilisateur.getRole())) {
            showMessage(
                    "Le type de compte sélectionné ne correspond pas "
                            + "au rôle associé à cet e-mail.",
                    MESSAGE_ERROR
            );
            return;
        }

        Stage stage = (Stage) emailField.getScene().getWindow();

        try {
            SceneNavigator.showDashboard(stage, utilisateur);
        } catch (IOException exception) {
            showMessage(
                    "Impossible d'ouvrir le tableau de bord.",
                    MESSAGE_ERROR
            );
        }
    }

    private void setLoading(boolean loading) {
        loginButton.setDisable(loading);
        loginButton.setText(loading ? "Connexion…" : "Se connecter");
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

    private void clearMessage() {
        if (messageLabel.getText().isEmpty()) {
            return;
        }

        messageLabel.setText("");
        messageLabel.getStyleClass().setAll("message");
    }
}
