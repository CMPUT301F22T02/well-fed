package com.example.wellfed.ingredient;


import androidx.fragment.app.FragmentActivity;

import com.example.wellfed.ActivityBase;

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
     * @param activity
     */
    public IngredientStorageController(FragmentActivity activity) {
        this.activity = (ActivityBase) activity;
        db = new StorageIngredientDB(activity.getApplicationContext(), false);
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
}
