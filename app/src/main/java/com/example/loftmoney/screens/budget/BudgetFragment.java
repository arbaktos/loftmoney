package com.example.loftmoney.screens.budget;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import io.reactivex.disposables.CompositeDisposable;


public class BudgetFragment extends Fragment {

    private BudgetViewModel budgetViewModel;
    private MoneyApi moneyApi;
    public Item.ItemType type;
    public ItemsAdapter adapter;
//    private final List<Item> items = new ArrayList<Item>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences sharedPreferences;

    public static final int LAUNCH_ADD_ITEM = 1;

    public BudgetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (getArguments() != null) {
            Item.ItemType typeInput = (Item.ItemType) getArguments().getSerializable("type");
            if(typeInput != null) {
                type = typeInput;
            } else {
                type = Item.ItemType.EXPENSE;
            }

        }
        return inflater.inflate(R.layout.fragment_budget, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        moneyApi = ((LoftApp) getActivity().getApplication()).moneyApi;
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.app_name), 0);
        configureViewModel();
        configureRecyclerView(view);
        configureSwipeRefreshLayout();
        budgetViewModel.loadItems(moneyApi, type, sharedPreferences);
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    private void configureViewModel() {
        budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);
        budgetViewModel.itemsLiveList.observe(getViewLifecycleOwner(), items -> {
            adapter.setItems(items, type);
        });
        budgetViewModel.messageString.observe(getViewLifecycleOwner(), this::showToast);
        budgetViewModel.messageInt.observe(getViewLifecycleOwner(), message ->
                showToast(getString(message)));
    }

    private void configureSwipeRefreshLayout() {
        budgetViewModel.toRefresh.observe(getViewLifecycleOwner(), toDo -> {
            if (toDo != null) swipeRefreshLayout.setRefreshing(toDo);
        });
        swipeRefreshLayout = getView().findViewById(R.id.swipe_refresh_layout);
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
                adapter.addItem(inputItem);
                budgetViewModel.addItemToRemote(inputItem, moneyApi);
                adapter.setItems(adapter.getItems(), type);
            }
        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void configureRecyclerView(View view) {
        adapter = new ItemsAdapter();
        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    public void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}