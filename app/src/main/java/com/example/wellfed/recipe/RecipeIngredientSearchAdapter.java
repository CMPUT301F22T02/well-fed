package com.example.wellfed.recipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.R;
import com.example.wellfed.common.DBAdapter;
import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.ingredient.IngredientDB;

/**
 * Adapter that connects the RecyclerView and data for the ingredients
 * in the {@link RecipeIngredientSearch}
 */
public class RecipeIngredientSearchAdapter extends DBAdapter<RecipeIngredientSearchAdapter.ViewHolder> {
	/**
	 * The Firebase Firestore database of ingredients
	 */
	private final IngredientDB db;

	/**
	 * The listener for when an item in the RecyclerView is clicked
	 */
	private OnItemClickListener listener;

	/**
	 * Interface for listeners that listen for clicks on an ingredient
	 */
	public interface OnItemClickListener {
		void onItemClick(Ingredient ingredient);
	}

	/**
	 * Creates a new RecipeIngredientSearchAdapter
	 *
	 * @param db the Ingredient database that the adapter is connected to
	 */
	public RecipeIngredientSearchAdapter(IngredientDB db) {
		super(db.getQuery());
		this.db = db;
	}

	/**
	 * Inflate the layout of a single View into the parent ViewGroup
	 *
	 * @param parent   the parent ViewGroup to inflate the layout in
	 * @param viewType an identifier for the view type
	 * @return the new ViewHolder for a Recipe created
	 */
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
			.inflate(R.layout.storage_ingredient_view_holder, parent, false);
		return new ViewHolder(view);
	}

	/**
	 * Bind the data with the views in the RecyclerView
	 *
	 * @param holder   the ViewHolder which holds Views to be bound to the recipe
	 * @param position the position of the View in the ViewHolder to bind the data to
	 */
	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		Ingredient ingredient = db.snapshotToIngredient(getSnapshot(position));
		holder.bind(ingredient, this.listener);
	}

	/**
	 * Sets the on-click listener for the RecyclerView
	 *
	 * @param listener the on-click listener that will be notified when an item is clicked
	 */
	public void setListener(OnItemClickListener listener) {
		this.listener = listener;
	}

	/**
	 * Holds the views for the ingredients in the search
	 */
	public static class ViewHolder extends RecyclerView.ViewHolder {
		/**
		 * The text view containing the description of the ingredient
		 */
		private final TextView textView;

		/**
		 * The text view containing the category of an ingredient
		 */
		private final TextView subTextView;

		/**
		 * The view for the ingredient
		 */
		private final View view;

		/**
		 * Creates a ViewHolder for an ingredient
		 *
		 * @param view the view to use
		 */
		public ViewHolder(@NonNull View view) {
			super(view);
			this.view = view;
			this.textView = view.findViewById(R.id.textView);
			this.subTextView = view.findViewById(R.id.subTextView);
		}

		/**
		 * Binds an ingredient to a ViewHolder, to properly display the data
		 *
		 * @param ingredient the Ingredient to bind to the ViewHolder
		 * @param listener   the ingredient click listener to bind to the ViewHolder
		 */
		public void bind(Ingredient ingredient, OnItemClickListener listener) {
			this.textView.setText(ingredient.getDescription());
			this.subTextView.setText(ingredient.getCategory());
			if (listener != null) {
				this.view.setOnClickListener(v -> listener.onItemClick(ingredient));
			}
		}
	}
}
