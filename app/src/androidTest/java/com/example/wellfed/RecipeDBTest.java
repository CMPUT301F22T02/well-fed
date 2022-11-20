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
     * Cleans up after the recipe in the test.
     *
     * @param testRecipe        the Recipe added to the DB in the test, to clean up
     * @param testIngredient    the first Ingredient added to the DB in the test, to clean up
     * @param testIngredient2   the second Ingredient added to the DB in the test, to clean up
     * @throws InterruptedException when latches are interrupted or the test times out
     */
    private void cleanUpRecipe(Recipe testRecipe, Ingredient testIngredient, Ingredient testIngredient2) throws InterruptedException {
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
     * This method tests the AddRecipe functionality and the GetRecipeFunctionality.
     * Works by adding an recipe to the database and then getting that recipe checking
     * if it is the same (throwing an interrupt if it is not) and then deleting the recipe
     * and the ingredients in the recipe
     *
     * @throws InterruptedException when latches are interrupted or the test times out
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
            assertEquals(addedRecipe.getId(), testRecipe.getId());
            assertEquals(addedRecipe.getTitle(), testRecipe.getTitle());
            assertEquals(addedRecipe.getCategory(), testRecipe.getCategory());
            assertEquals(addedRecipe.getComments(), testRecipe.getComments());
            assertEquals(addedRecipe.getPhotograph(), testRecipe.getPhotograph());
            assertEquals(addedRecipe.getPrepTimeMinutes(), testRecipe.getPrepTimeMinutes());
            assertEquals(addedRecipe.getServings(), testRecipe.getServings());
            latch.countDown();
        });

        if (!latch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        CountDownLatch deleteLatch = new CountDownLatch(1);
        recipeDB.delRecipe(testRecipe.getId(), (deletedRecipe, success) -> {
            assertEquals(deletedRecipe.getId(), testRecipe.getId());
            assertEquals(deletedRecipe.getTitle(), testRecipe.getTitle());
            assertEquals(deletedRecipe.getCategory(), testRecipe.getCategory());
            assertEquals(deletedRecipe.getComments(), testRecipe.getComments());
            assertEquals(deletedRecipe.getPhotograph(), testRecipe.getPhotograph());
            assertEquals(deletedRecipe.getPrepTimeMinutes(), testRecipe.getPrepTimeMinutes());
            assertEquals(deletedRecipe.getServings(), testRecipe.getServings());
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

        CountDownLatch latch = new CountDownLatch(1);
        recipeDB.addRecipe(testRecipe, (addedRecipe, success) -> {
            assertNotNull(addedRecipe);
            latch.countDown();
        });

        if (!latch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        // changing each field of the recipe
        Ingredient newTestIngredient = mockIngredient("Mayonnaise");
        newTestIngredient.setCategory("Condiment");

        testRecipe.setTitle("Egg Salad");
        testRecipe.setComments("This egg salad is great for picnics.");
        testRecipe.setServings(3);
        testRecipe.setPrepTimeMinutes(20);
        testRecipe.removeIngredient(testIngredient);
        testRecipe.addIngredient(newTestIngredient);
        testRecipe.setCategory("Lunch");

        recipeDB.updateRecipe(testRecipe, (updatedRecipe, success) -> {
            assertEquals(updatedRecipe.getId(), testRecipe.getId());
            assertEquals(updatedRecipe.getTitle(), testRecipe.getTitle());
            assertEquals(updatedRecipe.getCategory(), testRecipe.getCategory());
            assertEquals(updatedRecipe.getComments(), testRecipe.getComments());
            assertEquals(updatedRecipe.getPhotograph(), testRecipe.getPhotograph());
            assertEquals(updatedRecipe.getPrepTimeMinutes(), testRecipe.getPrepTimeMinutes());
            assertEquals(updatedRecipe.getServings(), testRecipe.getServings());
        });

        cleanUpRecipe(testRecipe, testIngredient, testIngredient2);
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
