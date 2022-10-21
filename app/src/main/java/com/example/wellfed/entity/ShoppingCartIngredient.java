package com.example.wellfed.entity;

public class ShoppingCartIngredient extends Ingredient{
    boolean isPickedUp;

    public ShoppingCartIngredient(String category, String description) {
        super(category, description);
        isPickedUp = false;
    }
}
