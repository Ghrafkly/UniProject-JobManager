package com.example.groupa.controllers;

import com.example.groupa.DatabaseConnection;
import com.example.groupa.JSSApp;
import com.example.groupa.utils.DatabaseUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Kyle Sharp
 * <p>
 *     This class is the controller for the advanced search page. It allows a user to search the DB using multiple parameters.
 * </p>
 */
public class AdvancedSearchController extends AnchorPane implements Initializable, Controller {
    @FXML
    private TextField tfTitle, tfCompany, tfSalary, tfLocation;
    @FXML
    private ComboBox<String> cbSkill, cbCertification;
    @FXML
    private Button btnSearch, btnCancel;

    private BaseController parentController;
    private final DatabaseConnection dbConnection = new DatabaseConnection();
    private DatabaseUtils dbUtils = new DatabaseUtils(dbConnection.getConnection());

    public AdvancedSearchController() {
        BaseController.currentStage = "advancedsearch";
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(JSSApp.class.getResource("fxml/advancedsearch.fxml"));

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
            String title = "";
            String company = "";
            int salary = 0;
            String location = "";
            String skill = "";
            String certification = "";

            if (!tfTitle.getText().isEmpty()) title = tfTitle.getText();
            if (!tfCompany.getText().isEmpty()) company = tfCompany.getText();
            if (!tfSalary.getText().isEmpty()) salary = Integer.parseInt(tfSalary.getText());
            if (!tfLocation.getText().isEmpty()) location = tfLocation.getText();
            if (!(cbSkill.getValue() == null)) skill = cbSkill.getValue();
            if (!(cbCertification.getValue() == null)) certification = cbCertification.getValue();

            BaseController.jobSearch = dbUtils.advancedSearch(title, company, salary, location, skill, certification);
            parentController.setContent("findjobs");
        });


        btnCancel.setOnAction(event -> {
            parentController.setContent("findjobs");
        });
    }

    /**
     * This method sets up the controller.
     */
    private void setup() {
        cbSkill.getItems().addAll(dbUtils.getSkills());
        cbCertification.getItems().addAll(dbUtils.getCertifications());

        tfLocation.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfLocation.setText(newValue.replaceAll("\\D", ""));
            } else if (newValue.length() > 4) {
                tfLocation.setText(oldValue);
            }
        });

        tfSalary.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfSalary.setText(newValue.replaceAll("\\D", ""));
            } else if (newValue.length() > 6) {
                tfSalary.setText(oldValue);
            }
        });
    }
}
