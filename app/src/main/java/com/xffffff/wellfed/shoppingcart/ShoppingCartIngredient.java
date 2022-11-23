/**
 * ShoppingCartIngredient
 *
 * Version 1.0
 *
 * 21 October, 2022
 *
 * Copyright (c) 2022.  0xFFFFFF
 * All rights reserved.
 */

package com.xffffff.wellfed.shoppingcart;

import com.xffffff.wellfed.ingredient.Ingredient;

/**
 * This class represents a ShoppingCartIngredient, which are Ingredients that can be added to a
 * ShoppingCart.
 *
 * @version 1.0
 * @see ShoppingCart
 */
public class ShoppingCartIngredient extends Ingredient {
    /**
     * Holds whether the ShoppingCartIngredient has been picked up.
     */
    boolean isPickedUp;
    /**
     * Holds whether the ShoppingCartIngredient has all of its fields completed.
     */
    boolean isComplete;

    /**
     * Creates a ShoppingCartIngredient which represents an Ingredient which can be added to a
     * ShoppingCart.
     * @param description A String representing the description of a ShoppingCartIngredient.
     */
    public ShoppingCartIngredient(String description) {
        super(description);
        isPickedUp = false;
        isComplete = false;
    }

    /**
     * Create a ShoppingCartIngredient using an Ingredient.
     *
     * @param ingredient The Ingredient to use.
     */
    public ShoppingCartIngredient(Ingredient ingredient) {
        super(ingredient.getDescription());
        isPickedUp = false;
        isComplete = false;
    }

    /**
     * Gets whether the ShoppingCartIngredient has been picked up.
     * @return A boolean representing whether the ShoppingCartIngredient has been picked up.
     */
    public boolean isPickedUp() {
        return isPickedUp;
    }

    /**
     * Sets whether a ShoppingCartIngredient has been picked up.
     * @param pickedUp A boolean representing whether the ShoppingCartIngredient has been picked up.
     */
    public void setPickedUp(boolean pickedUp) {
        isPickedUp = pickedUp;
    }

    /**
     * Gets whether a ShoppingCartIngredient has its fields completed.
     * @return A boolean representing whether the ShoppingCartIngredient has completed fields.
     */
    public boolean isComplete() {
        return isComplete;
    }

    /**
     * Sets whether a ShoppingCartIngredient has its fields completed.
     * @param complete A boolean representing whether the ShoppingCartIngredient has
     *                 completed fields.
     */
    public void setComplete(boolean complete) {
        isComplete = complete;
    }
}
