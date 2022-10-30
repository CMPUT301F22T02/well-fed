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
        this.storedIngredients.add(ingredient);
    }

    /**
     * Removes an Ingredient from the food storage.
     * @param ingredient An Ingredient object to remove
     */
    public void removeIngredient(StorageIngredient ingredient) {
        this.storedIngredients.remove(ingredient);
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
}
