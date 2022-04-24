package com.app.tripfinity.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tripfinity.R;
import com.app.tripfinity.model.ItineraryDay;
import com.app.tripfinity.model.Place;
import com.app.tripfinity.repository.ItineraryRepository;
import com.app.tripfinity.view.AddPlaceActivity;
import com.app.tripfinity.view.ItineraryViewActivity;
import com.app.tripfinity.view.PlaceClickListener;
import com.app.tripfinity.view.PlaceDetailsActivity;
import com.app.tripfinity.view.TripCreationActivity;
import com.app.tripfinity.viewholders.ItineraryDayViewHolder;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ItineraryDaysAdapter extends RecyclerView.Adapter<ItineraryDayViewHolder> implements PlaceClickListener {
    private static final String TAG = "ItineraryDaysAdapter";
    private List<ItineraryDay> days;
    private List<Place> places;
    private String itineraryId;
    private Context context;
    private String startDate;
    private boolean fromFeed;
    public ItineraryDaysAdapter(List<ItineraryDay> days, String itineraryId, Context context,
                                String startDate, boolean fromFeed) {
        this.days = days;
        this.itineraryId = itineraryId;
        this.context = context;
        this.startDate = startDate;
        this.fromFeed = fromFeed;
    }

    @Override
    public ItineraryDayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_layout, parent, false);
        return new ItineraryDayViewHolder(view, fromFeed);
    }

    public static String getDateForDay(String date,int addition) {
        SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",
                Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        try{
            //Setting the date to the given date
            c.setTime(sdf.parse(date));
        }catch(ParseException e){
            e.printStackTrace();
        }

        SimpleDateFormat sdf2 = new SimpleDateFormat("EE MMM dd yyyy",
                Locale.ENGLISH);
        //Number of Days to add
        c.add(Calendar.DAY_OF_MONTH, addition);
        //Date after adding the days to the given date
        String newDate = sdf2.format(c.getTime());
        return newDate;
    }

    @Override
    public void onBindViewHolder(@NonNull ItineraryDayViewHolder holder, int position) {
        Log.d(TAG,"position "+position);
        Log.d(TAG,"startDate "+startDate);
        String dayText = "Day: "+(position+1);
        holder.day.setText(dayText);

        holder.date.setText(getDateForDay(startDate,position));


        PlaceAdapter adapter = new PlaceAdapter(new ArrayList<>(),this,holder.getLayoutPosition());
        holder.placesRecyclerView.setAdapter(adapter);

        if (!fromFeed) {
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {


                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    int placePosition = viewHolder.getLayoutPosition();
                    ItineraryRepository itineraryRepository = ItineraryRepository.getInstance();
                    itineraryRepository.removePlace(itineraryId, days.get(holder.getLayoutPosition()).getId(), placePosition);
                }

            });
            itemTouchHelper.attachToRecyclerView(holder.placesRecyclerView);
        }

        holder.addPlaces.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Onclick on recycler view");
                Intent intent = new Intent(context, AddPlaceActivity.class);
                intent.putExtra("ItineraryId", itineraryId);
                intent.putExtra("dayId", days.get(holder.getBindingAdapterPosition()).getId());
                context.startActivity(intent);
            }
        });
        adapter.setItems(days.get(holder.getBindingAdapterPosition()).getPlaces());
        adapter.notifyDataSetChanged();

    }


    public void setItems(List<ItineraryDay> days) {
        this.days = days;
    }


    @Override
    public int getItemCount() {
        return days.size();
    }


    @Override
    public void OnClickLink(int position, int daysPosition) {

        Intent intent = new Intent(context, PlaceDetailsActivity.class);
        Place place = days.get(daysPosition).getPlaces().get(position);
        intent.putExtra("placeName",place.getName());
        intent.putExtra("notes",place.getNotes());
        intent.putExtra("startTime",place.getStartTime());
        context.startActivity(intent);
    }
}

