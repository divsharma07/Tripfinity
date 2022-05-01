package com.app.tripfinity.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.app.tripfinity.model.User;
import com.app.tripfinity.utils.Constants.*;
import android.widget.ImageView;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    public static String getParsedTimestamp(Timestamp timestamp) {
        String[] splitTimestamp = timestamp.toDate().toString().split(" ");
        String parsedTimestamp = "";
        if(splitTimestamp.length > 5) {
            parsedTimestamp = String.format("%s %s %s %s", splitTimestamp[1], splitTimestamp[2],
                    splitTimestamp[3], splitTimestamp[4]);
        }
        return parsedTimestamp;
    }

    public static void displayRoundImageFromUrl(final Context context, final String url,
                                                final ImageView imageView, int placeholderResId) {
        RequestOptions myOptions = new RequestOptions()
                .centerCrop()
                .dontAnimate();

        Glide.with(context)
                .asBitmap()
                .apply(myOptions)
                .load(url)
                .placeholder(placeholderResId)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    public static FirebaseUser getCurrentUser() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        return firebaseAuth.getCurrentUser();
    }
}
