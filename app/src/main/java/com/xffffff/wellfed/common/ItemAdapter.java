package com.xffffff.wellfed.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xffffff.wellfed.R;

import java.util.ArrayList;
import java.util.List;

public abstract class ItemAdapter<Item> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * list of {@link Item}
     */
    protected List<Item> items;

    public List<Item> getItems(){
        return items;
    }

    public ItemAdapter() {
        this.items = new ArrayList<>();
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
    @Override
    public int getItemCount() {
        return this.items.size();
    }

}
