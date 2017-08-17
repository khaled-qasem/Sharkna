package com.sharkna.khaled.sharkna.account;

/**
 * Created by Khaled on 8/17/2017.
 * Assumptions
 * Descriptions
 */

// TODO: 8/17/2017 make this class singleton
public class GmailAccount {

    private String name;
    private String email;

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
