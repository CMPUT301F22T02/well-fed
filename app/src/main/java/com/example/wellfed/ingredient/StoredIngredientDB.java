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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class StoredIngredientDB {
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
    public StoredIngredientDB() {
        this.db = FirebaseFirestore.getInstance();
        this.collection = db.collection("StoredIngredients");
        this.ingredients = db.collection("Ingredients");
    }

    /**
     * Adds an ingredient in storage to the Firebase DB.
     * @param storedIngredient the ingredient to be added
     * @return the ID of the added ingredient
     * @throws InterruptedException when the on success listeners cannot complete
     */
    public String addStoredIngredient(@NonNull StoredIngredient storedIngredient) throws InterruptedException {
        // Some of the fields of a StoredIngredient may be empty.
        Map<String, Object> ingredient = new HashMap<>();
        ingredient.put("category", storedIngredient.getCategory());
        ingredient.put("description", storedIngredient.getDescription());

        String id = this.ingredients.document().getId();
        CountDownLatch ingredientComplete = new CountDownLatch(1);
        this.ingredients
                .document(id)
                .set(ingredient)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + id);
                        System.out.println("added");
                        ingredientComplete.countDown();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        System.out.println("not added");
                        ingredientComplete.countDown();
                    }
                });
        // waiting for it to be finished
        ingredientComplete.await();

        // add it to stored ingredients now
        Map<String, Object> stored = new HashMap<>();
        stored.put("unit", storedIngredient.getUnit());
        stored.put("amount", storedIngredient.getAmount());
        stored.put("location", storedIngredient.getLocation());
        stored.put("best-before", storedIngredient.getBestBefore());
        DocumentReference documentReference = db.document("Ingredients/"+id);
        stored.put("ingredient", documentReference);

        CountDownLatch storageComplete = new CountDownLatch(1);
        this.collection
                .document(id)
                .set(stored)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + id);
                        storageComplete.countDown();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        storageComplete.countDown();
                    }
                });
        storageComplete.await();
        return id;
    }

    /**
     * Updates a stored ingredient in the Firebase DB.
     * @param id the ID of the ingredient to update
     * @param storedIngredient the Ingredient containing the updated fields
     * @throws InterruptedException when the on success listeners cannot complete
     */
    public void updateStoredIngredient(String id, StoredIngredient storedIngredient) throws InterruptedException {

        Map<String, Object> ingredient = new HashMap<>();
        ingredient.put("category", storedIngredient.getCategory());
        ingredient.put("description", storedIngredient.getDescription());

        CountDownLatch ingredientComplete = new CountDownLatch(1);
        this.ingredients
                .document(id)
                .set(ingredient)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot updated with ID: " + id);
                        ingredientComplete.countDown();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                        ingredientComplete.countDown();
                    }
                });

        ingredientComplete.await();
        DocumentReference storedDocument = collection.document(id);
        // update object in stored ingredients now
        Map<String, Object> stored = new HashMap<>();
        stored.put("unit", storedIngredient.getUnit());
        stored.put("amount", storedIngredient.getAmount());
        stored.put("location", storedIngredient.getLocation());
        stored.put("best-before", storedIngredient.getBestBefore());

        this.collection
                .document(id)
                .set(stored)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot updated with ID: " + id);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
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
    public void removeFromIngredients(String id) {
        // removes ingredient from storage AND ingredients
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

        this.ingredients
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
     * Gets an ingredient from the Firebase DB
     * @param id the ID of the ingredient to get
     * @return the ingredient queried
     * @throws InterruptedException when the onComplete listener is interrupted
     */
    public StoredIngredient getStoredIngredient(String id) throws InterruptedException {
        StoredIngredient obtainedIngredient = new StoredIngredient("temp");
        CountDownLatch complete = new CountDownLatch(2);
        this.collection
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            obtainedIngredient.setBestBefore((Date) document.getTimestamp("best-before").toDate());
                            obtainedIngredient.setLocation((String) document.get("location"));
                            obtainedIngredient.setAmount(((Long) document.get("amount")).intValue());
                            obtainedIngredient.setUnit((String) document.get("unit"));
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
                            complete.countDown();
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                            complete.countDown();
                        }
                    }
                });
        complete.await();
        return obtainedIngredient;
    }
}
