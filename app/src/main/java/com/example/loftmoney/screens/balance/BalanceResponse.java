package com.example.loftmoney.screens.balance;

import com.google.gson.annotations.SerializedName;

public class BalanceResponse {
    @SerializedName("status")
    String status;
    @SerializedName("total_expenses")
    String expenses;
    @SerializedName("total_income")
    String incomes;
}
