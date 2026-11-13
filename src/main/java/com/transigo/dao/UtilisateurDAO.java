package com.transigo.dao;

import com.transigo.config.DatabaseConnection;
import com.transigo.model.Role;
import com.transigo.model.Utilisateur;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

/**
 * Accès aux comptes internes de la table {@code utilisateurs}.
 */
public class UtilisateurDAO {

    private static final String COLUMNS =
            "id, nom, email, mot_de_passe_hash, role, actif";

    /**
     * Recherche un utilisateur par son adresse e-mail (insensible à la casse).
     *
     * @return l'utilisateur trouvé, ou {@code null} s'il n'existe pas
     */
    public Utilisateur findByEmail(String email) throws SQLException {
        String sql = "SELECT " + COLUMNS
                + " FROM utilisateurs"
                + " WHERE LOWER(email) = ?"
                + " LIMIT 1";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email.toLowerCase(Locale.ROOT));

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return mapUtilisateur(result);
                }
            }
        }

        return null;
    }

    private Utilisateur mapUtilisateur(ResultSet result) throws SQLException {
        String roleValue = result.getString("role");

        Role role;
        try {
            role = Role.valueOf(roleValue.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException | NullPointerException exception) {
            throw new SQLException("Rôle utilisateur invalide en base : " + roleValue, exception);
        }

        return new Utilisateur(
                result.getInt("id"),
                result.getString("nom"),
                result.getString("email"),
                result.getString("mot_de_passe_hash"),
                role,
                result.getBoolean("actif")
        );
    }
}
