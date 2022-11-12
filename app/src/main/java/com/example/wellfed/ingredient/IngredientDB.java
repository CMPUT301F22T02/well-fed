package com.example.wellfed.ingredient;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.wellfed.common.DBConnection;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    private FirebaseFirestore db;
    /**
     * Holds a connection to the DB.
     */
    private DBConnection ingredientsConnection;
    /**
     * Holds the collection for the users Ingredient collection in DB
     */
    private CollectionReference collection;

    /**
     * Constructs an IngredientDB object
     */
    public IngredientDB(Context context, boolean isTest) {
        this.ingredientsConnection = new DBConnection(context, isTest);
        db = this.ingredientsConnection.getDB();
        collection = this.ingredientsConnection.getCollection("Ingredients");
    }

    /**
     * The OnAddIngredientListener interface is used to handle the result of the
     * addIngredient method.
     */
    public interface OnAddIngredientListener {
        /**
         * Called when the addIngredient method is complete.
         *
         * @param ingredient The ingredient that was added to the database
         * @param success    True if the operation was successful, false
         *                   otherwise
         */
        void onAddIngredient(Ingredient ingredient, Boolean success);
    }

    /**
     * The OnGetIngredientListener interface is used to handle the result of the
     * getIngredient method.
     */
    public interface OnGetIngredientListener {
        /**
         * Called when the getIngredient method is complete.
         *
         * @param ingredient The ingredient that was retrieved from the database
         *                   or null if the ingredient was not retrieved.
         * @param success    True if the operation was successful, false
         *                   otherwise
         */
        void onGetIngredient(Ingredient ingredient, Boolean success);
    }

    /**
     * The OnDeleteIngredientListener interface is used to handle the result
     * of the
     * deleteIngredient method.
     */
    public interface OnDeleteIngredientListener {
        /**
         * Called when the deleteIngredient method is complete.
         *
         * @param ingredient The ingredient that was deleted from the
         *                   database.
         * @param success    True if the operation was successful, false
         *                   otherwise
         */
        void onDeleteIngredient(Ingredient ingredient, Boolean success);
    }

    /**
     * The OnUpdateIngredientReferenceCountListener interface is used to
     * handle the result
     * of the updateReferenceCount method.
     */
    public interface OnUpdateIngredientReferenceCountListener {
        /**
         * Called when the deleteIngredient method is complete.
         *
         * @param ingredient The ingredient that was deleted from the
         *                   database.
         * @param success    True if the operation was successful, false
         *                   otherwise
         */
        void onUpdateReferenceCount(Ingredient ingredient, Boolean success);
    }

    /**
     * Adds an ingredient in storage to the Firebase DB.
     *
     * @param ingredient the ingredient to add
     * @param listener   the listener to call when the ingredient is added
     */
    public void addIngredient(@NonNull Ingredient ingredient,
                              OnAddIngredientListener listener) {
        // creating batch and return value
        WriteBatch batch = db.batch();

        // add ingredient info to batch
        String ingredientId = collection.document().getId();
        DocumentReference ingredientRef = collection.document(ingredientId);
        Map<String, Object> item = new HashMap<>();
        item.put("category", ingredient.getCategory());
        item.put("description", ingredient.getDescription());
        batch.set(ingredientRef, item);

        batch.commit().addOnCompleteListener(task -> {
            Log.d(TAG, "addIngredient:onComplete");
            if (task.isSuccessful()) {
                Log.d(TAG, ":isSuccessful:" + ingredientId);
                ingredient.setId(ingredientId);
                listener.onAddIngredient(ingredient, true);
            } else {
                Log.d(TAG, ":isFailure:" + ingredientId);
                listener.onAddIngredient(ingredient, false);
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
    public void getIngredient(String id, OnGetIngredientListener listener) {
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
                              OnGetIngredientListener listener) {
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
                    listener.onGetIngredient(ingredient, true);
                } else {
                    Log.d(TAG, ":notExists:" + id);
                    listener.onGetIngredient(null, false);
                }
            } else {
                Log.d(TAG, ":isFailure:" + id);
                listener.onGetIngredient(null, false);
            }

        });
    }


    public void getIngredient(Ingredient ingredient,
                              OnGetIngredientListener listener) {
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
                            listener.onGetIngredient(ingredientInDb, true);
                            return;
                        }
                        listener.onGetIngredient(null, false);

                    } else {
                        Log.d(TAG, "Error getting documents: ",
                                task.getException());
                        listener.onGetIngredient(null, false);
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
                                 OnDeleteIngredientListener listener) {
        WriteBatch batch = db.batch();

        DocumentReference ingredientDocument =
                collection.document(ingredient.getId());
        batch.delete(ingredientDocument);

        batch.commit().addOnCompleteListener(task -> {
            Log.d(TAG, "deleteIngredient:onComplete");
            if (task.isSuccessful()) {
                Log.d(TAG, ":isSuccessful:" + ingredient.getId());
                listener.onDeleteIngredient(ingredient, true);
            } else {
                Log.d(TAG, ":isFailure:" + ingredient.getId());
                listener.onDeleteIngredient(ingredient, false);
            }
        });
    }

    /**
     * TODO
     * @param ingredient
     * @param delta
     * @param listener
     */
    public void updateReferenceCount(Ingredient ingredient, int delta,
                                     OnUpdateIngredientReferenceCountListener listener) {
        String id = getDocumentReference(ingredient).getId();
        collection.document(id).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Long count = document.getLong("count");
                            if (count == null) {
                                count = 0L;
                            }
                            count += delta;
                            if (count > 0) {
                                collection.document(id)
                                        .update("count", count)
                                        .addOnCompleteListener(task1 -> {
                                            listener.onUpdateReferenceCount(
                                                    ingredient,
                                                    task1.isSuccessful());
                                        });
                            } else {
                                // can safely remove unused ingredients from db
                                deleteIngredient(ingredient,
                                        (deleteIngredient, success) -> {
                                            listener.onUpdateReferenceCount(
                                                    ingredient, success);
                                        });
                            }
                        } else {
                            listener.onUpdateReferenceCount(ingredient, false);
                        }
                    } else {
                        listener.onUpdateReferenceCount(ingredient, false);
                    }
                });
    }

    /**
     * Converts a DocumentSnapshot of an ingredient to an ingredient object
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


    public Query getQuery(){
        return collection;
    }
}
