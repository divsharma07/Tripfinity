package com.app.tripfinity.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.tripfinity.model.Itinerary;
import com.app.tripfinity.repository.ItineraryRepository;

public class PlaceViewModel extends AndroidViewModel {
    ItineraryRepository itineraryRepository;

    public LiveData<Itinerary> getPlacesLiveData() {
        return placesLiveData;
    }

    LiveData<Itinerary> placesLiveData;
    public PlaceViewModel(@NonNull Application application) {
        super(application);
        itineraryRepository = ItineraryRepository.getInstance();
    }

    public void addPlace(String itineraryId, String dayId, String placeName, String notes, String startTime) {

        placesLiveData = itineraryRepository.addPlace(itineraryId, dayId,placeName, notes, startTime);
    }


}
