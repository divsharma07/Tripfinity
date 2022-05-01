package com.app.tripfinity.repository;

import android.location.Address;
import android.util.Log;

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
import java.util.List;
import java.util.Objects;

public class MainActivityRepository {
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private CollectionReference userRef = rootRef.collection(Constants.USER_COLLECTION);
    private FirebaseUser currentUser = firebaseAuth.getCurrentUser();

    public void storeUserLocationAndSubscribe(List<Address> addresses, GeoPoint geoPoint) {
        if (addresses.size() == 0) return;
        String tempTopicName;
        String updatedCity = "";
        String updatedState = "";
        if (addresses.get(0).getAdminArea() != null) {
            tempTopicName = addresses.get(0).getAdminArea();
            updatedState = addresses.get(0).getAdminArea();
        } else {
            tempTopicName = "default";
        }
        if (addresses.get(0).getLocality() != null) {
            updatedCity = addresses.get(0).getLocality();
        }

        final String topic = tempTopicName;
        if (currentUser != null) {
            DocumentReference userDocRef = userRef.document(Objects.requireNonNull(currentUser.getEmail()));
            userDocRef.update("userPhotoUrl", currentUser.getPhotoUrl());
            userDocRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Object oldTopic = document.get("topic");
                    if (oldTopic != null) {
                        FirebaseMessaging.getInstance().unsubscribeFromTopic(oldTopic.toString());
                    }
                    FirebaseMessaging.getInstance().subscribeToTopic(topic);
                }
            });
            userDocRef.update("registered", true);
            userDocRef.update("topic", topic);
            userDocRef.update("state", updatedState);
            userDocRef.update("city", updatedCity);
            userDocRef.update("geopoint", geoPoint);
        }
    }
}