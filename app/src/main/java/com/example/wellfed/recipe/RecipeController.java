package com.example.wellfed.recipe;

import androidx.fragment.app.FragmentActivity;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.common.DBConnection;


/**
 * Handles the business logic for the Recipes
 *
 * @version 1.0.0
 */
public class RecipeController {

	private final ActivityBase activity;

	/**
	 * Adapter for the list of recipes
	 */
	private final RecipeAdapter recipeAdapter;

	/**
	 * Stores the instance of {@link RecipeDB}
	 */
	private final RecipeDB recipeDB;

	/**
	 * Constructor that initializes the db connection and the adapter for the recipes
	 */
	public RecipeController(FragmentActivity activity) {
		this.activity = (ActivityBase) activity;
		DBConnection connection = new DBConnection(activity.getApplicationContext());
		recipeDB = new RecipeDB(connection);
		this.recipeAdapter = new RecipeAdapter(recipeDB);
	}

	/**
	 * Requests the database to edit a Recipe and handles the result
	 *
	 * @param recipe the recipe to edit in the database
	 */
	public void editRecipe(Recipe recipe) {
		recipeDB.updateRecipe(recipe, (updated, success) -> {
			if (!success) {
				this.activity.makeSnackbar("Failed to edit recipe");
			}
		});
	}

	/**
	 * Requests the database to delete a Recipe and handles the result
	 *
	 * @param id of the recipe to delete
	 */
	public void deleteRecipe(String id) {
		recipeDB.delRecipe(id, (deleted, success) -> {
			if (!success) {
				this.activity.makeSnackbar("Failed to delete recipe");
			}
		});
	}

	/**
	 * Adds the recipe to db and notifies the adapter
	 *
	 * @param recipe the recipe to add
	 */
	public void addRecipe(Recipe recipe) {
		recipeDB.addRecipe(recipe, (added, success) -> {
			if (!success) {
				this.activity.makeSnackbar("Failed to add " + recipe.getTitle());
			}
		});
	}


	/**
	 * Gets the sorted query of recipes.
	 *
	 * @param field the field to sort by
	 */
	public void sort(String field) {
		recipeAdapter.clearSnapshots();
		recipeAdapter.changeQuery(recipeDB.getSortQuery(field));
	}

	/**
	 * Gets the RecipeAdapter that connects the list of Recipes to the DB
	 *
	 * @return the RecipeAdapter
	 */
	public RecipeAdapter getRecipeAdapter() {
		return recipeAdapter;
	}

}
