package com.transigo.util;

import com.transigo.controller.DashboardController;
import com.transigo.model.Role;
import com.transigo.model.Utilisateur;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Centralise le chargement des vues et le passage d'une scène à l'autre.
 */
public final class SceneNavigator {

    private static final String LOGIN_VIEW =
            "/com/transigo/view/LoginView.fxml";
    private static final String LOGIN_STYLESHEET =
            "/com/transigo/css/login.css";

    private static final String ADMIN_DASHBOARD_VIEW =
            "/com/transigo/view/AdminDashboardView.fxml";
    private static final String MANAGER_DASHBOARD_VIEW =
            "/com/transigo/view/GestionnaireDashboardView.fxml";
    private static final String DASHBOARD_STYLESHEET =
            "/com/transigo/css/dashboard.css";

    private static final double SCENE_WIDTH = 1200;
    private static final double SCENE_HEIGHT = 780;

    private SceneNavigator() {
        // Classe utilitaire
    }

    public static void showLogin(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(requireResource(LOGIN_VIEW));
        Parent root = loader.load();

        applyScene(
                stage,
                root,
                "TransiGo — Logistics & Transport",
                LOGIN_STYLESHEET
        );
    }

    public static void showDashboard(Stage stage, Utilisateur utilisateur)
            throws IOException {

        boolean isAdmin = utilisateur.getRole() == Role.ADMINISTRATEUR;

        String view = isAdmin
                ? ADMIN_DASHBOARD_VIEW
                : MANAGER_DASHBOARD_VIEW;

        String title = isAdmin
                ? "TransiGo — Tableau de bord administrateur"
                : "TransiGo — Tableau de bord gestionnaire";

        FXMLLoader loader = new FXMLLoader(requireResource(view));
        Parent root = loader.load();

        DashboardController controller = loader.getController();
        controller.setUtilisateur(utilisateur);

        applyScene(stage, root, title, DASHBOARD_STYLESHEET);
    }

    private static void applyScene(
            Stage stage,
            Parent root,
            String title,
            String stylesheet
    ) throws IOException {

        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        scene.getStylesheets().add(
                requireResource(stylesheet).toExternalForm()
        );

        stage.setTitle(title);
        stage.setMinWidth(1000);
        stage.setMinHeight(650);
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
    }

    private static URL requireResource(String path) throws IOException {
        URL resource = SceneNavigator.class.getResource(path);

        if (resource == null) {
            throw new IOException("Ressource introuvable : " + path);
        }

        return resource;
    }
}
