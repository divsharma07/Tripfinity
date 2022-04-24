package com.app.tripfinity.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.app.tripfinity.R;
import com.app.tripfinity.viewmodel.PlaceViewModel;
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


import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class AddPlaceActivity extends AppCompatActivity {
    private static final String TAG = "AddPlaceActivity";
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private TextView searchBar;
    private String ItineraryId;
    private String dayId;
    PlaceViewModel placeViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_place);
        initPlaceModel();
        String apiKey = getString(R.string.google_maps_api_key);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }
        PlacesClient placesClient = Places.createClient(this);

        searchBar = findViewById(R.id.searchPlace);

        EditText notes = findViewById(R.id.addPlaceNotes);

        TextView startTime = findViewById(R.id.addTime);
        ItineraryId = getIntent().getStringExtra("ItineraryId");
        dayId = getIntent().getStringExtra("dayId");

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mHour = c.get(Calendar.HOUR_OF_DAY);
                int mMinute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddPlaceActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        startTime.setText(hourOfDay+":"+minute);
                    }
                }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });


        searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the fields to specify which types of place data to
                // return after the user has made a selection.
                List<Place.Field> fields = Arrays.asList(Place.Field.NAME);

                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                        .build(AddPlaceActivity.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });

        Button savePlace = findViewById(R.id.savePlace);
        savePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (searchBar.getText().toString().trim().length()>0) {
                    // add the place to the database
                    placeViewModel.addPlace(ItineraryId,dayId,searchBar.getText().toString(),
                            notes.getText().toString(),startTime.getText().toString());

                    placeViewModel.getPlacesLiveData().observe(AddPlaceActivity.this,itinerary -> {
                        Log.d(TAG,"Finishing Activity");
                        finish();
                    });
                }
                else {
                    Snackbar.make(v, "Please Enter valid Place"
                            ,Snackbar.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void initPlaceModel() {
        placeViewModel = new ViewModelProvider(this).get(PlaceViewModel.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.d(TAG, "Place entered: " + place.getName());
                searchBar.setText(place.getName());
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