package com.app.tripfinity.repository;


import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.app.tripfinity.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class TotalExpenseUserRepository  {
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

    public MutableLiveData<Integer> getUserData(String tripId) {
        MutableLiveData<Integer> data = new MutableLiveData<>();
        DocumentReference tripRef = rootRef.collection("Trips").document(tripId);
        rootRef.collection("Users").whereArrayContains("trips", tripRef).whereEqualTo("registered", true).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
//                    Log.d("firestore user call ", "Here");
                    List<User> userList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User user = document.toObject(User.class);
                        userList.add(user);
//                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    data.setValue(userList.size());
                } else {
//                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });


        return data;
    }

    public MutableLiveData<Double> getExpenseData(String tripId) {
        MutableLiveData<Double> data = new MutableLiveData<>();
        /*
        DocumentReference tripRef = rootRef.collection("Trips").document(tripId);
        rootRef.collection("Expenses").whereEqualTo("tripRef", tripRef).orderBy("timestamp", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.d("firestore expense call ", "Here");
                    List<Expense> expenseList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Expense expense = document.toObject(Expense.class);
                        expenseList.add(expense);
//                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                    list.setValue(expenseList);
                } else {
                    Log.d("firestore expense call ", task.getException().toString());
                }
            }
        });
        */

        return data;
    }
}
