package com.app.tripfinity.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.app.tripfinity.repository.InviteRepository;

public class InviteViewModel extends AndroidViewModel {

    InviteRepository inviteRepo;

    public InviteViewModel(Application application) {
        super(application);
        inviteRepo = new InviteRepository();
    }

    public void addUserToTrip(String tripId){
        inviteRepo.addUsersToTrip(tripId);
    }

    public LiveData<Boolean> checkUserExists(String email){
        return inviteRepo.checkUserExists(email);
    }
}
