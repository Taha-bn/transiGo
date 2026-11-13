package com.transigo.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Vérification des mots de passe stockés dans la colonne
 * {@code utilisateurs.mot_de_passe_hash}.
 *
 * <p>Seuls les hachages BCrypt sont acceptés. Aucun mot de passe en clair
 * n'est comparé, et aucun mot de passe n'est journalisé.</p>
 */
public final class PasswordHasher {

    private PasswordHasher() {
        // Classe utilitaire
    }

    public static boolean verify(String plainPassword, String storedHash) {
        if (plainPassword == null || storedHash == null || storedHash.isBlank()) {
            return false;
        }

        if (!storedHash.startsWith("$2")) {
            return false;
        }

        try {
            return BCrypt.checkpw(plainPassword, normalizePrefix(storedHash));
        } catch (RuntimeException exception) {
            // Hachage mal formé : on refuse la connexion.
            return false;
        }
    }

    /**
     * jBCrypt 0.4 ne reconnaît que le préfixe {@code $2a$}. Les variantes
     * {@code $2b$} et {@code $2y$} sont compatibles au niveau de l'algorithme.
     */
    private static String normalizePrefix(String hash) {
        if (hash.startsWith("$2b$") || hash.startsWith("$2y$")) {
            return "$2a$" + hash.substring(4);
        }
        return hash;
    }
}
