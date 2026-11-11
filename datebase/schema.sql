-- =====================================================================
--  TransiGo - Schéma PostgreSQL (aligné sur le dossier de conception)
--  7 tables : utilisateurs, clients, chauffeurs, vehicules,
--             commandes_transport, livraisons, historique_statut
-- =====================================================================

-- Nécessaire pour empêcher les chevauchements (RG-10, RG-11)
CREATE EXTENSION IF NOT EXISTS btree_gist;

DROP TABLE IF EXISTS historique_statut      CASCADE;
DROP TABLE IF EXISTS livraisons             CASCADE;
DROP TABLE IF EXISTS commandes_transport    CASCADE;
DROP TABLE IF EXISTS vehicules              CASCADE;
DROP TABLE IF EXISTS chauffeurs             CASCADE;
DROP TABLE IF EXISTS clients                CASCADE;
DROP TABLE IF EXISTS utilisateurs           CASCADE;

-- ---------------------------------------------------------------------
-- 1) Utilisateurs internes (ADMINISTRATEUR / GESTIONNAIRE uniquement)
--    Clients et chauffeurs n'ont PAS de compte (RG-17)
-- ---------------------------------------------------------------------
CREATE TABLE utilisateurs (
                              id                SERIAL       PRIMARY KEY,
                              nom               VARCHAR(100) NOT NULL,
                              email             VARCHAR(150) NOT NULL UNIQUE,
                              mot_de_passe_hash VARCHAR(255) NOT NULL,          -- hash BCrypt, jamais en clair
                              telephone         VARCHAR(20),
                              role              VARCHAR(20)  NOT NULL
                                  CHECK (role IN ('ADMINISTRATEUR', 'GESTIONNAIRE')),
                              actif             BOOLEAN      NOT NULL DEFAULT TRUE   -- désactivation logique (RG-18)
);

-- ---------------------------------------------------------------------
-- 2) Clients (entité métier, sans connexion)
-- ---------------------------------------------------------------------
CREATE TABLE clients (
                         id         SERIAL       PRIMARY KEY,
                         nom        VARCHAR(100) NOT NULL,
                         telephone  VARCHAR(20),
                         email      VARCHAR(150),
                         adresse    VARCHAR(255),
                         actif      BOOLEAN      NOT NULL DEFAULT TRUE
);

-- ---------------------------------------------------------------------
-- 3) Chauffeurs (ressource affectable, sans connexion)
-- ---------------------------------------------------------------------
CREATE TABLE chauffeurs (
                            id             SERIAL       PRIMARY KEY,
                            nom            VARCHAR(100) NOT NULL,
                            telephone      VARCHAR(20),
                            numero_permis  VARCHAR(30)  NOT NULL UNIQUE,
                            disponible     BOOLEAN      NOT NULL DEFAULT TRUE,
                            actif          BOOLEAN      NOT NULL DEFAULT TRUE
);

-- ---------------------------------------------------------------------
-- 4) Véhicules
-- ---------------------------------------------------------------------
CREATE TABLE vehicules (
                           id               SERIAL        PRIMARY KEY,
                           immatriculation  VARCHAR(20)   NOT NULL UNIQUE,
                           type             VARCHAR(50)   NOT NULL,
                           capacite_kg      NUMERIC(10,2) NOT NULL CHECK (capacite_kg > 0),
                           statut           VARCHAR(20)   NOT NULL DEFAULT 'DISPONIBLE'
                               CHECK (statut IN ('DISPONIBLE', 'EN_MAINTENANCE', 'HORS_SERVICE'))
);

-- ---------------------------------------------------------------------
-- 5) Commandes de transport (porte le statut opérationnel - RG-04)
-- ---------------------------------------------------------------------
CREATE TABLE commandes_transport (
                                     id                 SERIAL        PRIMARY KEY,
                                     reference          VARCHAR(30)   NOT NULL UNIQUE,
                                     date_creation      TIMESTAMP     NOT NULL DEFAULT NOW(),
                                     adresse_depart     VARCHAR(255)  NOT NULL,
                                     adresse_arrivee    VARCHAR(255)  NOT NULL,
                                     nature_marchandise VARCHAR(100)  NOT NULL,
                                     poids_kg           NUMERIC(10,2) NOT NULL CHECK (poids_kg > 0),      -- RG-03
                                     date_souhaitee     DATE,
                                     statut             VARCHAR(20)   NOT NULL DEFAULT 'EN_ATTENTE'
                                         CHECK (statut IN ('EN_ATTENTE', 'PLANIFIEE', 'EN_COURS',
                                                           'LIVREE', 'ANNULEE')),
                                     client_id          INTEGER       NOT NULL REFERENCES clients(id),       -- RG-02
                                     utilisateur_id     INTEGER       NOT NULL REFERENCES utilisateurs(id)   -- RG-02
);

-- ---------------------------------------------------------------------
-- 6) Livraisons (créée à la planification - RG-05, RG-12)
-- ---------------------------------------------------------------------
CREATE TABLE livraisons (
                            id                  SERIAL    PRIMARY KEY,
                            commande_id         INTEGER   NOT NULL UNIQUE                       -- 0..1 livraison par commande
                                REFERENCES commandes_transport(id),
                            vehicule_id         INTEGER   NOT NULL REFERENCES vehicules(id),
                            chauffeur_id        INTEGER   NOT NULL REFERENCES chauffeurs(id),
                            date_depart_prevue  TIMESTAMP NOT NULL,
                            date_arrivee_prevue TIMESTAMP NOT NULL,
                            date_depart_reelle  TIMESTAMP,
                            date_arrivee_reelle TIMESTAMP,

                            CONSTRAINT ck_dates_prevues  CHECK (date_arrivee_prevue > date_depart_prevue),   -- RG-07
                            CONSTRAINT ck_dates_reelles  CHECK (date_arrivee_reelle IS NULL
                                OR date_depart_reelle IS NULL
                                OR date_arrivee_reelle >= date_depart_reelle),

    -- RG-11 : un véhicule ne peut pas avoir deux livraisons qui se chevauchent
                            CONSTRAINT ex_vehicule_chevauchement EXCLUDE USING gist (
        vehicule_id WITH =,
        tsrange(date_depart_prevue, date_arrivee_prevue) WITH &&
    ),
    -- RG-10 : idem pour un chauffeur
                            CONSTRAINT ex_chauffeur_chevauchement EXCLUDE USING gist (
        chauffeur_id WITH =,
        tsrange(date_depart_prevue, date_arrivee_prevue) WITH &&
    )
);

-- ---------------------------------------------------------------------
-- 7) Historique des changements de statut (RG-16)
-- ---------------------------------------------------------------------
CREATE TABLE historique_statut (
                                   id              SERIAL      PRIMARY KEY,
                                   commande_id     INTEGER     NOT NULL REFERENCES commandes_transport(id),
                                   ancien_statut   VARCHAR(20)                      -- NULL pour la 1re entrée (création)
                                       CHECK (ancien_statut IN ('EN_ATTENTE', 'PLANIFIEE', 'EN_COURS',
                                                                'LIVREE', 'ANNULEE')),
                                   nouveau_statut  VARCHAR(20) NOT NULL
                                       CHECK (nouveau_statut IN ('EN_ATTENTE', 'PLANIFIEE', 'EN_COURS',
                                                                 'LIVREE', 'ANNULEE')),
                                   date_changement TIMESTAMP   NOT NULL DEFAULT NOW(),
                                   utilisateur_id  INTEGER     NOT NULL REFERENCES utilisateurs(id)
);

-- ---------------------------------------------------------------------
-- RG-08 : capacité du véhicule >= poids de la marchandise
-- (règle sur 3 tables, donc contrôlée par un trigger)
-- ---------------------------------------------------------------------
CREATE OR REPLACE FUNCTION verifier_capacite_livraison() RETURNS TRIGGER AS $$
DECLARE
v_capacite NUMERIC;
    v_poids    NUMERIC;
BEGIN
SELECT capacite_kg INTO v_capacite FROM vehicules WHERE id = NEW.vehicule_id;
SELECT poids_kg    INTO v_poids    FROM commandes_transport WHERE id = NEW.commande_id;
IF v_capacite < v_poids THEN
        RAISE EXCEPTION 'Capacité insuffisante : véhicule % kg < marchandise % kg',
                        v_capacite, v_poids;
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_capacite_livraison
    BEFORE INSERT OR UPDATE OF vehicule_id, commande_id ON livraisons
    FOR EACH ROW EXECUTE FUNCTION verifier_capacite_livraison();

-- ---------------------------------------------------------------------
-- Index sur les clés étrangères et colonnes de recherche
-- ---------------------------------------------------------------------
CREATE INDEX idx_commandes_client      ON commandes_transport(client_id);
CREATE INDEX idx_commandes_utilisateur ON commandes_transport(utilisateur_id);
CREATE INDEX idx_commandes_statut      ON commandes_transport(statut);
CREATE INDEX idx_livraisons_vehicule   ON livraisons(vehicule_id);
CREATE INDEX idx_livraisons_chauffeur  ON livraisons(chauffeur_id);
CREATE INDEX idx_historique_commande   ON historique_statut(commande_id, date_changement);
CREATE INDEX idx_historique_utilisateur ON historique_statut(utilisateur_id);
