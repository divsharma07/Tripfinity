package com.app.tripfinity.model;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;

public class User implements Serializable {
    private String uid;
    private String name;
    private String email;
    private GeoPoint cityGeoPoint;
    private String city;
    private String topic;
    private boolean isRegistered;
    @Exclude
    public boolean isAuthenticated;

    public User() {
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setIsRegistered(boolean isNew) {
        this.isRegistered = isNew;
    }

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

    public User(String uid, String name, String email) {
        this.uid = uid;
        this.name = name;
        this.email = email;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}