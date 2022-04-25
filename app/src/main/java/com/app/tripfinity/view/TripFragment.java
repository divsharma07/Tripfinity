package com.app.tripfinity.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tripfinity.R;
import com.app.tripfinity.model.Trip;
import com.app.tripfinity.model.User;
import com.app.tripfinity.repository.TripRepository;
import com.app.tripfinity.utils.Constants;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.zip.Inflater;

public class TripFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseFirestore db;
    private ProgressBar progressBar;
    private FirestoreRecyclerAdapter adapter;
    private TextView emptyView;
    private OnItemClickListener listener;
    private FloatingActionButton addButton;


    public static String getReadableDate(Date date) {
        SimpleDateFormat sdf2 = new SimpleDateFormat("EE MMM dd yyyy",
                Locale.ENGLISH);

        String readableDate = sdf2.format(date);
        return readableDate;
    }

    @androidx.annotation.Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trips, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.idProgressBar);

        addButton = (FloatingActionButton) view.findViewById(R.id.fab_add);

        String user = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

        emptyView = view.findViewById(R.id.empty_view);

        db = FirebaseFirestore.getInstance();

        DocumentReference documentReference = db.collection("Users").document(user);

        Query query = db.collection("Trips").whereArrayContains("users", documentReference)
                .orderBy("startDate");

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
                    progressBar.setVisibility(View.GONE);
                    Log.d("onBindViewHolder ", "" + model.getTripName());
                    holder.trip_name.setText(model.getTripName());


                    holder.start_date.setText(getReadableDate(model.getStartDate()));
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
            Log.d("TripFragment", "TripStartDate -> " + trip.getStartDate());
            Log.d("TripFragment", "Trip Itinerary Id -> " + trip.getItinerary().getId());

            Intent intent = new Intent(getActivity(), Tripfinity.class);
            //changed from ItineraryViewActivity to Tripfinity

            intent.putExtra(Constants.TRIP_ID, id);
            intent.putExtra(Constants.TRIP_NAME, trip.getTripName());
            intent.putExtra(Constants.TRIP_START_DATE, trip.getStartDate().toString());
            intent.putExtra(Constants.ITINERARY_ID, trip.getItinerary().getId());
            intent.putExtra(Constants.CAN_SHARE, trip.isCanShare());
            intent.putExtra(Constants.DESTINATION, trip.getDestination());

            Log.d("Inside Fragment",  "Itinerary -> " + trip.getItinerary().getId());
            startActivity(intent);

        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // remove trip at a given position
                // remove trip document
                // remove trip id from users collection
                // remove expenses with the given trip id
                int position = viewHolder.getLayoutPosition();
                TripRepository tripRepository = TripRepository.getInstance();
                Trip tripObj = (Trip) adapter.getItem(position);
                tripRepository.deleteTrip(tripObj.getTripId());


            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
        addButtonClick();
        
        return  view;
    }

    private class TripViewHolder extends RecyclerView.ViewHolder {

        private final TextView trip_name;
        private final TextView start_date;

        public TripViewHolder(@NonNull View itemView) {
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

    public void setOnItemClickListener(OnItemClickListener listener) {
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

    private void addButtonClick() {
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(),TripCreationActivity.class);
            intent.putExtra("displayButtonType", "createTrip");
            v.getContext().startActivity(intent);

        });
    }
}
