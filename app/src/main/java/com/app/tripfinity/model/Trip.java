package com.app.tripfinity.model;

import com.google.firebase.firestore.DocumentReference;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Trip {
    private String tripId;
    private Date startDate;
    private Date endDate;
    private String tripName;

    private boolean canShare;

    // TODO: add model reference
    private List<DocumentReference> expenses;

    // TODO: add model reference
    private List<DocumentReference> users;

    // TODO: add model reference
    private DocumentReference itinerary;

    public void setDestination(String destination) {
        this.destination = destination;
    }

    private String destination;

    public boolean isCanShare() {
        return canShare;
    }

    public Trip() {
        tripId = UUID.randomUUID().toString();
    }

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
        this.tripId = UUID.randomUUID().toString();
    }

    public String getDestination() {
        return destination;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
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

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setCanShare(boolean canShare) {
        this.canShare = canShare;
    }

    public void setExpenses(List<DocumentReference> expenses) {
        this.expenses = expenses;
    }

    public void setUsers(List<DocumentReference> users) {
        this.users = users;
    }

}