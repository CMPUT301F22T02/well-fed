package com.example.wellfed.mealplan;

import com.example.wellfed.common.DBConnection;
import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.ingredient.IngredientDB;
import com.example.wellfed.recipe.Recipe;
import com.example.wellfed.recipe.RecipeDB;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

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
     * @param mealPlan the MealPlan object to be added to the db.
     * @param listener the OnAddMealPlanListener object to handle the result.
     */
    public void addMealPlan(MealPlan mealPlan, OnAddMealPlanListener listener) {
        // Stores each ingredient as a HashMap with fields:
        // ingredientRef, amount & unit.
        ArrayList<HashMap<String, Object>> mealPlanIngredients = new ArrayList<>();

        for (Ingredient i: mealPlan.getIngredients()) {
            // Gets ingredient from db.
            ingredientDB.getIngredient(i, (foundIngredient, success1) -> {
                // Initializes a mapping for the ingredient.
                HashMap<String, Object> ingredientMap = new HashMap<>();

                // If the ingredient already exists in our Ingredient collection.
                if (foundIngredient != null) {
                    DocumentReference ingredientRef = ingredientDB.getDocumentReference(foundIngredient);
                    i.setId(foundIngredient.getId());

                    ingredientMap.put("ingredientRef", ingredientRef);
                    ingredientMap.put("amount", i.getAmount());
                    ingredientMap.put("unit", i.getUnit());
                } else {
                    // If the ingredient does not exist in our Ingredient collection,
                    // we will create a new ingredient for it.
                    ingredientDB.addIngredient(i, (addedIngredient, success2) -> {
//                        if (addedIngredient == null) {
//                            listener.onAddMealPlanResult(null, false);
//                            return;
//                        }
                        i.setId(addedIngredient.getId());

                        ingredientMap.put("ingredientRef", ingredientDB.getDocumentReference(addedIngredient));
                        ingredientMap.put("amount", i.getAmount());
                        ingredientMap.put("unit", i.getUnit());
                    });
                }
            });
        }

        // Stores references to recipes in the MealPlan object.
        ArrayList<DocumentReference> mealPlanRecipes = new ArrayList<>();
        for (Recipe r: mealPlan.getRecipes()) {
            if (r.getId() != null) {
                // If the recipe has an id, gets the recipe's reference from db.
                DocumentReference recipeRef = recipeDB.getDocumentReference(r.getId());
                mealPlanRecipes.add(recipeRef);
            } else {
                // If the recipe does not exist, we add it to the db.
                recipeDB.addRecipe(r, (addedRecipe, success) -> {
                    if (addedRecipe == null) {
                        listener.onAddMealPlanResult(null, false);
                        return;
                    }
                    r.setId(addedRecipe.getId());

                    mealPlanRecipes.add(recipeDB.getDocumentReference(addedRecipe.getId()));
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
        this.mealPlanCollection
            .add(mealPlanMap)
            .addOnSuccessListener(addedMealPlanDoc -> {
                // Sets the document id for the MealPlan object.
                mealPlan.setId(addedMealPlanDoc.getId());
                listener.onAddMealPlanResult(mealPlan, true);
            })
            .addOnFailureListener(failure -> {
                listener.onAddMealPlanResult(null, false);
            });
    }

    /**
     * Gets the MealPlan object with the given id from the db.
     * @param id the id of the MealPlan object to be retrieved.
     * @param listener the OnGetMealPlanListener object to handle the result.
     */
    public void getMealPlan(String id, OnGetMealPlanListener listener) {
        if (id == null) {
            listener.onGetMealPlanResult(null, false);
            return;
        }

        // Get reference to the MealPlan document with the given id.
        DocumentReference mealPlanRef = this.mealPlanCollection.document(id);
        mealPlanRef.get()
            .addOnSuccessListener(mealPlanDoc -> {

                // Initializes a new MealPlan object and sets its fields.
                MealPlan mealPlan = new MealPlan(mealPlanDoc.getString("title"));
                mealPlan.setId(mealPlanDoc.getId());
                mealPlan.setCategory(mealPlanDoc.getString("category"));
                mealPlan.setEatDate(mealPlanDoc.getDate("eat date"));
                mealPlan.setServings(Objects.requireNonNull(mealPlanDoc.getLong("servings")).intValue());

                // Initializes an empty ArrayList to store Ingredient objects.
                ArrayList<Ingredient> ingredients = new ArrayList<>();

                // Get the list of ingredients from the MealPlan document.
                ArrayList<HashMap<String, Object>> mealPlanIngredients = (ArrayList<HashMap<String, Object>>)
                    mealPlanDoc.get("ingredients");

                // Iterate over the ArrayList of HashMaps and produce Ingredient objects.
                for (HashMap<String, Object> ingredientMap: mealPlanIngredients) {
                    DocumentReference ingredientRef = (DocumentReference) ingredientMap.get("ingredientRef");
                    ingredientDB.getIngredient(ingredientRef, (foundIngredient, success) -> {
                        // Sets amount and unit for the ingredient found in the db via ingredientRef.
                        foundIngredient.setAmount((Double) ingredientMap.get("amount"));
                        foundIngredient.setUnit((String) ingredientMap.get("unit"));

                        // Adds the ingredient to the ArrayList.
                        ingredients.add(foundIngredient);
                    });
                }

                // Adds ingredients to the MealPlan object.
                for (Ingredient i: ingredients) {
                    mealPlan.addIngredient(i);
                }

                // Initializes an empty ArrayList to store Recipe objects.
                ArrayList<Recipe> recipes = new ArrayList<>();

                // Iterate over the ArrayList of DocumentReferences and produce Recipe objects
                // using the method implemented in RecipeDB.
                for (DocumentReference recipeRef: (ArrayList<DocumentReference>) mealPlanDoc.get("recipes")) {
                    recipeDB.getRecipe(recipeRef.getId(), (foundRecipe, success) -> {
                        recipes.add(foundRecipe);
                    });
                }

                // Adds recipes to the MealPlan object.
                for (Recipe r: recipes) {
                    mealPlan.addRecipe(r);
                }

                // Return the MealPlan object.
                listener.onGetMealPlanResult(mealPlan, true);
            })
            // If the MealPlan document is not found, return null.
            .addOnFailureListener(failure -> {
                listener.onGetMealPlanResult(null, false);
            });
    }

    /**
     * Deletes the MealPlan object with the given id from the db.
     *
     * @param mealPlan
     * @param listener the OnDeleteMealPlanListener object to handle the result.
     */
    public void delMealPlan(MealPlan mealPlan, OnDeleteMealPlanListener listener) {
        // Get reference to the MealPlan document with the given id.
        DocumentReference mealPlanRef =
            this.mealPlanCollection.document(mealPlan.getId());
        mealPlanRef.delete()
            .addOnSuccessListener(success -> {
                listener.onDeleteMealPlanResult(mealPlan, true);
            })
            .addOnFailureListener(failure -> {
                listener.onDeleteMealPlanResult(mealPlan, false);
            });
    }

    /**
     * Updates
     *
     * @param mealPlan the MealPlan object to be updated.
     * @param listener the OnUpdateMealPlanListener object to handle the result.
     */
    public void updateMealPlan(MealPlan mealPlan, OnUpdateMealPlanListener listener) throws Exception{
        if (mealPlan == null) {
            listener.onUpdateMealPlanResult(null, false);
        }

        // Adds the new MealPlan object to the db.
        addMealPlan(mealPlan, (addedMealPlan, success1) -> {
            // Check if the MealPlan object was added successfully.
            if (addedMealPlan == null) {
                listener.onUpdateMealPlanResult(null, false);
            } else {
                listener.onUpdateMealPlanResult(addedMealPlan, true);
            }

            // Deletes the old MealPlan object.
            delMealPlan(mealPlan, (deletedMealPlan, success2) -> {
                // Check if the MealPlan object was deleted successfully.
                if (deletedMealPlan == null) {
                    listener.onUpdateMealPlanResult(null, false);
                } else {
                    listener.onUpdateMealPlanResult(addedMealPlan, true);
                }
            });
        });
    }


    /**
     * Fet the DocumentReference object for the MealPlan document with the given id.
     *
     * @param id the id of the MealPlan document.
     * @return the DocumentReference object for the MealPlan document.
     */
    public DocumentReference getMealPlanDocumentReference(String id) {
        return this.mealPlanCollection.document(id);
    }

    /** Gets a query for MealPlans in the db.
     * @return the query for MealPlans in the db.
     */
    public Query getQuery() {
        return this.mealPlanCollection;
    }
}