package com.example.wellfed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import static java.util.concurrent.TimeUnit.SECONDS;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.wellfed.ingredient.StorageIngredient;
import com.example.wellfed.ingredient.StorageIngredientDB;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

// TODO: javadoc
@RunWith(AndroidJUnit4.class) public class StorageIngredientDBTest {
    private static final String TAG = "StorageIngredientDBTest";
    private static final long TIMEOUT = 5;
    StorageIngredientDB storageIngredientDB;
    StorageIngredient mockStorageIngredient;

    @Before public void before() {
        storageIngredientDB = new StorageIngredientDB(null, true);
        mockStorageIngredient =
                new StorageIngredient("Broccoli", 5.0, "kg", "Fridge",
                        new Date(), "Vegetable");
    }

    /**
     * Tests the add functionality and get functionality of db,
     * with a complete ingredient.
     *
     * @throws InterruptedException
     */
    @Test public void testAddFull() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        // testing whether it was what was inserted into db
        storageIngredientDB.addStorageIngredient(mockStorageIngredient,
                (addedStorageIngredient, addSuccess) -> {

                    assertNotNull(addedStorageIngredient);
                    assertTrue(addSuccess);
                    assertEquals(mockStorageIngredient.getDescription(),
                            addedStorageIngredient.getDescription());
                    assertEquals(mockStorageIngredient.getLocation(),
                            addedStorageIngredient.getLocation());
                    assertEquals(mockStorageIngredient.getAmount(),
                            addedStorageIngredient.getAmount());
                    storageIngredientDB.deleteStorageIngredient(
                            addedStorageIngredient,
                            (deletedStorageIngredient, deleteSuccess) -> {
                                assertNotNull(deletedStorageIngredient);
                                assertTrue(deleteSuccess);
                                latch.countDown();
                            });
                });

        if (!latch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }
    }

    @Test public void testGetStorageIngredient() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        storageIngredientDB.addStorageIngredient(mockStorageIngredient,
                (addedStorageIngredient, addSuccess) -> {
                    assertNotNull(addedStorageIngredient);
                    assertTrue(addSuccess);
                    storageIngredientDB.getStorageIngredient(
                            addedStorageIngredient.getStorageId(),
                            (foundStorageIngredient, foundSuccess) -> {
                                assertNotNull(foundStorageIngredient);
                                assertTrue(foundSuccess);
                                assertEquals(mockStorageIngredient.getAmount(),
                                        foundStorageIngredient.getAmount());
                                assertEquals(
                                        mockStorageIngredient.getDescription(),
                                        foundStorageIngredient.getDescription());
                                storageIngredientDB.deleteStorageIngredient(
                                        foundStorageIngredient,
                                        (deletedStorageIngredient,
                                         deleteSuccess) -> {
                                            assertNotNull(
                                                    deletedStorageIngredient);
                                            assertTrue(deleteSuccess);
                                            latch.countDown();
                                        });
                            });
                });

        if (!latch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }
    }

    //    /**
    //     * Tests the add and get functionality, when fields are blank.
    //     * @throws InterruptedException
    //     */
    //    @Test
    //    public void testAddMissingFields() throws InterruptedException {
    //        StorageIngredient storedIngredient = new StorageIngredient
    //        ("Broccoli");
    //
    //        // testing whether it was what was inserted into db
    //        String id = storageIngredientDB.addStoredIngredient
    //        (storedIngredient);
    //        StorageIngredient resultIngredient = storageIngredientDB
    //        .getStoredIngredient(id);
    //        assertEquals("Broccoli", resultIngredient.getDescription());
    //        assertNull(resultIngredient.getCategory());
    //        assertNull(resultIngredient.getBestBefore());
    //        assertNull(resultIngredient.getLocation());
    //        assertNull(resultIngredient.getAmount());
    //        assertNull(resultIngredient.getUnit());
    //
    //        // removing it afterward
    //        storageIngredientDB.removeFromIngredients(id);
    //    }
    //

    /**
     * Tests deleting an ingredient from the database
     */
    @Test public void testDeleteIngredient() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        storageIngredientDB.addStorageIngredient(mockStorageIngredient,
                (addedStorageIngredient, addSuccess) -> {
                    assertNotNull(addedStorageIngredient);
                    assertTrue(addSuccess);
                    storageIngredientDB.deleteStorageIngredient(
                            addedStorageIngredient,
                            (deletedStorageIngredient, deleteSuccess) -> {
                                assertNotNull(deletedStorageIngredient);
                                assertTrue(deleteSuccess);
                                latch.countDown();
                            });
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
    //    /**
    //     * Tests whether an exception is thrown upon getting an invalid
    //     ingredient.
    //     * @throws InterruptedException
    //     */
    //    @Test
    //    public void getNonExistingIngredient() throws InterruptedException {
    //        // TODO: change this to a assertThrows()
    //        boolean valid = true;
    //        try {
    //            storageIngredientDB.getStoredIngredient("-1");
    //        } catch (IllegalArgumentException e) {
    //            valid = false;
    //        }
    //        assertFalse(valid);
    //    }
    //

    /**
     * Tests whether all of the updated fields are reflected in the database.
     *
     * @throws InterruptedException
     */
    @Test public void testUpdateIngredient() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        storageIngredientDB.addStorageIngredient(mockStorageIngredient,
                (addedStorageIngredient, addSuccess) -> {
                    assertNotNull(addedStorageIngredient);
                    assertTrue(addSuccess);
                    StorageIngredient updatedStorageIngredient =
                            addedStorageIngredient;
                    updatedStorageIngredient.setDescription("Steamed Broccoli");
                    updatedStorageIngredient.setAmount(4.0);
                    updatedStorageIngredient.setUnit("lb");
                    Date newBestBefore = new Date();
                    updatedStorageIngredient.setBestBefore(newBestBefore);
                    updatedStorageIngredient.setLocation("Freezer");
                    storageIngredientDB.updateStorageIngredient(
                            updatedStorageIngredient,
                            (updatedIngredient, updateSuccess) -> {
                                assertNotNull(updatedIngredient);
                                assertTrue(updateSuccess);
                                assertEquals("Steamed Broccoli",
                                        updatedIngredient.getDescription());
                                assertEquals((Double) 4.0,
                                        updatedIngredient.getAmount());
                                assertEquals("lb", updatedIngredient.getUnit());
                                assertEquals(newBestBefore,
                                        updatedIngredient.getBestBeforeDate());
                                assertEquals("Freezer",
                                        updatedIngredient.getLocation());
                                storageIngredientDB.deleteStorageIngredient(
                                        updatedIngredient,
                                        (deletedStorageIngredient, deleteSuccess) -> {
                                            assertNotNull(
                                                    deletedStorageIngredient);
                                            assertTrue(deleteSuccess);
                                            latch.countDown();
                                        });
                            });
                });

        if (!latch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }
    }
}
