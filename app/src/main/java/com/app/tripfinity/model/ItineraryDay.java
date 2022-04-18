package com.app.tripfinity.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItineraryDay {
    List<Place> places;
    private String id;

    public ItineraryDay(List<Place> places) {
        this.places = places;
        id = UUID.randomUUID().toString();
    }

    public ItineraryDay() {
        this.places = new ArrayList<>();
        id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id.toString();
    }


    public List<Place> getPlaces() {
        return places;
    }
}
