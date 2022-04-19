package com.app.tripfinity.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.tripfinity.R;
import com.app.tripfinity.model.Expense;

import java.util.HashMap;
import java.util.List;

public class HistoryExpenseAdapter extends RecyclerView.Adapter<HistoryExpenseAdapter.HistoryExpenseViewHolder> {
    private List<Expense> expenseList;
    private HashMap<String, String> userEmailToName;

    public HistoryExpenseAdapter(List<Expense> expenseList, HashMap<String, String> map) {
        this.expenseList = expenseList;
        userEmailToName = map;
    }

    @NonNull
    @Override
    public HistoryExpenseAdapter.HistoryExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_expense_card, parent, false);
        return new HistoryExpenseAdapter.HistoryExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryExpenseAdapter.HistoryExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);

        holder.expenseName.setText(expense.getName());
        holder.expenseAmount.setText(String.valueOf(expense.getAmount()));
        holder.addedBy.setText(userEmailToName.get(expense.getAddedByUser()));
        holder.memberCount.setText(String.valueOf(expense.getUserIds().size()));
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    public static class HistoryExpenseViewHolder extends RecyclerView.ViewHolder {

        public TextView expenseName;
        public TextView addedBy;
        public TextView expenseAmount;
        public TextView memberCount;

        public HistoryExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseName = itemView.findViewById(R.id.history_expense_name);
            addedBy = itemView.findViewById(R.id.added_by);
            expenseAmount = itemView.findViewById(R.id.expense_amount);
            memberCount = itemView.findViewById(R.id.expense_member_count);
        }
    }
}
