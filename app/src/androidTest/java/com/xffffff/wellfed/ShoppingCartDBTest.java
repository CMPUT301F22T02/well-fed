package com.xffffff.wellfed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.xffffff.wellfed.common.DBConnection;
import com.xffffff.wellfed.ingredient.Ingredient;
import com.xffffff.wellfed.ingredient.StorageIngredient;
import com.xffffff.wellfed.ingredient.StorageIngredientDB;
import com.xffffff.wellfed.mealplan.MealPlan;
import com.xffffff.wellfed.mealplan.MealPlanDB;
import com.xffffff.wellfed.recipe.Recipe;
import com.xffffff.wellfed.recipe.RecipeDB;
import com.xffffff.wellfed.shoppingcart.ShoppingCart;
import com.xffffff.wellfed.shoppingcart.ShoppingCartDB;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Tests for the ShoppingCartDB class. Please note: These tests require
 * functional StorageIngredientDB, RecipeDB and MealPlanDB.
 */
public class ShoppingCartDBTest {
	/**
	 * Tag for logging.
	 */
	private static final String TAG = "ShoppingCartDBTest";
	/**
	 * The database to test
	 */
	private ShoppingCartDB shoppingCartDB;
	/**
	 * The recipe database to test with
	 */
	private RecipeDB recipeDB;
	/**
	 * The meal plan database to test with
	 */
	private MealPlanDB mealPlanDB;
	/**
	 * The storage ingredient database to test with
	 */
	private StorageIngredientDB storageIngredientDB;
	/**
	 * The firestore database to test with
	 */
	private FirebaseFirestore db;
	/**
	 * Timeout for the CountDownLatch
	 */
	private static final long TIMEOUT = 30;

	/**
	 * Setup DBs
	 */
	@Before
	public void setUpDB() {
		db = FirebaseFirestore.getInstance();
		DBConnection connection = new MockDBConnection();
		shoppingCartDB = new ShoppingCartDB(connection);
		recipeDB = new RecipeDB(connection);
		mealPlanDB = new MealPlanDB(connection);
		storageIngredientDB = new StorageIngredientDB(connection);
	}

	/**
	 * Creates a list of ingredients for testing.
	 */
	private ArrayList<Ingredient> mockIngredients() {
		ArrayList<Ingredient> ingredients = new ArrayList<>();
		ingredients.add(new Ingredient("Broccoli"));
		ingredients.add(new Ingredient("Carrot"));
		ingredients.add(new Ingredient("Potato"));
		ingredients.add(new Ingredient("Onion"));
		ingredients.add(new Ingredient("Garlic"));
		return ingredients;
	}

	/**
	 * Creates a list of storage ingredients for testing.
	 */
	private ArrayList<StorageIngredient> mockStorageIngredients() {
		ArrayList<StorageIngredient> storageIngredients = new ArrayList<>();
		storageIngredients.add(new StorageIngredient("Chicken", 1.0, "lb",
			"Pantry", new Date(), "Chicken"));
		storageIngredients.add(new StorageIngredient("Beef", 1.0, "lb",
			"Pantry", new Date(), "Beef"));
		storageIngredients.add(new StorageIngredient("Pork", 1.0, "lb",
			"Pantry", new Date(), "Pork"));
		storageIngredients.add(new StorageIngredient("Fish", 1.0, "lb",
			"Pantry", new Date(), "Fish"));
		storageIngredients.add(new StorageIngredient("Eggs", 1.0, "lb",
			"Pantry", new Date(), "Eggs"));
		return storageIngredients;
	}

	/**
	 * Creates a list of recipes for testing.
	 */
	private ArrayList<Recipe> mockRecipes() {
		ArrayList<Recipe> recipes = new ArrayList<>();
		Recipe recipe1 = new Recipe("Chicken Soup");
		recipe1.addIngredient(new Ingredient("Chicken"));
		recipe1.setCategory("Main");
		recipe1.setServings(1);
		recipe1.setPrepTimeMinutes(10);
		recipe1.setComments("Test");
		recipes.add(recipe1);
		Recipe recipe2 = new Recipe("Beef Stew");
		recipe2.addIngredient(new Ingredient("Beef"));
		recipe2.setCategory("Main");
		recipe2.setServings(1);
		recipe2.setPrepTimeMinutes(10);
		recipe2.setComments("Test");
		recipes.add(recipe2);
		Recipe recipe3 = new Recipe("Pork Chops");
		recipe3.addIngredient(new Ingredient("Pork"));
		recipe3.setCategory("Main");
		recipe3.setServings(1);
		recipe3.setPrepTimeMinutes(10);
		recipe3.setComments("Test");
		recipes.add(recipe3);
		return recipes;
	}

	/**
	 * Creates a list of meal plans for testing.
	 */
	private ArrayList<MealPlan> mockMealPlans() {
		ArrayList<MealPlan> mealPlans = new ArrayList<>();
		MealPlan mealPlan1 = new MealPlan("Chicken Soup");
		mealPlan1.addRecipe(new Recipe("Chicken Soup"));
		mealPlan1.setCategory("Main");
		mealPlan1.setServings(1);
		mealPlans.add(mealPlan1);
		MealPlan mealPlan2 = new MealPlan("Beef Stew");
		mealPlan2.addRecipe(new Recipe("Beef Stew"));
		mealPlan2.setCategory("Main");
		mealPlan2.setServings(1);
		mealPlans.add(mealPlan2);
		MealPlan mealPlan3 = new MealPlan("Pork Chops");
		mealPlan3.addRecipe(new Recipe("Pork Chops"));
		mealPlan3.setCategory("Main");
		mealPlan3.setServings(1);
		mealPlans.add(mealPlan3);
		return mealPlans;
	}

	/**
	 * Creates a shopping cart for testing.
	 */
	private ShoppingCart mockShoppingCart() {
		return new ShoppingCart();
	}

	/**
	 * Test adding ingredient to shopping cart.
	 */
	@Test
	public void addIngredientTest() throws InterruptedException {
		// Create a CountDownLatch to wait for the async calls to finish
		CountDownLatch latch = new CountDownLatch(1);
		// Get one of the ingredients
		Ingredient ingredient = mockIngredients().get(0);
		// Add the ingredient to the shopping cart
		shoppingCartDB.addIngredient(ingredient,
			(addedIngredient, success) -> {
				assertNotNull(addedIngredient);
				assertTrue(success);
				assertEquals(ingredient.getDescription(),
					addedIngredient.getDescription());
				assertFalse(addedIngredient.isPickedUp());
				assertFalse(addedIngredient.isComplete());
				latch.countDown();
		});
		// Wait for the async calls to finish
		if (!latch.await(TIMEOUT, TimeUnit.SECONDS)) {
			throw new InterruptedException();
		}
	}

	/**
	 * Test adding null ingredient to shopping cart.
	 */
	@Test
	public void addNullIngredientTest() throws InterruptedException {
		// Create a CountDownLatch to wait for the async calls to finish
		CountDownLatch latch = new CountDownLatch(1);
		// Create a shopping cart
		ShoppingCart shoppingCart = mockShoppingCart();
		// Add null ingredient to the shopping cart
		shoppingCartDB.addIngredient(null,
			(addedIngredient, success) -> {
				assertNull(addedIngredient);
				assertFalse(success);
				latch.countDown();
		});
		// Wait for the async calls to finish
		if (!latch.await(TIMEOUT, TimeUnit.SECONDS)) {
			throw new InterruptedException();
		}
	}

	/**
	 * Test deleting ingredient from shopping cart.
	 */
	@Test
	public void deleteIngredientTest() throws InterruptedException {
		// Create a CountDownLatch to wait for the async calls to finish
		CountDownLatch latch = new CountDownLatch(1);
		// Get one of the ingredients
		Ingredient ingredient = mockIngredients().get(0);
		// Add the ingredient to the shopping cart
		shoppingCartDB.addIngredient(ingredient,
			(addedIngredient, success) -> {
				assertNotNull(addedIngredient);
				assertTrue(success);
				assertEquals(ingredient.getDescription(),
					addedIngredient.getDescription());
				assertFalse(addedIngredient.isPickedUp());
				assertFalse(addedIngredient.isComplete());
				// Delete the ingredient from the shopping cart
				shoppingCartDB.deleteIngredient(addedIngredient,
					(deletedIngredient, success2) -> {
						assertNotNull(deletedIngredient);
						assertTrue(success2);
						assertEquals(addedIngredient.getDescription(),
							deletedIngredient.getDescription());
						latch.countDown();
				});
		});
		// Wait for the async calls to finish
		if (!latch.await(TIMEOUT, TimeUnit.SECONDS)) {
			throw new InterruptedException();
		}
	}

	/**
	 * Test deleting null ingredient from shopping cart.
	 */
	@Test
	public void deleteNullIngredientTest() throws InterruptedException {
		// Create a CountDownLatch to wait for the async calls to finish
		CountDownLatch latch = new CountDownLatch(1);
		// Create a shopping cart
		ShoppingCart shoppingCart = mockShoppingCart();
		// Delete null ingredient from the shopping cart
		shoppingCartDB.deleteIngredient(null,
			(deletedIngredient, success) -> {
				assertNull(deletedIngredient);
				assertFalse(success);
				latch.countDown();
		});
		// Wait for the async calls to finish
		if (!latch.await(TIMEOUT, TimeUnit.SECONDS)) {
			throw new InterruptedException();
		}
	}

	/**
	 * Test getting ingredients from shopping cart after adding 1 ingredient.
	 */
	@Test
	public void getIngredientsTest() throws InterruptedException {
		// Create a CountDownLatch to wait for the async calls to finish
		CountDownLatch latch = new CountDownLatch(1);
		// Get one of the ingredients
		Ingredient ingredient = mockIngredients().get(0);
		// Add the ingredient to the shopping cart
		shoppingCartDB.addIngredient(ingredient,
			(addedIngredient, success) -> {
				assertNotNull(addedIngredient);
				assertTrue(success);
				assertEquals(ingredient.getDescription(),
					addedIngredient.getDescription());
				assertFalse(addedIngredient.isPickedUp());
				assertFalse(addedIngredient.isComplete());
				// Get the ingredients from the shopping cart
				shoppingCartDB.getShoppingCart((shoppingCart, success2) -> {
					assertNotNull(shoppingCart);
					assertTrue(success2);
					assertEquals(1, shoppingCart.size());
					assertEquals(addedIngredient.getDescription(),
						shoppingCart.get(0).getDescription());
					latch.countDown();
				});
		});
		// Wait for the async calls to finish
		if (!latch.await(TIMEOUT, TimeUnit.SECONDS)) {
			throw new InterruptedException();
		}
	}

	/**
	 * Test getting ingredients from shopping cart after adding 2 ingredients.
	 */
	@Test
	public void getIngredientsTest2() throws InterruptedException {
		// Create a CountDownLatch to wait for the async calls to finish
		CountDownLatch latch = new CountDownLatch(1);
		// Get one of the ingredients
		Ingredient ingredient = mockIngredients().get(0);
		// Add the ingredient to the shopping cart
		shoppingCartDB.addIngredient(ingredient,
			(addedIngredient, success) -> {
				assertNotNull(addedIngredient);
				assertTrue(success);
				assertEquals(ingredient.getDescription(),
					addedIngredient.getDescription());
				assertFalse(addedIngredient.isPickedUp());
				assertFalse(addedIngredient.isComplete());
				// Get the ingredients from the shopping cart
				shoppingCartDB.getShoppingCart((shoppingCart, success2) -> {
					assertNotNull(shoppingCart);
					assertTrue(success2);
					assertEquals(1, shoppingCart.size());
					assertEquals(addedIngredient.getDescription(),
						shoppingCart.get(0).getDescription());
					// Add another ingredient to the shopping cart
					Ingredient ingredient2 = mockIngredients().get(1);
					shoppingCartDB.addIngredient(ingredient2,
						(addedIngredient2, success3) -> {
							assertNotNull(addedIngredient2);
							assertTrue(success3);
							assertEquals(ingredient2.getDescription(),
								addedIngredient2.getDescription());
							assertFalse(addedIngredient2.isPickedUp());
							assertFalse(addedIngredient2.isComplete());
							// Get the ingredients from the shopping cart
							shoppingCartDB.getShoppingCart((shoppingCart2, success4) -> {
								assertNotNull(shoppingCart2);
								assertTrue(success4);
								assertEquals(2, shoppingCart2.size());
								// Since the ingredients are added in a
								// random order, we need to check both
								// ingredients
								boolean foundIngredient1 = false;
								boolean foundIngredient2 = false;
								for (Ingredient ingredient3 : shoppingCart2) {
									if (ingredient3.getDescription().equals(addedIngredient.getDescription())) {
										foundIngredient1 = true;
									}
									if (ingredient3.getDescription().equals(addedIngredient2.getDescription())) {
										foundIngredient2 = true;
									}
								}
								assertTrue(foundIngredient1);
								assertTrue(foundIngredient2);
								latch.countDown();
							});
					});
				});
		});
		// Wait for the async calls to finish
		if (!latch.await(TIMEOUT, TimeUnit.SECONDS)) {
			throw new InterruptedException();
		}
	}
}