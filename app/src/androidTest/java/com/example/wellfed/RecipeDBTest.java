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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import junit.framework.TestCase;

@RunWith(AndroidJUnit4.class)
public class RecipeDBTest {
    RecipeDB recipeDB;
    IngredientDB ingredientDB;
    private static final long TIMEOUT = 10;

    /**
     * Sets up the DB connection before testing starts.
     */
    @Before
    public void before() {
        MockDBConnection connection = new MockDBConnection();
        recipeDB = new RecipeDB(connection);
        ingredientDB = new IngredientDB(connection);
    }

    /**
     * Creates a mock ingredient for use in RecipeDB testing.
     *
     * @param description the description of the mock ingredient
     * @return the mock ingredient created
     */
    private Ingredient mockIngredient(String description) {
        Ingredient testIngredient = new Ingredient();
        testIngredient.setDescription(description);
        testIngredient.setAmount(1.0);
        testIngredient.setCategory("Protein");
        testIngredient.setUnit("count");
        return testIngredient;
    }

    /**
     * Creates a mock recipe for use in RecipeDB testing.
     *
     * @param mockIngredient1 a mock ingredient to add to the recipe
     * @param mockIngredient2 a mock ingredient to add to the recipe
     * @return the mock recipe created
     */
    private Recipe mockRecipe(Ingredient mockIngredient1, Ingredient mockIngredient2) {
        Recipe testRecipe = new Recipe("Omelet");
        testRecipe.setComments("This delicious omelette uses duck eggs and chicken eggs");
        testRecipe.setServings(1);
        testRecipe.setPrepTimeMinutes(5);
        testRecipe.addIngredient(mockIngredient1);
        testRecipe.addIngredient(mockIngredient2);
        testRecipe.setCategory("Breakfast");
        return testRecipe;
    }

    /**
     * Asserts that two recipes are equal.
     *
     * @param actual the actual recipe after performing some DB operation
     * @param expected the expected recipe to check against the actual recipe
     */
    private void assertEqualRecipe(Recipe actual, Recipe expected) {
        assertEquals(actual.getId(), expected.getId());
        assertEquals(actual.getTitle(), expected.getTitle());
        assertEquals(actual.getCategory(), expected.getCategory());
        assertEquals(actual.getComments(), expected.getComments());
        assertEquals(actual.getPhotograph(), expected.getPhotograph());
        assertEquals(actual.getPrepTimeMinutes(), expected.getPrepTimeMinutes());
        assertEquals(actual.getServings(), expected.getServings());
        assertEquals(actual.getIngredients(), expected.getIngredients());
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
     * @see RecipeDBTest#testGetAddedRecipe() for checking whether an added recipe has correct fields
     * @throws InterruptedException when latches are interrupted or the test times out
     */
    @Test
    public void testAddDeleteRecipe() throws InterruptedException {
        Ingredient testIngredient = mockIngredient("Egg");
        Ingredient testIngredient2 = mockIngredient("Duck Egg");

        Recipe testRecipe = mockRecipe(testIngredient, testIngredient2);

        CountDownLatch latch = new CountDownLatch(1);
        recipeDB.addRecipe(testRecipe, (addedRecipe, success) -> {

            latch.countDown();
        });

        if (!latch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        CountDownLatch deleteLatch = new CountDownLatch(1);
        recipeDB.delRecipe(testRecipe.getId(), (deletedRecipe, success) -> {
            deleteLatch.countDown();
            assertEqualRecipe(deletedRecipe, testRecipe);
        });

        if (!deleteLatch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        CountDownLatch deleteIngredients = new CountDownLatch(2);
        ingredientDB.deleteIngredient(testIngredient, (deletedIngredient, success) -> {
            deleteIngredients.countDown();
            assertNotNull(deletedIngredient);
        });

        ingredientDB.deleteIngredient(testIngredient2, (deletedIngredient, success) -> {
            deleteIngredients.countDown();
            assertNotNull(deletedIngredient);
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
            Recipe emptyRecipe = new Recipe(null);
            emptyRecipe.setId("-1");
            assertEqualRecipe(deletedRecipe, emptyRecipe);
        });
    }

    /**
     * Tests updating a recipe.
     *
     * @see RecipeDBTest#testGetUpdatedRecipe() for testing whether an updated recipe has correct fields
     * @throws InterruptedException when the DB operation times out
     */
    @Test
    public void testUpdateOnRecipe() throws InterruptedException {
        Ingredient testIngredient = mockIngredient("Egg");
        Ingredient testIngredient2 = mockIngredient("Duck Egg");

        Recipe testRecipe = mockRecipe(testIngredient, testIngredient2);

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

        CountDownLatch updateLatch = new CountDownLatch(1);
        recipeDB.updateRecipe(testRecipe, (updatedRecipe, success) -> {
            updateLatch.countDown();
            assertTrue(success);
            assertEqualRecipe(updatedRecipe, testRecipe);
        });

        if (!updateLatch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        cleanUpRecipe(testRecipe, testIngredient, testIngredient2);
    }

    /**
     * Test updateRecipe functionality by updating a nonexistent document which won't add to the collection
     * @throws InterruptedException If updateRecipe transactions cannot complete successfully
     */
    @Test
    @Ignore("TODO: Fix callback for nonexistent recipes")
    public void testUpdateOnNonExistentRecipe() throws InterruptedException{
        Recipe testRecipe = new Recipe(null);
        testRecipe.setId("-1");

        CountDownLatch updateLatch = new CountDownLatch(1);
        recipeDB.updateRecipe(testRecipe, (updatedRecipe, success) -> {
            updateLatch.countDown();
            assertTrue(success);
            assertEqualRecipe(updatedRecipe, testRecipe);
        });

        if (!updateLatch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }
    }

    @Test
    public void testGetAddedRecipe() {

    }

    @Test
    public void testGetUpdatedRecipe() {

    }

    @Test
    public void testGetNonExistentRecipe() {

    }
}
