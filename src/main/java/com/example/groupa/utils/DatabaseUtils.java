package com.example.groupa.utils;

import com.example.groupa.controllers.BaseController;
import com.example.groupa.models.Job;
import com.example.groupa.models.User;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * @author Kyle Sharp
 * <p>
 * This class is used to handle all database calls.
 * All our stored procedures are called from here.
 * Each stored procedure has a method that calls it.
 * </p>
 */
public class DatabaseUtils {
    private final Connection connection;

    public DatabaseUtils(Connection connection) {
        this.connection = connection;
    }

    /**
     * This method checks if a user exists in the DB that matches someone attempting to register
     * 
     * @param email The email of the user attempting to register
     * @param role The role of the user attempting to register
     * @return True if the user exists, false if they do not
     */
    public boolean checkRegister(String email, String role) {
        String query = "{call check_register(?,?)}";
        boolean emailCheck = false;

        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setString(1, email);
            callableStatement.setString(2, role.toLowerCase());
            ResultSet resultSet = callableStatement.executeQuery();

            if (resultSet.next()) {
                emailCheck = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return emailCheck;
    }

    /**
     * This method checks if a company exists in the DB
     *
     * @param company The company name
     * @return True if the company exists, false if they do not
     */
    public boolean checkCompanies(String company) {
        String query = "{call check_companies(?)}";
        boolean companyCheck = false;
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setString(1, company);
            ResultSet resultSet = callableStatement.executeQuery();

            if (resultSet.next()) {
                companyCheck = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return !companyCheck;
    }

    /**
     * This method registers the user in the DB
     *
     * @param user The user object
     */
    public void userRegister(User user) {
        String query = "{call user_register(?, ?, ?, ?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setString(1, user.getEmail());
            callableStatement.setString(2, user.getPassword());
            callableStatement.setString(3, user.getfName());
            callableStatement.setString(4, user.getlName());
            callableStatement.setString(5, user.getRole().toLowerCase());
            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method updates user certification in the DB
     *
     * @param user_id The user id
     * @param certification The certification
     */
    public void update_certification(int user_id, String certification) {
        String query = "{call update_certification(?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setInt(1, user_id);
            callableStatement.setString(2, certification);
            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method updates user location in the DB
     *
     * @param user_id The user id
     * @param location The location
     */
    public void update_location(int user_id, String location) {
        String query = "call update_location(?, ?)";
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setInt(1, user_id);
            callableStatement.setString(2, location);
            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method updates user bio in the DB
     *
     * @param user_id The user id
     * @param biography The biography
     */
    public void update_biography(int user_id, String biography) {
        String query = "call update_biography(?, ?)";
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setInt(1, user_id);
            callableStatement.setString(2, biography);
            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method updates user company in the DB
     *
     * @param user_id The user id
     * @param company The company
     */
    public void update_company(int user_id, String company) {
        String query = "call update_company(?, ?)";
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setInt(1, user_id);
            callableStatement.setString(2, company);
            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method adds skills to the user
     *
     * @param user_id The user id
     * @param skills Skills to add
     */
    public void addSkillsUser(int user_id, ArrayList<String> skills) {
        String query = "{call add_skills_user(?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setInt(1, user_id);
            for (String skill : skills) {
                callableStatement.setString(2, skill);
                callableStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method adds a company to the DB
     *
     * @param company The company name
     */
    public void addCompany(String company) {
        String query = "{call add_company(?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setString(1, company);
            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method creates a job in the DB
     *
     * @param job The job object
     */
    public void createJob(Job job) {
        String query = "{call add_job(?, ?, ?, ?, ?, ?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setString(1, job.getJob_title());
            callableStatement.setString(2, job.getJob_description());
            callableStatement.setString(3, job.getJob_salary());
            callableStatement.setString(4, job.getJob_location());
            callableStatement.setString(5, job.getStatus());
            callableStatement.setString(6, BaseController.user.getCompany());
            callableStatement.setString(7, job.getCertification());
            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method pairs a user with the job they created
     *
     * @param jobID  The job id
     * @param userID The user id
     */
    public void createdJob(int jobID, int userID) {
        String query = "{call created_job(?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setInt(1, jobID);
            callableStatement.setInt(2, userID);
            callableStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method adds skills to a job
     *
     * @param job_id The job id
     * @param skills Skills to add
     */
    public void addSkillsJob(int job_id, ArrayList<String> skills) {
        String query = "{call add_job_skills(?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setInt(1, job_id);
            for (String skill : skills) {
                callableStatement.setString(2, skill);
                callableStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method adds categories to a job
     *
     * @param job_id The job id
     * @param categories Categories to add
     */
    public void addCategoriesJob(int job_id, ArrayList<String> categories) {
        String query = "{call add_job_categories(?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setInt(1, job_id);
            for (String category : categories) {
                callableStatement.setString(2, category);
                callableStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method logs in a user
     *
     * @param email The email
     * @param password The password
     * @param role The role
     * @return True if the user is logged in, false otherwise
     */
    public boolean login(String email, String password, String role) {
        String query = "{call user_login(?, ?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setString(1, email);
            callableStatement.setString(2, password);
            callableStatement.setString(3, role);
            ResultSet resultSet = callableStatement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * This method gets skills from the DB
     *
     * @return The skills
     */
    public ArrayList<String> getSkills() {
        String query = "{call get_all_skills()}";
        ArrayList<String> skills = new ArrayList<>();
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            ResultSet resultSet = callableStatement.executeQuery();

            while (resultSet.next()) {
                skills.add(resultSet.getString("skill_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return skills;
    }

    /**
     * This method gets categories from the DB
     *
     * @return The categories
     */
    public ArrayList<String> getCategories() {
        String query = "{call get_all_categories()}";
        ArrayList<String> categories = new ArrayList<>();
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            ResultSet resultSet = callableStatement.executeQuery();

            while (resultSet.next()) {
                categories.add(resultSet.getString("category_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    /**
     * This method gets jobs from the DB
     *
     * @return The jobs
     */
    public ArrayList<Job> getJobs() {
        String query = "{call get_all_jobs()}";
        ArrayList<Job> jobs = new ArrayList<>();
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            ResultSet resultSet = callableStatement.executeQuery();

            while (resultSet.next()) {
                Job job = new Job();
                job.setJob_title(resultSet.getString("job_title"));
                job.setJob_description(resultSet.getString("job_description"));
                job.setJob_salary(String.valueOf(resultSet.getInt("job_salary")));
                job.setJob_location(String.valueOf(resultSet.getString("job_location")));
                job.setJob_date_created(resultSet.getString("job_date_created"));
                job.setJob_date_app_close(resultSet.getString("job_date_app_close"));
                job.setStatus(resultSet.getString("status_type"));
                job.setCompany(resultSet.getString("company_name"));
                job.setCertification(resultSet.getString("certification_name"));
                job.setSkills(getJobSkills(resultSet.getInt("job_id")));
                jobs.add(job);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jobs;
    }

    /**
     * This method gets the skills for a job
     *
     * @param job_id The job id
     * @return The skills
     */
    public ArrayList<String> getJobSkills(int job_id) {
        String query = "{call get_job_skills(?)}";
        ArrayList<String> skills = new ArrayList<>();
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setInt(1, job_id);
            ResultSet resultSet = callableStatement.executeQuery();

            while (resultSet.next()) {
                skills.add(resultSet.getString("skill_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return skills;
    }

    /**
     * This method gets the categories for a job
     *
     * @param job_id The job id
     * @return The categories
     */
    public ArrayList<String> getJobCategories(int job_id) {
        String query = "{call get_job_categories(?)}";
        ArrayList<String> categories = new ArrayList<>();
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setInt(1, job_id);
            ResultSet resultSet = callableStatement.executeQuery();

            while (resultSet.next()) {
                categories.add(resultSet.getString("category_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    /**
     * This method get certifications from the DB
     *
     * @return The certifications
     */
    public ArrayList<String> getCertifications() {
        String query = "{call get_all_certifications()}";
        ArrayList<String> certifications = new ArrayList<>();
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            ResultSet resultSet = callableStatement.executeQuery();

            while (resultSet.next()) {
                certifications.add(resultSet.getString("certification_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return certifications;
    }

    /**
     * This method gets the user id
     *
     * @param email The email
     * @param role The role
     * @return The user id
     */
    public int getUserID(String email, String role) {
        String query = "{call get_user_id(?, ?)}";
        int userID = 0;
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setString(1, email);
            callableStatement.setString(2, role);
            ResultSet resultSet = callableStatement.executeQuery();

            if (resultSet.next()) {
                userID = Integer.parseInt(resultSet.getString("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userID;
    }

    /**
     * This method gets the job id
     *
     * @param title The job title
     * @param description The job description
     * @param company The company
     * @return The job id
     */
    public int getJobID(String title, String description, String company) {
        String query = "{call get_job_id(?, ?, ?)}";
        int jobID = 0;
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setString(1, title);
            callableStatement.setString(2, description);
            callableStatement.setString(3, company);
            ResultSet resultSet = callableStatement.executeQuery();

            if (resultSet.next()) {
                jobID = Integer.parseInt(resultSet.getString("job_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jobID;
    }

    /**
     * This method returns a user (seeker)
     *
     * @param id The user id
     * @return The user
     */
    public User getSeeker(int id) {
        String query = "{call get_seeker(?)}";
        User user = new User();
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setInt(1, id);
            ResultSet resultSet = callableStatement.executeQuery();

            if (resultSet.next()) {
                user.setfName(resultSet.getString("user_first_name"));
                user.setlName(resultSet.getString("user_last_name"));
                user.setEmail(resultSet.getString("email_address"));
                user.setRole(resultSet.getString("role_name"));
                user.setLocation(resultSet.getString("user_location"));
                user.setBio(resultSet.getString("user_biography"));
                user.setCertification(resultSet.getString("certification_name"));
                user.setPassword(resultSet.getString("user_password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * This method returns a user (recruiter)
     *
     * @param id The user id
     * @return The user
     */
    public User getRecruiter(int id) {
        String query = "{call get_recruiter(?)}";
        User user = new User();
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setInt(1, id);
            ResultSet resultSet = callableStatement.executeQuery();

            if (resultSet.next()) {
                user.setfName(resultSet.getString("user_first_name"));
                user.setlName(resultSet.getString("user_last_name"));
                user.setEmail(resultSet.getString("email_address"));
                user.setRole(resultSet.getString("role_name"));
                user.setLocation(resultSet.getString("user_location"));
                user.setCompany(resultSet.getString("company_name"));
                user.setBio(resultSet.getString("user_biography"));
                user.setPassword(resultSet.getString("user_password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * this method returns the user skills
     *
     * @param user_id The user id
     * @return The user skills
     */
    public ArrayList<String> getUserSkills(int user_id) {
        String query = "{call get_user_skills(?)}";
        ArrayList<String> skills = new ArrayList<>();
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setInt(1, user_id);
            ResultSet resultSet = callableStatement.executeQuery();

            while (resultSet.next()) {
                skills.add(resultSet.getString("skill_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return skills;
    }

    /**
     * This method returns state options
     *
     * @return The state options
     */
    public ArrayList<String> getStates() {
        String query = "{call get_states()}";
        ArrayList<String> states = new ArrayList<>();
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            ResultSet resultSet = callableStatement.executeQuery();

            while (resultSet.next()) {
                states.add(resultSet.getString("status_type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return states;
    }

    /**
     * This method applies a user for a job
     *
     * @param user_id The user id
     * @param job_id The job id
     */
    public void applyForJob(int user_id, int job_id) {
        String query = "{call apply_for_job(?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setInt(1, user_id);
            callableStatement.setInt(2, job_id);
            callableStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This checks if a user has applied for a job
     *
     * @param user_id The user id
     * @param job_id The job id
     * @return True if the user has applied for the job, false otherwise
     */
    public boolean hasApplied(int user_id, int job_id) {
        String query = "{call has_applied(?, ?)}";
        boolean hasApplied = false;
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setInt(1, user_id);
            callableStatement.setInt(2, job_id);
            ResultSet resultSet = callableStatement.executeQuery();

            if (resultSet.next()) {
                hasApplied = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hasApplied;
    }

    /**
     * This method cancels a job application
     * 
     * @param user_id The user id 
     * @param job_id The job id
     */
    public void cancelApplication(int user_id, int job_id) {
        String query = "{call cancel_application(?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setInt(1, user_id);
            callableStatement.setInt(2, job_id);
            callableStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method updates seeker details
     * 
     * @param user The user
     * @param userID The user id
     */
    public void updateSeeker(User user, int userID) {
        String query = "{call update_seeker(?, ?, ?, ?, ?, ?, ?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setInt(1, userID);
            callableStatement.setString(2, user.getfName());
            callableStatement.setString(3, user.getlName());
            callableStatement.setString(4, user.getEmail());
            callableStatement.setString(5, user.getLocation());
            callableStatement.setString(6, user.getPassword());
            callableStatement.setString(7, user.getBio());
            callableStatement.setString(8, user.getCertification());
            callableStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method updates recruiter details
     *
     * @param user The user
     * @param userID The user id
     */
    public void updateRecruiter(User user, int userID) {
        String query = "{call update_recruiter(?, ?, ?, ?, ?, ?, ?, ?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setInt(1, userID);
            callableStatement.setString(2, user.getfName());
            callableStatement.setString(3, user.getlName());
            callableStatement.setString(4, user.getEmail());
            callableStatement.setString(5, user.getLocation());
            callableStatement.setString(6, user.getPassword());
            callableStatement.setString(7, user.getBio());
            callableStatement.setString(8, user.getCompany());

            callableStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method deletes a seeker
     * 
     * @param userID The user id
     */
    public void deleteSeeker(int userID) {
        String query = "{call delete_seeker(?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setInt(1, userID);
            callableStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method deletes a recruiter
     *
     * @param userID The user id
     */
    public void deleteRecruiter(int userID) {
        String query = "{call delete_recruiter(?)}";
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setInt(1, userID);
            callableStatement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method searches for a job
     * 
     * @param searchQuery The search query
     * @param searchCategory The search category
     * @return The jobs
     */
    public ArrayList<Job> searchJob(String searchQuery, String searchCategory) {
        String regex = "\\b[phdPHD]{3}\\b";

        if (searchQuery.matches(regex)) {
            searchQuery = "Doctorate";
        }

        String query = switch (searchCategory) {
            case "title" -> "{call search_job_title(?)}";
            case "company" -> "{call search_job_company(?)}";
            case "salary" -> "{call search_job_salary(?)}";
            case "location" -> "{call search_job_location(?)}";
            case "skills" -> "{call search_job_skills(?)}";
            case "certification" -> "{call search_job_certification(?)}";
            default -> throw new RuntimeException("Invalid search category");
        };

        ArrayList<Job> jobs = new ArrayList<>();
        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            if (searchCategory.equalsIgnoreCase("salary")) {
                callableStatement.setInt(1, Integer.parseInt(searchQuery));
            } else {
                callableStatement.setString(1, searchQuery);
            }

            jobs = search(new ArrayList<>(), callableStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jobs;
    }

    /**
     * This method searches for a job using multiple search parameters
     *
     * @param title The title
     * @param company The company
     * @param Salary The salary
     * @param location The location
     * @param skill The skill
     * @param certification The certification
     * @return The jobs
     */
    public ArrayList<Job> advancedSearch(String title, String company, int Salary, String location, String skill, String certification) {
        ArrayList<Job> jobs = new ArrayList<>();

        String query;
        if (skill.isEmpty() && certification.isEmpty()) {
            query = "{call advanced_search2(?, ?, ?, ?)}";
        } else if (skill.isEmpty()) {
            query = "{call advanced_search3(?, ?, ?, ?, ?)}";
        } else if (certification.isEmpty()) {
            query = "{call advanced_search4(?, ?, ?, ?, ?)}";
        } else {
            query = "{call advanced_search(?, ?, ?, ?, ?, ?)}";
        }

        try {
            CallableStatement callableStatement = connection.prepareCall(query);
            callableStatement.setString(1, title);
            callableStatement.setString(2, company);
            callableStatement.setInt(3, Salary);
            callableStatement.setString(4, location);

            if (skill.isEmpty() && certification.isEmpty()) {
                // do nothing
            } else if (skill.isEmpty()) {
                callableStatement.setString(5, certification);
            } else if (certification.isEmpty()) {
                callableStatement.setString(5, skill);
            } else {
                callableStatement.setString(5, skill);
                callableStatement.setString(6, certification);
            }

            jobs = search(new ArrayList<>(), callableStatement);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jobs;
    }

    /**
     * Extracted method for searching
     *
     * @param jobs The jobs
     * @param callableStatement The callable statement
     * @return The jobs
     */
    private ArrayList<Job> search(ArrayList<Job> jobs, CallableStatement callableStatement) {
        ResultSet resultSet;

        try {
            resultSet = callableStatement.executeQuery();

            while (resultSet.next()) {
                Job job = new Job();
                job.setJob_title(resultSet.getString("job_title"));
                job.setJob_description(resultSet.getString("job_description"));
                job.setJob_salary(String.valueOf(resultSet.getInt("job_salary")));
                job.setJob_location(resultSet.getString("job_location"));
                job.setStatus(resultSet.getString("status_type"));
                job.setCompany(resultSet.getString("company_name"));
                job.setCertification(resultSet.getString("certification_name"));
                job.setSkills(getJobSkills(resultSet.getInt("job_id")));
                job.setCategories(getJobCategories(resultSet.getInt("job_id")));
                jobs.add(job);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return jobs;
    }
}
