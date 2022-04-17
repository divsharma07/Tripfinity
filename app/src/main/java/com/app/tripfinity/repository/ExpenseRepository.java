package com.app.tripfinity.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.app.tripfinity.model.Expense;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ExpenseRepository {
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    public static final String EXPENSE_COLLECTION = "Expenses";
    private CollectionReference expensesRef = rootRef.collection(EXPENSE_COLLECTION);

    public MutableLiveData<Expense> addNewExpense(Expense expense) {
        MutableLiveData<Expense> expenseMutableLiveData = new MutableLiveData<>();
        expensesRef.add(expense).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d("expense ID ", documentReference.getId());
                expense.setExpenseId(documentReference.getId());
                expenseMutableLiveData.setValue(expense);
            }
        });
        return expenseMutableLiveData;
    }
}
