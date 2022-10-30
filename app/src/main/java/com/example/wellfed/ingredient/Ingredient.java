/**
 * Ingredient
 *
 * Version 1.0
 *
 * 21 October, 2022
 *
 * Copyright (c) 2022.  0xFFFFFF
 * All rights reserved.
 */

package com.example.wellfed.ingredient;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class represents an Ingredient which can be used in many ways in a MealPlanning application.
 *
 * @version 1.0
 */
public class Ingredient implements Serializable {
    /**
     * Holds the description of an Ingredient. This can also be considered to be a title.
     */
    private String description;
    /**
     * Holds the category of an Ingredient.
     */
    private String category;
    /**
     * Holds the units that an amount of an Ingredient is in
     */
    private String unit;

    /**
     * Holds the number of items in an Ingredient
     */
    private Float amount;

    /**
     * Creates a new Ingredient object that represents an Ingredient used for various meal purposes.
     * @param description The description/title of an Ingredient
     */
    public Ingredient(String description) {
        this.description = description;
    }

    public Ingredient() {
        this.description = null;
    }

    /**
     * Sets the category (aka tag) of an Ingredient object.
     * @param category The category to add
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets the category.
     * @return The String representing the category
     */
    public String getCategory() {
        return category;
    }

    /**
     * Gets the description (aka title) of an Ingredient
     * @return The String representing the description (aka title) of the Ingredient
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description (aka title) of an Ingredient
     * @param description The String representing the description (aka title) of the Ingredient
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets a String representing the unit of an amount
     * @return A String representing the unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Sets the unit of an amount
     * @param unit A String representing the unit
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Sets the amount of an ingredient
     * @param amount the amount
     */
    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Float getAmount() {
        return amount;
    }
}
