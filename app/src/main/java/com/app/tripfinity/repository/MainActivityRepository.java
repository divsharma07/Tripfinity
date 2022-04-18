package com.app.tripfinity.repository;

import android.location.Address;

import androidx.annotation.NonNull;

import com.app.tripfinity.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class MainActivityRepository {
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private CollectionReference userRef = rootRef.collection(Constants.USER_COLLECTION);
    private FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    public void storeUserLocationAndSubscribe(List<Address> addresses, GeoPoint geoPoint) {
        if (addresses.size() == 0) return;

        String topicName = addresses.get(0).getAdminArea().toLowerCase(Locale.ROOT);
        String updatedCity = "";
        if (addresses.get(0).getLocality() != null) {
            updatedCity = addresses.get(0).getLocality();
        }
        String updatedCountry = addresses.get(0).getCountryName();
        if (currentUser != null) {
            DocumentReference userDocRef = userRef.document(Objects.requireNonNull(currentUser.getEmail()));
            userDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        Object oldTopic = document.get("topic");
                        if (oldTopic!=null) {
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(oldTopic.toString());
                        }
                        System.out.println("hello");
                    }
                }
            });
            userDocRef.update("topic", topicName);
            userDocRef.update("city", updatedCity);
            userDocRef.update("cityGeopoint", geoPoint);
            FirebaseMessaging.getInstance().subscribeToTopic(topicName);
        }
    }
}