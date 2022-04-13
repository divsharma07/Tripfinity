package com.app.tripfinity.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.tripfinity.model.Trip;
import com.app.tripfinity.model.User;
import com.app.tripfinity.repository.TripCreationRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TripCreationViewModel extends AndroidViewModel {
    TripCreationRepository tripCreationRepository;


    LiveData<Trip> createdTripLiveData;
    public TripCreationViewModel(@NonNull Application application) {
        super(application);
        tripCreationRepository = new TripCreationRepository();
    }

    public LiveData<Trip> getCreatedTripLiveData() {
        return createdTripLiveData;
    }

    // create new trip for a user
    // add the created trip id to the users trip list
    public void createNewTrip(String tripName, String startDate, String userId) throws ParseException {
        Trip trip = tripCreationRepository.createATrip(tripName,startDate,userId);
        createdTripLiveData = tripCreationRepository.addANewTrip(trip);
    }

}
