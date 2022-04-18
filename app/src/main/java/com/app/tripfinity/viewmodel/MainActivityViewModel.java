package com.app.tripfinity.viewmodel;

import android.app.Application;
import android.location.Address;
import android.location.Geocoder;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.app.tripfinity.repository.MainActivityRepository;
import com.app.tripfinity.view.MainActivity;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    private MainActivityRepository mainActivityRepository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mainActivityRepository = new MainActivityRepository();
    }

    public void storeUserLocationAndSubscribe(List<Address> addresses, GeoPoint geoPoint) throws IOException {
        mainActivityRepository.storeUserLocationAndSubscribe(addresses, geoPoint);
    }
}
