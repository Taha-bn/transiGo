package com.transigo;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    private static final String SHOW_PASSWORD = "Afficher";
    private static final String HIDE_PASSWORD = "Masquer";

    @Override
    public void start(Stage stage) {

        // ================= Partie gauche : identité TransiGo =================
        StackPane gauche = new StackPane();
        gauche.setMinWidth(440);
        gauche.setPrefWidth(440);
        gauche.setMaxWidth(440);
        gauche.getStyleClass().add("hero");

        // Découpe le hero pour laisser les motifs décoratifs déborder proprement
        Rectangle heroClip = new Rectangle();
        heroClip.widthProperty().bind(gauche.widthProperty());
        heroClip.heightProperty().bind(gauche.heightProperty());
        gauche.setClip(heroClip);

        // Halo décoratif en haut à droite
        Region halo = new Region();
        halo.getStyleClass().add("hero-glow");
        halo.setMouseTransparent(true);

        // Tracé de ligne de transport décoratif en bas à droite
        Group route = buildRoute();
        route.setMouseTransparent(true);
        StackPane.setAlignment(route, Pos.BOTTOM_RIGHT);
        route.setTranslateX(36);
        route.setTranslateY(28);

        VBox heroContent = new VBox(22);
        heroContent.setAlignment(Pos.CENTER_LEFT);
        heroContent.setPadding(new Insets(56, 48, 56, 48));

        StackPane logoBadge = brandMark();

        Label logo = new Label("TransiGo");
        logo.setFont(Font.font("Segoe UI", FontWeight.BLACK, FontPosture.ITALIC, 40));
        logo.getStyleClass().add("hero-wordmark");

        Label brandTagline = new Label("L O G I S T I C S   &   T R A N S P O R T");
        brandTagline.getStyleClass().add("brand-tagline");

        VBox brand = new VBox(5, logo, brandTagline);
        brand.setAlignment(Pos.CENTER_LEFT);

        Label slogan = new Label(
                "Transport intelligent,\ngestion simplifiée."
        );
        slogan.getStyleClass().add("hero-tagline");
        slogan.setWrapText(true);

        Region divider = new Region();
        divider.getStyleClass().add("hero-divider");
        divider.setMinHeight(1);
        divider.setPrefHeight(1);
        divider.setMaxWidth(64);

        VBox features = new VBox(14,
                feature("Suivi des trajets en temps réel"),
                feature("Gestion de flotte simplifiée"),
                feature("Pilotage centralisé")
        );

        heroContent.getChildren().addAll(
                logoBadge, brand, slogan, divider, features
        );
        gauche.getChildren().addAll(halo, route, heroContent);

        // ================= Partie droite : formulaire de connexion =================
        StackPane droite = new StackPane();
        droite.getStyleClass().add("form-side");
        droite.setPadding(new Insets(32));
        HBox.setHgrow(droite, Priority.ALWAYS);

        Region formGlow = new Region();
        formGlow.getStyleClass().add("form-glow");
        formGlow.setMouseTransparent(true);

        VBox card = new VBox(24);
        card.getStyleClass().add("card");
        card.setPrefWidth(400);
        card.setMaxWidth(400);
        card.setMaxHeight(Region.USE_PREF_SIZE);
        card.setAlignment(Pos.TOP_LEFT);

        // --- En-tête ---
        Label chip = new Label("CONNEXION SÉCURISÉE");
        chip.getStyleClass().add("chip");
        chip.setMaxWidth(Region.USE_PREF_SIZE);

        Label titre = new Label("Bienvenue sur TransiGo");
        titre.getStyleClass().add("card-title");

        Label description = new Label(
                "Connectez-vous à votre espace professionnel"
        );
        description.getStyleClass().add("card-subtitle");
        description.setWrapText(true);

        VBox header = new VBox(10, chip, titre, description);

        // --- Champs ---
        Label emailLabel = new Label("Adresse e-mail");
        emailLabel.getStyleClass().add("field-label");

        TextField email = new TextField();
        email.setPromptText("vous@entreprise.com");
        email.getStyleClass().add("input");
        StackPane emailField = withIcon(mailIcon(), email);

        Label passwordLabel = new Label("Mot de passe");
        passwordLabel.getStyleClass().add("field-label");

        PasswordField password = new PasswordField();
        password.setPromptText("Votre mot de passe");
        password.getStyleClass().add("input");

        TextField passwordPlain = new TextField();
        passwordPlain.setPromptText("Votre mot de passe");
        passwordPlain.getStyleClass().add("input");
        passwordPlain.setVisible(false);
        passwordPlain.setManaged(false);
        passwordPlain.textProperty().bindBidirectional(password.textProperty());

        StackPane passwordField = withIcon(lockIcon(), password, passwordPlain);

        // Afficher / masquer le mot de passe (n'affecte pas la validation)
        Button togglePassword = new Button(SHOW_PASSWORD);
        togglePassword.getStyleClass().add("link-button");
        togglePassword.setFocusTraversable(false);
        togglePassword.setOnAction(event -> {
            boolean show = !passwordPlain.isVisible();

            password.setVisible(!show);
            password.setManaged(!show);
            passwordPlain.setVisible(show);
            passwordPlain.setManaged(show);

            TextField active = show ? passwordPlain : password;
            active.requestFocus();
            active.positionCaret(active.getText().length());

            togglePassword.setText(show ? HIDE_PASSWORD : SHOW_PASSWORD);
        });

        Region labelSpacer = new Region();
        HBox.setHgrow(labelSpacer, Priority.ALWAYS);

        HBox passwordHeader = new HBox(8, passwordLabel, labelSpacer, togglePassword);
        passwordHeader.setAlignment(Pos.CENTER_LEFT);

        VBox emailGroup = new VBox(7, emailLabel, emailField);
        VBox passwordGroup = new VBox(7, passwordHeader, passwordField);
        VBox fields = new VBox(16, emailGroup, passwordGroup);

        // --- Action ---
        Button connexion = new Button("Se connecter");
        connexion.getStyleClass().add("primary-button");
        connexion.setMaxWidth(Double.MAX_VALUE);

        Label message = new Label();
        message.getStyleClass().add("message");
        message.setWrapText(true);
        message.setMinHeight(18);

        FadeTransition messageFade = new FadeTransition(Duration.millis(220), message);
        messageFade.setFromValue(0);
        messageFade.setToValue(1);

        connexion.setOnAction(event -> {
            if (email.getText().isBlank()
                    || password.getText().isBlank()) {

                message.setText("Veuillez remplir tous les champs.");
                message.getStyleClass().setAll("message", "message-error");

            } else {
                message.setText(
                        "Formulaire rempli ! Connexion à venir."
                );
                message.getStyleClass().setAll("message", "message-success");
            }
            messageFade.playFromStart();
        });

        VBox actions = new VBox(12, connexion, message);

        Label footer = new Label("© 2026 TransiGo — Tous droits réservés");
        footer.getStyleClass().add("footer");

        card.getChildren().addAll(header, fields, actions, footer);

        droite.getChildren().addAll(formGlow, card);
        StackPane.setAlignment(card, Pos.CENTER);

        // ================= Organisation générale =================
        HBox root = new HBox(gauche, droite);

        Scene scene = new Scene(root, 960, 620);

        var css = Main.class.getResource("/com/transigo/login.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }

        stage.setTitle("TransiGo — Logistics & Transport");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        playIntro(heroContent, card);
    }

    /** Emblème de marque : globe + trajectoire (inspiré du logo TransiGo). */
    private StackPane brandMark() {
        Circle globe = new Circle(20, 20, 17);
        globe.setFill(null);
        globe.setStroke(Color.web("#FFFFFF", 0.95));
        globe.setStrokeWidth(2);
        globe.setStrokeLineCap(StrokeLineCap.ROUND);

        Ellipse meridian = new Ellipse(20, 20, 7.5, 17);
        meridian.setFill(null);
        meridian.setStroke(Color.web("#FFFFFF", 0.55));
        meridian.setStrokeWidth(1.4);

        Ellipse equator = new Ellipse(20, 20, 17, 6.5);
        equator.setFill(null);
        equator.setStroke(Color.web("#FFFFFF", 0.45));
        equator.setStrokeWidth(1.3);

        Path swoosh = new Path(
                new MoveTo(-1, 27),
                new CubicCurveTo(9, 38, 32, 31, 41, 13)
        );
        swoosh.setFill(null);
        swoosh.setStroke(Color.web("#BFE3FF"));
        swoosh.setStrokeWidth(3);
        swoosh.setStrokeLineCap(StrokeLineCap.ROUND);

        Group mark = new Group(globe, meridian, equator, swoosh);

        StackPane badge = new StackPane(mark);
        badge.getStyleClass().add("brand-mark");
        badge.setMinSize(64, 64);
        badge.setPrefSize(64, 64);
        badge.setMaxSize(64, 64);
        return badge;
    }

    /** Petite ligne "avantage" avec pastille de validation. */
    private HBox feature(String text) {
        Label check = new Label("✓");
        check.getStyleClass().add("feature-check");
        check.setAlignment(Pos.CENTER);
        check.setMinSize(22, 22);
        check.setPrefSize(22, 22);
        check.setMaxSize(22, 22);

        Label label = new Label(text);
        label.getStyleClass().add("feature-text");

        HBox box = new HBox(12, check, label);
        box.setAlignment(Pos.CENTER_LEFT);
        return box;
    }

    /** Champ(s) de saisie avec icône vectorielle à gauche. */
    private StackPane withIcon(SVGPath icon, TextField... fields) {
        icon.getStyleClass().add("field-icon");
        icon.setMouseTransparent(true);

        StackPane box = new StackPane();
        box.getChildren().addAll(fields);
        box.getChildren().add(icon);

        StackPane.setAlignment(icon, Pos.CENTER_LEFT);
        StackPane.setMargin(icon, new Insets(0, 0, 0, 15));

        for (TextField field : fields) {
            field.focusedProperty().addListener((obs, wasFocused, isFocused) -> {
                boolean anyFocused = false;
                for (TextField f : fields) {
                    if (f.isFocused()) {
                        anyFocused = true;
                        break;
                    }
                }
                icon.getStyleClass().remove("field-icon-active");
                if (anyFocused) {
                    icon.getStyleClass().add("field-icon-active");
                }
            });
        }

        return box;
    }

    /** Icône enveloppe (style trait). */
    private SVGPath mailIcon() {
        SVGPath icon = new SVGPath();
        icon.setContent(
                "M4 4 H20 A2 2 0 0 1 22 6 V18 A2 2 0 0 1 20 20 "
                        + "H4 A2 2 0 0 1 2 18 V6 A2 2 0 0 1 4 4 Z "
                        + "M22 6 L12 13 L2 6"
        );
        return icon;
    }

    /** Icône cadenas (style trait). */
    private SVGPath lockIcon() {
        SVGPath icon = new SVGPath();
        icon.setContent(
                "M5 11 H19 A2 2 0 0 1 21 13 V20 A2 2 0 0 1 19 22 "
                        + "H5 A2 2 0 0 1 3 20 V13 A2 2 0 0 1 5 11 Z "
                        + "M7 11 V7 A5 5 0 0 1 17 7 V11"
        );
        return icon;
    }

    /** Tracé pointillé avec arrêts lumineux et véhicule animé (motif transport). */
    private Group buildRoute() {
        Path line = new Path(
                new MoveTo(10, 150),
                new CubicCurveTo(74, 118, 44, 60, 116, 38),
                new CubicCurveTo(164, 24, 186, 12, 214, 8)
        );
        line.setFill(null);
        line.setStroke(Color.web("#38BDF8", 0.5));
        line.setStrokeWidth(2);
        line.getStrokeDashArray().setAll(2.5, 9.0);
        line.setStrokeLineCap(StrokeLineCap.ROUND);

        Circle pulse = new Circle(3.4, Color.web("#E0F2FE"));
        pulse.setStyle(
                "-fx-effect: dropshadow(gaussian, rgba(255, 255, 255, 0.95), 12, 0.5, 0, 0);"
        );

        Group group = new Group(
                line,
                routeStop(10, 150),
                routeStop(116, 38),
                routeStop(214, 8),
                pulse
        );
        group.setOpacity(0.8);

        // Petit point lumineux qui parcourt la ligne, comme un trajet en direct
        PathTransition travel = new PathTransition(Duration.seconds(7), line, pulse);
        travel.setInterpolator(Interpolator.LINEAR);
        travel.setCycleCount(Animation.INDEFINITE);
        travel.play();

        return group;
    }

    private Circle routeStop(double x, double y) {
        Circle stop = new Circle(x, y, 4.2, Color.web("#7DD3FC", 0.9));
        stop.setStyle(
                "-fx-effect: dropshadow(gaussian, rgba(56, 189, 248, 0.85), 12, 0.35, 0, 0);"
        );
        return stop;
    }

    /** Apparition douce du panneau d'identité et de la carte de connexion. */
    private void playIntro(Node hero, Node card) {
        FadeTransition heroFade = new FadeTransition(Duration.millis(650), hero);
        heroFade.setFromValue(0);
        heroFade.setToValue(1);
        heroFade.setDelay(Duration.millis(60));
        heroFade.play();

        card.setOpacity(0);
        card.setTranslateY(18);

        FadeTransition cardFade = new FadeTransition(Duration.millis(500), card);
        cardFade.setToValue(1);

        TranslateTransition cardSlide = new TranslateTransition(Duration.millis(500), card);
        cardSlide.setToY(0);

        new ParallelTransition(cardFade, cardSlide).play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
