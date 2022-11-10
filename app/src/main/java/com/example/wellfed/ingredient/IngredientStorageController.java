package com.example.wellfed.ingredient;


public class IngredientStorageController {
    /**
     * the adapter that is used to display the ingredients in the list view
     */
    private StorageIngredientAdapter adapter;
    /**
     * the DB of ingredients
     */
    private StorageIngredientDB db;

    /**
     * Creates ingredients list that represents an empty food storage.
     */
    public IngredientStorageController(){
        db = new StorageIngredientDB();
        adapter = new StorageIngredientAdapter(db);
    }

    /**
     * Gets the adapter that is used to display the ingredients in the list view.
     *
     * @return the adapter that is used to display the ingredients in the list view
     */
    public StorageIngredientAdapter getAdapter() {
        return adapter;
    }

//    /**
//     * Deletes the ingredient at the given index from the food storage.
//     * @param pos the index of the ingredient to delete
//     */
//    public void deleteIngredient(int pos){
//        if(pos >= 0 && pos < ingredients.size()){
//            ingredients.remove(pos);
//            ingredientAdapter.notifyItemRemoved(pos);
//        }
//    }
//
//    /**
//     * Adds an Ingredient to the food storage.
//     * @param ingredient An Ingredient object to add
//     */
//    public void addIngredient(StorageIngredient ingredient){
//        if(ingredient != null){
//            if(!ingredients.contains(ingredient)){
//                ingredients.add(ingredient);
//                ingredientAdapter.notifyItemInserted(ingredients.size() - 1);
//            } else {
//                int index = ingredients.indexOf(ingredient);
//                ingredients.set(index, ingredient);
//                ingredientAdapter.notifyItemChanged(index);
//            }
//        }
//    }
//
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
//    public void updateIngredient(int position, StorageIngredient ingredient){
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
