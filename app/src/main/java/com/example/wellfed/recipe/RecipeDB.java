package com.example.wellfed.recipe;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

import android.media.Image;
import android.util.Log;

import com.example.wellfed.ingredient.Ingredient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class RecipeDB {
    /**
     * Holds an instance of the Firebase Firestore database
     */
    private final FirebaseFirestore db;
    /**
     * Holds a collection to the Recipes stored in the FireStore db database
     */
    final CollectionReference recipesCollection;
    /**
     * Holds a collection to the Recipe Ingredients stored in the FireStore db database
     */
    final RecipeIngredientDB recipeIngredientDB;

    /**
     * Create a RecipeDB object
     */
    public RecipeDB(){
        db = FirebaseFirestore.getInstance();
        this.recipesCollection = db.collection("Recipes");
        recipeIngredientDB = new RecipeIngredientDB();
    }

    /**
     * Adds a recipe to the Recipe collection in db and any ingredients
     * not already in Recipe Ingredients. This method will set the Recipe id
     * to the corresponding document id in the collection
     * @param recipe A Recipe object we want to add to the collection of Recipes
     *               and whose id we want to set
     * @return Returns the id of the Recipe document
     * @throws InterruptedException If any transaction in the method was not complete
     */
    public String addRecipe(Recipe recipe) throws InterruptedException {
        CountDownLatch addLatch = new CountDownLatch(1);

        ArrayList<DocumentReference> recipeIngredientDocuments = new ArrayList<>();
        String newRecipeId = recipesCollection.document().getId();

        for(Ingredient i: recipe.getIngredients()) {
            if (i.getId() == null) {
                i.setId(newRecipeId);
                DocumentReference newDocumentReference = recipeIngredientDB.getDocumentReference(recipeIngredientDB.addRecipeIngredient(i));
                recipeIngredientDocuments.add(newDocumentReference);
            }
            else{
                try {
                    recipeIngredientDB.getRecipeIngredient(i.getId());
                }
                catch(Exception err){
                    Log.d(TAG, "addRecipe: Failed to get recipe");
                }
            }
        }

        Map<String, Object> recipeMap = new HashMap<>();



        recipeMap.put("title", recipe.getTitle());
        recipeMap.put("comments", recipe.getComments());
        recipeMap.put("category", recipe.getCategory());
        recipeMap.put("prep-time-minutes", recipe.getPrepTimeMinutes());
        recipeMap.put("servings", recipe.getServings());
        recipeMap.put("photograph", recipe.getPhotograph());
        recipeMap.put("ingredients", recipeIngredientDocuments);

        DocumentReference newRecipe = recipesCollection.document(newRecipeId);

        db.runTransaction((Transaction.Function<Void>) transaction -> {

                    transaction.set(newRecipe, recipeMap);

                    return null;
                })
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "onSuccess: ");
                    addLatch.countDown();
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "onFailure: ");
                    addLatch.countDown();
                });

        addLatch.await();

        recipe.setId(newRecipeId);

        return newRecipeId;
    }

    /**
     * Deletes a recipe document with the id  from the collection
     * of recipes
     * @param id The id of recipe document we want to delete
     * @throws InterruptedException If the transaction in the method was not complete
     */
    public void delRecipe(String id) throws InterruptedException {
        CountDownLatch deleteLatch = new CountDownLatch(1);

        if(id.equals(NULL)){
            Log.d("Delete Recipe", "The Recipe does not have an id");
            return;
        }

        DocumentReference recipeToDelete = recipesCollection.document(id);

        db.runTransaction((Transaction.Function<Void>) transaction -> {

                    transaction.delete(recipeToDelete);

                    return null;
                })
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "onSuccess: ");
                    deleteLatch.countDown();
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "onFailure: ");
                    deleteLatch.countDown();
                });

        deleteLatch.await();
    }

    /**
     * Deletes a recipe document with the id  from the recipe collection (AND Ingredients)
     * of recipes
     * @param id The id of recipe document we want to delete
     * @throws InterruptedException If the transaction in the method was not complete
     */
    public void delRecipeAndIngredient(String id) throws InterruptedException {
        CountDownLatch deleteLatch = new CountDownLatch(1);

        if(id.equals(NULL)){
            Log.d("Delete Recipe", "The Recipe does not have an id");
            return;
        }

        //TODO: delete recipe along with its associated ingredient here
    }

    /**
     * Updates the corresponding recipe document in the collection with the fields of the
     * recipe object.
     * @param recipe A Recipe object whose changes we want to push to the collection of Recipes
     * @throws InterruptedException If the transaction in the method was not complete
     */
    public void editRecipe(Recipe recipe) throws InterruptedException {
        String id = recipe.getId();

        CountDownLatch editLatch = new CountDownLatch(1);

        ArrayList<DocumentReference> recipeIngredientDocuments = new ArrayList<>();

        for(Ingredient i: recipe.getIngredients()) {
            if (i.getId() == null) {
                DocumentReference newDocumentReference = recipeIngredientDB.getDocumentReference(recipeIngredientDB.addRecipeIngredient(i));
                recipeIngredientDocuments.add(newDocumentReference);
            }
            else{
                try {
                    recipeIngredientDB.getRecipeIngredient(i.getId());
                }
                catch(Exception err){
                    Log.d(TAG, "addRecipe: Failed to get recipe");
                }
            }
        }

        Map<String, Object> recipeMap = new HashMap<>();

        recipeMap.put("title", recipe.getTitle());
        recipeMap.put("comments", recipe.getComments());
        recipeMap.put("category", recipe.getCategory());
        recipeMap.put("prep-time-minutes", recipe.getPrepTimeMinutes());
        recipeMap.put("servings", recipe.getServings());
        recipeMap.put("photograph", recipe.getPhotograph());
        recipeMap.put("ingredients", recipeIngredientDocuments);


        DocumentReference newRecipe = recipesCollection.document(id);

        db.runTransaction((Transaction.Function<Void>) transaction -> {

                    transaction.update(newRecipe, recipeMap);

                    return null;
                })
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "onSuccess: ");
                    editLatch.countDown();
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "onFailure: ");
                    editLatch.countDown();
                });

        editLatch.await();
    }

    /**
     *
     * @param id A String with the id of the document who recipe we want
     * @return The Recipe object that corresponds to the document in the collection
     * If it does not exist then return null
     * @throws InterruptedException If the transaction in the method was not complete
     */
    public Recipe getRecipe(String id) throws InterruptedException {
        CountDownLatch getLatch = new CountDownLatch(1);

        Recipe recipe = new Recipe(null);
        recipe.setId(id);
        DocumentReference recipeDocument = recipesCollection.document(id);
        final DocumentSnapshot[] recipeSnapshot = new DocumentSnapshot[1];
        db.runTransaction((Transaction.Function<Void>) transaction -> {

                    recipeSnapshot[0] = transaction.get(recipeDocument);

                    return null;
                })
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "onSuccess: ");
                    getLatch.countDown();
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "onFailure: ");
                    getLatch.countDown();
                });

        getLatch.await();

        if(!recipeSnapshot[0].exists()){
            return null;
        }


        recipe = getRecipe(recipeSnapshot[0]);

        return recipe;
}

    /**
     * Gets a recipe from its DocumentSnapshot
     * @param recipeSnapshot The DocumentSnapshot of the recipe we want to get
     * @return The Recipe object that corresponds to the document in the collection
     */
    private Recipe getRecipe(DocumentSnapshot recipeSnapshot) {
        Recipe recipe = new Recipe(recipeSnapshot.getString("title"));
        recipe.setId(recipeSnapshot.getId());
        recipe.setCategory(recipeSnapshot.getString("category"));
        recipe.setComments(recipeSnapshot.getString("comments"));
//        TODO: manpreet fix
//        recipe.setPhotograph(recipeSnapshot.getString("photograph"));
        recipe.setPrepTimeMinutes(Objects.requireNonNull(recipeSnapshot.getLong("prep-time-minutes")).intValue());
        recipe.setServings(Objects.requireNonNull(recipeSnapshot.getLong("servings")).intValue());

        List<DocumentReference> recipeIngredients = (List<DocumentReference>) Objects.requireNonNull(
                recipeSnapshot.get("ingredients"));
        for(DocumentReference ingredient: recipeIngredients){
            try {
                recipeIngredientDB.getRecipeIngredient(ingredient.getId());
            }
            catch(Exception err){
                Log.d(TAG, "addRecipe: Failed to get recipe");
            }
        }

        return recipe;
    }

    /**
     * Makes an ArrayList of Recipes out of all the documents in the collection of Recipes
     * @return ArrayList The List of all Recipes contained in the
     * database
     * @throws InterruptedException If the transaction in the method was not complete
     */
    public ArrayList<Recipe> getRecipes() throws InterruptedException {
        Log.d(TAG, "getRecipes:");
        CountDownLatch recipesLatch = new CountDownLatch(1);
        final QuerySnapshot[] recipesSnapshot = new QuerySnapshot[1];
        Task<QuerySnapshot> task = recipesCollection.get()
                .addOnSuccessListener(value -> {
                    recipesSnapshot[0] = value;
                    Log.d(TAG, "getRecipes: OnSuccess");
                    recipesLatch.countDown();
                }).addOnFailureListener(e -> {
                    Log.d(TAG, "getRecipes: OnFailure");
                    recipesLatch.countDown();
                });
        recipesLatch.await();
        Log.d(TAG, "getRecipes:3");
        ArrayList<Recipe> recipes = new ArrayList<>();

        for (QueryDocumentSnapshot recipeSnapshot : recipesSnapshot[0]) {
            recipes.add(this.getRecipe(recipeSnapshot));
        }

        return recipes;
    }

    /**
     * Get the DocumentReference from Recipes collection for the given id
     * @param id The String of the document in Recipes collection we want
     * @return DocumentReference of the Recipe
     */
    public DocumentReference getDocumentReference(String id){
        return recipesCollection.document(id);
    }
}

