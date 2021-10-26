package com.example.loftmoney.ui.fragments.budget;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.loftmoney.LoftApp;
import com.example.loftmoney.model.Item;
import com.example.loftmoney.R;
import com.example.loftmoney.remote.ItemRemote;
import com.example.loftmoney.ui.additem.AddItemActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class BudgetFragment extends Fragment implements BudgetClickAdapter{
    Item.ItemType type;
    public ItemsAdapter adapter = new ItemsAdapter();
    public static final int LAUNCH_ADD_ITEM = 1;
    List<Item> items = new ArrayList<Item>();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public BudgetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == LAUNCH_ADD_ITEM  && resultCode  == -1) {
                assert data != null;
                Item inputItem = data.getParcelableExtra("item");
                items.add(inputItem);
                adapter.setItems(items, type);
            }
        } catch (Exception ex) {
            Toast.makeText(getContext(), ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }
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
        setupFab(view);
        configureRecyclerView(view);
        getItems();
    }

    private void setupFab(View view) {
        FloatingActionButton addFab = view.findViewById(R.id.budget_fab);
        addFab.setOnClickListener(v -> onBackgroundClick());
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


    @NonNull
    private void getItems() {
        Disposable disposable = ((LoftApp) getActivity().getApplication()).moneyApi.getMoneyItems(type.name().toLowerCase(Locale.ROOT))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(itemResponse -> {
                    if (itemResponse.getStatus().equals("success")) {
                        for (ItemRemote itemRemote: itemResponse.getItemsList()) {
                            items.add(Item.getInstance(itemRemote));
                        }
                        adapter.setItems(items, type);
                    } else {
                        Toast.makeText(getContext(), getString(R.string.connection_lost), Toast.LENGTH_LONG).show();
                    }
                }, throwable -> {
                    Toast.makeText(getContext(),throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                });
        compositeDisposable.add(disposable);
//
//        items.add(new Item("mode", "800", Item.ItemType.EXPENSE));
//        items.add(new Item("done", "3", Item.ItemType.INCOME));
//        items.add(new Item("post", "60", Item.ItemType.EXPENSE));
//        adapter.setItems(items, type);
    }

    @Override
    public void onBackgroundClick() {

        Intent intent = new Intent(getActivity(), AddItemActivity.class);
        //startActivity(intent);
        startActivityForResult(intent, LAUNCH_ADD_ITEM);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }
}