package com.example.wellfed;

import com.example.wellfed.recipe.RecipeIngredient;
import com.example.wellfed.recipe.RecipeIngredientDB;

import org.junit.Before;
import org.junit.Test;

import java.util.Objects;

public class RecipeIngredientDBTest {
    RecipeIngredientDB recipeIngredientDB;

    @Before
    public void before(){
        recipeIngredientDB = new RecipeIngredientDB();
    }

    /**
     * Tests the add and get functionality by adding a RecipeIngredient and getting the document
     * @throws InterruptedException If a transaction in a RecipeIngredientDB method does not succeed
     */
    @Test
    public void testAddRecipeIngredients() throws InterruptedException {

        RecipeIngredient testIngredient = new RecipeIngredient();
        testIngredient.setDescription("Description");
        testIngredient.setAmount(1.0F);
        testIngredient.setCategory("Test");
        testIngredient.setUnit("TestUnits");
        recipeIngredientDB.addRecipeIngredient(testIngredient);

        RecipeIngredient testIngredientFromDb = null;

        testIngredientFromDb = recipeIngredientDB.getRecipeIngredient(testIngredient.getId());


        assert Objects.equals(testIngredient.getId(), testIngredientFromDb.getId());
        assert Objects.equals(testIngredient.getAmount(), testIngredientFromDb.getAmount());
        assert Objects.equals(testIngredient.getCategory(), testIngredientFromDb.getCategory());
        assert Objects.equals(testIngredient.getDescription(), testIngredientFromDb.getDescription());
        assert Objects.equals(testIngredient.getUnit(), testIngredientFromDb.getUnit());


        recipeIngredientDB.delIngredient(testIngredient.getId());
    }

    /**
     * Tests the delete functionality on a Recipe, check that the document was deleted
     * @throws InterruptedException If a transaction in a RecipeIngredientDB method does not succeed
     */
    @Test
    public void testDelRecipeIngredient() throws InterruptedException{

    }

    /**
     * Tests the update functionality on a recipe ingredient document should check that the fields are
     * all changed
     * @throws InterruptedException If a transaction in a RecipeIngredientDB method does not succeed
     */
    @Test
    public void testUpdateRecipeIngredient() throws InterruptedException{
        RecipeIngredient testIngredient = new RecipeIngredient();
        testIngredient.setDescription("Description");
        testIngredient.setAmount(1.0F);
        testIngredient.setCategory("Test");
        testIngredient.setUnit("TestUnits");
        recipeIngredientDB.addRecipeIngredient(testIngredient);

        RecipeIngredient testIngredientFromDb = recipeIngredientDB.getRecipeIngredient(testIngredient.getId());


        assert Objects.equals(testIngredient.getId(), testIngredientFromDb.getId());
        assert Objects.equals(testIngredient.getAmount(), testIngredientFromDb.getAmount());
        assert Objects.equals(testIngredient.getCategory(), testIngredientFromDb.getCategory());
        assert Objects.equals(testIngredient.getDescription(), testIngredientFromDb.getDescription());
        assert Objects.equals(testIngredient.getUnit(), testIngredientFromDb.getUnit());

        testIngredient.setDescription("Description2");
        testIngredient.setAmount(5.0F);
        testIngredient.setCategory("Test2");
        testIngredient.setUnit("TestUnits2");
        recipeIngredientDB.updateRecipeIngredient(testIngredient);

        testIngredientFromDb = recipeIngredientDB.getRecipeIngredient(testIngredient.getId());

        assert Objects.equals(testIngredient.getDescription(), testIngredientFromDb.getDescription());
        assert Objects.equals(testIngredient.getAmount(), testIngredientFromDb.getAmount());
        assert Objects.equals(testIngredient.getCategory(), testIngredientFromDb.getCategory());
        assert Objects.equals(testIngredient.getDescription(), testIngredientFromDb.getDescription());
        assert Objects.equals(testIngredient.getUnit(), testIngredientFromDb.getUnit());

        recipeIngredientDB.delIngredient(testIngredient.getId());
    }

    /**
     * Tests the get functionality on a nonexistent recipe ingredient document. Should return null
     * @throws InterruptedException If a transaction in a RecipeIngredientDB method does not succeed
     */
    @Test
    public void testGetOnNonExistentRecipeIngredient() throws InterruptedException{
        assert recipeIngredientDB.getRecipeIngredient("-1") == null;
    }

    /**
     * Tests the delete functionality on a nonexistent recipe ingredient document
     * should not throw an interrupt unless the transaction cannot successfully complete
     * Both delIngredient and delRecipeIngredient.
     * @throws InterruptedException If a transaction in a RecipeIngredientDB method does not succeed
     */
    @Test
    public void testDeleteOnNonExistentRecipeIngredient() throws InterruptedException{
        recipeIngredientDB.delRecipeIngredient("-1");
        recipeIngredientDB.delIngredient("-1");
    }

    /**
     * Tests the update functionality when a non existent Recipe Ingredeidnet is updated
     * we check that the Recipe Ingredient isn't added
     * @throws InterruptedException If a transaction in a RecipeIngredientDB method does not succeed
     */
    @Test
    public void testUpdateOnNonExistentRecipeIngredient() throws InterruptedException{
        RecipeIngredient recipeIngredient = new RecipeIngredient();
        recipeIngredient.setId("-1");
        recipeIngredientDB.updateRecipeIngredient(recipeIngredient);

        assert recipeIngredientDB.getRecipeIngredient(recipeIngredient.getId()) == null;
    }
}
