package com.app.tripfinity.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tripfinity.R;
import com.app.tripfinity.viewmodel.InviteViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;


public class InviteActivity extends AppCompatActivity {

    InviteViewModel inviteViewModel;
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        initInviteViewModel();
    }

    private void initInviteViewModel() {
        inviteViewModel = new ViewModelProvider(this).get(InviteViewModel.class);
    }

    public void onInviteClicked(View view){
        EditText text = findViewById(R.id.editTextInviteEmail);
        String userEmail = text.getText().toString();
        inviteViewModel.checkUserExists(userEmail).observe(this, user -> {
            if(user == null) {
                inviteUserToApp(userEmail);
            }
            else {
                inviteViewModel.sendNotificationToUser(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getDisplayName(), user.getFcmToken(), "77nrAgVzOA8xdm2wxPGa");
                inviteViewModel.addUserToTrip("77nrAgVzOA8xdm2wxPGa");
            }
        });
    }


    private void inviteUserToApp(String email){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);

        View v = inflater.inflate(R.layout.fragment_invite_user, null);
        TextView textView = v.findViewById(R.id.inviteFragment);
        textView.setText(String.format("The user %s is not registered on Tripfinity. \n Invite them to the app?", email));
        builder.setView(v)

                // Add action buttons
                .setPositiveButton("Invite", (dialog, id) -> {

                    try {
                        inviteViewModel.sendInvitationToUser("", email).observe(this, inviteSent -> {
                            if(inviteSent){
                                Toast.makeText(this, "Invitation sent", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch(Exception e){

                        Toast.makeText(this, "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", (dialog, id) -> dialog.cancel());
        builder.create().show();

    }
}