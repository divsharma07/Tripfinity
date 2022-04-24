package com.app.tripfinity.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.tripfinity.repository.TotalExpenseUserRepository;

public class ExpenseCountViewModel extends AndroidViewModel {

    private TotalExpenseUserRepository repository;

    public ExpenseCountViewModel(@NonNull Application application) {
        super(application);
        repository = new TotalExpenseUserRepository();
    }

    public MutableLiveData<Integer> getUserDataForTrip(String tripId) {
        return repository.getUserData(tripId);
    }

    public MutableLiveData<Double> getExpensesForTrip(String tripId) {
        return repository.getExpenseData(tripId);
    }


}
