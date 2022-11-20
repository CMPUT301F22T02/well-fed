package com.example.wellfed.recipe;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

import android.content.Context;
import android.util.Log;

import com.example.wellfed.common.DBConnection;
import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.ingredient.IngredientDB;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The RecipeDB class is used to store and retrieve recipe data from
 * the Firebase Firestore database.
 */
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

    /**
     * Interface for listeners with methods that are called upon adding a recipe is completed
     */
    public interface OnRecipeDone {
        public void onAddRecipe(Recipe recipe, Boolean success);
    }

    /**
     * Interface for listeners with methods that are called upon adding an ingredient to a recipe
     */
    public interface OnRecipeIngredientAdded {
        public void onRecipeIngredientAdd(Ingredient ingredient, int totalAdded);
    }

    /**
     * Create a RecipeDB object
     *
     * @param connection the database connection for the RecipeDB
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
                        addRecipeHelper(recipeMap, recipeIngredients, recipe, listener);
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
                            addRecipeHelper(recipeMap, recipeIngredients, recipe, listener);
                        }
                    });
                }
            });
        }

    }

    /**
     * Helper function for adding a recipe to the database.
     * @param recipeMap     A hash map representing all of the data elements associated with a recipe
     * @param ingredients   A list of hash maps representing all of data elements associated with
     *                      ingredients inside of a recipe
     * @param recipe        The recipe to be added to the database
     * @param listener      A listener that will be notified when the database add is completed
     */
    public void addRecipeHelper(HashMap<String, Object> recipeMap, ArrayList<HashMap<String, Object>> ingredients,
                                Recipe recipe, OnRecipeDone listener) {
        recipeMap.put("ingredients", ingredients);
        recipeMap.put("title", recipe.getTitle());
        recipeMap.put("servings", recipe.getServings());
        recipeMap.put("category", recipe.getCategory());
        recipeMap.put("comments", recipe.getComments());
        recipeMap.put("photograph", recipe.getPhotograph());
        recipeMap.put("preparation-time", recipe.getPrepTimeMinutes());

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

    /**
     * Retrieves a recipe from the Firebase Firestore database.
     *
     * @param id        The ID of the recipe to retrieve
     * @param listener  The listener that will be notified upon completion of the retrieval
     */
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

                });
    }

    /**
     * Updates a recipe in the Firebase Firestore database.
     *
     * @param recipe    The recipe, which contains updated information from it's previous state
     * @param listener  The listener to be notified after the database update is completed
     */
    public void updateRecipe(Recipe recipe, OnRecipeDone listener) {
        String id = recipe.getId();
        addRecipe(recipe, (addedRecipe, success)->{
            if (addedRecipe == null){
                listener.onAddRecipe(null, false);
                return;
            }

            delRecipe(id, (deletedRecipe, success1)->{
                listener.onAddRecipe(addedRecipe, true);
            });
        });
    }



    /**
     * Deletes a recipe document with the id  from the collection
     * of recipes
     *
     * @param id        The id of recipe document we want to delete
     * @param listener  The listener to be notified after the database deletion is completed
     */
    public void delRecipe(String id, OnRecipeDone listener){
        if (id == null){
            listener.onAddRecipe(null, false);
        }

        DocumentReference recipeRef = this.collection.document(id);
        recipeRef.delete()
                .addOnSuccessListener(r->{
                    listener.onAddRecipe(new Recipe(id), true);
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


    /**
     * Converts a database snapshot of a recipe into a Recipe object
     *
     * @param doc   the DocumentSnapshot of a recipe document to be converted
     * @return the Recipe object created by the conversion
     */
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

    /**
     * Gets the collection that the RecipeDB is associated with.
     *
     * @return the collection as a Query
     */
    public Query getQuery() {
        return this.collection;
    }


}

