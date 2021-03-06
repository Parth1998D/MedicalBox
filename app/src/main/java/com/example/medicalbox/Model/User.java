package com.example.medicalbox.Model;

public class User {
    private String name;
    private String email;
    private String gender;
    private String age;
    private String number;
    private String password;

    public User() {}

    public User(String name, String email, String gender, String age, String number, String password) {
        this.name = name;
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.number = number;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
