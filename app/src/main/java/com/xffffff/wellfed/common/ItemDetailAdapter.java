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
                ingredient.getAmount() + " " + ingredient.getUnit());
        holder1.headline.setText(ingredient.getDescription());
        holder1.moreInfo.setOnClickListener(view -> {
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public TextView headline;
        public ImageView moreInfo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTextView);
            headline = itemView.findViewById(R.id.headlineTextView);
            moreInfo = itemView.findViewById(R.id.more_info_ImageView);

        }
    }


}
