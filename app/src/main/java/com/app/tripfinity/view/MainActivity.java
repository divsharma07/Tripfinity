package com.app.tripfinity.view;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.camera2.TotalCaptureResult;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import com.app.tripfinity.R;
import com.app.tripfinity.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.security.PrivilegedAction;

public class MainActivity extends AppCompatActivity {
    private GoogleSignInClient googleSignInClient;
    private final static String USER = "USER";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        User user = getUserFromIntent();

        initGoogleSignInClient();
        Toast.makeText(this, "Logged in as user "+ user, Toast.LENGTH_LONG);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TripFragment()).commit();
        //Toast.makeText(this, user.getEmail(), Toast.LENGTH_SHORT).show();



    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    Fragment fragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.nav_trips:
                            fragment = new TripFragment();
                            break;
                        case R.id.nav_feeds:
                            fragment = new FeedFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                    return true;
                }
            };


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