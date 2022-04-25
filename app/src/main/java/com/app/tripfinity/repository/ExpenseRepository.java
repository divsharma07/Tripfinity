package com.app.tripfinity.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.app.tripfinity.model.Expense;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

public class ExpenseRepository {
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    public static final String EXPENSE_COLLECTION = "Expenses";
    private CollectionReference expensesRef = rootRef.collection(EXPENSE_COLLECTION);

    public MutableLiveData<Expense> addNewExpense(Expense expense, String tripId) {
        MutableLiveData<Expense> expenseMutableLiveData = new MutableLiveData<>();
        final DocumentReference[] expenseReference = new DocumentReference[1];
        expensesRef.add(expense).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("expense ID ", documentReference.getId());
                expenseReference[0] = documentReference;
                expenseMutableLiveData.setValue(expense);

                // Adding expense reference in Trip
                if (expenseReference[0] != null) {
                    DocumentReference tripRef = rootRef.collection("Trips").document(tripId);
                    tripRef.update("expenses", FieldValue.arrayUnion(expenseReference[0]));
                }
            }
        });

        return expenseMutableLiveData;
    }

    public Expense createExpenseObject(String addedBy, double parseDouble, String name, ArrayList<String> usersList, String tripId) {
        DocumentReference tripRef = rootRef.collection("Trips").document(tripId);
        Expense e = new Expense(addedBy, parseDouble, name, usersList, tripRef, Timestamp.now());
        return e;
    }
}
