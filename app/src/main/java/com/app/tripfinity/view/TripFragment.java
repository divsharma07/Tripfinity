package com.app.tripfinity.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tripfinity.R;
import com.app.tripfinity.model.Trip;
import com.app.tripfinity.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.zip.Inflater;

public class TripFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private ArrayList<Trip> trips;
    private TripAdapter tripAdapter;
    private ProgressBar progressBar;
    private FirestoreRecyclerAdapter adapter;
    private TextView emptyView;

    @androidx.annotation.Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trips, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.idProgressBar);

        String user = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        //Toast.makeText(getActivity(), "-- User- -- -- " + user, Toast.LENGTH_SHORT).show();

        emptyView = view.findViewById(R.id.empty_view);

        db = FirebaseFirestore.getInstance();

        final int[] tripCount = {0};



        Trip trip = new Trip();
        trip.setTripName("Trip1");
        trip.setStartDate(new Date());

        DocumentReference documentReference = db.collection("Users").document(user);

        trips = new ArrayList<>();

        trips.add(trip);
        trips.add(trip);
        trips.add(trip);

        //tripAdapter = new TripAdapter(trips, getActivity());

        //recyclerView.setAdapter(tripAdapter);

        //progressBar.setVisibility(View.GONE);

        Query query = db.collection("TripTest").whereArrayContains("users", documentReference);

        FirestoreRecyclerOptions<Trip> options = new FirestoreRecyclerOptions.Builder<Trip>().setQuery(query, Trip.class).build();

        adapter = new FirestoreRecyclerAdapter<Trip, TripViewHolder>(options) {
            @NonNull
            @Override
            public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_item, parent, false);
                Log.d("OnCreateViewHolder", "Called");
                return new TripViewHolder(view);
            }

            @SuppressLint("SetTextI18n")
            @Override
            protected void onBindViewHolder(@NonNull TripViewHolder holder, int position, @NonNull Trip model) {
                if ( (model.getTripName() != null && model.getTripName().length() != 0) && model.getStartDate() != null) {
                    tripCount[0]++;
                    progressBar.setVisibility(View.GONE);
                    holder.trip_name.setText(model.getTripName());
                    holder.start_date.setText(model.getStartDate() + "");
                    Log.d("Inside onBindViewHolder", "Trip Name -> " + model.getTripName());
                }
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();

                if (getItemCount() != 0) {
                    //Toast.makeText(getActivity(), "Awesome", Toast.LENGTH_SHORT).show();
                    emptyView.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.GONE);
                    //Toast.makeText(getActivity(), "You are not into any trips :/", Toast.LENGTH_SHORT).show();
                    emptyView.setVisibility(View.VISIBLE);
                }
            }
        };

//        if (tripCount[0] == 0) {
//            progressBar.setVisibility(View.GONE);
//            Toast.makeText(getActivity(), "You are not into any trips :/", Toast.LENGTH_SHORT).show();
//
//        }

        recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        recyclerView.setLayoutManager(new LinearLayoutManagerWrapper(view.getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        //Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();

        //progressBar.setVisibility(View.GONE);

        //db.collection("Users").document(user).get();

        //db.collection("Trips").document()
        
        return  view;
    }

    private class TripViewHolder extends RecyclerView.ViewHolder {

        private final TextView trip_name;
        private final TextView start_date;

        public TripViewHolder(@NonNull View itemView) {
            super(itemView);
            trip_name = itemView.findViewById(R.id.trip_name);
            start_date = itemView.findViewById(R.id.start_date);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int position = getAbsoluteAdapterPosition();
                }
            });
        }
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
