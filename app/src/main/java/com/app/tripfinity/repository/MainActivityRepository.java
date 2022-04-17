package com.app.tripfinity.repository;

import static com.app.tripfinity.utils.HelperClass.logErrorMessage;

import android.location.Address;
import android.location.Geocoder;

import androidx.lifecycle.MutableLiveData;

import com.app.tripfinity.model.Topic;
import com.app.tripfinity.model.User;
import com.app.tripfinity.utils.Constants;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivityRepository {
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private CollectionReference topicRef = rootRef.collection(Constants.TOPIC_COLLECTION);
    private FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    public void storeUserLocationAndSubscribe(List<Address> addresses, GeoPoint geoPoint) throws IOException {
        if (addresses.size() == 0) return;

        String updatedState = addresses.get(0).getAdminArea();
        String updatedCity = "";
        if (addresses.get(0).getLocality() != null) {
            updatedCity = addresses.get(0).getLocality();
        }
        String updatedCountry = addresses.get(0).getCountryName();
        if (currentUser != null) {
            DocumentReference uidRef = topicRef.document(Objects.requireNonNull(currentUser.getEmail()));
            uidRef.update("topic", updatedState);
            uidRef.update("city", updatedCity);
            uidRef.update("cityGeopoint", geoPoint);
        }

        DocumentReference topicRefDoc = topicRef.document(updatedState);
        topicRefDoc.get().addOnCompleteListener(topicTask -> {
            if (topicTask.isSuccessful()) {
                DocumentSnapshot document = topicTask.getResult();
                if (!document.exists()) {
                    Topic newTopic = new Topic(updatedState, "State");
                    topicRefDoc.set(newTopic);
                } else {
                    logErrorMessage(Objects.requireNonNull(topicTask.getException()).getMessage());
                }
            }
        });
    }
}
