package com.example.wellfed.entity;

public class RecipeOrMealIngredient extends Ingredient{
    private int amount;
    private String unit;

    public RecipeOrMealIngredient(String category, String description) {
        super(category, description);
    }

    public RecipeOrMealIngredient(String category, String description, int amount, String unit) {
        super(category, description);
        this.amount = amount;
        this.unit = unit;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
