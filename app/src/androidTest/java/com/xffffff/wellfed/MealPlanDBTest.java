package com.xffffff.wellfed;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.xffffff.wellfed.ingredient.Ingredient;
import com.xffffff.wellfed.ingredient.IngredientDB;
import com.xffffff.wellfed.mealplan.MealPlan;
import com.xffffff.wellfed.mealplan.MealPlanDB;
import com.xffffff.wellfed.recipe.Recipe;
import com.xffffff.wellfed.recipe.RecipeDB;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Tests for the MealPlanDB. Please note: These tests require functional StoredIngredientDB
 * and RecipeDB. This is because the MealPlanDB requires that all StoredIngredient and
 * RecipeIngredients exist in the database before attempting to create a MealPlan.
 * <p>
 * Unfortunately, due to the nature of the database, it is hard to test only one function at once.
 * So, you will see some tests call delete methods, even when they are not testing deletes.
 */
public class MealPlanDBTest {
	/**
	 * Tag for logging.
	 */
	private static final String TAG = "MealPlanDBTest";
	/**
	 * The database to test.
	 */
	private MealPlanDB mealPlanDB;
	/**
	 * The recipe database to test with.
	 */
	private RecipeDB recipeDB;
	/**
	 * The ingredient database to test with.
	 */
	private IngredientDB ingredientDB;
	/**
	 * The Firestore database to test with.
	 */
	private FirebaseFirestore db;
	/**
	 * Timeout for the CountDownLatch.
	 */
	private static final long TIMEOUT = 30;

	/**
	 * Creates a new mock meal plan to test with.
	 */
	private MealPlan mockMealPlan() {
		MealPlan mealPlan = new MealPlan("Thanksgiving Dinner");
		mealPlan.setCategory("Supper");
		mealPlan.setEatDate(new Date());
		mealPlan.setServings(10);
		return mealPlan;
	}

	/**
	 * Creates a new mock recipe to test with.
	 */
	private Recipe mockRecipe() {
		Recipe recipe = new Recipe("Stuffing");
		recipe.setCategory("Side Dish");
		recipe.setServings(3);
		recipe.setPrepTimeMinutes(120);
		recipe.setComments("This hearty side is the original comfort food.");
		Ingredient ingredient = new Ingredient("White bread");
		ingredient.setCategory("Carbs");
		ingredient.setAmount(2.0);
		ingredient.setUnit("cups");
		recipe.addIngredient(ingredient);
		return recipe;
	}

	/**
	 * Creates a new mock ingredient to test with.
	 */
	private Ingredient mockIngredient() {
		Ingredient ingredient = new Ingredient("Cranberry Sauce");
		ingredient.setCategory("Canned");
		ingredient.setDescription("Great on a side dish");
		ingredient.setAmount(2.0);
		ingredient.setUnit("cans");
		return ingredient;
	}

	/**
	 * Creates a new mock ingredient with test fields.
	 */
	private Ingredient mockIngredient(String description, String category,
									  Double amount, String unit) {
		Ingredient ingredient = new Ingredient(description);
		ingredient.setCategory(category);
		ingredient.setAmount(amount);
		ingredient.setUnit(unit);
		return ingredient;
	}

	/**
	 * Sets up the database to test with.
	 */
	@Before
	public void setUpDB() {
		db = FirebaseFirestore.getInstance();
		MockDBConnection connection = new MockDBConnection();
		mealPlanDB = new MealPlanDB(connection);
		recipeDB = new RecipeDB(connection);
		ingredientDB = new IngredientDB(connection);
	}

	/**
	 * Testing adding a MealPlan
	 */
	@Test
	public void testAddMealPlan() throws InterruptedException {
		MealPlan mealPlan = mockMealPlan();

		// Add the meal plan to DB
		CountDownLatch addLatch = new CountDownLatch(1);
		AtomicReference<MealPlan> mealPlanReference = new AtomicReference<>();
		mealPlanDB.addMealPlan(mealPlan, (addedMealPlan, success) -> {
			assertTrue(success);
			mealPlanReference.set(addedMealPlan);
			addLatch.countDown();
		});

		if (!addLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(mealPlan.isEqual(mealPlanReference.get()));

		// get the MealPlan from the DB and check it
		CountDownLatch getLatch = new CountDownLatch(1);
		AtomicReference<MealPlan> obtainedMealPlanReference = new AtomicReference<>();
		mealPlanDB.addMealPlan(mealPlan, (retrievedMealPlan, success) -> {
			assertTrue(success);
			obtainedMealPlanReference.set(retrievedMealPlan);
			getLatch.countDown();
		});

		if (!getLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(mealPlan.isEqual(obtainedMealPlanReference.get()));
	}

	/**
	 * Testing adding a MealPlan with a an existing Recipe in the DB
	 */
	@Test
	public void testAddMealPlanWithRecipe() throws InterruptedException {
		// Create a mock recipe
		Recipe recipe = mockRecipe();
		// CountDownLatch to wait for the recipe to be added
		CountDownLatch recipeLatch = new CountDownLatch(1);
		AtomicReference<Recipe> recipeReference = new AtomicReference<>();
		recipeDB.addRecipe(recipe, (addedRecipe, success) -> {
			assertTrue(success);
			recipeReference.set(addedRecipe);
			recipeLatch.countDown();
		});

		if (!recipeLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(recipe.isEqual(recipeReference.get()));

		MealPlan mealPlan = mockMealPlan();
		mealPlan.addRecipe(recipeReference.get());

		CountDownLatch addLatch = new CountDownLatch(1);
		AtomicReference<MealPlan> mealPlanAddReference = new AtomicReference<>();
		mealPlanDB.addMealPlan(mealPlan, (addedMealPlan, success) -> {
			assertTrue(success);
			mealPlanAddReference.set(addedMealPlan);
			addLatch.countDown();
		});

		if (!addLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(mealPlan.isEqual(mealPlanAddReference.get()));

		CountDownLatch getLatch = new CountDownLatch(1);
		AtomicReference<MealPlan> mealPlanGetReference = new AtomicReference<>();
		mealPlanDB.addMealPlan(mealPlan, (retrievedMealPlan, success) -> {
			assertTrue(success);
			mealPlanGetReference.set(retrievedMealPlan);
			getLatch.countDown();
		});

		if (!getLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(mealPlan.isEqual(mealPlanGetReference.get()));
	}

	/**
	 * Test adding recipe to meal plan after meal plan is added to database
	 * Adds the Recipe to the DB first
	 */
	@Test
	public void testAddRecipeToMealPlan() throws InterruptedException {
		// Create a mock recipe
		Recipe recipe = mockRecipe();
		// Create a mock meal plan
		MealPlan mealPlan = mockMealPlan();

		// add mealPlan to db
		CountDownLatch addMealPlanLatch = new CountDownLatch(1);
		AtomicReference<MealPlan> addedMealPlanRef = new AtomicReference<>();
		mealPlanDB.addMealPlan(mealPlan, (addedMealPlan, success) -> {
			assertTrue(success);
			addedMealPlanRef.set(addedMealPlan);
			addMealPlanLatch.countDown();
		});

		if (!addMealPlanLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(mealPlan.isEqual(addedMealPlanRef.get()));

		// add recipe to db
		CountDownLatch addRecipeLatch = new CountDownLatch(1);
		AtomicReference<Recipe> addedRecipeRef = new AtomicReference<>();
		recipeDB.addRecipe(recipe, (addedRecipe, success) -> {
			assertTrue(success);
			addedRecipeRef.set(addedRecipe);
			addRecipeLatch.countDown();
		});

		if (!addRecipeLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(recipe.isEqual(addedRecipeRef.get()));

		// updating the meal plan to have the recipe
		mealPlan.addRecipe(recipe);

		CountDownLatch updateMealPlanLatch = new CountDownLatch(1);
		AtomicReference<MealPlan> updatedMealPlanRef = new AtomicReference<>();
		mealPlanDB.updateMealPlan(mealPlan, (updatedMealPlan, success) -> {
			assertTrue(success);
			updatedMealPlanRef.set(updatedMealPlan);
			updateMealPlanLatch.countDown();
		});

		if (!updateMealPlanLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(mealPlan.isEqual(updatedMealPlanRef.get()));
	}

	/**
	 * Test adding a MealPlan with an ingredient (already added to the DB_
	 */
	@Test
	public void testAddMealPlanWithIngredient() throws InterruptedException {
		// Create a mock ingredient
		Ingredient ingredient = mockIngredient();

		// CountDownLatch to wait for the ingredient to be added
		CountDownLatch ingredientLatch = new CountDownLatch(1);
		AtomicReference<Ingredient> ingredientRef = new AtomicReference<>();
		ingredientDB.addIngredient(ingredient, (addedIngredient, success) -> {
			assertTrue(success);
			ingredientRef.set(addedIngredient);
			ingredientLatch.countDown();
		});

		if (!ingredientLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(ingredient.isEqual(ingredientRef.get()));

		// add MealPlan to db
		MealPlan mealPlan = mockMealPlan();
		mealPlan.addIngredient(ingredientRef.get());

		CountDownLatch addMealPlanLatch = new CountDownLatch(1);
		AtomicReference<MealPlan> addMealPlanRef = new AtomicReference<>();
		mealPlanDB.addMealPlan(mealPlan, (addedMealPlan, success) -> {
			assertTrue(success);
			addMealPlanRef.set(addedMealPlan);
			addMealPlanLatch.countDown();
		});

		if (!addMealPlanLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(mealPlan.isEqual(addMealPlanRef.get()));

		// Check that the meal plan was added by getting it from DB
		CountDownLatch getMealPlanLatch = new CountDownLatch(1);
		AtomicReference<MealPlan> getMealPlanRef = new AtomicReference<>();
		mealPlanDB.getMealPlan(mealPlan.getId(), (retrievedMealPlan, success) -> {
			assertTrue(success);
			getMealPlanRef.set(retrievedMealPlan);
			getMealPlanLatch.countDown();
		});

		if (!getMealPlanLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(mealPlan.isEqual(getMealPlanRef.get()));
	}

	/**
	 * Test adding a MealPlan and then adding an Ingredient
	 */
	@Test
	public void testAddIngredientToMealPlan() throws InterruptedException {
		// Create a mock ingredient
		Ingredient ingredient = mockIngredient();
		// Create a mock meal plan
		MealPlan mealPlan = mockMealPlan();
		// CountDownLatch to wait for the meal plan to be added
		CountDownLatch addMealPlanLatch = new CountDownLatch(1);
		AtomicReference<MealPlan> addMealPlanRef = new AtomicReference<>();
		mealPlanDB.addMealPlan(mealPlan, (addedMealPlan, success) -> {
			assertTrue(success);
			addMealPlanRef.set(addedMealPlan);
			addMealPlanLatch.countDown();
		});

		if (!addMealPlanLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(mealPlan.isEqual(addMealPlanRef.get()));

		// Add the ingredient to the DB
		CountDownLatch ingredientLatch = new CountDownLatch(1);
		AtomicReference<Ingredient> ingredientRef = new AtomicReference<>();
		ingredientDB.addIngredient(ingredient, (addedIngredient, success) -> {
			assertTrue(success);
			ingredientRef.set(addedIngredient);
			ingredientLatch.countDown();
		});

		if (!ingredientLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(ingredient.isEqual(ingredientRef.get()));

		// Update MealPlan with the ingredient
		mealPlan.addIngredient(ingredient);
		CountDownLatch updateMealPlanLatch = new CountDownLatch(1);
		AtomicReference<MealPlan> updatedMealPlanRef = new AtomicReference<>();
		mealPlanDB.updateMealPlan(mealPlan, (updatedMealPlan, success) -> {
			assertTrue(success);
			updatedMealPlanRef.set(updatedMealPlan);
			updateMealPlanLatch.countDown();
		});

		if (!updateMealPlanLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(mealPlan.isEqual(updatedMealPlanRef.get()));

		// Check that the meal plan was added by getting it from DB
		CountDownLatch getMealPlanLatch = new CountDownLatch(1);
		AtomicReference<MealPlan> getMealPlanRef = new AtomicReference<>();
		mealPlanDB.getMealPlan(mealPlan.getId(), (addedMealPlan, success) -> {
			assertTrue(success);
			getMealPlanRef.set(addedMealPlan);
			getMealPlanLatch.countDown();
		});

		if (!getMealPlanLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(mealPlan.isEqual(getMealPlanRef.get()));
	}

	/**
	 * Test adding multiple MealPlans to the database and getting one
	 */
	@Test
	public void testAddMultipleMealPlans() throws InterruptedException {
		MealPlan mealPlan = mockMealPlan();
		// CountDownLatch to wait for the meal plan to be added
		CountDownLatch addMealPlanLatch = new CountDownLatch(1);
		AtomicReference<MealPlan> addMealPlanRef = new AtomicReference<>();
		mealPlanDB.addMealPlan(mealPlan, (addedMealPlan, success) -> {
			assertTrue(success);
			addMealPlanRef.set(addedMealPlan);
			addMealPlanLatch.countDown();
		});

		if (!addMealPlanLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(mealPlan.isEqual(addMealPlanRef.get()));

		MealPlan mealPlan2 = mockMealPlan();
		// CountDownLatch to wait for the meal plan to be added
		CountDownLatch addMealPlanLatch2 = new CountDownLatch(1);
		AtomicReference<MealPlan> addMealPlanRef2 = new AtomicReference<>();
		mealPlanDB.addMealPlan(mealPlan2, (addedMealPlan, success) -> {
			assertTrue(success);
			addMealPlanRef2.set(addedMealPlan);
			addMealPlanLatch2.countDown();
		});

		if (!addMealPlanLatch2.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(mealPlan2.isEqual(addMealPlanRef2.get()));

		// getting MealPlan2 from DB
		CountDownLatch getMealPlanLatch = new CountDownLatch(1);
		AtomicReference<MealPlan> getMealPlanRef = new AtomicReference<>();
		mealPlanDB.getMealPlan(mealPlan2.getId(), (addedMealPlan, success) -> {
			assertTrue(success);
			getMealPlanRef.set(addedMealPlan);
			getMealPlanLatch.countDown();
		});

		if (!getMealPlanLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(mealPlan2.isEqual(getMealPlanRef.get()));
	}

	/**
	 * Test adding a MealPlan with a 2 ingredients and 3 recipes to the database.
	 * Will not add Recipes and Ingredients to the DB beforehand.
	 * This also acts to test the db's ability to handle adding new recipes & ingredients
	 * from a MealPlan.
	 */
	@Test
	public void testAddMealPlanWithIngredientsAndRecipes() throws InterruptedException {
		// CountDownLatch to wait for the meal plan to be added
		CountDownLatch mealPlanLatch = new CountDownLatch(1);
		Ingredient ingredient = mockIngredient();
		Ingredient ingredient2 = mockIngredient();
		Recipe recipe = mockRecipe();
		Recipe recipe2 = mockRecipe();
		Recipe recipe3 = mockRecipe();
		MealPlan mealPlan = mockMealPlan();

		mealPlan.addIngredient(ingredient);
		mealPlan.addIngredient(ingredient2);
		mealPlan.addRecipe(recipe);
		mealPlan.addRecipe(recipe2);
		mealPlan.addRecipe(recipe3);

		// Add the meal plan to the database
		CountDownLatch addMealPlanLatch = new CountDownLatch(1);
		AtomicReference<MealPlan> addMealPlanRef = new AtomicReference<>();
		mealPlanDB.addMealPlan(mealPlan, (addedMealPlan, success) -> {
			assertTrue(success);
			addMealPlanRef.set(addedMealPlan);
			addMealPlanLatch.countDown();
		});

		if (!addMealPlanLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(mealPlan.isEqual(addMealPlanRef.get()));

		// getting MealPlan from DB
		CountDownLatch getMealPlanLatch = new CountDownLatch(1);
		AtomicReference<MealPlan> getMealPlanRef = new AtomicReference<>();
		mealPlanDB.getMealPlan(mealPlan.getId(), (addedMealPlan, success) -> {
			assertTrue(success);
			getMealPlanRef.set(addedMealPlan);
			getMealPlanLatch.countDown();
		});

		if (!getMealPlanLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(mealPlan.isEqual(getMealPlanRef.get()));
	}

	/**
	 * Test adding 2 meal plans to the database and deleting them the second one
	 */
	@Test
	public void testAdd2MealPlansDeleteSecond() throws InterruptedException {
		// Create a meal plan
		MealPlan mealPlan = mockMealPlan();
		MealPlan mealPlan2 = mockMealPlan();

		// Add the meal plan to the database
		CountDownLatch addMealPlanLatch = new CountDownLatch(1);
		AtomicReference<MealPlan> addMealPlanRef = new AtomicReference<>();
		mealPlanDB.addMealPlan(mealPlan, (addedMealPlan, success) -> {
			assertTrue(success);
			addMealPlanRef.set(addedMealPlan);
			addMealPlanLatch.countDown();
		});

		if (!addMealPlanLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(mealPlan.isEqual(addMealPlanRef.get()));

		// Add the second meal plan to the database
		CountDownLatch addMealPlanLatch2 = new CountDownLatch(1);
		AtomicReference<MealPlan> addMealPlanRef2 = new AtomicReference<>();
		mealPlanDB.addMealPlan(mealPlan2, (addedMealPlan, success) -> {
			assertTrue(success);
			addMealPlanRef2.set(addedMealPlan);
			addMealPlanLatch2.countDown();
		});

		if (!addMealPlanLatch2.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(mealPlan2.isEqual(addMealPlanRef2.get()));

		// Get all MealPlans
		CountDownLatch getMealPlansLatch = new CountDownLatch(1);
		AtomicReference<ArrayList<MealPlan>> mealPlansRef = new AtomicReference<>();
		mealPlanDB.getMealPlans((retrievedMealPlans, success) -> {
			assertTrue(success);
			mealPlansRef.set(retrievedMealPlans);
			getMealPlansLatch.countDown();
		});

		if (!getMealPlansLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		// Add the second meal plan to the database
		CountDownLatch deleteMealPlan2Latch = new CountDownLatch(1);
		mealPlanDB.delMealPlan(mealPlan2, (addedMealPlan, success) -> {
			assertTrue(success);
			deleteMealPlan2Latch.countDown();
		});

		if (!deleteMealPlan2Latch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		// Get all MealPlans
		CountDownLatch retrievedMealPlansLatch = new CountDownLatch(1);
		AtomicReference<ArrayList<MealPlan>> retrievedMealPlansRef = new AtomicReference<>();
		mealPlanDB.getMealPlans((retrievedMealPlans, success) -> {
			assertTrue(success);
			retrievedMealPlansRef.set(retrievedMealPlans);
			retrievedMealPlansLatch.countDown();
		});

		if (!retrievedMealPlansLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}


		assertEquals(mealPlansRef.get().size() - 1, retrievedMealPlansRef.get().size());

	}

	/**
	 * Test updating a meal plan title, category, eat date, servings,
	 * recipes, and ingredients in the database and retrieving it
	 */
	@Test
	public void testUpdateMealPlan() throws InterruptedException {
		MealPlan mealPlan = mockMealPlan();
		// Add the meal plan to the database
		CountDownLatch addMealPlanLatch = new CountDownLatch(1);
		AtomicReference<MealPlan> addMealPlanRef = new AtomicReference<>();
		mealPlanDB.addMealPlan(mealPlan, (addedMealPlan, success) -> {
			assertTrue(success);
			addMealPlanRef.set(addedMealPlan);
			addMealPlanLatch.countDown();
		});

		if (!addMealPlanLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(mealPlan.isEqual(addMealPlanRef.get()));

		mealPlan.setTitle("Christmas Dinner");
		mealPlan.setCategory("Holiday");
		mealPlan.setEatDate(new Date());
		mealPlan.setServings(5);
		mealPlan.addRecipe(mockRecipe());
		mealPlan.addRecipe(mockRecipe());
		mealPlan.addIngredient(mockIngredient());
		mealPlan.addIngredient(mockIngredient());

		// Update the meal plan in the database
		CountDownLatch updateMealPlanLatch = new CountDownLatch(1);
		AtomicReference<MealPlan> updatedMealPlanRef = new AtomicReference<>();
		mealPlanDB.updateMealPlan(mealPlan, (updatedMealPlan, success) -> {
			assertTrue(success);
			updatedMealPlanRef.set(updatedMealPlan);
			updateMealPlanLatch.countDown();
		});

		if (!updateMealPlanLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(mealPlan.isEqual(updatedMealPlanRef.get()));

		// Get the meal plan from the database
		// getting MealPlan from DB
		CountDownLatch getMealPlanLatch = new CountDownLatch(1);
		AtomicReference<MealPlan> getMealPlanRef = new AtomicReference<>();
		mealPlanDB.getMealPlan(mealPlan.getId(), (addedMealPlan, success) -> {
			assertTrue(success);
			getMealPlanRef.set(addedMealPlan);
			getMealPlanLatch.countDown();
		});

		if (!getMealPlanLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(mealPlan.isEqual(getMealPlanRef.get()));
	}

	/**
	 * Test adding a meal plan with 5 ingredients to the database
	 */
	@Test
	public void testAddMealPlanWith5Ingredients() throws InterruptedException {
		// CountDownLatch to wait for the asynchronous calls to finish
		CountDownLatch mealPlanLatch = new CountDownLatch(1);
		// Create a meal plan
		MealPlan mealPlan = mockMealPlan();
		// Add 5 ingredients
		mealPlan.addIngredient(mockIngredient());
		mealPlan.addIngredient(mockIngredient());
		mealPlan.addIngredient(mockIngredient());
		mealPlan.addIngredient(mockIngredient());
		mealPlan.addIngredient(mockIngredient());

		// Add the meal plan to the database
		CountDownLatch addMealPlanLatch = new CountDownLatch(1);
		AtomicReference<MealPlan> addMealPlanRef = new AtomicReference<>();
		mealPlanDB.addMealPlan(mealPlan, (addedMealPlan, success) -> {
			assertTrue(success);
			addMealPlanRef.set(addedMealPlan);
			addMealPlanLatch.countDown();
		});

		if (!addMealPlanLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(mealPlan.isEqual(addMealPlanRef.get()));

		// Get the meal plan from the database
		CountDownLatch getMealPlanLatch = new CountDownLatch(1);
		AtomicReference<MealPlan> getMealPlanRef = new AtomicReference<>();
		mealPlanDB.getMealPlan(mealPlan.getId(), (addedMealPlan, success) -> {
			assertTrue(success);
			getMealPlanRef.set(addedMealPlan);
			getMealPlanLatch.countDown();
		});

		if (!getMealPlanLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(mealPlan.isEqual(getMealPlanRef.get()));
	}

	/**
	 * Test adding a meal plan with 5 different ingredients to the database
	 */
	@Test
	public void testAddMealPlanWith5DifferentIngredients() throws InterruptedException {
		// CountDownLatch to wait for the asynchronous calls to finish
		CountDownLatch mealPlanLatch = new CountDownLatch(1);
		// Create a meal plan
		MealPlan mealPlan = mockMealPlan();
		mealPlan.setTitle("Fruit basket");
		mealPlan.setCategory("Fruits");
		// Add 5 different ingredients

		mealPlan.addIngredient(mockIngredient("Banana", "Fruit",
			5.0, "bananas"));
		mealPlan.addIngredient(mockIngredient("Apple", "Fruit",
				4.0, "apples"));
		mealPlan.addIngredient(mockIngredient("Orange", "Fruit",
				3.0, "oranges"));
		mealPlan.addIngredient(mockIngredient("Pear", "Fruit",
				2.0, "pears"));
		mealPlan.addIngredient(mockIngredient("Grape", "Fruit",
				1.0, "grapes"));

		// Add the meal plan to the database
		CountDownLatch addMealPlanLatch = new CountDownLatch(1);
		AtomicReference<MealPlan> addMealPlanRef = new AtomicReference<>();
		mealPlanDB.addMealPlan(mealPlan, (addedMealPlan, success) -> {
			assertTrue(success);
			addMealPlanRef.set(addedMealPlan);
			addMealPlanLatch.countDown();
		});

		if (!addMealPlanLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(mealPlan.isEqual(addMealPlanRef.get()));

		// Get the meal plan from the database
		CountDownLatch getMealPlanLatch = new CountDownLatch(1);
		AtomicReference<MealPlan> getMealPlanRef = new AtomicReference<>();
		mealPlanDB.getMealPlan(mealPlan.getId(), (addedMealPlan, success) -> {
			assertTrue(success);
			getMealPlanRef.set(addedMealPlan);
			getMealPlanLatch.countDown();
		});

		if (!getMealPlanLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(mealPlan.isEqual(getMealPlanRef.get()));

	}

	/**
	 * Test adding a meal plan with 5 recipes to the database
	 */
	@Test
	public void testAddMealPlanWith5Recipes() throws InterruptedException {
		// CountDownLatch to wait for the asynchronous calls to finish
		CountDownLatch mealPlanLatch = new CountDownLatch(1);
		// Create a meal plan
		MealPlan mealPlan = mockMealPlan();
		// Add 5 recipes
		mealPlan.addRecipe(mockRecipe());
		mealPlan.addRecipe(mockRecipe());
		mealPlan.addRecipe(mockRecipe());
		mealPlan.addRecipe(mockRecipe());
		mealPlan.addRecipe(mockRecipe());

		// Add the meal plan to the database
		CountDownLatch addMealPlanLatch = new CountDownLatch(1);
		AtomicReference<MealPlan> addMealPlanRef = new AtomicReference<>();
		mealPlanDB.addMealPlan(mealPlan, (addedMealPlan, success) -> {
			assertTrue(success);
			addMealPlanRef.set(addedMealPlan);
			addMealPlanLatch.countDown();
		});

		if (!addMealPlanLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(mealPlan.isEqual(addMealPlanRef.get()));

		// Get the meal plan from the database
		CountDownLatch getMealPlanLatch = new CountDownLatch(1);
		AtomicReference<MealPlan> getMealPlanRef = new AtomicReference<>();
		mealPlanDB.getMealPlan(mealPlan.getId(), (addedMealPlan, success) -> {
			assertTrue(success);
			getMealPlanRef.set(addedMealPlan);
			getMealPlanLatch.countDown();
		});

		if (!getMealPlanLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(mealPlan.isEqual(getMealPlanRef.get()));

	}

	/**
	 * Test adding a meal plan with 5 different recipes to the database
	 */
	@Test
	public void testAddMealPlanWith5DifferentRecipes() throws InterruptedException {
		// CountDownLatch to wait for the asynchronous calls to finish
		CountDownLatch mealPlanLatch = new CountDownLatch(1);
		// Create a meal plan
		MealPlan mealPlan = mockMealPlan();
		// Add 5 different recipes
		Recipe recipe1 = new Recipe("Chicken");
		Ingredient ingredient1 = new Ingredient("Chicken");
		recipe1.addIngredient(ingredient1);
		recipe1.setCategory("Main");
		recipe1.setServings(1);
		recipe1.setPrepTimeMinutes(30);
		recipe1.setComments("Poultry to eat for a burst of protein.");
		mealPlan.addRecipe(recipe1);
		Recipe recipe2 = new Recipe("Beef");
		Ingredient ingredient2 = new Ingredient("Beef");
		recipe2.addIngredient(ingredient2);
		recipe2.setCategory("Main");
		recipe2.setServings(5);
		recipe2.setPrepTimeMinutes(30);
		recipe2.setComments("Beefy goodness?");
		mealPlan.addRecipe(recipe2);
		Recipe recipe3 = new Recipe("Pork");
		Ingredient ingredient3 = new Ingredient("Pork");
		recipe3.addIngredient(ingredient3);
		recipe3.setCategory("Main");
		recipe3.setServings(3);
		recipe3.setPrepTimeMinutes(40);
		recipe3.setComments("Tasty pork");
		mealPlan.addRecipe(recipe3);
		Recipe recipe4 = new Recipe("Fish");
		Ingredient ingredient4 = new Ingredient("Fish");
		recipe4.addIngredient(ingredient4);
		recipe4.setCategory("Main");
		recipe4.setServings(2);
		recipe4.setPrepTimeMinutes(30);
		recipe4.setComments("Hopefully it's not still swimming!");
		mealPlan.addRecipe(recipe4);
		Recipe recipe5 = new Recipe("Lamb");
		Ingredient ingredient5 = new Ingredient("Lamb");
		recipe5.addIngredient(ingredient5);
		recipe5.setCategory("Main");
		recipe5.setServings(1);
		recipe5.setPrepTimeMinutes(25);
		recipe5.setComments("Yummy lamb");
		mealPlan.addRecipe(recipe5);


		// Add the meal plan to the database
		CountDownLatch addMealPlanLatch = new CountDownLatch(1);
		AtomicReference<MealPlan> addMealPlanRef = new AtomicReference<>();
		mealPlanDB.addMealPlan(mealPlan, (addedMealPlan, success) -> {
			assertTrue(success);
			addMealPlanRef.set(addedMealPlan);
			addMealPlanLatch.countDown();
		});

		if (!addMealPlanLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(mealPlan.isEqual(addMealPlanRef.get()));

		// Get the meal plan from the database
		CountDownLatch getMealPlanLatch = new CountDownLatch(1);
		AtomicReference<MealPlan> getMealPlanRef = new AtomicReference<>();
		mealPlanDB.getMealPlan(mealPlan.getId(), (addedMealPlan, success) -> {
			assertTrue(success);
			getMealPlanRef.set(addedMealPlan);
			getMealPlanLatch.countDown();
		});

		if (!getMealPlanLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}

		assertTrue(mealPlan.isEqual(getMealPlanRef.get()));

//		// Add the meal plan to the database
//		mealPlanDB.addMealPlan(mealPlan, (addedMealPlan, success) -> {
//			if (success) {
//				// Retrieve the meal plan
//				mealPlanDB.getMealPlan(addedMealPlan.getId(), (retrievedMealPlan, success2) -> {
//					if (success2) {
//						// Compare using the getters
//						assertEquals(addedMealPlan.getId(), retrievedMealPlan.getId());
//						assertEquals(addedMealPlan.getTitle(), retrievedMealPlan.getTitle());
//						assertEquals(addedMealPlan.getCategory(), retrievedMealPlan.getCategory());
//						assertEquals(addedMealPlan.getEatDate(), retrievedMealPlan.getEatDate());
//						assertEquals(addedMealPlan.getServings(), retrievedMealPlan.getServings());
//						// Check if the recipes are the same size
//						assertEquals(addedMealPlan.getRecipes().size(),
//							retrievedMealPlan.getRecipes().size());
//						// Check if the recipes exist in the retrieved meal plan using the getters id
//						for (Recipe currentRecipe : addedMealPlan.getRecipes()) {
//							boolean found = false;
//							for (Recipe retrievedRecipe : retrievedMealPlan.getRecipes()) {
//								if (currentRecipe.getId().equals(retrievedRecipe.getId())) {
//									found = true;
//									break;
//								}
//							}
//							assertTrue(found);
//						}
//						// Check if the ingredients are the same size
//						assertEquals(addedMealPlan.getIngredients().size(),
//							retrievedMealPlan.getIngredients().size());
//						// Check if the ingredients exist in the retrieved meal plan using the getters id
//						for (Ingredient currentIngredient : addedMealPlan.getIngredients()) {
//							boolean found = false;
//							for (Ingredient retrievedIngredient : retrievedMealPlan.getIngredients()) {
//								if (currentIngredient.getId().equals(retrievedIngredient.getId())) {
//									found = true;
//									break;
//								}
//							}
//							assertTrue(found);
//						}
//						// Count down the latch
//						mealPlanLatch.countDown();
//					} else {
//						Log.e(TAG, "Failed to retrieve meal plan");
//					}
//				});
//			} else {
//				Log.e(TAG, "Failed to add meal plan");
//			}
//		});
//		if (!mealPlanLatch.await(TIMEOUT, TimeUnit.SECONDS)) {
//			throw new InterruptedException();
//		}
	}
}
