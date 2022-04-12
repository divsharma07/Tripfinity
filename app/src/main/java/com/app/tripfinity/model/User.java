package com.app.tripfinity.model;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class User implements Serializable {
    private String uid;
    private String name;
    @SuppressWarnings("WeakerAccess")
    private String email;
    @Exclude
    private boolean isAuthenticated;
    @Exclude
    private boolean isNew, isCreated;

    public User() {}

    public boolean isUserNew() {
        return isNew;
    }

    public void setIsUserNew(boolean isNew) {
        this.isNew = isNew;
    }

    public String getEmail() {
        return email;
    }

    public void setIsCreated(boolean isCreated) {
        this.isCreated = isCreated;
    }

    public String getUid() {
        return uid;
    }

    public boolean isUserAuthenticated() {
        return isAuthenticated;
    }

    public void setUserAuthenticationStatus(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    public void setUserEmail(String email) {
        this.email = email;
    }

    public boolean isUserCreated() {
        return isCreated;
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