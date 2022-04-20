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

    private List<DocumentReference> expenses;

    private List<DocumentReference> users;

    private DocumentReference itinerary;

    private String destination;

    public Trip(Date startDate, Date endDate, String tripName, boolean canShare, List<DocumentReference> expenses,
                List<DocumentReference> users, DocumentReference itinerary, String destination) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.tripName = tripName;
        this.canShare = canShare;
        this.expenses = expenses;
        this.users = users;
        this.itinerary = itinerary;
        this.destination = destination;
    }

    public String getDestination() {
        return destination;
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

    public List<DocumentReference> getExpenses() {
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
