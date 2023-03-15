module com.example.groupa {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;
    requires com.opencsv;

    opens com.example.groupa to javafx.fxml;
    exports com.example.groupa;
    opens com.example.groupa.controllers to javafx.fxml;
    exports com.example.groupa.controllers;
    opens com.example.groupa.models to javafx.fxml;
    exports com.example.groupa.models;
    opens com.example.groupa.utils to javafx.fxml;
    exports com.example.groupa.utils;
}