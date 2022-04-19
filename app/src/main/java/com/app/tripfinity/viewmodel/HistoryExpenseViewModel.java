package com.app.tripfinity.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.tripfinity.model.Expense;
import com.app.tripfinity.repository.HistoryExpenseRepository;

import java.util.List;

public class HistoryExpenseViewModel extends AndroidViewModel {
    private HistoryExpenseRepository historyExpenseRepository;

    public HistoryExpenseViewModel(@NonNull Application application) {
        super(application);
        historyExpenseRepository = new HistoryExpenseRepository();
    }

    public LiveData<List<Expense>> getExpensesForTrip(String tripId) {
        return historyExpenseRepository.getExpenseData(tripId);
    }
}
