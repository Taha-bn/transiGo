package com.transigo.controller;

import com.transigo.model.Role;

/**
 * Type de compte affiché sur l'écran de connexion.
 *
 * <p>Ce choix est une préférence visuelle et de navigation. Il ne constitue
 * jamais une preuve d'autorisation : le rôle réel est toujours relu depuis la
 * base de données par le service d'authentification, puis comparé à ce choix.</p>
 */
public enum AccountType {

    ADMINISTRATEUR(
            "Administrateur",
            "Contrôle complet du système",
            Role.ADMINISTRATEUR
    ),

    GESTIONNAIRE(
            "Gestionnaire de Transport",
            "Opérations, flotte et planification",
            Role.GESTIONNAIRE
    );

    private final String title;
    private final String description;
    private final Role role;

    AccountType(String title, String description, Role role) {
        this.title = title;
        this.description = description;
        this.role = role;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Role toRole() {
        return role;
    }

    public boolean matches(Role actualRole) {
        return actualRole == role;
    }
}
