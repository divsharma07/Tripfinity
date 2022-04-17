package com.app.tripfinity.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.app.tripfinity.model.Expense;
import com.app.tripfinity.repository.ExpenseRepository;

import java.util.ArrayList;

public class ExpenseViewModel extends AndroidViewModel {
    private ExpenseRepository expenseRepository;

    public ExpenseViewModel(@NonNull Application application) {
        super(application);
        expenseRepository = new ExpenseRepository();
    }

    public void createExpense(String addedBy, String name, String amount, ArrayList<String> usersList) {
        Expense e = new Expense(addedBy, Double.parseDouble(amount), name, usersList);
        expenseRepository.addNewExpense(e);
    }
}
