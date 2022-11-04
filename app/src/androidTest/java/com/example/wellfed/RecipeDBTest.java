package com.example.wellfed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.recipe.Recipe;
import com.example.wellfed.recipe.RecipeDB;
import com.example.wellfed.recipe.RecipeIngredientDB;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;

@RunWith(AndroidJUnit4.class)
public class RecipeDBTest {
    RecipeDB recipeDB;
    RecipeIngredientDB recipeIngredientDB;

    @Before
    public void before() {
        recipeDB = new RecipeDB();
        recipeIngredientDB = new RecipeIngredientDB();
    }

    /**
     * This method tests the AddRecipe functionality and the GetRecipeFunctionality.
     * Works by adding an recipe to the database and then getting that recipe checking
     * if it is the same (throwing an interrupt if it is not) and then deleting the recipe
     * and the ingredients in the recipe
     *
     * @throws InterruptedException
     */
    @Test
    public void testAddRecipe() throws InterruptedException {
        Ingredient testIngredient = new Ingredient();
        testIngredient.setDescription("Egg");
        testIngredient.setAmount(1.0F);
        testIngredient.setCategory("Protein");
        testIngredient.setUnit("count");

        Recipe testRecipe = new Recipe("Omelet");
        testRecipe.setComments("This delicious breakfast is packed full of protein.");
        testRecipe.setServings(1);
        testRecipe.setPrepTimeMinutes(5);
        testRecipe.addIngredient(testIngredient);
        testRecipe.setCategory("Breakfast");

        String recipeId = recipeDB.addRecipe(testRecipe);

        String testRecipeId = testRecipe.getId();

        assert testRecipeId.equals(recipeId);

        Recipe testRecipe2 = recipeDB.getRecipe(testRecipeId);

        assertEquals(testRecipe2.getTitle(), testRecipe.getTitle());
        assertEquals(testRecipe2.getCategory(), testRecipe.getCategory());
        assertEquals(testRecipe2.getId(), testRecipe.getId());
        assertEquals(testRecipe2.getServings(), testRecipe.getServings());
        assertEquals(testRecipe2.getPrepTimeMinutes(), testRecipe.getPrepTimeMinutes());
        assertEquals(testRecipe2.getComments(), testRecipe.getComments());
        assertEquals(testRecipe2.getPhotograph(), testRecipe.getPhotograph());

        recipeDB.delRecipe(testRecipe.getId());
        recipeIngredientDB.delIngredient(testIngredient.getId());

    }

    /**
     * This method tests the delRecipe functionality and getRecipe. If we try to get an already deleted recipe
     * which should return null
     * @throws InterruptedException A RecipeDB could not complete its transaction or the recipe was not deleted
     */
    @Test
    public void testDelRecipe() throws InterruptedException {
        Ingredient testIngredient = new Ingredient();
        testIngredient.setDescription("Egg");
        testIngredient.setAmount(1.0F);
        testIngredient.setCategory("Protein");
        testIngredient.setUnit("count");

        Recipe testRecipe = new Recipe("Omelet");
        testRecipe.setComments("This delicious breakfast is packed full of protein.");
        testRecipe.setServings(1);
        testRecipe.setPrepTimeMinutes(5);
        testRecipe.addIngredient(testIngredient);
        testRecipe.setCategory("Breakfast");

        String recipeId = recipeDB.addRecipe(testRecipe);

        recipeDB.delRecipe(testRecipe.getId());
        recipeIngredientDB.delIngredient(testIngredient.getId());

        assertNull(recipeDB.getRecipe(testRecipe.getId()));
    }

    /**
     * Deletes a nonexistent document from firebase, should not throw any error
     * @throws InterruptedException If the transaction in delRecipe could not be completed
     */
    @Test
    public void testDeleteOnNonExistentRecipe() throws InterruptedException{
        recipeDB.delRecipe("-1");
    }

    @Test
    public void testUpdateOnRecipe() throws InterruptedException{
        Ingredient testIngredient = new Ingredient();
        testIngredient.setDescription("Egg");
        testIngredient.setAmount(1.0F);
        testIngredient.setCategory("Protein");
        testIngredient.setUnit("count");

        Recipe testRecipe = new Recipe("Omelet");
        testRecipe.setComments("This delicious breakfast is packed full of protein.");
        testRecipe.setServings(1);
        testRecipe.setPrepTimeMinutes(5);
        testRecipe.addIngredient(testIngredient);
        testRecipe.setCategory("Breakfast");

        String recipeId = recipeDB.addRecipe(testRecipe);

        testRecipe.setTitle("Lunch Omelette");
        testRecipe.setComments("This is a bigger version of the breakfast meal to be served for two.");
        testRecipe.setServings(2);
        testRecipe.setPrepTimeMinutes(10);
        testRecipe.setCategory("Lunch");

        recipeDB.editRecipe(testRecipe);
        Recipe fromDbTestRecipe = recipeDB.getRecipe(testRecipe.getId());
        assertEquals(testRecipe.getTitle(), fromDbTestRecipe.getTitle());
        assertEquals(testRecipe.getCategory(), fromDbTestRecipe.getCategory());
        assertEquals(testRecipe.getComments(), fromDbTestRecipe.getComments());
        assertEquals(testRecipe.getId(), fromDbTestRecipe.getId());
        assertEquals(testRecipe.getPrepTimeMinutes(), fromDbTestRecipe.getPrepTimeMinutes());

        recipeDB.delRecipe(testRecipe.getId());
        recipeIngredientDB.delIngredient(testIngredient.getId());
    }

    /**
     * Test editRecipe functionality by editing a nonexistent document which won't add to the collection
     * @throws InterruptedException If editRecipe or getRecipe transactions cannot complete successfully
     */
    @Test
    public void testUpdateOnNonExistentRecipe() throws InterruptedException{
        Recipe testRecipe = new Recipe("Fake Broccoli");
        testRecipe.setId("-1");
        recipeDB.editRecipe(testRecipe);

        assertNull(recipeDB.getRecipe(testRecipe.getId()));
    }
}
