-- =====================================================================
--  TransiGo - Comptes de test pour la connexion par rôle
--  À exécuter APRÈS schema.sql et data.sql.
--
--  Identifiants de test :
--    Administrateur          : admin@transigo.ma        / Admin@123!
--    Gestionnaire de transport : gestionnaire@transigo.ma / Gestion@123!
--
--  Les mots de passe sont stockés en BCrypt, conformément au schéma.
--  Ne jamais réutiliser ces identifiants en production.
-- =====================================================================

INSERT INTO utilisateurs (nom, email, mot_de_passe_hash, telephone, role, actif)
VALUES
    (
        'Administrateur Test',
        'admin@transigo.ma',
        '$2a$10$7NE8LQRlkmFff6Hh3sXgK.kt/3ElEthYhBCu4ZSR9bRed31LorhqO',
        '0600000000',
        'ADMINISTRATEUR',
        TRUE
    ),
    (
        'Gestionnaire Test',
        'gestionnaire@transigo.ma',
        '$2a$10$yFcmiqWAOfujNHw21VmL5u12iSVSDoS8brpeOSZWN3bD8u.7.Pu.K',
        '0600000001',
        'GESTIONNAIRE',
        TRUE
    )
ON CONFLICT (email) DO UPDATE SET
    nom = EXCLUDED.nom,
    mot_de_passe_hash = EXCLUDED.mot_de_passe_hash,
    role = EXCLUDED.role,
    actif = TRUE;
