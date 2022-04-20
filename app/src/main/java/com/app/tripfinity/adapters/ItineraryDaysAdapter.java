package com.app.tripfinity.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.app.tripfinity.R;
import com.app.tripfinity.model.ItineraryDay;
import com.app.tripfinity.viewholders.ItineraryDayViewHolder;

import java.util.List;

public class ItineraryDaysAdapter extends RecyclerView.Adapter<ItineraryDayViewHolder> {
    private static final String TAG = "ItineraryDaysAdapter";
    private List<ItineraryDay> days;
    public ItineraryDaysAdapter(List<ItineraryDay> days) {
        this.days = days;
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
    }

    public void setItems(List<ItineraryDay> days) {
        this.days = days;
    }


    @Override
    public int getItemCount() {
        return days.size();
    }


}

