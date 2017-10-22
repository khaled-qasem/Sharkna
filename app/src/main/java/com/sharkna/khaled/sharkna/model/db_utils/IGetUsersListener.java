package com.sharkna.khaled.sharkna.model.db_utils;

import com.sharkna.khaled.sharkna.model.User;

import java.util.ArrayList;

/**
 * Created by Khaled on 10/21/2017.
 * Assumptions
 * Descriptions
 */

public interface IGetUsersListener {
    public void onGetUsersResult(ArrayList<User> users);
}
