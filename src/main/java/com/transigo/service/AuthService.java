package com.transigo.service;

import com.transigo.dao.UtilisateurDAO;
import com.transigo.model.Utilisateur;
import com.transigo.util.PasswordHasher;

import java.sql.SQLException;
import java.util.Objects;

/**
 * Authentification des comptes internes TransiGo.
 *
 * <p>Le service ne connaît pas le type de compte sélectionné dans l'interface.
 * Il vérifie uniquement les identifiants et renvoie l'utilisateur réel (avec
 * son rôle issu de la base). La comparaison avec le choix visuel est faite par
 * le contrôleur, après authentification.</p>
 */
public class AuthService {

    private final UtilisateurDAO utilisateurDAO;

    public AuthService() {
        this(new UtilisateurDAO());
    }

    public AuthService(UtilisateurDAO utilisateurDAO) {
        this.utilisateurDAO = Objects.requireNonNull(utilisateurDAO, "utilisateurDAO");
    }

    public AuthResult authenticate(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isEmpty()) {
            return AuthResult.failure(AuthResult.Status.INVALID_CREDENTIALS);
        }

        try {
            Utilisateur utilisateur = utilisateurDAO.findByEmail(email.trim());

            if (utilisateur == null) {
                return AuthResult.failure(AuthResult.Status.INVALID_CREDENTIALS);
            }

            if (!PasswordHasher.verify(password, utilisateur.getmotPasseHash())) {
                return AuthResult.failure(AuthResult.Status.INVALID_CREDENTIALS);
            }

            if (!utilisateur.getActif()) {
                return AuthResult.failure(AuthResult.Status.INACTIVE_ACCOUNT);
            }

            return AuthResult.success(utilisateur);

        } catch (SQLException exception) {
            System.err.println(
                    "Erreur d'authentification (base de données) : "
                            + exception.getMessage()
            );
            return AuthResult.failure(AuthResult.Status.SERVER_ERROR);
        }
    }
}
