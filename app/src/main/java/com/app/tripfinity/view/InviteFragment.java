package com.app.tripfinity.view;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tripfinity.R;
import com.app.tripfinity.adapters.InviteUsersAdapter;
import com.app.tripfinity.listeners.RemoveInviteClickListener;
import com.app.tripfinity.model.User;
import com.app.tripfinity.viewmodel.AuthViewModel;
import com.app.tripfinity.viewmodel.InviteViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InviteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InviteFragment extends Fragment {

    private InviteViewModel inviteViewModel;
    private AuthViewModel authViewModel;
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    EditText text;
    String tripId;
    ArrayList<User> users;
    private RecyclerView recyclerView;
    private InviteUsersAdapter adapter;

    public InviteFragment() {
        // Required empty public constructor
    }

    public static InviteFragment newInstance(String tripId) {
        InviteFragment fragment = new InviteFragment();
        Bundle args = new Bundle();
        args.putString("trip", tripId);
        fragment.setArguments(args);
        return fragment;
    }

    private void initAuthViewModel() {
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInviteViewModel();
        initAuthViewModel();
        if (getArguments() != null) {
            tripId = getArguments().getString("trip");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invite, container, false);
        Button inviteButton = view.findViewById(R.id.addUser);
        text = view.findViewById(R.id.editTextInviteEmail);
        users = new ArrayList<>();
        if(getArguments() != null && getArguments().getSerializable("users") != null){
            users = (ArrayList<User>) getArguments().getSerializable("users");
        }
        recyclerView = view.findViewById(R.id.inviteRecyclerView);
        createRecyclerView();
        inviteButton.setOnClickListener(view1 -> onInviteClicked());
        return view;
    }

    private void initInviteViewModel() {
        inviteViewModel = new ViewModelProvider(requireActivity()).get(InviteViewModel.class);
    }

    private void createRecyclerView() {
        LinearLayoutManager rLayoutManager = new LinearLayoutManager(this.getContext());
        RemoveInviteClickListener listener = position -> {
            users.remove(position);
            adapter.notifyItemRemoved(position);
        };
        adapter = new InviteUsersAdapter(users, listener);

        recyclerView.setLayoutManager(rLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void onInviteClicked(){
        String userEmail = text.getText().toString();
        if(!userEmail.trim().equals("")) {
            inviteViewModel.checkUserExists(userEmail).observe(getViewLifecycleOwner(), user -> {
                if (user == null) {
                    inviteUserToApp(userEmail);
                } else {
                    addToUserList(user);
                    if (tripId != null) {
                        inviteViewModel.sendNotificationToUser(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getDisplayName(), user.getFcmToken(), tripId);
                        inviteViewModel.addUserToTrip(tripId);
                    }
                }
                text.setText("");
            });
        }
    }

    private void addToUserList(User user){
        if(users.stream().anyMatch(user1 -> user1.getEmail().equals(user.getEmail()))){
            Toast.makeText(this.getContext(), "User already added", Toast.LENGTH_SHORT).show();
        }
        else {
            users.add(user);
            adapter.notifyItemInserted(users.size() - 1);
            inviteViewModel.addUser(users);
        }
    }

    private void inviteUserToApp(String email){
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        LayoutInflater inflater = LayoutInflater.from(this.getContext());

        View v = inflater.inflate(R.layout.fragment_invite_user, null);
        TextView textView = v.findViewById(R.id.inviteFragment);
        textView.setText(String.format("The user %s is not registered on Tripfinity. \n Invite them to the app?", email));
        builder.setView(v)
                // Add action buttons
                .setPositiveButton("Invite", (dialog, id) -> {
                    try {
                        User user = new User(email);
                        addToUserList(user);
                        authViewModel.createUser(user, false);
                        inviteViewModel.sendInvitationToUser("", email).observe(getViewLifecycleOwner(), inviteSent -> {
                            if(inviteSent){
                                Toast.makeText(this.getContext(), "Invitation sent", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(this.getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch(Exception e){

                        Toast.makeText(this.getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
        builder.create().show();

    }
}