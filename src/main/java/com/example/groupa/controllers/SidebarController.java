package com.example.groupa.controllers;

import com.example.groupa.DatabaseConnection;
import com.example.groupa.JSSApp;
import com.example.groupa.models.Job;
import com.example.groupa.models.User;
import com.example.groupa.utils.DatabaseUtils;
import com.example.groupa.utils.InsertDataUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Kyle Sharp
 * <p>
 * This class is the controller for the sidebar. It is responsible for managing events that occur in the sidebar.
 * </p>
 */
public class SidebarController extends VBox implements Initializable, Controller {
    @FXML
    private Button btnFindJobs, btnCreateAccount, btnLogout, btnAccount, btnCreateJob, btnReset;
    @FXML
    private Label lblUser, lblType;

    private BaseController parentController;
    private InsertDataUtils insertDataUtils = new InsertDataUtils();
    private final DatabaseConnection dbConnection = new DatabaseConnection();
    private DatabaseUtils dbUtils = new DatabaseUtils(dbConnection.getConnection());

    public SidebarController(boolean loggedin) {
        String fxml = loggedin ? "fxml/sidebar_LoggedIn.fxml" : "fxml/sidebar_Guest.fxml";
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
        if (BaseController.loggedIn) {
            lblUser.setText(BaseController.user.getfName());
            lblType.setText(BaseController.user.getRole());
        }

        if (btnCreateJob != null) {
            if (BaseController.user.getRole() != null) {
                if (BaseController.user.getRole().equalsIgnoreCase("recruiter")) {
                    btnCreateJob.setOnAction(event -> {
                        BaseController.returnTo.add(BaseController.currentStage);
                        parentController.setContent("createjob");
                    });
                } else {
                    btnCreateJob.setDisable(true);
                }
            } else {
                this.getChildren().remove(btnCreateJob);
            }
        }

        btnFindJobs.setOnAction(event -> {
            parentController.setContent("findjobs");
        });

        if (btnCreateAccount != null) {
            btnCreateAccount.setOnAction(event -> {
                BaseController.returnTo.add("findjobs");
                parentController.setContent("createaccount");
            });
        } else if (btnAccount != null) {
            btnAccount.setOnAction(event -> {
                BaseController.returnTo.add("homepage");
                parentController.setContent("homepage");
            });
        }

        btnLogout.setOnAction(event -> {
            BaseController.user = new User();
            BaseController.job = new Job();
            BaseController.recruiterJob = new Job();
            parentController.setContent("welcome");
        });

        btnReset.setDisable(true);

//        btnReset.setOnAction(event -> {
//            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//            alert.setTitle("Resetting Database");
//            alert.setHeaderText("This will reset the database to its original state.");
//            alert.setContentText("Please confirm you want to reset the database. You will lose all entered data, and the application will close");
//            alert.showAndWait();
//
//            if (alert.getResult().getText().equalsIgnoreCase("ok")) {
//                insertDataUtils.deleteAll();
//                System.exit(0);
//            }
//        });
    }
}
