package com.xffffff.wellfed.mealplan;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xffffff.wellfed.ingredient.Ingredient;

import java.util.Locale;


public class MealPlanIngredientItemAdapter
        extends MealPlanItemAdapter<Ingredient> {
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1,
                                 int position) {
        super.onBindViewHolder(holder1, position);
        ItemViewHolder holder = (ItemViewHolder) holder1;
        Ingredient ingredient = items.get(position);
        if (ingredient == null) {
            return;
        }
        holder.getPrimaryTextView().setText(ingredient.getDescription());
        String secondaryText =
                String.format(Locale.CANADA, "%.2f", ingredient.getAmount()) + " "
                        + ingredient.getUnit() + " | " + ingredient.getCategory();
        holder.getSecondaryTextView().setText(secondaryText);
    }
}
