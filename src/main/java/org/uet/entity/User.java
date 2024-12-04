package org.uet.entity;

import org.uet.enums.Gender;

public class User {
    private String id;
    private String name;
    private Gender gender;
    private String className;
    private String major;
    private String phoneNumber;
    private String email;

    public User() {
    }

    public User(String id, String name, Gender gender, String className,
                String major, String phoneNumber, String email) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.className = className;
        this.major = major;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "{id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", gender=" + gender +
                ", className='" + className + '\'' +
                ", major='" + major + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
