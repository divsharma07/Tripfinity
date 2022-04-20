package com.app.tripfinity.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.TextView;

import com.app.tripfinity.R;
import com.app.tripfinity.adapters.ItineraryDaysAdapter;
import com.app.tripfinity.model.Itinerary;
import com.app.tripfinity.model.ItineraryDay;
import com.app.tripfinity.viewmodel.ItineraryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


import java.util.ArrayList;
import java.util.List;

public class ItineraryViewActivity extends AppCompatActivity {
    private static final String TAG = "ItineraryViewActivity";
    private ItineraryViewModel itineraryViewModel;
    private String tripId;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager dataLayoutManager;
    private ItineraryDaysAdapter adapter;
    private String itineraryId;
    private List<ItineraryDay> days;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"On create Called");
        setContentView(R.layout.activity_itinerary_view);
        initItineraryViewModel();
        Intent intent = getIntent();
        itineraryId = intent.getStringExtra("itineraryId");
        tripId = intent.getStringExtra("tripId");
        TextView tripName = findViewById(R.id.tripNameTextView);
        tripName.setText(intent.getStringExtra("tripName"));
        FloatingActionButton addDaysButton = findViewById(R.id.floatingActionButton);

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

    }


//    private void createRecyclerView() {
//        Log.d("ItineraryViewActivity","createRecyclerUserView called "+itineraryId);
//        dataLayoutManager = new LinearLayoutManager(this);
//        recyclerView = findViewById(R.id.days);
//        recyclerView.setHasFixedSize(true);
//        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
//        recyclerView.setLayoutManager(dataLayoutManager);
//        Query query = firestore.collection("Itinerary").whereEqualTo(FieldPath.documentId(),itineraryId);
//
//
//        options = new FirestoreRecyclerOptions.Builder<Itinerary>()
//                .setQuery(query, Itinerary.class)
//                .build();
//
//        adapter = new FirestoreRecyclerAdapter<Itinerary, ItineraryDaysViewHolder>(options) {
//            @NonNull
//            @Override
//            public ItineraryDaysViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                Log.d("ItineraryViewActivity","onCreateViewHolder!!!! ");
//                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.day_layout,parent,false);
//                return new ItineraryDaysViewHolder(view);
//            }
//
//            @Override
//            protected void onBindViewHolder(@NonNull ItineraryDaysViewHolder holder, int position, @NonNull Itinerary model) {
//                holder.day.setText(model.getDays().get(position).getId());
//                Log.d("ItineraryViewActivity","model ID "+model.getDays().get(position).getId());
//            }
//
//        };
//        recyclerView.setAdapter(adapter);
//
//
//
//    }


    @Override
    protected void onResume() {
        super.onResume();
        createRecyclerView();
    }

    private void createRecyclerView() {
        Log.d(TAG,"Creating recycler view for Itinerary: "+itineraryId);
        days = new ArrayList<>();
        dataLayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.days);
        recyclerView.setHasFixedSize(true);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        adapter = new ItineraryDaysAdapter(days,itineraryId,this);
        recyclerView.setLayoutManager(dataLayoutManager);
        recyclerView.setAdapter(adapter);
        firestore.collection("Itinerary").document(itineraryId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Itinerary itinerary = value.toObject(Itinerary.class);
                adapter.setItems(itinerary.getDays());
                adapter.notifyDataSetChanged();

            }
        });


    }


    private void initItineraryViewModel() {
        itineraryViewModel = new ViewModelProvider(this).get(ItineraryViewModel.class);
    }


}