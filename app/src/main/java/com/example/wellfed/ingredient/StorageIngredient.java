package com.example.wellfed.ingredient;


import java.util.ArrayList;

public class StorageIngredient extends Ingredient {
    /**
     * The amount of the ingredient in the storage.
     */
    private int amount;
    /**
     * The unit of the ingredient in the storage.
     */
    private String unit;

    /**
     * The location of the ingredient in the storage.
     */
    private String location;

    /**
     * Creates a new StorageIngredient object that represents an Ingredient used for various meal purposes.
     *
     * @param description The description/title of an Ingredient
     */
    public StorageIngredient(String description, int amount,
                             String unit, String location) {
        super(description);
        this.amount = amount;
        this.unit = unit;
        this.location = location;
    }

    /**
     * Gets the amount of the ingredient in the storage.
     *
     * @return The amount of the ingredient in the storage
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the ingredient in the storage.
     *
     * @param amount The amount of the ingredient in the storage
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Gets the unit of the ingredient in the storage.
     *
     * @return The unit of the ingredient in the storage
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Sets the unit of the ingredient in the storage.
     *
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     *  Gets the location of the ingredient in the storage.
     *
     * @return The location of the ingredient in the storage
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the ingredient in the storage.
     *
     * @param location The location of the ingredient in the storage
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Gets the description (aka title) of an Ingredient
     *
     * @return The String representing the description (aka title) of the Ingredient
     */
    public String getDescription() {
        return super.getDescription();
    }

    /**
     * Sets the description (aka title) of an Ingredient
     *
     * @param description The String representing the description (aka title) of the Ingredient
     */
    public void setDescription(String description) {
        super.setDescription(description);
    }
}