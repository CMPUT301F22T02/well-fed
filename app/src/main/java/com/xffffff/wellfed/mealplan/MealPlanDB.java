package com.xffffff.wellfed.mealplan;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.xffffff.wellfed.common.DBConnection;
import com.xffffff.wellfed.common.OnCompleteListener;
import com.xffffff.wellfed.ingredient.Ingredient;
import com.xffffff.wellfed.ingredient.IngredientDB;
import com.xffffff.wellfed.recipe.Recipe;
import com.xffffff.wellfed.recipe.RecipeDB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class is used to access the meal plan database.
 * It contains methods to add, remove, and update meal plans.
 * It also contains methods to add, remove, and update recipes in meal plans.
 */
public class MealPlanDB {
    /**
     * Declares tag for logging purposes.
     */
    private final static String TAG = "MealPlanDB";

    /**
     * Holds the instance of the RecipeDB.
     */
    private RecipeDB recipeDB;

    /**
     * Holds the instance of the StorageIngredientDB.
     */
    private IngredientDB ingredientDB;

    /**
     * Holds the CollectionReference for the users MealPlan collection.
     */
    private CollectionReference mealPlanCollection;

    /**
     * Constructor for class MealPlanDB, initializes declared fields.
     *
     * @param connection the DBConnection object used to access the db.
     */
    public MealPlanDB(DBConnection connection) {
        // Creates new instances of RecipeDB and storageIngredientDB
        // based on current user connection.
        recipeDB = new RecipeDB(connection);
        ingredientDB = new IngredientDB(connection);

        // Gets the current user's MealPlan collection from db,
        // create one if the collection DNE.
        this.mealPlanCollection = connection.getCollection("MealPlan");
    }

    /**
     * This method is used to add a MealPlan object to the db.
     *
     * @param mealPlan the MealPlan object to be added to the db.
     * @param listener the OnAddMealPlanListener object to handle the result.
     */
    public void addMealPlan(MealPlan mealPlan, OnCompleteListener<MealPlan> listener) {
        // Stores each ingredient as a HashMap with fields:
        // ingredientRef, amount & unit.
        ArrayList<HashMap<String, Object>> mealPlanIngredients =
                new ArrayList<>();

        // Stores references to recipes in the MealPlan object.
        ArrayList<DocumentReference> mealPlanRecipes = new ArrayList<>();

        // Initializes MealPlan document mapping.
        HashMap<String, Object> mealPlanMap = new HashMap<>();

        // AtomicInteger used to keep track of number of recipes
        // and ingredients we have got.
        AtomicInteger counter = new AtomicInteger(0);

        // Calculates number of ingredients and recipes.
        Integer numOfIngredientsAndRecipes =
                mealPlan.getIngredients().size() + mealPlan.getRecipes().size();

        // Iterate over each ingredient in the MealPlan object and
        // increment counter for each ingredient.
        for (Ingredient ingredient : mealPlan.getIngredients()) {
            ingredientDB.getIngredient(ingredient,
                    (foundIngredient, success) -> {
                        // If the ingredient already exists in Ingredient
                        // collection.
                        if (foundIngredient != null) {
                            // Initializes HashMap for mapping.
                            HashMap<String, Object> ingredientMap =
                                    new HashMap<>();

                            ingredient.setId(foundIngredient.getId());

                            // Sets fields.
                            ingredientMap.put("ingredientRef",
                                    ingredientDB.getDocumentReference(
                                            foundIngredient));
                            ingredientMap.put("amount", ingredient.getAmount());
                            ingredientMap.put("unit", ingredient.getUnit());

                            // Increments counter & adds HashMap to ArrayList
                            counter.addAndGet(1);
                            mealPlanIngredients.add(ingredientMap);

                            // Check if the counter has reached the number of
                            // ingredients and recipes.
                            if (counter.get() == numOfIngredientsAndRecipes) {
                                addMealPlanIncrementCounter(mealPlanMap,
                                        mealPlan, mealPlanIngredients,
                                        mealPlanRecipes, listener);
                            }
                        } else {
                            // If the ingredient does not exist in our
                            // Ingredient collection,
                            // we will create a new ingredient for it.
                            ingredientDB.addIngredient(ingredient,
                                    (addedIngredient, success1) -> {
                                        // Checks if adding a new Ingredient
                                        // was successful.
                                        if (addedIngredient == null) {
                                            listener.onComplete(null, false);
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
                                        mealPlanIngredients.add(ingredientMap);

                                        if (counter.get() ==
                                                numOfIngredientsAndRecipes) {
                                            addMealPlanIncrementCounter(
                                                    mealPlanMap, mealPlan,
                                                    mealPlanIngredients,
                                                    mealPlanRecipes, listener);
                                        }
                                    });
                        }
                    });
        }

        // Iterate over each recipe in the MealPlan object and
        // increment counter for each recipe.
        for (Recipe recipe : mealPlan.getRecipes()) {
            if (recipe.getId() != null) {
                // If the recipe has an id, gets the recipe's reference from db.
                DocumentReference recipeRef =
                        recipeDB.getDocumentReference(recipe.getId());

                // Atomically adds the given value to the current value,
                // with memory effects as specified by VarHandle.getAndAdd.
                counter.addAndGet(1);
                mealPlanRecipes.add(recipeRef);

                if (counter.get() == numOfIngredientsAndRecipes) {
                    addMealPlanIncrementCounter(mealPlanMap, mealPlan,
                            mealPlanIngredients, mealPlanRecipes, listener);
                }
            } else {
                // If the recipe does not exist, we add it to the db.
                recipeDB.addRecipe(recipe, (addedRecipe, success) -> {
                    if (addedRecipe == null) {
                        // Checks if adding a new Recipe was successful.
                        listener.onComplete(null, false);
                        return;
                    }
                    recipe.setId(addedRecipe.getId());

                    counter.addAndGet(1);
                    // Adds DocumentReference to the ArrayList.
                    mealPlanRecipes.add(
                            recipeDB.getDocumentReference(addedRecipe.getId()));

                    if (counter.get() == numOfIngredientsAndRecipes) {
                        addMealPlanIncrementCounter(mealPlanMap, mealPlan,
                                mealPlanIngredients, mealPlanRecipes, listener);
                    }
                });
            }
        }
    }

    /**
     * addMealPlanIncrementCounter is a helper method for addMealPlan that
     * increment counter for each recipe and ingredient in the MealPlan object.
     *
     * @param mealPlanMap         the HashMap to be used to map the MealPlan
     *                            object.
     * @param mealPlan            the MealPlan object to be added to the db.
     * @param mealPlanIngredients the ArrayList of HashMaps to be used to map
     * @param mealPlanRecipes     the ArrayList of DocumentReferences to be
     *                            used to
     * @param listener            the OnAddMealPlanListener object to handle
     *                            the result.
     */
    public void addMealPlanIncrementCounter(HashMap<String, Object> mealPlanMap,
                                            MealPlan mealPlan,
                                            ArrayList<HashMap<String, Object>> mealPlanIngredients,
                                            ArrayList<DocumentReference> mealPlanRecipes,
                                            OnCompleteListener<MealPlan> listener) {
        AtomicInteger counter = new AtomicInteger(0);
        Integer numOfIngredientsAndRecipes =
                mealPlan.getIngredients().size() + mealPlan.getRecipes().size();

        for (Ingredient ingredient : mealPlan.getIngredients()) {
            ingredientDB.updateReferenceCount(ingredient, 1, (i, success) -> {
                counter.addAndGet(1);
                if (counter.get() == numOfIngredientsAndRecipes) {
                    addMealPlanHelper(mealPlanMap, mealPlan,
                            mealPlanIngredients, mealPlanRecipes, listener);
                }
            });
        }

        for (Recipe recipe : mealPlan.getRecipes()) {
            recipeDB.updateReferenceCount(recipe, 1, (i, success) -> {
                counter.addAndGet(1);
                if (counter.get() == numOfIngredientsAndRecipes) {
                    addMealPlanHelper(mealPlanMap, mealPlan,
                            mealPlanIngredients, mealPlanRecipes, listener);
                }
            });
        }
    }

    /**
     * Helper function that adds the MealPlan document to db
     * once all transactions are finished.
     *
     * @param mealPlanMap         the mapping of the MealPlan object.
     * @param mealPlan            the MealPlan object to be added to the db.
     * @param mealPlanIngredients the ArrayList of HashMaps that stores
     *                            the ingredients in the MealPlan object.
     * @param mealPlanRecipes     the ArrayList of DocumentReferences that
     *                            stores the recipes in the MealPlan object.
     * @param listener            the OnAddMealPlanListener object to handle
     *                            the result.
     */
    public void addMealPlanHelper(HashMap<String, Object> mealPlanMap,
                                  MealPlan mealPlan,
                                  ArrayList<HashMap<String, Object>> mealPlanIngredients,
                                  ArrayList<DocumentReference> mealPlanRecipes,
                                  OnCompleteListener<MealPlan> listener) {
        // Fill the mealPlanMap with correct values.
        mealPlanMap.put("ingredients", mealPlanIngredients);
        mealPlanMap.put("recipes", mealPlanRecipes);
        mealPlanMap.put("title", mealPlan.getTitle());
        mealPlanMap.put("category", mealPlan.getCategory());
        mealPlanMap.put("eat date", mealPlan.getEatDate());
        mealPlanMap.put("servings", mealPlan.getServings());

        // Adds the MealPlan mapping to the db.
        this.mealPlanCollection.add(mealPlanMap)
                .addOnSuccessListener(addedMealPlanDoc -> {
                    // Sets the document id for the MealPlan object.
                    mealPlan.setId(addedMealPlanDoc.getId());

                    // Calls the listener's onAddMealPlanResult method.
                    listener.onComplete(mealPlan, true);
                }).addOnFailureListener(failure -> {
                    listener.onComplete(null, false);
                });
    }

    /**
     * Gets the MealPlan object with the given id from the db.
     *
     * @param id       the id of the MealPlan object to be retrieved.
     * @param listener the OnGetMealPlanListener object to handle the result.
     */
    public void getMealPlan(String id, OnCompleteListener<MealPlan> listener) {
        if (id == null) {
            listener.onComplete(null, false);
            return;
        }

        // Get reference to the MealPlan document with the given id.
        DocumentReference mealPlanRef = this.mealPlanCollection.document(id);
        mealPlanRef.get().addOnSuccessListener(mealPlanDoc -> {
                    getMealPlan(mealPlanDoc, listener);
                })
                // If the MealPlan document is not found, return null.
                .addOnFailureListener(failure -> {
                    listener.onComplete(null, false);
                });
    }

    /**
     * Gets the MealPlan proxy object with the given snapshot from the db.
     *
     * @param snapshot the snapshot of the MealPlan proxy object to be
     *                 retrieved.
     *
     * @return the MealPlan proxy object with the given snapshot.
     */
    public MealPlanProxy getMealPlanProxy(DocumentSnapshot snapshot) {
        MealPlanProxy mealPlan = new MealPlanProxy(snapshot.getString("title"));
        mealPlan.setId(snapshot.getId());
        mealPlan.setCategory(snapshot.getString("category"));
        mealPlan.setEatDate(snapshot.getDate("eat date"));
        mealPlan.setServings(snapshot.getLong("servings"));
        ArrayList<HashMap<String, Object>> ingredients =
                (ArrayList<HashMap<String, Object>>) snapshot.get(
                        "ingredients");
        ArrayList<HashMap<String, Object>> recipes =
                (ArrayList<HashMap<String, Object>>) snapshot.get("recipes");
        mealPlan.setIngredients(
                new ArrayList<>(Collections.nCopies(ingredients.size(), null)));
        mealPlan.setRecipes(
                new ArrayList<>(Collections.nCopies(recipes.size(), null)));
        return mealPlan;
    }

    /**
     * Gets the MealPlan object with the given id from the db.
     *
     * @param snapshot the snapshot of the MealPlan object to be retrieved.
     * @param listener the OnGetMealPlanListener object to handle the result.
     */
    public void getMealPlan(DocumentSnapshot snapshot,
                            OnCompleteListener<MealPlan> listener) {
        // Initializes a new MealPlan object and sets its fields.
        MealPlan mealPlan = getMealPlanProxy(snapshot).getMealPlan();

        // Initializes ArrayLists for ingredients & recipes.
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        ArrayList<Recipe> recipes = new ArrayList<>();

        // Get the list of MealPlan ingredients from the MealPlan document.
        ArrayList<HashMap<String, Object>> mealPlanIngredients =
                (ArrayList<HashMap<String, Object>>) snapshot.get(
                        "ingredients");

        // Serves as a lock to ensure that the processes won't
        // run in parallel.
        AtomicInteger counter = new AtomicInteger(0);

        // Calculates number of ingredients and recipes based on
        // the snapshot.
        Integer numOfIngredientsAndRecipes = mealPlanIngredients.size() +
                ((ArrayList<DocumentReference>) snapshot.get("recipes")).size();

        // Iterate over the ArrayList of HashMaps and produce Ingredient
        // objects.
        for (HashMap<String, Object> ingredientMap : mealPlanIngredients) {
            DocumentReference ingredientRef =
                    (DocumentReference) ingredientMap.get("ingredientRef");

            ingredientDB.getIngredient(ingredientRef,
                    (foundIngredient, success) -> {
                        // Sets amount and unit for the ingredient found in
                        // the db via ingredientRef.
                        foundIngredient.setAmount(
                                (Double) ingredientMap.get("amount"));
                        foundIngredient.setUnit(
                                (String) ingredientMap.get("unit"));

                        // Increment counter inside query on ingredientDB so
                        // that the processes won't run in parallel.
                        counter.addAndGet(1);

                        // Adds the ingredient to the ArrayList.
                        ingredients.add(foundIngredient);

                        if (counter.get() == numOfIngredientsAndRecipes) {
                            getMealPlanHelper(mealPlan, ingredients, recipes,
                                    listener);
                        }
                    });
        }

        // Iterate over the ArrayList of DocumentReferences and produce
        // Recipe objects using the method implemented in RecipeDB.
        for (DocumentReference recipeRef :
                (ArrayList<DocumentReference>) snapshot.get(
                "recipes")) {
            recipeDB.getRecipe(recipeRef.getId(), (foundRecipe, success) -> {
                counter.addAndGet(1);
                recipes.add(foundRecipe);

                if (counter.get() == numOfIngredientsAndRecipes) {
                    getMealPlanHelper(mealPlan, ingredients, recipes, listener);
                }
            });
        }
    }

    /**
     * Helper function for getMealPlan.
     *
     * @param mealPlan    the MealPlan object to be returned.
     * @param ingredients the ArrayList of ingredients to be added to the
     *                    MealPlan object.
     * @param recipes     the ArrayList of recipes to be added to the MealPlan
     *                    object.
     * @param listener    the OnGetMealPlanListener object to handle the result.
     */
    public void getMealPlanHelper(MealPlan mealPlan,
                                  ArrayList<Ingredient> ingredients,
                                  ArrayList<Recipe> recipes,
                                  OnCompleteListener<MealPlan> listener) {
        // Add ingredients and recipes to the MealPlan object.
        for (Ingredient ingredient : ingredients) {
            mealPlan.addIngredient(ingredient);
        }

        for (Recipe recipe : recipes) {
            mealPlan.addRecipe(recipe);
        }

        // Returns the MealPlan object.
        listener.onComplete(mealPlan, true);
    }

    /**
     * Deletes the MealPlan object with the given id from the db.
     *
     * @param mealPlan the MealPlan object to be deleted.
     * @param listener the OnDeleteMealPlanListener object to handle the result.
     */
    public void delMealPlan(MealPlan mealPlan,
                            OnCompleteListener<MealPlan> listener) {
        if (mealPlan == null) {
            listener.onComplete(null, false);
            return;
        }

        DocumentReference mealPlanRef =
                this.mealPlanCollection.document(mealPlan.getId());

        mealPlanRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    delMealPlanDecrementCounter(mealPlan, mealPlanRef,
                            listener);
                }
            }
        });
    }

    /**
     * delMealPlanDecrementCounter is an asynchronous helper function for
     * delMealPlan
     * that decrements the counter of the ingredients and recipes in the
     * MealPlan.
     *
     * @param mealPlan    the MealPlan object to be deleted.
     * @param mealPlanRef the DocumentReference of the MealPlan object to be
     * @param listener    the OnDeleteMealPlanListener object to handle the
     *                    result.
     */
    public void delMealPlanDecrementCounter(MealPlan mealPlan,
                                            DocumentReference mealPlanRef,
                                            OnCompleteListener<MealPlan> listener) {
        AtomicInteger counter = new AtomicInteger(0);
        Integer numOfIngredientsAndRecipes =
                mealPlan.getIngredients().size() + mealPlan.getRecipes().size();

        // Decrement count for each ingredient & recipe in the MealPlan object.
        for (Ingredient ingredient : mealPlan.getIngredients()) {
            ingredientDB.updateReferenceCount(ingredient, -1, (i, success) -> {
                counter.addAndGet(1);
                if (counter.get() == numOfIngredientsAndRecipes) {
                    delMealPlanHelper(mealPlan, mealPlanRef, listener);
                }
            });
        }

        for (Recipe recipe : mealPlan.getRecipes()) {
            recipeDB.updateReferenceCount(recipe, -1, (r, success) -> {
                counter.addAndGet(1);
                if (counter.get() == numOfIngredientsAndRecipes) {
                    delMealPlanHelper(mealPlan, mealPlanRef, listener);
                }
            });
        }
    }

    /**
     * delMealPlanHelper is a helper function for delMealPlanAsync.
     *
     * @param mealPlan    the MealPlan object to be deleted.
     * @param mealPlanRef the DocumentReference of the MealPlan object to be
     *                    deleted.
     * @param listener    the OnDeleteMealPlanListener object to handle the
     *                    result.
     */
    public void delMealPlanHelper(MealPlan mealPlan,
                                  DocumentReference mealPlanRef,
                                  OnCompleteListener<MealPlan> listener) {
        mealPlanRef.delete().addOnSuccessListener(success -> {
            listener.onComplete(mealPlan, true);
        }).addOnFailureListener(failure -> {
            listener.onComplete(mealPlan, false);
        });
    }

    /**
     * Get all MealPlan objects from the db.
     *
     * @param listener the OnGetMealPlansListener object to handle the result.
     */
    public void getMealPlans(OnCompleteListener<ArrayList<MealPlan>> listener) {
        this.mealPlanCollection.get().addOnSuccessListener(mealPlans -> {
            ArrayList<MealPlan> mealPlanList = new ArrayList<>();
            if (mealPlans.size() == 0) {
                listener.onComplete(mealPlanList, true);
                return;
            }
            for (DocumentSnapshot mealPlanDoc : mealPlans) {
                getMealPlan(mealPlanDoc, (foundMealPlan, success) -> {
                    mealPlanList.add(foundMealPlan);
                    if (mealPlanList.size() == mealPlans.size()) {
                        listener.onComplete(mealPlanList, true);
                    }
                });
            }
        }).addOnFailureListener(failure -> {
            listener.onComplete(null, false);
        });
    }

    /**
     * Updates the MealPlan object in the DB
     *
     * @param mealPlan the MealPlan object to be updated.
     * @param listener the OnUpdateMealPlanListener object to handle the result.
     */
    public void updateMealPlan(MealPlan mealPlan, OnCompleteListener<MealPlan> listener) {

        ArrayList<HashMap<String, Object>> mealPlanIngredients =
                new ArrayList<>();
        ArrayList<DocumentReference> mealPlanRecipes = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger(0);
        Integer numOfIngredientsAndRecipes =
                mealPlan.getIngredients().size() + mealPlan.getRecipes().size();

        // Iterate over each ingredient in the MealPlan object and
        // increment counter for each ingredient.
        for (Ingredient ingredient : mealPlan.getIngredients()) {
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
                            mealPlanIngredients.add(ingredientMap);

                            // Check if the counter has reached the number of
                            // ingredients and recipes.
                            if (counter.get() == numOfIngredientsAndRecipes) {
                                updateMealPlanFindOldMealPlan(mealPlan,
                                        listener, mealPlanIngredients,
                                        mealPlanRecipes);
                            }
                        } else {
                            // If the ingredient does not exist in our
                            // Ingredient collection,
                            // we will create a new ingredient for it.
                            ingredientDB.addIngredient(ingredient,
                                    (addedIngredient, success1) -> {
                                        // Checks if adding a new Ingredient
                                        // was successful.
                                        if (addedIngredient == null) {
                                            listener.onComplete(null, false);
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
                                        mealPlanIngredients.add(ingredientMap);

                                        if (counter.get() ==
                                                numOfIngredientsAndRecipes) {
                                            updateMealPlanFindOldMealPlan(
                                                    mealPlan, listener,
                                                    mealPlanIngredients,
                                                    mealPlanRecipes);
                                        }
                                    });
                        }
                    });
        }

        // Iterate over each recipe in the MealPlan object and
        // increment counter for each recipe.
        for (Recipe recipe : mealPlan.getRecipes()) {
            if (recipe.getId() != null) {
                recipe.setId(recipe.getId());

                // If the recipe has an id, gets the recipe's reference from db.
                DocumentReference recipeRef =
                        recipeDB.getDocumentReference(recipe.getId());

                // Atomically adds the given value to the current value,
                // with memory effects as specified by VarHandle.getAndAdd.
                counter.addAndGet(1);
                mealPlanRecipes.add(recipeRef);

                if (counter.get() == numOfIngredientsAndRecipes) {
                    updateMealPlanFindOldMealPlan(mealPlan, listener,
                            mealPlanIngredients, mealPlanRecipes);
                }
            } else {
                // If the recipe does not exist, we add it to the db.
                recipeDB.addRecipe(recipe, (addedRecipe, success) -> {
                    if (addedRecipe == null) {
                        // Checks if adding a new Recipe was successful.
                        listener.onComplete(null, false);
                        return;
                    }
                    recipe.setId(addedRecipe.getId());

                    counter.addAndGet(1);
                    // Adds DocumentReference to the ArrayList.
                    mealPlanRecipes.add(
                            recipeDB.getDocumentReference(addedRecipe.getId()));

                    if (counter.get() == numOfIngredientsAndRecipes) {
                        updateMealPlanFindOldMealPlan(mealPlan, listener,
                                mealPlanIngredients, mealPlanRecipes);
                    }
                });
            }
        }
    }

    /**
     * updateMealPlanFindOldMealPlan is a helper method for updateMealPlan.
     *
     * @param mealPlan            the MealPlan object to be updated.
     * @param listener            the OnUpdateMealPlanListener object to
     *                            handle the result.
     * @param mealPlanIngredients the ArrayList of HashMaps of ingredients.
     * @param mealPlanRecipes     the ArrayList of DocumentReferences of
     *                            recipes.
     */
    public void updateMealPlanFindOldMealPlan(MealPlan mealPlan,
                                              OnCompleteListener<MealPlan> listener,
                                              ArrayList<HashMap<String,
                                                      Object>> mealPlanIngredients,
                                              ArrayList<DocumentReference> mealPlanRecipes) {
        getMealPlan(mealPlan.getId(), (foundMealPlan, success) -> {
            if (foundMealPlan == null) {
                listener.onComplete(null, false);
                return;
            } else {
                updateMealPlanIncrementCounter(mealPlan, foundMealPlan,
                        listener, mealPlanIngredients, mealPlanRecipes);
            }
        });
    }

    /**
     * updateMealPlanIncrementCounter is a helper method for updateMealPlanAsync
     * that increments the counter for recipes and ingredients in the
     * MealPlan object.
     *
     * @param mealPlan            the MealPlan object to be updated.
     * @param foundMealPlan       the MealPlan object found in the db.
     * @param listener            the OnUpdateMealPlanListener object to
     *                            handle the result.
     * @param mealPlanIngredients the ArrayList of HashMaps of ingredients.
     * @param mealPlanRecipes     the ArrayList of DocumentReferences of
     *                            recipes.
     */
    public void updateMealPlanIncrementCounter(MealPlan mealPlan,
                                               MealPlan foundMealPlan,
                                               OnCompleteListener<MealPlan> listener,
                                               ArrayList<HashMap<String,
                                                       Object>> mealPlanIngredients,
                                               ArrayList<DocumentReference> mealPlanRecipes) {
        AtomicInteger counter = new AtomicInteger(0);
        Integer numOfIngredientsAndRecipes =
                mealPlan.getIngredients().size() + mealPlan.getRecipes().size();

        // Increment count for each ingredient & recipe in the updated
        // MealPlan object.
        for (Ingredient ingredient : mealPlan.getIngredients()) {
            ingredientDB.updateReferenceCount(ingredient, 1, (i, s) -> {
                counter.addAndGet(1);
                if (counter.get() == numOfIngredientsAndRecipes) {
                    updateMealPlanDecrementCounter(mealPlan, foundMealPlan,
                            listener, mealPlanIngredients, mealPlanRecipes);
                }
            });
        }

        for (Recipe recipe : mealPlan.getRecipes()) {
            recipeDB.updateReferenceCount(recipe, 1, (i1, s1) -> {
                counter.addAndGet(1);
                if (counter.get() == numOfIngredientsAndRecipes) {
                    updateMealPlanDecrementCounter(mealPlan, foundMealPlan,
                            listener, mealPlanIngredients, mealPlanRecipes);
                }
            });
        }
    }

    /**
     * updateMealPlanDecrementCounter is a helper method for updateMealPlanAsync
     * that decrements counter for recipes and ingredients in the MealPlan
     * object.
     *
     * @param mealPlan            the MealPlan object to be updated.
     * @param foundMealPlan       the MealPlan object found in the db.
     * @param listener            the OnUpdateMealPlanListener object to
     *                            handle the result.
     * @param mealPlanIngredients the ArrayList of HashMaps of ingredients.
     * @param mealPlanRecipes     the ArrayList of DocumentReferences of
     *                            recipes.
     */
    public void updateMealPlanDecrementCounter(MealPlan mealPlan,
                                               MealPlan foundMealPlan,
                                               OnCompleteListener<MealPlan> listener,
                                               ArrayList<HashMap<String,
                                                       Object>> mealPlanIngredients,
                                               ArrayList<DocumentReference> mealPlanRecipes) {
        AtomicInteger counter = new AtomicInteger(0);
        Integer numOfIngredientsAndRecipes =
                foundMealPlan.getIngredients().size() +
                        foundMealPlan.getRecipes().size();

        // Decrement count for each ingredient & recipe in the old MealPlan
        // object.
        for (Ingredient ingredient : foundMealPlan.getIngredients()) {
            ingredientDB.updateReferenceCount(ingredient, -1, (i, s) -> {
                counter.addAndGet(1);
                if (counter.get() == numOfIngredientsAndRecipes) {
                    updateMealPlanUpdateRecipes(mealPlan, listener,
                            mealPlanIngredients, mealPlanRecipes);
                }
            });
        }

        for (Recipe recipe : foundMealPlan.getRecipes()) {
            recipeDB.updateReferenceCount(recipe, -1, (i1, s1) -> {
                counter.addAndGet(1);
                if (counter.get() == numOfIngredientsAndRecipes) {
                    updateMealPlanUpdateRecipes(mealPlan, listener,
                            mealPlanIngredients, mealPlanRecipes);
                }
            });
        }
    }

    /**
     * updateMealPlanUpdateRecipes is a helper method for updateMealPlanAsync
     * that updates the recipes in the MealPlan object.
     *
     * @param mealPlan            the MealPlan object to be updated.
     * @param listener            the OnUpdateMealPlanListener object to
     *                            handle the result.
     * @param mealPlanIngredients the ArrayList of HashMaps of ingredients.
     * @param mealPlanRecipes     the ArrayList of DocumentReferences of
     *                            recipes.
     */
    public void updateMealPlanUpdateRecipes(MealPlan mealPlan,
                                            OnCompleteListener<MealPlan> listener,
                                            ArrayList<HashMap<String, Object>> mealPlanIngredients,
                                            ArrayList<DocumentReference> mealPlanRecipes) {
        AtomicInteger counter = new AtomicInteger(0);
        Integer numRecipes = mealPlan.getRecipes().size();

        if (numRecipes == 0) {
            updateMealPlanHelper(mealPlan, listener, mealPlanIngredients,
                    mealPlanRecipes);
        }

        for (Recipe recipe : mealPlan.getRecipes()) {
            recipeDB.updateRecipe(recipe, (i1, s1) -> {
                counter.addAndGet(1);
                if (counter.get() == numRecipes) {
                    updateMealPlanHelper(mealPlan, listener,
                            mealPlanIngredients, mealPlanRecipes);
                }
            });
        }
    }

    /**
     * Given the MealPlan object, updates it in the db.
     *
     * @param mealPlan            the MealPlan object to be updated.
     * @param listener            the OnUpdateMealPlanListener object to
     *                            handle the result.
     * @param mealPlanIngredients the ArrayList of Ingredient objects to be
     *                            updated.
     * @param mealPlanRecipes     the ArrayList of Recipe objects to be updated.
     */
    public void updateMealPlanHelper(MealPlan mealPlan,
                                     OnCompleteListener<MealPlan> listener,
                                     ArrayList<HashMap<String, Object>> mealPlanIngredients,
                                     ArrayList<DocumentReference> mealPlanRecipes) {
        // Get reference to the MealPlan document with the given id
        // so that we can perform update() on it.
        DocumentReference mealPlanRef =
                this.mealPlanCollection.document(mealPlan.getId());

        // Update the MealPlan document in the db.
        mealPlanRef.update("title", mealPlan.getTitle(), "category",
                        mealPlan.getCategory(), "eat date",
                        mealPlan.getEatDate(),
                        "servings", mealPlan.getServings(), "ingredients",
                        mealPlanIngredients, "recipes", mealPlanRecipes)
                .addOnSuccessListener(success -> {
                    listener.onComplete(mealPlan, true);
                }).addOnFailureListener(failure -> {
                    listener.onComplete(mealPlan, false);
                });
    }

    /**
     * Fet the DocumentReference object for the MealPlan document with the
     * given id.
     *
     * @param id the id of the MealPlan document.
     * @return the DocumentReference object for the MealPlan document.
     */
    public DocumentReference getMealPlanDocumentReference(String id) {
        return this.mealPlanCollection.document(id);
    }

    /**
     * Gets a query for MealPlans in the db.
     *
     * @return the query for MealPlans in the db.
     */
    public Query getQuery() {
        return this.mealPlanCollection.orderBy("eat date",
                Query.Direction.ASCENDING);
    }
}