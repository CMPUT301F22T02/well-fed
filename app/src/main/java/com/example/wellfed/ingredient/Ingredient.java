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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.example.wellfed.recipe.Recipe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

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
    private Double amount;

    /**
     * Holds the database ID of an Ingredient
     */
    private String id;

    /**
     * Creates a new Ingredient object that represents an Ingredient used for various meal purposes.
     * @param description The description/title of an Ingredient
     */
    public Ingredient(String description) {
        this.description = description;
    }

    /**
     * Creates a new Ingredient object that represents an Ingredient used for various meal purposes.
     */
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
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * Gets the amount of an ingredient
     * @return the amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Get the database ID of an ingredient. This returns null if the ingredient is not in DB.
     * @return the ID of the ingredient
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the database ID of an ingredient.
     * @param id the ID of the ingredient
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Checks whether the ingredient is equal to another ingredient.
     *
     * @param o the object to check equality with
     *
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o.getClass() != Ingredient.class) {
            return false;
        }

        ArrayList<Boolean> flags = new ArrayList<Boolean>();
        flags.add(Objects.equals(this.getId(), ((Ingredient) o).getId()));
        flags.add(Objects.equals(this.getDescription(), ((Ingredient) o).getDescription()));
        flags.add(Objects.equals(this.getCategory(), ((Ingredient) o).getCategory()));
        flags.add(Objects.equals(this.getUnit(), ((Ingredient) o).getUnit()));
        flags.add(Objects.equals(this.getAmount(), ((Ingredient) o).getAmount()));

        return !flags.contains(false);
    }
}
