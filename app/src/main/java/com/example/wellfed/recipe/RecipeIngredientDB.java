package com.example.wellfed.recipe;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

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

public class RecipeIngredientDB {
    /**
     * Holds an instance of the Firebase Firestore database
     */
    private final FirebaseFirestore db;
    /**
     * Holds an instance of the collection of RecipeIngredients stored in the FireStore db database
     */
    private final CollectionReference recipeIngredientsCollection;
    /**
     * Holds an instance of the collection of Ingredients stored in the FireStore db database
     */
    private final CollectionReference ingredientsCollection;

    /**
     * Creates a RecipeIngredientDB object
     */
    public RecipeIngredientDB(){
        db = FirebaseFirestore.getInstance();
        this.recipeIngredientsCollection = db.collection("RecipeIngredients");
        this.ingredientsCollection = db.collection("Ingredients");
    }

    /**
     * Adds a RecipeIngredient to the RecipeIngredient Collection. Adds an Ingredient to Ingredient
     * Collection. The new RecipeIngredient document has a reference to the new Ingredient document.
     * The RecipeIngredient has it's id set to the new RecipeIngredient document id.
     * @param ingredient The RecipeIngredient to be added
     * @return The id of the RecipeIngredient that was added
     * @throws InterruptedException If any transaction in the method was not complete
     */
    public String addRecipeIngredient(Ingredient ingredient) throws InterruptedException {
        String newIngredientId = ingredientsCollection.document().getId();
        String newRecipeIngredientId = recipeIngredientsCollection.document().getId();
        CountDownLatch addIngredient = new CountDownLatch(1);

        db.runTransaction((Transaction.Function<Void>) transaction -> {
            Map<String, Object> ingredientMap = new HashMap<>();
            ingredientMap.put("description", ingredient.getDescription());
            ingredientMap.put("category", ingredient.getCategory());

            DocumentReference newIngredientDocument = ingredientsCollection.document(newIngredientId);
            DocumentReference newRecipeIngredientDocument = recipeIngredientsCollection.document(newRecipeIngredientId);

            Map<String, Object> recipeIngredientMap = new HashMap<>();
            recipeIngredientMap.put("amount", ingredient.getAmount());
            recipeIngredientMap.put("unit", ingredient.getUnit());
            recipeIngredientMap.put("ingredient", newIngredientDocument);

            transaction.set(newIngredientDocument, ingredientMap);
            transaction.set(newRecipeIngredientDocument, recipeIngredientMap);
            return null;
        })
        .addOnSuccessListener(unused -> {
            Log.d(TAG, "onSuccess: ");
            addIngredient.countDown();
        })
        .addOnFailureListener(e -> {
            Log.d(TAG, "onFailure: ");
            addIngredient.countDown();
        });


        addIngredient.await();

        ingredient.setId(newRecipeIngredientId);

        return newRecipeIngredientId;
    }

    /**
     * Delete the RecipeIngredient document
     * @param id The String of the RecipeIngredient document we want to delete
     * @throws InterruptedException If any transaction in the method was not complete
     */
    public void delRecipeIngredient(String id) throws InterruptedException {
        DocumentReference delRecipe = recipeIngredientsCollection.document(id);

        CountDownLatch delLatch = new CountDownLatch(1);

        db.runTransaction((Transaction.Function<Void>) transaction -> {

            transaction.delete(delRecipe);

            return null;
        })
        .addOnSuccessListener(unused -> {
            Log.d(TAG, "onSuccess: ");
            delLatch.countDown();
        })
        .addOnFailureListener(e -> {
            Log.d(TAG, "onFailure: ");
            delLatch.countDown();
        });

        delLatch.await();
    }

    /**
     * Delete the RecipeIngredient and the referenced Ingredient document in the relevant collections
     * @param id The String of the RecipeIngredient document we want to delete
     * @throws InterruptedException If any transaction in the method was not complete
     */
    public void delIngredient(String id) throws InterruptedException {

        final DocumentReference[] recipeIngredientDocument = new DocumentReference[1];
        final DocumentReference[] ingredientDocument = new DocumentReference[1];

        CountDownLatch deleteCount = new CountDownLatch(1);
        CountDownLatch getDocuments = new CountDownLatch(1);

        db.runTransaction((Transaction.Function<Void>) transaction -> {

            recipeIngredientDocument[0] = recipeIngredientsCollection.document(id);

            DocumentSnapshot recipeIngredientSnapshot = transaction.get(recipeIngredientDocument[0]);

            ingredientDocument[0] = recipeIngredientSnapshot.getDocumentReference("ingredient");

            return null;
        })
        .addOnSuccessListener(unused -> {
            Log.d(TAG, "onSuccess: ");
            getDocuments.countDown();
        })
        .addOnFailureListener(e -> {
            Log.d(TAG, "onFailure: ");
            getDocuments.countDown();
        });

        getDocuments.await();

        db.runTransaction((Transaction.Function<Void>) transaction -> {

            transaction.delete(ingredientDocument[0]);
            transaction.delete(recipeIngredientDocument[0]);

            return null;
        })
        .addOnSuccessListener(unused -> {
            Log.d(TAG, "onSuccess: ");
            deleteCount.countDown();
        })
        .addOnFailureListener(e -> {
            Log.d(TAG, "onFailure: ");
            deleteCount.countDown();
        });

        deleteCount.await();
    }

    /**
     * Update the RecipeIngredient document in the database with the new RecipeIngredient fields
     * @param recipeIngredient The RecipeIngredient with the updated information
     * @throws InterruptedException If any transaction in the method was not complete
     */
    public void updateRecipeIngredient(Ingredient recipeIngredient) throws InterruptedException {
        String recipeIngredientId = recipeIngredient.getId();

        CountDownLatch readSnapshot = new CountDownLatch(1);
        CountDownLatch updateDocuments = new CountDownLatch(1);

        DocumentReference recipeDocument = this.recipeIngredientsCollection
                .document(recipeIngredientId);

        final DocumentReference[] ingredientDocument = new DocumentReference[1];

        db.runTransaction((Transaction.Function<Void>) transaction -> {

            DocumentSnapshot recipeIngredientSnapshot = transaction.get(recipeDocument);
            ingredientDocument[0] = recipeIngredientSnapshot.getDocumentReference("ingredient");
            return null;
        })
        .addOnSuccessListener(unused -> {
            Log.d(TAG, "onSuccess: ");
            readSnapshot.countDown();
        })
        .addOnFailureListener(e -> {
            Log.d(TAG, "onFailure: ");
            readSnapshot.countDown();
        });

        readSnapshot.await();


        Map<String, Object> ingredientMap = new HashMap<>();
        ingredientMap.put("description", recipeIngredient.getDescription());
        ingredientMap.put("category", recipeIngredient.getCategory());

        Map<String, Object> recipeIngredientMap = new HashMap<>();
        recipeIngredientMap.put("amount", recipeIngredient.getAmount());
        recipeIngredientMap.put("unit", recipeIngredient.getUnit());
        recipeIngredientMap.put("ingredient", ingredientDocument[0]);

        db.runTransaction((Transaction.Function<Void>) transaction -> {

            transaction.update(ingredientDocument[0], ingredientMap);
            transaction.update(recipeDocument, recipeIngredientMap);
            return null;
        })
        .addOnSuccessListener(unused -> {
            Log.d(TAG, "onSuccess: ");
            updateDocuments.countDown();
        })
        .addOnFailureListener(e -> {
            Log.d(TAG, "onFailure: Update Failed", e);

            updateDocuments.countDown();
        });

        updateDocuments.await();
    }

    /**
     * Get the RecipeIngredient Object from the recipe ingredient document with the same id provided
     * @param id A String of the id that corresponds to the id of the Recipe Ingredient document
     * @return RecipeIngredient corresponding to the document in the collection if it does not exist
     * return null
     * @throws InterruptedException If any transaction in the method was not complete
     */
    public Ingredient getRecipeIngredient(String id) throws InterruptedException {
        Ingredient recipeIngredient = new Ingredient();

        CountDownLatch readRecipeIngredient = new CountDownLatch(1);
        CountDownLatch readIngredient = new CountDownLatch(1);

        DocumentReference recipeIngredientDocument = recipeIngredientsCollection.document(id);
        final DocumentSnapshot[] recipeIngredientSnapshot = new DocumentSnapshot[1];
        final DocumentSnapshot[] ingredientSnapshot = new DocumentSnapshot[1];
        db.runTransaction((Transaction.Function<Void>) transaction -> {

            recipeIngredientSnapshot[0] = transaction.get(recipeIngredientDocument);

            return null;
        })
        .addOnSuccessListener(unused -> {
            Log.d(TAG, "onSuccess: ");
            readRecipeIngredient.countDown();
        })
        .addOnFailureListener(e -> {
            Log.d(TAG, "onFailure: ");
            readRecipeIngredient.countDown();
        });

        readRecipeIngredient.await();

        if(!recipeIngredientSnapshot[0].exists()){
            return null;
        }

        DocumentReference ingredientDocument = (DocumentReference) recipeIngredientSnapshot[0].get("ingredient");

        db.runTransaction((Transaction.Function<Void>) transaction -> {

            ingredientSnapshot[0] = transaction.get(ingredientDocument);

            return null;
        })
        .addOnSuccessListener(unused -> {
            Log.d(TAG, "onSuccess: ");
            readIngredient.countDown();
        })
        .addOnFailureListener(e -> {
            Log.d(TAG, "onFailure: ");
            readIngredient.countDown();
        });

        readIngredient.await();

        recipeIngredient.setId(id);
        if(!Objects.isNull(recipeIngredientSnapshot[0].getDouble("amount")))
            recipeIngredient.setAmount(Objects.requireNonNull(recipeIngredientSnapshot[0].getDouble("amount")).floatValue());
        if(!Objects.isNull(ingredientSnapshot[0].getString("description")))
            recipeIngredient.setDescription(Objects.requireNonNull(ingredientSnapshot[0].getString("description")));
        if(!Objects.isNull(recipeIngredientSnapshot[0].getString("unit")))
            recipeIngredient.setUnit(Objects.requireNonNull(recipeIngredientSnapshot[0].getString("unit")));
        if(!Objects.isNull(ingredientSnapshot[0].getString("category")))
            recipeIngredient.setCategory(Objects.requireNonNull(ingredientSnapshot[0].getString("category")));

        return recipeIngredient;
    }

    /**
     * Get the DocumentReference from RecipeIngredients collection for the given id
     * @param id The String of the document in RecipeIngredients collection we want
     * @return DocumentReference of the RecipeIngredient
     */
    public DocumentReference getDocumentReference(String id){
        return recipeIngredientsCollection.document(id);
    }

    /**
     * Gets all the corresponding RecipeIngredients from the Recipe Ingredient collection
     * @return ArrayList containing all RecipeIngredients
     * @throws InterruptedException If any transaction in the method was not complete
     */
    public ArrayList<Ingredient> getRecipeIngredients() throws InterruptedException {
        CountDownLatch recipesLatch = new CountDownLatch(1);
        final List<DocumentSnapshot>[] recipeIngredientSnapshots = new List[]{null};
        ArrayList<RecipeIngredient> recipeIngredients = new ArrayList<>();
        db.runTransaction((Transaction.Function<Void>) transaction -> {

            recipeIngredientsCollection.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    recipeIngredientSnapshots[0] = queryDocumentSnapshots.getDocuments();
                    recipesLatch.countDown();
                    Log.d(TAG, "onSuccess: ");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    recipesLatch.countDown();
                    Log.d(TAG, "onFailure: Could not get documents");
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

        for(DocumentSnapshot recipeIngredientSnapshot: recipeIngredientSnapshots[0]){
            RecipeIngredient recipe = null;
            try {
                recipe = this.getRecipeIngredient(recipeIngredientSnapshot.getId());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            recipeIngredients.add(recipe);
        }



        return recipeIngredients;
    }
}
