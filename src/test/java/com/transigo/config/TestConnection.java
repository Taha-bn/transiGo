package com.transigo.config;

import com.transigo.config.DatabaseConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class TestConnection {

    public static void main(String[] args) {

        try {
            Connection connection = DatabaseConnection.getConnection();

            System.out.println("Connexion réussie !");
            System.out.println("PostgreSQL est connecté.");

            connection.close();

        } catch (SQLException e) {
            System.out.println("Échec de connexion !");
            e.printStackTrace();
        }
    }
}
