package com.example.wellfed.ingredient;


import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * An Ingredient represented in a user's storage, with added amount, unit, location, and best before.
 */
public class StorageIngredient extends Ingredient implements Comparable<StorageIngredient> {
    /**
     * The amount of the ingredient in the storage.
     */
    private Double amount;
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
     * The category of the ingredient.
     */
    private String category;
    /**
     * The id of the storage ingredient.
     */
    private String storageId;

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
    public StorageIngredient(String description, Double amount,
                             String unit, String location, Date bestBefore) {
        super(description);
        this.amount = amount;
        this.unit = unit;
        this.location = location;
        this.bestBefore = bestBefore;
    }

    /**
     * Creates a new StorageIngredient object that represents an Ingredient used for various meal purposes.
     *
     * @param description The description/title of an Ingredient
     * @param amount      The amount of the ingredient in the storage.
     * @param unit        The unit of the ingredient in the storage.
     * @param location    The location of the ingredient in the storage.
     * @param bestBefore  The best before date of the ingredient in the storage.
     * @param category  The categories of the ingredient.
     */
    public StorageIngredient(String description, Double amount,
                             String unit, String location, Date bestBefore, String category) {
        super(description);
        this.amount = amount;
        this.unit = unit;
        this.location = location;
        this.bestBefore = bestBefore;
        this.category = category;
    }

    /**
     * Set categories of the ingredient.
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Get categories of the ingredient.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Gets the amount of the ingredient in the storage.
     *
     * @return The amount of the ingredient in the storage
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the ingredient in the storage.
     *
     * @param amount The amount of the ingredient in the storage
     */
    public void setAmount(Double amount) {
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
    public Date getBestBefore() {
        return bestBefore;
    }

    /**
     * Combines the amount and unit of the ingredient in the storage.
     *
     * @return The amount and unit of the ingredient in the storage
     */
    public String getAmountAndUnit() {
        return amount + " " + unit;
    }

    /**
     * Checks if the ingredient is equal to another ingredient.
     * @param o The ingredient to compare to.
     * @return True if the ingredients are equal, false otherwise.
     */
    @Override
    public int compareTo(StorageIngredient o) {
        return this.getDescription().compareTo(o.getDescription());
    }

    /**
     * Checks whether the ingredient is equal to another ingredient.
     *
     * @param o the object to check equality with
     *
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean isEqual(Object o) {
        if (o.getClass() != StorageIngredient.class) {
            return false;
        }

        ArrayList<Boolean> flags = new ArrayList<Boolean>();
        flags.add(Objects.equals(this.getId(), ((StorageIngredient) o).getId()));
        flags.add(Objects.equals(this.getDescription(), ((StorageIngredient) o).getDescription()));
        flags.add(Objects.equals(this.getCategory(), ((StorageIngredient) o).getCategory()));
        flags.add(Objects.equals(this.getUnit(), ((StorageIngredient) o).getUnit()));
        flags.add(Objects.equals(this.getAmount(), ((StorageIngredient) o).getAmount()));
        flags.add(Objects.equals(this.getBestBefore(), ((StorageIngredient) o).getBestBefore()));
        flags.add(Objects.equals(this.getStorageId(), ((StorageIngredient) o).getStorageId()));
        flags.add(Objects.equals(this.getAmountAndUnit(), ((StorageIngredient) o).getAmountAndUnit()));

        return !flags.contains(false);
    }

    /**
     * Get the database ID of a storage ingredient. This returns null if the
     * storage ingredient is not in DB.
     * @return the ID of the storage ingredient
     */
    public String getStorageId() {
        return storageId;
    }

    /**
     * Sets the database ID of a storage ingredient.
     * @param storageId the ID of the storage ingredient
     */
    public void setStorageId(String storageId) {
        this.storageId = storageId;
    }
}