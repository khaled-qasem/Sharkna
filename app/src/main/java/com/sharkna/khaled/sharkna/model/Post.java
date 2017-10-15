package com.sharkna.khaled.sharkna.model;

/**
 * Created by Khaled on 10/15/2017.
 * Assumptions
 * Descriptions
 */

public class Post {
    private String id;
    private String userName;
    private String imageUrl;
    private String description;

    public Post() {
    }

    public Post(String id, String userName, String imageUrl, String description) {
        this.id = id;
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
