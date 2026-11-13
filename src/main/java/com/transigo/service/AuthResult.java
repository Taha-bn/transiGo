package com.transigo.service;

import com.transigo.model.Utilisateur;

/**
 * Résultat d'une tentative d'authentification.
 */
public record AuthResult(Status status, Utilisateur utilisateur) {

    public enum Status {
        SUCCESS,
        INVALID_CREDENTIALS,
        INACTIVE_ACCOUNT,
        SERVER_ERROR
    }

    public static AuthResult success(Utilisateur utilisateur) {
        return new AuthResult(Status.SUCCESS, utilisateur);
    }

    public static AuthResult failure(Status status) {
        return new AuthResult(status, null);
    }

    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }
}
