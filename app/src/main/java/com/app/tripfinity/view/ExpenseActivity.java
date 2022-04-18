package com.app.tripfinity.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.app.tripfinity.R;
import com.app.tripfinity.model.User;
import com.app.tripfinity.viewmodel.ExpenseViewModel;
import com.app.tripfinity.viewmodel.MainExpenseViewModel;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

public class ExpenseActivity extends AppCompatActivity {
    private MainExpenseViewModel mainExpenseViewModel;
    private List<User> userList = new ArrayList<>();
    private List<DocumentReference> userRefList = new ArrayList<>();
    private String tripId = "77nrAgVzOA8xdm2wxPGa";
    private String loggedInUser = "abc@gmail.com";
    private TextView expenseUserName;
    private TextView expenseYouOwe;
    private TextView expenseYouAreOwed;
    private Button expenseHistory;
    private Button expenseAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        expenseUserName = (TextView) findViewById(R.id.expenseUserName);
        expenseYouOwe = (TextView) findViewById(R.id.expenseYouOwe);
        expenseYouAreOwed = (TextView) findViewById(R.id.expenseYouAreOwed);
        expenseHistory = (Button) findViewById(R.id.expenseHistory);
        expenseAdd = (Button) findViewById(R.id.expenseAdd);

        expenseUserName.setText("Hello " + "abc,");

        initMainExpenseViewModel();

        mainExpenseViewModel.getUserDataForTrip(tripId).observe(ExpenseActivity.this, list -> {
            userRefList = list;
            Log.d("user doc list size in view ", String.valueOf(list.size()));
            for (DocumentReference userRef : list) {
                mainExpenseViewModel.getUserObjects(userRef).observe(ExpenseActivity.this, user -> {
                    Log.d("user object in view", user.toString());
                    userList.add(user);
                });
            }
            Log.d("user actual list in view", String.valueOf(userList.size()));
        });


    }

    private void initMainExpenseViewModel() {
        mainExpenseViewModel = new ViewModelProvider(this).get(MainExpenseViewModel.class);
    }

}