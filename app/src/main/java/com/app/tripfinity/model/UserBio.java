package com.app.tripfinity.model;

import java.io.Serializable;

public class UserBio implements Serializable {

    private String uid;
    private String name;
    private String email;

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public void setUserEmail(String email) {
        this.email = email;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public UserBio(){

    }

    public UserBio(String email){
        this.email = email;
    }

    public UserBio(String uid, String name, String email) {
        this.uid = uid;
        this.name = name;
        this.email = email;
    }
}
