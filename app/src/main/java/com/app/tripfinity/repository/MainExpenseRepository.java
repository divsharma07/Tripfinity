package com.app.tripfinity.repository;

// Get users for this trip, make hashmap of mail and name (only registered ones)
// GET expenses from trip ID
// Get details of all expenses

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.tripfinity.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MainExpenseRepository {
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();

    public MutableLiveData<List<DocumentReference>> getUserData(String tripId) {
        MutableLiveData<List<DocumentReference>> list = new MutableLiveData<>();
        DocumentReference tripRef = rootRef.collection("Trips").document(tripId);
        tripRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                List<DocumentReference> userList = (List<DocumentReference>) documentSnapshot.get("users");
                list.postValue(userList);
//                List<User> midList = new ArrayList<>();
//                for (DocumentReference userRef : userList) {
//                    userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//                        @Override
//                        public void onSuccess(DocumentSnapshot documentSnapshot) {
//                            User user = documentSnapshot.toObject(User.class);
//                            Log.d("Each user :", user.toString());
//                            midList.add(user);
//                        }
//                    });
//                }

            }
        });
        return list;
    }

    public MutableLiveData<User> getUserDataObject(DocumentReference userRef) {
        MutableLiveData<User> userObj = new MutableLiveData<>();
            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User user = documentSnapshot.toObject(User.class);
                    Log.d("user object in repo ", user.toString());
                    userObj.setValue(user);
                }
            });

        return userObj;
    }
}
