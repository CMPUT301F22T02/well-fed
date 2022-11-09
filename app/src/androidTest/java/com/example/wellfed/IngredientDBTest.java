package com.example.wellfed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import static java.util.concurrent.TimeUnit.SECONDS;

import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.ingredient.IngredientDB;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

/**
 * The IngredientDBTest class is used to test the IngredientDB class.
 */
@RunWith(AndroidJUnit4.class)
public class IngredientDBTest {
    private static final String TAG = "IngredientDBTest";
    /**
     * Holds the timeout in seconds for the CountDownLatch.
     */
    private static final long TIMEOUT = 5;
    /**
     * Holds the instance of the IngredientDB class.
     */
    IngredientDB ingredientDB;
    /**
     * Holds the instance of the mockIngredient
     */
    Ingredient mockIngredient;
    /**
     * Holds the instance of the nonExistingIngredient
     */
    Ingredient nonExistingIngredient;

    /**
     * Creates a new IngredientDB object and mock ingredients.
     */
    @Before
    public void before() {
        ingredientDB = new IngredientDB();
        mockIngredient = new Ingredient("Broccoli");
        mockIngredient.setCategory("Vegetable");
        nonExistingIngredient = new Ingredient(null);
        nonExistingIngredient.setId("-1");
    }

    /**
     * Tests addIngredient and getIngredient on DB with a complete
     * ingredient, checks if the ingredient is added to DB and retrieved from
     * DB.
     *
     * @throws InterruptedException if the test times out
     */
    @Test
    public void testAddFull() throws InterruptedException {
        Log.d(TAG, "testAddFull");
        CountDownLatch latch = new CountDownLatch(1);
        ingredientDB.addIngredient(mockIngredient, ingredient -> {
            Log.d(TAG, ":onAddIngredient");
            String id = ingredient.getId();
            assertNotNull(ingredient);

            ingredientDB.getIngredient(id, getIngredient -> {
                Log.d(TAG, ":onGetIngredient");
                assertNotNull(getIngredient);
                assertEquals(mockIngredient.getCategory(),
                        getIngredient.getCategory());
                assertEquals(mockIngredient.getDescription(),
                        getIngredient.getDescription());

                // remove the ingredient
                ingredientDB.deleteIngredient(getIngredient,
                        (deleteIngredient) -> {
                            Log.d(TAG, ":onDeleteIngredient");
                            latch.countDown();
                        });
            });
        });
        if (!latch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }
    }

    /**
     * Tests the add and get functionality, when fields are blank.
     *
     * @throws InterruptedException if the test times out
     */
    @Test
    public void testAddMissingFields() throws InterruptedException {
        Log.d(TAG, "testAddMissingFields");
        CountDownLatch latch = new CountDownLatch(1);

        mockIngredient.setCategory(null);

        // testing whether it was what was inserted into db
        ingredientDB.addIngredient(mockIngredient, ingredient -> {
            Log.d(TAG, ":onAddIngredient");
            String id = ingredient.getId();
            assertNotNull(ingredient);

            ingredientDB.getIngredient(id, getIngredient -> {
                Log.d(TAG, ":onGetIngredient");
                assertNotNull(getIngredient);
                assertEquals("Broccoli", getIngredient.getDescription());
                assertNull(getIngredient.getCategory());

                // remove the ingredient
                ingredientDB.deleteIngredient(getIngredient,
                        (deleteIngredient) -> {
                            Log.d(TAG, ":onDeleteIngredient");
                            latch.countDown();
                        });
            });
        });

        if (!latch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }
    }

    /**
     * Tests deleteIngredient on DB, checks if the ingredient is deleted.
     *
     * @throws InterruptedException if the test times out
     */
    @Test
    public void testDeleteIngredient() throws InterruptedException {
        Log.d(TAG, "testDeleteIngredient");
        CountDownLatch latch = new CountDownLatch(1);

        ingredientDB.addIngredient(mockIngredient, addIngredient -> {
            Log.d(TAG, ":onAddIngredient");
            String id = addIngredient.getId();
            assertNotNull(addIngredient);
            ingredientDB.deleteIngredient(addIngredient, deleteIngredient -> {
                Log.d(TAG, ":onDeleteIngredient");
                assertNotNull(deleteIngredient);
                ingredientDB.getIngredient(id, getIngredient -> {
                    Log.d(TAG, ":onGetIngredient");
                    assertNull(getIngredient);
                    latch.countDown();
                });
            });
        });

        if (!latch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }
    }

    /**
     * Tests deleteIngredient with a NonExistingIngredient, checks if the
     * listener is called with not null.
     *
     * @throws InterruptedException if the test times out
     */
    @Test
    public void deleteNonExistingIngredient()
            throws InterruptedException {
        Log.d(TAG, "deleteNonExistingIngredient");
        CountDownLatch latch = new CountDownLatch(1);

        ingredientDB.deleteIngredient(nonExistingIngredient,
                deleteIngredient -> {
                    Log.d(TAG, ":onDeleteIngredient");
                    assertNotNull(deleteIngredient);
                    latch.countDown();
                });

        if (!latch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }
    }

    /**
     * Tests whether null is returned upon getting an invalid
     * ingredient.
     *
     * @throws InterruptedException if the test times out
     */
    @Test
    public void getNonExistingIngredient() throws InterruptedException {
        Log.d(TAG, "getNonExistingIngredient");
        CountDownLatch latch = new CountDownLatch(1);

        ingredientDB.getIngredient("-1",
                new IngredientDB.OnGetIngredientListener() {
                    @Override
                    public void onGetIngredient(Ingredient getIngredient) {
                        Log.d(TAG, ":onGetIngredient");
                        assertNull(getIngredient);
                        latch.countDown();
                    }
                });
        ingredientDB.getIngredient("-1", getIngredient -> {
            Log.d(TAG, ":onGetIngredient");
            assertNull(getIngredient);
            latch.countDown();
        });

        if (!latch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }
    }

    @Test
    public void getIngredientByCategory() throws InterruptedException {
        Log.d(TAG, "get Ingredient based on category and description");
        CountDownLatch latch = new CountDownLatch(1);

        ingredientDB.addIngredient(mockIngredient, addedIngredient -> {
            if (addedIngredient != null) {
                ingredientDB.getIngredient(mockIngredient, searchedIngredient -> {
                    Log.d(TAG, ":onGetIngredient by category");
                    assertNotNull(searchedIngredient);
                    ingredientDB.deleteIngredient(searchedIngredient, deletedIngredient -> {
                        Log.d(TAG, ":onDeleteIngredient");
                        latch.countDown();
                    });
                });
            }
        });

        if (!latch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }
    }


    /**
     * Tests updateIngredient on DB. Checks if the ingredient was updated.
     *
     * @throws InterruptedException if the test times out
     */
    @Test
    public void testUpdateIngredient() throws InterruptedException {
        Log.d(TAG, "testUpdateIngredient");

        String updatedCategory = "Fruit";
        String updatedDescription = "Apple";

        CountDownLatch latch = new CountDownLatch(1);

        ingredientDB.addIngredient(mockIngredient, addIngredient -> {
            Log.d(TAG, ":onAddIngredient");
            String id = addIngredient.getId();
            assertNotNull(addIngredient);

            addIngredient.setCategory(updatedCategory);
            addIngredient.setDescription(updatedDescription);

            ingredientDB.updateIngredient(addIngredient, updateIngredient -> {
                Log.d(TAG, ":onUpdateIngredient");
                assertNotNull(updateIngredient);
                ingredientDB.getIngredient(id, getIngredient -> {
                    Log.d(TAG, ":onGetIngredient");
                    assertEquals(updatedCategory, getIngredient.getCategory());
                    assertEquals(updatedDescription,
                            getIngredient.getDescription());
                    //  remove the ingredient
                    ingredientDB.deleteIngredient(getIngredient,
                            (deleteIngredient) -> {
                                Log.d(TAG, ":onDeleteIngredient");
                                latch.countDown();
                            });
                });
            });
        });
        if (!latch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }
    }


    @Test
    public void getIngredientByCategoryNotExists() throws InterruptedException {
        Log.d(TAG, "get Ingredient based on category and description");
        CountDownLatch latch = new CountDownLatch(1);
        Ingredient testIngredient = new Ingredient();
        testIngredient.setCategory("fffffffffffff");
        testIngredient.setDescription("affffffffffff");

        ingredientDB.getIngredient(testIngredient, searchIngredient -> {
            assertNull(searchIngredient);
            latch.countDown();
        });

        if (!latch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException("Timed out");
        }
    }
}


