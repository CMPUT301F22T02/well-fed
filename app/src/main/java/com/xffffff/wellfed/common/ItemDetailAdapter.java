package com.xffffff.wellfed.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xffffff.wellfed.R;
import com.xffffff.wellfed.ingredient.Ingredient;

import java.util.Locale;

/**
 * ItemDetailAdapter is the adapter for the item detail recycler view
 */
public class ItemDetailAdapter extends ItemAdapter {
    /**
     * onCreatedViewHolder inflates the view
     * @param parent activity that handles the ingredients
     * @param viewType view type
     * @return the view holder
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                      int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView =
            layoutInflater.inflate(R.layout.view_holder_item, parent,
                false);
        return new ItemDetailAdapter.ViewHolder(itemView);
    }

    /**
     * onBindViewHolder binds the view to the data
     * @param holder the view holder
     * @param position the position of the item
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,
                                 int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        Ingredient ingredient = (Ingredient) getItems().get(position);
        holder1.headline.setText(
            String.format(Locale.CANADA, "%.2f", ingredient.getAmount()) + " "
                + ingredient.getUnit());
        holder1.title.setText(ingredient.getDescription() + " | " + ingredient
            .getCategory());
    }

    /**
     * ViewHolder is the view holder for the item detail recycler view
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView headline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTextView);
            headline = itemView.findViewById(R.id.headlineTextView);
        }
    }
}
