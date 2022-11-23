package com.xffffff.wellfed.recipe;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.xffffff.wellfed.common.DBConnection;
import com.xffffff.wellfed.ingredient.Ingredient;
import com.xffffff.wellfed.ingredient.IngredientDB;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RecipeDB {
    /**
     * Holds an instance of the Firebase FireStore database
     */
    private final FirebaseFirestore db;
    /**
     * Holds a collection to the Recipe Ingredients stored in the FireStore db database
     */
    private final IngredientDB ingredientDB;
    /**
     * Holds a connection to the DB.
     */
    private DBConnection recipesConnection;

    /**
     * Holds the CollectionReference for the users Recipe collection.
     */
    private CollectionReference collection;

    public interface OnRecipeDone {
        public void onAddRecipe(Recipe recipe, Boolean success);
    }

    public interface OnRecipeIngredientAdded {
        public void onRecipeIngredientAdd(Ingredient ingredient, int totalAdded);
    }

    public interface OnUpdateRecipeReferenceCountListener {
        void onUpdateReferenceCount(Recipe recipe, Boolean success);
    }

    public interface OnUpdateRecipeListener {
        void onUpdateRecipe(Recipe recipe, Boolean success);
    }

    /**
     * Create a RecipeDB object
     */
    public RecipeDB(DBConnection connection) {
        this.recipesConnection = connection;
        db = this.recipesConnection.getDB();
        collection = this.recipesConnection.getCollection("Recipes");
        ingredientDB = new IngredientDB(connection);
    }


    /**
     * Adds a recipe to the Recipe collection in db and any ingredients
     * not already in Recipe Ingredients. This method will set the Recipe id
     * to the corresponding document id in the collection
     *
     * @param recipe A Recipe object we want to add to the collection of Recipes
     *               and whose id we want to set
     * @return Returns the id of the Recipe document
     */
    //todo it works but hacky
    public void addRecipe(Recipe recipe, OnRecipeDone listener){

        ArrayList<HashMap<String, Object>> recipeIngredients = new ArrayList<>();
        HashMap<String, Object> recipeMap = new HashMap<>();

        // add all the ingredients and store in recipeIngredients
        AtomicInteger added = new AtomicInteger();
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            Ingredient ingredient = recipe.getIngredients().get(i);
            ingredientDB.getIngredient(ingredient, (foundIngredient, success) -> {
                if (foundIngredient != null) {
                    DocumentReference doc = ingredientDB.getDocumentReference(foundIngredient);
                    Task<DocumentSnapshot> task = doc.get();
                    HashMap<String, Object> ingredientMap = new HashMap<>();
                    ingredient.setId(foundIngredient.getId());
                    ingredientMap.put("ingredientRef", ingredientDB.getDocumentReference(ingredient));
                    ingredientMap.put("amount", ingredient.getAmount());
                    ingredientMap.put("unit", ingredient.getUnit());
                    added.addAndGet(1);
                    recipeIngredients.add(ingredientMap);
                    if (added.get() == recipe.getIngredients().size()) {
                        addRecipeAsync(recipeMap, recipeIngredients, recipe, listener);
                    }

                } else {
                    ingredientDB.addIngredient(ingredient, (addedIngredient, success1) -> {
                        if (addedIngredient == null) {
                            listener.onAddRecipe(null, false);
                            return;
                        }
                        ingredient.setId(addedIngredient.getId());
                        HashMap<String, Object> ingredientMap = new HashMap<>();
                        ingredientMap.put("ingredientRef", ingredientDB.getDocumentReference(addedIngredient));
                        ingredientMap.put("amount", ingredient.getAmount());
                        ingredientMap.put("unit", ingredient.getUnit());
                        added.addAndGet(1);
                        recipeIngredients.add(ingredientMap);
                        if (added.get() == recipe.getIngredients().size()) {
                            addRecipeAsync(recipeMap, recipeIngredients, recipe, listener);
                        }
                    });
                }
            });
        }
    }

    public void addRecipeAsync(HashMap<String, Object> recipeMap, ArrayList<HashMap<String, Object>> ingredients,
                               Recipe recipe, OnRecipeDone listener) {
        // Update count of each ingredient.
        AtomicInteger counter = new AtomicInteger(0);
        for (Ingredient ingredient: recipe.getIngredients()) {
            ingredientDB.updateReferenceCount(ingredient, 1, (i, success) -> {
                counter.addAndGet(1);
                if (counter.get() == recipe.getIngredients().size()) {
                    addRecipeHelper(recipeMap, ingredients, recipe, listener);
                }
            });
        }
    }

    public void addRecipeHelper(HashMap<String, Object> recipeMap, ArrayList<HashMap<String, Object>> ingredients,
                                Recipe recipe, OnRecipeDone listener) {
        recipeMap.put("ingredients", ingredients);
        recipeMap.put("title", recipe.getTitle());
        recipeMap.put("servings", recipe.getServings());
        recipeMap.put("category", recipe.getCategory());
        recipeMap.put("comments", recipe.getComments());
        recipeMap.put("photograph", recipe.getPhotograph());
        recipeMap.put("preparation-time", recipe.getPrepTimeMinutes());
        recipeMap.put("count", 0);

        this.collection
            .add(recipeMap)
            .addOnSuccessListener(addedSnapshot -> {
                recipe.setId(addedSnapshot.getId());
                listener.onAddRecipe(recipe, true);
            })
            .addOnFailureListener(failure -> {
                listener.onAddRecipe(null, false);
            });
    }

    // todo call the listener when results fail
    // todo add db-tests for it
    public void getRecipe(String id, OnRecipeDone listener) {
        DocumentReference recipeRef = this.collection.document(id);
        recipeRef.get()
                .addOnSuccessListener(doc -> {
                    List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
                    Recipe recipe = new Recipe(doc.getString("title"));
                    recipe.setId(doc.getId());
                    recipe.setCategory(doc.getString("category"));
                    recipe.setComments(doc.getString("comments"));
                    recipe.setPhotograph(doc.getString("photograph"));
                    Long prepTime = (Long) doc.getData().get("preparation-time");
                    recipe.setPrepTimeMinutes(Integer.parseInt(Long.toString(prepTime)));
                    Long servings = (Long) doc.getData().get("servings");
                    recipe.setServings(Integer.parseInt(Long.toString(servings)));
                    ArrayList<HashMap<String,Object>> ingredients = (ArrayList<HashMap<String, Object>>)
                            doc.getData().get("ingredients");
                    int toFetch = ingredients.size();
                    AtomicInteger fetched = new AtomicInteger();
                    for (int i = 0; i<toFetch; i++){
                        DocumentReference ingredientRef = (DocumentReference) ingredients.get(i).get("ingredientRef");
                        int finalI = i;
                        ingredientDB.getIngredient(ingredientRef.getId(), (foundIngredient, success)->{
                            foundIngredient.setAmount((Double) ingredients.get(finalI).get("amount"));
                            foundIngredient.setUnit((String) ingredients.get(finalI).get("unit"));
                            recipe.addIngredient(foundIngredient);
                            fetched.addAndGet(1);
                            if(fetched.get() == toFetch){
                                listener.onAddRecipe(recipe, true);
                            }
                        });
                    }
                })
                .addOnFailureListener(failure -> {
                    listener.onAddRecipe(null, false);
                });
    }

    /**
     * Updates the Recipe object in the DB
     *
     * @param recipe the Recipe object to the updated
     * @param listener the OnUpdateRecipeListener object to handle the result
     */
    public void updateRecipe(Recipe recipe, OnUpdateRecipeListener listener) {
        ArrayList<HashMap<String, Object>> recipeIngredients
                = new ArrayList<>();

        // set counter & get # of Ingredients in Recipe
        AtomicInteger counter = new AtomicInteger(0);
        Integer numOfIngredients = recipe.getIngredients().size();

        // Iterate over each ingredient in the Recipe object and
        // increment counter for each ingredient.
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredientDB.getIngredient(ingredient, (foundIngredient, success) -> {
                // If the ingredient already exists in Ingredient collection.
                if (foundIngredient != null) {
                    ingredient.setId(foundIngredient.getId());

                    // Initializes HashMap for mapping.
                    HashMap<String, Object> ingredientMap = new HashMap<>();

                    // Sets fields.
                    ingredientMap.put("ingredientRef",
                            ingredientDB.getDocumentReference(foundIngredient));
                    ingredientMap.put("amount", ingredient.getAmount());
                    ingredientMap.put("unit", ingredient.getUnit());

                    // Increments counter & adds HashMap to ArrayList
                    counter.addAndGet(1);
                    recipeIngredients.add(ingredientMap);

                    // Check if the counter has reached the number of
                    // ingredients and recipes.
                    if (counter.get() == numOfIngredients) {
                        updateRecipeAsync(recipe, recipeIngredients, listener);
                    }
                } else {
                    // If the ingredient does not exist in our Ingredient collection,
                    // we will create a new ingredient for it.
                    ingredientDB.addIngredient(ingredient, (addedIngredient, success1) -> {
                        // Checks if adding a new Ingredient was successful.
                        if (addedIngredient == null) {
                            listener.onUpdateRecipe(null, false);
                            return;
                        }
                        ingredient.setId(addedIngredient.getId());

                        // Initializes HashMap for mapping.
                        HashMap<String, Object> ingredientMap = new HashMap<>();
                        ingredientMap.put("ingredientRef",
                                ingredientDB.getDocumentReference(addedIngredient));
                        ingredientMap.put("amount", ingredient.getAmount());
                        ingredientMap.put("unit", ingredient.getUnit());

                        counter.addAndGet(1);
                        recipeIngredients.add(ingredientMap);

                        if (counter.get() == numOfIngredients) {
                            updateRecipeAsync(recipe, recipeIngredients, listener);
                        }
                    });
                }
            });
        }
    }

    public void updateRecipeAsync(Recipe recipe,
                                  ArrayList<HashMap<String, Object>> recipeIngredients,
                                  OnUpdateRecipeListener listener) {
        getRecipe(recipe.getId(), (foundRecipe, success) -> {
                    if (foundRecipe == null) {
                        listener.onUpdateRecipe(null, false);
                        return;
                    } else {
                        updateRecipeAsync1(recipe, foundRecipe, recipeIngredients, listener);
                    }
                });
    }

    public void updateRecipeAsync1(Recipe recipe, Recipe foundRecipe,
                                   ArrayList<HashMap<String, Object>> recipeIngredients,
                                   OnUpdateRecipeListener listener) {
        AtomicInteger counter = new AtomicInteger(0);
        Integer numOfIngredients = recipe.getIngredients().size();

        // Increment count for each ingredient in the updated Recipe object.
        for (Ingredient ingredient: recipe.getIngredients()) {
            ingredientDB.updateReferenceCount(ingredient, 1, (i, s) -> {
                counter.addAndGet(1);
                if (counter.get() == numOfIngredients) {
                    updateRecipeAsync2(recipe, foundRecipe, recipeIngredients, listener);
                }
            });
        }
    }

    public void updateRecipeAsync2(Recipe recipe, Recipe foundRecipe,
                                   ArrayList<HashMap<String, Object>> recipeIngredients,
                                   OnUpdateRecipeListener listener) {
        AtomicInteger counter = new AtomicInteger(0);
        Integer numOfIngredients = foundRecipe.getIngredients().size();

        // Decrement counter for each ingredient in the old Recipe object.
        for (Ingredient ingredient: foundRecipe.getIngredients()) {
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
     * @param recipe the Recipe object to be updated
     * @param recipeIngredients the ArrayList of HashMaps of the ingredients
     * @param listener the OnUpdateRecipeListener object to handle the result
     */
    public void updateRecipeHelper(Recipe recipe,
                                   ArrayList<HashMap<String, Object>> recipeIngredients,
                                   OnUpdateRecipeListener listener) {
        DocumentReference recipeRef =
                this.collection.document(recipe.getId());

        recipeRef.update(
                "title", recipe.getTitle(),
                "category", recipe.getCategory(),
                "comments", recipe.getComments(),
                "photograph", recipe.getPhotograph(),
                "preparation-time", recipe.getPrepTimeMinutes(),
                "servings", recipe.getServings(),
                "ingredients", recipeIngredients
        ).addOnSuccessListener(success -> {
            listener.onUpdateRecipe(recipe, true);
        }).addOnFailureListener(failure -> {
            listener.onUpdateRecipe(null, false);
        });
    }


    /**
     * Deletes a recipe document with the id from the collection
     * of recipes
     *
     * @param recipe The recipe to delete.
     */
    public void delRecipe(Recipe recipe, OnRecipeDone listener){
        if (recipe == null){
            listener.onAddRecipe(null, false);
        }

        DocumentReference recipeRef = this.collection.document(recipe.getId());


        // Check if the Recipe exists in the Recipe collection.
        recipeRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Long count = document.getLong("count");
                            if (count != null && count > 0) {
                                // Do not allow user to delete recipe if count is > 0.
                                listener.onAddRecipe(null, false);
                                return;
                            } else {
                                delRecipeAsync(recipe, recipeRef, listener);
                            }
                        }
                    }
                });
    }

    public void delRecipeAsync(Recipe recipe, DocumentReference recipeRef, OnRecipeDone listener) {
        AtomicInteger counter = new AtomicInteger(0);
        Integer numOfIngredients = recipe.getIngredients().size();

        // Decrement count for each ingredient in the Recipe object.
        for (Ingredient ingredient: recipe.getIngredients()) {
            ingredientDB.updateReferenceCount(ingredient, -1, (i, s) -> {
                counter.addAndGet(1);
                if (counter.get() == numOfIngredients) {
                    delRecipeHelper(recipe, recipeRef, listener);
                }
            });
        }
    }

    public void delRecipeHelper(Recipe recipe, DocumentReference recipeRef, OnRecipeDone listener) {
        recipeRef.delete()
                .addOnSuccessListener(r->{
                    listener.onAddRecipe(recipe, true);
                })
                .addOnFailureListener(f->{
                    listener.onAddRecipe(null, false);
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


    public Recipe snapshotToRecipe(DocumentSnapshot doc) {
        Recipe recipe = new Recipe(doc.getString("title"));
        recipe.setCategory(doc.getString("category"));
        recipe.setComments(doc.getString("comments"));
        recipe.setPhotograph(doc.getString("photograph"));

        List<HashMap<String, Object>> ingredients = (List<HashMap<String, Object>>) doc.getData().get("ingredients");
        for (HashMap<String, Object> ingredientMap : ingredients) {
            DocumentReference ingredientRef = (DocumentReference) ingredientMap.get("ingredientRef");
            ingredientDB.getIngredient(ingredientRef, (foundIngredient, success) -> {
                if (foundIngredient != null) {
                    foundIngredient.setUnit((String) ingredientMap.get("unit"));
                    foundIngredient.setAmount((Double) ingredientMap.get("amount"));
                    recipe.addIngredient(foundIngredient);
                }
            });
        }
        return recipe;
    }

    public Query getQuery() {
        return this.collection;
    }

    public Query getSortQuery(String field){
        return this.collection.orderBy(field);
    }

    // TODO: updateReferenceCount(), see IngredientDB
    public void updateReferenceCount(Recipe recipe, int delta,
                                     OnUpdateRecipeReferenceCountListener listener) {
        final DocumentReference recipeRef =
                this.collection.document(recipe.getId());

        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(recipeRef);

                Long newCount = snapshot.getLong("count") + delta;
                transaction.update(recipeRef, "count", newCount);

                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                listener.onUpdateReferenceCount(recipe, true);
                Log.d(TAG, "Transaction success!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onUpdateReferenceCount(recipe, false);
                Log.w(TAG, "Transaction failure.", e);
            }
        });

//        // Get reference of the recipe document.
//        String id = getDocumentReference(recipe.getId()).getId();
//
//        // Get recipe document snapshot from db.
//        collection.document(id).get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot document = task.getResult();
//                        if (document.exists()) {
//                            Long count = document.getLong("count");
////                            if (count == null) {
////                                count = 0L;
////                            }
//
//                            count += delta;
//                            if (count > 0) {
//                                collection.document(id)
//                                        .update("count", count)
//                                        .addOnCompleteListener(task1 -> {
//                                            listener.onUpdateReferenceCount(
//                                                    recipe,
//                                                    task1.isSuccessful()
//                                            );
//                                        });
//                            } else {
//                                // Even if none of the meal plans reference this recipe,,
//                                // we still won't delete it. Only users can delete recipes.
//
////                                delRecipe(recipe, (deletedRecipe, success) -> {
////                                    listener.onUpdateReferenceCount(
////                                            recipe,
////                                            success
////                                    );
////                                });
//                            }
//                        } else {
//                            listener.onUpdateReferenceCount(recipe, false);
//                        }
//                    } else {
//                        listener.onUpdateReferenceCount(recipe, false);
//                    }
//                });
    }
}