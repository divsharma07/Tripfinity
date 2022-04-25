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
import com.app.tripfinity.viewmodel.ItineraryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.auth.FirebaseAuth;
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
    private ImageView home;
    private String startDate;
    private String tripNameString;
    private String destination;

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
            tripId = getArguments().getString("tripId");
            tripNameString = getArguments().getString("tripName");
            itineraryId = getArguments().getString("itineraryId");
            startDate = getArguments().getString("startDate");
            destination = getArguments().getString("destination");

        }

        Log.d(TAG, "Id ->" + itineraryId);
        Log.d(TAG, "Start Date in fragment ->" + startDate);
        TextView tripName = getView().findViewById(R.id.tripNameTextView);
        tripName.setText(tripNameString);
        FloatingActionButton addDaysButton = getView().findViewById(R.id.floatingActionButton);
        editTrip = getView().findViewById(R.id.editTrip);

        TextView startDateView = getView().findViewById(R.id.startDate);
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
                intent.putExtra("tripId", tripId);
                intent.putExtra("tripName", tripNameString);
                intent.putExtra("startDate",startDate);
                intent.putExtra("destination",destination);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        createRecyclerView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeAndSetHomeListener();
    }

    private void initializeAndSetHomeListener() {
        home = getView().findViewById(R.id.homeButton);
        home.setOnClickListener(v -> {
            if(getActivity()!=null) {
                getActivity().finish();
            }
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