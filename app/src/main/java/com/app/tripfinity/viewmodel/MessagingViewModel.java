package com.app.tripfinity.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.app.tripfinity.model.Message;
import com.app.tripfinity.repository.MessagingRepository;

public class MessagingViewModel extends AndroidViewModel {
    private MessagingRepository repository;
    private LiveData<String> tripIdLiveData;

    public MessagingViewModel(@NonNull Application application) {
        super(application);
        repository = new MessagingRepository();
    }

    public void sendMessage(Message message, String tripId) {
        repository.sendMessage(message, tripId);
    }
}
