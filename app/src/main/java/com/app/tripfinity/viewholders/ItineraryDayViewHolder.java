package com.app.tripfinity.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tripfinity.R;
import com.app.tripfinity.model.Place;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ItineraryDayViewHolder extends RecyclerView.ViewHolder {
    public TextView day;
    public TextView date;
    public RecyclerView placesRecyclerView;
    public TextView addPlaces;
    public ItineraryDayViewHolder(@NonNull View itemView, boolean fromFeed) {
        super(itemView);
        day = itemView.findViewById(R.id.dayIndex);
        date = itemView.findViewById(R.id.dayDate);
        placesRecyclerView = itemView.findViewById(R.id.places);
        addPlaces = itemView.findViewById(R.id.placesFloatingActionButton);
        if (fromFeed) {
            addPlaces.setVisibility(View.INVISIBLE);
        } else {
            addPlaces.setVisibility(View.VISIBLE);
        }
        placesRecyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(),
                LinearLayoutManager.VERTICAL, false));
    }
}
