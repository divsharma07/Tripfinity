package com.app.tripfinity.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.tripfinity.adapter.ExpenseAdapter;
import com.app.tripfinity.R;
import com.app.tripfinity.model.Expense;
import com.app.tripfinity.model.User;
import com.app.tripfinity.viewmodel.MainExpenseViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpenseActivity extends Fragment {
    private MainExpenseViewModel mainExpenseViewModel;
    private List<User> userList = new ArrayList<>();
    private List<Expense> expenseList = new ArrayList<>();
    private HashMap<String, Double> userAmountMap = new HashMap<>();
    private HashMap<String, String> userEmailToName = new HashMap<>();
    private ArrayList<String> dataToPopulate = new ArrayList<>();
    private double youOwe = 0;
    private double youAreOwed = 0;
    private String tripId = "77nrAgVzOA8xdm2wxPGa";
    private String loggedInUser;
    private String loggedInName;
    private TextView expenseUserName;
    private TextView expenseYouOwe;
    private TextView expenseYouAreOwed;
    private Button expenseHistory;
    private Button expenseAdd;
    private ExpenseAdapter expenseAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_expense, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onStart() {
        super.onStart();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        loggedInUser = firebaseUser.getEmail();
        loggedInName = firebaseUser.getDisplayName();

        expenseUserName = (TextView) getView().findViewById(R.id.expenseUserName);
        expenseYouOwe = (TextView) getView().findViewById(R.id.expenseYouOwe);
        expenseYouAreOwed = (TextView) getView().findViewById(R.id.expenseYouAreOwed);
        expenseHistory = (Button) getView().findViewById(R.id.expenseHistory);
        expenseAdd = (Button) getView().findViewById(R.id.expenseAdd);

        initMainExpenseViewModel();
    }

    @Override
    public void onResume() {
        super.onResume();



        expenseUserName.setText("Hello " + loggedInName + ",");

        mainExpenseViewModel.getUserDataForTrip(tripId).observe(getActivity(), list -> {
            Log.d("user list size in view ", String.valueOf(list.size()));
            userList = new ArrayList<>();
            userList = list;

            mainExpenseViewModel.getExpensesForTrip(tripId).observe(getActivity(), expList -> {
                Log.d("user expense call ", String.valueOf(expList.size()));
                expenseList = new ArrayList<>();
                expenseList = expList;

                userAmountMap = new HashMap<>();
                userEmailToName = new HashMap<>();
                dataToPopulate = new ArrayList<>();
                youOwe = 0;
                youAreOwed = 0;

                for (User user : userList) {
                    userEmailToName.put(user.getEmail(), user.getName());
                }

                // Explore all expenses
                for (Expense expense : expenseList) {
                    Log.d("each expense ", expense.getTimestamp().toString());

                    double eachSplit = expense.getAmount() / expense.getUserIds().size();
                    List<String> userIds = expense.getUserIds();

                    if (expense.getAddedByUser().equals(loggedInUser)) {
                        youAreOwed += eachSplit * (userIds.size() - 1);

                        for (String userEmail : userIds) {
                            if (!userEmail.equals(loggedInUser)) {
//                                userAmountMap.put(userEmail, userAmountMap.getOrDefault(userEmail, 0.0) + eachSplit);
                                if (userAmountMap.containsKey(userEmail)) {
                                    userAmountMap.put(userEmail, (double)userAmountMap.get(userEmail) + eachSplit);
                                } else {
                                    userAmountMap.put(userEmail, eachSplit);
                                }
                            }
                        }
                    } else {
                        if (userIds.contains(loggedInUser)) {
                            youOwe += eachSplit;

                            String currentPayer = expense.getAddedByUser();

//                            userAmountMap.put(currentPayer, userAmountMap.getOrDefault(currentPayer, 0.0) - eachSplit);
                            if (userAmountMap.containsKey(currentPayer)) {
                                userAmountMap.put(currentPayer, (double)userAmountMap.get(currentPayer) - eachSplit);
                            } else {
                                userAmountMap.put(currentPayer, (-1)*eachSplit);
                            }
                        }
                    }
                }

                // Processing done, Show all data here now
                expenseYouOwe.setText("You Owe $" + Math.round(youOwe * 100.0) / 100.0);
                expenseYouAreOwed.setText("You are Owed $" + Math.round(youAreOwed * 100.0) / 100.0);

                for (String email : userAmountMap.keySet()) {
                    if (userAmountMap.get(email) > 0) {
                        dataToPopulate.add(userEmailToName.get(email) + " owes you $" + Math.round(userAmountMap.get(email) * 100.0) / 100.0);
                    } else {
                        dataToPopulate.add("You owe " + userEmailToName.get(email) + "$" + Math.round(Math.abs(userAmountMap.get(email)) * 100.0) / 100.0);
                    }
                }

                Log.d("user ", dataToPopulate.toString());
                createExpenseRecyclerView(dataToPopulate);

                expenseAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent myIntent = new Intent(getActivity(), AddExpenseActivity.class);
                        myIntent.putExtra("tripId", tripId);
                        myIntent.putExtra("userEmailToName", userEmailToName);
                        myIntent.putExtra("loggedInUser", loggedInUser);
                        startActivity(myIntent);
                    }
                });

                expenseHistory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent myIntent = new Intent(getActivity(), HistoryExpenseActivity.class);
                        myIntent.putExtra("tripId", tripId);
                        myIntent.putExtra("userEmailToName", userEmailToName);
                        startActivity(myIntent);
                    }
                });

            });
        });

    }

    private void createExpenseRecyclerView(ArrayList<String> dataToPopulate) {
        layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView expenseRecyclerView = getView().findViewById(R.id.expenseRecyclerView);
        expenseRecyclerView.setHasFixedSize(true);

        expenseAdapter = new ExpenseAdapter(dataToPopulate);
        expenseRecyclerView.setAdapter(expenseAdapter);
        expenseRecyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onStop() {
        super.onStop();
        userList = new ArrayList<>();
        expenseList = new ArrayList<>();
        userAmountMap = new HashMap<>();
        userEmailToName = new HashMap<>();
        dataToPopulate = new ArrayList<>();
        youOwe = 0;
        youAreOwed = 0;
    }

    private void initMainExpenseViewModel() {
        mainExpenseViewModel = new ViewModelProvider(getActivity()).get(MainExpenseViewModel.class);
    }

}