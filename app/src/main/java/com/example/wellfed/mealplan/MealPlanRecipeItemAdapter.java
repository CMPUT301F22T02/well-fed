package com.example.wellfed.mealplan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;
import com.example.wellfed.common.EditItemAdapter;
import com.example.wellfed.common.ItemAdapter;
import com.example.wellfed.recipe.Recipe;
import com.squareup.picasso.Picasso;


public class MealPlanRecipeItemAdapter extends MealPlanItemAdapter<Recipe> {
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        super.onBindViewHolder(holder1, position);
        ItemViewHolder holder = (ItemViewHolder) holder1;
        Recipe recipe = items.get(position);
        holder.getPrimaryTextView().setText(recipe.getTitle());
        holder.getSecondaryTextView().setText("All ingredients available");
        Picasso.get()
                .load(recipe.getPhotograph())
                .rotate(90)
                .into(holder.getImageView());
    }
}
