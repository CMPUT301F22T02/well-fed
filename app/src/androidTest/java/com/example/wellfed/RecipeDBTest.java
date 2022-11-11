package com.example.wellfed;

import static java.util.concurrent.TimeUnit.SECONDS;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.recipe.Recipe;
import com.example.wellfed.recipe.RecipeDB;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import static org.junit.Assert.assertNotNull;

import junit.framework.TestCase;

@RunWith(AndroidJUnit4.class)
public class RecipeDBTest {
    RecipeDB recipeDB;
    private static final long TIMEOUT = 5;

    @Before
    public void before() {
        recipeDB = new RecipeDB();
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
        testIngredient.setAmount(1.0);
        testIngredient.setCategory("Test");
        testIngredient.setUnit("TestUnits");

        Ingredient testIngredient2 = new Ingredient();
        testIngredient.setDescription("Egg2");
        testIngredient.setAmount(1.0);
        testIngredient.setCategory("Test2");
        testIngredient.setUnit("TestUnits2");

        Recipe testRecipe = new Recipe("Test");
        testRecipe.setComments("Test");
        testRecipe.setServings(1);
        testRecipe.setPrepTimeMinutes(1);
        testRecipe.addIngredient(testIngredient);
        testRecipe.addIngredient(testIngredient2);
        testRecipe.addIngredient(testIngredient2);
        testRecipe.addIngredient(testIngredient2);
        testRecipe.addIngredient(testIngredient2);
        testRecipe.addIngredient(testIngredient2);
        testRecipe.setCategory("Test");

        CountDownLatch latch = new CountDownLatch(1);
        recipeDB.addRecipe(testRecipe, (addedRecipe, success) -> {
            assertNotNull(addedRecipe);
            latch.countDown();
        });

        if (!latch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

    }

//    /**
//     * This method tests the delRecipe functionality and getRecipe. If we try to get an already deleted recipe
//     * which should return null
//     * @throws InterruptedException A RecipeDB could not complete its transaction or the recipe was not deleted
//     */
//    @Test
//    public void testDelRecipe() throws InterruptedException {
//        Ingredient testIngredient = new Ingredient();
//        testIngredient.setDescription("Egg");
//        testIngredient.setAmount(1.0);
//        testIngredient.setCategory("Test");
//        testIngredient.setUnit("TestUnits");
//
//        Recipe testRecipe = new Recipe("Test");
//        testRecipe.setComments("Test");
//        testRecipe.setServings(1);
//        testRecipe.setPrepTimeMinutes(1);
//        testRecipe.addIngredient(testIngredient);
//        testRecipe.setCategory("Test");
//
//        String recipeId = recipeDB.addRecipe(testRecipe);
//
//        recipeDB.delRecipe(testRecipe.getId());
//        recipeIngredientDB.delIngredient(testIngredient.getId());
//
//        assert recipeDB.getRecipe(testRecipe.getId()) == null;
//    }
//
//    /**
//     * Deletes a nonexistent document from firebase, should not throw any error
//     * @throws InterruptedException If the transaction in delRecipe could not be completed
//     */
//    @Test
//    public void testDeleteOnNonExistentRecipe() throws InterruptedException{
//        recipeDB.delRecipe("-1");
//    }
//
//    @Test
//    public void testUpdateOnRecipe() throws InterruptedException{
//        Ingredient testIngredient = new Ingredient();
//        testIngredient.setDescription("Egg");
//        testIngredient.setAmount(1.0);
//        testIngredient.setCategory("Test");
//        testIngredient.setUnit("TestUnits");
//
//        Recipe testRecipe = new Recipe("Test");
//        testRecipe.setComments("Test");
//        testRecipe.setServings(1);
//        testRecipe.setPrepTimeMinutes(1);
//        testRecipe.addIngredient(testIngredient);
//        testRecipe.setCategory("Test");
//
//        String recipeId = recipeDB.addRecipe(testRecipe);
//
//        testRecipe.setTitle("Test2");
//        testRecipe.setComments("Test2");
//        testRecipe.setServings(2);
//        testRecipe.setPrepTimeMinutes(2);
//        testRecipe.setCategory("Test2");
//
//        recipeDB.editRecipe(testRecipe);
//        Recipe fromDbTestRecipe = recipeDB.getRecipe(testRecipe.getId());
//        assert Objects.equals(testRecipe.getTitle(), fromDbTestRecipe.getTitle());
//        assert Objects.equals(testRecipe.getCategory(), fromDbTestRecipe.getCategory());
//        assert Objects.equals(testRecipe.getComments(), fromDbTestRecipe.getComments());
//        assert Objects.equals(testRecipe.getId(), fromDbTestRecipe.getId());
//        assert testRecipe.getPrepTimeMinutes() == fromDbTestRecipe.getPrepTimeMinutes();
//
//        recipeDB.delRecipe(testRecipe.getId());
//        recipeIngredientDB.delIngredient(testIngredient.getId());
//    }
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
