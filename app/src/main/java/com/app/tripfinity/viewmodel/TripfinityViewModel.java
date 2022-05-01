package com.app.tripfinity.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.tripfinity.model.Expense;
import com.app.tripfinity.model.Trip;
import com.app.tripfinity.model.User;
import com.app.tripfinity.repository.MainExpenseRepository;
import com.app.tripfinity.repository.TripfinityRepository;

import java.util.List;

public class TripfinityViewModel extends AndroidViewModel {
    private TripfinityRepository tripfinityRepository;

    public TripfinityViewModel(@NonNull Application application) {
        super(application);
        tripfinityRepository = new TripfinityRepository();
    }

    public LiveData<List<Expense>> getExpensesForTrip(String tripId) {
        return tripfinityRepository.getExpenseData(tripId);
    }

    public LiveData<Trip> getTrip(String tripId) {
        return tripfinityRepository.getTripData(tripId);
    }
}
