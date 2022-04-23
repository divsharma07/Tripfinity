package com.app.tripfinity.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tripfinity.R;
import com.app.tripfinity.model.Place;
import com.app.tripfinity.viewholders.PlaceViewHolder;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceViewHolder> {
    private static final String TAG = "PlaceAdapter";
    List<Place> placeList;
    public PlaceAdapter(List<Place> placeList) {
        this.placeList = placeList;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder for place called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_layout, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        holder.place.setText(placeList.get(position).getName());
        Log.d(TAG,"Setting place "+placeList.get(position));
    }

    public void setItems(List<Place> placeList) {
        this.placeList = placeList;
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }
}
