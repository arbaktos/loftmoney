package com.example.loftmoney.remote;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ItemResponse {
    private String status;
    @SerializedName("data")
    private List<ItemRemote> itemsList;

    public String getStatus() {
        return status;
    }

    public List<ItemRemote> getItemsList() {
        return itemsList;
    }
}
