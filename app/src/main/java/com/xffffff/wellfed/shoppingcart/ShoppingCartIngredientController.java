package com.xffffff.wellfed.shoppingcart;

import android.app.Activity;
import android.util.Pair;

import com.xffffff.wellfed.ActivityBase;
import com.xffffff.wellfed.common.DBConnection;
import com.xffffff.wellfed.ingredient.Ingredient;
import com.xffffff.wellfed.ingredient.StorageIngredient;
import com.xffffff.wellfed.ingredient.StorageIngredientDB;
import com.xffffff.wellfed.mealplan.MealPlan;
import com.xffffff.wellfed.mealplan.MealPlanDB;
import com.xffffff.wellfed.recipe.Recipe;
import com.xffffff.wellfed.unit.UnitConverter;
import com.xffffff.wellfed.unit.UnitHelper;

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
        DBConnection connection =
                new DBConnection(activity.getApplicationContext());
        db = new ShoppingCartDB(connection);
        adapter = new ShoppingCartIngredientAdapter(db);
        storageIngredientDB = new StorageIngredientDB(connection);
        mealPlanDB = new MealPlanDB(connection);
    }

    /**
     * Gets the adapter.
     *
     * @return the adapter
     */
    public ShoppingCartIngredientAdapter getAdapter() {
        return adapter;
    }

    /**
     * Sets adapter to the given adapter.
     *
     * @param adapter the adapter to set
     */
    public void setAdapter(ShoppingCartIngredientAdapter adapter) {
        this.adapter = adapter;
    }

    public void generateShoppingCart() {
        UnitConverter unitConverter =
                new UnitConverter(activity.getApplicationContext());
        UnitHelper unitHelper = new UnitHelper(unitConverter);
        HashMap<String, Double> needed = new HashMap<>();
        mealPlanDB.getMealPlans((mealPlans, success) -> {
            for (MealPlan mealPlan : mealPlans) {
                for (Recipe r : mealPlan.getRecipes()) {
                    for (Ingredient ingredient : r.getIngredients()) {
                        Pair<Double, String> val =
                                bestUnitHelper(ingredient, unitHelper);
                        needed.put(val.second, val.first);
                    }
                }
                for (Ingredient ingredient : mealPlan.getIngredients()) {
                    String uid = ingredientUid(ingredient);
                    if (needed.get(uid) != null) {
                        Pair<Double, String> val =
                                bestUnitHelper(ingredient, unitHelper);
                        needed.put(val.second, needed.get(uid) + val.first);
                    } else {
                        Pair<Double, String> val =
                                bestUnitHelper(ingredient, unitHelper);
                        needed.put(val.second, val.first);
                    }
                }
            }
            // get all the items in the list
            storageIngredientDB.getAllStorageIngredients(
                    (storedIngredients, success1) -> {
                        if (!success1) {

                        }
                        for (StorageIngredient stored : storedIngredients) {
                            Pair<Double, String> val =
                                    bestUnitHelper(stored, unitHelper);
                            String uid = val.second;
                            if (needed.get(uid) != null) {
                                needed.put(val.second,
                                        needed.get(uid) - val.first);
                            }
                        }

                        // get all the items in the storage
                        db.getShoppingCart((cart, success2) -> {
                            HashMap<String, Integer> inCart = new HashMap<>();

                            for (int i = 0; i < cart.size(); i++) {
                                ShoppingCartIngredient cartItem = cart.get(i);
                                String uid = ingredientUid(cartItem);
                                inCart.put(uid, i);
                            }

                            // if shopping cart has something meal plan does not, remove it
                            for (String key : inCart.keySet()) {
                                if (needed.get(key) == null) {
                                    ShoppingCartIngredient cartItem =
                                            cart.get(inCart.get(key));
                                    db.deleteIngredient(cartItem, (a, s) -> {
                                        s = false;
                                    });
                                }
                            }

                            // if shopping cart has those items update them
                            // accordingly
                            for (String key : needed.keySet()) {
                                if (inCart.get(key) != null) {
                                    String[] details = key.split("####");
                                    String unit = details[2];
                                    // get items from the shopping cart
                                    ShoppingCartIngredient cartItem =
                                            cart.get(inCart.get(key));
                                    cartItem.setUnit(unit);
                                    cartItem.setAmount(needed.get(key));
                                    db.updateIngredient(cartItem, (a, s) -> {
                                        s = false;
                                    });
                                } else { // else create those items
                                    String[] details = key.split("####");
                                    String unit = details[2];
                                    Ingredient ingredient =
                                            new Ingredient(details[0]);
                                    ingredient.setUnit(unit);
                                    ingredient.setCategory(details[1]);
                                    ingredient.setAmount(needed.get(key));
                                    db.addIngredient(ingredient, (a, s) -> {
                                        s = false;
                                    });
                                }

                            }

                        });
                    });
        });


    }

    private Pair<Double, String> bestUnitHelper(Ingredient ingredient,
                                                UnitHelper unitHelper) {
        // reduce to smallest
        Pair<Double, String> smallest =
                unitHelper.convertToSmallest(ingredient.getUnit(),
                        ingredient.getAmount());
        ingredient.setUnit(smallest.second);
        ingredient.setAmount(smallest.first);

        Pair<Double, String> best = unitHelper.availableUnits(ingredient);
        ingredient.setAmount(best.first);
        ingredient.setUnit(best.second);

        return new Pair<>(ingredient.getAmount(), ingredientUid(ingredient));

    }

    private String ingredientUid(Ingredient ingredient) {
        String seperator = "####";
        String uid = ingredient.getDescription() + seperator +
                ingredient.getCategory() + seperator + ingredient.getUnit();
        return uid;
    }

    /**
     * Adds the ingredient to the shopping cart.
     *
     * @param ingredient the ingredient to add
     */
    public void addIngredientToShoppingCart(ShoppingCartIngredient ingredient) {
        db.addIngredient(ingredient, (addIngredient, addSuccess) -> {
            if (!addSuccess) {
                this.activity.makeSnackbar(
                        "Failed to add " + addIngredient.getDescription());
            } else {
                this.activity.makeSnackbar(
                        "Added " + addIngredient.getDescription());
            }
        });
    }

    /**
     * Updates the ingredient in the shopping cart.
     *
     * @param ingredient the ingredient to update
     */
    public void updateIngredientInShoppingCart(
            ShoppingCartIngredient ingredient) {
        db.updateIngredient(ingredient, (updateIngredient, updateSuccess) -> {
            if (!updateSuccess) {
                this.activity.makeSnackbar("Failed to update " +
                        updateIngredient.getDescription());
            } else {
                this.activity.makeSnackbar(
                        "Updated " + updateIngredient.getDescription());
            }
        });
    }
}
