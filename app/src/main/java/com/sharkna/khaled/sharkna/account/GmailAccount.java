package com.sharkna.khaled.sharkna.account;

/**
 * Created by Khaled on 8/17/2017.
 * Assumptions
 * Descriptions
 */

// TODO: 8/17/2017 make this class singleton
public final class GmailAccount {

    private String name;
    private String email;
    private static volatile GmailAccount instance;

    private GmailAccount() {
    }

    public static GmailAccount getInstance() {
        if (instance == null) {
            synchronized (GmailAccount.class) {
                if (instance == null) {
                    instance = new GmailAccount();
                }
            }
        }
        return instance;
    }
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
