package com.example.wellfed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import android.media.Image;

import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.recipe.Recipe;
import com.example.wellfed.recipe.RecipeIngredient;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Unit tests for the Ingredient model class.
 * Note: Getters and setters for basic data are often tested together.
 */
public class RecipeTest {
    /**
     * Creates a mock Recipe to be used in the tests, with no title.
     * @return the mock Recipe created
     */
    public Recipe mockRecipe() {
        return new Recipe("test");
    }

    /**
     * Creates a mock RecipeIngredient to be used in the tests.
     * @return the mock RecipeIngredient created
     */
    public RecipeIngredient mockRecipeIngredient() {return new RecipeIngredient();}

    /**
     * Tests whether a new Ingredient has the correctly initialized fields.
     */
    @Test
    public void testNullFields() {
        Recipe mock = mockRecipe();
        assertNull(mock.getCategory());
        assertNull(mock.getComments());
        assertNull(mock.getPhotograph());
        assertNull(mock.getPrepTimeMinutes());
        assertNull(mock.getServings());
    }

    /**
     * Tests adding of a recipe ingredient to the recipe.
     */
    @Test
    public void testAddAndGetIngredient() {
        Recipe recipe = mockRecipe();
        RecipeIngredient ingredient1 = mockRecipeIngredient();
        RecipeIngredient ingredient2 = mockRecipeIngredient();
        ingredient1.setDescription("hello");
        ingredient2.setDescription("hello2");

        assertThrows(IndexOutOfBoundsException.class, () -> {
            recipe.getIngredient(0);
        });
        recipe.addIngredient(ingredient1);
        assertEquals(ingredient1, recipe.getIngredient(0));

        recipe.addIngredient(ingredient2);
        assertEquals(ingredient2, recipe.getIngredient(1));
    }

    /**
     * Tests deleting of an recipe ingredient from the recipe.
     */
    @Test
    public void testRemoveIngredient() {
        Recipe recipe = mockRecipe();
        RecipeIngredient ingredient1 = mockRecipeIngredient();
        RecipeIngredient ingredient2 = mockRecipeIngredient();
        ingredient1.setDescription("hello");
        ingredient2.setDescription("hello2");

        recipe.addIngredient(ingredient1);
        recipe.addIngredient(ingredient2);

        recipe.removeIngredient(ingredient1);
        assertEquals(ingredient2, recipe.getIngredient(0));
        assertThrows(IndexOutOfBoundsException.class, () -> {
            recipe.getIngredient(1);
        });

        recipe.removeIngredient(ingredient2);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            recipe.getIngredient(0);
        });
    }

    /**
     * Tests the behaviour of removing an ingredient that is not in the recipe.
     */
    @Test
    public void testRemoveIngredientNotInRecipe() {
        Recipe recipe = mockRecipe();
        RecipeIngredient ingredient = mockRecipeIngredient();
        RecipeIngredient ingredient2 = mockRecipeIngredient();
        // no error should be thrown, as deleting a non existing ingredient is fine
        recipe.removeIngredient(ingredient);

        // a removed ingredient not in recipe should not affect other ingredients
        recipe.addIngredient(ingredient);
        recipe.removeIngredient(ingredient2);
        assertEquals(ingredient, recipe.getIngredient(0));
    }

    /**
     * Tests getting the List of ingredients from a recipe.
     */
    @Test
    public void testGetIngredients() {
        Recipe recipe = mockRecipe();
        RecipeIngredient ingredient = mockRecipeIngredient();
        RecipeIngredient ingredient2 = mockRecipeIngredient();

        recipe.addIngredient(ingredient);
        recipe.addIngredient(ingredient2);
        List<RecipeIngredient> ingredientList = recipe.getIngredients();

        assertEquals(ingredient, ingredientList.get(0));
        assertEquals(ingredient2, ingredientList.get(1));
    }

    /**
     * Tests getting and setting the category of a Recipe.
     */
    @Test
    public void testCategory() {
        Recipe recipe = mockRecipe();

        // test with string
        String string = "test";
        recipe.setCategory(string);
        assertEquals(string, recipe.getCategory());

        // test with literal
        recipe.setCategory("test");
        assertEquals("test", recipe.getCategory());
    }

    /**
     * Tests getting/setting title of the Recipe.
     */
    @Test
    public void testTitle() {
        Recipe recipe = mockRecipe();

        // test with string
        String string = "test";
        recipe.setTitle(string);
        assertEquals(string, recipe.getTitle());

        // test with literal
        recipe.setTitle("test");
        assertEquals("test", recipe.getTitle());
    }

    /**
     * Tests getting/setting comments of a Recipe.
     */
    @Test
    public void testComments() {
        Recipe recipe = mockRecipe();

        // test with string
        String string = "test";
        recipe.setComments(string);
        assertEquals(string, recipe.getTitle());

        // test with literal
        recipe.setComments("test");
        assertEquals("test", recipe.getComments());
    }

    /**
     * Tests getting/setting servings of a Recipe.
     */
    @Test
    public void testServings() {
        Recipe recipe = mockRecipe();

        // test with integer
        Integer servings = 6;
        recipe.setServings(servings);
        assertEquals(servings, recipe.getServings());
    }

    /**
     * Tests getting/setting prep time of a Recipe.
     */
    @Test
    public void testPrepTime() {
        Recipe recipe = mockRecipe();

        // test with integer
        Integer minutes = 6;
        recipe.setPrepTimeMinutes(minutes);
        assertEquals(minutes, recipe.getPrepTimeMinutes());
    }

    // todo: not sure how to do this properly
    @Test
    public void testImage() {

    }

    /**
     * Tests getting/setting the ID of a Recipe.
     */
    @Test
    public void testID() {
        Recipe recipe = mockRecipe();

        // test with string
        String string = "test";
        recipe.setId(string);
        assertEquals(string, recipe.getId());

        // test with literal
        recipe.setId("test");
        assertEquals("test", recipe.getId());
    }
}
