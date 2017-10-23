package com.sharkna.khaled.sharkna.model.db_utils;

import com.sharkna.khaled.sharkna.model.Comment;

import java.util.ArrayList;

/**
 * Created by Khaled on 10/23/2017.
 * Assumptions
 * Descriptions
 */

interface IGetAllCommentsListener {
    void onGetAllCommentsResult(ArrayList<Comment> comments);

}
