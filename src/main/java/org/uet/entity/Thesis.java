package org.uet.entity;

public class Thesis extends Document {
    private String supervisor;
    private String university;
    private String degree;
    private int submissionYear;
    private String major;

    public Thesis() {
        super();
        this.degree = "";
        this.major = "";
        this.submissionYear = 0;
        this.supervisor = "";
        this.university = "";
    }

    public Thesis(String author, String code, String description, String title,
                  String degree, String major, int submissionYear, String supervisor, String university) {
        super(author, code, description, title);
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
