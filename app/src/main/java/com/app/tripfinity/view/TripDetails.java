package com.app.tripfinity.view;

public class TripDetails {
    private String trip_name;
    private String start_date;

    public TripDetails(String trip_name, String start_date) {
        this.trip_name = trip_name;
        this.start_date = start_date;
    }


    public String getTrip_name() {
        return trip_name;
    }

    public void setTrip_name(String trip_name) {
        this.trip_name = trip_name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }
}
