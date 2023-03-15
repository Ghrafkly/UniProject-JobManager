package com.example.groupa.utils;

import com.example.groupa.DatabaseConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class InsertDataUtils {
    LoaderUtils loaderUtils = new LoaderUtils();
    private final Connection connection;

    public InsertDataUtils() {
        DatabaseConnection databaseConnection = new DatabaseConnection();
        this.connection = databaseConnection.getConnection();
    }

    public void insertCategories(List<String[]> categories) {
        try (CallableStatement statement = connection.prepareCall("{call insert_category(?)}")) {
            for (String[] category : categories) {
                statement.setString(1, category[0]);
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertCertifications(List<String[]> certifications) {
        try (CallableStatement statement = connection.prepareCall("{call insert_certification(?)}")) {
            for (String[] certification : certifications) {
                statement.setString(1, certification[0]);
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertCompanies(List<String[]> companies) {
        try (CallableStatement statement = connection.prepareCall("{call add_company(?)}")) {
            for (String[] company : companies) {
                statement.setString(1, company[0]);
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertMessageType(List<String[]> messageTypes) {
        try (CallableStatement statement = connection.prepareCall("{call insert_message_type(?)}")) {
            for (String[] messageType : messageTypes) {
                statement.setString(1, messageType[0]);
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertSkills(List<String[]> skills) {
        try (CallableStatement statement = connection.prepareCall("{call insert_skills(?)}")) {
            for (String[] skill : skills) {
                statement.setString(1, skill[0]);
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertRoles(List<String[]> roles) {
        String query = "call insert_roles(?)";
        try (CallableStatement statement = connection.prepareCall(query)) {
            for (String[] role : roles) {
                statement.setString(1, role[0]);
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertState(List<String[]> states) {
        String query = "call insert_state(?)";
        try (CallableStatement statement = connection.prepareCall(query)) {
            for (String[] state : states) {
                statement.setString(1, state[0]);
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertSeeker(List<String[]> seekers) {
        String query = "{call insert_seeker(?, ?, ?, ?, ?, ?)}";
        try (CallableStatement statement = connection.prepareCall(query)) {
            for (String[] seeker : seekers) {
                statement.setString(1, seeker[0]);
                statement.setString(2, seeker[1]);
                statement.setString(3, seeker[2]);
                statement.setString(4, seeker[3]);
                statement.setString(5, seeker[4]);
                statement.setString(6, seeker[5]);
                statement.execute();

                System.out.println(statement);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertRecruiter(List<String[]> recruiters) {
        String query = "{call insert_recruiter(?, ?, ?, ?, ?, ?)}";
        try (CallableStatement statement = connection.prepareCall(query)) {
            for (String[] recruiter : recruiters) {
                statement.setString(1, recruiter[0]);
                statement.setString(2, recruiter[1]);
                statement.setString(3, recruiter[2]);
                statement.setString(4, recruiter[3]);
                statement.setString(5, recruiter[4]);
                statement.setString(6, recruiter[5]);
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertJobs(List<String[]> jobs) {
        String query = "{call insert_job(?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement statement = connection.prepareCall(query)) {
            for (String[] job : jobs) {
                statement.setString(1, job[0]);
                statement.setString(2, job[1]);
                statement.setInt(3, Integer.parseInt(job[2]));
                statement.setString(4, job[3]);
                statement.setInt(5, Integer.parseInt(job[6]));
                statement.setInt(6, Integer.parseInt(job[7]));
                statement.setInt(7, Integer.parseInt(job[8]));
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertJobCategories(List<String[]> jobCategories) {
        String query = "{call insert_job_category(?, ?)}";
        try (CallableStatement statement = connection.prepareCall(query)) {
            for (String[] jobCategory : jobCategories) {
                statement.setString(1, jobCategory[0]);
                statement.setString(2, jobCategory[1]);
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertJobSkills(List<String[]> jobSkills) {
        try (CallableStatement statement = connection.prepareCall("{call insert_job_skills(?, ?)}")) {
            for (String[] skill : jobSkills) {
                statement.setString(1, skill[0]);
                statement.setString(2, skill[1]);
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteAll() {
        try (CallableStatement statement = connection.prepareCall("{call delete_all()}")) {
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void resetIncrements() {
        try (CallableStatement statement = connection.prepareCall("{call reset_increments()}")) {
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkForData() {
        try (CallableStatement statement = connection.prepareCall("{call check_for_data()}")) {
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            resultSet.next();
            return resultSet.getBoolean(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void readInAndLoadFiles() {
        insertState(loaderUtils.CSVReader("state"));
        insertCategories(loaderUtils.CSVReader("categories"));
        insertCertifications(loaderUtils.CSVReader("certification"));
        insertCompanies(loaderUtils.CSVReader("company"));
        insertMessageType(loaderUtils.CSVReader("message_type"));
        insertSkills(loaderUtils.CSVReader("skills"));
        insertRoles(loaderUtils.CSVReader("roles"));

        insertJobs(loaderUtils.CSVReader("jobs"));
        insertJobCategories(loaderUtils.CSVReader("job_categories"));
        insertJobSkills(loaderUtils.CSVReader("job_skills"));
        insertSeeker(loaderUtils.CSVReader("seekers"));
        insertRecruiter(loaderUtils.CSVReader("recruiters"));
    }
}
