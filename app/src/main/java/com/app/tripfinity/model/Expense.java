package com.app.tripfinity.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;
import java.util.List;

public class Expense implements Serializable {
    private String addedByUser;
    private double amount;
    private String name;
    private List<String> userIds;
    private DocumentReference tripRef;
    private Timestamp timestamp;


    public Expense(String addedByUser, double amount, String name, List<String> userIds, DocumentReference tripRef, Timestamp timestamp) {
        this.addedByUser = addedByUser;
        this.amount = amount;
        this.name = name;
        this.userIds = userIds;
        this.tripRef = tripRef;
        this.timestamp = timestamp;
    }

    public Expense() {
    }

    public String getAddedByUser() {
        return addedByUser;
    }

    public void setAddedByUser(String addedByUser) {
        this.addedByUser = addedByUser;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public DocumentReference getTripRef() {
        return tripRef;
    }

    public void setTripRef(DocumentReference tripRef) {
        this.tripRef = tripRef;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
