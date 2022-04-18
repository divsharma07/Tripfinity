package com.app.tripfinity.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.tripfinity.model.Itinerary;
import com.app.tripfinity.repository.ItineraryRepository;

public class ItineraryViewModel extends AndroidViewModel {
    private ItineraryRepository itineraryRepository;

    public LiveData<Itinerary> getCreatedItineraryLiveData() {
        return createdItineraryLiveData;
    }

    private LiveData<Itinerary> createdItineraryLiveData;
    public ItineraryViewModel(@NonNull Application application) {
        super(application);
        itineraryRepository = new ItineraryRepository();
    }

    // Need to check if an itinerary with a trip id exists or not
    public void updateItinerary(String tripId) {
        createdItineraryLiveData = itineraryRepository.updateItinerary(tripId);

    }
}
