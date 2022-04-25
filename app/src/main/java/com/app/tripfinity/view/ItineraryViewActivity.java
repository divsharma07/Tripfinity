package com.app.tripfinity.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tripfinity.R;
import com.app.tripfinity.adapters.ItineraryDaysAdapter;
import com.app.tripfinity.model.Itinerary;
import com.app.tripfinity.model.ItineraryDay;
import com.app.tripfinity.utils.Constants;
import com.app.tripfinity.viewmodel.ItineraryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ItineraryViewActivity extends Fragment {
    private static final String TAG = "ItineraryViewActivity";
    private ItineraryViewModel itineraryViewModel;
    private String tripId;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager dataLayoutManager;
    private ItineraryDaysAdapter adapter;
    private String itineraryId;
    private List<ItineraryDay> days;
    private ImageView editTrip;
    private String startDate;
    private String tripNameString;
    private String destination;
    private TextView tripName;
    private TextView startDateView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_itinerary_view, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"On create Called");
        initItineraryViewModel();
        tripNameString = "";
        if (getArguments() != null) {
            tripId = getArguments().getString(Constants.TRIP_ID);
            tripNameString = getArguments().getString(Constants.TRIP_NAME);
            itineraryId = getArguments().getString(Constants.ITINERARY_ID);
            startDate = getArguments().getString(Constants.TRIP_START_DATE);
            destination = getArguments().getString(Constants.DESTINATION);

        }

        Log.d(TAG, "Id ->" + itineraryId);
        Log.d(TAG, "Start Date in fragment ->" + startDate);
        Log.d(TAG, "Original destination ->" + destination);
        tripName = getView().findViewById(R.id.tripNameTextView);



        tripName.setText(tripNameString);
        FloatingActionButton addDaysButton = getView().findViewById(R.id.floatingActionButton);
        editTrip = getView().findViewById(R.id.editTrip);

        startDateView = getView().findViewById(R.id.startDate);


        startDateView.setText(ItineraryDaysAdapter.getDateForDay(startDate,0));
        addDaysButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if itinerary does not exist then it needs to be created and
                // the day should be added.
                // else a new day should be added to the existing itinerary.

                itineraryViewModel.updateItinerary(tripId);
                // once the itinerary is created, get all the days from it and
                // display in the recycler view.


            }
        });

        editTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TripCreationActivity.class);
                intent.putExtra("displayButtonType", "editTrip");
                intent.putExtra(Constants.TRIP_ID, tripId);
                intent.putExtra(Constants.TRIP_NAME, tripNameString);
                intent.putExtra(Constants.TRIP_START_DATE,startDate);
                intent.putExtra(Constants.DESTINATION,destination);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        createRecyclerView();
        itineraryViewModel.getTrip(tripId);
        itineraryViewModel.getTripLiveData().observe(getActivity(),trip -> {
            tripName.setText(trip.getTripName());
            startDateView.setText(TripFragment.getReadableDate(trip.getStartDate()));
        });


    }

    private void createRecyclerView() {
        Log.d(TAG,"Creating recycler view for Itinerary: "+itineraryId);
        days = new ArrayList<>();
        dataLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView = getView().findViewById(R.id.days);
        //recyclerView.setHasFixedSize(true);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        adapter = new ItineraryDaysAdapter(days,itineraryId,getActivity(),startDate, false);
        recyclerView.setLayoutManager(dataLayoutManager);
        recyclerView.setAdapter(adapter);
        Log.d(TAG, "ItineraryId ->" + itineraryId);
        firestore.collection( "Itinerary").document(itineraryId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Itinerary itinerary = value.toObject(Itinerary.class);
                Log.d("onEvent", "Itinerary id ->" + itineraryId);
                Log.d("onEvent", "Days ->" + itinerary.getDays());
                adapter.setItems(itinerary.getDays());
                adapter.notifyDataSetChanged();

            }
        });


    }


    private void initItineraryViewModel() {
        itineraryViewModel = new ViewModelProvider(getActivity()).get(ItineraryViewModel.class);
    }


}