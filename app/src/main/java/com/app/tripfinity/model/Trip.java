package com.app.tripfinity.model;

import com.google.firebase.firestore.DocumentReference;

import java.util.Date;
import java.util.List;

public class Trip {
    private String tripId;
    private Date startDate;
    private Date endDate;
    private String tripName;

    public boolean isCanShare() {
        return canShare;
    }

    private boolean canShare;

    // TODO: add model reference
    private List<String> expenses;

    // TODO: add model reference
    private List<DocumentReference> users;

    // TODO: add model reference
    private DocumentReference itinerary;

    public Trip(Date startDate, Date endDate, String tripName, boolean canShare, List<String> expenses,
                List<DocumentReference> users, DocumentReference itinerary) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.tripName = tripName;
        this.canShare = canShare;
        this.expenses = expenses;
        this.users = users;
        this.itinerary = itinerary;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getTripName() {
        return tripName;
    }

    public List<String> getExpenses() {
        return expenses;
    }

    public List<DocumentReference> getUsers() {
        return users;
    }

    public DocumentReference getItinerary() {
        return itinerary;
    }
    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }


}
