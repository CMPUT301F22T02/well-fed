package com.example.wellfed.ingredient;

import java.util.ArrayList;

/**
 * This class represents an IngredientL.
 *
 * @version 1.0
 */
public class IngredientListController {
    /**
     * The list of ingredients.
     */
    private final ArrayList<StorageIngredient> ingredients;

    /**
     * Creates a new IngredientListController object that represents a list of ingredients.
     */
    public IngredientListController() {
        ingredients = new ArrayList<>();
    }

    /**
     * Adds an ingredient to the list.
     *
     * @param ingredient The ingredient to add
     */
    public void addIngredient(StorageIngredient ingredient) {
        ingredients.add(ingredient);
    }

    /**
     * Removes an ingredient from the list.
     *
     * @param ingredient The ingredient to remove
     */
    public void removeIngredient(StorageIngredient ingredient) {
        ingredients.remove(ingredient);
    }

    /**
     * Gets the ingredient at the specified index.
     *
     * @param index The index where the ingredient is at
     * @return The ingredient at the specified index
     */
    public StorageIngredient getIngredient(int index) {
        return ingredients.get(index);
    }

    /**
     * Gets the size of the list.
     *
     * @return The size of the list
     */
    public int getSize() {
        return ingredients.size();
    }

    /**
     * Gets the list of ingredients.
     *
     * @return The list of ingredients
     */
    public ArrayList<StorageIngredient> getIngredients() {
        return ingredients;
    }

    /**
     * Adds multiple ingredients at once.
     *
     * @param ingredients The ingredients to add
     */
    public void addIngredients(ArrayList<StorageIngredient> ingredients) {
        this.ingredients.addAll(ingredients);
    }

    public void deleteIngredient(int position) {
        ingredients.remove(position);
    }

    public void updateIngredient(int position, Object result) {
        ingredients.set(position, (StorageIngredient) result);
    }
}
