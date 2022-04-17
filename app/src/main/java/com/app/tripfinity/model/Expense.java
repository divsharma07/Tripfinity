package com.app.tripfinity.model;

import java.util.List;

public class Expense {
    private String addedByUser;
    private double amount;
    private String name;
    private List<String> userIds;
//    private String expenseId;


    public Expense(String addedByUser, double amount, String name, List<String> userIds) {
        this.addedByUser = addedByUser;
        this.amount = amount;
        this.name = name;
        this.userIds = userIds;
    }

//    public void setExpenseId(String id) {
//        expenseId = id;
//    }

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

//    public String getExpenseId() {
//        return expenseId;
//    }
}
