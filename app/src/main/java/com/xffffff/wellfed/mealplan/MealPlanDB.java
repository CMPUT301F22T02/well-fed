package com.xffffff.wellfed.mealplan;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.xffffff.wellfed.common.DBConnection;
import com.xffffff.wellfed.ingredient.Ingredient;
import com.xffffff.wellfed.ingredient.IngredientDB;
import com.xffffff.wellfed.recipe.Recipe;
import com.xffffff.wellfed.recipe.RecipeDB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
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
     * Holds the instance of the Firebase FireStore DB.
     */
    private FirebaseFirestore db;

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

        // Gets the instance of the Firebase FireStore DB based
        // on current user connection.
        this.db = connection.getDB();

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
    public void addMealPlan(MealPlan mealPlan, OnAddMealPlanListener listener) {
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

        // TODO: Placeholder since we can't add recipes &
        // TODO: ingredients to MealPlan rn. Ideally we should
        // TODO: return (null, false) in this case.
        if (numOfIngredientsAndRecipes == 0) {
            addMealPlanHelper(mealPlanMap, mealPlan, mealPlanIngredients,
                    mealPlanRecipes, listener);
            // listener.onAddMealPlanResult(null, false);
        }

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
                                addMealPlanSync(mealPlanMap, mealPlan,
                                        mealPlanIngredients, mealPlanRecipes,
                                        listener);
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
                                            listener.onAddMealPlanResult(null,
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
                                        mealPlanIngredients.add(ingredientMap);

                                        if (counter.get() ==
                                                numOfIngredientsAndRecipes) {
                                            addMealPlanSync(mealPlanMap,
                                                    mealPlan,
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
                    addMealPlanSync(mealPlanMap, mealPlan, mealPlanIngredients,
                            mealPlanRecipes, listener);
                }
            } else {
                // If the recipe does not exist, we add it to the db.
                recipeDB.addRecipe(recipe, (addedRecipe, success) -> {
                    if (addedRecipe == null) {
                        // Checks if adding a new Recipe was successful.
                        listener.onAddMealPlanResult(null, false);
                        return;
                    }
                    recipe.setId(addedRecipe.getId());

                    counter.addAndGet(1);
                    // Adds DocumentReference to the ArrayList.
                    mealPlanRecipes.add(
                            recipeDB.getDocumentReference(addedRecipe.getId()));

                    if (counter.get() == numOfIngredientsAndRecipes) {
                        addMealPlanSync(mealPlanMap, mealPlan,
                                mealPlanIngredients, mealPlanRecipes, listener);
                    }
                });
            }
        }
    }

    public void addMealPlanSync(HashMap<String, Object> mealPlanMap,
                                MealPlan mealPlan,
                                ArrayList<HashMap<String, Object>> mealPlanIngredients,
                                ArrayList<DocumentReference> mealPlanRecipes,
                                OnAddMealPlanListener listener) {
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
                                  OnAddMealPlanListener listener) {
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
                    listener.onAddMealPlanResult(mealPlan, true);
                }).addOnFailureListener(failure -> {
                    listener.onAddMealPlanResult(null, false);
                });
    }

    /**
     * Gets the MealPlan object with the given id from the db.
     *
     * @param id       the id of the MealPlan object to be retrieved.
     * @param listener the OnGetMealPlanListener object to handle the result.
     */
    public void getMealPlan(String id, OnGetMealPlanListener listener) {
        if (id == null) {
            listener.onGetMealPlanResult(null, false);
            return;
        }

        // Get reference to the MealPlan document with the given id.
        DocumentReference mealPlanRef = this.mealPlanCollection.document(id);
        mealPlanRef.get().addOnSuccessListener(mealPlanDoc -> {
                    getMealPlan(mealPlanDoc, listener);
                })
                // If the MealPlan document is not found, return null.
                .addOnFailureListener(failure -> {
                    listener.onGetMealPlanResult(null, false);
                });
    }

    /**
     * Gets the MealPlan proxy object with the given snapshot from the db.
     *
     * @param snapshot the snapshot of the MealPlan proxy object to be
     *                 retrieved.
     */
    public MealPlanProxy getMealPlanProxy(DocumentSnapshot snapshot) {
        MealPlanProxy mealPlan = new MealPlanProxy(snapshot.getString("title"));
        mealPlan.setId(snapshot.getId());
        mealPlan.setCategory(snapshot.getString("category"));
        mealPlan.setEatDate(snapshot.getDate("eat date"));
        // TODO: refactor mealPlan to use Long instead of Integer
        mealPlan.setServings(
                Objects.requireNonNull(snapshot.getLong("servings"))
                        .intValue());
        ArrayList<HashMap<String, Object>> ingredients =
                (ArrayList<HashMap<String, Object>>) snapshot.get(
                        "ingredients");
        ArrayList<HashMap<String, Object>> recipes =
                (ArrayList<HashMap<String, Object>>) snapshot.get(
                        "recipes");
        mealPlan.setIngredients(new ArrayList<>(Collections.nCopies(ingredients.size(),
            null)));
        mealPlan.setRecipes(new ArrayList<>(Collections.nCopies(recipes.size(),
                null)));
        return mealPlan;
    }

    /**
     * Gets the MealPlan object with the given id from the db.
     *
     * @param snapshot the snapshot of the MealPlan object to be retrieved.
     * @param listener the OnGetMealPlanListener object to handle the result.
     */
    // TODO: async
    public void getMealPlan(DocumentSnapshot snapshot,
                            OnGetMealPlanListener listener) {
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

        // If the "ingredients" and "recipes" fields are empty, ie.,
        // numOfIngredientsAndRecipes is 0, then we can call
        // helper function directly. Ideally there should be sth in
        // these fields.
        if (numOfIngredientsAndRecipes == 0) {
            getMealPlanHelper(mealPlan, ingredients, recipes, listener);
        }


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
                        // that
                        // the processes won't run in parallel.
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
                                  OnGetMealPlanListener listener) {
        // Add ingredients and recipes to the MealPlan object.
        for (Ingredient ingredient : ingredients) {
            mealPlan.addIngredient(ingredient);
        }

        for (Recipe recipe : recipes) {
            mealPlan.addRecipe(recipe);
        }

        // Returns the MealPlan object.
        listener.onGetMealPlanResult(mealPlan, true);
    }

    /**
     * Deletes the MealPlan object with the given id from the db.
     *
     * @param mealPlan the MealPlan object to be deleted.
     * @param listener the OnDeleteMealPlanListener object to handle the result.
     */
    public void delMealPlan(MealPlan mealPlan,
                            OnDeleteMealPlanListener listener) {
        if (mealPlan == null) {
            listener.onDeleteMealPlanResult(null, false);
            return;
        }

        DocumentReference mealPlanRef =
                this.mealPlanCollection.document(mealPlan.getId());

        mealPlanRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    delMealPlanAsync(mealPlan, mealPlanRef, listener);
                }
            }
        });
    }

    public void delMealPlanAsync(MealPlan mealPlan,
                                 DocumentReference mealPlanRef,
                                 OnDeleteMealPlanListener listener) {
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

    public void delMealPlanHelper(MealPlan mealPlan,
                                  DocumentReference mealPlanRef,
                                  OnDeleteMealPlanListener listener) {
        mealPlanRef.delete().addOnSuccessListener(success -> {
            listener.onDeleteMealPlanResult(mealPlan, true);
        }).addOnFailureListener(failure -> {
            listener.onDeleteMealPlanResult(mealPlan, false);
        });
    }

    /**
     * Get all MealPlan objects from the db.
     *
     * @param listener the OnGetMealPlansListener object to handle the result.
     */
    public void getMealPlans(OnGetMealPlansListener listener) {
        this.mealPlanCollection.get().addOnSuccessListener(mealPlans -> {
            ArrayList<MealPlan> mealPlanList = new ArrayList<>();
            if (mealPlans.size() == 0) {
                listener.onGetMealPlansResult(mealPlanList, true);
                return;
            }
            for (DocumentSnapshot mealPlanDoc : mealPlans) {
                getMealPlan(mealPlanDoc, (foundMealPlan, success) -> {
                    mealPlanList.add(foundMealPlan);
                    if (mealPlanList.size() == mealPlans.size()) {
                        listener.onGetMealPlansResult(mealPlanList, true);
                    }
                });
            }
        }).addOnFailureListener(failure -> {
            listener.onGetMealPlansResult(null, false);
        });
    }

    /**
     * Updates the MealPlan object in the DB
     *
     * @param mealPlan the MealPlan object to be updated.
     * @param listener the OnUpdateMealPlanListener object to handle the result.
     */
    public void updateMealPlan(MealPlan mealPlan,
                               OnUpdateMealPlanListener listener) {

        ArrayList<HashMap<String, Object>> mealPlanIngredients =
                new ArrayList<>();
        ArrayList<DocumentReference> mealPlanRecipes = new ArrayList<>();
        AtomicInteger counter = new AtomicInteger(0);
        Integer numOfIngredientsAndRecipes =
                mealPlan.getIngredients().size() + mealPlan.getRecipes().size();

        if (numOfIngredientsAndRecipes == 0) {
            updateMealPlanHelper(mealPlan, listener, mealPlanIngredients,
                    mealPlanRecipes);
            // listener.onAddMealPlanResult(null, false);
        }

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
                                updateMealPlanAsync(mealPlan, listener,
                                        mealPlanIngredients, mealPlanRecipes);
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
                                            listener.onUpdateMealPlanResult(
                                                    null, false);
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
                                            updateMealPlanAsync(mealPlan,
                                                    listener,
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
                    updateMealPlanAsync(mealPlan, listener, mealPlanIngredients,
                            mealPlanRecipes);
                }
            } else {
                // If the recipe does not exist, we add it to the db.
                recipeDB.addRecipe(recipe, (addedRecipe, success) -> {
                    if (addedRecipe == null) {
                        // Checks if adding a new Recipe was successful.
                        listener.onUpdateMealPlanResult(null, false);
                        return;
                    }
                    recipe.setId(addedRecipe.getId());

                    counter.addAndGet(1);
                    // Adds DocumentReference to the ArrayList.
                    mealPlanRecipes.add(
                            recipeDB.getDocumentReference(addedRecipe.getId()));

                    if (counter.get() == numOfIngredientsAndRecipes) {
                        updateMealPlanAsync(mealPlan, listener,
                                mealPlanIngredients, mealPlanRecipes);
                    }
                });
            }
        }
    }

    public void updateMealPlanAsync(MealPlan mealPlan,
                                    OnUpdateMealPlanListener listener,
                                    ArrayList<HashMap<String, Object>> mealPlanIngredients,
                                    ArrayList<DocumentReference> mealPlanRecipes) {
        getMealPlan(mealPlan.getId(), (foundMealPlan, success) -> {
            if (foundMealPlan == null) {
                listener.onUpdateMealPlanResult(null, false);
                return;
            } else {
                updateMealPlanAsync1(mealPlan, foundMealPlan, listener,
                        mealPlanIngredients, mealPlanRecipes);
            }
        });
    }

    public void updateMealPlanAsync1(MealPlan mealPlan, MealPlan foundMealPlan,
                                     OnUpdateMealPlanListener listener,
                                     ArrayList<HashMap<String, Object>> mealPlanIngredients,
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
                    updateMealPlanAsync2(mealPlan, foundMealPlan, listener,
                            mealPlanIngredients, mealPlanRecipes);
                }
            });
        }

        for (Recipe recipe : mealPlan.getRecipes()) {
            recipeDB.updateReferenceCount(recipe, 1, (i1, s1) -> {
                counter.addAndGet(1);
                if (counter.get() == numOfIngredientsAndRecipes) {
                    updateMealPlanAsync2(mealPlan, foundMealPlan, listener,
                            mealPlanIngredients, mealPlanRecipes);
                }
            });
        }
    }

    public void updateMealPlanAsync2(MealPlan mealPlan, MealPlan foundMealPlan,
                                     OnUpdateMealPlanListener listener,
                                     ArrayList<HashMap<String, Object>> mealPlanIngredients,
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
                    updateMealPlanAsync3(mealPlan, listener,
                            mealPlanIngredients, mealPlanRecipes);
                }
            });
        }

        for (Recipe recipe : foundMealPlan.getRecipes()) {
            recipeDB.updateReferenceCount(recipe, -1, (i1, s1) -> {
                counter.addAndGet(1);
                if (counter.get() == numOfIngredientsAndRecipes) {
                    updateMealPlanAsync3(mealPlan, listener,
                            mealPlanIngredients, mealPlanRecipes);
                }
            });
        }
    }

    public void updateMealPlanAsync3(MealPlan mealPlan,
                                     OnUpdateMealPlanListener listener,
                                     ArrayList<HashMap<String, Object>> mealPlanIngredients,
                                     ArrayList<DocumentReference> mealPlanRecipes) {
        AtomicInteger counter = new AtomicInteger(0);
        Integer numRecipes = mealPlan.getRecipes().size();

        if (numRecipes == 0){
            updateMealPlanHelper(mealPlan, listener,
                            mealPlanIngredients, mealPlanRecipes);
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
     * @param listener            the OnUpdateMealPlanListener object to handle the result.
     * @param mealPlanIngredients the ArrayList of Ingredient objects to be updated.
     * @param mealPlanRecipes     the ArrayList of Recipe objects to be updated.
     */
    public void updateMealPlanHelper(MealPlan mealPlan,
                                     OnUpdateMealPlanListener listener,
                                     ArrayList<HashMap<String, Object>> mealPlanIngredients,
                                     ArrayList<DocumentReference> mealPlanRecipes) {
        // Get reference to the MealPlan document with the given id
        // so that we can perform update() on it.
        DocumentReference mealPlanRef =
                this.mealPlanCollection.document(mealPlan.getId());

        // Update the MealPlan document in the db.
        mealPlanRef.update("title", mealPlan.getTitle(), "category",
                        mealPlan.getCategory(), "eat date", mealPlan.getEatDate(),
                        "servings", mealPlan.getServings(), "ingredients",
                        mealPlanIngredients, "recipes", mealPlanRecipes)
                .addOnSuccessListener(success -> {
                    listener.onUpdateMealPlanResult(mealPlan, true);
                }).addOnFailureListener(failure -> {
                    listener.onUpdateMealPlanResult(mealPlan, false);
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

    /**
     * This interface is used to handle the result of
     * adding MealPlan to the db.
     */
    public interface OnAddMealPlanListener {
        /**
         * Called when addMealPlan returns a result.
         *
         * @param mealPlan the MealPlan object added to the db,
         *                 null if the add operation failed.
         * @param success  true if the add operation was successful,
         *                 false otherwise.
         */
        void onAddMealPlanResult(MealPlan mealPlan, boolean success);
    }

    /**
     * This interface is used to handle the result of
     * deleting the chosen MealPlan object from the db.
     */
    public interface OnDeleteMealPlanListener {
        /**
         * Called when deleteMealPlan returns a result.
         *
         * @param mealPlan the MealPlan object deleted from the db,
         *                 null if the delete operation failed.
         * @param success  true if the delete operation was successful,
         *                 false otherwise.
         */
        void onDeleteMealPlanResult(MealPlan mealPlan, boolean success);
    }

    /**
     * This interface is used to handle the result of
     * getting all the MealPlan objects from the db.
     */
    public interface OnGetMealPlansListener {
        /**
         * Called when getMealPlans returns a result.
         *
         * @param mealPlans the ArrayList of MealPlan objects
         *                  retrieved from the db,
         *                  null if the get operation failed.
         * @param success   true if the get operation was successful,
         *                  false otherwise.
         */
        void onGetMealPlansResult(ArrayList<MealPlan> mealPlans,
                                  boolean success);
    }


    /**
     * This interface is used to handle the result of
     * finding the MealPlan object in the db.
     */
    public interface OnGetMealPlanListener {
        /**
         * Called when getMealPlan returns a result.
         *
         * @param mealPlan the MealPlan object found in the db,
         *                 null if the get operation failed.
         * @param success  true if the get operation was successful,
         *                 false otherwise.
         */
        void onGetMealPlanResult(MealPlan mealPlan, boolean success);
    }

    /**
     * This interface is used to handle the result of
     * updating the chosen MealPlan object in the db.
     */
    public interface OnUpdateMealPlanListener {
        /**
         * Called when updateMealPlan returns a result.
         *
         * @param mealPlan the MealPlan object updated in the db,
         *                 null if the update operation failed.
         * @param success  true if the update operation was successful,
         *                 false otherwise.
         */
        void onUpdateMealPlanResult(MealPlan mealPlan, boolean success);
    }
}