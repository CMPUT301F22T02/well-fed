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
     * Holds an ArrayList of all of the categories associated with one Ingredient.
     */
    private final ArrayList<String> categories;
    /**
     * Holds the units that an amount of an Ingredient is in
     */
    private String unit;

    /**
     * Creates a new Ingredient object that represents an Ingredient used for various meal purposes.
     * @param description The description/title of an Ingredient
     */
    public Ingredient(String description) {
        this.description = description;
        this.categories = new ArrayList<>();
    }

    /**
     * Adds a category (aka tag) to an Ingredient object.
     * @param category The category to add
     */
    public void addCategory(String category) {
        categories.add(category);
    }

    /**
     * Removes a category (aka tag) from an Ingredient object.
     * @param category The category to remove
     */
    public void removeCategory(String category) {
        categories.remove(category);
    }

    /**
     * Gets the category at the specified index.
     * @param index The index where the category is at
     * @return The String representing the category
     */
    public String getCategory(int index) {
        return categories.get(index);
    }

    /**
     * Gets the entire ArrayList of categories.
     * @return The ArrayList of Strings containing all categories
     */
    public ArrayList<String> getCategories() {
        return categories;
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

    public Ingredient(){
        this.categories = new ArrayList<>();
    }
}
