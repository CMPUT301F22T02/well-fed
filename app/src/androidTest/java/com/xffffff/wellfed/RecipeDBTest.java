package com.xffffff.wellfed;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static java.util.concurrent.TimeUnit.SECONDS;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.xffffff.wellfed.ingredient.Ingredient;
import com.xffffff.wellfed.ingredient.IngredientDB;
import com.xffffff.wellfed.recipe.Recipe;
import com.xffffff.wellfed.recipe.RecipeDB;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Tests for the RecipeDB.
 *
 * Intended device: Pixel 6 Pro API 22
 */
@RunWith(AndroidJUnit4.class) public class RecipeDBTest {
    /**
     * The timeout for DB operations.
     */
    private static final long TIMEOUT = 5;
    /**
     * The recipeDB to test.
     */
    RecipeDB recipeDB;
    /**
     * The ingredientDB to test. (needed for recipeDB)
     */
    IngredientDB ingredientDB;

    /**
     * Sets up the DB connection before testing starts.
     */
    @Before public void before() {
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
    private Recipe mockRecipe(Ingredient mockIngredient1,
                              Ingredient mockIngredient2) {
        Recipe testRecipe = new Recipe("Omelet");
        testRecipe.setComments(
                "This delicious omelette uses duck eggs and chicken eggs");
        testRecipe.setServings(1L);
        testRecipe.setPrepTimeMinutes(5L);
        testRecipe.addIngredient(mockIngredient1);
        testRecipe.addIngredient(mockIngredient2);
        testRecipe.setCategory("Breakfast");
        return testRecipe;
    }

    /**
     * Cleans up after the recipe in the test.
     *
     * @param testRecipe      the Recipe added to the DB in the test, to
     *                        clean up
     * @param testIngredient  the first Ingredient added to the DB in the
     *                        test, to clean up
     * @param testIngredient2 the second Ingredient added to the DB in the
     *                        test, to clean up
     * @throws InterruptedException when latches are interrupted or the test
     * times out
     */
    private void cleanUpRecipe(Recipe testRecipe, Ingredient testIngredient,
                               Ingredient testIngredient2)
            throws InterruptedException {
        CountDownLatch deleteLatch = new CountDownLatch(1);
        AtomicReference<Recipe> deletedRecipeRef =
                new AtomicReference<Recipe>();
        recipeDB.delRecipe(testRecipe, (deletedRecipe, success) -> {
            deletedRecipeRef.set(deletedRecipe);
            deleteLatch.countDown();
        });

        if (!deleteLatch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        assertNotNull(deletedRecipeRef.get());

        CountDownLatch deleteIngredients = new CountDownLatch(2);
        AtomicReference<Ingredient> deletedIngredientRef1 =
                new AtomicReference<Ingredient>();
        AtomicReference<Ingredient> deletedIngredientRef2 =
                new AtomicReference<Ingredient>();
        ingredientDB.deleteIngredient(testIngredient,
                (deletedIngredient, success) -> {
                    deletedIngredientRef1.set(deletedIngredient);
                    deleteIngredients.countDown();
                });

        ingredientDB.deleteIngredient(testIngredient2,
                (deletedIngredient, success) -> {
                    deletedIngredientRef2.set(deletedIngredient);
                    deleteIngredients.countDown();
                });

        if (!deleteIngredients.await(2 * TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        assertNotNull(deletedIngredientRef1.get());
        assertNotNull(deletedIngredientRef2.get());
    }

    /**
     * This method tests the AddRecipe functionality and the
     * GetRecipeFunctionality.
     * Works by adding an recipe to the database and then getting that recipe
     * checking
     * if it is the same (throwing an interrupt if it is not) and then
     * deleting the recipe
     * and the ingredients in the recipe
     *
     * @throws InterruptedException when latches are interrupted or the test
     * times out
     * @see RecipeDBTest#testGetAddedRecipe() for checking whether an added
     * recipe has correct fields
     */
    @Test public void testAddDeleteRecipe() throws InterruptedException {
        Ingredient testIngredient = mockIngredient("Egg");
        Ingredient testIngredient2 = mockIngredient("Duck Egg");

        Recipe testRecipe = mockRecipe(testIngredient, testIngredient2);

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Recipe> addedRecipeRef = new AtomicReference<Recipe>();
        recipeDB.addRecipe(testRecipe, (addedRecipe, success) -> {
            addedRecipeRef.set(addedRecipe);
            latch.countDown();
        });

        if (!latch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }
        assertTrue(testRecipe.isEqual(addedRecipeRef.get()));

        CountDownLatch deleteLatch = new CountDownLatch(1);
        AtomicReference<Recipe> deletedRecipeRef =
                new AtomicReference<Recipe>();
        recipeDB.delRecipe(testRecipe, (deletedRecipe, success) -> {
            deletedRecipeRef.set(deletedRecipe);
            deleteLatch.countDown();

        });

        if (!deleteLatch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        // deleted recipe returns a new recipe with empty fields
        assertTrue(deletedRecipeRef.get().equals(testRecipe));

        CountDownLatch deleteIngredients = new CountDownLatch(2);
        AtomicReference<Ingredient> deletedIngredientRef1 =
                new AtomicReference<Ingredient>();
        AtomicReference<Ingredient> deletedIngredientRef2 =
                new AtomicReference<Ingredient>();
        ingredientDB.deleteIngredient(testIngredient,
                (deletedIngredient, success) -> {
                    deletedIngredientRef1.set(deletedIngredient);
                    deleteIngredients.countDown();
                });

        ingredientDB.deleteIngredient(testIngredient2,
                (deletedIngredient, success) -> {
                    deletedIngredientRef2.set(deletedIngredient);
                    deleteIngredients.countDown();
                });

        if (!deleteIngredients.await(2 * TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        assertNotNull(deletedIngredientRef1.get());
        assertNotNull(deletedIngredientRef2.get());
    }

    /**
     * Tests updating a recipe.
     *
     * @throws InterruptedException when the DB operation times out
     * @see RecipeDBTest#testGetUpdatedRecipe() for testing whether an
     * updated recipe has correct fields
     */
    @Test public void testUpdateOnRecipe() throws InterruptedException {
        Ingredient testIngredient = mockIngredient("Egg");
        Ingredient testIngredient2 = mockIngredient("Duck Egg");

        Recipe testRecipe = mockRecipe(testIngredient, testIngredient2);

        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<Recipe> addedRecipeRef = new AtomicReference<Recipe>();
        recipeDB.addRecipe(testRecipe, (addedRecipe, success) -> {
            addedRecipeRef.set(addedRecipe);
            latch.countDown();
        });

        if (!latch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }
        assertNotNull(addedRecipeRef.get());

        // changing each field of the recipe
        Ingredient newTestIngredient = mockIngredient("Mayonnaise");
        newTestIngredient.setCategory("Condiment");

        testRecipe.setTitle("Egg Salad");
        testRecipe.setComments("This egg salad is great for picnics.");
        testRecipe.setServings(3L);
        testRecipe.setPrepTimeMinutes(20L);
        testRecipe.removeIngredient(testIngredient);
        testRecipe.addIngredient(newTestIngredient);
        testRecipe.setCategory("Lunch");

        CountDownLatch updateLatch = new CountDownLatch(1);
        AtomicReference<Recipe> updatedRecipeRef =
                new AtomicReference<Recipe>();
        recipeDB.updateRecipe(testRecipe, (updatedRecipe, success) -> {
            updatedRecipeRef.set(updatedRecipe);
            updateLatch.countDown();
        });

        if (!updateLatch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }
        assertTrue(testRecipe.isEqual(updatedRecipeRef.get()));

        cleanUpRecipe(testRecipe, testIngredient, testIngredient2);
    }

    /**
     * Tests adding a new recipe, and then getting it.
     * Checks whether all of the fields are correct after getting it.
     */
    @Test public void testGetAddedRecipe() throws InterruptedException {
        // add the recipe
        Ingredient testIngredient = mockIngredient("Egg");
        Ingredient testIngredient2 = mockIngredient("Duck Egg");

        Recipe testRecipe = mockRecipe(testIngredient, testIngredient2);

        CountDownLatch addLatch = new CountDownLatch(1);
        AtomicReference<Recipe> addedRecipeRef = new AtomicReference<Recipe>();
        recipeDB.addRecipe(testRecipe, (addedRecipe, success) -> {
            addedRecipeRef.set(addedRecipe);
            addLatch.countDown();
        });

        if (!addLatch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }
        assertTrue(testRecipe.isEqual(addedRecipeRef.get()));

        // now, get the recipe
        CountDownLatch getLatch = new CountDownLatch(1);
        AtomicReference<Recipe> resultRecipeRef = new AtomicReference<Recipe>();
        recipeDB.getRecipe(addedRecipeRef.get().getId(),
                (resultRecipe, success) -> {
                    resultRecipeRef.set(resultRecipe);
                    getLatch.countDown();
                });

        if (!getLatch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        assertTrue(testRecipe.isEqual(resultRecipeRef.get()));

        cleanUpRecipe(testRecipe, testIngredient, testIngredient2);
    }

    /**
     * Tests updating a recipe, and then getting it.
     * Checks whether all of the fields are correct after getting it.
     */
    @Test public void testGetUpdatedRecipe() throws InterruptedException {
        // add the recipe
        Ingredient testIngredient = mockIngredient("Egg");
        Ingredient testIngredient2 = mockIngredient("Duck Egg");

        Recipe testRecipe = mockRecipe(testIngredient, testIngredient2);

        CountDownLatch addLatch = new CountDownLatch(1);
        AtomicReference<Recipe> addedRecipeRef = new AtomicReference<Recipe>();
        recipeDB.addRecipe(testRecipe, (addedRecipe, success) -> {
            addedRecipeRef.set(addedRecipe);
            addLatch.countDown();
        });

        if (!addLatch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }
        assertTrue(testRecipe.isEqual(addedRecipeRef.get()));

        // changing each field of the recipe
        Ingredient newTestIngredient = mockIngredient("Mayonnaise");
        newTestIngredient.setCategory("Condiment");

        testRecipe.setTitle("Egg Salad");
        testRecipe.setComments("This egg salad is great for picnics.");
        testRecipe.setServings(3L);
        testRecipe.setPrepTimeMinutes(20L);
        testRecipe.removeIngredient(testIngredient2);
        testRecipe.addIngredient(newTestIngredient);
        testRecipe.setCategory("Lunch");

        CountDownLatch updateLatch = new CountDownLatch(1);
        AtomicReference<Recipe> updatedRecipeRef =
                new AtomicReference<Recipe>();
        recipeDB.updateRecipe(testRecipe, (updatedRecipe, success) -> {
            updatedRecipeRef.set(updatedRecipe);
            updateLatch.countDown();
        });

        if (!updateLatch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }
        assertTrue(testRecipe.isEqual(updatedRecipeRef.get()));

        // now, get the recipe
        CountDownLatch getLatch = new CountDownLatch(1);
        AtomicReference<Recipe> resultRecipeRef = new AtomicReference<Recipe>();
        recipeDB.getRecipe(addedRecipeRef.get().getId(),
                (resultRecipe, success) -> {
                    resultRecipeRef.set(resultRecipe);
                    getLatch.countDown();
                });

        if (!getLatch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        assertTrue(testRecipe.isEqual(resultRecipeRef.get()));

        cleanUpRecipe(testRecipe, testIngredient, testIngredient2);
    }
}
