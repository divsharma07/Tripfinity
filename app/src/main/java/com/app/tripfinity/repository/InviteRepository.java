package com.app.tripfinity.repository;

import static com.app.tripfinity.utils.Constants.USER_COLLECTION;

import android.util.Log;
import androidx.lifecycle.MutableLiveData;
import com.app.tripfinity.model.User;
import com.app.tripfinity.utils.HelperClass;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.functions.FirebaseFunctionsException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InviteRepository {


    List<User> users;
    List<DocumentReference> userReferences;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private InviteRepository(){
        userReferences = new ArrayList<>();
        users = new ArrayList<>();
    }

    private static InviteRepository inviteRepository;
    public static InviteRepository getInstance() {
        if(inviteRepository == null) {
            inviteRepository = new InviteRepository();
        }
        return inviteRepository;
    }

    public MutableLiveData<User> checkUserExists(String email){
        Log.d("Email", email);
        DocumentReference userRef = db.collection(USER_COLLECTION).document(email);
        MutableLiveData<User> result = new MutableLiveData<>();
        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d("document", String.valueOf(document));
                    users.add(document.toObject(User.class));
                    userReferences.add(userRef);
                    result.setValue(document.toObject(User.class));
                } else {
                    result.setValue(null);
                }
            } else {
                result.setValue(null);
            }
        });
        return result;
    }

    public void addUserToTrip(String tripId, String email) {
        DocumentReference tripRef = db.collection("Trips").document(tripId);
        DocumentReference userRef = db.collection(USER_COLLECTION).document(email);
        tripRef.update("users", FieldValue.arrayUnion(userRef));
        userRef.update("trips", FieldValue.arrayUnion(tripRef));
    }


    public MutableLiveData<Boolean> sendInvitationToUser(String sender, String receiver) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        Map<String, Object> data = new HashMap<>();
        data.put("sender", sender);
        data.put("receiver", receiver);
        data.put("push", true);

        HelperClass.callFunction("sendemail", data).addOnCompleteListener(task -> {
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

    public void sendNotification(String sender, String token, String tripName) {
        Map<String, Object> data = new HashMap<>();
        data.put("sender", sender);
        data.put("token", token);
        data.put("trip", tripName);
        data.put("push", true);

        HelperClass.callFunction("invitenotification", data).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Exception e = task.getException();
                if (e instanceof FirebaseFunctionsException) {
                    FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                    FirebaseFunctionsException.Code code = ffe.getCode();
                    Object details = ffe.getDetails();
                    Log.d("Functions Exception", String.valueOf(details));
                }
            }
        });
    }

    public MutableLiveData<ArrayList<User>> getUsersInTrip(String tripId){
        MutableLiveData<ArrayList<User>> list = new MutableLiveData<>();
        DocumentReference tripRef = db.collection("Trips").document(tripId);
        db.collection("Users").whereArrayContains("trips", tripRef).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<User> userList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    User user = document.toObject(User.class);
                    if(!user.getEmail().equals(HelperClass.getCurrentUser().getEmail()))
                        userList.add(user);
                }
                list.setValue(userList);
            } else {
                Log.d("Error", "Error getting documents: ", task.getException());
            }
        });
        return list;
    }
}
