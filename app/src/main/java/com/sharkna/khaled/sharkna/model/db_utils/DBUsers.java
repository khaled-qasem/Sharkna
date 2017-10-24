package com.sharkna.khaled.sharkna.model.db_utils;

import com.sharkna.khaled.sharkna.model.User;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Khaled on 10/24/2017.
 * Assumptions
 * Descriptions
 */

public class DBUsers implements IGetUsersListener {
    HashMap<String,User> dbUsers;
    private static volatile DBUsers instance;

    public static DBUsers getInstance() {
        if (instance == null) {
            synchronized (DBUsers.class) {
                if (instance == null) {
                    instance = new DBUsers();
                }
            }
        }
        return instance;
    }

    private DBUsers() {
    }


    public HashMap<String, User> getDbUsers() {
        return dbUsers;
    }

    public void setDbUsers(HashMap<String, User> dbUsers) {
        this.dbUsers = dbUsers;
    }

    @Override
    public void onGetUsersResult(ArrayList<User> users) {
        for (User user :
                users) {
            dbUsers.put(String.valueOf(user.getId()), user);
        }
    }

    public User getUserById(String id) {
        return dbUsers.get(id);
    }
}
