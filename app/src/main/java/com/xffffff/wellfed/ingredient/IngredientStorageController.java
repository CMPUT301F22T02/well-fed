package com.xffffff.wellfed.ingredient;


import androidx.fragment.app.FragmentActivity;

import com.google.firebase.firestore.Query;
import com.xffffff.wellfed.ActivityBase;
import com.xffffff.wellfed.common.DBConnection;

public class IngredientStorageController {
    /**
     * the activity
     */
    private final ActivityBase activity;
    /**
     * the adapter that is used to display the ingredients in the list view
     */
    private final StorageIngredientAdapter adapter;
    /**
     * the DB of ingredients
     */
    private final StorageIngredientDB db;
    /**
     * The Field that is currently being sorted by
     */
    private String currentField = "description";
    /**
     * Creates ingredients list that represents an empty food storage.
     *
     * @param activity the activity that is using this controller
     */
    public IngredientStorageController(FragmentActivity activity) {
        this.activity = (ActivityBase) activity;
        DBConnection connection =
                new DBConnection(activity.getApplicationContext());
        db = new StorageIngredientDB(connection);
        adapter = new StorageIngredientAdapter(db);
        getSortedResults(currentField);
    }

    public StorageIngredientDB getDb() {
        return db;
    }

    /**
     * Gets the adapter that is used to display the ingredients in the list
     * view.
     *
     * @return the adapter that is used to display the ingredients in the
     * list view
     */
    public StorageIngredientAdapter getAdapter() {
        return adapter;
    }

    /**
     * Deletes StorageIngredient from adapter
     *
     * @param storageIngredient the StorageIngredient object to delete
     */
    public void deleteIngredient(StorageIngredient storageIngredient) {
        this.db.deleteStorageIngredient(storageIngredient,
                ((deleteStorageIngredient, deleteSuccess) -> {
                    if (!deleteSuccess) {
                        this.activity.makeSnackbar("Failed to delete" +
                                deleteStorageIngredient.getDescription());
                    } else {
                        this.activity.makeSnackbar("Deleted " +
                                deleteStorageIngredient.getDescription());
                    }
                }));
    }

    /**
     * Adds an StorageIngredient to adapter
     *
     * @param storageIngredient the StorageIngredient object to add
     */
    public void addIngredient(StorageIngredient storageIngredient) {
        db.addStorageIngredient(storageIngredient,
                (addIngredient, addSuccess) -> {
                    if (!addSuccess) {
                        this.activity.makeSnackbar("Failed to add " +
                                addIngredient.getDescription());
                    } else {
                        this.activity.makeSnackbar(
                                "Added " + addIngredient.getDescription());
                        getSortedResults(currentField);
                    }
                });
    }


    /**
     * Update StorageIngredient in adapter
     *
     * @param storageIngredient the StorageIngredient object to update
     */
    public void updateIngredient(StorageIngredient storageIngredient) {
        db.updateStorageIngredient(storageIngredient,
                (updateIngredient, updateSuccess) -> {
                    if (!updateSuccess) {
                        this.activity.makeSnackbar("Failed to update " +
                                updateIngredient.getDescription());
                    } else {
                        this.activity.makeSnackbar(
                                "Updated " + updateIngredient.getDescription());
                        getSortedResults(currentField);
                    }
                });
    }

    /**
     * Gets the DB of ingredients sorted by field
     *
     * @param field the field to sort by
     *              description | category | expiration
     */
    public void getSortedResults(String field) {
        this.currentField = field;
        Query query = db.getSortedQuery(field);
        adapter.changeQuery(query);
    }

    /**
     * Get the search results from the DB
     *
     * @param query the query to search for
     * @return The search results
     */
    public void getSearchResults(String field) {
        Query query = db.getSearchQuery(field);
        adapter.changeQuery(query);
    }
}