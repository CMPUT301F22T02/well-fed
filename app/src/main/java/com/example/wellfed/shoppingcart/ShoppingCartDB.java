package com.example.wellfed.shoppingcart;

import androidx.annotation.NonNull;

import com.example.wellfed.common.DBConnection;
import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.ingredient.IngredientDB;
import com.example.wellfed.ingredient.StorageIngredientDB;
import com.example.wellfed.mealplan.MealPlanDB;
import com.example.wellfed.recipe.RecipeDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

// TODO: create DB connection between shopping cart and Firestore.
public class ShoppingCartDB {
	/**
	 * Holds the tag for the logging purposes.
	 */
	private static final String TAG = "ShoppingCartDB";
	/**
	 * Holds the instance of the Firebase Firestore database.
	 */
	private FirebaseFirestore db;
	/**
	 * Holds a reference to the IngredientDB.
	 */
	private IngredientDB ingredientDB;
	/**
	 * Holds a reference to StorageIngredientDB.
	 */
	private StorageIngredientDB storageIngredientDB;
	/**
	 * Holds a reference to the recipeDB.
	 */
	private RecipeDB recipeDB;
	/**
	 * Holds a reference to MealPlanDB.
	 */
	private MealPlanDB mealPlanDB;
	/**
	 * Holds a connection to the users ingredient collection in the DB.
	 */
	private DBConnection connection;
	/**
	 * Holds the collection for the users
	 */
	private CollectionReference collection;

	/**
	 * Constructor for the ShoppingCartDB.
	 */
	public ShoppingCartDB(DBConnection connection) {
		this.connection = connection;
		this.ingredientDB = new IngredientDB(connection);
		this.storageIngredientDB = new StorageIngredientDB(connection);
		this.recipeDB = new RecipeDB(connection);
		this.mealPlanDB = new MealPlanDB(connection);
		this.db = connection.getDB();
		this.collection = connection.getCollection("ShoppingCart");
	}

	/**
	 * This interface is used to handle the result of adding of an ingredient
	 * to the shopping cart.
	 */
	public interface OnAddShoppingCart {
		/**
		 * This method is called when the shopping cart is updated.
		 *
		 * @param success True if the shopping cart was updated successfully.
		 */
		void onAddShoppingCart(Ingredient ingredient, boolean success);
	}

	/**
	 * Add an ingredient to the shopping cart.
	 *
	 * @param ingredient The ingredient to add to the shopping cart.
	 * @param listener   The listener to call when the ingredient is added.
	 */
	public void addIngredient(Ingredient ingredient,
							  OnAddShoppingCart listener) {
		if (ingredient == null) {
			listener.onAddShoppingCart(null, false);
			return;
		}

		if (ingredient.getId() == null) {
			ingredientDB.addIngredient(ingredient, (addedIngredient, success) -> {
				if (success) {
					addIngredient(addedIngredient, listener);
				} else {
					listener.onAddShoppingCart(null, false);
				}
			});
		}
	}

	/**
	 * This interface is used to handle the result of removing an ingredient
	 * from the shopping cart.
	 */
	public interface OnRemoveShoppingCart {
		/**
		 * This method is called when the shopping cart is updated.
		 *
		 * @param success True if the shopping cart was updated successfully.
		 */
		void onRemoveShoppingCart(Ingredient ingredient, boolean success);
	}

	/**
	 * Delete an ingredient from the shopping cart.
	 */
	public void deleteIngredient(Ingredient ingredient,
								 OnRemoveShoppingCart listener) {
		if (ingredient == null) {
			listener.onRemoveShoppingCart(null, false);
			return;
		}
		if (ingredient.getId() == null) {
			listener.onRemoveShoppingCart(null, false);
			return;
		}

		collection.document(ingredient.getId()).delete()
			.addOnCompleteListener(task ->
				listener.onRemoveShoppingCart(ingredient, task.isSuccessful()));
	}

	/**
	 * This interface is used to handle the updating of the shopping cart
	 * cart with respect to the MealPlan and the Storage.
	 */
	public interface OnUpdateShoppingCart {
		/**
		 * This method is called when the shopping cart is updated.
		 *
		 * @param success True if the shopping cart was updated successfully.
		 */
		void onUpdateShoppingCart(boolean success);
	}

	/**
	 * Update the shopping cart with the ingredients from the meal plan if
	 * not present in the StorageIngredientDB.
	 */
	public void updateShoppingCart(OnUpdateShoppingCart listener) {
		mealPlanDB.getMealPlans((mealPlans, success) -> {
			if (success) {
				for (int i = 0; i < mealPlans.size(); i++) {
					recipeDB.getRecipe(mealPlans.get(i).getId(), (recipe,
																  success1) -> {
						if (success1) {
							for (int j = 0; j < recipe.getIngredients().size(); j++) {
								Ingredient ingredient = recipe.getIngredients().get(j);
								storageIngredientDB.getStorageIngredient(ingredient.getId(), (storageIngredient, success2) -> {
									if (success2) {
										if (storageIngredient == null) {
											addIngredient(ingredient, (ingredient1, success3) -> {
												listener.onUpdateShoppingCart(success3);
											});
										}
									} else {
										listener.onUpdateShoppingCart(false);
									}
								});
							}
						} else {
							listener.onUpdateShoppingCart(false);
						}
					});
				}
			} else {
				listener.onUpdateShoppingCart(false);
			}
		});
	}
}
