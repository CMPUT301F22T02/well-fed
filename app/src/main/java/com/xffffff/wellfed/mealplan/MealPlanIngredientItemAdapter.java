package com.xffffff.wellfed.mealplan;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xffffff.wellfed.ingredient.Ingredient;


public class MealPlanIngredientItemAdapter
        extends MealPlanItemAdapter<Ingredient> {
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1,
                                 int position) {
        super.onBindViewHolder(holder1, position);
        ItemViewHolder holder = (ItemViewHolder) holder1;
        Ingredient recipe = items.get(position);
        holder.getPrimaryTextView().setText(recipe.getDescription());
        String secondaryText =
                recipe.getAmount() + " " + recipe.getUnit() + " | " +
                        recipe.getCategory();
        holder.getSecondaryTextView().setText(secondaryText);
    }
}
