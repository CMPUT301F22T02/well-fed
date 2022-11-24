//package com.xffffff.wellfed;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNull;
//
//import com.xffffff.wellfed.ingredient.Ingredient;
//import com.xffffff.wellfed.recipe.RecipeIngredientDB;
//
//import org.junit.Before;
//import org.junit.Test;
//
//public class RecipeIngredientDBTest {
//    RecipeDB recipeDB;
//
//    @Before
//    public void before(){
//        recipeIngredientDB = new RecipeIngredientDB();
//    }
//
//    @Test
//    public void addRecipeTest(){
//        recipeIngredientDB.addRecipe();
//    }
//
////    /**
////     * Tests the add and get functionality by adding a RecipeIngredient
// and getting the document
////     * @throws InterruptedException If a transaction in a
// RecipeIngredientDB method does not succeed
////     */
////    @Test
////    public void testAddRecipeIngredients() throws InterruptedException {
////        Ingredient testIngredient = new Ingredient();
////        testIngredient.setDescription("Egg");
////        testIngredient.setAmount(2.0);
////        testIngredient.setCategory("Protein");
////        testIngredient.setUnit("count");
////        recipeIngredientDB.addRecipeIngredient(testIngredient);
////
////        Ingredient testIngredientFromDb = null;
////
////        testIngredientFromDb = recipeIngredientDB.getRecipeIngredient
// (testIngredient.getId());
////
////
////        assertEquals(testIngredient.getId(), testIngredientFromDb.getId());
////        assertEquals(testIngredient.getAmount(), testIngredientFromDb
// .getAmount());
////        assertEquals(testIngredient.getCategory(), testIngredientFromDb
// .getCategory());
////        assertEquals(testIngredient.getDescription(),
// testIngredientFromDb.getDescription());
////        assertEquals(testIngredient.getUnit(), testIngredientFromDb
// .getUnit());
////
////
////        recipeIngredientDB.delIngredient(testIngredient.getId());
////    }
//
//    /**
//     * Tests the delete functionality on a Recipe, check that the document
//     was deleted
//     * @throws InterruptedException If a transaction in a
//     RecipeIngredientDB method does not succeed
//     */
////    @Test
////    public void testDelRecipeIngredient() throws InterruptedException{
////        Ingredient testIngredient = new Ingredient();
////        testIngredient.setDescription("Egg");
////        testIngredient.setAmount(2.0);
////        testIngredient.setCategory("Protein");
////        testIngredient.setUnit("count");
////        recipeIngredientDB.addRecipeIngredient(testIngredient);
////
////        recipeIngredientDB.delIngredient(testIngredient.getId());
////
////        assertNull(recipeIngredientDB.getRecipeIngredient(testIngredient
// .getId()));
////    }
////
////    /**
////     * Tests the update functionality on a recipe ingredient document
// should check that the fields are
////     * all changed
////     * @throws InterruptedException If a transaction in a
// RecipeIngredientDB method does not succeed
////     */
////    @Test
////    public void testUpdateRecipeIngredient() throws InterruptedException{
////        Ingredient testIngredient = new Ingredient();
////        testIngredient.setDescription("Egg");
////        testIngredient.setAmount(1.0);
////        testIngredient.setCategory("Protein");
////        testIngredient.setUnit("count");
////        recipeIngredientDB.addRecipeIngredient(testIngredient);
////
////        Ingredient testIngredientFromDb = recipeIngredientDB
// .getRecipeIngredient(testIngredient.getId());
////
////
////        assertEquals(testIngredient.getId(), testIngredientFromDb.getId());
////        assertEquals(testIngredient.getAmount(), testIngredientFromDb
// .getAmount());
////        assertEquals(testIngredient.getCategory(), testIngredientFromDb
// .getCategory());
////        assertEquals(testIngredient.getDescription(),
// testIngredientFromDb.getDescription());
////        assertEquals(testIngredient.getUnit(), testIngredientFromDb
// .getUnit());
////
////        testIngredient.setDescription("Scrambled eggs");
////        testIngredient.setAmount(500.0);
////        testIngredient.setCategory("Breakfast");
////        testIngredient.setUnit("grams");
////        recipeIngredientDB.updateRecipeIngredient(testIngredient);
////
////        testIngredientFromDb = recipeIngredientDB.getRecipeIngredient
// (testIngredient.getId());
////
////        assertEquals(testIngredient.getDescription(),
// testIngredientFromDb.getDescription());
////        assertEquals(testIngredient.getAmount(), testIngredientFromDb
// .getAmount());
////        assertEquals(testIngredient.getCategory(), testIngredientFromDb
// .getCategory());
////        assertEquals(testIngredient.getDescription(),
// testIngredientFromDb.getDescription());
////        assertEquals(testIngredient.getUnit(), testIngredientFromDb
// .getUnit());
////
////        recipeIngredientDB.delIngredient(testIngredient.getId());
////    }
////
////    /**
////     * Tests the get functionality on a nonexistent recipe ingredient
// document. Should return null
////     * @throws InterruptedException If a transaction in a
// RecipeIngredientDB method does not succeed
////     */
////    @Test
////    public void testGetOnNonExistentRecipeIngredient() throws
// InterruptedException{
////        assertNull(recipeIngredientDB.getRecipeIngredient("-1"));
////    }
////
////    /**
////     * Tests the delete functionality on a nonexistent recipe ingredient
// document
////     * should not throw an interrupt unless the transaction cannot
// successfully complete
////     * Both delIngredient and delRecipeIngredient.
////     * @throws InterruptedException If a transaction in a
// RecipeIngredientDB method does not succeed
////     */
////    @Test
////    public void testDeleteOnNonExistentRecipeIngredient() throws
// InterruptedException{
////        recipeIngredientDB.delRecipeIngredient("-1");
////        recipeIngredientDB.delIngredient("-1");
////    }
////
////    /**
////     * Tests the update functionality when a non existent Recipe
// Ingredeidnet is updated
////     * we check that the Recipe Ingredient isn't added
////     * @throws InterruptedException If a transaction in a
// RecipeIngredientDB method does not succeed
////     */
////    @Test
////    public void testUpdateOnNonExistentRecipeIngredient() throws
// InterruptedException{
////        Ingredient recipeIngredient = new Ingredient();
////        recipeIngredient.setId("-1");
////        recipeIngredientDB.updateRecipeIngredient(recipeIngredient);
////
////        assertNull(recipeIngredientDB.getRecipeIngredient
// (recipeIngredient.getId()));
////    }
//}
