
package com.transigo.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TestInsert {

    public static void main(String[] args) {

        String sql = """
            INSERT INTO utilisateurs
            (nom, email, mot_de_passe_hash, telephone, role)
            VALUES (?, ?, ?, ?, ?)
            ON CONFLICT (email) DO NOTHING
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "Test Java");
            statement.setString(2, "test.java@transigo.ma");
            statement.setString(3, "hash_test_java");
            statement.setString(4, "0611111111");
            statement.setString(5, "GESTIONNAIRE");

            int rows = statement.executeUpdate();

            if (rows == 1) {
                System.out.println("Insertion réussie !");
            } else {
                System.out.println("Cet utilisateur existe déjà.");
            }

        } catch (SQLException e) {
            System.out.println("Erreur d'insertion !");
            e.printStackTrace();
        }
    }
}
