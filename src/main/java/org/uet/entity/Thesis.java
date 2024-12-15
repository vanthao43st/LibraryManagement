package org.uet.entity;

public class Thesis extends Document {
    protected String supervisor;
    protected String university;
    protected String degree;
    protected int submissionYear;
    protected String major;

    public Thesis() {
        super();
        this.degree = "";
        this.major = "";
        this.submissionYear = 0;
        this.supervisor = "";
        this.university = "";
    }

    public Thesis(String author, String code, String description, String title,
                  String degree, String major, int quantity, int submissionYear, String supervisor, String university) {
        super(code, title, description, author, quantity);
        this.degree = degree;
        this.major = major;
        this.submissionYear = submissionYear;
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

    public int getSubmissionYear() {
        return submissionYear;
    }

    public void setSubmissionYear(int submissionYear) {
        this.submissionYear = submissionYear;
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
