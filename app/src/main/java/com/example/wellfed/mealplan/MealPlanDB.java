package com.example.wellfed.mealplan;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

import android.media.Image;
import android.util.Log;

import com.example.wellfed.common.DBConnection;
import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.ingredient.StorageIngredient;
import com.example.wellfed.ingredient.StorageIngredientDB;
import com.example.wellfed.recipe.Recipe;
import com.example.wellfed.recipe.RecipeDB;
import com.example.wellfed.recipe.RecipeIngredientDB;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

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
     * Holds the instance of the Firebase Firestore DB.
     */
    private FirebaseFirestore db;
    /**
     * Holds the collection for the users MealPlan collection in DB.
     */
    private CollectionReference collection;
}
//
//public class MealPlanDB {
//
//    /**
//     * Holds an instance of the Firebase Firestore database
//     */
//    private final FirebaseFirestore db;
//
//    /**
//     * Holds a collection to the Recipes stored in the FireStore db database
//     */
//    final CollectionReference mealCollection;
//
//    /**
//     * Holds a collection to Ingredients stored in the FireStore db database
//     */
//    final StorageIngredientDB ingredientDB;
//
//    /**
//     * Holds a collection to Ingredients stored in the FireStore db database
//     */
//    final RecipeDB recipeDB;
//
//
//    /**
//     * Create a MealPlanDB object
//     */
//    public MealPlanDB(){
//        db = FirebaseFirestore.getInstance();
//        this.mealCollection = db.collection("MealPlans");
//        this.recipeDB = new RecipeDB();
//        this.ingredientDB = new StorageIngredientDB();
//    }
//
//    /**
//     * Adds new MealPlan to the database.
//     * @param mealPlan the MealPlan to add to the database. Adding it to the DB
//     *                 will assign the id parameter of the MealPlan.
//     * @return A string containing the ID of the MealPlan added.
//     * @throws Exception if any ingredient or recipe is not in the database
//     */
//    public String addMealPlan(MealPlan mealPlan) throws Exception {
//        CountDownLatch addLatch = new CountDownLatch(1);
//        ArrayList<DocumentReference> mealPlanIngredientDocuments = new ArrayList<>();
//        ArrayList<DocumentReference> mealPlanRecipeDocuments = new ArrayList<>();
//
//        for(Ingredient i: mealPlan.getIngredients()) {
//            if (i.getId() != null) {
//                DocumentReference newDocumentReference = ingredientDB.getDocumentReference(i.getId());
//                mealPlanIngredientDocuments.add(newDocumentReference);
//            }
//            else{
//                throw new Exception("Ingredient cannot be null.");
//            }
//        }
//
//        for(Recipe r: mealPlan.getRecipes()) {
//            if (r.getId() != null) {
//                DocumentReference newDocumentReference = recipeDB.getDocumentReference(r.getId());
//                mealPlanRecipeDocuments.add(newDocumentReference);
//            }
//            else{
//                throw new Exception("Ingredient cannot be null.");
//            }
//        }
//
//        Map<String, Object> mealPlanMap = new HashMap<>();
//
//        String newMealID = mealCollection.document().getId();
//
//        mealPlanMap.put("title", mealPlan.getTitle());
//        mealPlanMap.put("category", mealPlan.getCategory());
//        mealPlanMap.put("eat-date", mealPlan.getEatDate());
//        mealPlanMap.put("servings", mealPlan.getServings());
//        mealPlanMap.put("ingredients", mealPlanIngredientDocuments);
//        mealPlanMap.put("recipes", mealPlanRecipeDocuments);
//
//        DocumentReference newMealPlan = mealCollection.document(newMealID);
//
//        db.runTransaction((Transaction.Function<Void>) transaction -> {
//            transaction.set(newMealPlan, mealPlanMap);
//            return null;
//        }).addOnSuccessListener(unused -> {
//            Log.d(TAG, "onSuccess: ");
//            addLatch.countDown();
//        }).addOnFailureListener(e -> {
//            Log.d(TAG, "onFailure: ");
//            addLatch.countDown();
//        });
//
//        addLatch.await();
//
//        mealPlan.setId(newMealID);
//
//        return newMealID;
//    }
//
//    /**
//     * Deletes a MealPlan document with the id from the collection
//     * of recipes
//     * @param id The id of MealPlan document we want to delete
//     * @throws InterruptedException If the transaction in the method was not complete
//     */
//    public void deleteMealPlan(String id) throws InterruptedException {
//        CountDownLatch deleteLatch = new CountDownLatch(1);
//
//        if(id.equals(NULL)){
//            Log.d("Delete Recipe", "The Recipe does not have an id");
//            return;
//        }
//
//        DocumentReference mealToDelete = mealCollection.document(id);
//
//        db.runTransaction((Transaction.Function<Void>) transaction -> {
//
//            transaction.delete(mealToDelete);
//
//            return null;
//        }).addOnSuccessListener(unused -> {
//            Log.d(TAG, "onSuccess: ");
//            deleteLatch.countDown();
//        }).addOnFailureListener(e -> {
//            Log.d(TAG, "onFailure: ");
//            deleteLatch.countDown();
//        });
//
//        deleteLatch.await();
//    }
//
//    /**
//     * Updates the corresponding MealPlan document in the collection with the fields of the
//     * MealPlan object.
//     * @param mealPlan A MealPlan object whose changes we want to push to the collection of MealPlans
//     *                 Note: All of the recipes and ingredients in the MealPlan must be in the DB.
//     * @throws InterruptedException If the transaction in the method was not complete
//     */
//    public void editMealPlan(MealPlan mealPlan) throws Exception {
//        String id = mealPlan.getId();
//
//        CountDownLatch editLatch = new CountDownLatch(1);
//
//        ArrayList<DocumentReference> mealPlanIngredientDocuments = new ArrayList<>();
//        ArrayList<DocumentReference> mealPlanRecipeDocuments = new ArrayList<>();
//
//        for(Ingredient i: mealPlan.getIngredients()) {
//            if (i.getId() != null) {
//                DocumentReference newDocumentReference = ingredientDB.getDocumentReference(i.getId());
//                mealPlanIngredientDocuments.add(newDocumentReference);
//            }
//            else{
//                throw new Exception("Ingredient cannot be null.");
//            }
//        }
//
//        for(Recipe r: mealPlan.getRecipes()) {
//            if (r.getId() != null) {
//                DocumentReference newDocumentReference = recipeDB.getDocumentReference(r.getId());
//                mealPlanRecipeDocuments.add(newDocumentReference);
//            }
//            else{
//                throw new Exception("Ingredient cannot be null.");
//            }
//        }
//
//        Map<String, Object> mealPlanMap = new HashMap<>();
//
//        mealPlanMap.put("title", mealPlan.getTitle());
//        mealPlanMap.put("category", mealPlan.getCategory());
//        mealPlanMap.put("eat-date", mealPlan.getEatDate());
//        mealPlanMap.put("servings", mealPlan.getServings());
//        mealPlanMap.put("ingredients", mealPlanIngredientDocuments);
//        mealPlanMap.put("recipes", mealPlanRecipeDocuments);
//
//
//        DocumentReference newMealPlan = mealCollection.document(id);
//
//        db.runTransaction((Transaction.Function<Void>) transaction -> {
//
//            transaction.update(newMealPlan, mealPlanMap);
//
//            return null;
//        }).addOnSuccessListener(unused -> {
//            Log.d(TAG, "onSuccess: ");
//            editLatch.countDown();
//        }).addOnFailureListener(e -> {
//            Log.d(TAG, "onFailure: ");
//            editLatch.countDown();
//        });
//
//        editLatch.await();
//    }
//
//    /**
//     * Gets a MealPlan from the DB.
//     * @param id A String with the id of the document who MealPlan we want
//     * @return The MealPlan object that corresponds to the document in the collection
//     * If it does not exist then return null
//     * @throws InterruptedException If the transaction in the method was not complete
//     */
//    public MealPlan getMealPlan(String id) throws InterruptedException {
//        CountDownLatch getLatch = new CountDownLatch(1);
//
//        MealPlan mealPlan = new MealPlan(null);
//        mealPlan.setId(id);
//        DocumentReference mealDocument = mealCollection.document(id);
//        final DocumentSnapshot[] mealSnapshot = new DocumentSnapshot[1];
//        db.runTransaction((Transaction.Function<Void>) transaction -> {
//
//            mealSnapshot[0] = transaction.get(mealDocument);
//
//            return null;
//        }).addOnSuccessListener(unused -> {
//            Log.d(TAG, "onSuccess: ");
//            getLatch.countDown();
//        }).addOnFailureListener(e -> {
//            Log.d(TAG, "onFailure: ");
//            getLatch.countDown();
//        });
//
//        getLatch.await();
//
//        if(!mealSnapshot[0].exists()){
//            return null;
//        }
//
//        mealPlan.setTitle(mealSnapshot[0].getString("title"));
//        mealPlan.setCategory(mealSnapshot[0].getString("category"));
//        mealPlan.setEatDate(mealSnapshot[0].getDate("eat-date"));
//        if (mealSnapshot[0].getLong("servings") == null) {
//            mealPlan.setServings(null);
//        } else {
//            mealPlan.setServings(Objects.requireNonNull(mealSnapshot[0].getLong("servings")).intValue());
//        }
//        // getting the ingredients
//        List<DocumentReference> mealPlanIngredients =
//                (List<DocumentReference>) Objects.requireNonNull(mealSnapshot[0].get("ingredients"));
//
//        for(DocumentReference ingredient: mealPlanIngredients){
//            try {
//                Ingredient result = ingredientDB.getStoredIngredient(ingredient.getId());
//                mealPlan.addIngredient(result);
//            }
//            catch(Exception err){
//                Log.d(TAG, "addMealPlan: Failed to get ingredient");
//            }
//        }
//
//        // getting the recipes
//        List<DocumentReference> mealPlanRecipes =
//                (List<DocumentReference>) Objects.requireNonNull(mealSnapshot[0].get("recipes"));
//
//        for(DocumentReference recipe: mealPlanRecipes){
//            try {
//                Recipe result = recipeDB.getRecipe(recipe.getId());
//                mealPlan.addRecipe(result);
//            }
//            catch(Exception err){
//                Log.d(TAG, "addMealPlan: Failed to get recipe with id: " + recipe.getId());
//            }
//        }
//
//        return mealPlan;
//    }
//
//    /**
//     * Makes an ArrayList of MealPlans out of all the documents in the collection of MealPlans
//     * @return ArrayList<MealPlan> The List of all MealPlans contained in the database
//     * @throws InterruptedException If the transaction in the method was not complete
//     */
//    public ArrayList<MealPlan> getMealPlans() throws InterruptedException {
//        CountDownLatch mealsLatch = new CountDownLatch(1);
//
//        ArrayList<MealPlan> meals = new ArrayList<>();
//        db.runTransaction((Transaction.Function<Void>) transaction -> {
//
//            List<DocumentSnapshot> mealSnapshots = mealCollection.get().getResult().getDocuments();
//
//            for(DocumentSnapshot recipeSnapshot: mealSnapshots){
//                MealPlan mealPlan = null;
//                try {
//                    mealPlan = this.getMealPlan(recipeSnapshot.getId());
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                meals.add(mealPlan);
//            }
//
//            return null;
//        }).addOnSuccessListener(unused -> {
//            Log.d(TAG, "onSuccess: ");
//            mealsLatch.countDown();
//        }).addOnFailureListener(e -> {
//            Log.d(TAG, "onFailure: ");
//            mealsLatch.countDown();
//        });
//
//        mealsLatch.await();
//
//        return meals;
//    }
//
//    /**
//     * Get the DocumentReference from MealPlans collection for the given id
//     * @param id The String of the document in MealPlan collection we want
//     * @return DocumentReference of the MealPlan
//     */
//    public DocumentReference getDocumentReference(String id){
//        return mealCollection.document(id);
//    }
//}
