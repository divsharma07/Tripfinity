package com.app.tripfinity.model;

import java.io.Serializable;

public class UserBio implements Serializable {

    private String uid;
    private String name;
    private String email;
    private String userPhotoUrl;

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

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public UserBio(){

    }

    public UserBio(String email){
        this.email = email;
    }

    public UserBio(String uid, String name, String email, String userPhotoUrl) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.userPhotoUrl = userPhotoUrl;
    }

    public UserBio(String uid, String name, String email) {
        this.uid = uid;
        this.name = name;
        this.email = email;
    }
}
