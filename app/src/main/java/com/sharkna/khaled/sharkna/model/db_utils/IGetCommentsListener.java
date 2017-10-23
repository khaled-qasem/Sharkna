package com.sharkna.khaled.sharkna.model.db_utils;

import com.sharkna.khaled.sharkna.model.Comment;

import java.util.ArrayList;

/**
 * Created by Khaled on 10/23/2017.
 * Assumptions
 * Descriptions
 */

public interface IGetCommentsListener {
    public void onGetCommentsResult(ArrayList<Comment> comments);

}
