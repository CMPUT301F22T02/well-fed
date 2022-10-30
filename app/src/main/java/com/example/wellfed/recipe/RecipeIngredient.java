package com.example.wellfed.recipe;

import com.example.wellfed.ingredient.Ingredient;

import java.io.Serializable;

public class RecipeIngredient extends Ingredient implements Serializable {

    private float amount;


    public Float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }



}
