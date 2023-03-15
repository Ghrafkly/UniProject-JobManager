package com.example.groupa.controllers;

import com.example.groupa.DatabaseConnection;
import com.example.groupa.JSSApp;
import com.example.groupa.utils.DatabaseUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * @author Kyle Sharp
 * <p>
 *     This class is the controller for the AccountDetails page. It handles users updating details or deleting their account.
 * </p>
 */
public class AccountDetailsController extends AnchorPane implements Initializable, Controller {
    @FXML
    private TextField tfFirstName, tfLastName, tfEmail, tfCompany, tfLocation, tfPassword;

    @FXML
    private PasswordField pfPassword;

    @FXML
    private TextArea taBio, taSkills;

    @FXML
    private ComboBox<String> cbCerts;

    @FXML
    private CheckBox cbShowPassword;

    @FXML
    private Button btnUpdateDetails, btnDeleteAccount, btnChangeSkills;

    private BaseController parentController;
    private final DatabaseConnection dbConnection = new DatabaseConnection();
    private DatabaseUtils dbUtils = new DatabaseUtils(dbConnection.getConnection());
    private String user;

    public AccountDetailsController(String user) {
        BaseController.currentStage = "accountdetails";
        System.out.println(BaseController.currentStage);
        this.user = user;
        String fxml = String.format("fxml/accountdetails_%s.fxml", user);
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
        setup();

        btnUpdateDetails.setOnAction(event -> {
            updateAccountDetails();
        });

        btnDeleteAccount.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Deleting account");
            alert.setHeaderText("Deleting account");
            alert.setContentText("Please confirm you want to delete your account");
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                int userID = dbUtils.getUserID(BaseController.user.getEmail(), BaseController.user.getRole());
                if (BaseController.user.getRole().equalsIgnoreCase("seeker")) {
                    dbUtils.deleteSeeker(userID);
                } else {
                    dbUtils.deleteRecruiter(userID);
                }
                parentController.setContent("welcome");
            }
        });
    }

    /**
     * This method setups the controller.
     */
    private void setup() {
        tfFirstName.setText(BaseController.user.getfName());
        tfLastName.setText(BaseController.user.getlName());
        tfEmail.setText(BaseController.user.getEmail());
        tfEmail.setEditable(false);

        tfLocation.setText(BaseController.user.getLocation());
        taBio.setText(BaseController.user.getBio());
        passwordField();

        if (BaseController.user.getRole().equalsIgnoreCase("seeker")) {
            ArrayList<String> skills = BaseController.user.getSkills();
            taSkills.setText(String.join(", ", skills));
        } else {
            tfCompany.setText(BaseController.user.getCompany());
        }

        if (user.equalsIgnoreCase("seeker")) {
            seeker();
        }
    }

    /**
     * Loads data specific to a seeker
     */
    private void seeker() {
        dbUtils.getCertifications().forEach(certification -> cbCerts.getItems().add(certification));
        System.out.println(BaseController.user.getCertification());
        cbCerts.getSelectionModel().select(BaseController.user.getCertification());


        btnChangeSkills.setOnAction(event -> {
            System.out.println("Change skills button clicked");
        });
    }

    /**
     * Sets the password field properties
     */
    private void passwordField() {
        pfPassword.setText(BaseController.user.getPassword());

        tfPassword.setManaged(false);
        tfPassword.setVisible(false);

        tfPassword.managedProperty().bind(cbShowPassword.selectedProperty());
        tfPassword.visibleProperty().bind(cbShowPassword.selectedProperty());

        pfPassword.managedProperty().bind(cbShowPassword.selectedProperty().not());
        pfPassword.visibleProperty().bind(cbShowPassword.selectedProperty().not());

        tfPassword.textProperty().bindBidirectional(pfPassword.textProperty());
    }

    /**
     * Updates the account details
     */
    private void updateAccountDetails() {
        if (emptyCheck()) {
            if (changedCheck()) {
                BaseController.user.setfName(tfFirstName.getText());
                BaseController.user.setlName(tfLastName.getText());
                BaseController.user.setEmail(tfEmail.getText());
                BaseController.user.setLocation(tfLocation.getText());
                BaseController.user.setBio(taBio.getText());
                BaseController.user.setPassword(pfPassword.getText());

                tfFirstName.setText(BaseController.user.getfName());
                tfLastName.setText(BaseController.user.getlName());
                tfEmail.setText(BaseController.user.getEmail());
                tfLocation.setText(BaseController.user.getLocation());
                taBio.setText(BaseController.user.getBio());
                pfPassword.setText(BaseController.user.getPassword());

                if (user.equalsIgnoreCase("seeker")) {
                    String[] skills = taSkills.getText().split(",");
                    ArrayList<String> skillsList = Arrays.stream(skills).map(String::trim).collect(Collectors.toCollection(ArrayList::new));

                    BaseController.user.setSkills(skillsList);
                    BaseController.user.setCertification(cbCerts.getValue());
                } else if (user.equalsIgnoreCase("recruiter")) {
                    BaseController.user.setCompany(tfCompany.getText());
                    tfCompany.setText(BaseController.user.getCompany());
                }
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Account updated!");
                alert.show();

                updateDB();
            }
        }
    }

    private boolean emptyCheck() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        StringBuilder alertMessage = new StringBuilder();

        boolean firstNameCheckEmpty = tfFirstName.getText().isEmpty();
        boolean lastNameCheckEmpty = tfLastName.getText().isEmpty();
        boolean locationCheckEmpty = tfLocation.getText().isEmpty();
        boolean passwordCheckEmpty = pfPassword.getText().isEmpty();
        boolean companyCheckEmpty = false;
        boolean locationCheckValid = tfLocation.getText().length() == 4;

        if (firstNameCheckEmpty) alertMessage.append("First name cannot be empty");
        if (lastNameCheckEmpty) alertMessage.append("Last name cannot be empty");
        if (locationCheckEmpty) alertMessage.append("Location cannot be empty");
        if (passwordCheckEmpty) alertMessage.append("Password cannot be empty");
        if (!locationCheckValid) alertMessage.append("Location must be a valid Australian postcode");


        if (BaseController.user.getRole().equalsIgnoreCase("recruiter")) {
            companyCheckEmpty = tfCompany.getText().isEmpty();
            if (companyCheckEmpty) {
                alertMessage.append("Company cannot be empty");
            }
        }

        if (firstNameCheckEmpty || lastNameCheckEmpty || companyCheckEmpty || locationCheckEmpty || passwordCheckEmpty || !locationCheckValid) {
            alert.setContentText(alertMessage.toString());
            alert.showAndWait();
        } else {
            return true;
        }

        return false;
    }

    private boolean changedCheck() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        StringBuilder alertMessage = new StringBuilder();

        boolean firstNameCheckChanged = tfFirstName.getText().equals(BaseController.user.getfName());
        boolean lastNameCheckChanged = tfLastName.getText().equals(BaseController.user.getlName());
        boolean locationCheckChanged = tfLocation.getText().equals(BaseController.user.getLocation());
        boolean companyCheckChanged;
        boolean passwordCheckChanged = pfPassword.getText().equals(BaseController.user.getPassword());

        if (!firstNameCheckChanged) alertMessage.append("Confirm update to first name. \n");
        if (!lastNameCheckChanged) alertMessage.append("Confirm update to last name. \n");
        if (!locationCheckChanged) alertMessage.append("Confirm update to location. \n");
        if (!passwordCheckChanged) alertMessage.append("Confirm update to password. \n");

        if (BaseController.user.getRole().equalsIgnoreCase("recruiter")) {
            companyCheckChanged = tfCompany.getText().equals(BaseController.user.getCompany());
            if (!companyCheckChanged) {
                alertMessage.append("Confirm update to company. \n");
            }
        }

        alert.setContentText(alertMessage.toString());
        alert.showAndWait();

        return alert.getResult() == ButtonType.OK;
    }

    /**
     * Updates the database with the new account details
     */
    private void updateDB() {
        if (user.equalsIgnoreCase("seeker")) {
            dbUtils.updateSeeker(BaseController.user, dbUtils.getUserID(BaseController.user.getEmail(), BaseController.user.getRole()));
        } else if (user.equalsIgnoreCase("recruiter")) {
            if (dbUtils.checkCompanies(BaseController.getUser().getCompany())) {
                System.out.println("Adding company");
                dbUtils.addCompany(BaseController.getUser().getCompany());
            }
            dbUtils.updateRecruiter(BaseController.user, dbUtils.getUserID(BaseController.user.getEmail(), BaseController.user.getRole()));
        }
    }
}
