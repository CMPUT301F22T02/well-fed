package com.example.wellfed.recipe;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

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

        for(RecipeIngredient i: recipe.getIngredients()) {
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

        String newRecipeId = recipesCollection.document().getId();

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
     * Updates the corresponding recipe document in the collection with the fields of the
     * recipe object.
     * @param recipe A Recipe object whose changes we want to push to the collection of Recipes
     * @throws InterruptedException If the transaction in the method was not complete
     */
    public void editRecipe(Recipe recipe) throws InterruptedException {
        String id = recipe.getId();

        CountDownLatch editLatch = new CountDownLatch(1);

        ArrayList<DocumentReference> recipeIngredientDocuments = new ArrayList<>();

        for(RecipeIngredient i: recipe.getIngredients()) {
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

        recipe.setTitle(recipeSnapshot[0].getString("title"));
        recipe.setCategory(recipeSnapshot[0].getString("category"));
        recipe.setComments(recipeSnapshot[0].getString("comments"));
        recipe.setPhotograph((Image) recipeSnapshot[0].get("phototgraph"));
        recipe.setPrepTimeMinutes(Objects.requireNonNull(recipeSnapshot[0].getLong("prep-time-minutes")).intValue());
        recipe.setServings(Objects.requireNonNull(recipeSnapshot[0].getLong("servings")).intValue());

        List<DocumentReference> recipeIngredients = (List<DocumentReference>) Objects.requireNonNull(recipeSnapshot[0].get("ingredients"));
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
     * @return ArrayList<Recipe> The List of all Recipes contained in the database
     * @throws InterruptedException If the transaction in the method was not complete
     */
    public ArrayList<Recipe> getRecipes() throws InterruptedException {
        CountDownLatch recipesLatch = new CountDownLatch(1);
        ArrayList<Recipe> recipes = new ArrayList<>();
        final List<DocumentSnapshot>[] recipeSnapshots = new List[]{null};
        db.runTransaction((Transaction.Function<Void>) transaction -> {

            recipesCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    recipeSnapshots[0] = queryDocumentSnapshots.getDocuments();
                    recipesLatch.countDown();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    recipesLatch.countDown();
                    Log.d(TAG, "onFailure: Not able to retrieve snapshot");
                }
            });
            return null;
        })
        .addOnSuccessListener(unused -> {
            Log.d(TAG, "onSuccess: ");
        })
        .addOnFailureListener(e -> {
            Log.d(TAG, "onFailure: ");
        });

        recipesLatch.await();

        for(DocumentSnapshot recipeSnapshot: recipeSnapshots[0]){
            Recipe recipe = null;
            try {
                recipe = this.getRecipe(recipeSnapshot.getId());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            recipes.add(recipe);
        }

        return recipes;
    }
}
