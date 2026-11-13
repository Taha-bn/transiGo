package com.transigo.util;

import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordHasherTest {

    private static final String PASSWORD = "Secret123!";

    @Test
    void acceptsMatchingBcryptPassword() {
        String hash = BCrypt.hashpw(PASSWORD, BCrypt.gensalt(4));

        assertTrue(PasswordHasher.verify(PASSWORD, hash));
    }

    @Test
    void rejectsWrongPassword() {
        String hash = BCrypt.hashpw(PASSWORD, BCrypt.gensalt(4));

        assertFalse(PasswordHasher.verify("MauvaisMotDePasse", hash));
    }

    @Test
    void acceptsBcryptVariantPrefixes() {
        String hash = BCrypt.hashpw(PASSWORD, BCrypt.gensalt(4));
        String variant = "$2b$" + hash.substring(4);

        assertTrue(PasswordHasher.verify(PASSWORD, variant));
    }

    @Test
    void rejectsPlainTextAndMalformedHashes() {
        assertFalse(PasswordHasher.verify(PASSWORD, "hash_test"));
        assertFalse(PasswordHasher.verify(PASSWORD, "$2a$10$trop-court"));
        assertFalse(PasswordHasher.verify(PASSWORD, ""));
        assertFalse(PasswordHasher.verify(null, null));
    }
}
