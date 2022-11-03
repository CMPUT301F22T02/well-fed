package com.example.wellfed.ingredient;


import java.util.Date;

public class StorageIngredient extends Ingredient {
    /**
     * The amount of the ingredient in the storage.
     */
    private Float amount;
    /**
     * The unit of the ingredient in the storage.
     */
    private String unit;

    /**
     * The location of the ingredient in the storage.
     */
    private String location;

    /**
     * The best before date of the ingredient in the storage.
     */
    private Date bestBefore;


    /**
     * Creates a new StorageIngredient object without data.
     */
    public StorageIngredient(String name) {
        super(name);
    }

    /**
     * Creates a new StorageIngredient object that represents an Ingredient used for various meal purposes.
     *
     * @param description The description/title of an Ingredient
     * @param amount      The amount of the ingredient in the storage.
     * @param unit        The unit of the ingredient in the storage.
     * @param location    The location of the ingredient in the storage.
     * @param bestBefore  The best before date of the ingredient in the storage.
     */
    public StorageIngredient(String description, Float amount,
                             String unit, String location, Date bestBefore) {
        super(description);
        this.amount = amount;
        this.unit = unit;
        this.location = location;
        this.bestBefore = bestBefore;
    }

    /**
     * Gets the amount of the ingredient in the storage.
     *
     * @return The amount of the ingredient in the storage
     */
    public Float getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the ingredient in the storage.
     *
     * @param amount The amount of the ingredient in the storage
     */
    public void setAmount(Float amount) {
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
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * Gets the location of the ingredient in the storage.
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

    /**
     * Sets the best before date of the ingredient in the storage.
     */
    public void setBestBefore(Date bestBefore) {
        this.bestBefore = bestBefore;
    }

    /**
     * Gets the best before date of the ingredient in the storage.
     *
     * @return The best before date of the ingredient in the storage
     */
    public String getBestBefore() {
        String bestBeforeString = bestBefore.toString();
        String[] bestBeforeSplit = bestBeforeString.split(" ");
        return bestBeforeSplit[0] + ", " + bestBeforeSplit[1] + " " + bestBeforeSplit[2];
    }

    /**
     * Combines the amount and unit of the ingredient in the storage.
     *
     * @return The amount and unit of the ingredient in the storage
     */
    public String getAmountAndUnit() {
        return amount + " " + unit;
    }
}