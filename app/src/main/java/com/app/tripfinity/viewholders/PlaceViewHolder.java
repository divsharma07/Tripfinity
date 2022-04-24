package com.app.tripfinity.viewholders;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tripfinity.R;
import com.app.tripfinity.view.PlaceClickListener;

public class PlaceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView place;
    public PlaceClickListener placeClickListener;
    public int daysPosition;
    public TextView startTime;
    public PlaceViewHolder(@NonNull View itemView, PlaceClickListener placeClickListener, int daysPosition) {
        super(itemView);
        place = itemView.findViewById(R.id.placeIndex);
        startTime = itemView.findViewById(R.id.placeTime);

        this.daysPosition = daysPosition;
        itemView.setOnClickListener(this);
        this.placeClickListener = placeClickListener;
    }

    @Override
    public void onClick(View v) {
        int position = getLayoutPosition();
        if (position != RecyclerView.NO_POSITION) {
            placeClickListener.OnClickLink(position,daysPosition);
        }
    }
}
