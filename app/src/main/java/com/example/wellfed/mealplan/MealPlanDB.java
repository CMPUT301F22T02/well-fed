package com.example.wellfed.mealplan;

import com.example.wellfed.common.DBConnection;
import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.ingredient.IngredientDB;
import com.example.wellfed.recipe.Recipe;
import com.example.wellfed.recipe.RecipeDB;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
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

        for (Ingredient i : mealPlan.getIngredients()) {
            // Gets ingredient from db.
            ingredientDB.getIngredient(i, (foundIngredient, success1) -> {
                // Initializes a mapping for the ingredient.
                HashMap<String, Object> ingredientMap = new HashMap<>();

                // If the ingredient already exists in our Ingredient
                // collection.
                if (foundIngredient != null) {
                    DocumentReference ingredientRef =
                            ingredientDB.getDocumentReference(foundIngredient);
                    i.setId(foundIngredient.getId());

                    ingredientMap.put("ingredientRef", ingredientRef);
                    ingredientMap.put("amount", i.getAmount());
                    ingredientMap.put("unit", i.getUnit());
                } else {
                    // If the ingredient does not exist in our Ingredient
                    // collection,
                    // we will create a new ingredient for it.
                    ingredientDB.addIngredient(i,
                            (addedIngredient, success2) -> {
                                //                        if (addedIngredient
                                //                        == null) {
                                //                            listener
                                //                            .onAddMealPlanResult(null, false);
                                //                            return;
                                //                        }
                                i.setId(addedIngredient.getId());

                                ingredientMap.put("ingredientRef",
                                        ingredientDB.getDocumentReference(
                                                addedIngredient));
                                ingredientMap.put("amount", i.getAmount());
                                ingredientMap.put("unit", i.getUnit());
                            });
                }
            });
        }

        // Stores references to recipes in the MealPlan object.
        ArrayList<DocumentReference> mealPlanRecipes = new ArrayList<>();
        for (Recipe r : mealPlan.getRecipes()) {
            if (r.getId() != null) {
                // If the recipe has an id, gets the recipe's reference from db.
                DocumentReference recipeRef =
                        recipeDB.getDocumentReference(r.getId());
                mealPlanRecipes.add(recipeRef);
            } else {
                // If the recipe does not exist, we add it to the db.
                recipeDB.addRecipe(r, (addedRecipe, success) -> {
                    if (addedRecipe == null) {
                        listener.onAddMealPlanResult(null, false);
                        return;
                    }
                    r.setId(addedRecipe.getId());

                    mealPlanRecipes.add(
                            recipeDB.getDocumentReference(addedRecipe.getId()));
                });
            }
        }

        // Initialize MealPlan document mapping.
        HashMap<String, Object> mealPlanMap = new HashMap<>();
        // Fill the map.
        mealPlanMap.put("title", mealPlan.getTitle());
        mealPlanMap.put("category", mealPlan.getCategory());
        mealPlanMap.put("eat date", mealPlan.getEatDate());
        mealPlanMap.put("servings", mealPlan.getServings());
        mealPlanMap.put("ingredients", mealPlanIngredients);
        mealPlanMap.put("recipes", mealPlanRecipes);

        // Adds the MealPlan mapping to the db.
        this.mealPlanCollection.add(mealPlanMap)
                .addOnSuccessListener(addedMealPlanDoc -> {
                    // Sets the document id for the MealPlan object.
                    mealPlan.setId(addedMealPlanDoc.getId());
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
     * Gets the MealPlan object with the given id from the db.
     *
     * @param snapshot the snapshot of the MealPlan object to be retrieved.
     * @param listener the OnGetMealPlanListener object to handle the result.
     */
    public void getMealPlan(DocumentSnapshot snapshot,
                            OnGetMealPlanListener listener) {
        // Initializes a new MealPlan object and sets its fields.
        MealPlan mealPlan = new MealPlan(snapshot.getString("title"));
        mealPlan.setId(snapshot.getId());
        mealPlan.setCategory(snapshot.getString("category"));
        mealPlan.setEatDate(snapshot.getDate("eat date"));
        //                    TODO: refactor mealPlan to use Long instead
        //                     of Integer
        mealPlan.setServings(
                Objects.requireNonNull(snapshot.getLong("servings"))
                        .intValue());

//        TODO: you have to fix this, this won't work, it is running in parallel
//        TODO: and you are trying to add to the mealPlan object before finish
        // Initializes an empty ArrayList to store Ingredient objects.
        ArrayList<Ingredient> ingredients = new ArrayList<>();

        // Get the list of ingredients from the MealPlan document.
        ArrayList<HashMap<String, Object>> mealPlanIngredients =
                (ArrayList<HashMap<String, Object>>) snapshot.get(
                        "ingredients");

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

                        // Adds the ingredient to the ArrayList.
                        ingredients.add(foundIngredient);
                    });
        }

        // Adds ingredients to the MealPlan object.
        for (Ingredient i : ingredients) {
            mealPlan.addIngredient(i);
        }

        // Initializes an empty ArrayList to store Recipe objects.
        ArrayList<Recipe> recipes = new ArrayList<>();

        // Iterate over the ArrayList of DocumentReferences and produce
        // Recipe objects
        // using the method implemented in RecipeDB.
        for (DocumentReference recipeRef :
                (ArrayList<DocumentReference>) snapshot.get(
                "recipes")) {
            recipeDB.getRecipe(recipeRef.getId(), (foundRecipe, success) -> {
                recipes.add(foundRecipe);
            });
        }

        // Adds recipes to the MealPlan object.
        for (Recipe r : recipes) {
            mealPlan.addRecipe(r);
        }

        // Return the MealPlan object.
        listener.onGetMealPlanResult(mealPlan, true);

    }

    /**
     * Deletes the MealPlan object with the given id from the db.
     *
     * @param mealPlan
     * @param listener the OnDeleteMealPlanListener object to handle the result.
     */
    public void delMealPlan(MealPlan mealPlan,
                            OnDeleteMealPlanListener listener) {
        DocumentReference mealPlanRef =
                this.mealPlanCollection.document(mealPlan.getId());
        mealPlanRef.delete().addOnSuccessListener(success -> {
            listener.onDeleteMealPlanResult(mealPlan, true);
        }).addOnFailureListener(failure -> {
            listener.onDeleteMealPlanResult(mealPlan, false);
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
        //        TODO: use update function instead of delete and add
        // Get reference to the MealPlan document with the given id
        // so that we can perform update() on it.
        DocumentReference mealPlanRef =
                this.mealPlanCollection.document(mealPlan.getId());

        // Using the helper functions, convert ingredients & recipes
        // stored in the MealPlan object to a format that can be
        // stored in the db.
        ArrayList<HashMap<String, Object>> mealPlanIngredients =
                getMealPlanIngredients(mealPlan);
        ArrayList<DocumentReference> mealPlanRecipes =
                getMealPlanRecipes(mealPlan);

        mealPlanRef.update(
                "title", mealPlan.getTitle(),
                "category", mealPlan.getCategory(),
                "eat date", mealPlan.getEatDate(),
                "servings", mealPlan.getServings(),
                "ingredients", mealPlanIngredients,
                "recipes", mealPlanRecipes
        ).addOnSuccessListener(success -> {
            listener.onUpdateMealPlanResult(mealPlan, true);
        }).addOnFailureListener(failure -> {
            listener.onUpdateMealPlanResult(mealPlan, false);
        });
    }

    /**
     * Helper function that gets the list of ingredients from the given
     * MealPlan object and returns an ArrayList of HashMaps that contain
     * the ingredientRef, amount & unit of each ingredient.
     *
     * @param mealPlan the MealPlan object.
     * @return ArrayList of HashMaps of converted ingredients.
     */
    public ArrayList<HashMap<String, Object>> getMealPlanIngredients(MealPlan mealPlan) {
        // Initializes an empty ArrayList to store the HashMaps.
        ArrayList<HashMap<String, Object>> mealPlanIngredients = new ArrayList<>();

        // AtomicInteger used later as a lock to ensure that the
        // ArrayList of HashMaps is returned only after all the
        // ingredients have been converted.
        AtomicInteger counter = new AtomicInteger(0);
        for (Ingredient ingredient: mealPlan.getIngredients()) {
            ingredientDB.getIngredient(ingredient, (foundIngredient, success) -> {
                // If the ingredient already exists in Ingredient collection.
                if (foundIngredient != null) {
                    // Initializes HashMap for mapping.
                    HashMap<String, Object> ingredientMap = new HashMap<>();

                    // Sets fields.
                    ingredientMap.put("ingredientRef",
                            ingredientDB.getDocumentReference(foundIngredient));
                    ingredientMap.put("amount", ingredient.getAmount());
                    ingredientMap.put("unit", ingredient.getUnit());

                    // Atomically adds the given value to the current value,
                    // with memory effects as specified by VarHandle.getAndAdd.
                    counter.addAndGet(1);
                    mealPlanIngredients.add(ingredientMap);
                } else {
                    // If the ingredient does not exist in our Ingredient collection,
                    // we will create a new ingredient for it.
                    ingredientDB.addIngredient(ingredient, (addedIngredient, success1) -> {
                        if (addedIngredient == null) {
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
                        mealPlanIngredients.add(ingredientMap);
                    });
                }
            });
        }

        return mealPlanIngredients;
    }

    /**
     * Helper function that gets the list of recipes from the given
     * MealPlan object and returns an ArrayList of DocumentReferences.
     *
     * @param mealPlan the MealPlan object.
     * @return ArrayList of DocumentReferences of converted recipes.
     */
    public ArrayList<DocumentReference> getMealPlanRecipes(MealPlan mealPlan) {
        // Initializes an empty ArrayList to store the DocumentReferences.
        ArrayList<DocumentReference> mealPlanRecipes = new ArrayList<>();

        // AtomicInteger used later as a lock to ensure that the
        // ArrayList of DocumentReferences is returned only after all the
        // recipes have been converted.
        AtomicInteger counter = new AtomicInteger(0);
        for (Recipe recipe : mealPlan.getRecipes()) {
            if (recipe.getId() != null) {
                // If the recipe has an id, gets the recipe's reference from db.
                DocumentReference recipeRef =
                        recipeDB.getDocumentReference(recipe.getId());

                counter.addAndGet(1);
                mealPlanRecipes.add(recipeRef);
            } else {
                // If the recipe does not exist, we add it to the db.
                recipeDB.addRecipe(recipe, (addedRecipe, success) -> {
                    if (addedRecipe == null) {
                        return;
                    }
                    recipe.setId(addedRecipe.getId());

                    counter.addAndGet(1);
                    // Adds DocumentReference to the ArrayList.
                    mealPlanRecipes.add(
                            recipeDB.getDocumentReference(addedRecipe.getId()));
                });
            }
        }

        return mealPlanRecipes;
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