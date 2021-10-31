package com.example.loftmoney.screens.additem;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.loftmoney.model.Item;
import com.example.loftmoney.R;

public class AddItemActivity extends AppCompatActivity implements AddItemOnClickAdapter {
    private TextView etName;
    private TextView etPrice;
    private Button btnAdd;

    private String mName;
    private String mPrice;

    private Item.ItemType type;
    private int typeColor;

    private Boolean isEnabled;

    public void setEnabled(Boolean enabled) {
        if (isEnabled != enabled) setColor(enabled);
        isEnabled = enabled;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        setTypeColor();
        setupEditTexts();
        setupBtnAdd();
    }

    private void setupEditTexts() {
        etName = findViewById(R.id.et_expense_name);
        etName.setTextColor(typeColor);
        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                mName = s.toString();
                checkEditTextHasText();
            }
        });
        etPrice = findViewById(R.id.et_expense);
        etPrice.setTextColor(typeColor);
        etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                mPrice = s.toString();
                checkEditTextHasText();
            }
        });
    }

    private void setTypeColor() {
        type = (Item.ItemType) getIntent().getSerializableExtra("type");
        if (type.equals(Item.ItemType.INCOME)) {
            typeColor = getResources().getColor(R.color.incomeColor);
        } else {
            typeColor = getResources().getColor(R.color.expenseColor);
        }
    }

    private void setupBtnAdd() {
        btnAdd = findViewById(R.id.btn_add);

        btnAdd.setOnClickListener(v -> {
            if (mName.equals("") || mPrice.equals("")) {
                Toast.makeText(getApplicationContext(), getString(R.string.fileds_are_empty), Toast.LENGTH_LONG).show();
            } else {
                Item newItem = new Item(mName, mPrice, type);
                onAddClick(newItem);
            }
        });
    }

    @Override
    public void onAddClick(Item item) {
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent.putExtra("item", item));
        finish();
    }

    public void checkEditTextHasText() {
        setEnabled(!TextUtils.isEmpty(mName) && !TextUtils.isEmpty(mPrice));
        btnAdd.setEnabled(isEnabled);
    }

    private void setColor(Boolean isEnabled) {
        if (isEnabled) {
            btnAdd.setTextColor(typeColor);
            btnAdd.setCompoundDrawableTintList(ColorStateList.valueOf(typeColor));
        } else {
            btnAdd.setTextColor(getColor(R.color.inactive_text));
            btnAdd.setCompoundDrawableTintList(ColorStateList.valueOf(getColor(R.color.inactive_text)));
        }
    }

}