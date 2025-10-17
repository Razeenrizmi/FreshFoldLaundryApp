package com.laundry.freshfoldlaundryapp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
    public static Connection connect() {
        // MySQL configuration instead of SQL Server
        String url = "jdbc:mysql://localhost:3307/freshfold_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String user = "root";  // Change this to your MySQL username
        String password = "your_secure_password";  // Change this to your MySQL password

        Connection con = null;

        try {
            // Load MySQL driver instead of SQL Server
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to MySQL DB
            con = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            System.out.println("MySQL Connection Error: " + e);
        }

        return con;
    }

    public static Connection getConnection() {
        return connect();
    }
}