package com.example.loftmoney.screens.budget.recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loftmoney.model.Item;
import com.example.loftmoney.R;
import com.example.loftmoney.screens.budget.BudgetAdapterClick;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>{

    private List<Item> items = new ArrayList<Item>();
    public BudgetAdapterClick budgetAdapterClick;
    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void updateItem(Item item) {
        int itemPosition = items.indexOf(item);
        items.set(itemPosition, item);
        notifyItemChanged(itemPosition);
    }



    public void setBudgetAdapterClick(BudgetAdapterClick budgetAdapterClick) {
        this.budgetAdapterClick = budgetAdapterClick;
    }

    public void setItems(List<Item> items, Item.ItemType type) {
        ArrayList<Item> filteredItems = new ArrayList();
        for (Item item: items) {
            if (item.getType() == type) {
                filteredItems.add(item);
            }
        }
        this.items = filteredItems;
        notifyDataSetChanged();
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
        return new ItemViewHolder(view, budgetAdapterClick);
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

        private final TextView name;
        private final TextView price;
        private final BudgetAdapterClick budgetAdapterClick;

        public ItemViewHolder(@NonNull View itemView, BudgetAdapterClick budgetAdapterClick) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_item_text);
            price = itemView.findViewById(R.id.tv_price);
            this.budgetAdapterClick = budgetAdapterClick;
        }

        public void bind(Item item){
            name.setText(item.getName());
            price.setText(item.getPrice());
            if (item.getType().equals(Item.ItemType.INCOME)) {
                int color = itemView.getResources().getColor(R.color.incomeColor);
                price.setTextColor(color);
            }
            if (item.getSelected() != null) {
                itemView.setBackgroundColor(itemView.getResources().getColor(
                        item.getSelected() ? R.color.bgColorEditMode : R.color.notSelected));
            }

            itemView.setOnLongClickListener(v -> {
                if (budgetAdapterClick != null) {
                    budgetAdapterClick.onLongClick(item);
                }
                return true;
            });

        }

    }
}


