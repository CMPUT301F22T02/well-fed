package com.example.wellfed.recipe;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.common.EditItemAdapter;
import com.example.wellfed.ingredient.Ingredient;


public class EditRecipesAdapter extends EditItemAdapter<Recipe> {
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1,
                                 int position) {
//        TODO: this holder type causes strange spaghetti code
        super.onBindViewHolder(holder1, position);
        ItemViewHolder holder = (ItemViewHolder) holder1;
        Recipe recipe = (Recipe) items.get(position);
        holder.getHeadlineTextView().setText(recipe.getTitle());
    }
}
