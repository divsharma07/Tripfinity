package com.app.tripfinity.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.app.tripfinity.R;
import com.app.tripfinity.model.Expense;
import com.app.tripfinity.model.User;
import com.app.tripfinity.viewmodel.ExpenseViewModel;
import com.app.tripfinity.viewmodel.MainExpenseViewModel;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpenseActivity extends AppCompatActivity {
    private MainExpenseViewModel mainExpenseViewModel;
    private List<User> userList = new ArrayList<>();
    private List<Expense> expenseList = new ArrayList<>();
    private HashMap<String, Double> userAmountMap = new HashMap<>();
    private HashMap<String, String> userEmailToName = new HashMap<>();
    private double youOwe = 0;
    private double youAreOwed = 0;
    private String tripId = "77nrAgVzOA8xdm2wxPGa";
    private String loggedInUser = "abc@gmail.com";
    private TextView expenseUserName;
    private TextView expenseYouOwe;
    private TextView expenseYouAreOwed;
    private Button expenseHistory;
    private Button expenseAdd;

    @RequiresApi(api = Build.VERSION_CODES.N)
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
            Log.d("user list size in view ", String.valueOf(list.size()));
            userList = list;

            mainExpenseViewModel.getExpensesForTrip(tripId).observe(ExpenseActivity.this, expList -> {
                expenseList = expList;

                for (User user : userList) {
                    userEmailToName.put(user.getEmail(), user.getName());
                }

                // Explore all expenses
                for (Expense expense : expenseList) {

                    double eachSplit = expense.getAmount() / expense.getUserIds().size();
                    List<String> userIds = expense.getUserIds();

                    if (expense.getAddedByUser().equals(loggedInUser)) {
                        youAreOwed += eachSplit * (userIds.size() - 1);

                        for (String userEmail : userIds) {
                            if (!userEmail.equals(loggedInUser)) {
                                userAmountMap.put(userEmail, userAmountMap.getOrDefault(userEmail, 0.0) + eachSplit);
                            }
                        }
                    } else {
                        if (userIds.contains(loggedInUser)) {
                            youOwe += eachSplit;

                            String currentPayer = expense.getAddedByUser();

                            userAmountMap.put(currentPayer, userAmountMap.getOrDefault(currentPayer, 0.0) - eachSplit);
                        }
                    }
                }

                // Processing done, Show all data here now

            });
        });



    }

    private void initMainExpenseViewModel() {
        mainExpenseViewModel = new ViewModelProvider(this).get(MainExpenseViewModel.class);
    }

}