package com.app.tripfinity.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.app.tripfinity.Constants;
import com.app.tripfinity.model.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class InviteRepository {


    MutableLiveData<List<User>> users;
    List<DocumentReference> userReferences;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public InviteRepository(){
        userReferences = new ArrayList<>();
    }

    public MutableLiveData<Boolean> checkUserExists(String email){
        Log.d("Email", email);
        DocumentReference userRef = db.collection(Constants.USER_COLLECTION).document(email);
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d("document", String.valueOf(document));
                    userReferences.add(userRef);
                    result.setValue(true);
                } else {
                    result.setValue(false);
                }
            } else {
                result.setValue(false);
            }
        });
        return result;
    }

    public void addUsersToTrip(String tripId) {
        DocumentReference tripRef = db.collection("Trips").document(tripId);
        tripRef.update("users", FieldValue.arrayUnion(userReferences.get(0)));
    }
}
