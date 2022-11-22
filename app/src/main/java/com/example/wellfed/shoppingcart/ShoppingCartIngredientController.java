package com.example.wellfed.shoppingcart;

import android.app.Activity;
import android.util.Pair;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.common.DBConnection;
import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.ingredient.StorageIngredient;
import com.example.wellfed.ingredient.StorageIngredientDB;
import com.example.wellfed.mealplan.MealPlan;
import com.example.wellfed.mealplan.MealPlanDB;
import com.example.wellfed.recipe.Recipe;

import java.util.ArrayList;
import java.util.HashMap;

public class ShoppingCartIngredientController {
    /**
     * The Activity that the controller is attached to.
     */
    private ActivityBase activity;
    /**
     * ShoppingCart adapter. This is used to update the recycler view.
     */
    private ShoppingCartIngredientAdapter adapter;
    /**
     * This is the db of the shopping cart.
     */
    private ShoppingCartDB db;

    private StorageIngredientDB storageIngredientDB;

    private MealPlanDB mealPlanDB;

    /**
     * The constructor for the controller.
     *
     * @param activity The activity that the controller is attached to.
     */
    public ShoppingCartIngredientController(Activity activity) {
        this.activity = (ActivityBase) activity;
        DBConnection connection = new DBConnection(activity.getApplicationContext());
        db = new ShoppingCartDB(connection);
        adapter = new ShoppingCartIngredientAdapter(db);
        storageIngredientDB = new StorageIngredientDB(connection);
        mealPlanDB = new MealPlanDB(connection);
    }

    /**
     * Sets adapter to the given adapter.
     *
     * @param adapter the adapter to set
     */
    public void setAdapter(ShoppingCartIngredientAdapter adapter) {
        this.adapter = adapter;
    }

    /**
     * Gets the adapter.
     *
     * @return the adapter
     */
    public ShoppingCartIngredientAdapter getAdapter() {
        return adapter;
    }

    public void generateShoppingCart() {
        HashMap<String, Double> needed = new HashMap<>();
        mealPlanDB.getMealPlans((mealPlans, success) -> {
            success = false;
            for (MealPlan mealPlan : mealPlans) {
                for (Recipe r : mealPlan.getRecipes()) {
                    for (Ingredient ingredient : r.getIngredients()) {
                        needed.put(ingredient.getDescription().toLowerCase()
                                + ingredient.getCategory().toLowerCase(), ingredient.getAmount());
                    }
                }
                for (Ingredient ingredient : mealPlan.getIngredients()) {
                    needed.put(ingredient.getDescription().toLowerCase()
                            + ingredient.getCategory().toLowerCase(), ingredient.getAmount());
                }
            }
            // get all the items in the list
            storageIngredientDB.getAllStorageIngredients((storedIngredients, success1) -> {
                if (!success1) {

                }
                for (StorageIngredient stored : storedIngredients) {
                    String uid = stored.getDescription().toLowerCase() + stored.getCategory().toLowerCase();
                    if (needed.get(uid) != null) {
                        needed.put(uid, needed.get(uid) - stored.getAmount());
                    }
                }
                // get all the items in the storage
                db.getShoppingCart((cart, success2) -> {
                    HashMap<String, Pair<String, Double>> inCart = new HashMap<>();
                    for (ShoppingCartIngredient cartItem : cart) {
                        String uid = cartItem.getDescription().toLowerCase() + cartItem.getCategory().toLowerCase()
                                + cartItem.getId();
                        inCart.put(uid, new Pair<>(cartItem.getId(), cartItem.getAmount()));
                    }
                    // if shopping cart has those items update them accordingly
                    for (String key : needed.keySet()) {
                        if (inCart.get(key) != null) {
                            Ingredient ingredient = new Ingredient();
                            ingredient.setCategory("adfafasfdasfa");
                            ingredient.setDescription(key);
                            ingredient.setAmount(needed.get(key));
                            ingredient.setUnit("lb");
                            db.addIngredient(ingredient, (added, success3) -> {

                            });
                        } else { // else create those items
                            Ingredient ingredient = new Ingredient();
                            ingredient.setCategory("adadfasdadasd");
                            ingredient.setDescription(key);
                            ingredient.setAmount(needed.get(key));
                            ingredient.setUnit("lb");
                            db.addIngredient(ingredient, (added, success3) -> {

                            });
                        }
                    }

                });
            });
        });


    }

    /**
     * Adds the ingredient to the shopping cart.
     *
     * @param ingredient the ingredient to add
     */
    public void addIngredientToShoppingCart(ShoppingCartIngredient ingredient) {
        db.addIngredient(ingredient, (addIngredient, addSuccess) -> {
            if (!addSuccess) {
                this.activity.makeSnackbar("Failed to add " + addIngredient.getDescription());
            } else {
                this.activity.makeSnackbar("Added " + addIngredient.getDescription());
            }
        });
    }

    /**
     * Deletes the ingredient from the shopping cart.
     *
     * @param ingredient the ingredient to delete
     */
    public void deleteIngredientFromShoppingCart(ShoppingCartIngredient ingredient) {
        db.deleteIngredient(ingredient, (delIngredient, delSuccess) -> {
            if (!delSuccess) {
                this.activity.makeSnackbar("Failed to delete " + delIngredient.getDescription());
            } else {
                this.activity.makeSnackbar("Deleted " + delIngredient.getDescription());
            }
        });
    }

    /**
     * Updates the ingredient in the shopping cart.
     *
     * @param ingredient the ingredient to update
     */
    public void updateIngredientInShoppingCart(ShoppingCartIngredient ingredient) {
        db.updateIngredient(ingredient, (updateIngredient, updateSuccess) -> {
            if (!updateSuccess) {
                this.activity.makeSnackbar("Failed to update " + updateIngredient.getDescription());
            } else {
                this.activity.makeSnackbar("Updated " + updateIngredient.getDescription());
            }
        });
    }
}
