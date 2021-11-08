package com.example.loftmoney.screens.main.adapter;

import androidx.fragment.app.Fragment;

import com.example.loftmoney.model.Item;

public class FragmentItem {
    private Fragment fragment;
    private String title;
    private Item.ItemType type;

    public Item.ItemType getType() {
        return type;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public String getTitle() {
        return title;
    }

    public FragmentItem(Fragment fragment, String title) {
        this.fragment = fragment;
        this.title = title;
    }
    public FragmentItem(Fragment fragment, String title, Item.ItemType type) {
        this.fragment = fragment;
        this.title = title;
        this.type = type;
    }
}
