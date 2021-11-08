package com.example.loftmoney.screens.main.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.loftmoney.model.Item;
import com.example.loftmoney.screens.balance.BalanceFragment;
import com.example.loftmoney.screens.budget.BudgetFragment;

import java.util.List;

public class MainPagerAdapter extends FragmentStateAdapter {
    private final List<FragmentItem> fragments;
    private final int behavior;

    public MainPagerAdapter(List<FragmentItem> fragments, @NonNull FragmentActivity fragmentActivity, int behavior) {
        super(fragmentActivity);
        this.fragments = fragments;
        this.behavior = behavior;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = fragments.get(position).getFragment();
        if (fragments.get(position).getType() != null) {
            Bundle args = new Bundle();
            args.putSerializable("type", fragments.get(position).getType());
            fragment.setArguments(args);
        }
        return fragment;
    }


    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
