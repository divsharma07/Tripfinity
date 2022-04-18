package com.app.tripfinity.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.app.tripfinity.model.Itinerary;
import com.app.tripfinity.model.ItineraryDay;
import com.app.tripfinity.model.Place;
import com.app.tripfinity.model.Trip;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Queue;

public class ItineraryRepository {

    private static final String TAG = "ItineraryRepository";
    public static final String TRIP_COLLECTION = "Trips";
    public static final String USER_COLLECTION = "Users";
    public static final String ITINERARY_COLLECTION = "Itinerary";
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private CollectionReference trips = rootRef.collection(TRIP_COLLECTION);
    private CollectionReference usersRef = rootRef.collection(USER_COLLECTION);
    private CollectionReference itineraryRef = rootRef.collection(ITINERARY_COLLECTION);
    MutableLiveData<Itinerary> newMutableLiveData;

    public ItineraryRepository() {
        newMutableLiveData = new MutableLiveData<>();
    }


    public MutableLiveData<Itinerary> updateItinerary(String tripId) {

        DocumentReference tripRef = trips.document(tripId);
        Query query = itineraryRef.whereEqualTo("trip", tripRef);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documents = task.getResult().getDocuments();
                    if (documents.isEmpty()) {
                        Log.d(TAG, "No Itinerary with the given trip found");

                        // create a new Day and add to the newly created Itinerary.
                        ItineraryDay itineraryDay = new ItineraryDay();
                        List<ItineraryDay> itineraryDayList = new ArrayList<>();
                        itineraryDayList.add(itineraryDay);
                        Itinerary itinerary = new Itinerary(itineraryDayList,tripRef);

                        // save the newly created itinerary.
                        itineraryRef.add(itinerary).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                                newMutableLiveData.setValue(itinerary);


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Error adding document", e);
                            }
                        });
                    } else {
                        // update the document
                        String itineraryId = documents.get(0).getId();
                        Log.d(TAG,"Updating itinerary Id "+itineraryId);
                        ItineraryDay newDay = new ItineraryDay();
                        itineraryRef.document(itineraryId).update("days", FieldValue.arrayUnion(newDay))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, "DocumentSnapshot successfully updated!");

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w(TAG, "Error updating document", e);
                                    }
                                });

                    }
                }


            }
        });
        return newMutableLiveData;
    }
}
