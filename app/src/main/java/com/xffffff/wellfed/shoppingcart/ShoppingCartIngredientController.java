package com.xffffff.wellfed.shoppingcart;

import android.app.Activity;
import android.util.Pair;

import com.google.firebase.firestore.Query;
import com.xffffff.wellfed.ActivityBase;
import com.xffffff.wellfed.common.DBConnection;
import com.xffffff.wellfed.common.DateUtil;
import com.xffffff.wellfed.ingredient.Ingredient;
import com.xffffff.wellfed.ingredient.StorageIngredient;
import com.xffffff.wellfed.ingredient.StorageIngredientDB;
import com.xffffff.wellfed.mealplan.MealPlan;
import com.xffffff.wellfed.mealplan.MealPlanController;
import com.xffffff.wellfed.mealplan.MealPlanDB;
import com.xffffff.wellfed.recipe.Recipe;
import com.xffffff.wellfed.unit.Unit;
import com.xffffff.wellfed.unit.UnitConverter;
import com.xffffff.wellfed.unit.UnitHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ShoppingCartIngredientController is a class that contains methods for
 * interacting with the shopping cart
 */
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
     * This is the db of the storage ingredients.
     */
    private StorageIngredientDB storageIngredientDB;
    /**
     * This is the db of the meal plans.
     */
    private MealPlanDB mealPlanDB;

    private UnitHelper unitHelper;

    private UnitConverter unitConverter;

    private MealPlanController mealPlanController;

    private DateUtil dateUtil;

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
        UnitConverter unitConverter =
                new UnitConverter(activity.getApplicationContext());
        unitHelper = new UnitHelper(unitConverter);
        mealPlanController = new MealPlanController(activity);
        dateUtil = new DateUtil();
    }

    /**
     * getSearchResults returns the search results for the given query.
     *
     * @param field The field to search.
     */
    public void getSearchResults(String field) {
    public void getSearchResults(String field) {
        Query query = db.getSearchQuery(field);
        adapter.setQuery(query);
    }

    /**
     * sortByField sorts the shopping cart by the given field.
     *
     * @param field The field to sort by.
     */
    public void sortByField(String field) {
    public void sortByField(String field) {
        Query query = db.getSortedQuery(field);
        adapter.setQuery(query);
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

    /**
     * generateShoppingCart generates the shopping cart from the given meal
     * plan and storage ingredients.
     */
    public void generateShoppingCart() {
        unitConverter =
                new UnitConverter(activity.getApplicationContext());
        unitHelper = new UnitHelper(unitConverter);
        HashMap<String, ArrayList<Pair<Integer, Double>>> required = new HashMap<>();

        mealPlanDB.getMealPlans((mealPlans, success) -> {
            for (MealPlan mealPlan : mealPlans) {
                for (Recipe r : mealPlan.getRecipes()) {
                    generateRequiredFromRecipes(r, mealPlan, required);
                }
                for (Ingredient ingredient : mealPlan.getIngredients()) {
                    generateRequiredFromIngredients(ingredient, mealPlan, required);
                }
            }
            // get all the items in the list
            storageIngredientDB.getAllStorageIngredients(
                    (storedIngredients, success1) -> {
                        if (!success1) {
                            // todo add error msg
                            return;
                        }
                        updateRequiredFromStorage(storedIngredients, required);


                        // get all the items in the storage
                        HashMap<String, Double> needed = new HashMap<>();
                        for (String key : required.keySet()) {
                            if (needed.get(key) == null) {
                                Double amountNeeded = 0.0;
                                for (int i = 0; i < required.get(key).size(); i++) {
                                    amountNeeded += required.get(key).get(i).second;
                                }
                                needed.put(key, amountNeeded);
                            }
                        }
                        db.getShoppingCart((cart, success2) -> {
                            HashMap<String, Integer> inCart = new HashMap<>();

                            for (int i = 0; i < cart.size(); i++) {
                                ShoppingCartIngredient cartItem = cart.get(i);
                                Pair<Double, String> val =
                                        bestUnitHelper(cartItem, unitHelper);

                                inCart.put(val.second, i);
                            }

                            // if shopping cart has something meal plan does not, remove it
                            for (String key : inCart.keySet()) {
                                ShoppingCartIngredient cartItem =
                                        cart.get(inCart.get(key));
                                Pair<Double, Unit> bestVal = unitConverter.scaleUnit(cartItem.getAmount(),
                                        unitConverter.build(cartItem.getUnit()));
                                if (bestVal.first <= 0.0 || needed.get(key) == null || needed.get(key) <= 0.0) {
                                    if (!cartItem.isPickedUp) {
                                        db.deleteIngredient(cartItem, (a, s) -> {
                                        });
                                    }
                                }
                            }
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
                                    Pair<Double, Unit> bestVal = unitConverter.scaleUnit(needed.get(key),
                                            unitConverter.build(unit));
                                    if (bestVal.first > 0.0) {
                                        cartItem.setUnit(bestVal.second.getUnit());
                                        cartItem.setAmount(bestVal.first);
                                        db.updateIngredient(cartItem, (a, s) -> {
                                            s = false;
                                        });
                                    }
                                } else { // else create those items
                                    String[] details = key.split("####");
                                    String unit = details[2];
                                    Ingredient ingredient =
                                            new Ingredient(details[0]);
                                    ingredient.setCategory(details[1]);
                                    Pair<Double, Unit> bestVal = unitConverter.scaleUnit(needed.get(key),
                                            unitConverter.build(unit));
                                    if (bestVal.first > 0.0) {
                                        ingredient.setUnit(bestVal.second.getUnit());
                                        ingredient.setAmount(bestVal.first);
                                        db.addIngredient(ingredient, (a, s) -> {
                                            s = false;
                                        });
                                    }
                                }

                    }

                });
            });
        });
    }

    /**
     * bestUnitHelper returns the best unit for the given ingredient.
     *
     * @param ingredient the ingredient to get the best unit for.
     * @param unitHelper the unit helper to use.
     * @return the best unit for the given ingredient.
     */
    private Pair<Double, String> bestUnitHelper(Ingredient ingredient, UnitHelper unitHelper) {
        // reduce to smallest
        Pair<Double, String> smallest = unitHelper.convertToSmallest(ingredient.getUnit(), ingredient.getAmount());
        ingredient.setUnit(smallest.second);
        ingredient.setAmount(smallest.first);

        Pair<Double, String> best = unitHelper.availableUnits(ingredient);
        ingredient.setAmount(best.first);
        ingredient.setUnit(best.second);

        return new Pair<>(ingredient.getAmount(), ingredientUid(ingredient));

    }

    /**
     * ingredientUid returns the uid for the given ingredient.
     *
     * @param ingredient the ingredient to get the uid for.
     * @return the uid for the given ingredient.
     */
    private String ingredientUid(Ingredient ingredient) {
        String seperator = "####";
        String uid = ingredient.getDescription() + seperator + ingredient.getCategory() + seperator + ingredient.getUnit();
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
                this.activity.makeSnackbar("Failed to add " + addIngredient.getDescription());
            } else {
                this.activity.makeSnackbar("Added " + addIngredient.getDescription());
            }
        });
    }

    public void updateCheckedStatus(String id, boolean isChecked) {
        db.updateIngredient(id, isChecked, (success -> {
            success = false;
        }));
    }

    /**
     * Updates the ingredient in the shopping cart.
     *
     * @param ingredient the ingredient to update
     */
    public void updateIngredientInShoppingCart(ShoppingCartIngredient ingredient) {
        db.updateIngredient(ingredient, (updateIngredient, updateSuccess) -> {
            if (!updateSuccess) {
                this.activity.makeSnackbar("Failed to update ");
            } else {
                this.activity.makeSnackbar("Updated " + updateIngredient.getDescription());
            }
        });
    }

    private void updateRequiredFromStorage(ArrayList<StorageIngredient> storedIngredients,
                                           HashMap<String, ArrayList<Pair<Integer, Double>>> required) {
        for (StorageIngredient stored : storedIngredients) {
            Pair<Double, String> val =
                    bestUnitHelper(stored, unitHelper);
            String uid = val.second;
            if (required.get(uid) != null) {
                if (required.get(uid) != null) {
                    ArrayList<Pair<Integer, Double>> neededBefore = required.get(uid);
                    for (int i = 0; i < neededBefore.size(); i++) {
                        // if ingredient can be used based on expiry
                        if (neededBefore.get(i).first < dateUtil.format(stored.getBestBefore())) {
                            if (neededBefore.get(i).second <= 0.0) {
                                continue;
                            }
                            Double stillRequired;
                            if (neededBefore.get(i).second < stored.getAmount()) {
                                stillRequired = neededBefore.get(i).second - stored.getAmount();
                                stored.setAmount(stored.getAmount() - neededBefore.get(i).second);
                            } else {
                                stillRequired = neededBefore.get(i).second - stored.getAmount();
                                stored.setAmount(0.0);
                            }
                            neededBefore.set(i, new Pair<>(neededBefore.get(i).first,
                                    stillRequired));
                        }
                    }
                }
            }
        }
    }

    private void generateRequiredFromIngredients(Ingredient ingredient, MealPlan mealPlan,
                                                 HashMap<String, ArrayList<Pair<Integer, Double>>> required) {
        String uid = ingredientUid(ingredient);
        if (required.get(uid) != null) {
            Pair<Double, String> val =
                    bestUnitHelper(ingredient, unitHelper);
            if (required.get(val.second) == null) {
                ArrayList<Pair<Integer, Double>> listNeeded = new ArrayList<>();
                listNeeded
                        .add(new Pair<Integer, Double>(dateUtil.format(mealPlan.getEatDate()), val.first));
                required.put(val.second, listNeeded);
            } else {
                required.get(val.second)
                        .add(new Pair<Integer, Double>(dateUtil.format(mealPlan.getEatDate()), val.first));
            }
        } else {
            Pair<Double, String> val =
                    bestUnitHelper(ingredient, unitHelper);
            if (required.get(val.second) == null) {
                ArrayList<Pair<Integer, Double>> listNeeded = new ArrayList<>();
                listNeeded
                        .add(new Pair<Integer, Double>(dateUtil.format(mealPlan.getEatDate()), val.first));
                required.put(val.second, listNeeded);
            } else {
                required.get(val.second)
                        .add(new Pair<Integer, Double>(dateUtil.format(mealPlan.getEatDate()), val.first));
            }
        }
    }

    private void generateRequiredFromRecipes(Recipe recipe, MealPlan mealPlan,
                                             HashMap<String, ArrayList<Pair<Integer, Double>>> required) {
        recipe = mealPlanController.scaleRecipe(recipe, mealPlan.getServings());
        for (Ingredient ingredient : recipe.getIngredients()) {
            generateRequiredFromIngredients(ingredient, mealPlan, required);
        }
    }

    public void addIngredientToStorage(ShoppingCartIngredient shoppingCartIngredient,
                                       StorageIngredient storageIngredient) {
        Pair<Double, String> smallestShoppingIngredient =
                unitHelper.convertToSmallest(shoppingCartIngredient.getUnit(),
                        shoppingCartIngredient.getAmount());

        Pair<Double, String> smallestStorageIngredient =
                unitHelper.convertToSmallest(storageIngredient.getUnit(),
                        storageIngredient.getAmount());

        boolean isIngredientNeeded = (smallestShoppingIngredient.first -
                smallestStorageIngredient.first >= 0.0) && smallestShoppingIngredient.second.equals(
                smallestStorageIngredient.second);

        storageIngredientDB.addStorageIngredient(storageIngredient, (added, success) -> {
            if (!success) {
                // todo failure msg
                return;
            }
            // todo success msg
            if (isIngredientNeeded) {
                db.updateIngredient(shoppingCartIngredient.getId(), !isIngredientNeeded, (
                        success1 -> {
                        }
                ));
            } else {
                db.deleteIngredient(shoppingCartIngredient.getId(), (deleteSuccess -> {

                }));
            }

        });
    }
}
