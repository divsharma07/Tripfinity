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

import com.app.tripfinity.R;
import com.app.tripfinity.viewmodel.ExpenseViewModel;
import com.app.tripfinity.viewmodel.SplashViewModel;

import java.util.ArrayList;

public class AddExpenseActivity extends AppCompatActivity {

    private ExpenseViewModel expenseViewModel;
    private EditText expenseName;
    private EditText expenseAmount;
    private TextView selectedUsers;
    private Button expenseButton;
    boolean[] userBoolean;
    ArrayList<Integer> langList = new ArrayList<>();
    String[] userList = {"juhisbhagtani@gmail.com", "sharmadivyanshu1996@gmail.com", "abc@gmail.com"};
    ArrayList<String> finalUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);
        initExpenseViewModel();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

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
                Log.d("Expense name ", expenseName.getText().toString());
                Log.d("Expense Amount ", expenseAmount.getText().toString());
                Log.d("expense selected ", finalUsers.toString());
                expenseViewModel.createExpense("abc@gmail.com", expenseName.getText().toString(),
                        expenseAmount.getText().toString(), finalUsers);
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
                            finalUsers.add(userList[i]);
                        } else {
                            finalUsers.remove(new String(userList[i]));
                        }
                    }
                });

                builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (finalUsers.size() > 0) {
                            selectedUsers.setText("Users Selected");
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