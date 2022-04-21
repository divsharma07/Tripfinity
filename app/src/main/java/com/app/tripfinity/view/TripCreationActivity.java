package com.app.tripfinity.view;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.tripfinity.R;
import com.app.tripfinity.model.Trip;
import com.app.tripfinity.model.User;
import com.app.tripfinity.utils.Constants;
import com.app.tripfinity.viewmodel.AuthViewModel;
import com.app.tripfinity.viewmodel.TripCreationViewModel;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TripCreationActivity extends AppCompatActivity {
    private static final String TAG = "TripCreationActivity";
    private String userId = "abc@gmail.com";
    TripCreationViewModel tripCreationViewModel;
    LiveData<Trip> newTrip;
    ArrayList<User> invitedUsers;

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

        TextView startDate = findViewById(R.id.startDateButton);
        Button inviteUsers = findViewById(R.id.inviteUsers);
        Button createTrip = findViewById(R.id.createTrip);
        invitedUsers = new ArrayList<>();

        ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        // Handle the Intent
                        if(intent != null) {
                            Bundle bundle = intent.getBundleExtra("users");
                            if(bundle.getSerializable("users") != null){
                            invitedUsers = (ArrayList<User>) bundle.getSerializable("users");
                            }
                        }
                    }
                });

        inviteUsers.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), InviteActivity.class);
            if(invitedUsers.size() != 0){
                intent.putExtra("users", invitedUsers);
            }
            mStartForResult.launch(intent);
        });

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
                        // create a trip
                        // add this trip id to the users collection

                        List<String> userEmails = invitedUsers.stream().map(User::getEmail).collect(Collectors.toList());
                        userEmails.add(0, userId);

                        tripCreationViewModel.createNewTrip(tripNameInput.getText().toString(),
                                startDate.getText().toString(),userEmails);

                        tripCreationViewModel.getCreatedTripLiveData().observe(TripCreationActivity.this,trip -> {
                            Log.d(TAG,"Created Trip Id: "+trip.getTripId());
                            tripCreationViewModel.createNewItinerary(trip.getTripId());
                            tripCreationViewModel.getCreatedItineraryLiveData().observe(TripCreationActivity.this,itinerary -> {
                                goToItineraryViewActivity(trip.getTripId(),trip.getStartDate(),trip.getTripName(),itinerary.getId());
                            });

                        });

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(invitedUsers != null && invitedUsers.size() != 0){
            TextView userCount = findViewById(R.id.users_count);
            userCount.setText(String.format("%d user(s) invited", invitedUsers.size()));
        }
    }

    private void goToItineraryViewActivity(String tripId, Date startDate, String tripName, String itineraryId) {
        Intent intent = new Intent(TripCreationActivity.this, ItineraryViewActivity.class);
        intent.putExtra("tripId", tripId);
        intent.putExtra("tripName", tripName);
        intent.putExtra("startDate", startDate);
        intent.putExtra("itineraryId", itineraryId);
        startActivity(intent);

    }
}