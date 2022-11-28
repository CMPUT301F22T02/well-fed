package com.xffffff.wellfed.storage;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.xffffff.wellfed.common.DBConnection;
import com.xffffff.wellfed.common.OnCompleteListener;
import com.xffffff.wellfed.ingredient.Ingredient;
import com.xffffff.wellfed.ingredient.IngredientDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The StorageIngredientDB class is a database for the storage ingredients.
 */
public class StorageIngredientDB {
    /**
     * Holds the tag for logging purposes
     */
    private final static String TAG = "StorageIngredientDB";

    /**
     * Holds the instance of the Firebase FireStore DB.
     */
    private final FirebaseFirestore db;
    /**
     * Holds a reference to the IngredientDB
     */
    private final IngredientDB ingredientDB;
    /**
     * Holds the collection for the users StoredIngredient collection in DB
     */
    private final CollectionReference collection;

    /**
     * Creates a reference to the Firebase DB.
     *
     * @param connection the DBConnection object
     */
    public StorageIngredientDB(DBConnection connection) {
        this.ingredientDB = new IngredientDB(connection);
        this.db = connection.getDB();
        this.collection = connection.getCollection("StoredIngredients");
    }

    /**
     * Adds an ingredient in storage to the Firebase DB.
     *
     * @param storageIngredient the ingredient to be added
     * @param listener          the listener to be called when the operation is
     *                          complete
     */
    public void addStorageIngredient(
            @NonNull StorageIngredient storageIngredient,
            OnCompleteListener<StorageIngredient> listener) {
        Log.d(TAG, "addStorageIngredient:");
        ingredientDB.getIngredient(storageIngredient,
                (foundIngredient, foundSuccess) -> {
                    Log.d(TAG, "getIngredient:");
                    if (!foundSuccess) {
                        Log.d(TAG, "foundSuccess:false");
                        // create a new ingredient
                        ingredientDB.addIngredient(storageIngredient,
                                (addedIngredient, addSuccess) -> {
                                    Log.d(TAG, "addIngredient:");
                                    addStorageIngredient(storageIngredient,
                                            addedIngredient, listener);
                                });

                    } else {
                        // ingredient already exists
                        Log.d(TAG, "foundSuccess:true");
                        addStorageIngredient(storageIngredient, foundIngredient,
                                listener);
                    }
                });
    }

    /**
     * Adds an ingredient in storage to the Firebase DB.
     *
     * @param storageIngredient the ingredient to be added
     * @param ingredient        the ingredient to be added
     * @param listener          the listener to be called when the operation is
     */
    private void addStorageIngredient(StorageIngredient storageIngredient,
                                      Ingredient ingredient,
                                      OnCompleteListener<StorageIngredient> listener) {
        Log.d(TAG, "addStorageIngredient:");
        storageIngredient.setId(ingredient.getId());
        HashMap<String, Object> storageIngredientMap = new HashMap<>();
        storageIngredientMap.put("category", storageIngredient.getCategory());
        storageIngredientMap.put("description",
                storageIngredient.getDescription());
        storageIngredientMap.put("best-before",
                storageIngredient.getBestBefore());
        storageIngredientMap.put("location", storageIngredient.getLocation());
        storageIngredientMap.put("amount", storageIngredient.getAmount());
        storageIngredientMap.put("unit", storageIngredient.getUnit());
        storageIngredientMap.put("search-field", storageIngredient.getDescription().toLowerCase());
        storageIngredientMap.put("Ingredient",
                ingredientDB.getDocumentReference(ingredient));
        this.collection.add(storageIngredientMap)
                .addOnSuccessListener(stored -> {
                    Log.d(TAG, "success:");
                    storageIngredient.setStorageId(stored.getId());
                    ingredientDB.updateReferenceCount(ingredient, 1,
                            (updatedIngredient, success) -> {
                                Log.d(TAG, "updateReferenceCount:");
                                listener.onComplete(
                                        storageIngredient, success);
                            });
                }).addOnFailureListener(exception -> {
                    Log.d(TAG, "failure:");
                    listener.onComplete(storageIngredient, false);
                });
    }

    /**
     * Updates a stored ingredient in the Firebase DB.
     *
     * @param storageIngredient the Ingredient containing the updated fields
     * @param listener          the listener to handle the result
     */
    public void updateStorageIngredient(StorageIngredient storageIngredient,
                                        OnCompleteListener<StorageIngredient> listener) {
        getStorageIngredient(storageIngredient.getStorageId(),
                (oldStorageIngredient, getSuccess1) -> {
                    if (!getSuccess1) {
                        listener.onComplete(storageIngredient,
                                false);
                        return;
                    }
                    ingredientDB.getIngredient(storageIngredient,
                            (getIngredient, getSuccess) -> {
                                if (getSuccess) {
                                    updateStorageIngredient(storageIngredient,
                                            oldStorageIngredient, getIngredient,
                                            listener);
                                    return;
                                }
                                ingredientDB.addIngredient(storageIngredient,
                                        (addedIngredient, addSuccess) -> {
                                            Log.d(TAG, "addIngredient:");
                                            if (!addSuccess) {
                                                listener.onComplete(
                                                        storageIngredient,
                                                        false);
                                                return;
                                            }
                                            updateStorageIngredient(
                                                    storageIngredient,
                                                    oldStorageIngredient,
                                                    addedIngredient, listener);
                                        });
                            });

                });
    }

    /**
     * Updates a stored ingredient in the Firebase DB.
     *
     * @param storageIngredient the Ingredient containing the updated fields
     * @param oldStorageIngredient the old ingredient
     * @param ingredient        the ingredient to be added
     * @param listener          the listener to handle the result
     */
    public void updateStorageIngredient(StorageIngredient storageIngredient,
                                        StorageIngredient oldStorageIngredient,
                                        Ingredient ingredient,
                                        OnCompleteListener<StorageIngredient> listener) {
        WriteBatch batch = db.batch();
        DocumentReference storageIngredientRef =
                this.collection.document(storageIngredient.getStorageId());
        batch.update(storageIngredientRef, "category",
                storageIngredient.getCategory());
        batch.update(storageIngredientRef, "description",
                storageIngredient.getDescription());
        batch.update(storageIngredientRef, "unit", storageIngredient.getUnit());
        batch.update(storageIngredientRef, "amount",
                storageIngredient.getAmount());
        batch.update(storageIngredientRef, "location",
                storageIngredient.getLocation());
        batch.update(storageIngredientRef, "best-before",
                storageIngredient.getBestBefore());
        batch.update(storageIngredientRef, "Ingredient",
                ingredientDB.getDocumentReference(ingredient));
        batch.update(storageIngredientRef, "search-field", storageIngredient.getDescription().toLowerCase());

        batch.commit().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Updated storage ingredient");
                ingredientDB.updateReferenceCount(storageIngredient, 1,
                        (updatedIngredient2, success) -> {
                            if (!success) {
                                listener.onComplete(
                                        storageIngredient, false);
                                return;
                            }
                            ingredientDB.updateReferenceCount(
                                    oldStorageIngredient, -1,
                                    (updatedIngredient3, success2) -> listener.onComplete(
                                            storageIngredient, success2));
                        });
            } else {
                Log.d(TAG, "Failed to update storage ingredient");
                listener.onComplete(storageIngredient, false);
            }
        });
    }

    /**
     * Removes an ingredient from storage, but keeps the Ingredient reference
     *
     * @param storageIngredient the storageIngredient to remove
     * @param listener         the listener to handle the result
     */
    public void deleteStorageIngredient(StorageIngredient storageIngredient,
                                        OnCompleteListener<StorageIngredient> listener) {
        this.collection.document(storageIngredient.getStorageId()).delete()
                .addOnSuccessListener(onDelete -> {
                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    ingredientDB.updateReferenceCount(storageIngredient, -1,
                            (updatedIngredient, success) -> {
                                Log.d(TAG, "updateReferenceCount:");
                                listener.onComplete(
                                        storageIngredient, success);
                            });
                }).addOnFailureListener(failure -> {
                    Log.d(TAG, "Failed to delete the storageIngredient");
                    listener.onComplete(storageIngredient,
                            false);
                });
    }

    /**
     * Gets an ingredient from the Firebase DB
     *
     * @param id the ID of the ingredient to get
     *           a StorageIngredient
     *           with a null description.
     *           when the onComplete listener is interrupted
     * @param listener the listener to handle the result
     */
    public void getStorageIngredient(String id,
                                     OnCompleteListener<StorageIngredient> listener) {
        this.collection.document(id).get()
                .addOnSuccessListener(storedSnapshot -> {
                    if (storedSnapshot.exists()) {
                        Log.d(TAG, "StorageIngredient found");
                        this.getStorageIngredient(storedSnapshot, listener);
                    } else {
                        Log.d(TAG, "Failed to get Stored Ingredient");
                        listener.onComplete(null, false);
                    }
                }).addOnFailureListener(failure -> {
                    Log.d(TAG, "Failed to get Stored Ingredient");
                    listener.onComplete(null, false);
                });
    }

    /**
     * Gets an ingredient from the Firebase DB
     *
     * @param snapshot the snapshot of the StorageIngredient to get
     * @param listener the listener to call when the ingredient is found
     */
    public void getStorageIngredient(DocumentSnapshot snapshot,
                                     OnCompleteListener<StorageIngredient> listener) {
        StorageIngredient storageIngredient =
                new StorageIngredient(snapshot.getString("Description"));
        storageIngredient.setStorageId(snapshot.getId());
        storageIngredient.setCategory(snapshot.getString("category"));
        storageIngredient.setDescription(snapshot.getString("description"));
        storageIngredient.setBestBefore(snapshot.getDate("best-before"));
        storageIngredient.setLocation(snapshot.getString("location"));
        storageIngredient.setAmount(snapshot.getDouble("amount"));
        storageIngredient.setUnit(snapshot.getString("unit"));
        DocumentReference ingredientReference =
                (DocumentReference) snapshot.get("Ingredient");
        assert ingredientReference != null;
        storageIngredient.setId(ingredientReference.getId());
        listener.onComplete(storageIngredient, true);
    }

    /**
     * Gets a sorted query for StorageIngredients in FireStore
     *
     * @param field the field to sort by
     * @return the query
     */
    public Query getSortedQuery(String field) {
        return this.collection.orderBy(field);
    }

    /**
     * getSearchedQuery returns a query for StorageIngredients in FireStore
     * @param field the field to search by
     * @return the query
     */
    public Query getSearchQuery(String field) {
        return this.collection.orderBy("search-field")
                .startAt(field.toLowerCase()).endAt(field.toLowerCase() + '~');
    }

    /**
     * getAllStorageIngredients returns all StorageIngredients in FireStore
     * @param listener the listener to handle the result
     */
    public void getAllStorageIngredients(OnCompleteListener<ArrayList<StorageIngredient>> listener) {
        // If collection is empty, return empty query
        if (this.collection == null) {
            listener.onComplete(null, false);
            return;
        }
        ArrayList<StorageIngredient> storageIngredients = new ArrayList<>();
        this.collection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.size() == 0) {
                listener.onComplete(storageIngredients, true);
                return;
            }
            AtomicInteger i = new AtomicInteger(queryDocumentSnapshots.size());
            AtomicInteger found = new AtomicInteger(0);
            for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                DocumentReference ingredientRef =
                        (DocumentReference) snapshot.getData()
                                .get("Ingredient");
                assert ingredientRef != null;
                ingredientRef.get().addOnSuccessListener(ingredientSnap -> {
                    String description =
                            ingredientSnap.getString("description");
                    StorageIngredient storageIngredient =
                            new StorageIngredient(description);
                    storageIngredient.setStorageId(snapshot.getId());
                    storageIngredient.setCategory(
                            ingredientSnap.getString("category"));
                    storageIngredient.setAmount(
                            (Double) snapshot.getData().get("amount"));
                    storageIngredient.setUnit(
                            (String) snapshot.getData().get("unit"));
                    storageIngredient.setLocation(
                            (String) snapshot.getData().get("location"));
                    // Get Firebase Timestamp and convert to Date
                    Timestamp bestBefore =
                            (Timestamp) snapshot.getData().get("best-before");
                    assert bestBefore != null;
                    storageIngredient.setBestBefore(bestBefore.toDate());

                    storageIngredients.add(storageIngredient);
                    found.getAndAdd(1);
                    if (found.get() == i.get()) {
                        listener.onComplete(storageIngredients, true);
                    }
                }).addOnFailureListener(
                        failure -> listener.onComplete(null, false));
            }
        });
    }
}