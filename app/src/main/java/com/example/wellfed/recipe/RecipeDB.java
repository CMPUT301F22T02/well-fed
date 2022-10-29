package com.example.wellfed.recipe;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

import android.media.Image;
import android.util.Log;

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

public class RecipeDB {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    final CollectionReference recipesCollection;
    final RecipeIngredientDB recipeIngredientDB;

    public RecipeDB(){
        this.recipesCollection = db.collection("Recipes");
        recipeIngredientDB = new RecipeIngredientDB();
    }

    public void addRecipe(Recipe recipe) throws InterruptedException {
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
    }

    public void delRecipe(Recipe recipe) throws InterruptedException {
        CountDownLatch deleteLatch = new CountDownLatch(1);

        if(recipe.getId().equals(NULL)){
            Log.d("Delete Recipe", "The Recipe does not have an id");
            return;
        }

        DocumentReference recipeToDelete = recipesCollection.document(recipe.getId());

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

    public ArrayList<Recipe> getRecipes() throws InterruptedException {
        CountDownLatch recipesLatch = new CountDownLatch(1);

        ArrayList<Recipe> recipes = new ArrayList<>();
        db.runTransaction((Transaction.Function<Void>) transaction -> {

                    List<DocumentSnapshot> recipeSnapshots = recipesCollection.get().getResult().getDocuments();

                    for(DocumentSnapshot recipeSnapshot: recipeSnapshots){
                        Recipe recipe = null;
                        try {
                            recipe = this.getRecipe(recipeSnapshot.getId());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        recipes.add(recipe);
                    }

                    return null;
                })
                .addOnSuccessListener(unused -> {
                    Log.d(TAG, "onSuccess: ");
                    recipesLatch.countDown();
                })
                .addOnFailureListener(e -> {
                    Log.d(TAG, "onFailure: ");
                    recipesLatch.countDown();
                });

        recipesLatch.await();

        return recipes;
    }
}
