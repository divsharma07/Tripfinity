package com.app.tripfinity.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;

import com.app.tripfinity.R;
import com.app.tripfinity.viewmodel.AuthViewModel;
import com.app.tripfinity.viewmodel.TripCreationViewModel;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class TripCreationActivity extends AppCompatActivity {
    private static final String TAG = "TripCreationActivity";
    private String userId = "abc@gmail.com";
    TripCreationViewModel tripCreationViewModel;

    private void initTripCreationViewModel() {
        tripCreationViewModel = new ViewModelProvider(this).get(TripCreationViewModel.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_creation2);
        initTripCreationViewModel();
        // take trip name from the user
        EditText tripNameInput = findViewById(R.id.tripName);
        // take start date from the user

        EditText startDate = findViewById(R.id.startDateButton);

        Button createTrip = findViewById(R.id.createTrip);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(TripCreationActivity.this,
                        (view, year1, monthOfYear, dayOfMonth) -> {
                            Date dateFromUser = new Date();
                            // set day of month , month and year value in the edit text
                            startDate.setText(new StringBuilder().append( year1 ).append( "-" )
                                    .append( monthOfYear+1 ).append( "-" ).append( dayOfMonth ));

                        }, year, month, day);
                datePickerDialog.show();
            }

        });

        createTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // all data from the user is there now
                if (tripNameInput.getText().toString().trim().length() > 0 &&
                        startDate.getText().toString().trim().length() > 0) {
                    Log.d(TAG,"Trip Name given: "+tripNameInput.getText().toString());
                    Log.d(TAG,"Start Date given: "+startDate.getText().toString());
                    // create a new trip and save in fireStore
                    // need to user view model methods for this
                    try {
                        tripCreationViewModel.createNewTrip(tripNameInput.getText().toString(),
                                startDate.getText().toString(),userId);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }
            }
        });

    }
}