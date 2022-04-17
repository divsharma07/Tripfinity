package com.app.tripfinity.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.tripfinity.repository.SplashRepository;
import com.app.tripfinity.model.User;

public class SplashViewModel extends AndroidViewModel {
    private SplashRepository splashRepository;
    LiveData<User> isUserAuthenticatedLiveData;
    LiveData<User> userLiveData;

    public SplashViewModel(Application application) {
        super(application);
        splashRepository = new SplashRepository();
    }

    public void checkIfUserIsAuthenticated() {
        isUserAuthenticatedLiveData = splashRepository.checkIfUserIsAuthenticatedInFirebase();
    }

    public void setUid(String userEmail) {
        userLiveData = splashRepository.addUserToLiveData(userEmail);
    }

    public LiveData<User> getIsUserAuthenticatedLiveData() {
        return isUserAuthenticatedLiveData;
    }

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

}