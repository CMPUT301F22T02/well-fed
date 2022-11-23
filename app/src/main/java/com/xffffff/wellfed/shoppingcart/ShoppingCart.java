/**
 * ShoppingCart
 * <p>
 * Version 1.0
 * <p>
 * 21 October, 2022
 * <p>
 * Copyright (c) 2022.  0xFFFFFF
 * All rights reserved.
 */

package com.xffffff.wellfed.shoppingcart;

import java.util.ArrayList;

/**
 * This class represents a ShoppingCart, which represents a cart that
 * ingredients are picked up in,
 * and information is added to.
 *
 * @version 1.0
 * @see ShoppingCartIngredient
 */
public class ShoppingCart {
    /**
     * Holds the ShoppingCartIngredients in the ShoppingCart.
     */
    private final ArrayList<ShoppingCartIngredient> ingredients;

    /**
     * Creates a ShoppingCart object representing a ShoppingCart that holds
     * ShoppingCartIngredients
     * to be picked up.
     */
    public ShoppingCart() {
        this.ingredients = new ArrayList<>();
    }

    /**
     * Adds a ShoppingCartIngredient to the ShoppingCart.
     * @param ingredient The ShoppingCartIngredient to be added to the
     *                   ShoppingCart.
     */
    public void addIngredient(ShoppingCartIngredient ingredient) {
        this.ingredients.add(ingredient);
    }

    /**
     * Removes a ShoppingCartIngredient from the ShoppingCart.
     * @param ingredient The ShoppingCartIngredient to be removed from the
     *                   ShoppingCart.
     */
    public void removeIngredient(ShoppingCartIngredient ingredient) {
        this.ingredients.remove(ingredient);
    }

    /**
     * Gets a ShoppingCartIngredient from the ShoppingCart at the specified
     * index.
     * @param index The index to get the ShoppingCartIngredient at.
     * @return A ShoppingCartIngredient that was indexed.
     */
    public ShoppingCartIngredient getIngredient(int index) {
        return this.ingredients.get(index);
    }

    /**
     * Gets the entire list of ShoppingCartIngredients in a ShoppingCart.
     * @return The entire ArrayList of ShoppingCartIngredients in the
     * ShoppingCart.
     */
    public ArrayList<ShoppingCartIngredient> getIngredients() {
        return this.ingredients;
    }

    /**
     * Set ingredients in the ShoppingCart.
     *
     * @param ingredients The ingredients to be set in the ShoppingCart.
     *                    This is used to set the ingredients in the
     *                    ShoppingCart.
     */
    public void setIngredients(ArrayList<ShoppingCartIngredient> ingredients) {
        this.ingredients.clear();
        this.ingredients.addAll(ingredients);
    }

}
