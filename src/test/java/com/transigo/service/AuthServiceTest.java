package com.transigo.service;

import com.transigo.dao.UtilisateurDAO;
import com.transigo.model.Role;
import com.transigo.model.Utilisateur;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthServiceTest {

    private static final String PASSWORD = "Secret123!";
    private static final String HASH =
            BCrypt.hashpw(PASSWORD, BCrypt.gensalt(4));

    @Test
    void authenticatesValidActiveUser() {
        Utilisateur user = new Utilisateur(
                1,
                "Administrateur",
                "admin@transigo.ma",
                HASH,
                Role.ADMINISTRATEUR,
                true
        );

        AuthResult result = new AuthService(new FakeUtilisateurDAO(user, null))
                .authenticate("admin@transigo.ma", PASSWORD);

        assertEquals(AuthResult.Status.SUCCESS, result.status());
        assertSame(user, result.utilisateur());
    }

    @Test
    void rejectsUnknownEmail() {
        AuthResult result = new AuthService(new FakeUtilisateurDAO(null, null))
                .authenticate("inconnu@transigo.ma", PASSWORD);

        assertEquals(AuthResult.Status.INVALID_CREDENTIALS, result.status());
        assertNull(result.utilisateur());
    }

    @Test
    void rejectsWrongPassword() {
        Utilisateur user = new Utilisateur(
                2,
                "Gestionnaire",
                "gestionnaire@transigo.ma",
                HASH,
                Role.GESTIONNAIRE,
                true
        );

        AuthResult result = new AuthService(new FakeUtilisateurDAO(user, null))
                .authenticate("gestionnaire@transigo.ma", "MauvaisMotDePasse");

        assertEquals(AuthResult.Status.INVALID_CREDENTIALS, result.status());
    }

    @Test
    void rejectsInactiveAccountAfterPasswordCheck() {
        Utilisateur user = new Utilisateur(
                3,
                "Compte désactivé",
                "inactif@transigo.ma",
                HASH,
                Role.GESTIONNAIRE,
                false
        );

        AuthResult result = new AuthService(new FakeUtilisateurDAO(user, null))
                .authenticate("inactif@transigo.ma", PASSWORD);

        assertEquals(AuthResult.Status.INACTIVE_ACCOUNT, result.status());
    }

    @Test
    void reportsDatabaseFailureWithoutLeakingIt() {
        AuthResult result = new AuthService(
                new FakeUtilisateurDAO(null, new SQLException("connexion refusée"))
        ).authenticate("admin@transigo.ma", PASSWORD);

        assertEquals(AuthResult.Status.SERVER_ERROR, result.status());
        assertTrue(result.utilisateur() == null);
    }

    private static final class FakeUtilisateurDAO extends UtilisateurDAO {

        private final Utilisateur utilisateur;
        private final SQLException failure;

        private FakeUtilisateurDAO(Utilisateur utilisateur, SQLException failure) {
            this.utilisateur = utilisateur;
            this.failure = failure;
        }

        @Override
        public Utilisateur findByEmail(String email) throws SQLException {
            if (failure != null) {
                throw failure;
            }

            if (utilisateur == null
                    || !utilisateur.getEmail().equalsIgnoreCase(email)) {
                return null;
            }

            return utilisateur;
        }
    }
}
