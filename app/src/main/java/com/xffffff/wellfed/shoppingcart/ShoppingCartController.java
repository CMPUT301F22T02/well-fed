package com.xffffff.wellfed.shoppingcart;

import android.app.Activity;
import android.util.Pair;

import com.google.firebase.firestore.Query;
import com.xffffff.wellfed.common.DBConnection;
import com.xffffff.wellfed.common.DateUtil;
import com.xffffff.wellfed.ingredient.Ingredient;
import com.xffffff.wellfed.storage.StorageIngredient;
import com.xffffff.wellfed.storage.StorageIngredientDB;
import com.xffffff.wellfed.mealplan.MealPlan;
import com.xffffff.wellfed.mealplan.MealPlanController;
import com.xffffff.wellfed.mealplan.MealPlanDB;
import com.xffffff.wellfed.recipe.Recipe;
import com.xffffff.wellfed.unit.Unit;
import com.xffffff.wellfed.unit.UnitConverter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ShoppingCartIngredientController is a class that contains methods for
 * interacting with the shopping cart
 */
public class ShoppingCartController {
    /**
     * ShoppingCart adapter. This is used to update the recycler view.
     */
    private ShoppingCartIngredientAdapter adapter;
    /**
     * This is the db of the shopping cart.
     */
    private final ShoppingCartDB db;

    /**
     * This is the db for the storage ingredients.
     */
    private final StorageIngredientDB storageIngredientDB;

    /**
     * This is the db for the meal plans.
     */
    private final MealPlanDB mealPlanDB;
    /**
     * This is the unit converter class for the controller.
     */
    private UnitConverter unitConverter;

    /**
     * This is the constroller for the meal plans.
     */
    private final MealPlanController mealPlanController;

    /**
     * This is the date util class for the controller.
     */
    private final DateUtil dateUtil;

    /**
     * The constructor for the controller.
     *
     * @param activity The activity that the controller is attached to.
     */
    public ShoppingCartController(Activity activity) {
        DBConnection connection = new DBConnection(activity.getApplicationContext());
        db = new ShoppingCartDB(connection);
        adapter = new ShoppingCartIngredientAdapter(db);
        storageIngredientDB = new StorageIngredientDB(connection);
        mealPlanDB = new MealPlanDB(connection);
        unitConverter =
                new UnitConverter(activity.getApplicationContext());
        mealPlanController = new MealPlanController(activity);
        dateUtil = new DateUtil();
    }

    /**
     * getSearchResults returns the search results for the given query.
     *
     * @param field The field to search.
     */
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
                                    bestUnitHelper(cartItem);

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
     * @return the best unit for the given ingredient.
     */
    private Pair<Double, String> bestUnitHelper(Ingredient ingredient) {
        // reduce to smallest
        Pair<Double, String> smallest = unitConverter.convertToSmallest(ingredient.getUnit(), ingredient.getAmount());
        ingredient.setUnit(smallest.second);
        ingredient.setAmount(smallest.first);

        Pair<Double, String> best = unitConverter.availableUnits(ingredient);
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

    public void updateCheckedStatus(String id, boolean isChecked) {
        db.updateIngredient(id, isChecked, (success -> {
            success = false;
        }));
    }

    /**
     * updateRequiredIngredients updates the required ingredients for the
     * given meal plan.
     * @param storedIngredients the ingredients to update.
     * @param required the required ingredients.
     */
    private void updateRequiredFromStorage(ArrayList<StorageIngredient> storedIngredients,
                                           HashMap<String, ArrayList<Pair<Integer, Double>>> required) {
        for (StorageIngredient stored : storedIngredients) {
            Pair<Double, String> val = bestUnitHelper(stored);
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

    /**
     * updateRequiredIngredients updates the required ingredients for the
     * @param ingredient the ingredient to update.
     * @param mealPlan the meal plan to update.
     * @param required the required ingredients.
     */
    private void generateRequiredFromIngredients(Ingredient ingredient, MealPlan mealPlan,
                                                 HashMap<String, ArrayList<Pair<Integer, Double>>> required) {
        String uid = ingredientUid(ingredient);
        if (required.get(uid) != null) {
            Pair<Double, String> val = bestUnitHelper(ingredient);
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
            Pair<Double, String> val = bestUnitHelper(ingredient);
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

    /***
     * generateRequiredFromRecipe generates the required ingredients for the
     * given recipe.
     * @param recipe the recipe to generate the required ingredients for.
     * @param mealPlan the meal plan to generate the required ingredients for.
     * @param required the required ingredients.
     */
    private void generateRequiredFromRecipes(Recipe recipe, MealPlan mealPlan,
                                             HashMap<String, ArrayList<Pair<Integer, Double>>> required) {
        recipe = mealPlanController.scaleRecipe(recipe, mealPlan.getServings());
        for (Ingredient ingredient : recipe.getIngredients()) {
            generateRequiredFromIngredients(ingredient, mealPlan, required);
        }
    }

    /**
     * addIngredientToStorage adds the given ingredient to the storage.
     * @param shoppingCartIngredient the ingredient to add.
     * @param storageIngredient the storage ingredient to add.
     */
    public void addIngredientToStorage(ShoppingCartIngredient shoppingCartIngredient,
                                       StorageIngredient storageIngredient) {
        Pair<Double, String> smallestShoppingIngredient =
                unitConverter.convertToSmallest(shoppingCartIngredient.getUnit(),
                        shoppingCartIngredient.getAmount());

        Pair<Double, String> smallestStorageIngredient =
                unitConverter.convertToSmallest(storageIngredient.getUnit(),
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
                db.deleteIngredient(shoppingCartIngredient.getId(),
                        ((deleteIngredient, deleteSuccess) -> {

                }));
            }

        });
    }
}
