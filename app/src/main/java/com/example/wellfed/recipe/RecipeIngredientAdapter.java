package com.example.wellfed.recipe;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.common.EditItemAdapter;
import com.example.wellfed.ingredient.Ingredient;


public class RecipeIngredientAdapter extends EditItemAdapter<Ingredient> {
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1, int position) {
        super.onBindViewHolder(holder1, position);
        ItemViewHolder holder = (ItemViewHolder) holder1;
        Ingredient ingredient = (Ingredient) items.get(position);
        holder.getLeadingTextView().setText(
                ingredient.getAmount() + " " + ingredient.getUnit());
        holder.getHeadlineTextView().setText(ingredient.getDescription());
    }
}
