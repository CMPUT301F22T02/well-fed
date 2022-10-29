package com.example.wellfed.recipe;

import com.example.wellfed.ingredient.Ingredient;

import java.io.Serializable;

public class RecipeIngredient extends Ingredient implements Serializable {

    private String id;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



}
