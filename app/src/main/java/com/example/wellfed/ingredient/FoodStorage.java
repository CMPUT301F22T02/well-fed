/**
 * FoodStorage
 *
 * Version 1.0
 *
 * 21 October, 2022
 *
 * Copyright (c) 2022.  0xFFFFFF
 * All rights reserved.
 */

package com.example.wellfed.ingredient;

import java.util.ArrayList;
// todo what is this?
/**
 * This class represents <i>food that is stored.</i>
 *
 * @version 1.0
 * @see StorageIngredient
 */
public class FoodStorage {
    /**
     * Holds the list of Ingredients in the food storage.
     */
    private final ArrayList<StorageIngredient> storedIngredients;

    /**
     * Creates a new FoodStorage object that represents an empty food storage.
     */
    public FoodStorage() {
        this.storedIngredients = new ArrayList<>();
    }

    /**
     * Adds an Ingredient to the food storage.
     * @param ingredient An Ingredient object to add
     */
    public void addIngredient(StorageIngredient ingredient) {
        if (ingredient != null) {
            if (!storedIngredients.contains(ingredient)) {
                storedIngredients.add(ingredient);
            }
        }
    }

    /**
     * Gets the StoredIngredient stored at index i.
     * @param i the index where the StoredIngredient is stored.
     * @return the StoredIngredient at index i
     */
    public StorageIngredient getIngredient(int i) {
        return this.storedIngredients.get(i);
    }

    /**
     * Gets the entire ArrayList where all StoredIngredients are kept.
     * @return the ArrayList of all stored ingredients
     */
    public ArrayList<StorageIngredient> getIngredients() {
        return this.storedIngredients;
    }

    /**
     * Delete the ingredient at the given index from the food storage.
     * @param index the index of the ingredient to delete
     */
    public void deleteIngredient(int index) {
        this.storedIngredients.remove(index);
    }

    /**
     * Set ingredients to the given list of ingredients.
     * @param ingredients the list of ingredients to set
     */
    public void setIngredients(ArrayList<StorageIngredient> ingredients) {
        this.storedIngredients.clear();
        this.storedIngredients.addAll(ingredients);
    }

    /**
     * Update the ingredient at the given index from the food storage.
     * @param i the index of the ingredient to update
     * @param ingredient2 the ingredient to update with
     */
    public void updateIngredient(int i, StorageIngredient ingredient2) {
        this.storedIngredients.remove(i);
        if (this.storedIngredients.size() > i) {
            this.storedIngredients.add(i, ingredient2);
        } else {
            this.storedIngredients.add(ingredient2);
        }
    }
}
