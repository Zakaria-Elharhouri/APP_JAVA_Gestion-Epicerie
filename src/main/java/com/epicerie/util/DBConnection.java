package com.epicerie.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/epicerie?useSSL=false&autoReconnect=true";
    private static final String USER = "root";
    private static final String PASSWORD = "";


    private static Connection customConnection = null;

    private DBConnection() {}

  
    public static Connection getConnection() {
    if (customConnection != null) {
        return customConnection;
    }
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    } catch (ClassNotFoundException | SQLException e) {
        throw new RuntimeException("Échec de la connexion à la base de données", e);
    }
}


    // Définition d'une connexion personnalisée
    public static void setConnection(Connection connection) {
        customConnection = connection;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                if (!conn.isClosed()) {
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
            }
        }
    }
}
