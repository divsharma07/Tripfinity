package com.app.tripfinity.view;

import static com.app.tripfinity.utils.Constants.TRIP_COLLECTION;
import static com.app.tripfinity.utils.HelperClass.getCurrentUser;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.app.tripfinity.R;
import com.app.tripfinity.adapters.MessageListAdapter;
import com.app.tripfinity.model.Message;
import com.app.tripfinity.viewmodel.MessagingViewModel;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class MessagingFragment extends Fragment {
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private List<Message> messageList = new ArrayList<>();
    private FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    private CollectionReference tripRef = rootRef.collection(TRIP_COLLECTION);
    private MessagingViewModel messagingViewModel;
    private Button messageSendButton;
    private String tripId;
    private ListenerRegistration messageListener;

    public MessagingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeMessagingViewViewModel();
    }

    private void initializeMessagingViewViewModel() {
        messagingViewModel = new ViewModelProvider(getActivity()).get(MessagingViewModel.class);
    }

    private void addMessageListener() {
        messageListener = tripRef.document(tripId).collection("messages")
                .orderBy("timestamp").addSnapshotListener((snapshot, firebaseException) -> {
            assert snapshot != null;
<<<<<<< HEAD
            int initialSize = messageList.size() - 1;
=======
>>>>>>> 7e66911c3e75c0d8ce908f18a4e20ecb4a23acc6
            messageList.clear();
            for (DocumentSnapshot document : snapshot) {
                Message message = document.toObject(Message.class);
                messageList.add(message);
            }
            mMessageAdapter.notifyDataSetChanged();
            mMessageRecycler.scrollToPosition(messageList.size()-1);
        });
    }

    private void initializeRecyclerView() {
        mMessageRecycler = getView().findViewById(R.id.recycler_gchat);
        mMessageAdapter = new MessageListAdapter(getContext(), messageList);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mMessageRecycler.setAdapter(mMessageAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messaging, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        messageSendButton = (Button) getView().findViewById(R.id.button_gchat_send);
        initializeTripId();
        subscribeForNotifications();
        addMessageListener();
        initializeSendButtonListener();
        initializeRecyclerView();
    }

    private void subscribeForNotifications() {
        FirebaseMessaging.getInstance().subscribeToTopic(tripId);
    }

    private void initializeTripId() {
        if (getArguments() != null) {
            tripId = getArguments().getString("tripId");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        messageListener.remove();
    }

    private void initializeSendButtonListener() {
        messageSendButton.setOnClickListener(v ->
        {
            EditText messageBox = getView().findViewById(R.id.edit_gchat_message);
            String content = messageBox.getText().toString();
            messageBox.setText("");
            FirebaseUser currentUser = getCurrentUser();
            String userPhotoUrl = "";
            Timestamp currentTime = new Timestamp(System.currentTimeMillis() / 1000, 0);
            if (content.equals("")) {
                return;
            }
            if (currentUser.getPhotoUrl() != null) {
                userPhotoUrl = currentUser.getPhotoUrl().toString();
            }
            Message newMessage = new Message(content, currentUser.getEmail(),
                    currentUser.getDisplayName(), userPhotoUrl, currentTime);

            messagingViewModel.sendMessage(newMessage, tripId);
        });
    }
}