package com.example.wellfed.shoppingcart;

import android.app.Activity;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.common.DBConnection;
import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.ingredient.StorageIngredient;

import java.util.ArrayList;

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
    }

    /**
     * Sets adapter to the given adapter.
     * @param adapter the adapter to set
     */
    public void setAdapter(ShoppingCartIngredientAdapter adapter) {
        this.adapter = adapter;
    }

    /**
     * Gets the adapter.
     * @return the adapter
     */
    public ShoppingCartIngredientAdapter getAdapter() {
        return adapter;
    }

    public void generateShoppingCart(){
        Ingredient ingredient = new Ingredient();
        ingredient.setDescription("test");
        ingredient.setUnit("lb");
        ingredient.setAmount(2.0);
        ingredient.setCategory("fruit");
        db.addIngredient(ingredient, (a,s)->{

        });
    }    /**
     * Adds the ingredient to the shopping cart.
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
