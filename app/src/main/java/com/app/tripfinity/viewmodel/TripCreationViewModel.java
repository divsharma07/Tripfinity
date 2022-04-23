package com.app.tripfinity.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.tripfinity.model.Itinerary;
import com.app.tripfinity.model.Trip;
import com.app.tripfinity.model.User;
import com.app.tripfinity.repository.ItineraryRepository;
import com.app.tripfinity.repository.TripCreationRepository;
import com.google.firebase.firestore.DocumentReference;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TripCreationViewModel extends AndroidViewModel {
    private static final String TAG = "TripCreationViewModel";
    TripCreationRepository tripCreationRepository;
    ItineraryRepository itineraryRepository;
    LiveData<Trip> createdTripLiveData;
    LiveData<Itinerary> createdItineraryLiveData;

    public LiveData<Trip> getUpdatedTripLiveData() {
        return updatedTripLiveData;
    }

    LiveData<Trip> updatedTripLiveData;

    public LiveData<Trip> getTripLiveData() {
        return tripLiveData;
    }

    LiveData<Trip> tripLiveData;
    public TripCreationViewModel(@NonNull Application application) {
        super(application);
        tripCreationRepository = new TripCreationRepository();
        itineraryRepository = ItineraryRepository.getInstance();
    }

    public LiveData<Trip> getCreatedTripLiveData() {
        return createdTripLiveData;
    }

    public LiveData<Itinerary> getCreatedItineraryLiveData() {
        return createdItineraryLiveData;
    }

    // create new trip for a user
    // add the created trip id to the users trip list
    public void createNewTrip(String tripName, String startDate, String userId, String destination)
            throws ParseException {

        createdTripLiveData = tripCreationRepository.addANewTrip(tripName,startDate,userId,destination);

    }

    public void createNewItinerary(String tripId) {
        Log.d(TAG,"Creating Itinerary with trip Id: "+tripId);
        createdItineraryLiveData = itineraryRepository.updateItinerary(tripId);
    }


    public void getTrip(String tripId) {
        tripLiveData = itineraryRepository.getTrip(tripId);
    }

    public void updateTrip(Trip trip) {
        updatedTripLiveData = itineraryRepository.updateTrip(trip);
    }
}
