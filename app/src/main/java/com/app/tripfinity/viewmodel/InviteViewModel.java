package com.app.tripfinity.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.app.tripfinity.model.User;
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

    public LiveData<User> checkUserExists(String email){
        return inviteRepo.checkUserExists(email);
    }

    public LiveData<Boolean> sendInvitationToUser(String sender, String receiver){
        return inviteRepo.sendInvitationToUser(sender,receiver);
    }

    public void sendNotificationToUser(String sender, String token, String tripName) {
        inviteRepo.sendNotification(sender,token, tripName);
    }
}
