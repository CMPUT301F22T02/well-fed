package com.example.wellfed;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.wellfed.recipe.Recipe;
import com.example.wellfed.recipe.RecipeDB;
import com.example.wellfed.recipe.RecipeIngredient;
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
        RecipeIngredient testIngredient = new RecipeIngredient();
        testIngredient.setDescription("Description");
        testIngredient.setAmount(1.0F);
        testIngredient.setCategory("Test");
        testIngredient.setUnit("TestUnits");

        Recipe testRecipe = new Recipe("Test");
        testRecipe.setComments("Test");
        testRecipe.setServings(1);
        testRecipe.setPrepTimeMinutes(1);
        testRecipe.addIngredient(testIngredient);
        testRecipe.setCategory("Test");

        String recipeId = recipeDB.addRecipe(testRecipe);

        String testRecipeId = testRecipe.getId();

        assert testRecipeId.equals(recipeId);

        Recipe testRecipe2 = recipeDB.getRecipe(testRecipeId);

        assert Objects.equals(testRecipe2.getTitle(), testRecipe.getTitle());
        assert Objects.equals(testRecipe2.getCategory(), testRecipe.getCategory());
        assert Objects.equals(testRecipe2.getId(), testRecipe.getId());
        assert Objects.equals(testRecipe2.getServings(), testRecipe.getServings());
        assert Objects.equals(testRecipe2.getPrepTimeMinutes(), testRecipe.getPrepTimeMinutes());
        assert Objects.equals(testRecipe2.getComments(), testRecipe.getComments());
        assert Objects.equals(testRecipe2.getPhotograph(), testRecipe.getPhotograph());

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
        RecipeIngredient testIngredient = new RecipeIngredient();
        testIngredient.setDescription("Description");
        testIngredient.setAmount(1.0F);
        testIngredient.setCategory("Test");
        testIngredient.setUnit("TestUnits");

        Recipe testRecipe = new Recipe("Test");
        testRecipe.setComments("Test");
        testRecipe.setServings(1);
        testRecipe.setPrepTimeMinutes(1);
        testRecipe.addIngredient(testIngredient);
        testRecipe.setCategory("Test");

        String recipeId = recipeDB.addRecipe(testRecipe);

        recipeDB.delRecipe(testRecipe.getId());
        recipeIngredientDB.delIngredient(testIngredient.getId());

        assert recipeDB.getRecipe(testRecipe.getId()) == null;
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
        RecipeIngredient testIngredient = new RecipeIngredient();
        testIngredient.setDescription("Description");
        testIngredient.setAmount(1.0F);
        testIngredient.setCategory("Test");
        testIngredient.setUnit("TestUnits");

        Recipe testRecipe = new Recipe("Test");
        testRecipe.setComments("Test");
        testRecipe.setServings(1);
        testRecipe.setPrepTimeMinutes(1);
        testRecipe.addIngredient(testIngredient);
        testRecipe.setCategory("Test");

        String recipeId = recipeDB.addRecipe(testRecipe);

        testRecipe.setTitle("Test2");
        testRecipe.setComments("Test2");
        testRecipe.setServings(2);
        testRecipe.setPrepTimeMinutes(2);
        testRecipe.setCategory("Test2");

        recipeDB.editRecipe(testRecipe);
        Recipe fromDbTestRecipe = recipeDB.getRecipe(testRecipe.getId());
        assert Objects.equals(testRecipe.getTitle(), fromDbTestRecipe.getTitle());
        assert Objects.equals(testRecipe.getCategory(), fromDbTestRecipe.getCategory());
        assert Objects.equals(testRecipe.getComments(), fromDbTestRecipe.getComments());
        assert Objects.equals(testRecipe.getId(), fromDbTestRecipe.getId());
        assert testRecipe.getPrepTimeMinutes() == fromDbTestRecipe.getPrepTimeMinutes();

        recipeDB.delRecipe(testRecipe.getId());
        recipeIngredientDB.delIngredient(testIngredient.getId());
    }

    /**
     * Test editRecipe functionality by editing a nonexistent document which won't add to the collection
     * @throws InterruptedException If editRecipe or getRecipe transactions cannot complete successfully
     */
    @Test
    public void testUpdateOnNonExistentRecipe() throws InterruptedException{
        Recipe testRecipe = new Recipe("Test");
        testRecipe.setId("-1");
        recipeDB.editRecipe(testRecipe);

        assert recipeDB.getRecipe(testRecipe.getId()) == null;
    }
}
