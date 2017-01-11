package com.docdoku.hackaton.hackathon;

/**
 * Created by Lucas-PCP on 11/01/2017.
 */

public class Auth {
    private String id;
    private String password;

    public Auth(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
