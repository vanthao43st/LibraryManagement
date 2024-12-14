package org.uet.entity;

import org.uet.enums.Gender;

public class User {
    private String id;
    private String fullname;
    private Gender gender;
    private String classname;
    private String major;
    private String phonenumber;
    private String email;
    private String username;
    private String password;

    public User() {
        this.id = "";
        this.fullname = "";
        this.gender = Gender.MALE;
        this.classname = "";
        this.major = "";
        this.phonenumber = "";
        this.email = "abc@gmail.com";
        this.username = "";
        this.password = "123456";
    }

    public User(String id, String fullname, Gender gender, String classname,
                String major, String phonenumber, String email, String username, String password) {
        this.id = id;
        this.fullname = fullname;
        this.gender = gender;
        this.classname = classname;
        this.major = major;
        this.phonenumber = phonenumber;
        this.email = email;
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "classname='" + classname + '\'' +
                ", id='" + id + '\'' +
                ", fullname='" + fullname + '\'' +
                ", gender=" + gender +
                ", major='" + major + '\'' +
                ", phonenumber='" + phonenumber + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
