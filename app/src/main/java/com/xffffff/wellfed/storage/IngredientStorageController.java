package com.xffffff.wellfed.storage;


import androidx.fragment.app.FragmentActivity;

import com.google.firebase.firestore.Query;
import com.xffffff.wellfed.ActivityBase;
import com.xffffff.wellfed.common.DBConnection;
import com.xffffff.wellfed.ingredient.StorageIngredient;
import com.xffffff.wellfed.ingredient.StorageIngredientAdapter;
import com.xffffff.wellfed.ingredient.StorageIngredientDB;

/**
 * The IngredientStorageController class is a controller for the
 * IngredientStorageActivity.
 */
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
        Query query = db.getSortedQuery(field);
        adapter.setQuery(query);
    }

    /**
     * Get the search results from the DB
     * @param field the field to search by
     * @return The search results
     */
    public void getSearchResults(String field) {
        Query query = db.getSearchQuery(field);
        adapter.setQuery(query);
    }
}
