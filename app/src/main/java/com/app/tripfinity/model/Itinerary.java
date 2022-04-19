package com.app.tripfinity.model;

import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Itinerary {
    private List<ItineraryDay> days;
    private DocumentReference trip;
    private String id;

    public Itinerary(List<ItineraryDay> days, DocumentReference trip) {
        this.days = days;
        this.trip = trip;
        id = UUID.randomUUID().toString();
    }

    public Itinerary() {
        this.days = new ArrayList<>();
        this.trip = null;
        id = UUID.randomUUID().toString();
    }

    public List<ItineraryDay> getDays() {
        return days;
    }

    public DocumentReference getTrip() {
        return trip;
    }

    public String getId() {
        return id;
    }

}
