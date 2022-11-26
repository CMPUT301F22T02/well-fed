package com.xffffff.wellfed.shoppingcart;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.xffffff.wellfed.common.DBConnection;
import com.xffffff.wellfed.ingredient.Ingredient;
import com.xffffff.wellfed.ingredient.IngredientDB;
import com.xffffff.wellfed.ingredient.StorageIngredient;
import com.xffffff.wellfed.ingredient.StorageIngredientDB;
import com.xffffff.wellfed.mealplan.MealPlan;
import com.xffffff.wellfed.mealplan.MealPlanDB;
import com.xffffff.wellfed.recipe.Recipe;
import com.xffffff.wellfed.recipe.RecipeDB;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;

// TODO: create DB connection between shopping cart and Firestore.
public class ShoppingCartDB {
    /**
     * Holds the tag for the logging purposes.
     */
    private static final String TAG = "ShoppingCartDB";
    /**
     * Holds a reference to StorageIngredientDB.
     */
    private final StorageIngredientDB storageIngredientDB;
    /**
     * Holds a reference to the recipeDB.
     */
    private final RecipeDB recipeDB;
    /**
     * Holds a reference to MealPlanDB.
     */
    private final MealPlanDB mealPlanDB;
    /**
     * Holds the collection for the users
     */
    private final CollectionReference collection;
    /**
     * Holds the instance of the Firebase Firestore database.
     */
    private FirebaseFirestore db;
    /**
     * Holds a reference to the IngredientDB.
     */
    private IngredientDB ingredientDB;

    /**
     * Constructor for the ShoppingCartDB.
     */
    public ShoppingCartDB(DBConnection connection) {
        this.ingredientDB = new IngredientDB(connection);
        this.storageIngredientDB = new StorageIngredientDB(connection);
        this.recipeDB = new RecipeDB(connection);
        this.mealPlanDB = new MealPlanDB(connection);
        this.db = connection.getDB();
        this.collection = connection.getCollection("ShoppingCart");
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
        ingredientDB.getIngredient(ingredient, (foundIngredient, success1) -> {
            if (foundIngredient == null) {
                ingredientDB.addIngredient(ingredient,
                        (addedIngredient, success) -> {
                            if (!success) {
                                listener.onAddShoppingCart(null, false);
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


    public void addShoppingHelper(Ingredient ingredient,
                                  OnAddShoppingCart listener) {
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
                listener.onAddShoppingCart(null, false);
            }
            listener.onAddShoppingCart(shoppingCartIngredient, true);
        });
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
                    listener.onAddShoppingCart(ingredient, true);
                }).addOnFailureListener(failure -> {
                    listener.onAddShoppingCart(null, false);
                });

    }


    /**
     * Update an ingredient in the shopping cart.
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

    public interface OnPickedUpChange {
        public void onPickedUpChange(boolean success);
    }

    /**
     * Get the shopping cart.
     */
    public void getShoppingCart(OnGetShoppingCart listener) {
        collection.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                listener.onGetShoppingCart(null, false);
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
            listener.onGetShoppingCart(shoppingCart, true);
        });
    }

    /**
     * Delete an ingredient from the shopping cart.
     */
    public void deleteIngredient(ShoppingCartIngredient ingredient,
                                 OnRemoveShoppingCart listener) {
        if (ingredient == null || ingredient.getId() == null) {
            listener.onRemoveShoppingCart(null, false);
            return;
        }

        collection.document(ingredient.getId()).delete().addOnCompleteListener(
                task -> listener.onRemoveShoppingCart(ingredient,
                        task.isSuccessful()));
    }

    public void deleteIngredient(String id, OnDelete listener) {
        if (id == null) {
            listener.onDelete(false);
            return;
        }

        collection.document(id).delete().addOnCompleteListener(
                task -> listener.onDelete(task.isSuccessful()));
    }

    ;

    public interface OnDelete {
        public void onDelete(boolean succeess);
    }

    /**
     * Update the shopping cart with the ingredients from the meal plan if
     * not present in the StorageIngredientDB.
     */
    public void updateShoppingCart(OnUpdateShoppingCart listener) {
        mealPlanDB.getMealPlans((mealPlan, success) -> {
            if (success) {
                storageIngredientDB.getAllStorageIngredients(
                        (storageIngredients, success2) -> {
                            if (success2) {
                                ShoppingCart shoppingCart = new ShoppingCart();
                                for (MealPlan meal : mealPlan) {
                                    for (Ingredient ingredient :
                                            meal.getIngredients()) {
                                        // Check if the ingredient is in the
                                        // storage
                                        // using getDescription
                                        boolean inStorage = false;
                                        for (StorageIngredient storageIngredient : storageIngredients) {
                                            if (storageIngredient.getDescription()
                                                    .equals(ingredient.getDescription())) {
                                                inStorage = true;
                                                break;
                                            }
                                        }
                                        if (!inStorage) {
                                            shoppingCart.addIngredient(
                                                    new ShoppingCartIngredient(
                                                            ingredient));
                                        }
                                    }

                                    for (Recipe recipe : meal.getRecipes()) {
                                        for (Ingredient ingredient :
                                                recipe.getIngredients()) {
                                            // Check if the ingredient is in
                                            // the storage
                                            // using getDescription
                                            boolean inStorage = false;
                                            for (StorageIngredient storageIngredient : storageIngredients) {
                                                if (storageIngredient.getDescription()
                                                        .equals(ingredient.getDescription())) {
                                                    inStorage = true;
                                                    break;
                                                }
                                            }
                                            if (!inStorage) {
                                                shoppingCart.addIngredient(
                                                        new ShoppingCartIngredient(
                                                                ingredient));
                                            }
                                        }
                                    }
                                }
                                updateShoppingCart(listener);
                            } else {
                                listener.onUpdateShoppingCart(null, false);
                            }
                        });
            }
        });
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

    public Query getQueryByPickedUp(boolean pickedUp) {
        return collection.whereEqualTo("picked", pickedUp);
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
        void onAddShoppingCart(ShoppingCartIngredient ingredient,
                               boolean success);
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
        void onRemoveShoppingCart(ShoppingCartIngredient ingredient,
                                  boolean success);
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
        void onGetShoppingCart(ArrayList<ShoppingCartIngredient> shoppingCart,
                               boolean success);
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
        void onUpdateShoppingCart(ShoppingCart shoppingCart, boolean success);

    }

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
