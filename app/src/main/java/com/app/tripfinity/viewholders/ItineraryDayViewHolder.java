package com.app.tripfinity.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tripfinity.R;
import com.app.tripfinity.model.Place;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ItineraryDayViewHolder extends RecyclerView.ViewHolder {
    public TextView day;
    public RecyclerView placesRecyclerView;
    public FloatingActionButton addPlaces;
    public ItineraryDayViewHolder(@NonNull View itemView) {
        super(itemView);
        day = itemView.findViewById(R.id.dayIndex);
        placesRecyclerView = itemView.findViewById(R.id.places);
        addPlaces = itemView.findViewById(R.id.placesFloatingActionButton);
        placesRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(),
                LinearLayoutManager.VERTICAL, false));
    }
}
