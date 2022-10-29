package com.example.wellfed;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.wellfed.recipe.Recipe;
import com.example.wellfed.recipe.RecipeDB;
import com.example.wellfed.recipe.RecipeIngredient;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

@RunWith(AndroidJUnit4.class)
public class RecipeDBTest {
    RecipeDB recipeDB;

    @Before
    public void before(){
        recipeDB = new RecipeDB();
    }

    @Test
    public void testAddRecipe() {
        RecipeIngredient testIngredient = new RecipeIngredient();
        testIngredient.setDescription("Description");
        testIngredient.setAmount(1);
        testIngredient.setCategory("Test");
        testIngredient.setUnit("TestUnits");

        Recipe testRecipe = new Recipe("Test");
        testRecipe.setComments("Test");
        testRecipe.setServings(1);
        testRecipe.setPrepTimeMinutes(1);
        testRecipe.addIngredient(testIngredient);
        testRecipe.setCategory("Test");

        try {
            recipeDB.addRecipe(testRecipe);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Get the recipe document and then edit it

        String testRecipeId = testRecipe.getId();

        Recipe testRecipe2 = null;
        try {
            testRecipe2 = recipeDB.getRecipe(testRecipeId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assert Objects.equals(testRecipe2.getTitle(), "Test");

        testRecipe.setTitle("Test2");
        try {
            recipeDB.editRecipe(testRecipe);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            testRecipe2 = recipeDB.getRecipe(testRecipeId);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assert Objects.equals(testRecipe2.getTitle(), "Test2");

        try {
            recipeDB.delRecipe(testRecipe);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
