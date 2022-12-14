package com.xffffff.wellfed.recipe;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Transaction;
import com.xffffff.wellfed.common.DBConnection;
import com.xffffff.wellfed.common.OnCompleteListener;
import com.xffffff.wellfed.ingredient.Ingredient;
import com.xffffff.wellfed.ingredient.IngredientDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * RecipeDB is a class that handles all the database operations for Recipe
 * objects.
 */
public class RecipeDB {
    /**
     * Holds an instance of the Firebase FireStore database
     */
    private final FirebaseFirestore db;
    /**
     * Holds a collection to the Recipe Ingredients stored in the FireStore
     * db database
     */
    private final IngredientDB ingredientDB;

    /**
     * Holds the CollectionReference for the users Recipe collection.
     */
    private CollectionReference collection;

    /**
     * Create a RecipeDB object
     *
     * @param connection: the DBConnection object to connect to the database
     */
    public RecipeDB(DBConnection connection) {
        db = connection.getDB();
        collection = connection.getCollection("Recipes");
        ingredientDB = new IngredientDB(connection);
    }

    /**
     * Adds a recipe to the Recipe collection in db and any ingredients
     * not already in Recipe Ingredients. This method will set the Recipe id
     * to the corresponding document id in the collection
     *
     * @param recipe A Recipe object we want to add to the collection of Recipes
     *               and whose id we want to set
     * @param listener: the listener to call when the operation is complete
     */
    public void addRecipe(Recipe recipe, OnCompleteListener<Recipe> listener) {

        ArrayList<HashMap<String, Object>> recipeIngredients =
                new ArrayList<>();
        HashMap<String, Object> recipeMap = new HashMap<>();

        // add all the ingredients and store in recipeIngredients
        AtomicInteger added = new AtomicInteger();
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            Ingredient ingredient = recipe.getIngredients().get(i);
            ingredientDB.getIngredient(ingredient,
                    (foundIngredient, success) -> {
                        if (foundIngredient != null) {
                            DocumentReference doc =
                                    ingredientDB.getDocumentReference(
                                            foundIngredient);
                            HashMap<String, Object> ingredientMap =
                                    new HashMap<>();
                            ingredient.setId(foundIngredient.getId());
                            ingredientMap.put("ingredientRef",
                                    ingredientDB.getDocumentReference(
                                            ingredient));
                            ingredientMap.put("amount", ingredient.getAmount());
                            ingredientMap.put("unit", ingredient.getUnit());
                            added.addAndGet(1);
                            recipeIngredients.add(ingredientMap);
                            if (added.get() == recipe.getIngredients().size()) {
                                addRecipeIncrementCounter(recipeMap, recipeIngredients,
                                        recipe, listener);
                            }

                        } else {
                            ingredientDB.addIngredient(ingredient,
                                    (addedIngredient, success1) -> {
                                        if (addedIngredient == null) {
                                            listener.onComplete(null, false);
                                            return;
                                        }
                                        ingredient.setId(
                                                addedIngredient.getId());
                                        HashMap<String, Object> ingredientMap =
                                                new HashMap<>();
                                        ingredientMap.put("ingredientRef",
                                                ingredientDB.getDocumentReference(
                                                        addedIngredient));
                                        ingredientMap.put("amount",
                                                ingredient.getAmount());
                                        ingredientMap.put("unit",
                                                ingredient.getUnit());
                                        added.addAndGet(1);
                                        recipeIngredients.add(ingredientMap);
                                        if (added.get() ==
                                                recipe.getIngredients()
                                                        .size()) {
                                            addRecipeIncrementCounter(recipeMap,
                                                    recipeIngredients, recipe,
                                                    listener);
                                        }
                                    });
                        }
                    });
        }
    }

    /**
     * addRecipeIncrementCounter is a helper method for addRecipe. It increments the
     * count of ingredients in the database.
     *
     * @param recipeMap A HashMap of the recipe to be added.
     * @param ingredients A list of ingredients to be added.
     * @param recipe The recipe to be added.
     * @param listener The listener to be called when the recipe is added.
     */
    public void addRecipeIncrementCounter(HashMap<String, Object> recipeMap,
                               ArrayList<HashMap<String, Object>> ingredients,
                               Recipe recipe, OnCompleteListener<Recipe> listener) {
        // Update count of each ingredient.
        AtomicInteger counter = new AtomicInteger(0);
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredientDB.updateReferenceCount(ingredient, 1, (i, success) -> {
                counter.addAndGet(1);
                if (counter.get() == recipe.getIngredients().size()) {
                    addRecipeHelper(recipeMap, ingredients, recipe, listener);
                }
            });
        }
    }

    /**
     * Helper function that adds the Recipe document to db
     * once all transactions are finished.
     *
     * @param recipeMap A HashMap of the recipe to be added.
     * @param ingredients A list of ingredients to be added.
     * @param recipe The recipe to be added.
     * @param listener The listener to be called when the recipe is added.
     */
    public void addRecipeHelper(HashMap<String, Object> recipeMap,
                                ArrayList<HashMap<String, Object>> ingredients,
                                Recipe recipe, OnCompleteListener<Recipe> listener) {
        recipeMap.put("ingredients", ingredients);
        recipeMap.put("title", recipe.getTitle());
        recipeMap.put("servings", recipe.getServings());
        recipeMap.put("category", recipe.getCategory());
        recipeMap.put("comments", recipe.getComments());
        recipeMap.put("photograph", recipe.getPhotograph());
        recipeMap.put("preparation-time", recipe.getPrepTimeMinutes());
        recipeMap.put("count", 0);
        recipeMap.put("search-field", recipe.getTitle().toLowerCase());

        this.collection.add(recipeMap).addOnSuccessListener(addedSnapshot -> {
            recipe.setId(addedSnapshot.getId());
            listener.onComplete(recipe, true);
        }).addOnFailureListener(failure -> {
            listener.onComplete(null, false);
        });
    }

    /**
     * Gets the Recipe object with the given id from the db.
     *
     * @param id The id of the Recipe to be retrieved.
     * @param listener The listener to be called when the recipe is retrieved.
     */
    public void getRecipe(String id, OnCompleteListener<Recipe> listener) {
        DocumentReference recipeRef = this.collection.document(id);
        recipeRef.get().addOnSuccessListener(doc -> {
            Recipe recipe = new Recipe(doc.getString("title"));
            recipe.setId(doc.getId());
            recipe.setCategory(doc.getString("category"));
            recipe.setComments(doc.getString("comments"));
            recipe.setPhotograph(doc.getString("photograph"));
            recipe.setPrepTimeMinutes(doc.getLong("preparation-time"));
            recipe.setServings(doc.getLong("servings"));
            ArrayList<HashMap<String, Object>> ingredients =
                    (ArrayList<HashMap<String, Object>>) doc.getData()
                            .get("ingredients");
            int toFetch = ingredients.size();
            AtomicInteger fetched = new AtomicInteger();
            for (int i = 0; i < toFetch; i++) {
                DocumentReference ingredientRef =
                        (DocumentReference) ingredients.get(i)
                                .get("ingredientRef");
                int finalI = i;
                ingredientDB.getIngredient(ingredientRef.getId(),
                        (foundIngredient, success) -> {
                            foundIngredient.setAmount(
                                    (Double) ingredients.get(finalI)
                                            .get("amount"));
                            foundIngredient.setUnit(
                                    (String) ingredients.get(finalI)
                                            .get("unit"));
                            recipe.addIngredient(foundIngredient);
                            fetched.addAndGet(1);
                            if (fetched.get() == toFetch) {
                                listener.onComplete(recipe, true);
                            }
                        });
            }
        }).addOnFailureListener(failure -> {
            listener.onComplete(null, false);
        });
    }

    /**
     * Updates the Recipe object in the DB
     *
     * @param recipe   the Recipe object to the updated
     * @param listener the OnUpdateRecipeListener object to handle the result
     */
    public void updateRecipe(Recipe recipe, OnCompleteListener<Recipe> listener) {
        ArrayList<HashMap<String, Object>> recipeIngredients =
                new ArrayList<>();

        // set counter & get # of Ingredients in Recipe
        AtomicInteger counter = new AtomicInteger(0);
        Integer numOfIngredients = recipe.getIngredients().size();

        // Iterate over each ingredient in the Recipe object and
        // increment counter for each ingredient.
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredientDB.getIngredient(ingredient,
                    (foundIngredient, success) -> {
                        // If the ingredient already exists in Ingredient
                        // collection.
                        if (foundIngredient != null) {
                            ingredient.setId(foundIngredient.getId());

                            // Initializes HashMap for mapping.
                            HashMap<String, Object> ingredientMap =
                                    new HashMap<>();

                            // Sets fields.
                            ingredientMap.put("ingredientRef",
                                    ingredientDB.getDocumentReference(
                                            foundIngredient));
                            ingredientMap.put("amount", ingredient.getAmount());
                            ingredientMap.put("unit", ingredient.getUnit());

                            // Increments counter & adds HashMap to ArrayList
                            counter.addAndGet(1);
                            recipeIngredients.add(ingredientMap);

                            // Check if the counter has reached the number of
                            // ingredients and recipes.
                            if (counter.get() == numOfIngredients) {
                                updateRecipeFindOldRecipe(recipe, recipeIngredients,
                                        listener);
                            }
                        } else {
                            // If the ingredient does not exist in our Ingredient collection,
                            // we will create a new ingredient for it.
                            ingredientDB.addIngredient(ingredient,
                                    (addedIngredient, success1) -> {
                                        // Checks if adding a new Ingredient
                                        // was successful.
                                        if (addedIngredient == null) {
                                            listener.onComplete(null,
                                                    false);
                                            return;
                                        }
                                        ingredient.setId(
                                                addedIngredient.getId());

                                        // Initializes HashMap for mapping.
                                        HashMap<String, Object> ingredientMap =
                                                new HashMap<>();
                                        ingredientMap.put("ingredientRef",
                                                ingredientDB.getDocumentReference(
                                                        addedIngredient));
                                        ingredientMap.put("amount",
                                                ingredient.getAmount());
                                        ingredientMap.put("unit",
                                                ingredient.getUnit());

                                        counter.addAndGet(1);
                                        recipeIngredients.add(ingredientMap);

                                        if (counter.get() == numOfIngredients) {
                                            updateRecipeFindOldRecipe(recipe,
                                                    recipeIngredients,
                                                    listener);
                                        }
                                    });
                        }
                    });
        }
    }

    /**
     * A helper method for updateRecipe() that finds the old recipe in the db.
     *
     * @param recipe The new recipe to be updated.
     * @param recipeIngredients The ingredients of the new recipe.
     * @param listener The listener to be called when the recipe is updated.
     */
    public void updateRecipeFindOldRecipe(Recipe recipe,
                                  ArrayList<HashMap<String, Object>> recipeIngredients,
                                  OnCompleteListener<Recipe> listener) {
        getRecipe(recipe.getId(), (foundRecipe, success) -> {
            if (foundRecipe == null) {
                listener.onComplete(null, false);
                return;
            } else {
                updateRecipeIncrementCounter(recipe, foundRecipe, recipeIngredients,
                        listener);
            }
        });
    }

    /**
     * Helper method that increments counter for each ingredient in the
     * updated Recipe.
     *
     * @param recipe The new recipe to be updated.
     * @param foundRecipe The old recipe to be updated.
     * @param recipeIngredients The ingredients of the new recipe.
     * @param listener The listener to be called when the recipe is updated.
     */
    public void updateRecipeIncrementCounter(Recipe recipe, Recipe foundRecipe,
                                   ArrayList<HashMap<String, Object>> recipeIngredients,
                                   OnCompleteListener<Recipe> listener) {
        AtomicInteger counter = new AtomicInteger(0);
        Integer numOfIngredients = recipe.getIngredients().size();

        // Increment count for each ingredient in the updated Recipe object.
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredientDB.updateReferenceCount(ingredient, 1, (i, s) -> {
                counter.addAndGet(1);
                if (counter.get() == numOfIngredients) {
                    updateRecipeDecrementCounter(recipe, foundRecipe, recipeIngredients,
                            listener);
                }
            });
        }
    }

    /**
     * Helper method that decrements counter for each ingredient in the
     * old Recipe.
     *
     * @param recipe The new recipe to be updated.
     * @param foundRecipe The old recipe to be updated.
     * @param recipeIngredients The ingredients of the new recipe.
     * @param listener The listener to be called when the recipe is updated.
     */
    public void updateRecipeDecrementCounter(Recipe recipe, Recipe foundRecipe,
                                   ArrayList<HashMap<String, Object>> recipeIngredients,
                                   OnCompleteListener<Recipe> listener) {
        AtomicInteger counter = new AtomicInteger(0);
        Integer numOfIngredients = foundRecipe.getIngredients().size();

        // Decrement counter for each ingredient in the old Recipe object.
        for (Ingredient ingredient : foundRecipe.getIngredients()) {
            ingredientDB.updateReferenceCount(ingredient, -1, (i, s) -> {
                counter.addAndGet(1);
                if (counter.get() == numOfIngredients) {
                    updateRecipeHelper(recipe, recipeIngredients, listener);
                }
            });
        }
    }

    /**
     * Given the Recipe object, updates it in the db.
     *
     * @param recipe            the Recipe object to be updated
     * @param recipeIngredients the ArrayList of HashMaps of the ingredients
     * @param listener          the OnUpdateRecipeListener object to handle
     *                          the result
     */
    public void updateRecipeHelper(Recipe recipe,
                                   ArrayList<HashMap<String, Object>> recipeIngredients,
                                   OnCompleteListener<Recipe> listener) {
        DocumentReference recipeRef = this.collection.document(recipe.getId());

        recipeRef.update("title", recipe.getTitle(), "category",
                        recipe.getCategory(), "comments", recipe.getComments(),
                        "photograph", recipe.getPhotograph(), "preparation" +
                                "-time",
                        recipe.getPrepTimeMinutes(), "servings",
                        recipe.getServings(),
                        "ingredients", recipeIngredients,
                        "search-field", recipe.getTitle().toLowerCase())
                .addOnSuccessListener(success -> {
                    listener.onComplete(recipe, true);
                }).addOnFailureListener(failure -> {
                    listener.onComplete(null, false);
                });
    }

    /**
     * Deletes a recipe document with the id from the collection
     * of recipes
     *
     * @param recipe The recipe to delete.
     * @param listener The listener to be called when the recipe is deleted.
     */
    public void delRecipe(Recipe recipe, OnCompleteListener<Recipe> listener) {
        DocumentReference recipeRef = this.collection.document(recipe.getId());

        // Check if the Recipe exists in the Recipe collection.
        recipeRef.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                listener.onComplete(recipe, false);
            }
            DocumentSnapshot document = task.getResult();
            if (document.exists()) {
                Long count = document.getLong("count");
                if (count != null && count > 0) {
                    // Do not allow user to delete recipe if count is > 0.
                    listener.onComplete(null, null);
                } else {
                    delRecipeDecrementCounter(recipe, recipeRef, document, listener);
                }
            }
        });
    }

    /**
     * Helper function for decrementing counter of every ingredient in the
     * deleted Recipe object.
     *
     * @param recipe The recipe to delete.
     * @param recipeRef The reference to the recipe to delete.
     * @param recipeDoc The document of the recipe to delete.
     * @param listener The listener to be called when the recipe is deleted.
     */
    public void delRecipeDecrementCounter(Recipe recipe, DocumentReference recipeRef,
                               DocumentSnapshot recipeDoc,
                               OnCompleteListener<Recipe> listener) {
        AtomicInteger counter = new AtomicInteger(0);
        // Get ingredients from recipe document.
        ArrayList<HashMap<String, Object>> ingredientMap =
                (ArrayList<HashMap<String, Object>>) recipeDoc.get("ingredients");
        int numOfIngredients = ingredientMap.size();

        // Decrement count for each ingredient in the Recipe object.
        for (int j = 0; j < numOfIngredients; j++) {
            DocumentReference ingredientRef =
                    (DocumentReference) ingredientMap.get(j).get(
                            "ingredientRef");
            Ingredient ingredient = new Ingredient();
            ingredient.setId(ingredientRef.getId());
            ingredientDB.updateReferenceCount(ingredient, -1, (i, s) -> {
                counter.addAndGet(1);
                if (counter.get() == numOfIngredients) {
                    delRecipeHelper(recipe, recipeRef, listener);
                }
            });
        }
    }

    /**
     * Helper function for deleting the recipe document.
     *
     * @param recipe The recipe to delete.
     * @param recipeRef The reference to the recipe document.
     * @param listener The listener to be called when the recipe is deleted.
     */
    public void delRecipeHelper(Recipe recipe, DocumentReference recipeRef,
                                OnCompleteListener<Recipe> listener) {
        recipeRef.delete().addOnSuccessListener(r -> {
            listener.onComplete(recipe, true);
        }).addOnFailureListener(f -> {
            listener.onComplete(null, false);
        });
    }

    /**
     * Get the DocumentReference from Recipes collection for the given id
     *
     * @param id The String of the document in Recipes collection we want
     * @return DocumentReference of the Recipe
     */
    public DocumentReference getDocumentReference(String id) {
        return this.collection.document(id);
    }

    /**
     * Gets a query for Recipes in the db.
     *
     * @return the query for Recipes in the db.
     */
    public Query getQuery() {
        return this.collection;
    }

    /**
     * Gets query from the db with Recipes sorted by given field.
     *
     * @param field The field to sort by.
     * @return the query for Recipes in the db.
     */
    public Query getSortQuery(String field) {
        return this.collection.orderBy(field);
    }

    /**
     * Updates the counter for a Recipe object in the db.
     *
     * @param recipe The Recipe object to update.
     * @param delta The amount to increment the counter by.
     * @param listener The listener to be called when the counter is updated.
     */
    public void updateReferenceCount(Recipe recipe, int delta,
                                     OnCompleteListener<Recipe> listener) {
        final DocumentReference recipeRef =
                this.collection.document(recipe.getId());

        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction)
                    throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(recipeRef);

                Long newCount = snapshot.getLong("count") + delta;
                transaction.update(recipeRef, "count", newCount);

                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                listener.onComplete(recipe, true);
                Log.d(TAG, "Transaction success!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onComplete(recipe, false);
                Log.w(TAG, "Transaction failure.", e);
            }
        });
    }

    /**
     * Query for the search functionality.
     *
     * @param field The keyword to search for.
     * @return Results matching the keyword.
     */
    public Query getSearchQuery(String field) {
        return this.collection.orderBy("search-field")
                .startAt(field.toLowerCase()).endAt(field.toLowerCase() + '~');
    }
}