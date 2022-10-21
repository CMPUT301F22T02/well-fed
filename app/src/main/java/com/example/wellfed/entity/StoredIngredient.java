package com.example.wellfed.entity;

import java.util.Date;

public class StoredIngredient extends Ingredient{
    private String location;
    private Date bestBefore;
    private int amountStored;
    private String unitStored;

    public StoredIngredient(String category, String description) {
        super(category, description);
    }

    public StoredIngredient(String category, String description, Date bestBefore) {
        super(category, description);
        this.bestBefore = bestBefore;
    }

    public StoredIngredient(String category, String description, String location) {
        super(category, description);
        this.location = location;
    }

    public StoredIngredient(String category, String description, String location, Date bestBefore) {
        super(category, description);
        this.bestBefore = bestBefore;
        this.location = location;
    }

    public StoredIngredient(String category,
                            String description,
                            String location,
                            Date bestBefore,
                            int amountStored,
                            String unitStored) {
        super(category, description);
        this.location = location;
        this.bestBefore = bestBefore;
        this.amountStored = amountStored;
        this.unitStored = unitStored;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getBestBefore() {
        return bestBefore;
    }

    public void setBestBefore(Date bestBefore) {
        this.bestBefore = bestBefore;
    }

    public int getAmountStored() {
        return amountStored;
    }

    public void setAmountStored(int amountStored) {
        this.amountStored = amountStored;
    }

    public String getUnitStored() {
        return unitStored;
    }

    public void setUnitStored(String unitStored) {
        this.unitStored = unitStored;
    }
}
