package com.sharkna.khaled.sharkna.model.db_utils;

/**
 * Created by Khaled on 10/23/2017.
 * Assumptions
 * Descriptions
 */

public class Like {
    private int id;
    private int user_id;
    private int post_id;

    public Like(int id, int user_id, int post_id) {
        this.id = id;
        this.user_id = user_id;
        this.post_id = post_id;
    }

    public Like() {
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getPost_id() {
        return post_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setPost_id(int post_id) {
        this.post_id = post_id;
    }
}
