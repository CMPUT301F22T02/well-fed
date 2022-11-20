package com.example.wellfed;

import static java.util.concurrent.TimeUnit.SECONDS;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.ingredient.IngredientDB;
import com.example.wellfed.recipe.Recipe;
import com.example.wellfed.recipe.RecipeDB;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import junit.framework.TestCase;

@RunWith(AndroidJUnit4.class)
public class RecipeDBTest {
    RecipeDB recipeDB;
    IngredientDB ingredientDB;
    private static final long TIMEOUT = 5;

    @Before
    public void before() {
        MockDBConnection connection = new MockDBConnection();
        recipeDB = new RecipeDB(connection);
        ingredientDB = new IngredientDB(connection);
    }

    private Ingredient mockIngredient(String description) {
        Ingredient testIngredient = new Ingredient();
        testIngredient.setDescription(description);
        testIngredient.setAmount(1.0);
        testIngredient.setCategory("Protein");
        testIngredient.setUnit("count");
        return testIngredient;
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
    public void testAddDeleteRecipe() throws InterruptedException {
        Ingredient testIngredient = mockIngredient("Egg");
        Ingredient testIngredient2 = mockIngredient("Duck Egg");

        Recipe testRecipe = new Recipe("Omelet");
        testRecipe.setComments("This delicious omelette uses duck eggs and chicken eggs");
        testRecipe.setServings(1);
        testRecipe.setPrepTimeMinutes(5);
        testRecipe.addIngredient(testIngredient);
        testRecipe.addIngredient(testIngredient2);
        testRecipe.setCategory("Breakfast");

        CountDownLatch latch = new CountDownLatch(1);
        recipeDB.addRecipe(testRecipe, (addedRecipe, success) -> {
            assertNotNull(addedRecipe);
            latch.countDown();
        });

        if (!latch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        CountDownLatch deleteLatch = new CountDownLatch(1);
        recipeDB.delRecipe(testRecipe.getId(), (deletedRecipe, success) -> {
            assertNotNull(deletedRecipe);
            deleteLatch.countDown();
        });

        if (!deleteLatch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        CountDownLatch deleteIngredients = new CountDownLatch(2);
        ingredientDB.deleteIngredient(testIngredient, (deletedIngredient, success) -> {
            assertNotNull(deletedIngredient);
            deleteIngredients.countDown();
        });

        ingredientDB.deleteIngredient(testIngredient2, (deletedIngredient, success) -> {
            assertNotNull(deletedIngredient);
            deleteIngredients.countDown();
        });

        if (!deleteIngredients.await(2*TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }
    }


    /**
     * Deletes a nonexistent document from firebase, should not throw any error
     * @throws InterruptedException If the transaction in delRecipe could not be completed
     */
    @Test
    public void testDeleteOnNonExistentRecipe() throws InterruptedException{
        recipeDB.delRecipe("-1", (deletedRecipe, success) -> {
            // assert the ID of the deleted recipe is -1 and everything is null
            assertEquals(deletedRecipe.getId(), "-1");
            assertNull(deletedRecipe.getTitle());
            assertNull(deletedRecipe.getCategory());
            assertNull(deletedRecipe.getComments());
            assertNull(deletedRecipe.getPhotograph());
            assertNull(deletedRecipe.getPrepTimeMinutes());
            assertNull(deletedRecipe.getServings());
        });
    }

    @Test
    public void testUpdateOnRecipe() throws InterruptedException {
        Ingredient testIngredient = mockIngredient("Egg");
        Ingredient testIngredient2 = mockIngredient("Duck Egg");

        Recipe testRecipe = new Recipe("Omelet");
        testRecipe.setComments("This delicious omelette uses duck eggs and chicken eggs");
        testRecipe.setServings(1);
        testRecipe.setPrepTimeMinutes(5);
        testRecipe.addIngredient(testIngredient);
        testRecipe.addIngredient(testIngredient2);
        testRecipe.setCategory("Breakfast");

        
    }
//
//    /**
//     * Test editRecipe functionality by editing a nonexistent document which won't add to the collection
//     * @throws InterruptedException If editRecipe or getRecipe transactions cannot complete successfully
//     */
//    @Test
//    public void testUpdateOnNonExistentRecipe() throws InterruptedException{
//        Recipe testRecipe = new Recipe("Test");
//        testRecipe.setId("-1");
//        recipeDB.editRecipe(testRecipe);
//
//        assert recipeDB.getRecipe(testRecipe.getId()) == null;
//    }
//
//    /**
//     * Test getRecipes functionality by requesting a list of all recipes.
//     * @throws InterruptedException If getRecipes transactions cannot
//     * complete successfully
//     */
//    @Test
//    public void testGetRecipes() throws InterruptedException {
//        ArrayList<Recipe> recipes = recipeDB.getRecipes();
//        int count = recipes.size();
//        Recipe testRecipe = new Recipe("Cake");
//        testRecipe.setPrepTimeMinutes(90);
//        testRecipe.setServings(12);
//
//        recipeDB.addRecipe(testRecipe);
//        recipes = recipeDB.getRecipes();
//        assert recipes.size() == count + 1;
//        recipeDB.delRecipe(testRecipe.getId());
//        recipes = recipeDB.getRecipes();
//        assert recipes.size() == count;
//    }
}
