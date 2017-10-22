package com.sharkna.khaled.sharkna.model;

/**
 * Created by Khaled on 10/20/2017.
 * Assumptions
 * Descriptions
 */

public class User {
    private int id;
    private String userName;
    private String email;
    private String firstName;
    private String lastName;
    private String imageURL;
    private int points;
    private String password;

    public User(int id, String userName, String firstName, String lastName, String imageURL, String email, int points, String password) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageURL = imageURL;
        this.points = points;
        this.password = password;
    }

    public User() {
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public int getPoints() {
        return points;
    }

    public String getPassword() {
        return password;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
