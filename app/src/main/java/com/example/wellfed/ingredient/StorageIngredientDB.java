package com.example.wellfed.ingredient;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class StorageIngredientDB {

    /**
     * Holds the tag for logging purposes
     */
    private final static String TAG = "StorageIngredientDB";

    /**
     * Holds the instance of the Firebase Firestore DB.
     */
    private FirebaseFirestore db;
    /**
     * Holds a reference to the StoredIngredients collection in the Firebase DB.
     */
    private final CollectionReference collection;
    /**
     * Holds a reference to the IngredientDB
     */
    private final IngredientDB ingredientDB;

    /**
     * This interface is used to handle the result of
     * adding StorageIngredient to the db
     */
    public interface OnAddStorageIngredientListener {
        /**
         * Called when addStorageIngredient returns a result
         *
         * @param storageIngredient the storageIngredient added to the db,
         *                          null if the storageIngredient was not added
         * @param success           true if the operation is successful,
         *                          false otherwise
         */
        void onAddStoredIngredient(StorageIngredient storageIngredient,
                                   Boolean success);
    }

    /**
     * This interface is used to handle the result of
     * adding StorageIngredient to the db
     */
    public interface OnDeleteStorageIngredientListener {
        /**
         * Called when addStorageIngredient returns a result
         *
         * @param storageIngredient the storageIngredient deleted from the db
         * @param success           true if the operation is successful, false
         *                          otherwise
         */
        void onDeleteStoredIngredient(StorageIngredient storageIngredient,
                                      Boolean success);
    }

    /**
     * This interface is used to handle the result of
     * finding StorageIngredient to the db
     */
    public interface OnGetStorageIngredientListener {
        /**
         * Called when addStorageIngredient returns a result
         *
         * @param storageIngredient the storageIngredient returned by the db
         * @param success           true if the operation is successful, false
         *                          otherwise
         */
        void onGetStoredIngredient(StorageIngredient storageIngredient,
                                   Boolean success);
    }

    /**
     * Creates a reference to the Firebase DB.
     */
    public StorageIngredientDB() {
        this.db = FirebaseFirestore.getInstance();
        this.collection = db.collection("StoredIngredients");
        this.ingredientDB = new IngredientDB();
    }

    /**
     * Adds an ingredient in storage to the Firebase DB.
     *
     * @param storedIngredient the ingredient to be added
     * @return a map containing the ID of the Ingredient and StoredIngredient
     */
    public void addStoredIngredient(@NonNull StorageIngredient storedIngredient,
                                    OnAddStorageIngredientListener listener) {
        // if ingredient already exists
        Ingredient ingredient = new Ingredient();
        ingredient.setCategory(storedIngredient.getCategory());
        ingredient.setDescription(storedIngredient.getDescription());
        HashMap<String, Object> toStore = new HashMap<>();
        toStore.put("best-before", storedIngredient.getBestBefore());
        toStore.put("location", storedIngredient.getLocation());
        toStore.put("amount", storedIngredient.getAmount());
        toStore.put("unit", storedIngredient.getUnit());
        ingredientDB.getIngredient(ingredient,
                (foundIngredient, foundSuccess) -> {
                    if (foundIngredient == null) {
                        // create a new ingredient
                        ingredientDB.addIngredient(ingredient,
                                (addedIngredient, addSuccess) -> {
                                    DocumentReference ingredientDocument =
                                            ingredientDB.getDocumentReference(
                                                    addedIngredient);
                                    toStore.put("Ingredient",
                                            ingredientDocument);
                                    this.collection.add(toStore)
                                            .addOnSuccessListener(
                                                    storedReference -> {
                                                        Log.d(TAG,
                                                                "Added storedIngredient with id: " +
                                                                        storedReference.getId());
                                                        storedIngredient.setId(
                                                                storedReference.getId());
                                                        listener.onAddStoredIngredient(
                                                                storedIngredient,
                                                                true);
                                                    })
                                            .addOnFailureListener(failure -> {
                                                Log.d(TAG, "Failed to add " +
                                                        "storage " +
                                                        "ingredient");
                                                listener.onAddStoredIngredient(
                                                        storedIngredient,
                                                        false);
                                            });
                                });

                    } else {
                        // document already exists
                        DocumentReference documentReference =
                                db.collection("Ingredients")
                                        .document(foundIngredient.getId());

                        toStore.put("Ingredient", documentReference);
                        this.collection.add(toStore)
                                .addOnSuccessListener(stored -> {
                                    Log.d(TAG,
                                            "Added storedIngredient with id: " +
                                                    stored.getId());
                                    storedIngredient.setId(stored.getId());
                                    listener.onAddStoredIngredient(
                                            storedIngredient, true);
                                }).addOnFailureListener(exception -> {
                                    Log.d(TAG, "Failed to add storage " +
                                            "ingredient");
                                    listener.onAddStoredIngredient(null, false);
                                });
                    }
                });
    }

    /**
     * Updates a stored ingredient in the Firebase DB.
     *
     * @param storedIngredient the Ingredient containing the updated fields
     * @throws InterruptedException when the on success listeners cannot
     *                              complete
     */
    public void updateStoredIngredient(String id,
                                       StorageIngredient storedIngredient)
            throws InterruptedException {
        WriteBatch batch = db.batch();

        Ingredient ingredient = new Ingredient();
        ingredient.setCategory(storedIngredient.getCategory());
        ingredient.setDescription(storedIngredient.getDescription());
        DocumentReference ingredientDocument =
                ingredientDB.getDocumentReference(ingredient);
        batch.update(ingredientDocument, "category",
                storedIngredient.getCategory());
        batch.update(ingredientDocument, "description",
                storedIngredient.getDescription());

        DocumentReference storedDocument =
                collection.document(storedIngredient.getId());
        // update object in stored ingredients now
        batch.update(storedDocument, "unit", storedIngredient.getUnit());
        batch.update(storedDocument, "amount", storedIngredient.getAmount());
        batch.update(storedDocument, "location",
                storedIngredient.getLocation());
        batch.update(storedDocument, "best-before",
                storedIngredient.getBestBefore());

        CountDownLatch batchComplete = new CountDownLatch(1);
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "StoredIngredient updated with ID: " +
                        storedIngredient.getId());

                batchComplete.countDown();
            }
        });

        batchComplete.await();

    }

    /**
     * Removes an ingredient from storage, but keeps the Ingredient reference
     *
     * @param storageIngredient the storageIngredient to remove
     */
    public void deleteStorageIngredient(StorageIngredient storageIngredient,
                                        OnDeleteStorageIngredientListener listener) {
        // removes ingredient from storage
        this.collection.document(storageIngredient.getId()).delete()
                .addOnSuccessListener(onDelete -> {
                    Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    listener.onDeleteStoredIngredient(storageIngredient, true);
                }).addOnFailureListener(failure -> {
                    Log.d(TAG, "Failed to delete the storageIngredient");
                    listener.onDeleteStoredIngredient(null, false);
                });
    }

    //    /**
    //     * Removes an ingredient from storage, and the ingredient collection
    //     *
    //     * @param id the ID of the ingredient to remove
    //     */
    //    public void removeFromIngredients(String id) throws
    //    InterruptedException {
    //        // removes ingredient from storage AND ingredients
    //        WriteBatch batch = db.batch();
    //        DocumentReference ingredientRef = this.ingredients.document(id);
    //        DocumentReference storedRef = this.collection.document(id);
    //        batch.delete(ingredientRef);
    //        batch.delete(storedRef);
    //
    //        CountDownLatch batchComplete = new CountDownLatch(1);
    //        batch.commit().addOnCompleteListener(new
    //        OnCompleteListener<Void>() {
    //            @Override public void onComplete(@NonNull Task<Void> task) {
    //                Log.d(TAG, "StoredIngredient deleted with ID: " + id);
    //
    //                batchComplete.countDown();
    //            }
    //        });
    //        batchComplete.await();
    //    }

    /**
     * Gets an ingredient from the Firebase DB
     *
     * @param id the ID of the ingredient to get
     * @return The ingredient queried. If there is no result, it will return
     * a StorageIngredient
     * with a null description.
     * when the onComplete listener is interrupted
     */
    public void getStorageIngredient(String id,
                                     OnGetStorageIngredientListener listener) {
        this.collection.document(id).get()
                .addOnSuccessListener(storedSnapshot -> {
                    Log.d(TAG, "StorageIngredient found");
                    this.getStorageIngredient(storedSnapshot, listener);
                }).addOnFailureListener(failure -> {
                    Log.d(TAG, "Failed to get Stored Ingredient");
                    listener.onGetStoredIngredient(null, false);
                });
    }

    /**
     * Gets an ingredient from the Firebase DB
     *
     * @param snapshot the snapshot of the StorageIngredient to get
     * @param listener the listener to call when the ingredient is found
     */
    public void getStorageIngredient(DocumentSnapshot snapshot,
                                     OnGetStorageIngredientListener listener) {
        StorageIngredient storageIngredient =
                new StorageIngredient(snapshot.getString("Description"));
        storageIngredient.setId(snapshot.getId());
        // todo add correct way to parse and add dates
        storageIngredient.setBestBefore(new Date());
        storageIngredient.setLocation(snapshot.getString("location"));
        storageIngredient.setAmount(
                Float.parseFloat(((Double) snapshot.get("amount")).toString()));
        storageIngredient.setUnit(snapshot.getString("unit"));

        DocumentReference ingredientReference =
                (DocumentReference) snapshot.get("Ingredient");
        if (ingredientReference == null) {
            listener.onGetStoredIngredient(null, false);
            return;
        }

        ingredientDB.getIngredient(ingredientReference,
                (getIngredient, getSuccess) -> {
                    if (getIngredient == null || getSuccess == null) {
                        listener.onGetStoredIngredient(null, false);
                        return;
                    }
                    storageIngredient.setCategory(getIngredient.getCategory());
                    storageIngredient.setDescription(
                            getIngredient.getDescription());
                    listener.onGetStoredIngredient(storageIngredient, true);
                });
    }

    /**
     * Get the DocumentReference from Recipes collection for the given id
     *
     * @param id The String of the document in Recipes collection we want
     * @return DocumentReference of the Recipe
     */
    public DocumentReference getDocumentReference(String id) {
        return collection.document(id);
    }

    /**
     * Gets a query for StorageIngredients in Firestore
     *
     * @return the query
     */
    public Query getQuery() {
        return collection;
        //                .orderBy("timestamp", Query.Direction.DESCENDING)
    }
}
