package com.app.tripfinity.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.app.tripfinity.model.Expense;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HistoryExpenseRepository {
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

    public MutableLiveData<List<Expense>> getExpenseData(String tripId) {
        MutableLiveData<List<Expense>> list = new MutableLiveData<>();
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
                    }
                    list.setValue(expenseList);
                } else {
                    Log.d("firestore expense call ", task.getException().toString());
                }
            }
        });
        return list;
    }

    public MutableLiveData<Boolean> removeExpense(Expense expense) {
        MutableLiveData<Boolean> deleteSuccess = new MutableLiveData<>();
        String addedBy = expense.getAddedByUser();
        Timestamp timestamp = expense.getTimestamp();
        DocumentReference tripref = expense.getTripRef();

        rootRef.collection("Expenses").whereEqualTo("addedByUser", addedBy).whereEqualTo("timestamp", timestamp).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        DocumentReference documentReference = document.getReference();

                        // Expense delete
                        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    deleteSuccess.setValue(true);

                                    // Delete from Trips as well
                                    tripref.update("expenses", FieldValue.arrayRemove(documentReference));
                                    Log.d("firestore delete call ", "Removed from db");
                                } else {
                                    deleteSuccess.setValue(false);
                                }
                            }
                        });

                    }
                } else {
                    Log.d("firestore delete call ", task.getException().toString());
                }
            }
        });
        return deleteSuccess;
    }
}
