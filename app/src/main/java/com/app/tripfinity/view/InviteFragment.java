package com.app.tripfinity.view;

import android.app.AlertDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.app.tripfinity.R;
import com.app.tripfinity.adapters.InviteUsersAdapter;
import com.app.tripfinity.model.User;
import com.app.tripfinity.model.UserBio;
import com.app.tripfinity.utils.HelperClass;
import com.app.tripfinity.viewmodel.AuthViewModel;
import com.app.tripfinity.viewmodel.InviteViewModel;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InviteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InviteFragment extends Fragment {

    private InviteViewModel inviteViewModel;
    private AuthViewModel authViewModel;
    EditText text;
    String tripId;
    ArrayList<User> users;
    private RecyclerView recyclerView;
    private InviteUsersAdapter adapter;
    private ProgressBar progressBar;
    private final Handler progressHandler = new Handler();


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
            tripId = getArguments().getString("tripId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invite, container, false);
        ImageButton inviteButton = view.findViewById(R.id.addUser);
        text = view.findViewById(R.id.editTextInviteEmail);
        progressBar = view.findViewById(R.id.progressBar);
        users = new ArrayList<>();
        if(getArguments() != null){
            if(getArguments().getSerializable("users") != null) {
                for (UserBio user : (ArrayList<User>) getArguments().getSerializable("users")) {
                    users.add(new User(user.getUid(), user.getName(), user.getEmail(), user.getUserPhotoUrl(), user.getFcmToken()));
                }
                inviteViewModel.addUser(users);
            }
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
        adapter = new InviteUsersAdapter(users, getContext());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder linkHolder, int direction) {
                int position = linkHolder.getLayoutPosition();
                users.remove(position);
                adapter.notifyItemRemoved(position);
                inviteViewModel.addUser(users);
            }
        });
        if(getArguments() != null && getArguments().getString("tripId") != null){
            progressBar.setVisibility(ProgressBar.VISIBLE);
            tripId = getArguments().getString("tripId");
            inviteViewModel.getUsersInTrip(tripId).observe(getViewLifecycleOwner(), userList -> {
                users.addAll(userList);
                adapter.notifyItemRangeInserted(0, users.size());
                progressHandler.post(() -> {
                    progressBar.setVisibility(ProgressBar.GONE);
                });
            });
        }
        if(tripId == null){
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }

        recyclerView.setLayoutManager(rLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void onInviteClicked(){
        String userEmail = text.getText().toString().trim();
        if(!userEmail.endsWith("@gmail.com")){
            Toast.makeText(this.getContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
        }
        else {
            inviteViewModel.checkUserExists(userEmail).observe(getViewLifecycleOwner(), user -> {
                if (user == null || !user.isRegistered()) {
                    inviteUserToApp(userEmail);
                } else {
                    addToUserList(user);
                    if (tripId != null) {
                        String tripName = getArguments().getString("tripName");
                        inviteViewModel.sendNotificationToUser(HelperClass.getCurrentUser().getDisplayName(), user.getFcmToken(), tripName);
                        inviteViewModel.addUserToTrip(tripId, userEmail);
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
                        if(tripId != null){
                            inviteViewModel.addUserToTrip(tripId, email);
                        }
                        inviteViewModel.sendInvitationToUser(HelperClass.getCurrentUser().getDisplayName(), email).observe(getViewLifecycleOwner(), inviteSent -> {
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