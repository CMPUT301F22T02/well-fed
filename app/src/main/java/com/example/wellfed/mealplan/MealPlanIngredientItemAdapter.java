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
import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.recipe.Recipe;
import com.squareup.picasso.Picasso;


public class MealPlanIngredientItemAdapter extends MealPlanItemAdapter<Ingredient> {
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        ItemViewHolder holder = (ItemViewHolder) holder1;
        Ingredient recipe = items.get(position);
        holder.getPrimaryTextView().setText(recipe.getDescription());
        holder.getSecondaryTextView().setText("Ingredient available");
    }
}
