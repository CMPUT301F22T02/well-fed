package com.xffffff.wellfed.recipe;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.xffffff.wellfed.common.EditItemAdapter;


public class EditRecipesAdapter extends EditItemAdapter<Recipe> {
	/**
	 * OnBindViewHolder binds the view holder to the data. It sets the text
	 * of the view holder to the name of the recipe.
	 *
	 * @param holder1  The view holder to bind.
	 * @param position The position of the Recipe in the list.
	 */
	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder1,
								 int position) {
//        TODO: this holder type causes strange spaghetti code
		super.onBindViewHolder(holder1, position);
		ItemViewHolder holder = (ItemViewHolder) holder1;
		Recipe recipe = items.get(position);
		holder.getHeadlineTextView().setText(recipe.getTitle());
	}
}
