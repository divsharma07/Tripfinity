package com.app.tripfinity.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.app.tripfinity.R;

public class PlaceDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);
        Intent intent = getIntent();
        TextView placeName = findViewById(R.id.placeName);
        TextView placeNotes = findViewById(R.id.placeNotes);
        TextView placeStartTime = findViewById(R.id.placeStartTime);

        placeName.setText(intent.getStringExtra("placeName"));

        String placeNotesString = intent.getStringExtra("notes");
        placeNotes.setText(placeNotesString);

        String placeNotesStartTime = intent.getStringExtra("startTime");
        placeStartTime.setText(placeNotesStartTime);

    }
}