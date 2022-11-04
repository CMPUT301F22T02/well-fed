package com.example.wellfed;

import static android.content.ContentValues.TAG;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.util.Log;

import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.ingredient.StorageIngredient;
import com.example.wellfed.ingredient.StorageIngredientDB;
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
    /**
     * The MealPlanDB that the meal plans are tested on.
     */
    MealPlanDB mealPlanDB;

    /**
     * The RecipeDB that allows testing whether recipes are valid.
     */
    RecipeDB recipeDB;

    /**
     * The IngredientDB that allows testing whether ingredients are valid.
     */
    StorageIngredientDB storedIngredientDB;

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
    private StorageIngredient mockIngredient() {
        StorageIngredient mockIngredient = new StorageIngredient("Banana");
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
        storedIngredientDB = new StorageIngredientDB();
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
        StorageIngredient mockIngredient = mockIngredient();
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
        assertEquals(mockMealPlan.getId(), result.getId());
        assertEquals(mockMealPlan.getTitle(), result.getTitle());
        assertEquals(mockMealPlan.getCategory(), result.getCategory());
        assertEquals(mockMealPlan.getEatDate(), result.getEatDate());
        assertEquals(mockMealPlan.getServings(), result.getServings());

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

        recipeDB.delRecipeAndIngredient(recipeID);
        storedIngredientDB.removeFromIngredients(ingredientID);
        mealPlanDB.deleteMealPlan(id);
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

    /**
     * Tests updating all fields of a MealPlan.
     */
    @Test
    public void testUpdateMealPlan() throws Exception {
        // test updating a meal plan
        MealPlan mockMealPlan = mockMealPlan();

        Integer servings = 1;
        mockMealPlan.setServings(servings);
        Date eatDate = new Date(2001, 1, 12);
        mockMealPlan.setEatDate(eatDate);
        mockMealPlan.setCategory("Breakfast");

        mealPlanDB.addMealPlan(mockMealPlan);

        // testing editing each field one-by-one
        mockMealPlan.setTitle("Froot Loops with Banana");
        mealPlanDB.editMealPlan(mockMealPlan);
        MealPlan updatedMealPlan = mealPlanDB.getMealPlan(mockMealPlan.getId());
        assertEquals(mockMealPlan.getId(), updatedMealPlan.getId());
        assertEquals(mockMealPlan.getTitle(), updatedMealPlan.getTitle());
        assertEquals(mockMealPlan.getCategory(), updatedMealPlan.getCategory());
        assertEquals(mockMealPlan.getEatDate(), updatedMealPlan.getEatDate());
        assertEquals(mockMealPlan.getServings(), updatedMealPlan.getServings());

        mockMealPlan.setCategory("Brunch");
        mealPlanDB.editMealPlan(mockMealPlan);
        updatedMealPlan = mealPlanDB.getMealPlan(mockMealPlan.getId());
        assertEquals(mockMealPlan.getId(), updatedMealPlan.getId());
        assertEquals(mockMealPlan.getTitle(), updatedMealPlan.getTitle());
        assertEquals(mockMealPlan.getCategory(), updatedMealPlan.getCategory());
        assertEquals(mockMealPlan.getEatDate(), updatedMealPlan.getEatDate());
        assertEquals(mockMealPlan.getServings(), updatedMealPlan.getServings());

        Date newEatDate = new Date(2022, 01, 01);
        mockMealPlan.setEatDate(newEatDate);
        mealPlanDB.editMealPlan(mockMealPlan);
        updatedMealPlan = mealPlanDB.getMealPlan(mockMealPlan.getId());
        assertEquals(mockMealPlan.getId(), updatedMealPlan.getId());
        assertEquals(mockMealPlan.getTitle(), updatedMealPlan.getTitle());
        assertEquals(mockMealPlan.getCategory(), updatedMealPlan.getCategory());
        assertEquals(mockMealPlan.getEatDate(), updatedMealPlan.getEatDate());
        assertEquals(mockMealPlan.getServings(), updatedMealPlan.getServings());

        mockMealPlan.setServings(2);
        mealPlanDB.editMealPlan(mockMealPlan);
        updatedMealPlan = mealPlanDB.getMealPlan(mockMealPlan.getId());
        assertEquals(mockMealPlan.getId(), updatedMealPlan.getId());
        assertEquals(mockMealPlan.getTitle(), updatedMealPlan.getTitle());
        assertEquals(mockMealPlan.getCategory(), updatedMealPlan.getCategory());
        assertEquals(mockMealPlan.getEatDate(), updatedMealPlan.getEatDate());
        assertEquals(mockMealPlan.getServings(), updatedMealPlan.getServings());

        mealPlanDB.deleteMealPlan(mockMealPlan.getId());

    }

    /**
     * Testing adding ingredients and recipes to the MealPlan.
     */
    @Test
    public void testAddIngredientsAndRecipes() throws Exception {
        StorageIngredient ingredient1 = mockIngredient();
        StorageIngredient ingredient2 = mockIngredient();
        Recipe recipe1 = mockRecipe();
        Recipe recipe2 = mockRecipe();
        MealPlan mealPlan = mockMealPlan();

        recipeDB.addRecipe(recipe1);
        recipeDB.addRecipe(recipe2);
        storedIngredientDB.addStoredIngredient(ingredient1);
        storedIngredientDB.addStoredIngredient(ingredient2);

        mealPlanDB.addMealPlan(mealPlan);

        mealPlan.addRecipe(recipe1);
        mealPlan.addRecipe(recipe2);
        mealPlan.addIngredient(ingredient1);
        mealPlan.addIngredient(ingredient2);

        mealPlanDB.editMealPlan(mealPlan);
        MealPlan resultMealPlan = mealPlanDB.getMealPlan(mealPlan.getId());
        Recipe resultRecipe = resultMealPlan.getRecipe(0);
        Ingredient resultIngredient = resultMealPlan.getIngredient(0);

        resultRecipe = mealPlanDB.getMealPlan(mealPlan.getId()).getRecipe(1);
        resultIngredient = mealPlanDB.getMealPlan(mealPlan.getId()).getIngredient(1);

        Log.d(TAG, "res"+ resultRecipe.getId());
        Log.d(TAG, "res"+ resultIngredient.getId());
        assertEquals(mealPlan.getRecipe(1).getId(), resultRecipe.getId());
        assertEquals(mealPlan.getIngredient(1).getId(), resultIngredient.getId());

        recipeDB.delRecipeAndIngredient(recipe1.getId());
        recipeDB.delRecipeAndIngredient(recipe2.getId());
        storedIngredientDB.removeFromIngredients(ingredient1.getId());
        storedIngredientDB.removeFromIngredients(ingredient2.getId());
        mealPlanDB.deleteMealPlan(mealPlan.getId());
    }

    /**
     * Testing deleting a MealPlan
     */
    @Test
    public void testDeleteMealPlan() throws Exception {
        // test deleting a meal plan
        MealPlan mealPlan = mockMealPlan();
        mealPlanDB.addMealPlan(mealPlan);
        mealPlanDB.deleteMealPlan(mealPlan.getId());

        assertNull(mealPlanDB.getMealPlan(mealPlan.getId()));
    }
}
