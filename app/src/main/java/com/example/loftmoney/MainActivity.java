package com.example.loftmoney;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;

import com.example.loftmoney.ui.additem.AddItemActivity;
import com.example.loftmoney.ui.fragments.budget.BudgetClickAdapter;
import com.example.loftmoney.ui.fragments.budget.BudgetFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity implements BudgetClickAdapter {

    private BudgetFragment activeFragment;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupFab();
        setupTabs();
    }

    private void setupTabs() {
        TabLayout tabs = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager);
        MainPagerAdapter adapter = new MainPagerAdapter(this);
        viewPager.setAdapter(adapter);
        String[] tabTitles = getResources().getStringArray(R.array.main_pager_titles);

        new TabLayoutMediator(tabs, viewPager,
                (tab, position) -> tab.setText(
                        tabTitles[position]
                )
        ).attach();
    }

    private void getActiveFragment() {
        final int activeFragmentIndex = viewPager.getCurrentItem();
        activeFragment = (BudgetFragment) getSupportFragmentManager().getFragments().get(activeFragmentIndex);
    }

    private void setupFab() {
        FloatingActionButton addFab = findViewById(R.id.add_fab);
        addFab.setOnClickListener(v -> onFabClick());
    }

    @Override
    public void onFabClick() {
        getActiveFragment();
        Intent intent = new Intent(MainActivity.this, AddItemActivity.class).putExtra("type", activeFragment.type);
        activeFragment.startActivityForResult(intent, BudgetFragment.LAUNCH_ADD_ITEM);
    }

}