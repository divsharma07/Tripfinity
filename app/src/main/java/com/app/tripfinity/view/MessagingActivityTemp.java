package com.app.tripfinity.view;

import static com.app.tripfinity.utils.HelperClass.getCurrentUser;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.app.tripfinity.R;
import com.app.tripfinity.model.Message;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MessagingActivityTemp extends AppCompatActivity {
    // TODO : Remove this activity and integerate with the main activity that has the tab view
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging_temp);
    }
}