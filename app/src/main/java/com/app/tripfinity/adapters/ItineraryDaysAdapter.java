package com.app.tripfinity.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tripfinity.R;
import com.app.tripfinity.model.ItineraryDay;
import com.app.tripfinity.model.Place;
import com.app.tripfinity.view.AddPlaceActivity;
import com.app.tripfinity.view.ItineraryViewActivity;
import com.app.tripfinity.view.TripCreationActivity;
import com.app.tripfinity.viewholders.ItineraryDayViewHolder;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;


import java.util.ArrayList;
import java.util.List;

public class ItineraryDaysAdapter extends RecyclerView.Adapter<ItineraryDayViewHolder> {
    private static final String TAG = "ItineraryDaysAdapter";
    private List<ItineraryDay> days;
    private List<Place> places;
    private String itineraryId;
    private Context context;
    public ItineraryDaysAdapter(List<ItineraryDay> days, String itineraryId, Context context) {
        this.days = days;
        places = new ArrayList<>();
        this.itineraryId = itineraryId;
        this.context = context;
    }

    @Override
    public ItineraryDayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_layout, parent, false);
        return new ItineraryDayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItineraryDayViewHolder holder, int position) {
        Log.d(TAG,"Binding days "+days.get(0));
        Log.d(TAG,"position "+position);
        holder.day.setText(days.get(position).getId());
        Log.d(TAG,"createRecyclerUserView called ");

        PlaceAdapter adapter = new PlaceAdapter(places);
        holder.placesRecyclerView.setAdapter(adapter);

        holder.addPlaces.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Onclick on recycler view");
                Intent intent = new Intent(context, AddPlaceActivity.class);
                context.startActivity(intent);
            }
        });


    }


    public void setItems(List<ItineraryDay> days) {
        this.days = days;
    }


    @Override
    public int getItemCount() {
        return days.size();
    }


}

