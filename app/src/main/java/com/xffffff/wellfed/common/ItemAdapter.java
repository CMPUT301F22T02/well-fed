package com.xffffff.wellfed.common;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * The class ItemAdapter is used to display items in a RecyclerView
 */
public abstract class ItemAdapter<Item>
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * list of {@link Item}
     */
    protected List<Item> items;

    public ItemAdapter() {
        this.items = new ArrayList<>();
    }

    public List<Item> getItems() {
        return items;
    }

    /**
     * @param items the list of items
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }

    /**
     * @return count of the number of ingredients in our list
     */
    @Override public int getItemCount() {
        return this.items.size();
    }

}
