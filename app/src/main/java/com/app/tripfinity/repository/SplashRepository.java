package com.app.tripfinity.repository;

import static com.app.tripfinity.utils.HelperClass.logErrorMessage;

import androidx.lifecycle.MutableLiveData;

import com.app.tripfinity.model.User;
import com.app.tripfinity.repository.AuthRepository;
import com.app.tripfinity.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashRepository {
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private User user = new User();
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = rootRef.collection(Constants.USER_COLLECTION);

    public MutableLiveData<User> checkIfUserIsAuthenticatedInFirebase() {
        MutableLiveData<User> isUserAuthenticateInFirebaseMutableLiveData = new MutableLiveData<>();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            user.isAuthenticated = false;
            isUserAuthenticateInFirebaseMutableLiveData.setValue(user);
        } else {
            user.setUid(firebaseUser.getUid());
            user.isAuthenticated = true;
            user.setUserEmail(firebaseUser.getEmail());
            isUserAuthenticateInFirebaseMutableLiveData.setValue(user);
        }
        return isUserAuthenticateInFirebaseMutableLiveData;
    }

    public MutableLiveData<User> addUserToLiveData(String userEmail) {
        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
        usersRef.document(userEmail).get().addOnCompleteListener(userTask -> {
            if (userTask.isSuccessful()) {
                DocumentSnapshot document = userTask.getResult();
                if (document.exists()) {
                    User user = document.toObject(User.class);
                    userMutableLiveData.setValue(user);
                }
            } else {
                logErrorMessage(userTask.getException().getMessage());
            }
        });
        return userMutableLiveData;
    }
}
