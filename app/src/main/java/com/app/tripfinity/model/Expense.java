package com.app.tripfinity.model;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;

public class Expense {
    private String addedByUser;
    private double amount;
    private String name;
    private List<String> userIds;
    private DocumentReference tripRef;


    public Expense(String addedByUser, double amount, String name, List<String> userIds, DocumentReference tripRef) {
        this.addedByUser = addedByUser;
        this.amount = amount;
        this.name = name;
        this.userIds = userIds;
        this.tripRef = tripRef;
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
}
