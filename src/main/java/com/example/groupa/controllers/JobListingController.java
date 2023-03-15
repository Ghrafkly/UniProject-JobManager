package com.example.groupa.controllers;

import com.example.groupa.DatabaseConnection;
import com.example.groupa.JSSApp;
import com.example.groupa.models.Job;
import com.example.groupa.utils.DatabaseUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Kyle Sharp
 * <p>
 *     This class is the controller for the job listing page.
 * </p>
 *
 */
public class JobListingController extends AnchorPane implements Initializable, Controller {
    @FXML
    private Label lblTitle, lblCompany, lblLocation, lblSalary, lblCertification, lblBack;
    @FXML
    private TextArea taDescription, taSkills;
    @FXML
    private Button btnApply;

    private BaseController parentController;
    private final DatabaseConnection dbConnection = new DatabaseConnection();
    private DatabaseUtils dbUtils = new DatabaseUtils(dbConnection.getConnection());

    public JobListingController() {
        BaseController.currentStage = "joblisting";
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(JSSApp.class.getResource("fxml/joblisting.fxml"));

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
        Job job = BaseController.job;
        int jobID = dbUtils.getJobID(job.getJob_title(), job.getJob_description(), job.getCompany());

        lblTitle.setText(job.getJob_title());
        lblCompany.setText(job.getCompany());
        lblLocation.setText(job.getJob_location());
        lblSalary.setText(job.getJob_salary());
        lblCertification.setText(job.getCertification());
        taDescription.setText(job.getJob_description());

        if (job.getSkills().size() > 0) {
            job.getSkills().forEach(skill -> taSkills.appendText(skill + "\n"));
        } else {
            taSkills.setText("No skills required");
        }

        if (BaseController.user.getRole() != null) {
            loggedIn(jobID, BaseController.user.getRole());
        } else {
            btnApply.setText("Register to apply");
            btnApply.setOnAction(event -> {
                parentController.setContent("createaccount");
            });
        }

        lblBack.setOnMouseClicked(event -> {
            parentController.ReturnPage();
        });
    }

    /**
     * This method is called when if user is logged in.
     *
     * @param jobID The job ID of the job being viewed.
     * @param user The user that is logged in.
     */
    public void loggedIn(int jobID, String user) {
        int userID = dbUtils.getUserID(BaseController.user.getEmail(), BaseController.user.getRole());

        if (user.equalsIgnoreCase("recruiter")) {
            btnApply.setVisible(false);
            btnApply.setDisable(true);
        } else if (user.equalsIgnoreCase("seeker")) {
            btnApply.setId("apply");
            if (dbUtils.hasApplied(userID, jobID)) {
                btnApply.setText("Cancel");
                btnApply.setId("cancel");
            }
            btnApply.setOnAction(event -> {
                if (btnApply.getText().equals("Apply Now")) {
                    dbUtils.applyForJob(userID, jobID);
                    btnApply.setText("Cancel application");
                    btnApply.setId("cancel");
                } else {
                    dbUtils.cancelApplication(userID, jobID);
                    btnApply.setText("Apply Now");
                    btnApply.setId("apply");
                }
            });
        }
    }
}
