package com.sharkna.khaled.sharkna.model.db_utils;

import com.sharkna.khaled.sharkna.model.Like;

import java.util.ArrayList;

/**
 * Created by Khaled on 10/23/2017.
 * Assumptions
 * Descriptions
 */

interface IGetAllLikesListener {
    void onGetAllLikesResult(ArrayList<Like> likesList);

//    void onGetAllCommentsResult(ArrayList<Comment> comments);

}
