use monjss;

# Delete data from all tables
DELETE FROM job_categories;
DELETE FROM job_skills;
DELETE FROM message;
DELETE FROM message_content;
DELETE FROM user_experience;
DELETE FROM user_skills;
DELETE FROM status;

DELETE FROM jobs;
DELETE FROM users;
DELETE FROM company;

DELETE FROM categories;
DELETE FROM message_type;
DELETE FROM certification;
DELETE FROM state;
DELETE FROM skills;
DELETE FROM roles;

# Stored Procedures

# -----------------------------
# Registers a new user (Seeker)
# -----------------------------
DROP PROCEDURE IF EXISTS `user_register`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `user_register`(IN email VARCHAR(50), IN password VARCHAR(45), IN first_name VARCHAR(50), IN last_name VARCHAR(50), IN role_name VARCHAR(50))
BEGIN
    INSERT INTO users (email_address, user_password, user_first_name, user_last_name, role_id)
    VALUES (email, password, first_name, last_name,
            (SELECT role_id
             FROM roles
             WHERE roles.role_name = role_name));
END ;;


# ----------------------------
# Checks if the login is valid
# ----------------------------
DROP PROCEDURE IF EXISTS `user_login`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `user_login`(IN email VARCHAR(50), password VARCHAR(45), IN role_name VARCHAR(45))
BEGIN
    SELECT *
    FROM users
    WHERE users.email_address = email
      AND users.user_password = password
      AND role_id = (SELECT roles.role_id FROM roles WHERE roles.role_name = role_name);
END ;;
DELIMITER ;


# --------------
# Deletes a user
# --------------
DROP PROCEDURE IF EXISTS `user_delete`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `user_delete`(IN email VARCHAR(50), IN role_name VARCHAR(50))
BEGIN
    DELETE FROM users
    WHERE email_address = email
      AND role_id = (SELECT role_id FROM roles WHERE roles.role_name = role_name);
END ;;


# -----------------------------------------
# Checks if the email is already registered
# -----------------------------------------
DROP PROCEDURE IF EXISTS `check_register`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `check_register`(IN email VARCHAR(50), IN role VARCHAR(50))
BEGIN
    SELECT * FROM users
                      INNER JOIN roles ON users.role_id = roles.role_id
    WHERE users.email_address = email
      AND role_name = role;
END ;;


# -------------
# Get companies
# -------------
DROP PROCEDURE IF EXISTS `check_companies`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `check_companies`(IN name VARCHAR(50))
BEGIN
    SELECT * FROM company
    WHERE company_name = name;
END ;;


# ---------------------
# Update certifications
# ---------------------
DROP PROCEDURE IF EXISTS `update_certification`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_certification`(IN userID INT, IN cert VARCHAR(50))
BEGIN
    UPDATE users
    SET users.certification_id =
            (SELECT certification_id
             FROM certification
             WHERE certification_name = cert)
    WHERE users.user_id = userID;
END ;;


# ----------------------
# Update Location
# ----------------------
DROP PROCEDURE IF EXISTS `update_location`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_location`(IN user int, IN location VARCHAR(50))
BEGIN
    UPDATE users
    SET user_location = location
    WHERE user_id = user;
END ;;


# ----------------------
# Update Biography
# ----------------------
DROP PROCEDURE IF EXISTS `update_biography`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_biography`(IN user int, IN biography VARCHAR(50))
BEGIN
    UPDATE users
    SET user_biography = biography
    WHERE user_id = user;
END ;;


# ---------------------
# Update Company
# ---------------------
DROP PROCEDURE IF EXISTS `update_company`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_company`(IN user int, IN company VARCHAR(50))
BEGIN
    UPDATE users
    SET company_id = (SELECT company_id FROM company WHERE company_name = company)
    WHERE user_id = user;
END ;;
DELIMITER ;


# -------------------
# Add skills for user
# -------------------
DROP PROCEDURE IF EXISTS `add_skills_user`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `add_skills_user`(IN user_id INT, IN skill VARCHAR(50))
BEGIN
    INSERT INTO user_skills (user_id, skill_id)
    VALUES (user_id,
            (SELECT skill_id
             FROM skills
             WHERE skills.skill_name = skill));
END ;;


# ------------------
# Add skills for job
# ------------------
DROP PROCEDURE IF EXISTS `add_skills_job`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `add_skills_job`(IN job_id INT, IN skill VARCHAR(50))
BEGIN
    INSERT INTO job_skills (job_id, skill_id)
    VALUES (job_id,
            (SELECT skill_id
             FROM skills
             WHERE skills.skill_name = skill));
END ;;


# ----------------
# Add job category
# ----------------
DROP PROCEDURE IF EXISTS `add_job_category`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `add_job_category`(IN job_id INT, IN category VARCHAR(50))
BEGIN
    INSERT INTO job_categories (job_id, category_id)
    VALUES (job_id,
            (SELECT category_id
             FROM categories
             WHERE categories.category_name = category));
END ;;


# -------
# Add job
# -------
DROP PROCEDURE IF EXISTS `add_job`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `add_job`(IN title VARCHAR(50), IN description VARCHAR(50), IN salary INT, IN location VARCHAR(50), IN status VARCHAR(50), IN companyName VARCHAR(50), IN cert VARCHAR(50))
BEGIN
    INSERT INTO jobs (job_title, job_description, job_salary, job_location, job_date_created, job_date_app_close, status_id, company_id, certification_id)
    VALUES (title, description, salary, location, DATE (NOW()), DATE (NOW()) + INTERVAL 1 MONTH,
            (SELECT status_id
             FROM state
             WHERE status_type = status),
            (SELECT company_id
             FROM company
             WHERE company_name = companyName),
            (SELECT certification_id
             FROM certification
             WHERE certification_name = cert));
END ;;


# --------------
# Add job skills
# --------------
DROP PROCEDURE IF EXISTS `add_job_skills`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `add_job_skills`(IN jobID INT, IN skill VARCHAR(50))
BEGIN
    INSERT INTO job_skills (job_id, skill_id)
    VALUES (jobID,
            (SELECT skill_id
             FROM skills
             WHERE skills.skill_name = skill));
END ;;


# ------------------
# Add job categories
# ------------------
DROP PROCEDURE IF EXISTS `add_job_categories`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `add_job_categories`(IN jobID INT, IN category VARCHAR(50))
BEGIN
    INSERT INTO job_categories (job_id, category_id)
    VALUES (jobID,
            (SELECT category_id
             FROM categories
             WHERE categories.category_name = category));
END ;;

# -----------
# Add Company
# -----------
DROP PROCEDURE IF EXISTS `add_company`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `add_company`(IN name VARCHAR(50))
BEGIN
    INSERT INTO company (company_name)
    VALUES (name);
END ;;


# ---------------
# Apply for a job
# ---------------
DROP PROCEDURE IF EXISTS `apply_for_job`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `apply_for_job`(IN userID INT, IN jobID INT)
BEGIN
    INSERT INTO status (user_id, job_id, status_id)
    VALUES (userID, jobID,
            (SELECT state.status_id
             FROM state
             WHERE state.status_type = 'applied'));
END ;;


# -------------
# Created a job
# -------------
DROP PROCEDURE IF EXISTS `created_job`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `created_job`(IN userID INT, IN jobID INT)
BEGIN
    INSERT INTO status (user_id, job_id, status_id)
    VALUES (userID, jobID,
            (SELECT state.status_id
             FROM state
             WHERE state.status_type = 'created'));
END ;;


# ---------------------
# Has applied for a job
# ---------------------
DROP PROCEDURE IF EXISTS `has_applied`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `has_applied`(IN userID INT, IN jobID INT)
BEGIN
    SELECT *
    FROM status
    WHERE user_id = userID AND job_id = jobID;
END ;;


# ------------------
# Cancel application
# ------------------
DROP PROCEDURE IF EXISTS `cancel_application`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `cancel_application`(IN userID INT, IN jobID INT)
BEGIN
    DELETE FROM status
    WHERE user_id = userID AND job_id = jobID;
END ;;


# -----------
# Get user id
# -----------
DROP PROCEDURE IF EXISTS `get_user_id`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_user_id`(IN email VARCHAR(50), IN role VARCHAR(50))
BEGIN
    SELECT user_id FROM users
                            INNER JOIN roles ON users.role_id = roles.role_id
    WHERE users.email_address = email
      AND role_name = role;
END ;;


# ----------
# Get job id
# ----------
DROP PROCEDURE IF EXISTS `get_job_id`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_job_id`(IN title VARCHAR(50), IN description VARCHAR(255), IN cname VARCHAR(50))
BEGIN
    SELECT job_id FROM jobs
                           INNER JOIN company ON jobs.company_id = company.company_id
    WHERE job_title = title
      AND job_description = description
      AND company_name = cname;
END ;;

SELECT * FROM jobs;


# ----------
# Get all Skills
# ----------
DROP PROCEDURE IF EXISTS `get_all_skills`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_all_skills`()
BEGIN
    SELECT * FROM skills;
END ;;


# ------------------
# Get all categories
# ------------------
DROP PROCEDURE IF EXISTS `get_all_categories`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_all_categories`()
BEGIN
    SELECT * FROM categories;
END ;;


# --------------
# Get job skills
# --------------
DROP PROCEDURE IF EXISTS `get_job_skills`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_job_skills`(IN job_id INT)
BEGIN
    SELECT skill_name FROM job_skills
                               INNER JOIN skills ON job_skills.skill_id = skills.skill_id
    WHERE job_skills.job_id = job_id;
END ;;


# ------------------
# Get job categories
# ------------------
DROP PROCEDURE IF EXISTS `get_job_categories`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_job_categories`(IN job_id INT)
BEGIN
    SELECT category_name FROM job_categories
                                  INNER JOIN categories ON job_categories.category_id = categories.category_id
    WHERE job_categories.job_id = job_id;
END ;;


# ---------------
# Get user skills
# ---------------
DROP PROCEDURE IF EXISTS `get_user_skills`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_user_skills`(IN user_id INT)
BEGIN
    SELECT skill_name FROM user_skills
                               INNER JOIN skills ON user_skills.skill_id = skills.skill_id
    WHERE user_skills.user_id = user_id;
END ;;


# ----------------------
# Get all certifications
# ----------------------
DROP PROCEDURE IF EXISTS `get_all_certifications`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_all_certifications`()
BEGIN
    SELECT * FROM certification;
END ;;


# -----------------
# Get all jobs
# -----------------
DROP PROCEDURE IF EXISTS `get_all_jobs`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_all_jobs`()
BEGIN
    SELECT * FROM jobs
                      INNER JOIN company ON jobs.company_id = company.company_id
                      INNER JOIN state ON jobs.status_id = state.status_id
                      INNer JOIN certification ON jobs.certification_id = certification.certification_id;
END ;;


# ----------
# Get Seeker
# ----------
DROP PROCEDURE IF EXISTS `get_seeker`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_seeker`(IN user_id INT)
BEGIN
    SELECT * FROM users
                      INNER JOIN roles ON users.role_id = roles.role_id
                      INNER JOIN certification ON users.certification_id = certification.certification_id
    WHERE users.user_id = user_id;
END ;;


# ----------
# Get Recruiter
# ----------
DROP PROCEDURE IF EXISTS `get_recruiter`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_recruiter`(IN user_id INT)
BEGIN
    SELECT * FROM users
                      INNER JOIN roles ON users.role_id = roles.role_id
                      INNER JOIN company ON users.company_id = company.company_id
    WHERE users.user_id = user_id;
END ;;


# -------
# Get Job
# -------
DROP PROCEDURE IF EXISTS `get_job`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_job`(IN jobID INT)
BEGIN
    SELECT jobs.*, skill_name, company_name
    FROM jobs
             INNER JOIN job_skills ON jobs.job_id = job_skills.job_id
             INNER JOIN skills ON job_skills.skill_id = skills.skill_id
             INNER JOIN company ON jobs.company_id = company.company_id
    WHERE jobs.job_id = jobID;
END ;;


# ----------------
# Get states
# ----------------
DROP PROCEDURE IF EXISTS `get_states`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_states`()
BEGIN
    SELECT status_type FROM state;
END ;;


# ----------------
# Get job status
# ----------------
DROP PROCEDURE IF EXISTS `get_job_status`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `get_job_status`(IN job_id INT)
BEGIN
    SELECT status_type FROM jobs
                                INNER JOIN state ON jobs.status_id = state.status_id
    WHERE jobs.job_id = job_id;
END ;;


# -------------
# Update Seeker
# -------------
DROP PROCEDURE IF EXISTS `update_seeker`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_seeker`(IN userID INT, IN fName VARCHAR(50), IN lName VARCHAR(50), IN email VARCHAR(50), IN location VARCHAR(50), IN pwd VARCHAR(50), IN bio VARCHAR(255), IN cert VARCHAR(50))
BEGIN
    UPDATE users
    SET user_first_name = fName,
        user_first_name = lName,
        email_address = email,
        user_location = location,
        user_password = pwd,
        user_biography = bio,
        certification_id =
            (SELECT certification_id
             FROM certification
             WHERE certification_name = cert)
    WHERE user_id = userID;
END ;;


# ---------------
# Update Recruiter
# ---------------
DROP PROCEDURE IF EXISTS `update_recruiter`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_recruiter`(IN userID INT, IN fName VARCHAR(50), IN lName VARCHAR(50), IN email VARCHAR(50), IN location VARCHAR(50), IN pwd VARCHAR(50), IN bio VARCHAR(255), IN comp VARCHAR(50))
BEGIN
    UPDATE users
    SET user_first_name = fName,
        user_first_name = lName,
        email_address = email,
        user_location = location,
        user_password = pwd,
        user_biography = bio,
        company_id =
            (SELECT company_id
             FROM company
             WHERE company_name = comp)
    WHERE user_id = userID;
END ;;

# ---------------------
# Update certifications
# ---------------------
DROP PROCEDURE IF EXISTS `update_certification`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_certification`(IN userID INT, IN cert VARCHAR(50))
BEGIN
    UPDATE users
    SET users.certification_id =
            (SELECT certification_id
             FROM certification
             WHERE certification_name = cert)
    WHERE users.user_id = userID;
END ;;


# ----------------------
# Update Location
# ----------------------
DROP PROCEDURE IF EXISTS `update_location`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_location`(IN user int, IN location VARCHAR(50))
BEGIN
    UPDATE users
    SET user_location = location
    WHERE user_id = user;
END ;;

# ----------------------
# Update first name
# ----------------------
DROP PROCEDURE IF EXISTS `update_fName`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_fName`(IN user int, IN fName VARCHAR(50))
BEGIN
    UPDATE users
    SET user_first_name = fName
    WHERE user_id = user;
END ;;

# ----------------------
# Update last name
# ----------------------
DROP PROCEDURE IF EXISTS `update_lName`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_lName`(IN user int, IN lName VARCHAR(50))
BEGIN
    UPDATE users
    SET user_last_name = lName
    WHERE user_id = user;
END ;;

# ----------------------
# Update Password
# ----------------------
DROP PROCEDURE IF EXISTS `update_password`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_password`(IN user int, IN password VARCHAR(50))
BEGIN
    UPDATE users
    SET user_password = password
    WHERE user_id = user;
END ;;

# ----------------------
# Update Biography
# ----------------------
DROP PROCEDURE IF EXISTS `update_biography`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_biography`(IN user int, IN biography VARCHAR(50))
BEGIN
    UPDATE users
    SET user_biography = biography
    WHERE user_id = user;
END ;;


# ---------------------
# Update Company
# ---------------------
DROP PROCEDURE IF EXISTS `update_company`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `update_company`(IN user int, IN c VARCHAR(50))
BEGIN
    UPDATE users
    SET users.company_id =
            (SELECT company_id
             FROM company
             WHERE company_name = c)
    WHERE user_id = user;
END ;;
DELIMITER ;


# -----------
# Delete Seeker
# -----------
DROP PROCEDURE IF EXISTS `delete_seeker`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `delete_seeker`(IN userID INT)
BEGIN
    DELETE FROM user_skills
    WHERE user_id = userID;
    DELETE FROM users
    WHERE user_id = userID;
END ;;


# ----------------
# Delete Recruiter
# ----------------
DROP PROCEDURE IF EXISTS `delete_recruiter`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `delete_recruiter`(IN userID INT)
BEGIN
    DELETE FROM users
    WHERE user_id = userID;
END ;;


# ------------------
# Search job (Title)
# ------------------
DROP PROCEDURE IF EXISTS `search_job_title`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `search_job_title`(IN title VARCHAR(50))
BEGIN
    SELECT *
    FROM jobs
             INNER JOIN company c on jobs.company_id = c.company_id
             INNER JOIN certification c2 on jobs.certification_id = c2.certification_id
             INNER JOIN state s on jobs.status_id = s.status_id
    WHERE job_title LIKE CONCAT('%', title, '%');
END ;;


# --------------------
# Search job (Company)
# --------------------
DROP PROCEDURE IF EXISTS `search_job_company`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `search_job_company`(IN comp VARCHAR(50))
BEGIN
    SELECT *
    FROM jobs
             INNER JOIN company c on jobs.company_id = c.company_id
             INNER JOIN certification c2 on jobs.certification_id = c2.certification_id
             INNER JOIN state s on jobs.status_id = s.status_id
    WHERE c.company_name LIKE CONCAT('%', comp, '%');
END ;;


# -------------------
# Search job (Salary)
# -------------------
DROP PROCEDURE IF EXISTS `search_job_salary`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `search_job_salary`(IN sal INT)
BEGIN
    SELECT *
    FROM jobs
             INNER JOIN company c on jobs.company_id = c.company_id
             INNER JOIN certification c2 on jobs.certification_id = c2.certification_id
             INNER JOIN state s on jobs.status_id = s.status_id
    WHERE jobs.job_salary >= sal;
END ;;


# ---------------------
# Search job (Location)
# ---------------------
DROP PROCEDURE IF EXISTS `search_job_location`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `search_job_location`(IN loc VARCHAR(50))
BEGIN
    SELECT *
    FROM jobs
             INNER JOIN company c on jobs.company_id = c.company_id
             INNER JOIN certification c2 on jobs.certification_id = c2.certification_id
             INNER JOIN state s on jobs.status_id = s.status_id
    WHERE job_location LIKE CONCAT(loc, '%');
END ;;


# --------------------------
# Search job (Certification)
# --------------------------
DROP PROCEDURE IF EXISTS `search_job_certification`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `search_job_certification`(IN cert VARCHAR(50))
BEGIN
    SELECT *
    FROM jobs
             INNER JOIN company c on jobs.company_id = c.company_id
             INNER JOIN certification c2 on jobs.certification_id = c2.certification_id
             INNER JOIN state s on jobs.status_id = s.status_id
    WHERE c2.certification_name LIKE CONCAT(cert, '%');
END ;;


# -------------------
# Search job (Skills)
# -------------------
DROP PROCEDURE IF EXISTS `search_job_skills`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `search_job_skills`(IN skill VARCHAR(50))
BEGIN
    SELECT *
    FROM jobs
             INNER JOIN company c on jobs.company_id = c.company_id
             INNER JOIN certification c2 on jobs.certification_id = c2.certification_id
             INNER JOIN state s on jobs.status_id = s.status_id
             INNER JOIN job_skills js on jobs.job_id = js.job_id
             INNER JOIN skills s2 on js.skill_id = s2.skill_id
    WHERE s2.skill_name LIKE CONCAT('%', skill, '%');
END ;;


# ---------------
# Advanced Search
# ---------------
DROP PROCEDURE IF EXISTS `advanced_search`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `advanced_search`(IN title VARCHAR(50), IN comp VARCHAR(50), IN sal INT, IN loc VARCHAR(50), IN cert VARCHAR(50), IN skill VARCHAR(50))
BEGIN
    SELECT *
    FROM jobs
             INNER JOIN company c on jobs.company_id = c.company_id
             INNER JOIN certification c2 on jobs.certification_id = c2.certification_id
             INNER JOIN state s on jobs.status_id = s.status_id
             INNER JOIN job_skills js on jobs.job_id = js.job_id
             INNER JOIN skills s2 on js.skill_id = s2.skill_id
    WHERE job_title LIKE CONCAT('%', title, '%')
      AND c.company_name LIKE CONCAT('%', comp, '%')
      AND jobs.job_salary >= sal
      AND job_location LIKE CONCAT(loc, '%')
      AND c2.certification_name LIKE CONCAT(cert, '%')
      AND s2.skill_name LIKE CONCAT('%', skill, '%');
END ;;


# -----------------
# Advanced Search 2
# -----------------
DROP PROCEDURE IF EXISTS `advanced_search2`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `advanced_search2`(IN title VARCHAR(50), IN comp VARCHAR(50), IN sal INT, IN loc VARCHAR(50))
BEGIN
    SELECT *
    FROM jobs
             INNER JOIN company c on jobs.company_id = c.company_id
             INNER JOIN certification c2 on jobs.certification_id = c2.certification_id
             INNER JOIN state s on jobs.status_id = s.status_id
    WHERE job_title LIKE CONCAT('%', title, '%')
      AND c.company_name LIKE CONCAT('%', comp, '%')
      AND jobs.job_salary >= sal
      AND job_location LIKE CONCAT(loc, '%');
END ;;


# -----------------
# Advanced Search 3
# -----------------
DROP PROCEDURE IF EXISTS `advanced_search3`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `advanced_search3`(IN title VARCHAR(50), IN comp VARCHAR(50), IN sal INT, IN loc VARCHAR(50), IN cert VARCHAR(50))
BEGIN
    SELECT *
    FROM jobs
             INNER JOIN company c on jobs.company_id = c.company_id
             INNER JOIN certification c2 on jobs.certification_id = c2.certification_id
             INNER JOIN state s on jobs.status_id = s.status_id
    WHERE job_title LIKE CONCAT('%', title, '%')
      AND c.company_name LIKE CONCAT('%', comp, '%')
      AND jobs.job_salary >= sal
      AND job_location LIKE CONCAT(loc, '%')
      AND c2.certification_name LIKE CONCAT(cert, '%');
END ;;


# -----------------
# Advanced Search 4
# -----------------
DROP PROCEDURE IF EXISTS `advanced_search4`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `advanced_search4`(IN title VARCHAR(50), IN comp VARCHAR(50), IN sal INT, IN loc VARCHAR(50), IN skill VARCHAR(50))
BEGIN
    SELECT *
    FROM jobs
             INNER JOIN company c on jobs.company_id = c.company_id
             INNER JOIN certification c2 on jobs.certification_id = c2.certification_id
             INNER JOIN state s on jobs.status_id = s.status_id
             INNER JOIN job_skills js on jobs.job_id = js.job_id
             INNER JOIN skills s2 on js.skill_id = s2.skill_id
    WHERE job_title LIKE CONCAT('%', title, '%')
      AND c.company_name LIKE CONCAT('%', comp, '%')
      AND jobs.job_salary >= sal
      AND job_location LIKE CONCAT(loc, '%')
      AND s2.skill_name LIKE CONCAT('%', skill, '%');
END ;;


# ------------
# Insert Roles
# ------------
DROP PROCEDURE IF EXISTS `insert_roles`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_roles`(IN role VARCHAR(50))
BEGIN
    INSERT INTO roles(role_name)
    VALUES (role);
END ;;


# ---------------
# Insert Category
# ---------------
DROP PROCEDURE IF EXISTS `insert_category`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_category`(IN cat VARCHAR(50))
BEGIN
    INSERT INTO categories(category_name)
    VALUES (cat);
END ;;
DELIMITER ;

# --------------------
# Insert Certification
# --------------------
DROP PROCEDURE IF EXISTS `insert_certification`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_certification`(IN cert VARCHAR(50))
BEGIN
    INSERT INTO certification(certification_name)
    VALUES (cert);
END ;;


# -----------------
# Insert Job Skills
# -----------------
DROP PROCEDURE IF EXISTS `insert_job_skills`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_job_skills`(IN job_id INT, IN skill_id INT)
BEGIN
    INSERT INTO job_skills(job_id, skill_id)
    VALUES (job_id, skill_id);
END ;;


# -------------------
# Insert Job Category
# -------------------
DROP PROCEDURE IF EXISTS `insert_job_category`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_job_category`(IN job_id INT, IN cat_id INT)
BEGIN
    INSERT INTO job_categories(job_id, category_id)
    VALUES (job_id, cat_id);
END ;;


# ----------------
# Insert Job State
# ----------------
DROP PROCEDURE IF EXISTS `insert_state`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_state`(IN type VARCHAR(50))
BEGIN
    INSERT INTO state(status_type)
    VALUES (type);
END ;;


# ----------
# Insert Job
# ----------
DROP PROCEDURE IF EXISTS `insert_job`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_job`(IN title VARCHAR(50), IN description VARCHAR(500), IN sal INT, IN loc VARCHAR(50), IN state_id INT, IN comp_id INT, IN cert_id INT)
BEGIN
    INSERT INTO jobs(job_title, job_description, job_salary, job_location, job_date_created, job_date_app_close, status_id, company_id, certification_id)
    VALUES (title, description, sal, loc, DATE (NOW()), DATE (NOW()) + INTERVAL 1 MONTH, state_id, comp_id, cert_id);
END ;;


# -------------------
# Insert Message Type
# -------------------
DROP PROCEDURE IF EXISTS `insert_message_type`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_message_type`(IN type VARCHAR(50))
BEGIN
    INSERT INTO message_type(message_type)
    VALUES (type);
END ;;


# -------------
# Insert Skills
# -------------
DROP PROCEDURE IF EXISTS `insert_skills`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_skills`(IN skill VARCHAR(50))
BEGIN
    INSERT INTO skills(skill_name)
    VALUES (skill);
END ;;


# -------------
# Insert Seeker
# -------------
DROP PROCEDURE IF EXISTS `insert_seeker`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_seeker`(IN email VARCHAR(50), IN roleID INT, IN pass VARCHAR(50), IN fname VARCHAR(50), IN lname VARCHAR(50), IN cert_id VARCHAR(50))
BEGIN
    INSERT INTO users(email_address, role_id, user_password, user_first_name, user_last_name, certification_id)
    VALUES (email, roleID, pass, fname, lname, cert_id);
END ;;


# ----------------
# Insert Recruiter
# ----------------
DROP PROCEDURE IF EXISTS `insert_recruiter`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_recruiter`(IN email VARCHAR(50), IN roleID INT, IN pass VARCHAR(50), IN fname VARCHAR(50), IN lname VARCHAR(50), IN comp_id INT)
BEGIN
    INSERT INTO users(email_address, role_id, user_password, user_first_name, user_last_name, company_id)
    VALUES (email, roleID, pass, fname, lname, comp_id);
END ;;


# ----------
# Delete All
# ----------
DROP PROCEDURE IF EXISTS `delete_all`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `delete_all`()
BEGIN
    DELETE FROM status;
    DELETE FROM user_skills;
    DELETE FROM users;
    DELETE FROM job_categories;
    DELETE FROM job_skills;
    DELETE FROM jobs;
    DELETE FROM categories;
    DELETE FROM certification;
    DELETE FROM company;
    DELETE FROM job_categories;
    DELETE FROM message;
    DELETE FROM message_content;
    DELETE FROM message_type;
    DELETE FROM user_experience;
    DELETE FROM skills;
    DELETE FROM roles;
    DELETE FROM state;
END ;;


# ----------------
# Reset Increments
# ----------------
DROP PROCEDURE IF EXISTS `reset_increments`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `reset_increments`()
BEGIN
    ALTER TABLE user_skills AUTO_INCREMENT = 1;
    ALTER TABLE users AUTO_INCREMENT = 1;
    ALTER TABLE job_categories AUTO_INCREMENT = 1;
    ALTER TABLE job_skills AUTO_INCREMENT = 1;
    ALTER TABLE jobs AUTO_INCREMENT = 1;
    ALTER TABLE categories AUTO_INCREMENT = 1;
    ALTER TABLE certification AUTO_INCREMENT = 1;
    ALTER TABLE company AUTO_INCREMENT = 1;
    ALTER TABLE job_categories AUTO_INCREMENT = 1;
    ALTER TABLE message AUTO_INCREMENT = 1;
    ALTER TABLE message_content AUTO_INCREMENT = 1;
    ALTER TABLE message_type AUTO_INCREMENT = 1;
    ALTER TABLE user_experience AUTO_INCREMENT = 1;
    ALTER TABLE skills AUTO_INCREMENT = 1;
    ALTER TABLE roles AUTO_INCREMENT = 1;
    ALTER TABLE state AUTO_INCREMENT = 1;
    ALTER TABLE status AUTO_INCREMENT = 1;
END ;;


# --------------
# Check for Data
# --------------
DROP PROCEDURE IF EXISTS `check_for_data`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `check_for_data`()
BEGIN
    SELECT COUNT(*) FROM users;
END ;;
