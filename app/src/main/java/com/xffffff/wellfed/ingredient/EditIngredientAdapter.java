package com.xffffff.wellfed.ingredient;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xffffff.wellfed.common.EditItemAdapter;

import java.util.Locale;

/**
 * Adapter for a recipe ingredient, in the recyclerview for recipe ingredients
 */
public class EditIngredientAdapter extends EditItemAdapter<Ingredient> {

    /**
     * Binds a viewholder to an ingredient
     *
     * @param holder1  the viewholder
     * @param position the position of the item in the viewholder
     */
    @Override public void onBindViewHolder(
            @NonNull RecyclerView.ViewHolder holder1, int position) {
        super.onBindViewHolder(holder1, position);
        ItemViewHolder holder = (ItemViewHolder) holder1;
        Ingredient ingredient = items.get(position);
        String leadingText =
                String.format(Locale.CANADA, "%.2f", ingredient.getAmount()) + " " + ingredient.getUnit();
        holder.getLeadingTextView().setText(leadingText);
        holder.getHeadlineTextView().setText(ingredient.getDescription());
    }
}
