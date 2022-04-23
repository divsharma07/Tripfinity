package com.app.tripfinity.viewholders;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tripfinity.R;

public class PlaceViewHolder extends RecyclerView.ViewHolder {
    public TextView place;
    public PlaceViewHolder(@NonNull View itemView) {
        super(itemView);
        place = itemView.findViewById(R.id.placeIndex);
    }
}
