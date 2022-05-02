package com.app.tripfinity.service;

import com.app.tripfinity.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessagingService;
import java.util.concurrent.atomic.AtomicInteger;

public class NotificationService extends FirebaseMessagingService {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    public void onNewToken(String token) {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        saveNewToken(token);
    }

    private void saveNewToken(String token) {
        final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        final CollectionReference usersRef = rootRef.collection(Constants.USER_COLLECTION);
        if(firebaseUser != null && firebaseUser.getEmail()!=null) {
            DocumentReference user = usersRef.document(firebaseUser.getEmail());
            user.update("fcmToken", token);
        }
    }
}