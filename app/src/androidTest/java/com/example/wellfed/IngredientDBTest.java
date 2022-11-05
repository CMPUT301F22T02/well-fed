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
import com.example.wellfed.ingredient.StorageIngredient;
import com.example.wellfed.ingredient.StorageIngredientDB;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeoutException;


@RunWith(AndroidJUnit4.class) public class IngredientDBTest {
    private static final String TAG = "IngredientDBTest";
    private static final long TIMEOUT = 5;
    IngredientDB ingredientDB;
    Ingredient mockIngredient;

    @Before public void before() {
        ingredientDB = new IngredientDB();
        mockIngredient = new Ingredient("Broccoli");
        mockIngredient.setCategory("Vegetable");
    }

    /**
     * Tests the add functionality and get functionality of db,
     * with a complete ingredient.
     *
     * @throws InterruptedException if the test times out
     */
    @Test public void testAddFull() throws InterruptedException {
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
         * Tests deleting an ingredient from the database
         * @throws InterruptedException if the test times out
         */
        @Test
        public void testDeleteIngredient() throws InterruptedException {
            Log.d(TAG, "testDeleteIngredient");
            CountDownLatch latch = new CountDownLatch(1);

            ingredientDB.addIngredient(mockIngredient,
                    new IngredientDB.OnAddIngredientListener() {
                        @Override
                        public void onAddIngredient(Ingredient addIngredient) {
                            Log.d(TAG, ":onAddIngredient");
                            String id = addIngredient.getId();
                            assertNotNull(addIngredient);
                            ingredientDB.deleteIngredient(addIngredient,
                                    new IngredientDB.OnDeleteIngredientListener() {
                                        @Override
                                        public void onDeleteIngredient(
                                                Ingredient deleteIngredient) {
                                            Log.d(TAG, ":onDeleteIngredient");
                                            assertNotNull(deleteIngredient);
                                            ingredientDB.getIngredient(id,
                                                    new IngredientDB.OnGetIngredientListener() {
                                                        @Override
                                                        public void onGetIngredient(
                                                                Ingredient getIngredient) {
                                                            Log.d(TAG,
                                                                    ":onGetIngredient");
                                                            assertNull(
                                                                    getIngredient);
                                                            latch.countDown();
                                                        }
                                                    });
                                        }
                                    });
                        }
                    });

            if (!latch.await(TIMEOUT, SECONDS)) {
                throw new InterruptedException();
            }
        }
    //
    //    /**
    //     * Tests deleting a non-existing ingredient.
    //     */
    //    @Test
    //    public void deleteNonExistingIngredient() throws
    //    InterruptedException {
    //        // attempting to remove non-existing ingredient from db
    //        // this test will succeed if no error is thrown
    //        storageIngredientDB.removeFromIngredients("-1");
    //    }
    //
        /**
         * Tests whether null is returned upon getting an invalid
         ingredient.
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

            if (!latch.await(TIMEOUT, SECONDS)) {
                throw new InterruptedException();
            }
        }
    //
    //    /**
    //     * Tests whether all of the updated fields are reflected in the
    //     database.
    //     * @throws InterruptedException
    //     */
    //    @Test
    //    public void testUpdateIngredient() throws InterruptedException {
    //        StorageIngredient oldIngredient = new StorageIngredient
    //        ("Broccoli");
    //        oldIngredient.setAmount(5.0f);
    //        oldIngredient.setUnit("kg");
    //        Date bestBefore = new Date(2022, 10, 1);
    //        oldIngredient.setBestBefore(bestBefore);
    //        oldIngredient.setCategory("Vegetable");
    //        oldIngredient.setLocation("Fridge");
    //
    //        String id = storageIngredientDB.addStoredIngredient
    //        (oldIngredient);
    //
    //        // change description to Test2
    //        StorageIngredient updatedIngredient = oldIngredient;
    //        updatedIngredient.setDescription("Steamed Broccoli");
    //        storageIngredientDB.updateStoredIngredient(id, updatedIngredient);
    //        StorageIngredient resultIngredient = storageIngredientDB
    //        .getStoredIngredient(id);
    //        assertEquals("Steamed Broccoli", updatedIngredient
    //        .getDescription());
    //        assertEquals(5.0f, resultIngredient.getAmount(), 0.01);
    //        assertEquals("kg", resultIngredient.getUnit());
    //        assertEquals(bestBefore, resultIngredient.getBestBefore());
    //        assertEquals("Vegetable", resultIngredient.getCategory());
    //        assertEquals("Fridge", resultIngredient.getLocation());
    //
    //        // change amount to 4
    //        updatedIngredient.setAmount(4.0f);
    //        storageIngredientDB.updateStoredIngredient(id, updatedIngredient);
    //        resultIngredient = storageIngredientDB.getStoredIngredient(id);
    //        assertEquals("Steamed Broccoli", updatedIngredient
    //        .getDescription());
    //        assertEquals(4.0f, resultIngredient.getAmount(), 0.01);
    //        assertEquals("kg", resultIngredient.getUnit());
    //        assertEquals(bestBefore, resultIngredient.getBestBefore());
    //        assertEquals("Vegetable", resultIngredient.getCategory());
    //        assertEquals("Fridge", resultIngredient.getLocation());
    //
    //        // change unit to Test2
    //        updatedIngredient.setUnit("lb");
    //        storageIngredientDB.updateStoredIngredient(id, updatedIngredient);
    //        resultIngredient = storageIngredientDB.getStoredIngredient(id);
    //        assertEquals("Steamed Broccoli", updatedIngredient
    //        .getDescription());
    //        assertEquals(4.0f, resultIngredient.getAmount(), 0.01);
    //        assertEquals("lb", resultIngredient.getUnit());
    //        assertEquals(bestBefore, resultIngredient.getBestBefore());
    //        assertEquals("Vegetable", resultIngredient.getCategory());
    //        assertEquals("Fridge", resultIngredient.getLocation());
    //
    //        // change best-before to new date
    //        Date newBestBefore = new Date(2023, 11, 2);
    //        updatedIngredient.setBestBefore(newBestBefore);
    //        storageIngredientDB.updateStoredIngredient(id, updatedIngredient);
    //        resultIngredient = storageIngredientDB.getStoredIngredient(id);
    //        assertEquals("Steamed Broccoli", updatedIngredient
    //        .getDescription());
    //        assertEquals(4.0f, resultIngredient.getAmount(), 0.01);
    //        assertEquals("lb", resultIngredient.getUnit());
    //        assertEquals(newBestBefore, resultIngredient.getBestBefore());
    //        assertEquals("Vegetable", resultIngredient.getCategory());
    //        assertEquals("Fridge", resultIngredient.getLocation());
    //
    //        // change category
    //        updatedIngredient.setCategory("Cooked Vegetable");
    //        storageIngredientDB.updateStoredIngredient(id, updatedIngredient);
    //        resultIngredient = storageIngredientDB.getStoredIngredient(id);
    //        assertEquals("Steamed Broccoli", updatedIngredient
    //        .getDescription());
    //        assertEquals(4.0f, resultIngredient.getAmount(), 0.01);
    //        assertEquals("lb", resultIngredient.getUnit());
    //        assertEquals(newBestBefore, resultIngredient.getBestBefore());
    //        assertEquals("Cooked Vegetable", resultIngredient.getCategory());
    //        assertEquals("Fridge", resultIngredient.getLocation());
    //
    //        // change location
    //        updatedIngredient.setLocation("Freezer");
    //        storageIngredientDB.updateStoredIngredient(id, updatedIngredient);
    //        resultIngredient = storageIngredientDB.getStoredIngredient(id);
    //        assertEquals("Steamed Broccoli", updatedIngredient
    //        .getDescription());
    //        assertEquals(4.0f, resultIngredient.getAmount(), 0.01);
    //        assertEquals("lb", resultIngredient.getUnit());
    //        assertEquals(newBestBefore, resultIngredient.getBestBefore());
    //        assertEquals("Cooked Vegetable", resultIngredient.getCategory());
    //        assertEquals("Freezer", resultIngredient.getLocation());
    //
    //        storageIngredientDB.removeFromIngredients(id);
    //    }
}
