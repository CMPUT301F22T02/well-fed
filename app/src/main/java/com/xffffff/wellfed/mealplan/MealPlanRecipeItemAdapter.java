package com.xffffff.wellfed.mealplan;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.xffffff.wellfed.recipe.Recipe;

/**
 * The MealPlanRecipeItemAdapter class binds ArrayList to RecyclerView.
 */
public class MealPlanRecipeItemAdapter extends MealPlanItemAdapter<Recipe> {
    /**
     * onBindViewHolder method binds a Recipe object and a ItemViewHolder
     * object.
     *
     * @param holder1  the view holder
     * @param position the position of the item in the ArrayList
     */
    @Override public void onBindViewHolder(
            @NonNull RecyclerView.ViewHolder holder1, int position) {
        super.onBindViewHolder(holder1, position);
        ItemViewHolder holder = (ItemViewHolder) holder1;
        Recipe recipe = items.get(position);
        holder.getPrimaryTextView().setText(recipe.getTitle());
        String text = "All ingredients available";
        holder.getSecondaryTextView().setText(text);
        Picasso.get().load(recipe.getPhotograph()).rotate(90)
                .into(holder.getImageView());
    }
}
