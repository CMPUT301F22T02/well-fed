/**
 * StoredIngredient
 *
 * Version 1.0
 *
 * 21 October, 2022
 *
 * Copyright (c) 2022.  0xFFFFFF
 * All rights reserved.
 */

package com.example.wellfed.ingredient;

import com.example.wellfed.ingredient.FoodStorage;
import com.example.wellfed.ingredient.Ingredient;

import java.util.Date;

/**
 * This class represents a StoredIngredient, which are Ingredients that can be added to FoodStorage.
 *
 * @version 1.0
 * @see FoodStorage
 */
public class StoredIngredient extends Ingredient {
    /**
     * Holds the location of a StoredIngredient.
     */
    private String location;
    /**
     * Holds the best before date of a StoredIngredient.
     */
    private Date bestBefore;

    /**
     * Creates a StoredIngredient object which represents an Ingredient that can be added
     * to FoodStorage.
     * @param name A String representing the name of a StoredIngredient.
     */
    public StoredIngredient(String name) {
        super(name);
    }

    /**
     * Gets the location of a StoredIngredient
     * @return A String representing the location of a StoredIngredient
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of a StoredIngredient
     * @param location A String representing the location of a StoredIngredient
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the best before date of a StoredIngredient
     * @return A Date representing the best before of a StoredIngredient
     */
    public Date getBestBefore() {
        return bestBefore;
    }

    /**
     * Sets the best before date of a StoredIngredient
     * @param bestBefore A Date representing the best before of a StoredIngredient
     */
    public void setBestBefore(Date bestBefore) {
        this.bestBefore = bestBefore;
    }
}
