package com.example.loftmoney.screens.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.viewpager2.widget.ViewPager2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.loftmoney.R;
import com.example.loftmoney.model.Item;
import com.example.loftmoney.screens.additem.AddItemActivity;
import com.example.loftmoney.screens.balance.BalanceFragment;
import com.example.loftmoney.screens.budget.BudgetAdapterClick;
import com.example.loftmoney.screens.budget.BudgetFragment;
import com.example.loftmoney.screens.main.adapter.FragmentItem;
import com.example.loftmoney.screens.main.adapter.MainPagerAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MainClickAdapter, EditModeListener {

    private ViewPager2 viewPager;
    private Toolbar toolbar;
    private TabLayout tabs;
    private Fragment activeFragment;
    private ImageView backIcon;
    private ImageView bucketIcon;
    private TextView toolbarTextView;
    private FloatingActionButton addFab;
    private List<FragmentItem> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupFab();
        setupTabs();
        setupToolbar();
    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        backIcon = findViewById(R.id.toolbarBackButton);
        bucketIcon = findViewById(R.id.toolbarBucket);
        toolbarTextView = findViewById(R.id.toolbarText);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackClick();
            }
        });
        bucketIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(getString(R.string.deletion))
                        .setMessage(getString(R.string.message_deletion))
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onDeleteItems();
                            }
                        })
                        .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });
    }

    private void setupTabs() {
        fragments = setupFragments();
        int balanceFragmentPosition = 2;
        MainPagerAdapter adapter = new MainPagerAdapter(fragments, this, 0);//behavior?

        tabs = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabs, viewPager,
                (tab, position) -> tab.setText(
                        fragments.get(position).getTitle()
                )
        ).attach();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (position == balanceFragmentPosition) {
                    addFab.hide();
                } else {
                    addFab.show();
                }
                onClearEdit();
            }
        });
    }


    private Fragment getActiveFragment() {
        final int activeFragmentIndex = viewPager.getCurrentItem();
        activeFragment = getSupportFragmentManager().getFragments().get(activeFragmentIndex);
        return activeFragment;
    }

    private void setupFab() {
        addFab = findViewById(R.id.add_fab);
        addFab.setOnClickListener(v -> onFabClick());
    }

    @Override
    public void onFabClick() {
        Intent intent;
        activeFragment = getActiveFragment();
        if (activeFragment instanceof BudgetFragment) {
            BudgetFragment budgetFragment = (BudgetFragment) activeFragment;
            intent = new Intent(MainActivity.this, AddItemActivity.class).putExtra("type", budgetFragment.type);
        } else {
            intent = new Intent(MainActivity.this, AddItemActivity.class);
        }
        activeFragment.startActivityForResult(intent, BudgetFragment.LAUNCH_ADD_ITEM);
    }

    @Override
    public void onEditModeChangeListener(boolean status) {
        toolbar.setBackgroundColor(getApplicationContext().getColor(
                status ? R.color.editModeColor : R.color.colorPrimary));
        tabs.setBackgroundColor(getApplicationContext().getColor(status ? R.color.editModeColor : R.color.colorPrimary));
        backIcon.setVisibility(status ? View.VISIBLE : View.INVISIBLE);
        bucketIcon.setVisibility(status ? View.VISIBLE : View.INVISIBLE);
        addFab.setVisibility(status ? View.INVISIBLE : View.VISIBLE);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(
                status? getResources().getColor(R.color.statusBarEditModeColor)
                        : getResources().getColor(R.color.colorPrimary));

    }

    @Override
    public void onCounterChanged(Integer counter) {
        if (counter > 0) {
            toolbarTextView.setText(getString(R.string.chosenItems) + counter);
        } else {
            toolbarTextView.setText(getString(R.string.activity_main_toolbar_title));
        }
    }

    @Override
    public void onBackClick() {
        onClearEdit();
    }

    private void onClearEdit() {
        activeFragment = getActiveFragment();
        if (activeFragment instanceof BudgetFragment) {
            BudgetFragment budgetFragment = (BudgetFragment) activeFragment;
            budgetFragment.onClearSelect();
        } else {
            for (FragmentItem fragmentItem : fragments) {
                if (fragmentItem.getFragment() instanceof BudgetFragment) {
                    BudgetFragment budgetFragment = ((BudgetFragment) fragmentItem.getFragment());
                    budgetFragment.onClearSelect();
                }
            }
        }
    }

    public void onDeleteItems() {
        activeFragment = getActiveFragment();
        BudgetFragment budgetFragment = (BudgetFragment) activeFragment;
        budgetFragment.onItemsDelete();
    }

    private List<FragmentItem> setupFragments() {
        List<FragmentItem> fragments = new ArrayList();
        fragments.add(new FragmentItem(new BudgetFragment(), getString(R.string.expenses), Item.ItemType.EXPENSE));
        fragments.add(new FragmentItem(new BudgetFragment(), getString(R.string.incomes), Item.ItemType.INCOME));
        fragments.add(new FragmentItem(new BalanceFragment(), getString(R.string.balance)));
        return fragments;
    }
}