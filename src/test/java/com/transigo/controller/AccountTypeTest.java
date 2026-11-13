package com.transigo.controller;

import com.transigo.model.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountTypeTest {

    @Test
    void administrateurMatchesOnlyAdministrateurRole() {
        assertTrue(AccountType.ADMINISTRATEUR.matches(Role.ADMINISTRATEUR));
        assertFalse(AccountType.ADMINISTRATEUR.matches(Role.GESTIONNAIRE));
    }

    @Test
    void gestionnaireMatchesOnlyGestionnaireRole() {
        assertTrue(AccountType.GESTIONNAIRE.matches(Role.GESTIONNAIRE));
        assertFalse(AccountType.GESTIONNAIRE.matches(Role.ADMINISTRATEUR));
    }
}
