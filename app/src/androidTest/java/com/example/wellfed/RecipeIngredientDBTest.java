package com.example.wellfed;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import com.example.wellfed.recipe.RecipeIngredient;
import com.example.wellfed.recipe.RecipeIngredientDB;

import org.junit.Before;
import org.junit.Test;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class RecipeIngredientDBTest {
    RecipeIngredientDB recipeIngredientDB;

    @Before
    public void before(){
        recipeIngredientDB = new RecipeIngredientDB();
    }

    @Test
    public void testAddandDelRecipeIngredients(){

        RecipeIngredient testIngredient = new RecipeIngredient();
        testIngredient.setDescription("Description");
        testIngredient.setAmount(1);
        testIngredient.setCategory("Test");
        testIngredient.setUnit("TestUnits");
        try {
            recipeIngredientDB.addRecipeIngredient(testIngredient);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        RecipeIngredient testIngredientFromDb = null;

        try {
            testIngredientFromDb = recipeIngredientDB.getRecipeIngredient(testIngredient.getId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        assert Objects.equals(testIngredient.getId(), testIngredientFromDb.getId());
        assert Objects.equals(testIngredient.getAmount(), testIngredientFromDb.getAmount());
        assert Objects.equals(testIngredient.getCategory(), testIngredientFromDb.getCategory());
        assert Objects.equals(testIngredient.getDescription(), testIngredientFromDb.getDescription());
        assert Objects.equals(testIngredient.getUnit(), testIngredientFromDb.getUnit());

        testIngredient.setAmount((float) 5.0);
        try {
            recipeIngredientDB.updateRecipeIngredient(testIngredient);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            testIngredientFromDb = recipeIngredientDB.getRecipeIngredient(testIngredient.getId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assert Objects.equals(testIngredient.getAmount(), testIngredientFromDb.getAmount());

        try {
            recipeIngredientDB.delIngredient(testIngredient.getId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
