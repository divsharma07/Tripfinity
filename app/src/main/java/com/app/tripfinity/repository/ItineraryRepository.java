package com.app.tripfinity.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
    public static final String ITINERARY_COLLECTION = "Itinerary";
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private CollectionReference trips = rootRef.collection(TRIP_COLLECTION);
    private CollectionReference itineraryRef = rootRef.collection(ITINERARY_COLLECTION);
    MutableLiveData<Itinerary> newMutableLiveData;
    private static ItineraryRepository itineraryRepository;
    private ItineraryRepository() {
        newMutableLiveData = new MutableLiveData<>();
        placesMutableLiveData = new MutableLiveData<>();
    }

    MutableLiveData<Itinerary> placesMutableLiveData;
    public static ItineraryRepository getInstance() {
        if(itineraryRepository == null) {
            itineraryRepository = new ItineraryRepository();
        }
        return itineraryRepository;
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

                        List<ItineraryDay> itineraryDayList = new ArrayList<>();
                        ItineraryDay day = new ItineraryDay();
                        itineraryDayList.add(day);
                        Itinerary itinerary = new Itinerary(itineraryDayList,tripRef);

                        // save the newly created itinerary.
                        itineraryRef.document(itinerary.getId()).set(itinerary)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d(TAG, "DocumentSnapshot written with ID: " + itinerary.getId());
                                newMutableLiveData.setValue(itinerary);
                            }
                        })
                       .addOnFailureListener(new OnFailureListener() {
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

    public MutableLiveData<Itinerary> addPlace(String itineraryId, String dayId,
                                               String placeName, String notes, String startTime) {
        Log.d(TAG, "Updating Itinerary with id: "+itineraryId);
        Log.d(TAG, "Updating Day with id: "+dayId);
        itineraryRef.document(itineraryId).get().
                addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Itinerary itinerary = task.getResult().toObject(Itinerary.class);
                Place newPlace = new Place(placeName,notes,startTime);
                for (ItineraryDay day: itinerary.getDays()) {
                    if (day.getId().equals(dayId)) {
                        day.getPlaces().add(newPlace);
                    }
                }

                itineraryRef.document(itineraryId).set(itinerary)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "Places Updated !!!");
                        placesMutableLiveData.setValue(itinerary);
                    }
                });

            }
        });
        return placesMutableLiveData;
    }
}
