package com.app.tripfinity.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

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
import com.app.tripfinity.viewmodel.AuthViewModel;
import com.app.tripfinity.viewmodel.TripCreationViewModel;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TripCreationActivity extends AppCompatActivity {
    private static final String TAG = "TripCreationActivity";
    private String userId;
    TripCreationViewModel tripCreationViewModel;
    LiveData<Trip> newTrip;
    private String tripId;
    private TextView destination;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;

    private void initTripCreationViewModel() {
        tripCreationViewModel = new ViewModelProvider(this).get(TripCreationViewModel.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_creation2);

        userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        Intent intent = getIntent();
        String buttonType = intent.getStringExtra("displayButtonType");

        if (buttonType.equals("createTrip")) {
            findViewById(R.id.saveEditTrip).setVisibility(View.INVISIBLE);
            findViewById(R.id.createTrip).setVisibility(View.VISIBLE);
        } else if (buttonType.equals("editTrip")){
            findViewById(R.id.saveEditTrip).setVisibility(View.VISIBLE);
            findViewById(R.id.createTrip).setVisibility(View.INVISIBLE);
        }




        initTripCreationViewModel();
        // take trip name from the user
        EditText tripNameInput = findViewById(R.id.tripName);
        // take start date from the user

        TextView startDate = findViewById(R.id.startDateButton);

        Button createTrip = findViewById(R.id.createTrip);

        destination = findViewById(R.id.tripDestination);

        String apiKey = getString(R.string.google_maps_api_key);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }
        PlacesClient placesClient = Places.createClient(this);

        destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Place.Field> fields = Arrays.asList(Place.Field.NAME);

                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                        .setTypeFilter(TypeFilter.CITIES).build(TripCreationActivity.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
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
                            startDate.setText(new StringBuilder().append(year1).append( "-" )
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
                    Log.d(TAG,"Destination given: "+destination.getText().toString());
                    // create a new trip and save in fireStore
                    // need to user view model methods for this
                    try {
                        // create a trip
                        // add this trip id to the users collection
                        tripCreationViewModel.createNewTrip(tripNameInput.getText().toString(),
                                startDate.getText().toString(),userId,destination.getText().toString());

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
                else {
                    Snackbar.make(v, "Please Enter valid Trip name and Start date."
                            ,Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.saveEditTrip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                tripId = intent.getStringExtra("tripId");
                if (tripNameInput.getText().toString().trim().length() > 0 &&
                        startDate.getText().toString().trim().length() > 0) {
                    Log.d(TAG,"Trip Name given: "+tripNameInput.getText().toString());
                    Log.d(TAG,"Start Date given: "+startDate.getText().toString());
                    Log.d(TAG,"Destination given: "+destination.getText().toString());
                    // create a new trip and save in fireStore
                    // need to user view model methods for this
                    // create a trip
                    // add this trip id to the users collection
                    tripCreationViewModel.getTrip(tripId);

                    tripCreationViewModel.getTripLiveData().observe(TripCreationActivity.this,trip -> {
                        Log.d(TAG,"Queried Trip Id: "+trip.getTripId());
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd", Locale.ENGLISH);
                        Date startDateObj = null;
                        try {
                            startDateObj = formatter.parse(startDate.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Date endDateObj = startDateObj;
                        trip.setTripName(tripNameInput.getText().toString());
                        trip.setStartDate(startDateObj);
                        trip.setEndDate(endDateObj);
                        trip.setDestination(destination.getText().toString());

                        // setting trip data.
                        tripCreationViewModel.updateTrip(trip);

                        tripCreationViewModel.getUpdatedTripLiveData().observe(TripCreationActivity.this,trip1 -> {
                            Intent returnIntent = new Intent(TripCreationActivity.this,ItineraryViewActivity.class);
                            returnIntent.putExtra("tripId", trip1.getTripId());
                            returnIntent.putExtra("tripName", trip1.getTripName());
                            returnIntent.putExtra("startDate", trip1.getStartDate());
                            returnIntent.putExtra("itineraryId", trip1.getItinerary().getId());
                            startActivity(returnIntent);
                        });

                    });

                }
                else {
                    Snackbar.make(view, "Please Enter valid Trip name and Start date."
                            ,Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void goToItineraryViewActivity(String tripId,Date startDate,String tripName,String itineraryId) {
        Intent intent = new Intent(TripCreationActivity.this, Tripfinity.class);
        // changed from ItineraryViewActivity to Tripfnity

        intent.putExtra("tripId", tripId);
        intent.putExtra("tripName", tripName);
        intent.putExtra("startDate", startDate);
        intent.putExtra("itineraryId", itineraryId);
        startActivity(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.d(TAG, "Place entered: " + place.getName());
                destination.setText(place.getName());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.d(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}