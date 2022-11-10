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
        db = new StorageIngredientDB();
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
     * Deletes the ingredient at the given index from the food storage.
     *
     * @param storageIngredient the ingredient to delete
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
         * Adds an Ingredient to the food storage.
         * @param ingredient An Ingredient object to add
         */
        public void addIngredient(StorageIngredient ingredient) {
            db.addStoredIngredient(ingredient,
                    (addIngredient, addSuccess) -> {
                        if (!addSuccess) {
                            this.activity.makeSnackbar("Failed to add " +
                                    addIngredient.getDescription());
                        } else {
                            this.activity.makeSnackbar("Added " +
                                    addIngredient.getDescription());
                        }
                    });
        }

    //    /**
    //     * Set ingredients to the given list of ingredients.
    //     * @param ingredients the list of ingredients to set
    //     */
    //    public void setIngredients(ArrayList<StorageIngredient> ingredients){
    //        this.ingredients = ingredients;
    //    }
    //
    //    /**
    //     * Get ingredients.
    //     * @return the list of ingredients
    //     */
    //    public ArrayList<StorageIngredient> getIngredients() {
    //        return ingredients;
    //    }
    //
    //    /**
    //     * Update the ingredient at the given index.
    //     * @param position the index of the ingredient to update
    //     * @param ingredient the ingredient to update
    //     */
    //    public void updateIngredient(int position, StorageIngredient
    //    ingredient){
    //        ingredients.set(position, ingredient);
    //        ingredientAdapter.notifyItemChanged(position);
    //    }
    //
    //    /**
    //     * Update the ingredient
    //     * @param ingredient the ingredient to update
    //     */
    //    public void updateIngredient(StorageIngredient ingredient){
    //        int position = ingredients.indexOf(ingredient);
    //        if(position >= 0 && position < ingredients.size()){
    //            ingredients.set(position, ingredient);
    //            ingredientAdapter.notifyItemChanged(position);
    //        }
    //    }
}
