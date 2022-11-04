package com.example.wellfed;

import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.mealplan.MealPlan;
import com.example.wellfed.mealplan.MealPlanDB;
import com.example.wellfed.recipe.Recipe;
import com.example.wellfed.recipe.RecipeDB;
import com.example.wellfed.recipe.RecipeIngredientDB;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the MealPlanDB. Please note: These tests require functional StoredIngredientDB
 * and RecipeDB. This is because the MealPlanDB requires that all StoredIngredient and
 * RecipeIngredients exist in the database before attempting to create a MealPlan.
 */
public class MealPlanDBTest {
    MealPlanDB mealPlanDB;

    /**
     * Creates a new mock meal plan to be used in testing.
     * @return the created mock meal plan
     */
    private MealPlan mockMealPlan() {
        return new MealPlan("Cereal and Banana Breakfast");
    }

    /**
     * Creates a new mock recipe to be used in testing.
     * @return the created mock recipe
     */
    private Recipe mockRecipe() {
        return new Recipe("Cereal");
    }

    /**
     * Creates a new mock ingredient to be used in testing.
     * @return the created mock ingredient
     */
    private Ingredient mockIngredient() {
        return  new Ingredient("Banana");
    }

    /**
     * Sets up the mealPlanDb before any of the tests are run.
     */
    @Before
    public void before() {
        mealPlanDB = new MealPlanDB();
    }

    /**
     * Tests addMealPlan functionality and getMealPlan functionality. These cannot be separated
     * as checking whether a MealPlan exists in the db requires adding something to the db.
     *
     * Please note: These tests require working functionality of add
     */
    @Test
    public void testAddAndGetMealPlan() throws Exception {
        MealPlan mock = mockMealPlan();
        mealPlanDB.addMealPlan(mock);
    }
}
