package com.example.groupa.controllers;

import com.example.groupa.DatabaseConnection;
import com.example.groupa.JSSApp;
import com.example.groupa.models.Job;
import com.example.groupa.models.User;
import com.example.groupa.utils.DatabaseUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * @author Kyle Sharp
 * <p>
 *     This class is the base controller for all other controllers.
 * </p>
 */
public class BaseController extends AnchorPane implements Initializable {
    @FXML
    private BorderPane bpContent;
    @FXML
    private AnchorPane application;

    private final DatabaseConnection dbConnection = new DatabaseConnection();
    private DatabaseUtils dbUtils = new DatabaseUtils(dbConnection.getConnection());
    public static boolean loggedIn = false;
    public static Job job = new Job();
    public static Job recruiterJob = new Job();
    public static User user = new User();
    public static String currentStage = "welcome";
    public static ArrayList<String> returnTo = new ArrayList<>();
    public static ArrayList<Job> jobSearch = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setContent("welcome");
    }

    /**
     * Sets the header of the application
     */
    public void setHeader() {
        HeaderController headerController = new HeaderController();
        headerController.setParentController(this);

        bpContent.setTop(headerController);
    }

    /**
     * Sets the sidebar of the application
     */
    public void setSidebar() {
        SidebarController sidebarController = new SidebarController(loggedIn);
        sidebarController.setParentController(this);

        bpContent.setLeft(sidebarController);
    }

    /**
     * Sets the content of the application
     * @param child The name to indicate which controller to load
     */
    public void setContent(String child) {
        bpContent.getChildren().clear();
        switch (child) {
            case "welcome" -> {
                WelcomeController welcomeController = new WelcomeController();
                welcomeController.setParentController(this);
                bpContent.setCenter(welcomeController);
                loggedIn = false;
                setStylesheet();
            }
            case "createaccount", "skillsStage", "register" -> {
                setHeader();
                bpContent.setLeft(null);
                CreateAccountController createAccountController = switch (child) {
                    case "createaccount" -> new CreateAccountController("loginStage");
                    case "skillsStage" -> new CreateAccountController("skillsStage");
                    case "register" -> new CreateAccountController("detailsStage");
                    default -> throw new IllegalStateException("Unexpected value: " + child);
                };

                createAccountController.setParentController(this);
                bpContent.setCenter(createAccountController);
            }
            case "findjobs" -> {
                setMenus();
                FindJobsController findJobsController = new FindJobsController(jobSearch);
                findJobsController.setParentController(this);
                bpContent.setCenter(findJobsController);
            }
            case "joblisting" -> {
                setMenus();
                JobListingController jobListingController = new JobListingController();
                jobListingController.setParentController(this);
                bpContent.setCenter(jobListingController);
            }
            case "createjob", "createjob2", "createjob3" -> {
                setMenus();
                CreateJobController createJobController = new CreateJobController(child);
                createJobController.setParentController(this);
                bpContent.setCenter(createJobController);
            }
            case "homepage" -> {
                setMenus();
                setStylesheet();
                HomePageController homepageController = new HomePageController(BaseController.user.getRole());
                homepageController.setParentController(this);
                bpContent.setCenter(homepageController);
            }
            case "accountdetails" -> {
                setMenus();
                AccountDetailsController accountDetailsController = new AccountDetailsController(BaseController.user.getRole());
                accountDetailsController.setParentController(this);
                bpContent.setCenter(accountDetailsController);
            }
            case "advancedsearch" -> {
                setMenus();
                AdvancedSearchController advancedSearchController = new AdvancedSearchController();
                advancedSearchController.setParentController(this);
                bpContent.setCenter(advancedSearchController);
            }
        }
    }

    /**
     * Sets the stylesheet of the application
     */
    public void setStylesheet() {
        application.getStylesheets().clear();
        application.getStylesheets().add(loggedIn
                ? Objects.requireNonNull(JSSApp.class.getResource("css/" + user.getRole().toLowerCase() + ".css")).toExternalForm()
                : Objects.requireNonNull(JSSApp.class.getResource("css/guest.css")).toExternalForm());
    }

    /**
     * Sets the menus of the application
     */
    public void setMenus() {
        setHeader();
        setSidebar();
    }

    /**
     * Back button functionality
     */
    public void ReturnPage() {
        String temp = returnTo.get(returnTo.size() - 1);
        returnTo.remove(returnTo.size() - 1);
        setContent(temp);
    }

    public void removeReturnTo() {
        returnTo = new ArrayList<>();
    }

    public static User getUser() {
        return user;
    }

    public static Job getJob() {
        return job;
    }
}
