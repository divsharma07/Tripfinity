package com.app.tripfinity.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.app.tripfinity.model.Trip;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class TripCreationRepository {
    private static final String TAG = "TripCreationRepository";
    public static final String TRIP_COLLECTION = "Trips";
    public static final String USER_COLLECTION = "Users";
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private CollectionReference trips = rootRef.collection(TRIP_COLLECTION);
    private CollectionReference usersRef = rootRef.collection(USER_COLLECTION);

//    public interface UserCallback {
//        void onCallback(DocumentReference user);
//    }
    public Trip createATrip(String tripName, String startDate, List<String> userIds, String destination) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd", Locale.ENGLISH);
        Date startDateObj = formatter.parse(startDate);
        Date endDateObj = startDateObj;
        List<DocumentReference> expenses = new ArrayList<>();
        List<DocumentReference> users = new ArrayList<>();

        for(String userId : userIds) {
            users.add(usersRef.document(userId));
        }
        DocumentReference itinerary = null;
        Log.d(TAG,"user retrieved "+users);
        Trip trip = new Trip(startDateObj,endDateObj,tripName,false, expenses, users,itinerary,destination);

        return trip;
    }



//    private void getUserReference(UserCallback callback,String userId) {
//        usersRef.document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//                        Log.d(TAG, "Document exists!");
//                        callback.onCallback(document.getReference());
//                    } else {
//                        Log.d(TAG, "Document does not exist!");
//                    }
//                } else {
//                    Log.d(TAG, "Failed with: ", task.getException());
//                }
//
//            }
//        });
//    }

    public MutableLiveData<Trip> addANewTrip(Trip trip, List<String> userIds) {
        MutableLiveData<Trip> newMutableTripLiveData = new MutableLiveData<>();
        trips.document(trip.getTripId()).set(trip).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "DocumentSnapshot Trip with ID: " + trip.getTripId());
                newMutableTripLiveData.setValue(trip);
                for(String userId : userIds){
                    addUsersToTrip(trips.document(trip.getTripId()),userId);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Error adding document", e);
            }
        });

        return newMutableTripLiveData;
    }

    public void addUsersToTrip(DocumentReference trip, String userId) {
        MutableLiveData<Boolean> isUpdated = new MutableLiveData<>();
        DocumentReference user = usersRef.document(userId);
        user.update("trips", FieldValue.arrayUnion(trip)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "DocumentSnapshot successfully updated!");
                isUpdated.setValue(true);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error updating document", e);
                isUpdated.setValue(false);
            }
        });
    }

}
