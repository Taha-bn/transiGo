
-- ==========================================
-- TransiGo - Données de test
-- Fichier : data.sql
-- ==========================================

-- 1. Utilisateur
INSERT INTO utilisateurs
(nom, email, mot_de_passe_hash, telephone, role)
VALUES
    ('Gestionnaire Test', 'gestionnaire@transigo.ma',
     'hash_test', '0600000001', 'GESTIONNAIRE');

-- 2. Client
INSERT INTO clients
(nom, telephone, email, adresse)
VALUES
    ('Client Test', '0600000002',
     'client@transigo.ma', 'Taroudant');

-- 3. Chauffeur
INSERT INTO chauffeurs
(nom, telephone, numero_permis)
VALUES
    ('Chauffeur Test', '0600000003', 'PERMIS-TR-001');

-- 4. Véhicule
INSERT INTO vehicules
(immatriculation, type, capacite_kg, statut)
VALUES
    ('12345-A-6', 'Camion', 5000, 'DISPONIBLE');

-- 5. Commande de transport
INSERT INTO commandes_transport
(reference, adresse_depart, adresse_arrivee,
 nature_marchandise, poids_kg, date_souhaitee,
 statut, client_id, utilisateur_id)
VALUES
    (
        'CMD-TEST-001',
        'Taroudant',
        'Agadir',
        'Marchandise générale',
        500,
        '2026-11-12',
        'PLANIFIEE',
        (SELECT id FROM clients WHERE email = 'client@transigo.ma'),
        (SELECT id FROM utilisateurs WHERE email = 'gestionnaire@transigo.ma')
    );

-- 6. Livraison
INSERT INTO livraisons
(commande_id, vehicule_id, chauffeur_id,
 date_depart_prevue, date_arrivee_prevue)
VALUES
    (
        (SELECT id FROM commandes_transport WHERE reference = 'CMD-TEST-001'),
        (SELECT id FROM vehicules WHERE immatriculation = '12345-A-6'),
        (SELECT id FROM chauffeurs WHERE numero_permis = 'PERMIS-TR-001'),
        '2026-11-12 08:00:00',
        '2026-11-12 12:00:00'
    );

-- 7. Historique des statuts
INSERT INTO historique_statut
(commande_id, ancien_statut, nouveau_statut, utilisateur_id)
VALUES
    (
        (SELECT id FROM commandes_transport WHERE reference = 'CMD-TEST-001'),
        'EN_ATTENTE',
        'PLANIFIEE',
        (SELECT id FROM utilisateurs WHERE email = 'gestionnaire@transigo.ma')
    );
