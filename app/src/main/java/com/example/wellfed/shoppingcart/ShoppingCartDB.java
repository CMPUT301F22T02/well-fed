package com.example.wellfed.shoppingcart;

import androidx.annotation.NonNull;

import com.example.wellfed.common.DBConnection;
import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.ingredient.IngredientDB;
import com.example.wellfed.ingredient.StorageIngredient;
import com.example.wellfed.ingredient.StorageIngredientDB;
import com.example.wellfed.mealplan.MealPlan;
import com.example.wellfed.mealplan.MealPlanDB;
import com.example.wellfed.recipe.Recipe;
import com.example.wellfed.recipe.RecipeDB;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.Map;

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
		void onAddShoppingCart(ShoppingCartIngredient ingredient, boolean success);
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
					ShoppingCartIngredient shoppingCartIngredient =
							new ShoppingCartIngredient(addedIngredient);
					addIngredient(shoppingCartIngredient, listener);
				} else {
					listener.onAddShoppingCart(null, false);
				}
			});
		}

		ShoppingCartIngredient shoppingCartIngredient =
				new ShoppingCartIngredient(ingredient);

		shoppingCartIngredient.setId(ingredient.getId());
		shoppingCartIngredient.setDescription(ingredient.getDescription());
		shoppingCartIngredient.setAmount(ingredient.getAmount());
		shoppingCartIngredient.setUnit(ingredient.getUnit());
		shoppingCartIngredient.setCategory(ingredient.getCategory());
		HashMap<String, Object> storageIngredientMap = new HashMap<>();
		storageIngredientMap.put("id", ingredient.getId());
		storageIngredientMap.put("description", ingredient.getDescription());
		storageIngredientMap.put("amount", ingredient.getAmount());
		storageIngredientMap.put("unit", ingredient.getUnit());
		storageIngredientMap.put("category", ingredient.getCategory());
		storageIngredientMap.put("Ingredient", ingredientDB.getDocumentReference(ingredient.getId()));
		collection.document(ingredient.getId()).set(storageIngredientMap).addOnCompleteListener(task -> {
			if (task.isSuccessful()) {
				listener.onAddShoppingCart(shoppingCartIngredient, true);
			} else {
				listener.onAddShoppingCart(null, false);
			}
		});
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
		void onRemoveShoppingCart(ShoppingCartIngredient ingredient, boolean success);
	}

	/**
	 * Delete an ingredient from the shopping cart.
	 */
	public void deleteIngredient(ShoppingCartIngredient ingredient,
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
	 * Update an ingredient in the shopping cart.
	 */
	public void updateIngredient(ShoppingCartIngredient ingredient,
								 OnAddShoppingCart listener) {
		if (ingredient == null) {
			listener.onAddShoppingCart(null, false);
			return;
		}
		if (ingredient.getId() == null) {
			listener.onAddShoppingCart(null, false);
			return;
		}

		HashMap<String, Object> storageIngredientMap = new HashMap<>();
		storageIngredientMap.put("id", ingredient.getId());
		storageIngredientMap.put("description", ingredient.getDescription());
		storageIngredientMap.put("amount", ingredient.getAmount());
		storageIngredientMap.put("unit", ingredient.getUnit());
		storageIngredientMap.put("category", ingredient.getCategory());
		storageIngredientMap.put("Ingredient", ingredientDB.getDocumentReference(ingredient.getId()));
		collection.document(ingredient.getId()).set(storageIngredientMap).addOnCompleteListener(task -> {
			if (task.isSuccessful()) {
				listener.onAddShoppingCart(ingredient, true);
			} else {
				listener.onAddShoppingCart(null, false);
			}
		});
	}

	/**
	 * This interface is used to handle the result of getting the shopping cart.
	 */
	public interface OnGetShoppingCart {
		/**
		 * This method is called when the shopping cart is retrieved.
		 *
		 * @param success True if the shopping cart was retrieved successfully.
		 */
		void onGetShoppingCart(ShoppingCart shoppingCart, boolean success);
	}

	/**
	 * Get the shopping cart.
	 */
	public void getShoppingCart(OnGetShoppingCart listener) {
		collection.get().addOnCompleteListener(task -> {
			if (task.isSuccessful()) {
				ShoppingCart shoppingCart = new ShoppingCart();
				for (QueryDocumentSnapshot document : task.getResult()) {
					DocumentReference ingredientRef = (DocumentReference) document.get("Ingredient");
					assert ingredientRef != null;
					ingredientRef.get().addOnCompleteListener(task1 -> {
						if (task1.isSuccessful()) {
							DocumentSnapshot document1 = task1.getResult();
							if (document1.exists()) {
								Ingredient ingredient = document1.toObject(Ingredient.class);
								assert ingredient != null;
								ShoppingCartIngredient shoppingCartIngredient =
									new ShoppingCartIngredient(ingredient);
								shoppingCartIngredient.setId(ingredient.getId());
								shoppingCartIngredient.setDescription(ingredient.getDescription());
								shoppingCartIngredient.setAmount(ingredient.getAmount());
								shoppingCartIngredient.setUnit(ingredient.getUnit());
								shoppingCartIngredient.setCategory(ingredient.getCategory());
								shoppingCart.addIngredient(shoppingCartIngredient);
							}
						}
					});
				}
				listener.onGetShoppingCart(shoppingCart, true);
			} else {
				listener.onGetShoppingCart(null, false);
			}
		});
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
		mealPlanDB.getMealPlans((mealPlan, success) -> {
			if (success) {
				storageIngredientDB.getAllStorageIngredients((storageIngredients, success2) -> {
					if (success2) {
						ShoppingCart shoppingCart = new ShoppingCart();
						for (MealPlan meal : mealPlan) {
							for (Ingredient ingredient : meal.getIngredients()) {
								// Check if the ingredient is in the storage
								// using getDescription
								boolean inStorage = false;
								for (StorageIngredient storageIngredient : storageIngredients) {
									if (storageIngredient.getDescription().equals(ingredient.getDescription())) {
										inStorage = true;
										break;
									}
								}
								if (!inStorage) {
									shoppingCart.addIngredient(new ShoppingCartIngredient(ingredient));
								}
							}
						}
						updateShoppingCart(listener);
					} else {
						listener.onUpdateShoppingCart(false);
					}
				});
			}
		});
	}
}
