package com.app.tripfinity.model;

import com.google.firebase.firestore.GeoPoint;

public class Place {
    private GeoPoint location;
    private String name;
    private String notes;
    private String startTime;

    public Place(GeoPoint location, String name, String notes, String startTime) {
        this.location = location;
        this.name = name;
        this.notes = notes;
        this.startTime = startTime;
    }

    public Place() {
        this.location = null;
        this.name = null;
        this.notes = null;
        this.startTime = null;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getNotes() {
        return notes;
    }

    public String getStartTime() {
        return startTime;
    }
}
