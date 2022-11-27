package com.xffffff.wellfed.mealplan;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xffffff.wellfed.ingredient.Ingredient;

import java.util.Locale;

/**
 * MealPlanIngredientItemAdapter is the adapter for the RecyclerView in the
 * MealPlanIngredientItemActivity. It holds a list of ingredients and displays
 * them in a list.
 */
public class MealPlanIngredientItemAdapter
        extends MealPlanItemAdapter<Ingredient> {
    /**
     * onBindViewHolders binds the data to the view holder.
     * @param holder1 the view holder
     * @param position the position of the item in the ArrayList
     */
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
