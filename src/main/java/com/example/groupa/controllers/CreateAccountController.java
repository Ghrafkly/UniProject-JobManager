package com.example.groupa.controllers;

import com.example.groupa.DatabaseConnection;
import com.example.groupa.JSSApp;
import com.example.groupa.utils.DatabaseUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

/**
 * @author Kyle Sharp
 * <p>
 *     This class is the controller for creating an account
 * </p>
 */
public class CreateAccountController extends AnchorPane implements Initializable, Controller {
    @FXML
    private Button btnNext, btnCancel, btnRegister;
    @FXML
    private TextField tfFirstName, tfLastName, tfEmail, tfCompany, tfLocation;
    @FXML
    private TextArea taBio;
    @FXML
    private PasswordField pfPassword, pfConfirmPassword;
    @FXML
    private RadioButton rbSeeker, rbRecruiter;
    @FXML
    private ComboBox<String> cbCertifications;
    @FXML
    private VBox vbContent;

    private String detailsStage;
    private BaseController parentController;
    private final DatabaseConnection dbConnection = new DatabaseConnection();
    private DatabaseUtils dbUtils = new DatabaseUtils(dbConnection.getConnection());

    public CreateAccountController(String detailsStage) {
        this.detailsStage = detailsStage;
        switch (detailsStage) {
            case "loginStage" -> loadLoginStagePage();
            case "skillsStage" -> loadSkillsStagePage();
            case "detailsStage" -> loadDetailsStagePage();
        }
    }

    @Override
    public void setParentController(BaseController parentController) {
        this.parentController = parentController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        switch (detailsStage) {
            case "loginStage" -> loginStage();
            case "skillsStage" -> skillsStage();
            case "detailsStage" -> detailsStage();
            default -> throw new IllegalStateException("Unexpected value: " + detailsStage);
        }
        btnCancel.setOnAction(event -> {
            parentController.ReturnPage();
        });
    }

    /**
     * Loads the login stage page
     */
    public void loadLoginStagePage() {
        BaseController.currentStage = "loginStage";
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(JSSApp.class.getResource("fxml/createaccount.fxml"));

            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the skills stage page
     */
    public void loadSkillsStagePage() {
        BaseController.currentStage = "skillsStage";
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(JSSApp.class.getResource("fxml/skills.fxml"));

            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the details stage page
     */
    public void loadDetailsStagePage() {
        BaseController.currentStage = "detailsStage";
        String fxml = String.format("fxml/enterdetails_%s.fxml", BaseController.getUser().getRole());
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

    /**
     * First page of the create account process
     */
    public void loginStage() {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        ToggleGroup toggleGroup = new ToggleGroup();
        rbSeeker.setToggleGroup(toggleGroup);
        rbRecruiter.setToggleGroup(toggleGroup);

        btnNext.setOnAction(event -> {
            StringBuilder alertMessage = new StringBuilder();
            String toggle = "";

            boolean firstNameCheck = tfFirstName.getText().isEmpty();
            boolean lastNameCheck = tfLastName.getText().isEmpty();
            boolean emailCheck = tfEmail.getText().isEmpty();
            boolean validEmail = checkEmailValidation(tfEmail.getText());
            boolean passwordCheck = pfPassword.getText().isEmpty();
            boolean confirmPasswordCheck = pfConfirmPassword.getText().isEmpty();
            boolean passwordsMatchCheck = pfPassword.getText().equals(pfConfirmPassword.getText());
            boolean radioCheck = toggleGroup.getSelectedToggle() == null;
            boolean emailExists = (dbUtils.checkRegister(tfEmail.getText(), toggle));

            if (firstNameCheck) alertMessage.append("Please enter your first name.\n");
            if (lastNameCheck) alertMessage.append("Please enter your last name.\n");
            if (emailCheck) alertMessage.append("Please enter your email.\n");
            if (emailExists) alertMessage.append("Account already exists.\n");
            if (!validEmail) alertMessage.append("Please ensure your email is valid.\n");
            if (passwordCheck) alertMessage.append("Please enter a password.\n");
            if (confirmPasswordCheck) alertMessage.append("Please confirm your password.\n");
            if (!passwordsMatchCheck) alertMessage.append("Passwords do not match.\n");
            if (radioCheck) {
                alertMessage.append("Please select a role.\n");
            } else {
                toggle = toggleGroup.getSelectedToggle().equals(rbSeeker) ? "Seeker" : "Recruiter";
            }

            alert.setContentText(alertMessage.toString());
            if (!alertMessage.isEmpty()) alert.show();


            if (!firstNameCheck && !lastNameCheck && !emailCheck && !passwordCheck && !confirmPasswordCheck && passwordsMatchCheck && !radioCheck && validEmail) {
                BaseController.user.setfName(tfFirstName.getText());
                BaseController.user.setlName(tfLastName.getText());
                BaseController.user.setEmail(tfEmail.getText());
                BaseController.user.setPassword(pfPassword.getText());
                BaseController.user.setRole(toggle);

                switch (BaseController.getUser().getRole()) {
                    case "Seeker" -> parentController.setContent("skillsStage");
                    case "Recruiter" -> parentController.setContent("register");
                    default -> System.out.println("Error: Role not found.");
                }
            }
        });
    }

    /**
     * Second page of the create account process. Only for seekers
     */
    public void skillsStage() {
        ArrayList<String> skills = dbUtils.getSkills();
        skills.forEach(skill -> {
            CheckBox checkBox = new CheckBox(skill);
            checkBox.setId("cb" + skill);
            vbContent.getChildren().add(checkBox);
        });

        btnNext.setOnAction(event -> {
            ArrayList<String> selectedSkills = new ArrayList<>();
            for (Node node : vbContent.getChildren()) {
                if (node instanceof CheckBox checkBox) {
                    if (checkBox.isSelected()) {
                        selectedSkills.add(checkBox.getText());
                    }
                }
            }

            BaseController.user.setSkills(selectedSkills);
            parentController.setContent("register");
        });
    }

    /**
     * Final page of the create account process
     */
    public void detailsStage() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        tfLocation.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfLocation.setText(newValue.replaceAll("\\D", ""));
            } else if (newValue.length() > 4) {
                tfLocation.setText(oldValue);
            }
        });

        if (BaseController.getUser().getRole().equalsIgnoreCase("seeker")) {
            dbUtils.getCertifications().forEach(certification -> cbCertifications.getItems().add(certification));
            cbCertifications.getSelectionModel().selectFirst();

            btnRegister.setOnAction(event -> {
                StringBuilder alertMessage = new StringBuilder();
                boolean certificationsCheck = cbCertifications.getValue() == null;
                boolean locationCheck = tfLocation.getText().isEmpty();
                boolean locationValid = tfLocation.getText().length() == 4;
                boolean bioCheck = taBio.getText().isEmpty();

                if (certificationsCheck) alertMessage.append("Please select a certification.\n");
                if (locationCheck) alertMessage.append("Please enter your location.\n");
                if (!locationValid && !locationCheck) alertMessage.append("Please enter a valid Australian postcode");
                if (bioCheck) alertMessage.append("Please enter your bio.\n");

                if (!locationCheck && !bioCheck && !certificationsCheck && locationValid) {
                    BaseController.user.setCertification(cbCertifications.getValue());
                    BaseController.user.setLocation(tfLocation.getText());
                    BaseController.user.setBio(taBio.getText());

                    completeRegistration();
                } else {
                    alert.setContentText(alertMessage.toString());
                    alert.show();
                }
            });
        } else if (BaseController.getUser().getRole().equalsIgnoreCase("recruiter")) {
            btnRegister.setOnAction(event -> {
                StringBuilder alertMessage = new StringBuilder();
                boolean companyCheck = tfCompany.getText().isEmpty();
                boolean locationCheck = tfLocation.getText().isEmpty();
                boolean locationValid = tfLocation.getText().length() == 4;
                boolean bioCheck = taBio.getText().isEmpty();

                if (companyCheck) alertMessage.append("Please enter your company name.\n");
                if (locationCheck) alertMessage.append("Please enter your location.\n");
                if (!locationValid && !locationCheck) alertMessage.append("Please enter a valid Australian postcode");
                if (bioCheck) alertMessage.append("Please enter your bio.\n");

                if (!companyCheck && !locationCheck && !bioCheck && locationValid) {
                    BaseController.user.setCompany(tfCompany.getText());
                    BaseController.user.setLocation(tfLocation.getText());
                    BaseController.user.setBio(taBio.getText());

                    completeRegistration();
                } else {
                    alert.setContentText(alertMessage.toString());
                    alert.show();
                }
            });
        }
    }

    /**
     * Completes the registration process
     */
    public void completeRegistration() {
        dbUtils.userRegister(BaseController.getUser());
        int userID = dbUtils.getUserID(BaseController.getUser().getEmail(), BaseController.getUser().getRole());

        if (BaseController.user.getRole().equalsIgnoreCase("seeker")) {
            dbUtils.update_certification(userID, BaseController.getUser().getCertification());
            dbUtils.addSkillsUser(userID, BaseController.getUser().getSkills());
        } else if (BaseController.user.getRole().equalsIgnoreCase("recruiter")) {
            if (dbUtils.checkCompanies(BaseController.getUser().getCompany())) {
                System.out.println("Adding company");
                dbUtils.addCompany(BaseController.getUser().getCompany());
            }
            dbUtils.update_company(userID, BaseController.getUser().getCompany());
        }

        dbUtils.update_location(userID, BaseController.getUser().getLocation());
        dbUtils.update_biography(userID, BaseController.getUser().getBio());

        BaseController.loggedIn = true;

        parentController.setContent("homepage");
    }

    public static boolean patternMatches(String emailAddress, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(emailAddress)
                .matches();
    }
    public boolean checkEmailValidation(String emailCheck) {
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return (patternMatches(emailCheck, regexPattern));
    }
}
