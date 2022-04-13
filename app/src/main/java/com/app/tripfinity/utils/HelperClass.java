package com.app.tripfinity.utils;

import android.util.Log;
import com.app.tripfinity.utils.Constants.*;

public class HelperClass {
    public static void logErrorMessage(String errorMessage) {
        Log.d(Constants.FIREBASE_AUTH_TAG, errorMessage);
    }
}
