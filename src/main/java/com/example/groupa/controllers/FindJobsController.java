package com.example.groupa.controllers;

import com.example.groupa.DatabaseConnection;
import com.example.groupa.JSSApp;
import com.example.groupa.models.Job;
import com.example.groupa.utils.DatabaseUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * @author Kyle Sharp
 * <p>
 *     This class is the controller to list jobs in the application from the DB
 * </p>
 */
public class FindJobsController extends AnchorPane implements Initializable, Controller {
    @FXML
    private TableView<Job> tbvJobs;

    @FXML
    private TableColumn<Job, String> tcTitle, tcLocation, tcSalary, tcCompany;

    private ObservableList<Job> jobs;
    private BaseController parentController;
    private final DatabaseConnection dbConnection = new DatabaseConnection();
    private DatabaseUtils dbUtils = new DatabaseUtils(dbConnection.getConnection());

    public FindJobsController(ArrayList<Job> jobs) {
        this.jobs = FXCollections.observableArrayList(jobs);
        BaseController.currentStage = "findjobs";
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(JSSApp.class.getResource("fxml/findJobs.fxml"));

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
        tcTitle.setCellValueFactory(new PropertyValueFactory<>("job_title"));
        tcCompany.setCellValueFactory(new PropertyValueFactory<>("company"));
        tcSalary.setCellValueFactory(new PropertyValueFactory<>("job_salary"));
        tcLocation.setCellValueFactory(new PropertyValueFactory<>("job_location"));

        setTableData();

        tbvJobs.setRowFactory(tv -> {
            TableRow<Job> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    Job rowData = row.getItem();
                    selectJob(rowData);
                }
            });
            return row;
        });
    }

    /**
     * This method allows a user to select a job from the table and view the job details
     * @param job The job that the user has selected
     */
    public void selectJob(Job job) {
        BaseController.job = job;
        BaseController.returnTo.add("findjobs");
        parentController.setContent("joblisting");
    }

    /**
     * This method sets the table data to the jobs that are in the DB, or from search parameters
     */
    public void setTableData() {
        tbvJobs.getItems().clear();
        ObservableList<Job> data = FXCollections.observableArrayList(jobs);
        tbvJobs.setItems(data);
    }
}
