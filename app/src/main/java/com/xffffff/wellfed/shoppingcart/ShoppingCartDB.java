package com.xffffff.wellfed.shoppingcart;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.xffffff.wellfed.common.DBConnection;
import com.xffffff.wellfed.common.OnCompleteListener;
import com.xffffff.wellfed.ingredient.Ingredient;
import com.xffffff.wellfed.ingredient.IngredientDB;
import com.xffffff.wellfed.storage.StorageIngredient;
import com.xffffff.wellfed.storage.StorageIngredientDB;
import com.xffffff.wellfed.mealplan.MealPlan;
import com.xffffff.wellfed.mealplan.MealPlanDB;
import com.xffffff.wellfed.recipe.Recipe;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is the database for the shopping cart list
 * <p>
 * It is used to store the ingredients in the shopping cart list
 * and to retrieve them
 * It also contains methods to add and remove ingredients from the list
 * </p>
 */
public class ShoppingCartDB {

    /**
     * Holds the collection for the users
     */
    private final CollectionReference collection;
    /**
     * Holds a reference to the IngredientDB.
     */
    private IngredientDB ingredientDB;

    /**
     * Constructor for the ShoppingCartDB.
     *
     * @param connection The DBConnection to use.
     */
    public ShoppingCartDB(DBConnection connection) {
        this.ingredientDB = new IngredientDB(connection);
        this.collection = connection.getCollection("ShoppingCart");
    }

    /**
     * Add an ingredient to the shopping cart.
     *
     * @param ingredient The ingredient to add to the shopping cart.
     * @param listener   The listener to call when the ingredient is added.
     */
    public void addIngredient(Ingredient ingredient,
                              OnCompleteListener<ShoppingCartIngredient> listener) {
        if (ingredient == null) {
            listener.onComplete(null, false);
            return;
        }
        ingredientDB.getIngredient(ingredient, (foundIngredient, success1) -> {
            if (foundIngredient == null) {
                ingredientDB.addIngredient(ingredient,
                    (addedIngredient, success) -> {
                        if (!success) {
                            listener.onComplete(null, false);
                        }
                        ingredient.setId(addedIngredient.getId());
                        addShoppingHelper(ingredient, listener);
                    });
            } else {
                ingredient.setId(foundIngredient.getId());
                addShoppingHelper(ingredient, listener);
            }
        });


    }

    /**
     * addShoppingHelper is a helper method for addIngredient.
     *
     * @param ingredient The ingredient to add to the shopping cart.
     * @param listener   The listener to call when the ingredient is added.
     */
    public void addShoppingHelper(Ingredient ingredient,
                                  OnCompleteListener<ShoppingCartIngredient> listener) {
        ShoppingCartIngredient shoppingCartIngredient =
            new ShoppingCartIngredient(ingredient.getDescription());
        shoppingCartIngredient.setCategory(ingredient.getCategory());
        shoppingCartIngredient.setAmount(ingredient.getAmount());
        shoppingCartIngredient.setUnit(ingredient.getUnit());

        shoppingCartIngredient.setComplete(false);
        shoppingCartIngredient.setPickedUp(false);

        HashMap<String, Object> storageIngredientMap = new HashMap<>();
        storageIngredientMap.put("id", ingredient.getId());
        storageIngredientMap.put("description", ingredient.getDescription());
        storageIngredientMap.put("amount", ingredient.getAmount());
        storageIngredientMap.put("unit", ingredient.getUnit());
        storageIngredientMap.put("category", ingredient.getCategory());
        storageIngredientMap.put("picked", false);
        storageIngredientMap.put("complete", false);
        storageIngredientMap.put("Ingredient",
            ingredientDB.getDocumentReference(ingredient));
        storageIngredientMap.put("search-field", ingredient.getDescription().toLowerCase());

        collection.add(storageIngredientMap).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                listener.onComplete(null, false);
            }
            listener.onComplete(shoppingCartIngredient, true);
        });
    }

    /**
     * Update an ingredient in the shopping cart.
     *
     * @param ingredient The ingredient to update in the shopping cart.
     * @param listener  The listener to call when the ingredient is updated.
     */
    public void updateIngredient(ShoppingCartIngredient ingredient,
                                 OnCompleteListener<ShoppingCartIngredient> listener) {
        if (ingredient == null) {
            listener.onComplete(null, false);
            return;
        }
        if (ingredient.getId() == null) {
            listener.onComplete(null, false);
            return;
        }

        DocumentReference doc = collection.document(ingredient.getId());
        doc.update("id", ingredient.getId(), "description",
                ingredient.getDescription(), "amount",
                ingredient.getAmount(),
                "unit", ingredient.getUnit(), "category",
                ingredient.getCategory(), "Ingredient",
                ingredientDB.getDocumentReference(ingredient), "picked",
                ingredient.isPickedUp, "complete",
                ingredient.isComplete,
                "search-field", ingredient.getDescription().toLowerCase())
            .addOnSuccessListener(success -> {
                listener.onComplete(ingredient, true);
            }).addOnFailureListener(failure -> {
                listener.onComplete(null, false);
            });

    }


    /**
     * Update an ingredient in the shopping cart.
     *
     * @param id The id of the ingredient to update in the shopping cart.
     * @param isPickedUp The boolean to set the ingredient to.
     * @param listener  The listener to call when the ingredient is updated.
     */
    public void updateIngredient(String id, boolean isPickedUp,
                                 OnPickedUpChange listener) {
        if (id == null) {
            listener.onPickedUpChange(false);
            return;
        }

        DocumentReference doc = collection.document(id);
        doc.update("picked",
                        isPickedUp
                )
                .addOnSuccessListener(success -> {
                    listener.onPickedUpChange(true);
                }).addOnFailureListener(failure -> {
                    listener.onPickedUpChange(false);
                });

    }

    /**
     * onPickedUpChange is an interface for the updateIngredient method.
     */
    public interface OnPickedUpChange {
        void onPickedUpChange(boolean success);
    }

    /**
     * Get the shopping cart.
     *
     * @param listener The listener to call when the shopping cart is retrieved.
     */
    public void getShoppingCart(OnCompleteListener<ArrayList<ShoppingCartIngredient>> listener) {
        collection.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                listener.onComplete(null, false);
                return;
            }
            ArrayList<ShoppingCartIngredient> shoppingCart = new ArrayList<>();
            for (QueryDocumentSnapshot document : task.getResult()) {
                ShoppingCartIngredient ingredient = new ShoppingCartIngredient(
                    document.getString("description"));
                ingredient.setId(document.getId());
                ingredient.setAmount(document.getDouble("amount"));
                ingredient.setUnit(document.getString("unit"));
                ingredient.setCategory(document.getString("category"));
                ingredient.setPickedUp(
                    Boolean.TRUE.equals(document.getBoolean("picked")));
                ingredient.setComplete(
                    Boolean.TRUE.equals(document.getBoolean("complete")));
                shoppingCart.add(ingredient);
            }
            listener.onComplete(shoppingCart, true);
        });
    }

    /**
     * Delete an ingredient from the shopping cart.
     *
     * @param ingredient The ingredient to delete from the shopping cart.
     * @param listener The listener to call when the ingredient is deleted.
     */
    public void deleteIngredient(ShoppingCartIngredient ingredient,
                                 OnCompleteListener<ShoppingCartIngredient> listener) {
        if (ingredient == null || ingredient.getId() == null) {
            listener.onComplete(null, false);
            return;
        }

        collection.document(ingredient.getId()).delete().addOnCompleteListener(
            task -> listener.onComplete(ingredient,
                task.isSuccessful()));
    }

    /**
     * Delete all ingredients from the shopping cart.
     * @param id The id of the ingredient to delete from the shopping cart.
     * @param listener The listener to call when the ingredient is deleted.
     */
    public void deleteIngredient(String id, OnCompleteListener<ShoppingCartIngredient> listener) {
        if (id == null) {
            listener.onComplete(null, false);
            return;
        }

        collection.document(id).delete().addOnCompleteListener(
                task -> listener.onComplete(null, task.isSuccessful()));
    }

    /**
     * Get a query of the shopping cart.
     *
     * @return The query of the shopping cart.
     */
    public Query getQuery() {
        return collection.orderBy("description", Query.Direction.ASCENDING);
    }

    /**
     * Sort the shopping cart by the given field.
     *
     * @param field The category to sort by.
     * @return The query of the shopping cart.
     */
    public Query getSortedQuery(String field) {
        return collection.orderBy(field);
    }

    /**
     * Search the shopping cart by the given query string.
     *
     * @param field The query string.
     * @return The query of the shopping cart.
     */
    public Query getSearchQuery(String field) {
        return this.collection.orderBy("search-field")
            .startAt(field.toLowerCase()).endAt(field.toLowerCase() + '~');
    }

    /**
     * snapshot to shopping cart ingredient converter
     * @param doc snapshot
     * @return shopping cart ingredient
     */
    public ShoppingCartIngredient snapshotToShoppingCartIngredient(DocumentSnapshot doc) {
        String description = doc.getString("description");
        String category = doc.getString("category");
        String unit = doc.getString("unit");
        String id = doc.getId();
        boolean isPickedUp = (Boolean) doc.getData().get("picked");
        Double amount = ((Double) doc.getData().get("amount"));

        ShoppingCartIngredient shoppingCartIngredient = new ShoppingCartIngredient(description);
        shoppingCartIngredient.setId(id);
        shoppingCartIngredient.setCategory(category);
        shoppingCartIngredient.setUnit(unit);
        shoppingCartIngredient.setPickedUp(isPickedUp);
        shoppingCartIngredient.setAmount(amount);

        return shoppingCartIngredient;
    }

}
