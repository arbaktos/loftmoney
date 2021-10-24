package com.example.loftmoney.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {
    private String name;
    private String price;
    private ExpenseType type;



    public Item(String name, String price, ExpenseType type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }

    protected Item(Parcel in) {
        name = in.readString();
        price = in.readString();
        type = ExpenseType.valueOf(in.readString());
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(name);
        parcel.writeString(price);
        parcel.writeString(this.type.name());
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public ExpenseType getType() {
        return type;
    }

    public void setType(ExpenseType type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public enum ExpenseType {
        EXPENSE, INCOME;
    }
}


