package com.example.wellfed.ingredient;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class StorageIngredientDB {
    /**
     * Holds the instance of the Firebase Firestore DB.
     */
    private FirebaseFirestore db;
    /**
     * Holds a reference to the StoredIngredients collection in the Firebase DB.
     */
    private CollectionReference collection;
    /**
     * Holds a reference to the Ingredients collection in the Firebase DB.
     */
    private CollectionReference ingredients;

    /**
     * Creates a reference to the Firebase DB.
     */
    public StorageIngredientDB() {
        this.db = FirebaseFirestore.getInstance();
        this.collection = db.collection("StoredIngredients");
        this.ingredients = db.collection("Ingredients");
    }

    /**
     * Adds an ingredient in storage to the Firebase DB.
     * @param storedIngredient the ingredient to be added
     * @return a map containing the ID of the Ingredient and StoredIngredient
     * @throws InterruptedException when the on success listeners cannot complete
     */
    public String addStoredIngredient(@NonNull StorageIngredient storedIngredient) throws InterruptedException {
        // creating batch and return value
        WriteBatch batch = db.batch();

        // add ingredient info to batch
        String ingredientId = this.ingredients.document().getId();
        DocumentReference ingredientRef = ingredients.document(ingredientId);
        Map<String, Object> ingredient = new HashMap<>();
        ingredient.put("category", storedIngredient.getCategory());
        ingredient.put("description", storedIngredient.getDescription());
        batch.set(ingredientRef, ingredient);

        // add stored ingredient info to batch
        DocumentReference storedIngredientRef = collection.document(ingredientId);
        Map<String, Object> stored = new HashMap<>();
        stored.put("unit", storedIngredient.getUnit());
        stored.put("amount", storedIngredient.getAmount());
        stored.put("location", storedIngredient.getLocation());
        stored.put("best-before", storedIngredient.getBestBefore());
        DocumentReference documentReference = db.document("Ingredients/"+ingredientId);
        stored.put("ingredient", documentReference);
        batch.set(storedIngredientRef, stored);

        CountDownLatch batchComplete = new CountDownLatch(1);
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "Ingredient written with ID: " + ingredientId);
                batchComplete.countDown();
            }
        });

        batchComplete.await();
        return ingredientId;
    }

    /**
     * Updates a stored ingredient in the Firebase DB.
     * @param id the ID of the StoredIngredient to update
     * @param storedIngredient the Ingredient containing the updated fields
     * @throws InterruptedException when the on success listeners cannot complete
     */
    public void updateStoredIngredient(String id, StorageIngredient storedIngredient) throws InterruptedException {
        WriteBatch batch = db.batch();

        DocumentReference ingredientDocument = ingredients.document(id);
        batch.update(ingredientDocument, "category", storedIngredient.getCategory());
        batch.update(ingredientDocument, "description", storedIngredient.getDescription());

        DocumentReference storedDocument = collection.document(id);
        // update object in stored ingredients now
        batch.update(storedDocument, "unit", storedIngredient.getUnit());
        batch.update(storedDocument, "amount", storedIngredient.getAmount());
        batch.update(storedDocument, "location", storedIngredient.getLocation());
        batch.update(storedDocument, "best-before", storedIngredient.getBestBefore());

        CountDownLatch batchComplete = new CountDownLatch(1);
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "StoredIngredient updated with ID: " + id);

                batchComplete.countDown();
            }
        });

        batchComplete.await();
    }

    /**
     * Removes an ingredient from storage, but keeps the Ingredient reference
     * @param id the ID of an ingredient to remove
     */
    public void removeFromStorage(String id) {
        // removes ingredient from storage
        this.collection
                .document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }

    /**
     * Removes an ingredient from storage, and the ingredient collection
     * @param id the ID of the ingredient to remove
     */
    public void removeFromIngredients(String id) throws InterruptedException {
        // removes ingredient from storage AND ingredients
        WriteBatch batch = db.batch();
        DocumentReference ingredientRef = this.ingredients.document(id);
        DocumentReference storedRef = this.collection.document(id);
        batch.delete(ingredientRef);
        batch.delete(storedRef);

        CountDownLatch batchComplete = new CountDownLatch(1);
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "StoredIngredient deleted with ID: " + id);

                batchComplete.countDown();
            }
        });
        batchComplete.await();
    }

    /**
     * Gets an ingredient from the Firebase DB
     * @param id the ID of the ingredient to get
     * @return The ingredient queried. If there is no result, it will return a StoredIngredient
     *          with a null description.
     * @throws InterruptedException when the onComplete listener is interrupted
     */
    public StorageIngredient getStoredIngredient(String id) throws InterruptedException {
        StorageIngredient obtainedIngredient = new StorageIngredient(null);
        CountDownLatch complete = new CountDownLatch(2);
        CountDownLatch found = new CountDownLatch(2);
        this.collection
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            // ensuring that methods don't get called on null values
                            if (document.getTimestamp("best-before") != null) {
                                obtainedIngredient.setBestBefore((Date) document.getTimestamp("best-before").toDate());
                            } else {
                                obtainedIngredient.setBestBefore(null);
                            }

                            if (document.get("amount") != null) {
                                obtainedIngredient.setAmount(((Double) document.get("amount")).floatValue());
                            } else {
                                obtainedIngredient.setAmount(null);
                            }

                            obtainedIngredient.setLocation((String) document.get("location"));
                            obtainedIngredient.setUnit((String) document.get("unit"));
                            found.countDown();
                            complete.countDown();
                        } else {
                            Log.d(TAG, "Error getting document. ");
                            complete.countDown();
                        }
                    }
                });

        this.ingredients
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, document.getId() + " => " + document.get("description"));
                                obtainedIngredient.setDescription((String) document.get("description"));
                                obtainedIngredient.setCategory((String) document.get("category"));
                            } else {
                                Log.d(TAG, "No such document");
                            }
                            found.countDown();
                            complete.countDown();
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                            complete.countDown();
                        }
                    }
                });
        complete.await();

        if (obtainedIngredient.getDescription() == null) {
            throw new IllegalArgumentException("Item not found in database.");
        }

        return obtainedIngredient;
    }
}
