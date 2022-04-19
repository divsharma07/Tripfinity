package com.app.tripfinity.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tripfinity.R;

public class ItineraryDayViewHolder extends RecyclerView.ViewHolder {
    public TextView day;
    public ItineraryDayViewHolder(@NonNull View itemView) {
        super(itemView);
        day = itemView.findViewById(R.id.dayIndex);
    }
}
