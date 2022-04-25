package com.app.tripfinity.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

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


    public Trip createATrip(String tripName, String startDate, List<String> userIds, String destination,  Boolean canShare) throws ParseException {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd", Locale.ENGLISH);
        Date startDateObj = formatter.parse(startDate);
        Date endDateObj = startDateObj;
        List<DocumentReference> expenses = new ArrayList<>();
        List<DocumentReference> users = new ArrayList<>();

        for(String userId : userIds) {
            users.add(usersRef.document(userId));
        }
        DocumentReference itinerary = null;
        Log.d(TAG,"users retrieved "+users);
        Trip trip = new Trip(startDateObj,endDateObj,tripName,canShare, expenses, users,itinerary,destination);

        return trip;
    }

    public MutableLiveData<Trip> addANewTrip(String tripName, String startDate, List<String> userIds, String destination,
                Boolean canShare) throws ParseException {
        Trip trip = createATrip(tripName,startDate,userIds,destination,canShare);
        MutableLiveData<Trip> newMutableTripLiveData = new MutableLiveData<>();
        trips.document(trip.getTripId()).set(trip).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "DocumentSnapshot Trip with ID: " + trip.getTripId());
                newMutableTripLiveData.setValue(trip);

                DocumentReference user = usersRef.document(userIds.get(0));
                addTopicToTrip(trips.document(trip.getTripId()),user);
                for(String userId : userIds){
                    addTripToUser(trips.document(trip.getTripId()), usersRef.document(userId));
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

    public MutableLiveData<Boolean> addTripToUser(DocumentReference trip, DocumentReference user) {
        MutableLiveData<Boolean> isUpdated = new MutableLiveData<>();

        user.update("trips", FieldValue.arrayUnion(trip)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "User successfully updated! :"+user.getId());
                isUpdated.setValue(true);
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error updating User document", e);
                isUpdated.setValue(false);
            }
        });
        return isUpdated;
    }

    public MutableLiveData<Boolean> addTopicToTrip(DocumentReference trip,DocumentReference user) {
        MutableLiveData<Boolean> isUpdated = new MutableLiveData<>();

      user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
           @Override
           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               String topic = (String) task.getResult().get("topic");
               trip.update("sourceLocation", topic).addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void aVoid) {
                       Log.d(TAG, "Trip successfully updated: "+trip.getId());
                       isUpdated.setValue(true);
                   }
               })
                   .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Log.w(TAG, "Error updating Trip document", e);
                           isUpdated.setValue(false);
                       }
                   });
           }
       });
        return isUpdated;
    }
}
