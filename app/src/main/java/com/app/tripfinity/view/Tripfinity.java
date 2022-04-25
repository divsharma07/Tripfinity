package com.app.tripfinity.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.app.tripfinity.R;
import com.app.tripfinity.utils.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Tripfinity extends AppCompatActivity {
    private Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tripfinity);
        if(getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Intent intent = getIntent();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_menu);
        bundle = new Bundle();
        Log.d("Tripfinity","Destination -> "+intent.getStringExtra(Constants.DESTINATION));
        bundle.putString("tripId", intent.getStringExtra("tripId"));
        bundle.putString("tripName", intent.getStringExtra("tripName"));
        bundle.putString("startDate", intent.getStringExtra("startDate"));
        bundle.putString("itineraryId", intent.getStringExtra("itineraryId"));
        bundle.putString(Constants.DESTINATION, intent.getStringExtra(Constants.DESTINATION));
        bundle.putString(Constants.CAN_SHARE, intent.getStringExtra(Constants.CAN_SHARE));

        bottomNav.setOnNavigationItemSelectedListener(navListener);
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


}
