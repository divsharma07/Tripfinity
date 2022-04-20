package com.app.tripfinity.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.invite_fragment_container_view, InviteFragment.class, null)
                    .commit();
        }

        // Objects.requireNonNull(getSupportActionBar()).setTitle("Invite Friends to Trip");

    }


}