package com.app.tripfinity.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.app.tripfinity.Constants;
import com.app.tripfinity.model.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InviteRepository {


    MutableLiveData<List<User>> users;
    List<DocumentReference> userReferences;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseFunctions fn = FirebaseFunctions.getInstance();

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
        //TODO: Fix the void to boolean
        DocumentReference tripRef = db.collection("Trips").document(tripId);
        tripRef.update("users", FieldValue.arrayUnion(userReferences.get(0)));
    }

    private Task<String> sendEmail(String sender, String receiver) {
        Map<String, Object> data = new HashMap<>();
        data.put("text", receiver);
        data.put("push", true);

        return fn
                .getHttpsCallable("sendemail")
                .call(data)
                .continueWith(task -> (String) task.getResult().getData());
    }

    public MutableLiveData<Boolean> sendInvitationToUser(String sender, String receiver) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        sendEmail(sender, receiver).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Exception e = task.getException();
                if (e instanceof FirebaseFunctionsException) {
                    FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                    FirebaseFunctionsException.Code code = ffe.getCode();
                    Object details = ffe.getDetails();
                    Log.d("Functions Exception", String.valueOf(details));
                }
                result.setValue(false);
            }

        });
        result.setValue(true);
        return result;
    }
}
