package com.app.tripfinity.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.tripfinity.R;
import com.app.tripfinity.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class MainActivity extends AppCompatActivity {
    private GoogleSignInClient googleSignInClient;
    private final static String USER = "USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity ","Main Activity launched!!!");
        setContentView(R.layout.activity_main);
        User user = getUserFromIntent();
        initGoogleSignInClient();

        Toast.makeText(this, "Logged in as user "+ user, Toast.LENGTH_LONG);
        Button testCreateTripButton = findViewById(R.id.createTripTest);
        testCreateTripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),TripCreationActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }


    private User getUserFromIntent() {
        return (User) getIntent().getSerializableExtra(USER);
    }

    private void initGoogleSignInClient() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
    }

}