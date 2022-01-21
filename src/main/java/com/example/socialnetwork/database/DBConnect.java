package com.example.socialnetwork.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {

    private static final String CONNECTION_URL = "jdbc:postgresql://localhost:5432/MAP_ToySocialNetwork";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "12345678";

    public Connection connection() {
        Connection dbConn = null;

        try {
            dbConn = DriverManager.getConnection(CONNECTION_URL, USERNAME, PASSWORD);
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }

        return dbConn;
    }
}
