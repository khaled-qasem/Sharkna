package com.sharkna.khaled.sharkna.model;

/**
 * Created by Khaled on 10/20/2017.
 * Assumptions
 * Descriptions
 */

public class Post {

    private int id;
    private int userId;
    private int municipalityId;
    private String description;
    private String image;
    private String server_image_url;
    private int publicPost;
    private User user;

    public Post(int id, int userId, int municipalityId, String description, String server_image_url) {
        this.id = id;
        this.userId = userId;
        this.municipalityId = municipalityId;
        this.description = description;
        this.server_image_url = server_image_url;
    }

    public Post() {
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setMunicipalityId(int municipalityId) {
        this.municipalityId = municipalityId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getMunicipalityId() {
        return municipalityId;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public int getPublicPost() {
        return publicPost;
    }

    public void setPublicPost(int publicPost) {
        this.publicPost = publicPost;
    }

    public String getServer_image_url() {
        return server_image_url;
    }

    public void setServer_image_url(String server_image_url) {
        this.server_image_url = server_image_url;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
