package com.app.tripfinity.view;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.app.tripfinity.R;
import com.app.tripfinity.adapters.ItineraryDaysAdapter;
import com.app.tripfinity.model.Itinerary;
import com.app.tripfinity.model.ItineraryDay;
import com.app.tripfinity.viewmodel.ExpenseCountViewModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class FeedDetailActivity extends AppCompatActivity {

    private static final String TAG = "FeedDetailActivity";
    private String itineraryId;
    private String tripId;
    private String startDate;
    private ExpenseCountViewModel expenseCountViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);

        Intent intent = getIntent();

        itineraryId = intent.getStringExtra("itineraryId");
        tripId = intent.getStringExtra("tripId");
        startDate = intent.getStringExtra("startDate");

        ((TextView)findViewById(R.id.trip_name)).setText(intent.getStringExtra("tripName"));
        ((TextView)findViewById(R.id.date)).setText(ItineraryDaysAdapter.getDateForDay(startDate, 0));

        initMainExpenseViewModel();

        createRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void createRecyclerView() {
        Log.d(TAG,"Creating recycler view for Itinerary: "+itineraryId);
        ArrayList<ItineraryDay> days = new ArrayList<>();
        LinearLayoutManager dataLayoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        //recyclerView.setHasFixedSize(true);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        ItineraryDaysAdapter adapter = new ItineraryDaysAdapter(days, itineraryId, this, startDate, true);
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

    private void initMainExpenseViewModel() {
        expenseCountViewModel = new ViewModelProvider(this).get(ExpenseCountViewModel.class);
    }
}