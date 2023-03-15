package com.example.groupa.controllers;

import com.example.groupa.JSSApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Group A
 * <p>
 *     This class is the controller for the home page.
 * </p>
 */
public class HomePageController extends AnchorPane implements Initializable, Controller {
    @FXML
    private Button btnAccountDetails, btnApplications, btnCreatedJobs, btnInbox;

    private String user;
    private BaseController parentController;

    public HomePageController(String user) {
        this.user = user;
        BaseController.currentStage = "homepage";
        String fxml = String.format("fxml/homepage_%s.fxml", user);
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(JSSApp.class.getResource(fxml));

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
        btnAccountDetails.setOnAction(event -> {
            parentController.setContent("accountdetails");
        });

        if (user.equalsIgnoreCase("seeker")) {
            btnApplications.setOnAction(event -> {
                System.out.println("Applications");
            });
        } else {
            btnCreatedJobs.setOnAction(event -> {
                System.out.println("Created Jobs");
            });
        }
    }
}
