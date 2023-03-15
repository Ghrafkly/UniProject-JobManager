package com.example.groupa.controllers;

import com.example.groupa.DatabaseConnection;
import com.example.groupa.JSSApp;
import com.example.groupa.utils.DatabaseUtils;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Kyle Sharp
 * <p>
 *     This class is the controller for the header of the application.
 * </p>
 *
 */
public class HeaderController extends HBox implements Initializable, Controller {
    @FXML
    private Button btnSearch, btnAdvancedSearch, btnCancel;
    @FXML
    private TextField tfSearchBox;
    @FXML
    private ComboBox<String> cbCategory = new ComboBox<>();

    private BaseController parentController;
    private SearchController searchController;
    private final DatabaseConnection dbConnection = new DatabaseConnection();
    private DatabaseUtils dbUtils = new DatabaseUtils(dbConnection.getConnection());

    public HeaderController() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(JSSApp.class.getResource("fxml/header.fxml"));

            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setParentController(BaseController parentController) {
        this.parentController = parentController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setup();

        btnSearch.setOnAction(event -> {
            if (tfSearchBox.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Search field is empty");
                alert.setContentText("Please enter a search term");
                alert.showAndWait();
            } else {
                searchController = new SearchController(tfSearchBox.getText(), cbCategory.getValue());
                searchController.setParentController(parentController);
                searchController.search();
            }
        });

        btnAdvancedSearch.setOnAction(event -> {
            parentController.setContent("advancedsearch");
        });


    }

    /**
     * Setups the header
     */
    private void setup() {
        ChangeListener<String> changeListener = (observableValue, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfSearchBox.setText(newValue.replaceAll("\\D", ""));
            }
        };

        cbCategory.getItems().addAll("Title", "Company", "Salary", "Location", "Skills", "Certification");
        cbCategory.getSelectionModel().selectFirst();

        cbCategory.setOnAction(event -> {
            tfSearchBox.setText("");
            if (cbCategory.getValue().equals("Salary")) {
                tfSearchBox.textProperty().addListener(changeListener);
            } else {
                tfSearchBox.textProperty().removeListener(changeListener);
            }
        });
    }
}
