package com.app.tripfinity.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.tripfinity.R;
import com.app.tripfinity.model.Trip;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class FeedFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private ProgressBar progressBar;
    private FirestoreRecyclerAdapter adapter;
    private TextView emptyView;
    private TripFragment.OnItemClickListener listener;

    @androidx.annotation.Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.idProgressBar);


        String user = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        emptyView = view.findViewById(R.id.empty_view);

        db = FirebaseFirestore.getInstance();

        Query query = db.collection("Trips").whereEqualTo("canShare", true);

        FirestoreRecyclerOptions<Trip> options = new FirestoreRecyclerOptions.Builder<Trip>().setQuery(query, Trip.class).build();

        adapter = new FirestoreRecyclerAdapter<Trip, FeedViewHolder>(options) {
            @NonNull
            @Override
            public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item, parent, false);
                Log.d("OnCreateViewHolder", "Called");
                return new FeedViewHolder(view);
            }

            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull FeedViewHolder holder, int position, @NonNull Trip model) {
                if ( (model.getTripName() != null && model.getTripName().length() != 0) && model.getStartDate() != null) {
                    progressBar.setVisibility(View.GONE);
                    Log.d("onBindViewHolder ", "" + model.getTripName());
                    holder.trip_name.setText(model.getTripName());
                    holder.start_date.setText(model.getStartDate() + "");
                    Log.d("Inside onBindViewHolder", "Trip Name -> " + model.getTripName());
                }
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();

                if (getItemCount() != 0) {
                    emptyView.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                }
            }
        };

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManagerWrapper(view.getContext(), LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(adapter);

        setOnItemClickListener((documentSnapshot, position) -> {
            Trip trip = documentSnapshot.toObject(Trip.class);
            String id = documentSnapshot.getId();

            Log.d("TripFragment", "TripName -> " + trip.getTripName());
            Log.d("TripFragment", "TripId -> " + id);
            Log.d("TripFragment", "TripStartDate -> " + trip.getStartDate().toString());
            Log.d("TripFragment", "Trip Itinerary Id -> " + trip.getItinerary().getId());

            Intent intent = new Intent(getActivity(), FeedDetailActivity.class);
            //changed from ItineraryViewActivity to Tripfinity

            intent.putExtra("tripId", id);
            intent.putExtra("tripName", trip.getTripName());
            intent.putExtra("startDate",  trip.getStartDate().toString());
            intent.putExtra("itineraryId", trip.getItinerary().getId());

            Log.d("Inside Fragment",  "Itinerary -> " + trip.getItinerary().getId());
            startActivity(intent);

        });

        return  view;
    }

    private class FeedViewHolder extends RecyclerView.ViewHolder {

        private final TextView trip_name;
        private final TextView start_date;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            trip_name = itemView.findViewById(R.id.trip_name);
            start_date = itemView.findViewById(R.id.start_date);


            itemView.setOnClickListener(view -> {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick((DocumentSnapshot) adapter.getSnapshots().getSnapshot(position), position);
                }
            });

        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(TripFragment.OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
