package com.example.wellfed.shoppingcart;

import com.example.wellfed.ingredient.StorageIngredient;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class ShoppingCartIngredientController {
    /**
     * Holds a list of ShoppingCartIngredients in the shopping cart.
     */
    private ArrayList<ShoppingCartIngredient> ingredients;

    /**
     * adapter is used to display the ingredients in the recycler view.
     */
    private ShoppingCartIngredientAdapter adapter;

    /**
     * Initialize ingredient list.
     */
    public ShoppingCartIngredientController() {
        ingredients = new ArrayList<>();
    }

    /**
     * Sets adapter to the given adapter.
     * @param adapter the adapter to set
     */
    public void setAdapter(ShoppingCartIngredientAdapter adapter) {
        this.adapter = adapter;
    }

    /**
     * Deletes the ingredient at the given index from the shopping cart.
     * @param pos the index of the ingredient to delete
     */
    public void deleteIngredient(int pos) {
        if (pos >= 0 && pos < ingredients.size()) {
            ingredients.remove(pos);
            adapter.notifyItemRemoved(pos);
        }
    }

    /**
     * Adds an Ingredient to the shopping cart.
     * @param ingredient An ShoppingCartIngredient object to add
     */
    public void addIngredient(ShoppingCartIngredient ingredient) {
//        if (ingredient != null) {
//            if (!ingredients.contains(ingredient)) {
//                ingredients.add(ingredient);
//                adapter.notifyItemInserted(ingredients.size() - 1);
//            } else {
//                int index = ingredients.indexOf(ingredient);
//                ingredients.set(index, ingredient);
//                adapter.notifyItemChanged(index);
//            }
//        }
        ingredients.add(ingredient);
        adapter.notifyItemInserted(ingredients.size()-1);
    }

    /**
     * Set ingredients to the given list of ingredients.
     * @param ingredients the list of ingredients to set
     */
    public void setIngredients(ArrayList<ShoppingCartIngredient> ingredients){
        this.ingredients = ingredients;
    }

    /**
     * Update the ingredient at the given index.
     * @param position the index of the ingredient to update
     * @param ingredient the ingredient to update
     */
    public void updateIngredient(int position, ShoppingCartIngredient ingredient){
        ingredients.set(position, ingredient);
        adapter.notifyItemChanged(position);
    }

    /**
     * Method overloading. Update the ingredient.
     * @param ingredient the ingredient to update
     */
    public void updateIngredient(ShoppingCartIngredient ingredient){
        int position = ingredients.indexOf(ingredient);
        if(position >= 0 && position < ingredients.size()){
            ingredients.set(position, ingredient);
            adapter.notifyItemChanged(position);
        }
    }

    public void editIngredient(int position, ShoppingCartIngredient modifiedIngredient) {
        ingredients.set(position, modifiedIngredient);
        adapter.notifyItemChanged(position);
    }

    /**
     * Get ingredients.
     * @return the list of ingredients
     */
    public ArrayList<ShoppingCartIngredient> getIngredients() {
        return ingredients;
    }
}
