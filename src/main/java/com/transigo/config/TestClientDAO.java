
package com.transigo.config;

import com.transigo.dao.ClientDAO;
import com.transigo.model.Client;

import java.sql.SQLException;
import java.util.List;

public class TestClientDAO {

    public static void main(String[] args) {

        ClientDAO clientDAO = new ClientDAO();

        try {

            // 1. Ajouter un client
            Client client = new Client(
                    0,
                    "Test Client",
                    "0600000000",
                    "test@example.com",
                    "Taroudant",
                    true
            );

            clientDAO.addClient(client);

            int idClient = client.getId();

            System.out.println("Client ajouté avec succès !");
            System.out.println("ID généré : " + idClient);


            // 2. Recuperer tous les clients
            List<Client> clients = clientDAO.getAllClients();

            System.out.println("\nListe des clients :");

            for (Client c : clients) {
                System.out.println(c);
            }


            // 3. Rechercher un client par ID
            Client foundClient = clientDAO.getClientById(idClient);

            System.out.println("\nRecherche par ID :");

            if (foundClient != null) {
                System.out.println(foundClient);
            } else {
                System.out.println("Client introuvable");
            }


            // 4. Modifier un client
            Client clientToUpdate = clientDAO.getClientById(idClient);

            if (clientToUpdate != null) {

                clientToUpdate.setNom("Client Modifie");
                clientToUpdate.setTelephone("0612345678");
                clientToUpdate.setEmail("modified@example.com");
                clientToUpdate.setAdresse("Agadir");

                clientDAO.updateClient(clientToUpdate);

                System.out.println("\nClient modifie avec succès !");

                Client updatedClient =
                        clientDAO.getClientById(idClient);

                System.out.println(updatedClient);

            } else {
                System.out.println("Client introuvable");
            }


            // 5. Supprimer un client
            clientDAO.deleteClient(idClient);

            System.out.println("\nClient supprimé !");

            Client deletedClient =
                    clientDAO.getClientById(idClient);

            if (deletedClient == null) {
                System.out.println(
                        "Suppression confirmée : client introuvable !"
                );
            } else {
                System.out.println("Erreur : le client existe encore !");
            }


        } catch (SQLException e) {

            System.out.println("Erreur SQL : " + e.getMessage());
            e.printStackTrace();

        }
    }
}
