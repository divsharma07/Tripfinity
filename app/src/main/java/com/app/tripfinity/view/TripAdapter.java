package com.app.tripfinity.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.app.tripfinity.R;

import java.util.ArrayList;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.ViewHolder> {

    private ArrayList<TripDetails> listTrips;
    private Context context;

    public TripAdapter(ArrayList<TripDetails> listTrips, Context context) {
        this.listTrips = listTrips;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.trip_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TripDetails tripDetails = listTrips.get(position);
        holder.trip_name.setText(tripDetails.getTrip_name());
        holder.start_date.setText(tripDetails.getStart_date());
    }

    @Override
    public int getItemCount() {
        return listTrips.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView trip_name;
        private final TextView start_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            trip_name = itemView.findViewById(R.id.trip_name);
            start_date = itemView.findViewById(R.id.start_date);
        }
    }
}