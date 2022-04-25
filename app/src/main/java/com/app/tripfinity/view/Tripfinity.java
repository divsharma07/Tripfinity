package com.app.tripfinity.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tripfinity.R;
import com.app.tripfinity.model.Expense;
import com.app.tripfinity.model.Trip;
import com.app.tripfinity.utils.Constants;
import com.app.tripfinity.viewmodel.MainExpenseViewModel;
import com.app.tripfinity.viewmodel.TripfinityViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class Tripfinity extends AppCompatActivity {
    private Bundle bundle;
    private ImageView home;
    private TextView leaveTrip;
    private TripfinityViewModel tripfinityViewModel;
    private FirebaseAuth firebaseAuth;
    private String loggedInUser;
    private String tripId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripfinity);
        initializeAndSetHomeListener();
        Intent intent = getIntent();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_menu);
        bundle = new Bundle();
        bundle.putString(Constants.TRIP_ID, intent.getStringExtra("tripId"));
        bundle.putString(Constants.TRIP_NAME, intent.getStringExtra("tripName"));
        bundle.putString(Constants.TRIP_START_DATE, intent.getStringExtra("startDate"));
        bundle.putString(Constants.ITINERARY_ID, intent.getStringExtra("itineraryId"));
        bundle.putString(Constants.DESTINATION, intent.getStringExtra(Constants.DESTINATION));
        bundle.putString(Constants.CAN_SHARE, intent.getStringExtra(Constants.CAN_SHARE));

        bottomNav.setOnNavigationItemSelectedListener(navListener);
        leaveTrip = findViewById(R.id.leaveTrip);

        tripId = intent.getStringExtra("tripId");
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        loggedInUser = firebaseUser.getEmail();

        tripfinityViewModel = new ViewModelProvider(this).get(TripfinityViewModel.class);
        leaveTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tripfinityViewModel.getExpensesForTrip(tripId).observe(Tripfinity.this, list -> {

                    tripfinityViewModel.getTrip(tripId).observe(Tripfinity.this, t -> {

                        for (Expense expense : list) {
                            if (expense.getAddedByUser().equals(loggedInUser) || expense.getUserIds().contains(loggedInUser)) {
                                Snackbar.make(findViewById(R.id.tripfinityActivity), "You are Involved in Expenses, can't leave!", Snackbar.LENGTH_LONG).show();
                                return;
                            }
                        }

                        Trip trip = t;

//                        Log.d("triper id ", tripId);
                        // Delete from user and trip
                        DocumentReference tripRef = FirebaseFirestore.getInstance().collection("Trips").document(tripId);
                        DocumentReference userRef = FirebaseFirestore.getInstance().collection("Users").document(loggedInUser);

                        tripRef.update("users", FieldValue.arrayRemove(userRef));
                        userRef.update("trips", FieldValue.arrayRemove(tripRef));

                        Toast.makeText(Tripfinity.this, "Successfully Left the Trip!", Toast.LENGTH_LONG).show();

                        // delete trip as well in this case
                        if (trip.getUsers().size() == 1) {
                            tripRef.delete();
                        }
                        finish();
                    });

                });
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, ItineraryViewActivity.class, bundle).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {

                    switch (menuItem.getItemId()) {
                        case R.id.nav_expense:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, ExpenseActivity.class, bundle).commit();
                            break;
                        case R.id.nav_itinerary:
                            Log.d("Tripfinity", "Launching itinerary view from switch case");
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, ItineraryViewActivity.class, bundle).commit();
                            break;
                        case R.id.nav_group:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, InviteFragment.class, bundle).commit();
                            break;
                        case R.id.nav_message:
                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container2, MessagingFragment.class, bundle).commit();
                            break;
                    }


                    return true;
                }
            };

    private void initializeAndSetHomeListener() {
        home = findViewById(R.id.homeButton);
        home.setOnClickListener(v -> finish());
    }

}
