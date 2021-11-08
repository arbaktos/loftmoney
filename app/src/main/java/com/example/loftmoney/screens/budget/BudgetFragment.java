package com.example.loftmoney.screens.budget;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.loftmoney.LoftApp;
import com.example.loftmoney.model.Item;
import com.example.loftmoney.R;
import com.example.loftmoney.remote.MoneyApi;
import com.example.loftmoney.screens.budget.recyclerview.ItemsAdapter;
import com.example.loftmoney.screens.main.EditModeListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;


public class BudgetFragment extends Fragment implements ItemChoiceListener {

    public Item.ItemType type;
    private BudgetViewModel budgetViewModel;
    private MoneyApi moneyApi;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPreferences sharedPreferences;
    private final ItemsAdapter adapter = new ItemsAdapter();

    public static final int LAUNCH_ADD_ITEM = 1;

    public BudgetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter.setBudgetAdapterClick(new BudgetAdapterClick() {
            @Override
            public void onLongClick(Item item) {
                if (!budgetViewModel.isEditMode.getValue()) {
                    budgetViewModel.isEditMode.postValue(true);
                }
                item.setSelected(!item.getSelected());
                adapter.updateItem(item);
                budgetViewModel.countSelected();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_budget, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        moneyApi = ((LoftApp) getActivity().getApplication()).moneyApi;
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.app_name), 0);
        configureArguments();
        configureViewModel();
        configureRecyclerView(view);
        configureSwipeRefreshLayout(view);
    }

    private void configureArguments() {
        if (getArguments() != null) {
            Item.ItemType typeInput = (Item.ItemType) getArguments().getSerializable("type");
            if(typeInput != null) {
                type = typeInput;
            } else {
                type = Item.ItemType.EXPENSE;
            }
        }
    }

    private void configureViewModel() {
        budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);
        budgetViewModel.loadItems(moneyApi, type, sharedPreferences);
        budgetViewModel.itemsLiveList.observe(getViewLifecycleOwner(), items -> {
            adapter.setItems(items, type);
        });
        budgetViewModel.messageString.observe(getViewLifecycleOwner(), this::showToast);
        budgetViewModel.messageInt.observe(getViewLifecycleOwner(), message ->
                showToast(getString(message)));
        budgetViewModel.isEditMode.observe(getViewLifecycleOwner(), isEditMode -> {
            Activity activity = getActivity();
            if (activity instanceof EditModeListener) {
                ((EditModeListener) activity).onEditModeChangeListener(isEditMode);
            }
        });
        budgetViewModel.selectedItemCounter.observe(getViewLifecycleOwner(), numSelected -> {
            Activity activity = getActivity();
            if (activity instanceof EditModeListener) {
                ((EditModeListener) activity).onCounterChanged(numSelected);
            }
        });
    }

    private void configureSwipeRefreshLayout(View view) {
        budgetViewModel.toRefresh.observe(getViewLifecycleOwner(), toDo -> {
            if (toDo != null) swipeRefreshLayout.setRefreshing(toDo);
        });
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            budgetViewModel.loadItems(moneyApi, type, sharedPreferences);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == LAUNCH_ADD_ITEM  && resultCode  == -1) {
                assert data != null;
                Item inputItem = data.getParcelableExtra("item");
                adapter.addItem(inputItem);// delete when api arrives
                budgetViewModel.addItemToRemote(inputItem, moneyApi);
            }
        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void configureRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onClearSelect() {
        budgetViewModel.isEditMode.postValue(false);
        budgetViewModel.selectedItemCounter.postValue(0);
        for (Item item: budgetViewModel.itemsLiveList.getValue()) {
            if (item.getSelected()) {
                item.setSelected(false);
                adapter.updateItem(item);
            }
        }
    }
    public void deleteSelectedItems() {
        List<Item> items = budgetViewModel.itemsLiveList.getValue();
        List<Item> itemToDelete = new ArrayList<>();
        for (Item item: items) {
            if (item.getSelected()) {
                itemToDelete.add(item);
            }
        }
        items.removeAll(itemToDelete);
        adapter.setItems(items, type);
    }

    @Override
    public void onItemsDelete() {
        deleteSelectedItems();
        budgetViewModel.isEditMode.postValue(false);
        budgetViewModel.selectedItemCounter.postValue(0);
    }

    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


}