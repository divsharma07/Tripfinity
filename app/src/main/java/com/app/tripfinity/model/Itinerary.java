package com.app.tripfinity.model;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class Itinerary {
    private List<ItineraryDay> days;
    private DocumentReference trip;

    public Itinerary(List<ItineraryDay> days, DocumentReference trip) {
        this.days = days;
        this.trip = trip;
    }

    public List<ItineraryDay> getDays() {
        return days;
    }

    public DocumentReference getTrip() {
        return trip;
    }
}
