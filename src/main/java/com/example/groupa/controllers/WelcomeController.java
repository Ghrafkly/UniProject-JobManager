package com.example.groupa.controllers;

import com.example.groupa.DatabaseConnection;
import com.example.groupa.JSSApp;
import com.example.groupa.models.Job;
import com.example.groupa.models.User;
import com.example.groupa.utils.DatabaseUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * @author Kyle Sharp
 * <p>
 * This class is the controller for the welcome screen. It is responsible for logging in or registering a new user.
 * </p>
 */
public class WelcomeController extends AnchorPane implements Initializable, Controller {
    @FXML
    private Button btnGuest, btnLogin, btnRegister;
    @FXML
    private RadioButton rbSeeker, rbRecruiter;
    @FXML
    private TextField tfEmail;
    @FXML
    private PasswordField pfPassword;

    private BaseController parentController;
    private final DatabaseConnection dbConnection = new DatabaseConnection();
    private DatabaseUtils dbUtils = new DatabaseUtils(dbConnection.getConnection());

    public WelcomeController() {
        BaseController.currentStage = "welcome";
        BaseController.user = new User();
        BaseController.job = new Job();
        BaseController.returnTo = new ArrayList<>();
        BaseController.recruiterJob = new Job();

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(JSSApp.class.getResource("fxml/welcome.fxml"));

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

    public void setDbUtils(Connection dbConnection) {
        dbUtils = new DatabaseUtils(dbConnection);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup toggleGroup = new ToggleGroup();
        rbSeeker.setToggleGroup(toggleGroup);
        rbRecruiter.setToggleGroup(toggleGroup);

        BaseController.jobSearch = dbUtils.getJobs();

        btnGuest.setOnAction(event -> {
            BaseController.loggedIn = false;
            parentController.setContent("findjobs");
        });
        btnLogin.setOnAction(event -> {
            boolean emailCheck = tfEmail.getText().isEmpty();
            boolean passwordCheck = pfPassword.getText().isEmpty();
            boolean radioCheck = toggleGroup.getSelectedToggle() == null;
            String toggle;

            if (emailCheck || passwordCheck || radioCheck) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Please fill in all fields");
                alert.show();
            } else {
                toggle = ((RadioButton) toggleGroup.getSelectedToggle()).getText();
                boolean login = dbUtils.login(tfEmail.getText(), pfPassword.getText(), toggle);

                if (!login) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error");
                    alert.setContentText("Incorrect email or password");
                    alert.show();
                } else {
                    int userID = dbUtils.getUserID(tfEmail.getText(), toggle);

                    System.out.println(userID);

                    System.out.println(dbUtils.getSeeker(userID));

                    BaseController.user = toggle.equalsIgnoreCase("Seeker")
                            ? dbUtils.getSeeker(userID)
                            : dbUtils.getRecruiter(userID);
                    BaseController.user.setSkills(dbUtils.getUserSkills(userID));
                    BaseController.loggedIn = true;

                    parentController.setContent("homepage");
                }
            }
        });
        btnRegister.setOnAction(event -> {
            BaseController.returnTo.add("welcome");
            parentController.setContent("createaccount");
        });
    }
}
