package com.example.loftmoney.screens.budget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loftmoney.model.Item;
import com.example.loftmoney.R;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> items = new ArrayList<Item>();

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items, Item.ItemType type) {
        ArrayList<Item> filteredItems = new ArrayList();
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getType() == type) {
                filteredItems.add(items.get(i));

            }
        }

        this.items = filteredItems;
        notifyDataSetChanged();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public void clearItems() {
        items.clear();
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView price;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_item_text);
            price = itemView.findViewById(R.id.tv_price);
        }

        public void bind(Item item){
            name.setText(item.getName());
            price.setText(item.getPrice());
            if (item.getType().equals(Item.ItemType.INCOME)) {
                int color = itemView.getResources().getColor(R.color.incomeColor);
                price.setTextColor(color);
            }
        }
    }
}


