package com.example.wellfed.recipe;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

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

public class RecipeIngredientDB {
    FirebaseFirestore db;
    final CollectionReference recipeIngredientsCollection;
    final CollectionReference ingredientsCollection;

    public RecipeIngredientDB(){
        db = FirebaseFirestore.getInstance();
        this.recipeIngredientsCollection = db.collection("RecipeIngredients");
        this.ingredientsCollection = db.collection("Ingredients");
    }

    public String addRecipeIngredient(RecipeIngredient i) throws InterruptedException {
        String newIngredientId = ingredientsCollection.document().getId();
        String newRecipeIngredientId = recipeIngredientsCollection.document().getId();
        CountDownLatch addIngredient = new CountDownLatch(1);

        db.runTransaction((Transaction.Function<Void>) transaction -> {
            Map<String, Object> ingredientMap = new HashMap<>();
            ingredientMap.put("description", i.getDescription());
            ingredientMap.put("category", i.getCategory());

            DocumentReference newIngredientDocument = ingredientsCollection.document(newIngredientId);
            DocumentReference newRecipeIngredientDocument = recipeIngredientsCollection.document(newRecipeIngredientId);

            Map<String, Object> recipeIngredientMap = new HashMap<>();
            recipeIngredientMap.put("amount", i.getAmount());
            recipeIngredientMap.put("unit", i.getUnit());
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

        i.setId(newRecipeIngredientId);

        return newRecipeIngredientId;
    }

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

    public void updateRecipeIngredient(RecipeIngredient recipeIngredient) throws InterruptedException {
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

    public RecipeIngredient getRecipeIngredient(String id) throws InterruptedException {
        RecipeIngredient recipeIngredient = new RecipeIngredient();

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
        recipeIngredient.setAmount(Objects.requireNonNull(recipeIngredientSnapshot[0].getDouble("amount")).floatValue());
        recipeIngredient.setDescription(Objects.requireNonNull(ingredientSnapshot[0].getString("description")));
        recipeIngredient.setUnit(Objects.requireNonNull(recipeIngredientSnapshot[0].getString("unit")));
        recipeIngredient.setCategory((String) Objects.requireNonNull(ingredientSnapshot[0].getString("category")));

        return recipeIngredient;
    }

    public DocumentReference getDocumentReference(String id){
        return recipeIngredientsCollection.document(id);
    }

    public ArrayList<RecipeIngredient> getDocumentReferences() throws InterruptedException {
        CountDownLatch recipesLatch = new CountDownLatch(1);

        ArrayList<RecipeIngredient> recipeIngredients = new ArrayList<>();
        db.runTransaction((Transaction.Function<Void>) transaction -> {

            List<DocumentSnapshot> recipeIngredientSnapshots = recipeIngredientsCollection.get().getResult().getDocuments();

            for(DocumentSnapshot recipeIngredientSnapshot: recipeIngredientSnapshots){
                RecipeIngredient recipe = null;
                try {
                    recipe = this.getRecipeIngredient(recipeIngredientSnapshot.getId());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                recipeIngredients.add(recipe);
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

        return recipeIngredients;
    }
}
