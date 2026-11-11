
package com.transigo.config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestRead {

    public static void main(String[] args) {

        String sql = "SELECT nom, email, role FROM utilisateurs";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            System.out.println("Liste des utilisateurs :");

            while (result.next()) {
                System.out.println("Nom : " + result.getString("nom"));
                System.out.println("Email : " + result.getString("email"));
                System.out.println("Role : " + result.getString("role"));
                System.out.println("-------------------");
            }

        } catch (SQLException e) {
            System.out.println("Erreur de lecture !");
            e.printStackTrace();
        }
    }
}
