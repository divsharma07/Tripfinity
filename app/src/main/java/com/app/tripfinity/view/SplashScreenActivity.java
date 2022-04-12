package com.app.tripfinity.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import com.app.tripfinity.model.User;
import com.app.tripfinity.viewmodel.SplashViewModel;

public class SplashScreenActivity extends AppCompatActivity {
    SplashViewModel splashViewModel;
    private final static String USER = "USER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSplashViewModel();
        checkIfUserIsAuthenticated();
    }

    private void initSplashViewModel() {
        splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
    }

    private void checkIfUserIsAuthenticated() {
        splashViewModel.checkIfUserIsAuthenticated();
        splashViewModel.getIsUserAuthenticatedLiveData().observe(this, user -> {
            if (!user.isUserAuthenticated()) {
                goToAuthInActivity();
                finish();
            } else {
                getUserFromDatabase(user.getEmail());
            }
        });
    }

    private void goToAuthInActivity() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
    }

    private void getUserFromDatabase(String userEmail) {
        splashViewModel.setUid(userEmail);
        splashViewModel.getUserLiveData().observe(this, user -> {
            goToMainActivity(user);
            finish();
        });
    }

    private void goToMainActivity(User user) {
        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
        intent.putExtra(USER, user);
        startActivity(intent);
        finish();
    }

}
