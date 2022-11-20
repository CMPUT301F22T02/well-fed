package com.example.wellfed.recipe;

import com.example.wellfed.common.EditItemAdapter;
import com.example.wellfed.ingredient.Ingredient;


public class RecipeIngredientAdapter extends EditItemAdapter<Ingredient> {
    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Ingredient ingredient = items.get(position);
        holder.getLeadingTextView()
                .setText(ingredient.getAmount() + " " + ingredient.getUnit());
        holder.getHeadlineTextView().setText(ingredient.getDescription());
    }
}
