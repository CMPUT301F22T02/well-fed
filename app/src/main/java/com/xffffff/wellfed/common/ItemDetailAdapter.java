package com.xffffff.wellfed.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xffffff.wellfed.R;
import com.xffffff.wellfed.ingredient.Ingredient;

import java.util.Locale;

public class ItemDetailAdapter extends ItemAdapter {
    @NonNull @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                      int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView =
                layoutInflater.inflate(R.layout.view_holder_item, parent,
                        false);
        return new ItemDetailAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,
                                 int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        Ingredient ingredient = (Ingredient) getItems().get(position);
        holder1.title.setText(
                String.format(Locale.CANADA, "%.2f", ingredient.getAmount()) + " "
                        + ingredient.getUnit());
        holder1.headline.setText(ingredient.getDescription() + " | " + ingredient
                .getCategory());
    }

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
