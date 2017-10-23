package com.sharkna.khaled.sharkna.model;

import android.net.Uri;

/**
 * Created by Khaled on 10/20/2017.
 * Assumptions
 * Descriptions
 */

public class FeedItem {
    public int likesCount;
    private boolean isLiked;
    public Uri photoUri;
    private String description;
    private String server_image_url;
    private String userPhotoUri;
    private String userName;
    private String firstName;
    private String lastName;
    private int userId;
    private int postId;

    public FeedItem(int likesCount, boolean isLiked) {
        this.likesCount = likesCount;
        this.isLiked = isLiked;
    }

    public FeedItem() {
    }

    public int getLikesCount() {
        return likesCount;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public Uri getPhotoUri() {
        return photoUri;
    }

    public String getDescription() {
        return description;
    }

    public String getServer_image_url() {
        return server_image_url;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setServer_image_url(String server_image_url) {
        this.server_image_url = server_image_url;
    }

    public String getUserPhotoUri() {
        return userPhotoUri;
    }

    public String getUserName() {
        return userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setUserPhotoUri(String userPhotoUri) {
        this.userPhotoUri = userPhotoUri;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getUserId() {
        return userId;
    }

    public int getPostId() {
        return postId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

}
