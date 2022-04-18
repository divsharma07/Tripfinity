package com.app.tripfinity.utils;

import android.util.Log;
import com.app.tripfinity.utils.Constants.*;
import com.google.firebase.messaging.FirebaseMessaging;

public class HelperClass {
    public static void logErrorMessage(String errorMessage) {
        Log.d(Constants.FIREBASE_AUTH_TAG, errorMessage);
    }

    public static void enableFCM() {
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
    }

    public static void disableFCM() {
        FirebaseMessaging.getInstance().setAutoInitEnabled(false);
        new Thread(() -> {
            FirebaseMessaging.getInstance().deleteToken();
        }).start();
    }
}
