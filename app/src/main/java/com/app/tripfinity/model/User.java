package com.app.tripfinity.model;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.List;

public class User implements Serializable {
    private String uid;
    private String name;
    private String email;
    private GeoPoint geoPoint;
    private String city;
    private String topic;
    private String state;
    private String country;
    private String fcmToken;
    private boolean isRegistered;
    @Exclude
    public boolean isAuthenticated;
    private GeoPoint cityGeoPoint;
    @Exclude
    private List<String> trips;

    public User() {
    }



    public List<String> getTrips() {
        return trips;
    }

    public void setTrips(List<String> trips) {
        this.trips = trips;
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

    public User(String email){
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

    public String getCity() {
        return city;
    }

    public String getTopic() {
        return topic;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setIsUserNew(boolean isNewUser) {

    }

    public void setIsCreated(boolean b) {
    }

    public void setUserAuthenticationStatus(boolean b) {
    }

    public boolean isUserCreated() {
        return true;
    }

    public boolean isUserNew() {
        return true;
    }

    public boolean isUserAuthenticated() {
        return true;
    }
}