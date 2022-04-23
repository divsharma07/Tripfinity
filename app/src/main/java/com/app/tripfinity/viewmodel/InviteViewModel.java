package com.app.tripfinity.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.tripfinity.model.User;
import com.app.tripfinity.model.UserBio;
import com.app.tripfinity.repository.InviteRepository;

import java.util.ArrayList;
import java.util.List;

public class InviteViewModel extends AndroidViewModel {

    InviteRepository inviteRepo;
    MutableLiveData<ArrayList<User>> users;

    public InviteViewModel(Application application) {
        super(application);
        inviteRepo = new InviteRepository();
        users = new MutableLiveData<>();
    }

    public void addUserToTrip(String tripId){
        inviteRepo.addUsersToTrip(tripId);
    }

    public LiveData<User> checkUserExists(String email){
        return inviteRepo.checkUserExists(email);
    }

    public LiveData<Boolean> sendInvitationToUser(String sender, String receiver){
        return inviteRepo.sendInvitationToUser(sender,receiver);
    }

    public void sendNotificationToUser(String sender, String token, String tripName) {
        inviteRepo.sendNotification(sender,token, tripName);
    }

    public void addUser(ArrayList<User> userList){
        users.setValue(userList);
    }

    public LiveData<ArrayList<User>> getUsers(){
        return users;
    }
}
