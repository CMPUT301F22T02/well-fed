package com.example.wellfed.shoppingcart;

import com.example.wellfed.common.DBConnection;
import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.ingredient.IngredientDB;
import com.example.wellfed.ingredient.StorageIngredientDB;
import com.example.wellfed.mealplan.MealPlanDB;
import com.example.wellfed.recipe.RecipeDB;
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
	 * This interface is used to handle the result of updating the shopping
	 * cart with respect to the MealPlan
	 */

	/**
	 * Update the shopping cart with the ingredients from the meal plan if
	 * not present in the StorageIngredientDB.
	 */
	public void updateShoppingCart() {

	}


}
