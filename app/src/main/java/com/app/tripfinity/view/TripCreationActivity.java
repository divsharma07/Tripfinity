package com.app.tripfinity.view;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.app.tripfinity.R;
import com.app.tripfinity.adapters.ItineraryDaysAdapter;
import com.app.tripfinity.model.Trip;
import com.app.tripfinity.model.User;
import com.app.tripfinity.model.User;
import com.app.tripfinity.model.UserBio;
import com.app.tripfinity.utils.Constants;
import com.app.tripfinity.utils.HelperClass;
import com.app.tripfinity.viewmodel.AuthViewModel;
import com.app.tripfinity.viewmodel.TripCreationViewModel;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TripCreationActivity extends AppCompatActivity {
    private static final String TAG = "TripCreationActivity";
    private String userId;
    TripCreationViewModel tripCreationViewModel;
    LiveData<Trip> newTrip;
    private String tripId;
    private TextView destination;
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    ArrayList<UserBio> invitedUsers;

    private String originalStartDate;
    private String originalTripName;
    private String originalDestination;
    private boolean originalCanShare;

    private void initTripCreationViewModel() {
        tripCreationViewModel = new ViewModelProvider(this).get(TripCreationViewModel.class);
    }


    public static String getDateForDay(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-dd",
                Locale.ENGLISH);
        Calendar c = Calendar.getInstance();
        try{
            //Setting the date to the given date
            c.setTime(sdf.parse(date));
        }catch(ParseException e){
            try {
                SimpleDateFormat sdf2 = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",
                        Locale.ENGLISH);
                c.setTime(sdf2.parse(date));
            }catch (ParseException ex) {
                try {
                    SimpleDateFormat sdf3 = new SimpleDateFormat("EE MMM dd yyyy",
                            Locale.ENGLISH);
                    c.setTime(sdf3.parse(date));
                }catch (ParseException exception) {

                }

            }
        }

        SimpleDateFormat sdf2 = new SimpleDateFormat("EE MMM dd yyyy",
                Locale.ENGLISH);

        //Date after adding the days to the given date
        String newDate = sdf2.format(c.getTime());
        return newDate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_creation2);

        initTripCreationViewModel();

        userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        originalStartDate = "";
        originalTripName = "";
        originalDestination = "";
        Intent intent = getIntent();
        // take trip name from the user
        EditText tripNameInput = findViewById(R.id.tripName);
        // take start date from the user
        TextView startDate = findViewById(R.id.startDateButton);
        TextView inviteUsers = findViewById(R.id.inviteUsers);
        Button createTrip = findViewById(R.id.createTrip);
        destination = findViewById(R.id.tripDestination);
        String buttonType = intent.getStringExtra("displayButtonType");

        if (buttonType.equals("createTrip")) {
            ((Button)findViewById(R.id.saveEditTrip)).setVisibility(View.INVISIBLE);
            ((Button)findViewById(R.id.createTrip)).setVisibility(View.VISIBLE);


        } else if (buttonType.equals("editTrip")){
            Log.d("TripCreationAvtivity", "buttonType -> editTrip");
            ((Button)findViewById(R.id.saveEditTrip)).setVisibility(View.VISIBLE);
            ((Button)findViewById(R.id.saveEditTrip)).setText("Edit");
            ((Button)findViewById(R.id.createTrip)).setVisibility(View.INVISIBLE);
            TextView textView = findViewById(R.id.mode);
            textView.setText("Edit Trip");


            tripId = intent.getStringExtra(Constants.TRIP_ID);
            tripCreationViewModel.getTrip(tripId);

            tripCreationViewModel.getTripLiveData().observe(TripCreationActivity.this,trip -> {
                ((Switch)findViewById(R.id.isShareableSwitch)).setChecked(trip.isCanShare());
                tripNameInput.setText(trip.getTripName());
                destination.setText(trip.getDestination());
                startDate.setText(TripFragment.getReadableDate(trip.getStartDate()));
            });





        }




        initTripCreationViewModel();

        invitedUsers = new ArrayList<>();

        ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent resultIntent = result.getData();
                        // Handle the Intent
                        if(resultIntent != null) {
                            Bundle bundle = resultIntent.getBundleExtra("users");
                            if(bundle != null && bundle.getSerializable("users") != null){
                            invitedUsers = (ArrayList<UserBio>) bundle.getSerializable("users");
                            }
                            else{
                                invitedUsers = new ArrayList<>();
                            }
                        }
                    }
                });

        inviteUsers.setOnClickListener(view -> {
            Intent resultIntent = new Intent(view.getContext(), InviteActivity.class);
            if(invitedUsers.size() != 0){
                resultIntent.putExtra("users", invitedUsers);
            }
            mStartForResult.launch(resultIntent);
        });




        String apiKey = getString(R.string.google_maps_api_key);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }
        PlacesClient placesClient = Places.createClient(this);

        destination.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.NAME);

            // Start the autocomplete intent.
            Intent intent1 = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                    .setTypeFilter(TypeFilter.CITIES).build(TripCreationActivity.this);
            startActivityForResult(intent1, AUTOCOMPLETE_REQUEST_CODE);
        });

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(TripCreationActivity.this,
                        (view, year1, monthOfYear, dayOfMonth) -> {
                            // set day of month , month and year value in the edit text
                              String date = getDateForDay(new StringBuilder().append(year1).append( "-" )
                                        .append( monthOfYear+1 ).append( "-" ).append( dayOfMonth ).toString());

                            startDate.setText(date);

                        }, year, month, day);
                datePickerDialog.show();
            }

        });
        // can share logic
        Switch toggle = findViewById(R.id.isShareableSwitch);
        createTrip.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                // all data from the user is there now

                if (tripNameInput.getText().toString().trim().length() > 0 &&
                        startDate.getText().toString().trim().length() > 0 &&
                        destination.getText().toString().trim().length() > 0) {
                    Log.d(TAG,"Trip Name given: "+tripNameInput.getText().toString());
                    Log.d(TAG,"Start Date given: "+startDate.getText().toString());
                    Log.d(TAG,"Destination given: "+destination.getText().toString());
                    Log.d(TAG,"Is sharable: "+toggle.isChecked());
                    // create a new trip and save in fireStore
                    // need to user view model methods for this
                    try {
                        // fetch topic from user

                        // create a trip
                        // add this trip id to the users collection

                        List<String> userEmails = invitedUsers.stream().map(UserBio::getEmail).collect(Collectors.toList());
                        userEmails.add(0, userId);
                        Log.d(TAG, "User ids: "+userEmails);
                        tripCreationViewModel.createNewTrip(tripNameInput.getText().toString(),
                                startDate.getText().toString(),userEmails,destination.getText().toString(),toggle.isChecked());

                        for(UserBio user: invitedUsers){
                            if(user.getFcmToken() != null){
                                tripCreationViewModel.sendNotification(HelperClass.getCurrentUser().getDisplayName(), user.getFcmToken(), tripNameInput.getText().toString());
                            }
                        }

                        tripCreationViewModel.getCreatedTripLiveData().observe(TripCreationActivity.this,trip -> {
                            Log.d(TAG,"Created Trip Id: "+trip.getTripId());
                            tripCreationViewModel.createNewItinerary(trip.getTripId());
                            tripCreationViewModel.getCreatedItineraryLiveData().observe(TripCreationActivity.this,itinerary -> {
                                goToItineraryViewActivity(trip.getTripId(),trip.getStartDate(),
                                        trip.getTripName(),itinerary.getId(),trip.isCanShare());
                            });

                        });

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
                else {
                    if (tripNameInput.getText().toString().trim().length() == 0) {
                        Snackbar.make(v, "Please Enter valid Trip name"
                                ,Snackbar.LENGTH_SHORT).show();
                    }
                    else if (startDate.getText().toString().trim().length() == 0) {
                        Snackbar.make(v, "Please Enter valid Start Date"
                                ,Snackbar.LENGTH_SHORT).show();
                    }
                    else if (destination.getText().toString().trim().length() == 0) {
                        Snackbar.make(v, "Please Enter valid Destination"
                                ,Snackbar.LENGTH_SHORT).show();
                    }

                }
            }
        });

        findViewById(R.id.saveEditTrip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                tripId = intent.getStringExtra(Constants.TRIP_ID);

                if (tripNameInput.getText().toString().trim().length() > 0 &&
                        startDate.getText().toString().trim().length() > 0 &&
                        destination.getText().toString().trim().length() > 0) {
                    Log.d(TAG,"Trip Name given: "+tripNameInput.getText().toString());
                    Log.d(TAG,"Start Date given: "+startDate.getText().toString());
                    Log.d(TAG,"Destination given: "+destination.getText().toString());
                    Log.d(TAG,"Is sharable: "+toggle.isChecked());

                    tripCreationViewModel.getTrip(tripId);

                    tripCreationViewModel.getTripLiveData().observe(TripCreationActivity.this,trip -> {
                        Log.d(TAG,"Queried Trip Id: "+trip.getTripId());
                        SimpleDateFormat formatter = new SimpleDateFormat("EE MMM dd yyyy",
                                Locale.ENGLISH);
                        Date startDateObj = null;
                        try {
                            startDateObj = formatter.parse(startDate.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Date endDateObj = startDateObj;
                        trip.setTripName(tripNameInput.getText().toString());
                        trip.setStartDate(startDateObj);
                        trip.setEndDate(endDateObj);
                        trip.setDestination(destination.getText().toString());
                        trip.setCanShare(toggle.isChecked());
                        // setting trip data.
                        tripCreationViewModel.updateTrip(trip);

                        finish();

                    });

                }
                else {
                    if (tripNameInput.getText().toString().trim().length() == 0) {
                        Snackbar.make(view, "Please Enter valid Trip name"
                                ,Snackbar.LENGTH_SHORT).show();
                    }
                    else if (startDate.getText().toString().trim().length() == 0) {
                        Snackbar.make(view, "Please Enter valid Start Date"
                                ,Snackbar.LENGTH_SHORT).show();
                    }
                    else if (destination.getText().toString().trim().length() == 0) {
                        Snackbar.make(view, "Please Enter valid Destination"
                                ,Snackbar.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        TextView userCount = findViewById(R.id.users_count);
        if(invitedUsers != null && invitedUsers.size() != 0){
            userCount.setText(String.format("%d invited", invitedUsers.size()));
            userCount.setVisibility(View.VISIBLE);
        } else {
            userCount.setVisibility(View.INVISIBLE);
        }


        destination = findViewById(R.id.tripDestination);

    }

    private void goToItineraryViewActivity(String tripId, Date startDate, String tripName,
                                           String itineraryId, Boolean canShare) {
        Intent intent = new Intent(TripCreationActivity.this, Tripfinity.class);
        // changed from ItineraryViewActivity to Tripfnity
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        intent.putExtra(Constants.TRIP_ID, tripId);
        intent.putExtra(Constants.TRIP_NAME, tripName);
        intent.putExtra(Constants.TRIP_START_DATE, TripCreationActivity.getDateForDay(startDate.toString()));
        intent.putExtra(Constants.ITINERARY_ID, itineraryId);
        intent.putExtra(Constants.DESTINATION,destination.getText().toString());
        intent.putExtra(Constants.CAN_SHARE,canShare);
        startActivity(intent);
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.d(TAG, "Place entered: " + place.getName());
                destination.setText(place.getName());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.d(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}