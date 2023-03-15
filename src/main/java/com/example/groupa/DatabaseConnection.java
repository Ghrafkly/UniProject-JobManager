package com.example.groupa;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * @author Kyle Sharp
 * <p>
 * This class is used to connect to the database.
 * </p>
 */
public class DatabaseConnection {

    public Connection connection;

    public Connection getConnection() {
        try (FileReader reader = new FileReader("src/main/resources/com/example/groupa/database/dbconfig.properties")) {
            Properties props = new Properties();
            props.load(reader);
            String user = props.getProperty("dbUser");
            String password = props.getProperty("dbPassword");
            String dbName = props.getProperty("dbName");
            String dbUrl = "jdbc:mysql://localhost:3306/" + dbName;

            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(dbUrl, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }
}