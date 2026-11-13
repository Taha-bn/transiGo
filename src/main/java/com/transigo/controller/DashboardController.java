package com.transigo.controller;

import com.transigo.model.Role;
import com.transigo.model.Utilisateur;
import com.transigo.util.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Contrôleur commun aux tableaux de bord administrateur et gestionnaire.
 * Le rôle affiché provient exclusivement de l'utilisateur authentifié.
 */
public class DashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label subtitleLabel;

    @FXML
    private Label userNameLabel;

    @FXML
    private Label userEmailLabel;

    @FXML
    private Label roleBadge;

    @FXML
    public void initialize() {
        // Les données sont injectées après le chargement via setUtilisateur().
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        if (utilisateur == null) {
            return;
        }

        welcomeLabel.setText("Bienvenue, " + utilisateur.getNom());
        userNameLabel.setText(utilisateur.getNom());
        userEmailLabel.setText(utilisateur.getEmail());

        roleBadge.setText(
                utilisateur.getRole() == Role.ADMINISTRATEUR
                        ? "Administrateur"
                        : "Gestionnaire de transport"
        );
    }

    @FXML
    private void handleLogout() {
        try {
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            SceneNavigator.showLogin(stage);
        } catch (IOException exception) {
            subtitleLabel.setText(
                    "Impossible de revenir à l'écran de connexion."
            );
        }
    }
}
