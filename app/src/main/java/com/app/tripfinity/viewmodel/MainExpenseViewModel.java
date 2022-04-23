package com.app.tripfinity.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.tripfinity.model.Expense;
import com.app.tripfinity.model.User;
import com.app.tripfinity.repository.MainExpenseRepository;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

public class MainExpenseViewModel extends AndroidViewModel {
    private MainExpenseRepository mainExpenseRepository;

    public MainExpenseViewModel(@NonNull Application application) {
        super(application);
        mainExpenseRepository = new MainExpenseRepository();
    }

    public LiveData<List<User>> getUserDataForTrip(String tripId) {
        return mainExpenseRepository.getUserData(tripId);
    }

    public LiveData<List<Expense>> getExpensesForTrip(String tripId) {
        return mainExpenseRepository.getExpenseData(tripId);
    }
}
