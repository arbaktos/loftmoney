package com.example.loftmoney;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.loftmoney.model.Item;
import com.example.loftmoney.ui.fragments.balance.BalanceFragment;
import com.example.loftmoney.ui.fragments.budget.BudgetFragment;

public class MainPagerAdapter extends FragmentStateAdapter {
    private final String[] types = {"expense", "income"};

    public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        if (position != 2) {
            Bundle args = new Bundle();
            args.putString("type",types[position]);
            final BudgetFragment budgetFragment = new BudgetFragment();
            budgetFragment.setArguments(args);
            return budgetFragment;
        }
        return new BalanceFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
