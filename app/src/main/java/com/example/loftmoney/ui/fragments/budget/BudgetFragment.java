package com.example.loftmoney.ui.fragments.budget;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.loftmoney.model.Item;
import com.example.loftmoney.R;
import com.example.loftmoney.ui.additem.AddItemActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BudgetFragment extends Fragment implements BudgetClickAdapter{
    Item.ExpenseType type;
    public ItemsAdapter adapter = new ItemsAdapter();
    public static final int LAUNCH_ADD_ITEM = 1;
    List<Item> items = new ArrayList<Item>();

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
            } else {
                generateItems();
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
            String typeStr = getArguments().getString("type");
            if(typeStr != null) {
                type = Item.ExpenseType.valueOf(typeStr.toUpperCase(Locale.ROOT));
            } else {
                type = Item.ExpenseType.EXPENSE;
            }

        }
        return inflater.inflate(R.layout.fragment_budget, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler);
        FloatingActionButton addFab = view.findViewById(R.id.budget_fab);

        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackgroundClick();
            }
        });
        generateItems();

    }

    @NonNull
    private void generateItems() {

        items.add(new Item("mode", "800", Item.ExpenseType.EXPENSE));
        items.add(new Item("done", "3", Item.ExpenseType.INCOME));
        items.add(new Item("post", "60", Item.ExpenseType.EXPENSE));
        adapter.setItems(items, type);
    }

    @Override
    public void onBackgroundClick() {

        Intent intent = new Intent(getActivity(), AddItemActivity.class);
        //startActivity(intent);
        startActivityForResult(intent, LAUNCH_ADD_ITEM);
    }
}