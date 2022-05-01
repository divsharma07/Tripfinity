package com.app.tripfinity.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.app.tripfinity.model.Trip;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TripRepository {
    private static final String TAG = "TripRepository";
    public static final String ITINERARY_COLLECTION = "Itinerary";
    public static final String TRIP_COLLECTION = "Trips";
    public static final String EXPENSE_COLLECTION = "Expenses";
    public static final String USER_COLLECTION = "Users";
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private CollectionReference trips = rootRef.collection(TRIP_COLLECTION);
    private CollectionReference itineraryRef = rootRef.collection(ITINERARY_COLLECTION);
    private CollectionReference expenseRef = rootRef.collection(EXPENSE_COLLECTION);
    private CollectionReference userRef = rootRef.collection(USER_COLLECTION);
    private static TripRepository tripRepository;

    public static TripRepository getInstance() {
        if (tripRepository == null) {
            tripRepository = new TripRepository();
        }
        return tripRepository;
    }

    public MutableLiveData<List<Trip>> getAllTrips(List<Integer> tripIds) {
        MutableLiveData<List<Trip>> tripsData = new MutableLiveData<>();
        trips.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Trip> tripArrayList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Trip trip = document.toObject(Trip.class);
                        tripArrayList.add(trip);
                    }
                    tripsData.setValue(tripArrayList);
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
        return tripsData;
    }

    public void deleteTrip(String tripId) {
        // delete itinerary with the given trip id
        DocumentReference tripRef = trips.document(tripId);
        Query queryItinerary = itineraryRef.whereEqualTo("trip", tripRef);

        queryItinerary.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                String itineraryId = task.getResult().getDocuments().get(0).getId();
                itineraryRef.document(itineraryId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Itinerary with the Id: " + itineraryId + " deleted");
                    }
                });
            }
        });

        // delete all the expenses of the trip
        Query queryExpenses = expenseRef.whereEqualTo("tripRef", tripRef);
        queryExpenses.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot snapshot : task.getResult().getDocuments()) {
                    Log.d(TAG, "Deleting expense with Id: " + snapshot.getId());
                    expenseRef.document(snapshot.getId()).delete();
                }
            }
        });

        // remove trip id from users collection
        String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        userRef.document(userId).update("trips", FieldValue.arrayRemove(tripRef)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG,"Trip removed from user.");
            }
        });



        trips.document(tripId).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Trip deleted");
            }
        });
    }
}
