package com.app.tripfinity.utils;

import android.util.Log;

import com.app.tripfinity.model.User;
import com.app.tripfinity.utils.Constants.*;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Map;

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

    public static Task<String> callFunction(String function, Map<String, Object> data) {
        FirebaseFunctions fn = FirebaseFunctions.getInstance();
        return fn
                .getHttpsCallable(function)
                .call(data)
                .continueWith(task -> (String) task.getResult().getData());
    }
}
