package com.app.tripfinity.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.app.tripfinity.adapters.HistoryExpenseAdapter;
import com.app.tripfinity.R;
import com.app.tripfinity.model.Expense;
import com.app.tripfinity.viewmodel.HistoryExpenseViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.List;

public class HistoryExpenseActivity extends AppCompatActivity {
    private RecyclerView historyRecyclerView;
    private HistoryExpenseAdapter historyExpenseAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Expense> expenseList;
    private HashMap<String, String> userEmailToName = new HashMap<>();
    private HistoryExpenseViewModel historyExpenseViewModel;
    private ProgressBar progressBar;
    private CardView noExpenseHistory;
    private String tripId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_expense);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        progressBar = findViewById(R.id.historyProgressBar);
        noExpenseHistory = findViewById(R.id.noHistoryPresent);
        noExpenseHistory.setVisibility(View.GONE);
        tripId = getIntent().getExtras().getString("tripId");
        userEmailToName = (HashMap<String, String>) getIntent().getSerializableExtra("userEmailToName");
        Log.d("history trip ", tripId);
        Log.d("history map ", userEmailToName.toString());
        initHistoryExpenseViewModel();

        historyExpenseViewModel.getExpensesForTrip(tripId).observe(HistoryExpenseActivity.this, list -> {
            expenseList = list;

            progressBar.setVisibility(View.GONE);

            if (expenseList.size() > 0) {
                createHistoryRecyclerView(expenseList);
            } else {
                noExpenseHistory.setVisibility(View.VISIBLE);
            }

        });

    }

    private void createHistoryRecyclerView(List<Expense> expenseList) {
        layoutManager = new LinearLayoutManager(this);
        historyRecyclerView = findViewById(R.id.history_expense_recycler_view);
        historyRecyclerView.setHasFixedSize(true);

        historyExpenseAdapter = new HistoryExpenseAdapter(expenseList, userEmailToName);
        historyRecyclerView.setAdapter(historyExpenseAdapter);
        historyRecyclerView.setLayoutManager(layoutManager);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder linkHolder, int direction) {
                int position = linkHolder.getLayoutPosition();

                // Call to firestore
                historyExpenseViewModel.removeExpenseFromTrip(expenseList.get(position)).observe(HistoryExpenseActivity.this, value -> {
                    if (value) {
                        Snackbar.make(findViewById(R.id.historyExpenseId), "Expense Deleted Successfully", Snackbar.LENGTH_LONG).show();

                        expenseList.remove(position);
                        historyExpenseAdapter.notifyItemRemoved(position);
                    } else {
                        Snackbar.make(findViewById(R.id.historyExpenseId), "Problem deleting this expense", Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });
        itemTouchHelper.attachToRecyclerView(historyRecyclerView);
    }

    private void initHistoryExpenseViewModel() {
        historyExpenseViewModel = new ViewModelProvider(this).get(HistoryExpenseViewModel.class);
    }
}