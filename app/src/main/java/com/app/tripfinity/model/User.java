package com.app.tripfinity.model;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.GeoPoint;

import java.io.Serializable;
import java.util.List;

public class User extends UserBio implements Serializable {

    private GeoPoint geoPoint;
    private String city;
    private String topic;
    private String state;
    private String country;

    private boolean registered;
    @Exclude
    public boolean isAuthenticated;
    private GeoPoint cityGeoPoint;
    @Exclude
    private List<DocumentReference> trips;

    public User() {
        super();
    }

    public List<DocumentReference> getTrips() {
        return trips;
    }

    public void setTrips(List<DocumentReference> trips) {
        this.trips = trips;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setIsRegistered(boolean isNew) {
        this.registered = isNew;
    }

    public User(String email){
        super(email);
    }

    public User(String uid, String name, String email, String userPhoto, String fcmToken) {
        super(uid, name, email, userPhoto, fcmToken);
    }

    public User(String uid, String name, String email) {
        super(uid, name, email);
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
}