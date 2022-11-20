package com.example.wellfed;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import android.util.Log;

import com.example.wellfed.common.DBConnection;
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
	public void testAddMealPlan() {
		MealPlan mealPlan = mockMealPlan();

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
						assertEquals(addedMealPlan.getRecipes(), retrievedMealPlan.getRecipes());
						assertEquals(addedMealPlan.getIngredients(),
							retrievedMealPlan.getIngredients());
					} else {
						Log.e(TAG, "Failed to retrieve meal plan");
					}
				});
			} else {
				Log.e(TAG, "Failed to add meal plan");
			}
		});
	}

	/**
	 * Testing adding a MealPlan with a Recipe
	 */
	@Test
	public void testAddMealPlanWithRecipe() {
		// Create a mock recipe
		Recipe recipe = mockRecipe();
		// Add the recipe to the database
		recipeDB.addRecipe(recipe, (addedRecipe, success) -> {
			if (success) {
				// Create a mock meal plan
				MealPlan mealPlan = mockMealPlan();
				// Set the recipe
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
								assertEquals(addedMealPlan.getRecipes(),
									retrievedMealPlan.getRecipes());
								assertEquals(addedMealPlan.getIngredients(),
									retrievedMealPlan.getIngredients());
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
	}

	/**
	 * Test adding recipe to meal plan after meal plan is added to database
	 */
	@Test
	public void testAddRecipeToMealPlan() {
		// Create a mock recipe
		Recipe recipe = mockRecipe();
		// Create a mock meal plan
		MealPlan mealPlan = mockMealPlan();
		// Add the meal plan to the database
		mealPlanDB.addMealPlan(mealPlan, (addedMealPlan, success) -> {
			if (success) {
				// Add the recipe to the database
				recipeDB.addRecipe(recipe, (addedRecipe, success2) -> {
					if (success2) {
						// Update the meal plan with the recipe
						addedMealPlan.addRecipe(addedRecipe);
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
										assertEquals(updatedMealPlan.getRecipes(),
											retrievedMealPlan.getRecipes());
										assertEquals(updatedMealPlan.getIngredients(),
											retrievedMealPlan.getIngredients());
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
	}

	/**
	 * Test adding a MealPlan with an ingredient
	 */
	@Test
	public void testAddMealPlanWithIngredient() {
		// Create a mock ingredient
		StorageIngredient ingredient = mockIngredient();
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
								assertEquals(addedMealPlan.getRecipes(),
									retrievedMealPlan.getRecipes());
								assertEquals(addedMealPlan.getIngredients(),
									retrievedMealPlan.getIngredients());
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
	}

	/**
	 * Test adding a MealPlan after adding an ingredient
	 */
	@Test
	public void testAddIngredientToMealPlan() {
		// Create a mock ingredient
		StorageIngredient ingredient = mockIngredient();
		// Create a mock meal plan
		MealPlan mealPlan = mockMealPlan();
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
											assertEquals(updatedMealPlan.getRecipes(),
												retrievedMealPlan.getRecipes());
											assertEquals(updatedMealPlan.getIngredients(),
												retrievedMealPlan.getIngredients());
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
	}

	/**
	 * Test adding multiple MealPlans to the database
	 */
	@Test
	public void testAddMultipleMealPlans() {
		// Create a mock meal plan
		MealPlan mealPlan = mockMealPlan();
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
								assertEquals(addedMealPlan2.getRecipes(),
									retrievedMealPlan.getRecipes());
								assertEquals(addedMealPlan2.getIngredients(),
									retrievedMealPlan.getIngredients());
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
	}

	/**
	 * Test adding a MealPlan with a 2 ingredients and 3 recipes to the database
	 */
	@Test
	public void testAddMealPlanWithIngredientsAndRecipes() {
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
																				assertEquals(updatedMealPlan.getRecipes(),
																					retrievedMealPlan.getRecipes());
																				assertEquals(updatedMealPlan.getIngredients(),
																					retrievedMealPlan.getIngredients());
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
	}

	/**
	 * Test adding 2 meal plans to the database and deleting them the second one
	 */
	@Test
	public void testAdd2MealPlansDeleteSecond() {
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
						// Delete the second meal plan
						mealPlanDB.delMealPlan(addedMealPlan2,
							(deletedMealPlan, success3) -> {
								if (success3) {
									// Check that the meal plan was deleted
									mealPlanDB.getMealPlan(addedMealPlan2.getId(), (retrievedMealPlan, success4) -> {
										if (success4) {
											// Compare using the getters
											assertNull(retrievedMealPlan);
										} else {
											Log.e(TAG, "Failed to retrieve meal plan");
										}
									});
								} else {
									Log.e(TAG, "Failed to delete meal plan");
								}
							});
					} else {
						Log.e(TAG, "Failed to add meal plan 2");
					}
				});
			} else {
				Log.e(TAG, "Failed to add meal plan");
			}
		});
	}

	/**
	 * Test updating a meal plan title, category, eat date, servings,
	 * recipes, and ingredients in the database and retrieving it
	 */
	@Test
	public void testUpdateMealPlan() {
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
								assertEquals(updatedMealPlan.getRecipes(), retrievedMealPlan.getRecipes());
								assertEquals(updatedMealPlan.getIngredients(), retrievedMealPlan.getIngredients());
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
	}

	/**
	 * Test adding a meal plan with 5 ingredients to the database
	 */
	@Test
	public void testAddMealPlanWith5Ingredients() {
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
						assertEquals(addedMealPlan.getRecipes(), retrievedMealPlan.getRecipes());
						assertEquals(addedMealPlan.getIngredients(), retrievedMealPlan.getIngredients());
					} else {
						Log.e(TAG, "Failed to retrieve meal plan");
					}
				});
			} else {
				Log.e(TAG, "Failed to add meal plan");
			}
		});
	}
}
