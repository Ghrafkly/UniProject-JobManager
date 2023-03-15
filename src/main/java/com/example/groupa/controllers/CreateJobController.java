package com.example.groupa.controllers;

import com.example.groupa.DatabaseConnection;
import com.example.groupa.JSSApp;
import com.example.groupa.models.Job;
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

/**
 * @author Kyle Sharp
 * <p>
 *     This class is the controller to create a job
 * </p>
 */
public class CreateJobController extends AnchorPane implements Initializable, Controller {
    @FXML
    public TextField tfTitle, tfLocation;
    @FXML
    private TextArea taDescription;
    @FXML
    private VBox vbContentOdd, vbContentEven, vbContent;
    @FXML
    public Spinner<Integer> spSalary;
    @FXML
    public ComboBox<String> cbCertification, cbStatus;
    @FXML
    public Button btnCancel, btnNext, btnCreate;

    private String stage;
    private BaseController parentController;
    private DatabaseConnection dbConnection = new DatabaseConnection();
    private DatabaseUtils dbUtils = new DatabaseUtils(dbConnection.getConnection());

    public CreateJobController(String stage) {
        this.stage = stage;
        switch (stage) {
            case "createjob" -> loadCreateJobPage();
            case "createjob2" -> loadCreateJob2Page();
            case "createjob3" -> loadCreateJob3Page();
        }
    }

    @Override
    public void setParentController(BaseController parentController) {
        this.parentController = parentController;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        switch (stage) {
            case "createjob" -> createJob();
            case "createjob2" -> createJob2();
            case "createjob3" -> createJob3();
        }

        btnCancel.setOnAction(event -> {
            if (BaseController.returnTo.get(BaseController.returnTo.size() - 1).equalsIgnoreCase("createjob")
                    || BaseController.returnTo.get(BaseController.returnTo.size() - 1).equalsIgnoreCase("createjob2")) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Are you sure you want to go back?");
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK){
                    parentController.ReturnPage();
                }

            } else {
                BaseController.recruiterJob = new Job();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setContentText("Are you sure you want to go back?");
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK){
                    parentController.ReturnPage();
                }
            }
        });
    }

    /**
     * Loads the create job page
     */
    public void loadCreateJobPage() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(JSSApp.class.getResource("fxml/createjob.fxml"));

            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the create job 2 page
     */
    public void loadCreateJob2Page() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(JSSApp.class.getResource("fxml/createjob2.fxml"));

            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads the create job 3 page
     */
    public void loadCreateJob3Page() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(JSSApp.class.getResource("fxml/createjob3.fxml"));

            loader.setRoot(this);
            loader.setController(this);
            loader.load();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create job stage
     */
    public void createJob() {
        if (BaseController.recruiterJob.getJob_title() != null) {
            tfTitle.setText(BaseController.recruiterJob.getJob_title());
            tfLocation.setText(BaseController.recruiterJob.getJob_location());
            taDescription.setText(BaseController.recruiterJob.getJob_description());
            spSalary.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(30000, 1000000, Integer.parseInt(BaseController.recruiterJob.getJob_salary()), 10000));
            cbCertification.setValue(BaseController.recruiterJob.getCertification());
            cbStatus.setValue(BaseController.recruiterJob.getStatus());
        } else {
            spSalary.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(30000, 1000000, 30000, 10000));
        }

        dbUtils.getCertifications().forEach(certification -> cbCertification.getItems().add(certification));
        dbUtils.getStates().forEach(state -> cbStatus.getItems().add(state));

        tfLocation.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tfLocation.setText(newValue.replaceAll("\\D", ""));
            } else if (newValue.length() > 4) {
                tfLocation.setText(oldValue);
            }
        });

        spSalary.setEditable(true);
        spSalary.getEditor().setTextFormatter(new TextFormatter<>(change -> change.getControlNewText().matches("\\d*") && spSalary.getEditor().getText().length() < 7 ? change : null));

        btnNext.setOnAction(event -> {
            if (tfTitle.getText().isEmpty() || taDescription.getText().isEmpty() || tfLocation.getText().isEmpty() || cbCertification.getValue() == null || cbStatus.getValue() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Please fill out all fields.");
                alert.showAndWait();
            } else {
                BaseController.recruiterJob = new Job();
                BaseController.recruiterJob.setJob_title(tfTitle.getText());
                BaseController.recruiterJob.setJob_description(taDescription.getText());
                BaseController.recruiterJob.setJob_location(tfLocation.getText());
                BaseController.recruiterJob.setJob_salary(String.valueOf(spSalary.getValue()));
                BaseController.recruiterJob.setCertification(cbCertification.getValue());
                BaseController.recruiterJob.setStatus(cbStatus.getValue());

                BaseController.returnTo.add("createjob");
                parentController.setContent("createjob2");
            }
        });
    }

    /**
     * Create job stage 2
     */
    public void createJob2() {
        ArrayList<String> categories = dbUtils.getCategories();
        categories.forEach(category -> {
            CheckBox checkBox = new CheckBox(category);
            checkBox.setId("cb" + category);
            if (categories.indexOf(category) % 2 == 0) {
                vbContentOdd.getChildren().add(checkBox);
            } else {
                vbContentEven.getChildren().add(checkBox);
            }
        });

        if (BaseController.recruiterJob.getCategories() != null) {
            for (String category : BaseController.recruiterJob.getCategories()) {
                CheckBox checkBox = (CheckBox) vbContentOdd.lookup("#cb" + category);
                if (checkBox == null) {
                    checkBox = (CheckBox) vbContentEven.lookup("#cb" + category);
                }
//                checkBox.setSelected(true);
            }
        }

        btnNext.setOnAction(event -> {
            ArrayList<String> selectedCategories = new ArrayList<>();
            for (Node node : vbContentOdd.getChildren()) {
                if (node instanceof CheckBox checkBox) {
                    if (checkBox.isSelected()) {
                        selectedCategories.add(checkBox.getText());
                    }
                }
            }
            for (Node node : vbContentEven.getChildren()) {
                if (node instanceof CheckBox checkBox) {
                    if (checkBox.isSelected()) {
                        selectedCategories.add(checkBox.getText());
                    }
                }
            }

            BaseController.recruiterJob.setCategories(selectedCategories);
            BaseController.returnTo.add("createjob2");
            parentController.setContent("createjob3");
        });
    }

    /**
     * Create job stage 3
     */
    public void createJob3() {
        ArrayList<String> skills = dbUtils.getSkills();
        skills.forEach(skill -> {
            CheckBox checkBox = new CheckBox(skill);
            checkBox.setId("cb" + skill);
            vbContent.getChildren().add(checkBox);
        });

        btnCreate.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Job Created");
            alert.setHeaderText("Job Created");
            alert.setContentText("Your job has been created.");
            alert.showAndWait();

            ArrayList<String> selectedSkills = new ArrayList<>();
            for (Node node : vbContent.getChildren()) {
                if (node instanceof CheckBox checkBox) {
                    if (checkBox.isSelected()) {
                        selectedSkills.add(checkBox.getText());
                    }
                }
            }

            BaseController.recruiterJob.setSkills(selectedSkills);
            completeCreation();
            BaseController.recruiterJob = new Job();
            BaseController.jobSearch = dbUtils.getJobs();
            parentController.removeReturnTo();
            parentController.setContent("findjobs");
        });
    }

    /**
     * Complete job creation
     */
    public void completeCreation() {
        dbUtils.createJob(BaseController.recruiterJob);
        int jobID = dbUtils.getJobID(BaseController.recruiterJob.getJob_title(), BaseController.recruiterJob.getJob_description(), BaseController.user.getCompany());
        int userID = dbUtils.getUserID(BaseController.user.getEmail(), BaseController.user.getRole());
        dbUtils.addSkillsJob(jobID, BaseController.recruiterJob.getSkills());
        dbUtils.addCategoriesJob(jobID, BaseController.recruiterJob.getCategories());
        dbUtils.createdJob(userID, jobID);
    }
}
