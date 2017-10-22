package com.sharkna.khaled.sharkna.model.db_utils;

import com.sharkna.khaled.sharkna.model.Post;

import java.util.ArrayList;

/**
 * Created by Khaled on 10/22/2017.
 * Assumptions
 * Descriptions
 */

public interface IGetPostsListener {

    public void onGetPostsResult(ArrayList<Post> posts);
}
