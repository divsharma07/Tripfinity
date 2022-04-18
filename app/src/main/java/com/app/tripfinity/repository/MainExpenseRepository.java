package com.app.tripfinity.repository;


import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.tripfinity.model.Expense;
import com.app.tripfinity.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainExpenseRepository {
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

    public MutableLiveData<List<User>> getUserData(String tripId) {
        MutableLiveData<List<User>> list = new MutableLiveData<>();
        DocumentReference tripRef = rootRef.collection("Trips").document(tripId);
        rootRef.collection("Users").whereArrayContains("trips", tripRef).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<User> userList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User user = document.toObject(User.class);
                        userList.add(user);
//                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    list.setValue(userList);
                } else {
//                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        return list;
    }

    public MutableLiveData<List<Expense>> getExpenseData(String tripId) {
        MutableLiveData<List<Expense>> list = new MutableLiveData<>();
        DocumentReference tripRef = rootRef.collection("Trips").document(tripId);
        rootRef.collection("Expenses").whereEqualTo("tripRef", tripRef).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Expense> expenseList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Expense expense = document.toObject(Expense.class);
                        expenseList.add(expense);
//                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    list.setValue(expenseList);
                } else {
//                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        return list;
    }
}
