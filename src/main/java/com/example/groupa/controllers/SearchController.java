package com.example.groupa.controllers;

import com.example.groupa.DatabaseConnection;
import com.example.groupa.utils.DatabaseUtils;

/**
 * @author Kyle Sharp
 * <p>
 *     This class is the controller for the search functionality
 * </p>
 *
 */
public class SearchController implements Controller {
    private String searchQuery;
    private String searchCategory;

    private BaseController parentController;
    private final DatabaseConnection dbConnection = new DatabaseConnection();
    private DatabaseUtils dbUtils = new DatabaseUtils(dbConnection.getConnection());

    public SearchController(String query, String category) {
        this.searchQuery = query;
        this.searchCategory = category.toLowerCase();
    }

    /**
     * This method is used to search the database for a job based on the search query and category
     */
    public void search() {
        BaseController.jobSearch = dbUtils.searchJob(searchQuery, searchCategory);
        parentController.setContent("findjobs");
    }

    @Override
    public void setParentController(BaseController parentController) {
        this.parentController = parentController;
    }
}
