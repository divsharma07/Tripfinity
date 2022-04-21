package com.app.tripfinity.repository;

import static com.app.tripfinity.utils.Constants.MESSAGES_SUBSCOLLECTION;
import static com.app.tripfinity.utils.Constants.TRIP_COLLECTION;
import static com.app.tripfinity.utils.HelperClass.getCurrentUser;

import com.app.tripfinity.model.Message;
import com.app.tripfinity.utils.Constants;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MessagingRepository {
    FirebaseUser user;
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private CollectionReference tripRef = rootRef.collection(TRIP_COLLECTION);

    public MessagingRepository() {
        user = getCurrentUser();
    }

    public void sendMessage(Message message, String tripId) {
        tripRef.document(tripId).collection(MESSAGES_SUBSCOLLECTION).add(message);
    }
}
