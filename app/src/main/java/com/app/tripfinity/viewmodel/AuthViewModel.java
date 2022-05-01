package com.app.tripfinity.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.tripfinity.repository.AuthRepository;
import com.app.tripfinity.model.User;
import com.google.firebase.auth.AuthCredential;

public class AuthViewModel extends AndroidViewModel {
    private AuthRepository authRepository;
    LiveData<User> authenticatedUserLiveData;
    LiveData<User> createdUserLiveData;

    public AuthViewModel(Application application) {
        super(application);
        authRepository = new AuthRepository();
    }

    public LiveData<User> getCreatedUserLiveData() {
        return createdUserLiveData;
    }

    public LiveData<User> getAuthenticatedUserLiveData() {
        return authenticatedUserLiveData;
    }

    public void signInWithGoogle(AuthCredential googleAuthCredential) {
        authenticatedUserLiveData = authRepository.firebaseSignInWithGoogle(googleAuthCredential);
    }

    public void createUser(User authenticatedUser, boolean isRegistered, String tripId) {
        createdUserLiveData = authRepository.createUserInFirestoreIfNotExists(authenticatedUser, isRegistered, tripId);
    }
}