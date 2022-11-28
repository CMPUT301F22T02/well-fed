package com.xffffff.wellfed.ingredient;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;
import com.xffffff.wellfed.common.DBConnection;
import com.xffffff.wellfed.common.OnCompleteListener;

import java.util.HashMap;
import java.util.Map;

/**
 * The IngredientDB class is used to store and retrieve ingredient data from
 * the database.
 */
public class IngredientDB {
    /**
     * Holds the TAG for logging.
     */
    private static final String TAG = "IngredientDB";
    /**
     * Holds the instance of the Firebase Firestore DB.
     */
    private final FirebaseFirestore db;
    /**
     * Holds the collection for the users Ingredient collection in DB
     */
    private final CollectionReference collection;

    /**
     * Constructs an IngredientDB object
     */
    public IngredientDB(DBConnection connection) {
        db = connection.getDB();
        collection = connection.getCollection("Ingredients");
    }

    /**
     * Adds an ingredient in storage to the Firebase DB.
     *
     * @param ingredient the ingredient to add
     * @param listener   the listener to call when the ingredient is added
     */
    public void addIngredient(@NonNull Ingredient ingredient,
                              OnCompleteListener<Ingredient> listener) {
        // creating batch and return value
        WriteBatch batch = db.batch();

        // add ingredient info to batch
        String ingredientId = collection.document().getId();
        DocumentReference ingredientRef = collection.document(ingredientId);
        Map<String, Object> item = new HashMap<>();
        item.put("category", ingredient.getCategory());
        item.put("description", ingredient.getDescription());
        item.put("count", 0);
        item.put("search-field", ingredient.getDescription().toLowerCase());
        batch.set(ingredientRef, item);

        batch.commit().addOnCompleteListener(task -> {
            Log.d(TAG, "addIngredient:onComplete");
            if (task.isSuccessful()) {
                Log.d(TAG, ":isSuccessful:" + ingredientId);
                ingredient.setId(ingredientId);
                listener.onComplete(ingredient, true);
            } else {
                Log.d(TAG, ":isFailure:" + ingredientId);
                listener.onComplete(ingredient, false);
            }
        });
    }

    /**
     * Gets an ingredient from Firebase DB
     *
     * @param id       the id of the ingredient to be retrieved
     * @param listener the listener to be called when the ingredient is
     *                 retrieved
     */
    public void getIngredient(String id, OnCompleteListener<Ingredient> listener) {
        DocumentReference ingredientRef = collection.document(id);
        getIngredient(ingredientRef, listener);
    }

    /**
     * Gets an ingredient from Firebase DB
     *
     * @param ingredientRef the DocumentReference of the ingredient to be
     *                      retrieved
     * @param listener      the listener to be called when the ingredient is
     *                      retrieved
     */
    public void getIngredient(DocumentReference ingredientRef,
                              OnCompleteListener<Ingredient> listener) {
        ingredientRef.get().addOnCompleteListener(task -> {
            String id = ingredientRef.getId();
            Log.d(TAG, "getIngredient:onComplete");
            if (task.isSuccessful()) {
                Log.d(TAG, ":isSuccessful:" + id);
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, ":exists:" + id);
                    Ingredient ingredient = new Ingredient();
                    ingredient.setCategory(document.getString("category"));
                    ingredient.setDescription(
                            document.getString("description"));
                    ingredient.setId(id);
                    listener.onComplete(ingredient, true);
                } else {
                    Log.d(TAG, ":notExists:" + id);
                    listener.onComplete(null, false);
                }
            } else {
                Log.d(TAG, ":isFailure:" + id);
                listener.onComplete(null, false);
            }

        });
    }

    /**
     * Gets an ingredient from Firebase DB
     *
     * @param ingredient the ingredient to be
     *                   retrieved
     * @param listener   the listener to be called when the ingredient is
     *                   retrieved
     */
    public void getIngredient(Ingredient ingredient,
                              OnCompleteListener<Ingredient> listener) {
        collection.whereEqualTo("category", ingredient.getCategory())
                .whereEqualTo("description", ingredient.getDescription())
                .limit(1).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document :
                                task.getResult()) {
                            Log.d(TAG,
                                    document.getId() + " => " + document.getData());
                            Ingredient ingredientInDb =
                                    snapshotToIngredient(document);
                            ingredientInDb.setId(document.getId());
                            listener.onComplete(ingredientInDb, true);
                            return;
                        }
                        listener.onComplete(null, false);

                    } else {
                        Log.d(TAG, "Error getting documents: ",
                                task.getException());
                        listener.onComplete(null, false);
                    }
                });
    }

    /**
     * Deletes an ingredient from the Firebase DB.
     *
     * @param ingredient the ingredient to delete
     * @param listener   the listener to call when the ingredient is deleted
     */
    public void deleteIngredient(@NonNull Ingredient ingredient,
                                 OnCompleteListener<Ingredient> listener) {
        WriteBatch batch = db.batch();

        DocumentReference ingredientDocument =
                collection.document(ingredient.getId());
        batch.delete(ingredientDocument);

        batch.commit().addOnCompleteListener(task -> {
            Log.d(TAG, "deleteIngredient:onComplete");
            if (task.isSuccessful()) {
                Log.d(TAG, ":isSuccessful:" + ingredient.getId());
                listener.onComplete(ingredient, true);
            } else {
                Log.d(TAG, ":isFailure:" + ingredient.getId());
                listener.onComplete(ingredient, false);
            }
        });
    }

    /**
     * Updates the reference count of an ingredient in the Firebase DB. The
     * reference count keeps
     * track of how many references exist to an object - if the object has 0
     * references, it is
     * safely deleted.
     *
     * @param ingredient the ingredient to update the reference count of
     * @param delta      how much to shift the reference count by
     * @param listener   the listener to be called when the reference count
     *                   is updated
     */
    public void updateReferenceCount(Ingredient ingredient, int delta,
                                     OnCompleteListener<Ingredient> listener) {
        final DocumentReference ingredientRef =
                collection.document(ingredient.getId());

        db.runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction)
                    throws FirebaseFirestoreException {
                DocumentSnapshot snapshot = transaction.get(ingredientRef);

                Long newCount = snapshot.getLong("count") + delta;
                transaction.update(ingredientRef, "count", newCount);

                // Success
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onComplete(ingredient, true);
                Log.d(TAG, "Transaction success!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onComplete(ingredient, false);
                Log.w(TAG, "Transaction failure.", e);
            }
        });
    }

    /**
     * Converts a DocumentSnapshot of an ingredient to an Ingredient object
     *
     * @param document the DocumentSnapshot to convert
     * @return the ingredient
     */
    public Ingredient snapshotToIngredient(DocumentSnapshot document) {
        Ingredient ingredient = new Ingredient();

        ingredient.setCategory(document.getString("category"));
        ingredient.setDescription(document.getString("description"));
        ingredient.setId(document.getId());
        return ingredient;
    }

    /**
     * Get document reference of an ingredient
     *
     * @param ingredient the ingredient to get the reference of
     * @return the document reference
     */
    public DocumentReference getDocumentReference(Ingredient ingredient) {
        return collection.document(ingredient.getId());
    }

    /**
     * Gets the CollectionReference that Ingredients are stored in
     *
     * @return the CollectionReference
     */
    public Query getQuery() {
        return collection;
    }

    public Query getSortQuery(String field, boolean ascending) {
        return collection.orderBy(field, ascending ? Query.Direction.ASCENDING :
                Query.Direction.DESCENDING);
    }

    public Query getSearchQuery(String field) {
        return this.collection.orderBy("search-field")
                .startAt(field.toLowerCase()).endAt(field.toLowerCase() + '~');
    }
}
