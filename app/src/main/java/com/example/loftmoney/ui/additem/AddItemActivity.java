package com.example.loftmoney.ui.additem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.loftmoney.MainActivity;
import com.example.loftmoney.model.Item;
import com.example.loftmoney.R;
import com.example.loftmoney.navigation.AppRouter;
import com.example.loftmoney.ui.fragments.budget.BudgetFragment;

import java.util.ArrayList;

public class AddItemActivity extends AppCompatActivity implements AddItemOnClickAdapter, AppRouter {
    private Button btnAdd;
    private TextView etName;
    private TextView etPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        btnAdd = findViewById(R.id.btn_add);
        etName = findViewById(R.id.et_expense_name);
        etPrice = findViewById(R.id.et_expense);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item newItem = new Item(etName.getText().toString(), etPrice.getText().toString(), Item.ExpenseType.EXPENSE);
                    onAddClick(newItem);
            }
        });
    }

    @Override
    public void onAddClick(Item item) {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent.putExtra("item", item));
//        routeTo(new BudgetFragment());
    }

    @Override
    public void routeTo(Fragment fragment) {
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.add_item, fragment)
//                .commit();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}