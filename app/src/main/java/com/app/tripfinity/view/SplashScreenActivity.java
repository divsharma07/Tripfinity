package com.app.tripfinity.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import com.app.tripfinity.model.User;
import com.app.tripfinity.utils.Constants;
import com.app.tripfinity.viewmodel.SplashViewModel;

import java.io.Serializable;

public class SplashScreenActivity extends AppCompatActivity implements Serializable {
    SplashViewModel splashViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initSplashViewModel();
        super.onCreate(savedInstanceState);
        checkIfUserIsAuthenticated();
    }

    private void initSplashViewModel() {
        splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);
    }

    private void checkIfUserIsAuthenticated() {
        splashViewModel.checkIfUserIsAuthenticated();
        splashViewModel.getIsUserAuthenticatedLiveData().observe(this, user -> {
            if (!user.isAuthenticated) {
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
        splashViewModel.setUserEmail(userEmail);
        splashViewModel.getUserLiveData().observe(this, user -> {
            goToMainActivity(user);
            finish();
        });
    }

    private void goToMainActivity(User user) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //intent.putExtra(Constants.USER, user);
        startActivity(intent);
    }
}
