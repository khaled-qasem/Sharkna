package com.sharkna.khaled.sharkna.account;

/**
 * Created by Khaled on 8/17/2017.
 * Assumptions
 * Descriptions
 */

// TODO: 8/17/2017 make this class singleton
public final class CurrentAccount {

    private String userName;
    private String userEmail;
    private String userPhotoURL;
    private static volatile CurrentAccount instance;

    private CurrentAccount() {
    }

    public static CurrentAccount getInstance() {
        if (instance == null) {
            synchronized (CurrentAccount.class) {
                if (instance == null) {
                    instance = new CurrentAccount();
                }
            }
        }
        return instance;
    }
    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhotoURL() {
        return userPhotoURL;
    }

    public void setUserPhotoURL(String userPhotoURL) {
        this.userPhotoURL = userPhotoURL;
    }
}
