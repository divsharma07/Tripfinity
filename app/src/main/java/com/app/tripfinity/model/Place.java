package com.app.tripfinity.model;

import com.google.firebase.firestore.GeoPoint;

public class Place {
    private String name;
    private String notes;
    private String startTime;

    public Place(String name, String notes, String startTime) {
        this.name = name;
        this.notes = notes;
        this.startTime = startTime;
    }

    public Place() {
        this.name = null;
        this.notes = null;
        this.startTime = null;
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
