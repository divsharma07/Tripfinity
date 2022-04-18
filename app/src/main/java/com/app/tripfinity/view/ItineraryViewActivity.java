package com.app.tripfinity.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.tripfinity.R;
import com.app.tripfinity.viewmodel.ItineraryViewModel;
import com.app.tripfinity.viewmodel.TripCreationViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ItineraryViewActivity extends AppCompatActivity {
    private ItineraryViewModel itineraryViewModel;
    private String tripId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary_view);
        initItineraryViewModel();
        Intent intent = getIntent();
        tripId = intent.getStringExtra("tripId");
        FloatingActionButton addDaysButton = findViewById(R.id.floatingActionButton);
        addDaysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if itinerary does not exist then it needs to be created and
                // the day should be added.
                // else a new day should be added to the existing itinerary.
                itineraryViewModel.updateItinerary(tripId);
                // once the itinerary is created, get all the days from it and
                // display in the recycler view.

            }
        });

    }
    private void initItineraryViewModel() {
        itineraryViewModel = new ViewModelProvider(this).get(ItineraryViewModel.class);
    }
    // Need to add dynamic elements inside recycler view.




    // if an itinerary with the trip id exists then display all the days of that itinerary
    //      display all the places for a day
    //      for each place display the place name which should be clickable

}