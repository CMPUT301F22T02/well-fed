package com.example.wellfed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.mealplan.MealPlan;
import com.example.wellfed.recipe.Recipe;

import org.junit.Test;

import java.util.Date;
import java.util.List;

/**
 * Unit tests for the MealPlan model class.
 */
public class MealPlanTest {
    /**
     * Creates a mock MealPlan to be used in the tests.
     * @return the mock MealPlan created
     */
    private MealPlan mockMealPlan() {
        return new MealPlan("Thanksgiving Dinner");
    }

    /**
     * Creates a mock MealIngredient to be used in the tests.
     * @return the mock MealIngredient created
     */
    private Ingredient mockMealIngredient(){return new Ingredient();}

    /**
     * Creates a mock Recipe to be used in the tests.
     * @return the mock Recipe created
     */
    private Recipe mockMealRecipe(){
        Recipe recipe = new Recipe("Stuffing");
        return recipe;
    }

    /**
     * Tests adding of an Ingredient to the MealPlan.
     */
    @Test
    public void testAddAndGetIngredient() {
        MealPlan mealPlan = mockMealPlan();
        Ingredient ingredient1 = mockMealIngredient();
        Ingredient ingredient2 = mockMealIngredient();
        ingredient1.setDescription("Milk");
        ingredient2.setDescription("Dinner rolls");

        assertThrows(IndexOutOfBoundsException.class, () -> {
            mealPlan.getIngredient(0);
        });
        mealPlan.addIngredient(ingredient1);
        assertEquals(ingredient1, mealPlan.getIngredient(0));

        mealPlan.addIngredient(ingredient2);
        assertEquals(ingredient2, mealPlan.getIngredient(1));
    }

    /**
     * Tests deleting of an Ingredient from the MealPlan.
     */
    @Test
    public void testRemoveIngredient() {
        MealPlan mealPlan = mockMealPlan();
        Ingredient ingredient1 = mockMealIngredient();
        Ingredient ingredient2 = mockMealIngredient();
        ingredient1.setDescription("Milk");
        ingredient2.setDescription("Dinner rolls");

        mealPlan.addIngredient(ingredient1);
        mealPlan.addIngredient(ingredient2);

        mealPlan.removeIngredient(ingredient1);
        assertEquals(ingredient2, mealPlan.getIngredient(0));
        assertThrows(IndexOutOfBoundsException.class, () -> {
            mealPlan.getIngredient(1);
        });

        mealPlan.removeIngredient(ingredient2);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            mealPlan.getIngredient(0);
        });
    }

    /**
     * Tests the behaviour of removing an ingredient that is not in the MealPlan.
     */
    @Test
    public void testRemoveIngredientNotInMealPlan() {
        MealPlan mealPlan = mockMealPlan();
        Ingredient ingredient = mockMealIngredient();
        Ingredient ingredient2 = mockMealIngredient();
        // no error should be thrown, as deleting a non existing ingredient is fine
        mealPlan.removeIngredient(ingredient);

        // a removed ingredient not in recipe should not affect other ingredients
        mealPlan.addIngredient(ingredient);
        mealPlan.removeIngredient(ingredient2);
        assertEquals(ingredient, mealPlan.getIngredient(0));
    }

    /**
     * Tests adding and getting of a Recipe to/from the MealPlan.
     */
    @Test
    public void testAddAndGetRecipe() {
        MealPlan mealPlan = mockMealPlan();
        Recipe recipe1 = mockMealRecipe();
        Recipe recipe2 = mockMealRecipe();

        assertThrows(IndexOutOfBoundsException.class, () -> {
            mealPlan.getRecipe(0);
        });

        mealPlan.addRecipe(recipe1);
        mealPlan.addRecipe(recipe2);

        assertEquals(recipe1, mealPlan.getRecipe(0));
        assertEquals(recipe2, mealPlan.getRecipe(1));
    }

    /**
     * Tests deleting of an Recipe from the MealPlan.
     */
    @Test
    public void testRemoveRecipe() {
        MealPlan mealPlan = mockMealPlan();
        Recipe recipe1 = mockMealRecipe();
        Recipe recipe2 = mockMealRecipe();

        mealPlan.addRecipe(recipe1);
        mealPlan.addRecipe(recipe2);

        mealPlan.removeRecipe(recipe1);
        assertEquals(recipe2, mealPlan.getRecipe(0));
        assertThrows(IndexOutOfBoundsException.class, () -> {
            mealPlan.getRecipe(1);
        });

        mealPlan.removeRecipe(recipe2);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            mealPlan.getIngredient(0);
        });
    }

    /**
     * Tests the behaviour of removing an Recipe that is not in the MealPlan.
     */
    @Test
    public void testRemoveRecipeNotInMealPlan() {
        MealPlan mealPlan = mockMealPlan();
        Recipe recipe1 = mockMealRecipe();
        Recipe recipe2 = mockMealRecipe();
        // no error should be thrown, as deleting a non existing ingredient is fine
        mealPlan.removeRecipe(recipe1);

        // a removed ingredient not in recipe should not affect other ingredients
        mealPlan.addRecipe(recipe1);
        mealPlan.removeRecipe(recipe2);
        assertEquals(recipe1, mealPlan.getRecipe(0));
    }

    /**
     * Tests getting the List of Ingredients from a MealPlan.
     */
    @Test
    public void testGetIngredients() {
        MealPlan mealPlan = mockMealPlan();
        Ingredient ingredient = mockMealIngredient();
        Ingredient ingredient2 = mockMealIngredient();

        mealPlan.addIngredient(ingredient);
        mealPlan.addIngredient(ingredient2);
        List<Ingredient> ingredientList = mealPlan.getIngredients();

        assertEquals(ingredient, ingredientList.get(0));
        assertEquals(ingredient2, ingredientList.get(1));
    }

    /**
     * Tests getting the List of Ingredients from a MealPlan.
     */
    @Test
    public void testGetRecipes() {
        MealPlan mealPlan = mockMealPlan();
        Recipe recipe = mockMealRecipe();
        Recipe recipe2 = mockMealRecipe();

        mealPlan.addRecipe(recipe);
        mealPlan.addRecipe(recipe2);
        List<Recipe> recipeList = mealPlan.getRecipes();

        assertEquals(recipe, recipeList.get(0));
        assertEquals(recipe2, recipeList.get(1));
    }

    /**
     * Tests and setting the category of a MealPlan.
     */
    @Test
    public void testCategory() {
        MealPlan mealPlan = mockMealPlan();

        // test with string
        String string = "Dinner";
        mealPlan.setCategory(string);
        assertEquals(string, mealPlan.getCategory());

        // test with literal
        mealPlan.setCategory("Supper");
        assertEquals("Supper", mealPlan.getCategory());
    }

    /**
     * Tests getting and setting the title of a MealPlan.
     */
    @Test
    public void testTitle() {
        MealPlan mealPlan = mockMealPlan();

        // test with string
        String string = "Christmas Dinner";
        mealPlan.setTitle(string);
        assertEquals(string, mealPlan.getTitle());

        // test with literal
        mealPlan.setTitle("Christmas Dinner");
        assertEquals("Christmas Dinner", mealPlan.getTitle());
    }

    /**
     * Tests getting and setting the servings of a MealPlan.
     */
    @Test
    public void testServings() {
        MealPlan mealPlan = mockMealPlan();

        // test with integer
        Integer servings = 6;
        mealPlan.setServings(servings);
        assertEquals(servings, mealPlan.getServings());
    }

    /**
     * Tests getting and setting the eat date of the MealPlan
     */
    @Test
    public void testEatDate() {
        MealPlan mealPlan = mockMealPlan();

        Date eatDate = new Date();
        mealPlan.setEatDate(eatDate);
        assertEquals(eatDate, mealPlan.getEatDate());
    }

    /**
     * Tests that a meal plan is always equals if all of the fields are identical.
     * This is done by changing each field individually, and then invoking equals
     */
    @Test
    public void testEquals() {
        Ingredient mockIngredient = mockMealIngredient();
        Ingredient mockIngredient2 = mockMealIngredient();
        mockIngredient.setDescription("Milk");
        mockIngredient2.setDescription("Frosted Flakes");

        Recipe mockRecipe = mockMealRecipe();
        Recipe mockRecipe2 = mockMealRecipe();
        mockRecipe.setTitle("Cereal");
        mockRecipe2.setTitle("Glass of milk");
        mockRecipe.addIngredient(mockIngredient);
        mockRecipe.addIngredient(mockIngredient2);
        mockRecipe2.addIngredient(mockIngredient);

        MealPlan mockMealPlan = mockMealPlan();
        MealPlan mockMealPlan2 = mockMealPlan();

        assertTrue(mockMealPlan.equals(mockMealPlan2));
        assertTrue(mockMealPlan2.equals(mockMealPlan));

        mockMealPlan.setTitle("Cereal");
        mockMealPlan2.setTitle("Cereal");
        assertTrue(mockMealPlan.equals(mockMealPlan2));
        assertTrue(mockMealPlan2.equals(mockMealPlan));

        mockMealPlan.setId("ID");
        mockMealPlan2.setId("ID");
        assertTrue(mockMealPlan.equals(mockMealPlan2));
        assertTrue(mockMealPlan2.equals(mockMealPlan));

        mockMealPlan.setCategory("Breakfast");
        mockMealPlan2.setCategory("Breakfast");
        assertTrue(mockMealPlan.equals(mockMealPlan2));
        assertTrue(mockMealPlan2.equals(mockMealPlan));

        mockMealPlan.setServings(5);
        mockMealPlan2.setServings(5);
        assertTrue(mockMealPlan.equals(mockMealPlan2));
        assertTrue(mockMealPlan2.equals(mockMealPlan));

        mockMealPlan.setEatDate(new Date(11, 12, 2022));
        mockMealPlan2.setEatDate(new Date(11, 12, 2022));
        assertTrue(mockMealPlan.equals(mockMealPlan2));
        assertTrue(mockMealPlan2.equals(mockMealPlan));


        // adding ingredients out of order
        mockMealPlan.addIngredient(mockIngredient);
        mockMealPlan.addIngredient(mockIngredient2);

        mockMealPlan2.addIngredient(mockIngredient2);
        mockMealPlan2.addIngredient(mockIngredient);
        assertTrue(mockMealPlan.equals(mockMealPlan2));
        assertTrue(mockMealPlan2.equals(mockMealPlan));

        // adding recipes out of order
        mockMealPlan.addRecipe(mockRecipe);
        mockMealPlan.addRecipe(mockRecipe2);

        mockMealPlan2.addRecipe(mockRecipe2);
        mockMealPlan2.addRecipe(mockRecipe);
        assertTrue(mockMealPlan.equals(mockMealPlan2));
        assertTrue(mockMealPlan2.equals(mockMealPlan));
    }

    /**
     * Tests that a meal plan is always equals if all of the fields are identical.
     * This is done by changing each field individually, and then invoking equals
     */
    @Test
    public void testNotEquals() {
        Ingredient mockIngredient = mockMealIngredient();
        Ingredient mockIngredient2 = mockMealIngredient();
        mockIngredient.setDescription("Milk");
        mockIngredient2.setDescription("Frosted Flakes");

        Recipe mockRecipe = mockMealRecipe();
        Recipe mockRecipe2 = mockMealRecipe();
        mockRecipe.setTitle("Cereal");
        mockRecipe2.setTitle("Glass of milk");
        mockRecipe.addIngredient(mockIngredient);
        mockRecipe.addIngredient(mockIngredient2);
        mockRecipe2.addIngredient(mockIngredient);

        MealPlan mockMealPlan = mockMealPlan();
        MealPlan mockMealPlan2 = mockMealPlan();

        assertTrue(mockMealPlan.equals(mockMealPlan2));
        assertTrue(mockMealPlan2.equals(mockMealPlan));

        mockMealPlan.setTitle("Cereal");
        mockMealPlan2.setTitle("Frosted Flakes");
        assertFalse(mockMealPlan.equals(mockMealPlan2));
        assertFalse(mockMealPlan2.equals(mockMealPlan));

        mockMealPlan2.setTitle("Cereal");
        mockMealPlan.setId("ID");
        mockMealPlan2.setId("ID2");
        assertFalse(mockMealPlan.equals(mockMealPlan2));
        assertFalse(mockMealPlan2.equals(mockMealPlan));

        mockMealPlan2.setId("ID");
        mockMealPlan.setCategory("Breakfast");
        mockMealPlan2.setCategory("Break fast");
        assertFalse(mockMealPlan.equals(mockMealPlan2));
        assertFalse(mockMealPlan2.equals(mockMealPlan));

        mockMealPlan2.setCategory("Breakfast");
        mockMealPlan.setServings(5);
        mockMealPlan2.setServings(6);
        assertFalse(mockMealPlan.equals(mockMealPlan2));
        assertFalse(mockMealPlan2.equals(mockMealPlan));

        mockMealPlan2.setServings(5);
        mockMealPlan.setEatDate(new Date(11, 12, 2022));
        mockMealPlan2.setEatDate(new Date(11, 12, 2023));
        assertFalse(mockMealPlan.equals(mockMealPlan2));
        assertFalse(mockMealPlan2.equals(mockMealPlan));

        mockMealPlan2.setEatDate(new Date(11, 12, 2022));

        // adding ingredients
        mockMealPlan.addIngredient(mockIngredient);
        assertFalse(mockMealPlan.equals(mockMealPlan2));
        assertFalse(mockMealPlan2.equals(mockMealPlan));

        mockMealPlan.addIngredient(mockIngredient2);
        assertFalse(mockMealPlan.equals(mockMealPlan2));
        assertFalse(mockMealPlan2.equals(mockMealPlan));

        mockMealPlan2.addIngredient(mockIngredient2);
        mockMealPlan2.addIngredient(mockIngredient);
        assertTrue(mockMealPlan.equals(mockMealPlan2));
        assertTrue(mockMealPlan2.equals(mockMealPlan));

        // adding recipes
        mockMealPlan.addRecipe(mockRecipe);
        assertFalse(mockMealPlan.equals(mockMealPlan2));
        assertFalse(mockMealPlan2.equals(mockMealPlan));

        mockMealPlan.addRecipe(mockRecipe2);
        assertFalse(mockMealPlan.equals(mockMealPlan2));
        assertFalse(mockMealPlan2.equals(mockMealPlan));

        mockMealPlan2.addRecipe(mockRecipe2);
        mockMealPlan2.addRecipe(mockRecipe);
        assertTrue(mockMealPlan.equals(mockMealPlan2));
        assertTrue(mockMealPlan2.equals(mockMealPlan));
    }
}
