package com.example.wellfed;

import static java.util.concurrent.TimeUnit.SECONDS;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.util.Log;

import com.example.wellfed.common.DBConnection;
import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.ingredient.StorageIngredient;
import com.example.wellfed.ingredient.StorageIngredientDB;
import com.example.wellfed.mealplan.MealPlan;
import com.example.wellfed.mealplan.MealPlanDB;
import com.example.wellfed.recipe.Recipe;
import com.example.wellfed.recipe.RecipeDB;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
	private StorageIngredientDB storedIngredientDB;
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
		MealPlan mealPlan = new MealPlan("Mock Meal Plan");
		mealPlan.setCategory("Mock Category");
		mealPlan.setEatDate(new Date());
		mealPlan.setServings(1);
		return mealPlan;
	}

	/**
	 * Creates a new mock recipe to test with.
	 */
	private Recipe mockRecipe() {
		Recipe recipe = new Recipe("Mock Recipe");
		recipe.setCategory("Mock Category");
		recipe.setServings(1);
		recipe.setPrepTimeMinutes(1);
		recipe.setComments("Mock Comments");
		recipe.setPrepTimeMinutes(1);
		recipe.setTitle("Mock Title");
		Ingredient ingredient = new Ingredient("Mock Ingredient");
		ingredient.setCategory("Mock Category");
		ingredient.setAmount(1.0);
		ingredient.setUnit("g");
		recipe.addIngredient(ingredient);
		return recipe;
	}

	/**
	 * Creates a new mock ingredient to test with.
	 */
	private StorageIngredient mockIngredient() {
		StorageIngredient ingredient = new StorageIngredient("Mock Ingredient");
		ingredient.setCategory("Mock Category");
		ingredient.setLocation("Mock Location");
		ingredient.setDescription("Mock Description");
		ingredient.setAmount(1.0);
		ingredient.setUnit("Mock Unit");
		ingredient.setBestBefore(new Date(2017, 1, 1));
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
		storedIngredientDB = new StorageIngredientDB(connection);
	}

	/**
	 * Testing adding a MealPlan
	 */
	@Test
	public void testAddMealPlan() throws InterruptedException {
		MealPlan mealPlan = mockMealPlan();
		CountDownLatch latch = new CountDownLatch(1);
		// Add the meal plan to DB
		mealPlanDB.addMealPlan(mealPlan, (addedMealPlan, success) -> {
			if (success) {
				// Check that the meal plan was added
				mealPlanDB.getMealPlan(addedMealPlan.getId(), (retrievedMealPlan, success2) -> {
					if (success2) {
						// Compare using the getters
						assertEquals(addedMealPlan.getId(), retrievedMealPlan.getId());
						assertEquals(addedMealPlan.getTitle(),
							retrievedMealPlan.getTitle());
						assertEquals(addedMealPlan.getCategory(), retrievedMealPlan.getCategory());
						assertEquals(addedMealPlan.getEatDate(), retrievedMealPlan.getEatDate());
						assertEquals(addedMealPlan.getServings(), retrievedMealPlan.getServings());
						// Check if the recipes are the same size
						assertEquals(addedMealPlan.getRecipes().size(),
							retrievedMealPlan.getRecipes().size());
						// Check if the recipes exist in the retrieved meal plan using the getters id
						for (Recipe currentRecipe : addedMealPlan.getRecipes()) {
							boolean found = false;
							for (Recipe retrievedRecipe : retrievedMealPlan.getRecipes()) {
								if (currentRecipe.getId().equals(retrievedRecipe.getId())) {
									found = true;
									break;
								}
							}
							assertTrue(found);
						}
						// Check if the ingredients are the same size
						assertEquals(addedMealPlan.getIngredients().size(),
							retrievedMealPlan.getIngredients().size());
						// Check if the ingredients exist in the retrieved meal plan using the getters id
						for (Ingredient currentIngredient : addedMealPlan.getIngredients()) {
							boolean found = false;
							for (Ingredient retrievedIngredient : retrievedMealPlan.getIngredients()) {
								if (currentIngredient.getId().equals(retrievedIngredient.getId())) {
									found = true;
									break;
								}
							}
							assertTrue(found);
						}
						latch.countDown();
					} else {
						Log.e(TAG, "Failed to retrieve meal plan");
					}
				});
			} else {
				Log.e(TAG, "Failed to add meal plan");
			}
		});
		if (!latch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}
	}

	/**
	 * Testing adding a MealPlan with a Recipe
	 */
	@Test
	public void testAddMealPlanWithRecipe() throws InterruptedException {
		// Create a mock recipe
		Recipe recipe = mockRecipe();
		// CountDownLatch to wait for the recipe to be added
		CountDownLatch recipeLatch = new CountDownLatch(1);
		// Add the recipe to the database
		recipeDB.addRecipe(recipe, (addedRecipe, success) -> {
			if (success) {
				// Create a mock meal plan
				MealPlan mealPlan = mockMealPlan();
				// Add the recipe to the meal plan
				mealPlan.addRecipe(addedRecipe);
				// Add the meal plan to the database
				mealPlanDB.addMealPlan(mealPlan, (addedMealPlan, success2) -> {
					if (success2) {
						// Check that the meal plan was added
						mealPlanDB.getMealPlan(addedMealPlan.getId(), (retrievedMealPlan, success3) -> {
							if (success3) {
								// Compare using the getters
								assertEquals(addedMealPlan.getId(), retrievedMealPlan.getId());
								assertEquals(addedMealPlan.getTitle(),
									retrievedMealPlan.getTitle());
								assertEquals(addedMealPlan.getCategory(),
									retrievedMealPlan.getCategory());
								assertEquals(addedMealPlan.getEatDate(),
									retrievedMealPlan.getEatDate());
								assertEquals(addedMealPlan.getServings(),
									retrievedMealPlan.getServings());
								// Check if the recipes are the same size
								assertEquals(addedMealPlan.getRecipes().size(),
									retrievedMealPlan.getRecipes().size());
								// Check if the recipes exist in the retrieved meal plan using the getters id
								for (Recipe currentRecipe : addedMealPlan.getRecipes()) {
									boolean found = false;
									for (Recipe retrievedRecipe : retrievedMealPlan.getRecipes()) {
										if (currentRecipe.getId().equals(retrievedRecipe.getId())) {
											found = true;
											break;
										}
									}
									assertTrue(found);
								}
								// Check if the ingredients are the same size
								assertEquals(addedMealPlan.getIngredients().size(),
									retrievedMealPlan.getIngredients().size());
								// Check if the ingredients exist in the retrieved meal plan using the getters id
								for (Ingredient currentIngredient : addedMealPlan.getIngredients()) {
									boolean found = false;
									for (Ingredient retrievedIngredient : retrievedMealPlan.getIngredients()) {
										if (currentIngredient.getId().equals(retrievedIngredient.getId())) {
											found = true;
											break;
										}
									}
									assertTrue(found);
								}
								recipeLatch.countDown();
							} else {
								Log.e(TAG, "Failed to retrieve meal plan");
							}
						});
					} else {
						Log.e(TAG, "Failed to add meal plan");
					}
				});
			} else {
				Log.e(TAG, "Failed to add recipe");
			}
		});
		if (!recipeLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}
	}

	/**
	 * Test adding recipe to meal plan after meal plan is added to database
	 */
	@Test
	public void testAddRecipeToMealPlan() throws InterruptedException {
		// Create a mock recipe
		Recipe recipe = mockRecipe();
		// Create a mock meal plan
		MealPlan mealPlan = mockMealPlan();
		// CountDownLatch to wait for the meal plan to be added
		CountDownLatch mealPlanLatch = new CountDownLatch(1);
		// Add the meal plan to the database
		mealPlanDB.addMealPlan(mealPlan, (addedMealPlan, success) -> {
			if (success) {
				// Add the recipe to the database
				recipeDB.addRecipe(recipe, (addedRecipe, success2) -> {
					if (success2) {
						// Add the recipe to the meal plan
						mealPlanDB.updateMealPlan(addedMealPlan, (updatedMealPlan, success3) -> {
							if (success3) {
								// Check that the meal plan was added
								mealPlanDB.getMealPlan(updatedMealPlan.getId(), (retrievedMealPlan, success4) -> {
									if (success4) {
										// Compare using the getters
										assertEquals(updatedMealPlan.getId(), retrievedMealPlan.getId());
										assertEquals(updatedMealPlan.getTitle(),
											retrievedMealPlan.getTitle());
										assertEquals(updatedMealPlan.getCategory(),
											retrievedMealPlan.getCategory());
										assertEquals(updatedMealPlan.getEatDate(),
											retrievedMealPlan.getEatDate());
										assertEquals(updatedMealPlan.getServings(),
											retrievedMealPlan.getServings());
										// Check if the recipes are the same size
										assertEquals(addedMealPlan.getRecipes().size(),
											retrievedMealPlan.getRecipes().size());
										// Check if the recipes exist in the retrieved meal plan using the getters id
										for (Recipe currentRecipe : addedMealPlan.getRecipes()) {
											boolean found = false;
											for (Recipe retrievedRecipe : retrievedMealPlan.getRecipes()) {
												if (currentRecipe.getId().equals(retrievedRecipe.getId())) {
													found = true;
													break;
												}
											}
											assertTrue(found);
										}
										// Check if the ingredients are the same size
										assertEquals(addedMealPlan.getIngredients().size(),
											retrievedMealPlan.getIngredients().size());
										// Check if the ingredients exist in the retrieved meal plan using the getters id
										for (Ingredient currentIngredient : addedMealPlan.getIngredients()) {
											boolean found = false;
											for (Ingredient retrievedIngredient : retrievedMealPlan.getIngredients()) {
												if (currentIngredient.getId().equals(retrievedIngredient.getId())) {
													found = true;
													break;
												}
											}
											assertTrue(found);
										}
										mealPlanLatch.countDown();
									} else {
										Log.e(TAG, "Failed to retrieve meal plan");
									}
								});
							} else {
								Log.e(TAG, "Failed to update meal plan");
							}
						});
					} else {
						Log.e(TAG, "Failed to add recipe");
					}
				});
			} else {
				Log.e(TAG, "Failed to add meal plan");
			}
		});
		if (!mealPlanLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}
	}

	/**
	 * Test adding a MealPlan with an ingredient
	 */
	@Test
	public void testAddMealPlanWithIngredient() throws InterruptedException {
		// Create a mock ingredient
		StorageIngredient ingredient = mockIngredient();
		// CountDownLatch to wait for the ingredient to be added
		CountDownLatch ingredientLatch = new CountDownLatch(1);
		// Add the ingredient to the database
		storedIngredientDB.addStorageIngredient(ingredient, (addedIngredient, success) -> {
			if (success) {
				// Create a mock meal plan
				MealPlan mealPlan = mockMealPlan();
				// Set the ingredient
				mealPlan.addIngredient(addedIngredient);
				// Add the meal plan to the database
				mealPlanDB.addMealPlan(mealPlan, (addedMealPlan, success2) -> {
					if (success2) {
						// Check that the meal plan was added
						mealPlanDB.getMealPlan(addedMealPlan.getId(), (retrievedMealPlan, success3) -> {
							if (success3) {
								// Compare using the getters
								assertEquals(addedMealPlan.getId(), retrievedMealPlan.getId());
								assertEquals(addedMealPlan.getTitle(),
									retrievedMealPlan.getTitle());
								assertEquals(addedMealPlan.getCategory(),
									retrievedMealPlan.getCategory());
								assertEquals(addedMealPlan.getEatDate(),
									retrievedMealPlan.getEatDate());
								assertEquals(addedMealPlan.getServings(),
									retrievedMealPlan.getServings());
								// Check if the recipes are the same size
								assertEquals(addedMealPlan.getRecipes().size(),
									retrievedMealPlan.getRecipes().size());
								// Check if the recipes exist in the retrieved meal plan using the getters id
								for (Recipe currentRecipe : addedMealPlan.getRecipes()) {
									boolean found = false;
									for (Recipe retrievedRecipe : retrievedMealPlan.getRecipes()) {
										if (currentRecipe.getId().equals(retrievedRecipe.getId())) {
											found = true;
											break;
										}
									}
									assertTrue(found);
								}
								// Check if the ingredients are the same size
								assertEquals(addedMealPlan.getIngredients().size(),
									retrievedMealPlan.getIngredients().size());
								// Check if the ingredients exist in the retrieved meal plan using the getters id
								for (Ingredient currentIngredient : addedMealPlan.getIngredients()) {
									boolean found = false;
									for (Ingredient retrievedIngredient : retrievedMealPlan.getIngredients()) {
										if (currentIngredient.getId().equals(retrievedIngredient.getId())) {
											found = true;
											break;
										}
									}
									assertTrue(found);
								}
								ingredientLatch.countDown();
							} else {
								Log.e(TAG, "Failed to retrieve meal plan");
							}
						});
					} else {
						Log.e(TAG, "Failed to add meal plan");
					}
				});
			} else {
				Log.e(TAG, "Failed to add ingredient");
			}
		});
		if (!ingredientLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}
	}

	/**
	 * Test adding a MealPlan after adding an ingredient
	 */
	@Test
	public void testAddIngredientToMealPlan() throws InterruptedException {
		// Create a mock ingredient
		StorageIngredient ingredient = mockIngredient();
		// Create a mock meal plan
		MealPlan mealPlan = mockMealPlan();
		// CountDownLatch to wait for the meal plan to be added
		CountDownLatch mealPlanLatch = new CountDownLatch(1);
		// Add the meal plan to the database
		mealPlanDB.addMealPlan(mealPlan, (addedMealPlan, success) -> {
			if (success) {
				// Add the ingredient to the database
				storedIngredientDB.addStorageIngredient(ingredient,
					(addedIngredient, success2) -> {
						if (success2) {
							// Update the meal plan with the ingredient
							addedMealPlan.addIngredient(addedIngredient);
							// Add the ingredient to the meal plan
							mealPlanDB.updateMealPlan(addedMealPlan, (updatedMealPlan, success3) -> {
								if (success3) {
									// Check that the meal plan was added
									mealPlanDB.getMealPlan(updatedMealPlan.getId(), (retrievedMealPlan, success4) -> {
										if (success4) {
											// Compare using the getters
											assertEquals(updatedMealPlan.getId(), retrievedMealPlan.getId());
											assertEquals(updatedMealPlan.getTitle(),
												retrievedMealPlan.getTitle());
											assertEquals(updatedMealPlan.getCategory(),
												retrievedMealPlan.getCategory());
											assertEquals(updatedMealPlan.getEatDate(),
												retrievedMealPlan.getEatDate());
											assertEquals(updatedMealPlan.getServings(),
												retrievedMealPlan.getServings());
											// Check if the recipes are the same size
											assertEquals(addedMealPlan.getRecipes().size(),
												retrievedMealPlan.getRecipes().size());
											// Check if the recipes exist in the retrieved meal plan using the getters id
											for (Recipe currentRecipe : addedMealPlan.getRecipes()) {
												boolean found = false;
												for (Recipe retrievedRecipe : retrievedMealPlan.getRecipes()) {
													if (currentRecipe.getId().equals(retrievedRecipe.getId())) {
														found = true;
														break;
													}
												}
												assertTrue(found);
											}
											// Check if the ingredients are the same size
											assertEquals(addedMealPlan.getIngredients().size(),
												retrievedMealPlan.getIngredients().size());
											// Check if the ingredients exist in the retrieved meal plan using the getters id
											for (Ingredient currentIngredient : addedMealPlan.getIngredients()) {
												boolean found = false;
												for (Ingredient retrievedIngredient : retrievedMealPlan.getIngredients()) {
													if (currentIngredient.getId().equals(retrievedIngredient.getId())) {
														found = true;
														break;
													}
												}
												assertTrue(found);
											}
											mealPlanLatch.countDown();
										} else {
											Log.e(TAG, "Failed to retrieve meal plan");
										}
									});
								} else {
									Log.e(TAG, "Failed to update meal plan");
								}
							});
						} else {
							Log.e(TAG, "Failed to add ingredient");
						}
					});
			} else {
				Log.e(TAG, "Failed to add meal plan");
			}
		});
		if (!mealPlanLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}
	}

	/**
	 * Test adding multiple MealPlans to the database
	 */
	@Test
	public void testAddMultipleMealPlans() throws InterruptedException {
		// Create a mock meal plan
		MealPlan mealPlan = mockMealPlan();
		// CountDownLatch to wait for the meal plan to be added
		CountDownLatch mealPlanLatch = new CountDownLatch(1);
		// Add the meal plan to the database
		mealPlanDB.addMealPlan(mealPlan, (addedMealPlan, success) -> {
			if (success) {
				// Create a mock meal plan
				MealPlan mealPlan2 = mockMealPlan();
				// Add the meal plan to the database
				mealPlanDB.addMealPlan(mealPlan2, (addedMealPlan2, success2) -> {
					if (success2) {
						// Check that the meal plan was added
						mealPlanDB.getMealPlan(addedMealPlan2.getId(), (retrievedMealPlan, success3) -> {
							if (success3) {
								// Compare using the getters
								assertEquals(addedMealPlan2.getId(), retrievedMealPlan.getId());
								assertEquals(addedMealPlan2.getTitle(),
									retrievedMealPlan.getTitle());
								assertEquals(addedMealPlan2.getCategory(),
									retrievedMealPlan.getCategory());
								assertEquals(addedMealPlan2.getEatDate(),
									retrievedMealPlan.getEatDate());
								assertEquals(addedMealPlan2.getServings(),
									retrievedMealPlan.getServings());
								// Check if the recipes are the same size
								assertEquals(addedMealPlan.getRecipes().size(),
									retrievedMealPlan.getRecipes().size());
								// Check if the recipes exist in the retrieved meal plan using the getters id
								for (Recipe currentRecipe : addedMealPlan.getRecipes()) {
									boolean found = false;
									for (Recipe retrievedRecipe : retrievedMealPlan.getRecipes()) {
										if (currentRecipe.getId().equals(retrievedRecipe.getId())) {
											found = true;
											break;
										}
									}
									assertTrue(found);
								}
								// Check if the ingredients are the same size
								assertEquals(addedMealPlan.getIngredients().size(),
									retrievedMealPlan.getIngredients().size());
								// Check if the ingredients exist in the retrieved meal plan using the getters id
								for (Ingredient currentIngredient : addedMealPlan.getIngredients()) {
									boolean found = false;
									for (Ingredient retrievedIngredient : retrievedMealPlan.getIngredients()) {
										if (currentIngredient.getId().equals(retrievedIngredient.getId())) {
											found = true;
											break;
										}
									}
									assertTrue(found);
								}
								mealPlanLatch.countDown();
							} else {
								Log.e(TAG, "Failed to retrieve meal plan");
							}
						});
					} else {
						Log.e(TAG, "Failed to add meal plan");
					}
				});
			} else {
				Log.e(TAG, "Failed to add meal plan");
			}
		});
		if (!mealPlanLatch.await(TIMEOUT, SECONDS)) {
			throw new InterruptedException();
		}
	}

	/**
	 * Test adding a MealPlan with a 2 ingredients and 3 recipes to the database
	 */
	@Test
	public void testAddMealPlanWithIngredientsAndRecipes() throws InterruptedException {
		// CountDownLatch to wait for the meal plan to be added
		CountDownLatch mealPlanLatch = new CountDownLatch(1);
		// Create a mock ingredient
		StorageIngredient ingredient = mockIngredient();
		// Create a mock ingredient
		StorageIngredient ingredient2 = mockIngredient();
		// Create a mock recipe
		Recipe recipe = mockRecipe();
		// Create a mock recipe
		Recipe recipe2 = mockRecipe();
		// Create a mock recipe
		Recipe recipe3 = mockRecipe();
		// Create a mock meal plan
		MealPlan mealPlan = mockMealPlan();
		// Add the meal plan to the database
		mealPlanDB.addMealPlan(mealPlan, (addedMealPlan, success) -> {
			if (success) {
				// Add the ingredient to the database
				storedIngredientDB.addStorageIngredient(ingredient,
					(addedIngredient, success2) -> {
						if (success2) {
							// Add the ingredient to the database
							storedIngredientDB.addStorageIngredient(ingredient2,
								(addedIngredient2, success3) -> {
									if (success3) {
										// Add the recipe to the database
										recipeDB.addRecipe(recipe, (addedRecipe, success4) -> {
											if (success4) {
												// Add the recipe to the database
												recipeDB.addRecipe(recipe2, (addedRecipe2, success5) -> {
													if (success5) {
														// Add the recipe to the database
														recipeDB.addRecipe(recipe3, (addedRecipe3, success6) -> {
															if (success6) {
																// Update the meal plan with the ingredient
																addedMealPlan.addIngredient(addedIngredient);
																addedMealPlan.addIngredient(addedIngredient2);
																// Update the meal plan with the recipe
																addedMealPlan.addRecipe(addedRecipe);
																addedMealPlan.addRecipe(addedRecipe2);
																addedMealPlan.addRecipe(addedRecipe3);
																// Add the ingredient to the meal plan
																mealPlanDB.updateMealPlan(addedMealPlan, (updatedMealPlan, success7) -> {
																	if (success7) {
																		// Check that the meal plan was added
																		mealPlanDB.getMealPlan(updatedMealPlan.getId(), (retrievedMealPlan, success8) -> {
																			if (success8) {
																				// Compare using the getters
																				assertEquals(updatedMealPlan.getId(), retrievedMealPlan.getId());
																				assertEquals(updatedMealPlan.getTitle(),
																					retrievedMealPlan.getTitle());
																				assertEquals(updatedMealPlan.getCategory(),
																					retrievedMealPlan.getCategory());
																				assertEquals(updatedMealPlan.getEatDate(),
																					retrievedMealPlan.getEatDate());
																				assertEquals(updatedMealPlan.getServings(),
																					retrievedMealPlan.getServings());
																				// Check if the ingredients are the same size
																				assertEquals(addedMealPlan.getIngredients().size(),
																					retrievedMealPlan.getIngredients().size());
																				// Check if the ingredients exist in the retrieved meal plan using the getters id
																				for (Ingredient currentIngredient : addedMealPlan.getIngredients()) {
																					boolean found = false;
																					for (Ingredient retrievedIngredient : retrievedMealPlan.getIngredients()) {
																						if (currentIngredient.getId().equals(retrievedIngredient.getId())) {
																							found = true;
																							break;
																						}
																					}
																					assertTrue(found);
																				}
																				// Check if the recipes are the same size
																				assertEquals(addedMealPlan.getRecipes().size(),
																					retrievedMealPlan.getRecipes().size());
																				// Check if the recipes exist in the retrieved meal plan using the getters id
																				for (Recipe currentRecipe : addedMealPlan.getRecipes()) {
																					boolean found = false;
																					for (Recipe retrievedRecipe : retrievedMealPlan.getRecipes()) {
																						if (currentRecipe.getId().equals(retrievedRecipe.getId())) {
																							found = true;
																							break;
																						}
																					}
																					assertTrue(found);
																				}
																				mealPlanLatch.countDown();
																			} else {
																				Log.e(TAG, "Failed to retrieve meal plan");
																			}
																		});
																	} else {
																		Log.e(TAG, "Failed to update meal plan");
																	}
																});
															} else {
																Log.e(TAG, "Failed to add recipe 3");
															}
														});
													} else {
														Log.e(TAG, "Failed to add recipe 2");
													}
												});
											} else {
												Log.e(TAG, "Failed to add recipe");
											}
										});
									} else {
										Log.e(TAG, "Failed to add ingredient 2");
									}
								});
						} else {
							Log.e(TAG, "Failed to add ingredient");
						}
					});
			} else {
				Log.e(TAG, "Failed to add meal plan");
			}
		});
		if (!mealPlanLatch.await(TIMEOUT, TimeUnit.SECONDS)) {
			throw new InterruptedException();
		}
	}

	/**
	 * Test adding 2 meal plans to the database and deleting them the second one
	 */
	@Test
	public void testAdd2MealPlansDeleteSecond() throws InterruptedException {
		// CountDownLatch to wait for the asynchronous calls to finish
		CountDownLatch mealPlanLatch = new CountDownLatch(1);
		// Create a meal plan
		MealPlan mealPlan = mockMealPlan();
		// Create a meal plan
		MealPlan mealPlan2 = mockMealPlan();
		// Add the meal plan to the database
		mealPlanDB.addMealPlan(mealPlan, (addedMealPlan, success) -> {
			if (success) {
				// Add the meal plan to the database
				mealPlanDB.addMealPlan(mealPlan2, (addedMealPlan2, success2) -> {
					if (success2) {
						mealPlanDB.getMealPlans((allMealPlans1, success3) -> {
							// Delete the second meal plan
							mealPlanDB.delMealPlan(addedMealPlan2,
								(deletedMealPlan, success4) -> {
									if (success4) {
										mealPlanDB.getMealPlans((retrievedMealPlans, success5) -> {
											if (success5) {
												// Check if length is 0 (no meal plans)
												assertEquals(allMealPlans1.size() - 1, retrievedMealPlans.size());
											} else {
												Log.e(TAG, "Failed to retrieve meal plans");
											}
											mealPlanLatch.countDown();
										});
									} else {
										Log.e(TAG, "Failed to delete meal plan");
									}
								});
						});
					} else {
						Log.e(TAG, "Failed to add meal plan 2");
					}
				});
			} else {
				Log.e(TAG, "Failed to add meal plan");
			}
		});
		if (!mealPlanLatch.await(TIMEOUT, TimeUnit.SECONDS)) {
			throw new InterruptedException();
		}
	}

	/**
	 * Test updating a meal plan title, category, eat date, servings,
	 * recipes, and ingredients in the database and retrieving it
	 */
	@Test
	public void testUpdateMealPlan() throws InterruptedException {
		// CountDownLatch to wait for the asynchronous calls to finish
		CountDownLatch mealPlanLatch = new CountDownLatch(1);
		// Create a meal plan
		MealPlan mealPlan = mockMealPlan();
		// Add the meal plan to the database
		mealPlanDB.addMealPlan(mealPlan, (addedMealPlan, success) -> {
			if (success) {
				// Update the meal plan
				MealPlan updatedMealPlan = mockMealPlan();
				updatedMealPlan.setId(addedMealPlan.getId());
				updatedMealPlan.setTitle("Updated Title");
				updatedMealPlan.setCategory("Updated Category");
				updatedMealPlan.setEatDate(new Date());
				updatedMealPlan.setServings(2);
				updatedMealPlan.addRecipe(mockRecipe());
				updatedMealPlan.addRecipe(mockRecipe());
				updatedMealPlan.addIngredient(mockIngredient());
				updatedMealPlan.addIngredient(mockIngredient());

				// Update the meal plan in the database
				mealPlanDB.updateMealPlan(updatedMealPlan,
					(test, success2) -> {
						if (success2) {
							// Retrieve the meal plan
							mealPlanDB.getMealPlan(addedMealPlan.getId(), (retrievedMealPlan, success3) -> {
								if (success3) {
									// Compare using the getters
									assertEquals(updatedMealPlan.getId(), retrievedMealPlan.getId());
									assertEquals(updatedMealPlan.getTitle(), retrievedMealPlan.getTitle());
									assertEquals(updatedMealPlan.getCategory(), retrievedMealPlan.getCategory());
									assertEquals(updatedMealPlan.getEatDate(), retrievedMealPlan.getEatDate());
									assertEquals(updatedMealPlan.getServings(), retrievedMealPlan.getServings());
									// Check if the recipes are the same size
									assertEquals(updatedMealPlan.getRecipes().size(),
										retrievedMealPlan.getRecipes().size());
									// Check if the recipes exist in the retrieved meal plan using the getters id
									for (Recipe currentRecipe : updatedMealPlan.getRecipes()) {
										boolean found = false;
										for (Recipe retrievedRecipe : retrievedMealPlan.getRecipes()) {
											if (currentRecipe.getId().equals(retrievedRecipe.getId())) {
												found = true;
												break;
											}
										}
										assertTrue(found);
									}
									// Check if the ingredients are the same size
									assertEquals(updatedMealPlan.getIngredients().size(),
										retrievedMealPlan.getIngredients().size());
									// Check if the ingredients exist in the retrieved meal plan using the getters id
									for (Ingredient currentIngredient : updatedMealPlan.getIngredients()) {
										boolean found = false;
										for (Ingredient retrievedIngredient : retrievedMealPlan.getIngredients()) {
											if (currentIngredient.getId().equals(retrievedIngredient.getId())) {
												found = true;
												break;
											}
										}
										assertTrue(found);
									}								// Count down the latch
									mealPlanLatch.countDown();
								} else {
									Log.e(TAG, "Failed to retrieve meal plan");
								}
							});
						} else {
							Log.e(TAG, "Failed to update meal plan");
						}
					});
			} else {
				Log.e(TAG, "Failed to add meal plan");
			}
		});
		if (!mealPlanLatch.await(TIMEOUT, TimeUnit.SECONDS)) {
			throw new InterruptedException();
		}
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
		mealPlanDB.addMealPlan(mealPlan, (addedMealPlan, success) -> {
			if (success) {
				// Retrieve the meal plan
				mealPlanDB.getMealPlan(addedMealPlan.getId(), (retrievedMealPlan, success2) -> {
					if (success2) {
						// Compare using the getters
						assertEquals(addedMealPlan.getId(), retrievedMealPlan.getId());
						assertEquals(addedMealPlan.getTitle(), retrievedMealPlan.getTitle());
						assertEquals(addedMealPlan.getCategory(), retrievedMealPlan.getCategory());
						assertEquals(addedMealPlan.getEatDate(), retrievedMealPlan.getEatDate());
						assertEquals(addedMealPlan.getServings(), retrievedMealPlan.getServings());
						// Check if the recipes are the same size
						assertEquals(addedMealPlan.getRecipes().size(),
							retrievedMealPlan.getRecipes().size());
						// Check if the recipes exist in the retrieved meal plan using the getters id
						for (Recipe currentRecipe : addedMealPlan.getRecipes()) {
							boolean found = false;
							for (Recipe retrievedRecipe : retrievedMealPlan.getRecipes()) {
								if (currentRecipe.getId().equals(retrievedRecipe.getId())) {
									found = true;
									break;
								}
							}
							assertTrue(found);
						}
						// Check if the ingredients are the same size
						assertEquals(addedMealPlan.getIngredients().size(),
							retrievedMealPlan.getIngredients().size());
						// Check if the ingredients are the same
						// Check if the ingredients exist in the retrieved meal plan using the getters id
						for (Ingredient currentIngredient : addedMealPlan.getIngredients()) {
							boolean found = false;
							for (Ingredient retrievedIngredient : retrievedMealPlan.getIngredients()) {
								if (currentIngredient.getId().equals(retrievedIngredient.getId())) {
									found = true;
									break;
								}
							}
							assertTrue(found);
						}
						// Count down the latch
						mealPlanLatch.countDown();
					} else {
						Log.e(TAG, "Failed to retrieve meal plan");
					}
				});
			} else {
				Log.e(TAG, "Failed to add meal plan");
			}
		});
		if (!mealPlanLatch.await(TIMEOUT, TimeUnit.SECONDS)) {
			throw new InterruptedException();
		}
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
		// Add 5 different ingredients
		mealPlan.addIngredient(new StorageIngredient("Banana", 2.0, "Banana",
			"Pantry", new Date()));
		mealPlan.addIngredient(new StorageIngredient("Apple", 2.0, "Apple",
			"Pantry", new Date()));
		mealPlan.addIngredient(new StorageIngredient("Orange", 2.0, "Orange",
			"Pantry", new Date()));
		mealPlan.addIngredient(new StorageIngredient("Pear", 2.0, "Pear",
			"Pantry", new Date()));
		mealPlan.addIngredient(new StorageIngredient("Grape", 2.0, "Grape",
			"Pantry", new Date()));
		// Add the meal plan to the database
		mealPlanDB.addMealPlan(mealPlan, (addedMealPlan, success) -> {
			if (success) {
				// Retrieve the meal plan
				mealPlanDB.getMealPlan(addedMealPlan.getId(), (retrievedMealPlan, success2) -> {
					if (success2) {
						// Compare using the getters
						assertEquals(addedMealPlan.getId(), retrievedMealPlan.getId());
						assertEquals(addedMealPlan.getTitle(), retrievedMealPlan.getTitle());
						assertEquals(addedMealPlan.getCategory(), retrievedMealPlan.getCategory());
						assertEquals(addedMealPlan.getEatDate(), retrievedMealPlan.getEatDate());
						assertEquals(addedMealPlan.getServings(), retrievedMealPlan.getServings());
						// Check if the recipes are the same size
						assertEquals(addedMealPlan.getRecipes().size(),
							retrievedMealPlan.getRecipes().size());
						// Check if the recipes exist in the retrieved meal plan using the getters id
						for (Recipe currentRecipe : addedMealPlan.getRecipes()) {
							boolean found = false;
							for (Recipe retrievedRecipe : retrievedMealPlan.getRecipes()) {
								if (currentRecipe.getId().equals(retrievedRecipe.getId())) {
									found = true;
									break;
								}
							}
							assertTrue(found);
						}
						// Check if the ingredients are the same size
						assertEquals(addedMealPlan.getIngredients().size(),
							retrievedMealPlan.getIngredients().size());
						// Check if the ingredients exist in the retrieved meal plan using the getters id
						for (Ingredient currentIngredient : addedMealPlan.getIngredients()) {
							boolean found = false;
							for (Ingredient retrievedIngredient : retrievedMealPlan.getIngredients()) {
								if (currentIngredient.getId().equals(retrievedIngredient.getId())) {
									found = true;
									break;
								}
							}
							assertTrue(found);
						}
						// Count down the latch
						mealPlanLatch.countDown();
					} else {
						Log.e(TAG, "Failed to retrieve meal plan");
					}
				});
			} else {
				Log.e(TAG, "Failed to add meal plan");
			}
		});
		if (!mealPlanLatch.await(TIMEOUT, TimeUnit.SECONDS)) {
			throw new InterruptedException();
		}
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
		mealPlanDB.addMealPlan(mealPlan, (addedMealPlan, success) -> {
			if (success) {
				// Retrieve the meal plan
				mealPlanDB.getMealPlan(addedMealPlan.getId(), (retrievedMealPlan, success2) -> {
					if (success2) {
						// Compare using the getters
						assertEquals(addedMealPlan.getId(), retrievedMealPlan.getId());
						assertEquals(addedMealPlan.getTitle(), retrievedMealPlan.getTitle());
						assertEquals(addedMealPlan.getCategory(), retrievedMealPlan.getCategory());
						assertEquals(addedMealPlan.getEatDate(), retrievedMealPlan.getEatDate());
						assertEquals(addedMealPlan.getServings(), retrievedMealPlan.getServings());
						// Check if the recipes are the same size
						assertEquals(addedMealPlan.getRecipes().size(),
							retrievedMealPlan.getRecipes().size());
						// Check if the recipes exist in the retrieved meal plan using the getters id
						for (Recipe currentRecipe : addedMealPlan.getRecipes()) {
							boolean found = false;
							for (Recipe retrievedRecipe : retrievedMealPlan.getRecipes()) {
								if (currentRecipe.getId().equals(retrievedRecipe.getId())) {
									found = true;
									break;
								}
							}
							assertTrue(found);
						}
						// Check if the ingredients are the same size
						assertEquals(addedMealPlan.getIngredients().size(),
							retrievedMealPlan.getIngredients().size());
						// Check if the ingredients exist in the retrieved meal plan using the getters id
						for (Ingredient currentIngredient : addedMealPlan.getIngredients()) {
							boolean found = false;
							for (Ingredient retrievedIngredient : retrievedMealPlan.getIngredients()) {
								if (currentIngredient.getId().equals(retrievedIngredient.getId())) {
									found = true;
									break;
								}
							}
							assertTrue(found);
						}
						// Count down the latch
						mealPlanLatch.countDown();
					} else {
						Log.e(TAG, "Failed to retrieve meal plan");
					}
				});
			} else {
				Log.e(TAG, "Failed to add meal plan");
			}
		});
		if (!mealPlanLatch.await(TIMEOUT, TimeUnit.SECONDS)) {
			throw new InterruptedException();
		}
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
		recipe1.setPrepTimeMinutes(10);
		recipe1.setComments("Test");
		mealPlan.addRecipe(recipe1);
		Recipe recipe2 = new Recipe("Beef");
		Ingredient ingredient2 = new Ingredient("Beef");
		recipe2.addIngredient(ingredient2);
		recipe2.setCategory("Main");
		recipe2.setServings(1);
		recipe2.setPrepTimeMinutes(10);
		recipe2.setComments("Test");
		mealPlan.addRecipe(recipe2);
		Recipe recipe3 = new Recipe("Pork");
		Ingredient ingredient3 = new Ingredient("Pork");
		recipe3.addIngredient(ingredient3);
		recipe3.setCategory("Main");
		recipe3.setServings(1);
		recipe3.setPrepTimeMinutes(10);
		recipe3.setComments("Test");
		mealPlan.addRecipe(recipe3);
		Recipe recipe4 = new Recipe("Fish");
		Ingredient ingredient4 = new Ingredient("Fish");
		recipe4.addIngredient(ingredient4);
		recipe4.setCategory("Main");
		recipe4.setServings(1);
		recipe4.setPrepTimeMinutes(10);
		recipe4.setComments("Test");
		mealPlan.addRecipe(recipe4);
		Recipe recipe5 = new Recipe("Lamb");
		Ingredient ingredient5 = new Ingredient("Lamb");
		recipe5.addIngredient(ingredient5);
		recipe5.setCategory("Main");
		recipe5.setServings(1);
		recipe5.setPrepTimeMinutes(10);
		recipe5.setComments("Test");
		mealPlan.addRecipe(recipe5);



		// Add the meal plan to the database
		mealPlanDB.addMealPlan(mealPlan, (addedMealPlan, success) -> {
			if (success) {
				// Retrieve the meal plan
				mealPlanDB.getMealPlan(addedMealPlan.getId(), (retrievedMealPlan, success2) -> {
					if (success2) {
						// Compare using the getters
						assertEquals(addedMealPlan.getId(), retrievedMealPlan.getId());
						assertEquals(addedMealPlan.getTitle(), retrievedMealPlan.getTitle());
						assertEquals(addedMealPlan.getCategory(), retrievedMealPlan.getCategory());
						assertEquals(addedMealPlan.getEatDate(), retrievedMealPlan.getEatDate());
						assertEquals(addedMealPlan.getServings(), retrievedMealPlan.getServings());
						// Check if the recipes are the same size
						assertEquals(addedMealPlan.getRecipes().size(),
							retrievedMealPlan.getRecipes().size());
						// Check if the recipes exist in the retrieved meal plan using the getters id
						for (Recipe currentRecipe : addedMealPlan.getRecipes()) {
							boolean found = false;
							for (Recipe retrievedRecipe : retrievedMealPlan.getRecipes()) {
								if (currentRecipe.getId().equals(retrievedRecipe.getId())) {
									found = true;
									break;
								}
							}
							assertTrue(found);
						}
						// Check if the ingredients are the same size
						assertEquals(addedMealPlan.getIngredients().size(),
							retrievedMealPlan.getIngredients().size());
						// Check if the ingredients exist in the retrieved meal plan using the getters id
						for (Ingredient currentIngredient : addedMealPlan.getIngredients()) {
							boolean found = false;
							for (Ingredient retrievedIngredient : retrievedMealPlan.getIngredients()) {
								if (currentIngredient.getId().equals(retrievedIngredient.getId())) {
									found = true;
									break;
								}
							}
							assertTrue(found);
						}
						// Count down the latch
						mealPlanLatch.countDown();
					} else {
						Log.e(TAG, "Failed to retrieve meal plan");
					}
				});
			} else {
				Log.e(TAG, "Failed to add meal plan");
			}
		});
		if (!mealPlanLatch.await(TIMEOUT, TimeUnit.SECONDS)) {
			throw new InterruptedException();
		}
	}
}
