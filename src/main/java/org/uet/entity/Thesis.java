package org.uet.entity;

public class Thesis extends Document {
    private String supervisor;
    private String university;
    private String degree;
    private String submission_year;
    private String major;

    public Thesis(String degree, String major, String submission_year, String supervisor, String university) {
        super();
        this.degree = degree;
        this.major = major;
        this.submission_year = submission_year;
        this.supervisor = supervisor;
        this.university = university;
    }

    public Thesis(String author, String code, String description, String title, String degree, String major, String submission_year, String supervisor, String university) {
        super(author, code, description, title);
        this.degree = degree;
        this.major = major;
        this.submission_year = submission_year;
        this.supervisor = supervisor;
        this.university = university;
    }

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getSubmission_year() {
        return submission_year;
    }

    public void setSubmission_year(String submission_year) {
        this.submission_year = submission_year;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    @Override
    public String toString() {
        return "";
    }
}
