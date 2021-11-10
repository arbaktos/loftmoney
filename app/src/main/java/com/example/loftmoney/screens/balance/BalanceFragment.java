package com.example.loftmoney.screens.balance;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loftmoney.R;
import com.example.loftmoney.model.Item;
import com.example.loftmoney.screens.budget.BudgetViewModel;

import java.util.List;

public class BalanceFragment extends Fragment {
    private int balance;
    private float expenses;
    private float incomes;
    private BalanceView balanceView;
    private TextView balanceTextView;
    private TextView expensesTextView;
    private TextView incomesTextView;
    private BudgetViewModel budgetViewModel;

    public BalanceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_balance, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureViewModel();
        setupViews(view);
    }

    private void setupViews(View view) {
        balanceView = view.findViewById(R.id.balanceView);
        balanceTextView = view.findViewById(R.id.balance_amount);
        expensesTextView = view.findViewById(R.id.expenses_amount);
        incomesTextView = view.findViewById(R.id.incomes_amount);
    }

    private void configureViewModel() {
        budgetViewModel = new ViewModelProvider(requireActivity()).get(BudgetViewModel.class);
        budgetViewModel.itemsLiveList.observe(getViewLifecycleOwner(), items -> {

            if (items.size() > 0) {
                countDataForFragment(items);
                updateViews();
            }

        });
//        BalanceViewModel balanceViewModel = new ViewModelProvider(this).get(BalanceViewModel.class);
//        balanceViewModel.getCurrentBalance(moneyApi);
//        balanceViewModel.balance.observe(getViewLifecycleOwner(), s -> {
//            balanceTextView.setText(s);
//            balance = Integer.parseInt(s);
//        });
//        balanceViewModel.incomes.observe(getViewLifecycleOwner(), s -> {
//            incomesTextView.setText(s);
//            incomes = Float.parseFloat(s);
//            balanceView.update(expenses, incomes);
//        });
//        balanceViewModel.expenses.observe(getViewLifecycleOwner(), s -> {
//            expensesTextView.setText(s);
//            expenses = Float.parseFloat(s);
//            balanceView.update(expenses, incomes);
//        });
//        balanceViewModel.stringMessage.observe(getViewLifecycleOwner(), this::showToast);

    }

    private void updateViews() {
        expensesTextView.setText(String.valueOf(expenses));
        incomesTextView.setText(String.valueOf(incomes));
        balanceTextView.setText(String.valueOf(balance));
        balanceView.update(expenses, incomes);
    }

    private void countDataForFragment(List<Item> items) {
        int _expenses = 0;
        int _incomes = 0;
        for (Item item: items) {
            if (item.getType() == Item.ItemType.EXPENSE) {
                _expenses += Integer.parseInt(item.getPrice());
            } else {
                _incomes += Integer.parseInt(item.getPrice());
            }
        }
        expenses = _expenses;
        incomes = _incomes;
        balance = _incomes - _expenses;
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}