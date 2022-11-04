package com.example.wellfed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.util.Log;

import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.ingredient.StoredIngredient;
import com.example.wellfed.ingredient.StoredIngredientDB;
import com.example.wellfed.mealplan.MealPlan;
import com.example.wellfed.mealplan.MealPlanDB;
import com.example.wellfed.recipe.Recipe;
import com.example.wellfed.recipe.RecipeDB;
import com.example.wellfed.recipe.RecipeIngredientDB;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

/**
 * Tests for the MealPlanDB. Please note: These tests require functional StoredIngredientDB
 * and RecipeDB. This is because the MealPlanDB requires that all StoredIngredient and
 * RecipeIngredients exist in the database before attempting to create a MealPlan.
 *
 * Unfortunately, due to the nature of the database, it is hard to test only one function at once.
 * So, you will see some tests call delete methods, even when they are not testing deletes.
 */
public class MealPlanDBTest {
    MealPlanDB mealPlanDB;
    RecipeDB recipeDB;
    StoredIngredientDB storedIngredientDB;

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
        Recipe mockRecipe = new Recipe("Cereal");
        mockRecipe.setComments("This lovely recipe is great for a burst of energy in the morning.");
        mockRecipe.setPrepTimeMinutes(3);
        mockRecipe.setServings(1);
        mockRecipe.setCategory("Breakfast");
        return mockRecipe;
    }

    /**
     * Creates a new mock ingredient to be used in testing.
     * @return the created mock ingredient
     */
    private StoredIngredient mockIngredient() {
        StoredIngredient mockIngredient = new StoredIngredient("Banana");
        mockIngredient.setAmount(1.0f);
        mockIngredient.setUnit("count");
        mockIngredient.setCategory("Fruit");
        return mockIngredient;
    }

    /**
     * Sets up the mealPlanDb before any of the tests are run.
     */
    @Before
    public void before() {
        mealPlanDB = new MealPlanDB();
        recipeDB = new RecipeDB();
        storedIngredientDB = new StoredIngredientDB();
    }

    /**
     * Tests addMealPlan functionality and getMealPlan functionality. These cannot be separated
     * as checking whether a MealPlan exists in the db requires adding something to the db.
     *
     * Please note: These tests require working functionality of addMealPlan.
     */
    @Test
    public void testAddAndGetMealPlan() throws Exception {
        // test adding a recipe with full fields
        MealPlan mockMealPlan = mockMealPlan();
        StoredIngredient mockIngredient = mockIngredient();
        Ingredient mockIngredientRecipe = mockIngredient();

        // setting up Ingredient in DB
        String ingredientID = storedIngredientDB.addStoredIngredient(mockIngredient);

        // setting up recipe with an ingredient
        Recipe mockRecipe = mockRecipe();
        mockRecipe.addIngredient(mockIngredientRecipe);

        String recipeID = recipeDB.addRecipe(mockRecipe);

        Integer servings = 1;
        mockMealPlan.setServings(servings);
        Date eatDate = new Date(2001, 1, 12);
        mockMealPlan.setEatDate(eatDate);
        mockMealPlan.setCategory("Breakfast");
        mockMealPlan.addIngredient(mockIngredient);
        mockMealPlan.addRecipe(mockRecipe);

        String id = mealPlanDB.addMealPlan(mockMealPlan);

        MealPlan result = mealPlanDB.getMealPlan(id);
        assertEquals(id, result.getId());
        assertEquals("Cereal and Banana Breakfast", result.getTitle());
        assertEquals("Breakfast", result.getCategory());
        assertEquals(eatDate, result.getEatDate());
        assertEquals((Integer) 1, result.getServings());

        // making sure we can access recipe
        Recipe afterRecipe = result.getRecipe(0);
        assertEquals(mockRecipe.getTitle(), afterRecipe.getTitle());
        assertEquals(mockRecipe.getComments(), afterRecipe.getComments());
        assertEquals(mockRecipe.getPrepTimeMinutes(), afterRecipe.getPrepTimeMinutes());
        assertEquals(mockRecipe.getServings(), afterRecipe.getServings());
        assertEquals(mockRecipe.getCategory(), afterRecipe.getCategory());
        assertEquals(mockRecipe.getId(), afterRecipe.getId());

        // making sure we can access ingredient
        Ingredient afterIngredient = result.getIngredient(0);
        assertEquals(mockIngredient.getAmount(), afterIngredient.getAmount());
        assertEquals(mockIngredient.getUnit(), afterIngredient.getUnit());
        assertEquals(mockIngredient.getDescription(), afterIngredient.getDescription());
        assertEquals(mockIngredient.getCategory(), afterIngredient.getCategory());
        assertEquals(mockIngredient.getId(), afterIngredient.getId());

        recipeDB.delRecipe(recipeID);
        storedIngredientDB.removeFromIngredients(ingredientID);
        mealPlanDB.deleteMealPlan(id);
    }

    @Test
    public void testUpdateMealPlan() {
        // test updating a meal plan

    }

    @Test
    public void testDeleteMealPlan() {
        // test deleting a meal plan
    }

    @Test
    public void testGetNonExistentMealPlan() {

    }

    /**
     * Tests adding and getting an empty MealPlan, with only a title.
     */
    @Test
    public void testAddEmptyMealPlan() throws Exception {
        // test adding a recipe with nothing in it
        MealPlan mock = mockMealPlan();
        String id = mealPlanDB.addMealPlan(mock);

        MealPlan result = mealPlanDB.getMealPlan(id);
        assertEquals(id, result.getId());
        assertEquals(mock.getTitle(), result.getTitle());
        assertNull(result.getCategory());
        assertNull(result.getEatDate());
        assertNull(result.getServings());
        assertTrue(result.getRecipes().isEmpty());
        assertTrue(result.getIngredients().isEmpty());

        mealPlanDB.deleteMealPlan(id);
    }



}
