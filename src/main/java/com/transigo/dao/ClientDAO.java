
package com.transigo.dao;

import com.transigo.config.DatabaseConnection;
import com.transigo.model.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO {

    // Ajouter un client
    public void addClient(Client client) throws SQLException {

        String sql = """
                INSERT INTO clients (nom, telephone, email, adresse, actif)
                VALUES (?, ?, ?, ?, ?)
                RETURNING id
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, client.getNom());
            statement.setString(2, client.getTelephone());
            statement.setString(3, client.getEmail());
            statement.setString(4, client.getAdresse());
            statement.setBoolean(5, client.isActif());

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    client.setId(result.getInt("id"));
                }
            }
        }
    }

    // Recuperer tous les clients
    public List<Client> getAllClients() throws SQLException {

        List<Client> clients = new ArrayList<>();

        String sql = "SELECT * FROM clients";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {

                Client client = new Client(
                        result.getInt("id"),
                        result.getString("nom"),
                        result.getString("telephone"),
                        result.getString("email"),
                        result.getString("adresse"),
                        result.getBoolean("actif")
                );

                clients.add(client);
            }
        }

        return clients;
    }

    // Recuperer un client par son ID
    public Client getClientById(int id) throws SQLException {

        String sql = "SELECT * FROM clients WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {

                    return new Client(
                            result.getInt("id"),
                            result.getString("nom"),
                            result.getString("telephone"),
                            result.getString("email"),
                            result.getString("adresse"),
                            result.getBoolean("actif")
                    );
                }
            }
        }

        return null;
    }

    // Modifier un client
    public void updateClient(Client client) throws SQLException {

        String sql = """
            UPDATE clients
            SET nom = ?, telephone = ?, email = ?, adresse = ?, actif = ?
            WHERE id = ?
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, client.getNom());
            statement.setString(2, client.getTelephone());
            statement.setString(3, client.getEmail());
            statement.setString(4, client.getAdresse());
            statement.setBoolean(5, client.isActif());
            statement.setInt(6, client.getId());

            statement.executeUpdate();
        }
    }
    // Supprimer un client
    public void deleteClient(int id) throws SQLException {

        String sql = "DELETE FROM clients WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();
        }
    }


}
