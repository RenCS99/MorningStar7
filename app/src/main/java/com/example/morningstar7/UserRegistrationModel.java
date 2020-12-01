package com.example.morningstar7;


public class UserRegistrationModel {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String userEmail;


    // Constructors


    public UserRegistrationModel(String firstName, String lastName, String username, String password, String userEmail) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.userEmail = userEmail;
    }

    public UserRegistrationModel() {
    }

    // toString is necessary for printing the contents of a class object

    @Override
    public String toString() {
        return "UserRegistrationModel{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", username='" + username + '\'' +
                ", password=" + password +
                ", userEmail='" + userEmail + '\'' +
                '}';
    }

    // Getters and Setters

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
