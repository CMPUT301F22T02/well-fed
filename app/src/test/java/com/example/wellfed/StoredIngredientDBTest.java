package com.example.wellfed;

import com.example.wellfed.ingredient.StoredIngredient;
import com.example.wellfed.ingredient.StoredIngredientDB;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Date;

public class StoredIngredientDBTest {
    @Test
    public void addIngredient() {
        StoredIngredient ingredient = new StoredIngredient("Broccoli");
        ingredient.setCategory("Vegetable");
        ingredient.setAmount(2);
        ingredient.setUnit("kg");
        ingredient.setLocation("fridge");
        Date bestBefore = new Date(2022, 10, 2);
        ingredient.setBestBefore(bestBefore);

        StoredIngredientDB ingredientDB = new StoredIngredientDB();
        ingredientDB.addStoredIngredient(ingredient);
    }
}
