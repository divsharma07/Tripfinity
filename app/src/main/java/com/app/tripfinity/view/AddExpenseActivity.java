package com.app.tripfinity.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tripfinity.R;
import com.app.tripfinity.viewmodel.ExpenseViewModel;
import com.app.tripfinity.viewmodel.SplashViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddExpenseActivity extends AppCompatActivity {

    private HashMap<String, String> userEmailToName = new HashMap<>();
    private ExpenseViewModel expenseViewModel;
    private EditText expenseName;
    private EditText expenseAmount;
    private TextView selectedUsers;
    private Button expenseButton;
    boolean[] userBoolean;
    ArrayList<Integer> langList = new ArrayList<>();
    private String tripId;
    private String loggedInUser;
    // All users of a trip except logged in user
    String[] userList;
    String[] emails;
    ArrayList<String> finalUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        initExpenseViewModel();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        userEmailToName = (HashMap<String, String>) getIntent().getSerializableExtra("userEmailToName");
        loggedInUser = getIntent().getStringExtra("loggedInUser");
        tripId = getIntent().getExtras().getString("tripId");

        String[] users = new String[userEmailToName.size() - 1];
        emails = new String[userEmailToName.size() - 1];
        int i = 0;

        for (Map.Entry<String, String> entry : userEmailToName.entrySet()) {
            if (!entry.getKey().equals(loggedInUser)) {
                users[i] = entry.getValue();
                emails[i] = entry.getKey();
                i++;
            }
        }

        userList = users;

        expenseName = (EditText) findViewById(R.id.expenseName);
        expenseAmount = (EditText) findViewById(R.id.expenseAmount);
        selectedUsers = (TextView) findViewById(R.id.availableUsers);
        expenseButton = (Button) findViewById(R.id.addExpense);

        userBoolean = new boolean[userList.length];
        finalUsers = new ArrayList<>();
        setupMultiSelectBox();

        expenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d("Expense name ", expenseName.getText().toString());
//                Log.d("Expense Amount ", expenseAmount.getText().toString());

                if (expenseName.getText().toString().length() == 0) {
                    Snackbar.make(findViewById(R.id.addExpenseActivity), "Please name what did you spend on!", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (expenseAmount.getText().toString().length() == 0) {
                    Snackbar.make(findViewById(R.id.addExpenseActivity), "Expense amount is Empty! Please enter.", Snackbar.LENGTH_LONG).show();
                    return;
                }
                try {
                    double test = Double.parseDouble(expenseAmount.getText().toString());
                } catch (NumberFormatException | NullPointerException e) {
                    Snackbar.make(findViewById(R.id.addExpenseActivity), "Expense amount is Incorrect! Please correct it.", Snackbar.LENGTH_LONG).show();
                    return;
                }

                if (finalUsers.size() == 0) {
                    Snackbar.make(findViewById(R.id.addExpenseActivity), "Please select at least one user to split with!", Snackbar.LENGTH_LONG).show();
                    return;
                }

                // Logged In user involved in transaction by default
                finalUsers.add(loggedInUser);
//                Log.d("expense selected ", finalUsers.toString());

                expenseViewModel.createExpense(loggedInUser, expenseName.getText().toString(),
                        expenseAmount.getText().toString(), finalUsers, tripId);

                Toast.makeText(AddExpenseActivity.this, "Expense Added Successfully!", Toast.LENGTH_LONG).show();
                finish();

            }
        });
    }

    private void initExpenseViewModel() {
        expenseViewModel = new ViewModelProvider(this).get(ExpenseViewModel.class);
    }

    private void setupMultiSelectBox() {
        selectedUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddExpenseActivity.this);
                builder.setTitle("Select Users");

                builder.setCancelable(false);
                builder.setMultiChoiceItems(userList, userBoolean, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if (b) {
                            finalUsers.add(emails[i]);
//                            Log.d("multiple user ", finalUsers.toString());
                        } else {
                            finalUsers.remove(new String(emails[i]));
//                            Log.d("multiple user ", finalUsers.toString());
                        }
                    }
                });

                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (finalUsers.size() > 0) {
                            selectedUsers.setText(finalUsers.size() + " User(s) Selected");
                        } else {
                            selectedUsers.setText("No User is Selected");
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }
}