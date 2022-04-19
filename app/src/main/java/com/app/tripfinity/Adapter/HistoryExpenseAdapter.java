package com.app.tripfinity.Adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HistoryExpenseAdapter extends RecyclerView.Adapter<HistoryExpenseAdapter.HistoryExpenseViewHolder> {
    @NonNull
    @Override
    public HistoryExpenseAdapter.HistoryExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryExpenseAdapter.HistoryExpenseViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class HistoryExpenseViewHolder extends RecyclerView.ViewHolder {

        public HistoryExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
