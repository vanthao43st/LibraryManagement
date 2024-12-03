package org.uet.controllers;

public class Student {
    private int stt;
    private String studentId;
    private String name;
    private String gender;
    private String studentClass;
    private String major;
    private String phone;
    private String email;
    private String password;

    public Student(String studentId, String name, String gender, String studentClass, String major,
                   String phone, String email, String password) {
        this.studentId = studentId;
        this.name = name;
        this.gender = gender;
        this.studentClass = studentClass;
        this.major = major;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String Password) {
        this.password = password;
    }
}
