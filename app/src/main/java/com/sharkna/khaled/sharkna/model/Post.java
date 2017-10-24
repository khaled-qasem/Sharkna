package com.sharkna.khaled.sharkna.model;

import java.util.ArrayList;

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
    private boolean like;
    private int numberOfLikes;
    private String latitude;
    private String longitude;
    private ArrayList<Comment> comments;

    public Post(int id, int userId, int municipalityId, String description, String server_image_url,int numberOfLikes) {
        this.id = id;
        this.userId = userId;
        this.municipalityId = municipalityId;
        this.description = description;
        this.server_image_url = server_image_url;
        this.numberOfLikes = numberOfLikes;
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

    public boolean isLike() {
        return like;
    }

    public int getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setLike(boolean like) {
        this.like = like;
    }

    public void setNumberOfLikes(int numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
