package com.example.groupa.models;

import java.util.ArrayList;

public class Job {
    private String job_title, job_description, job_salary, job_location, job_date_created, job_date_app_close, status, company, certification;

    private ArrayList<String> skills = new ArrayList<>();
    private ArrayList<String> categories = new ArrayList<>();

    public Job() {
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getJob_description() {
        return job_description;
    }

    public void setJob_description(String job_description) {
        this.job_description = job_description;
    }

    public String getJob_salary() {
        return job_salary;
    }

    public void setJob_salary(String job_salary) {
        this.job_salary = job_salary;
    }

    public String getJob_location() {
        return job_location;
    }

    public void setJob_location(String job_location) {
        this.job_location = job_location;
    }

    public String getJob_date_created() {
        return job_date_created;
    }

    public void setJob_date_created(String job_date_created) {
        this.job_date_created = job_date_created;
    }

    public String getJob_date_app_close() {
        return job_date_app_close;
    }

    public void setJob_date_app_close(String job_date_app_close) {
        this.job_date_app_close = job_date_app_close;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public ArrayList<String> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<String> skills) {
        this.skills = skills;
    }

    public String getCertification() {
        return certification;
    }

    public void setCertification(String certification) {
        this.certification = certification;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        return "JobMark2{" +
                "job_title='" + job_title + '\'' +
                ", job_description='" + job_description + '\'' +
                ", job_salary='" + job_salary + '\'' +
                ", job_location='" + job_location + '\'' +
                ", job_date_created='" + job_date_created + '\'' +
                ", job_date_app_close='" + job_date_app_close + '\'' +
                ", status='" + status + '\'' +
                ", company='" + company + '\'' +
                ", certification='" + certification + '\'' +
                ", skills=" + skills +
                ", categories=" + categories +
                '}';
    }

}
