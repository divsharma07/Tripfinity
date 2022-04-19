package com.app.tripfinity.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.app.tripfinity.R;
import com.app.tripfinity.model.Expense;
import com.app.tripfinity.viewmodel.HistoryExpenseViewModel;
import com.app.tripfinity.viewmodel.MainExpenseViewModel;

import java.util.HashMap;
import java.util.List;

public class HistoryExpenseActivity extends AppCompatActivity {
    private RecyclerView historyRecyclerView;
    private List<Expense> expenseList;
    private HashMap<String, String> userEmailToName = new HashMap<>();
    private HistoryExpenseViewModel historyExpenseViewModel;
    private String tripId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_expense);

        tripId = getIntent().getExtras().getString("tripId");
        userEmailToName = (HashMap<String, String>) getIntent().getSerializableExtra("userEmailToName");
        Log.d("history trip ", tripId);
        Log.d("history map ", userEmailToName.toString());
        initHistoryExpenseViewModel();

        historyExpenseViewModel.getExpensesForTrip(tripId).observe(HistoryExpenseActivity.this, list -> {
            expenseList = list;



        });

    }

    private void initHistoryExpenseViewModel() {
        historyExpenseViewModel = new ViewModelProvider(this).get(HistoryExpenseViewModel.class);
    }
}