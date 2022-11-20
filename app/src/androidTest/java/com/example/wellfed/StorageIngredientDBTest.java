package com.example.wellfed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import static java.util.concurrent.TimeUnit.SECONDS;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.wellfed.ingredient.StorageIngredient;
import com.example.wellfed.ingredient.StorageIngredientDB;


import org.checkerframework.checker.units.qual.C;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

// TODO: javadoc
@RunWith(AndroidJUnit4.class) public class StorageIngredientDBTest {

    private static final String TAG = "StorageIngredientDBTest";

    private static final long TIMEOUT = 5;

    StorageIngredientDB storageIngredientDB;

    StorageIngredient mockStorageIngredient;

    StorageIngredient mockNonExistentStorageIngredient;

    private void removeStorageIngredient(StorageIngredient mockStorageIngredient) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        storageIngredientDB.deleteStorageIngredient(mockStorageIngredient,(deletedStorageIngredient, success) -> {
            assertTrue(success);
            assertNotNull(deletedStorageIngredient);
            latch.countDown();
        });

        if(!latch.await(TIMEOUT, SECONDS)){
            throw new InterruptedException();
        }
    }

    @Before public void before() {
        MockDBConnection connection = new MockDBConnection();
        storageIngredientDB = new StorageIngredientDB(connection);
        mockStorageIngredient =
                new StorageIngredient("Broccoli", 5.0, "kg", "Fridge",
                        new Date(), "Vegetable");
        mockNonExistentStorageIngredient = new StorageIngredient(null);
        mockNonExistentStorageIngredient.setStorageId("-1");
    }

    /**
     * Tests the add functionality and get functionality of db,
     * with a complete ingredient.
     *
     * @throws InterruptedException
     */
    @Test public void testAddStorageIngredient() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        // testing whether it was what was inserted into db
        storageIngredientDB.addStorageIngredient(mockStorageIngredient,
                (addedStorageIngredient, addSuccess) -> {

                    assertNotNull(addedStorageIngredient);
                    assertTrue(addSuccess);
                    assertEquals(mockStorageIngredient.getId(),
                            addedStorageIngredient.getId());
                    assertEquals(mockStorageIngredient.getStorageId(),
                            addedStorageIngredient.getStorageId());
                    assertEquals(mockStorageIngredient.getDescription(),
                            addedStorageIngredient.getDescription());
                    assertEquals(mockStorageIngredient.getLocation(),
                            addedStorageIngredient.getLocation());
                    assertEquals(mockStorageIngredient.getAmount(),
                            addedStorageIngredient.getAmount());
                    assertEquals(mockStorageIngredient.getCategory(),
                            addedStorageIngredient.getCategory());
                    assertEquals(mockStorageIngredient.getBestBeforeDate(),
                            addedStorageIngredient.getBestBeforeDate());
                    assertEquals(mockStorageIngredient.getUnit(),
                            addedStorageIngredient.getUnit());
                    assertEquals(mockStorageIngredient.getAmountAndUnit(),
                            addedStorageIngredient.getAmountAndUnit());
                    latch.countDown();
                });



        if (!latch.await(2*TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        removeStorageIngredient(mockStorageIngredient);
    }

    @Test public void testGetStorageIngredient() throws InterruptedException {
        CountDownLatch addLatch = new CountDownLatch(1);
        CountDownLatch getLatch = new CountDownLatch(1);

        storageIngredientDB.addStorageIngredient(mockStorageIngredient,
                (addedStorageIngredient, addSuccess) -> {
                    assertNotNull(addedStorageIngredient);
                    assertTrue(addSuccess);
                    addLatch.countDown();
                });

        if(!addLatch.await(TIMEOUT, SECONDS)){
            throw new InterruptedException();
        }

        storageIngredientDB.getStorageIngredient(mockStorageIngredient.getStorageId(),
                (foundStorageIngredient, success) -> {
                    assertTrue(success);
                    assertNotNull(foundStorageIngredient);
                    assertEquals(foundStorageIngredient.getId(),
                            mockStorageIngredient.getId());
                    assertEquals(foundStorageIngredient.getStorageId(),
                            mockStorageIngredient.getStorageId());
                    assertEquals(foundStorageIngredient.getDescription(),
                            mockStorageIngredient.getDescription());
                    assertEquals(foundStorageIngredient.getLocation(),
                            mockStorageIngredient.getLocation());
                    assertEquals(foundStorageIngredient.getAmount(),
                            mockStorageIngredient.getAmount());
                    assertEquals(foundStorageIngredient.getAmount(),
                            mockStorageIngredient.getAmount());
                    assertEquals(foundStorageIngredient.getCategory(),
                            mockStorageIngredient.getCategory());
                    assertEquals(foundStorageIngredient.getBestBeforeDate(),
                            mockStorageIngredient.getBestBeforeDate());
                    assertEquals(foundStorageIngredient.getUnit(),
                            mockStorageIngredient.getUnit());
                    assertEquals(foundStorageIngredient.getAmountAndUnit(),
                            mockStorageIngredient.getAmountAndUnit());
                    getLatch.countDown();
                });


        if (!getLatch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        removeStorageIngredient(mockStorageIngredient);
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
        CountDownLatch addLatch = new CountDownLatch(1);
        CountDownLatch delLatch = new CountDownLatch(1);

        storageIngredientDB.addStorageIngredient(mockStorageIngredient,
                (addedStorageIngredient, success)->{
                    assertTrue(success);
                    assertNotNull(addedStorageIngredient);
                    assertEquals(addedStorageIngredient.getStorageId(),
                            mockStorageIngredient.getStorageId());
                    addLatch.countDown();
                });

        if (!addLatch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        storageIngredientDB.deleteStorageIngredient(mockStorageIngredient,
                (deletedStorageIngredient, success)-> {
                    assertTrue(success);
                    assertNotNull(deletedStorageIngredient);
                    assertEquals(mockStorageIngredient.getId(),
                            deletedStorageIngredient.getId());
                    assertEquals(mockStorageIngredient.getStorageId(),
                            deletedStorageIngredient.getStorageId());
                    assertEquals(mockStorageIngredient.getDescription(),
                            deletedStorageIngredient.getDescription());
                    assertEquals(mockStorageIngredient.getLocation(),
                            deletedStorageIngredient.getLocation());
                    assertEquals(mockStorageIngredient.getAmount(),
                            deletedStorageIngredient.getAmount());
                    assertEquals(mockStorageIngredient.getCategory(),
                            deletedStorageIngredient.getCategory());
                    assertEquals(mockStorageIngredient.getBestBeforeDate(),
                            deletedStorageIngredient.getBestBeforeDate());
                    assertEquals(mockStorageIngredient.getUnit(),
                            deletedStorageIngredient.getUnit());
                    assertEquals(mockStorageIngredient.getAmountAndUnit(),
                            deletedStorageIngredient.getAmountAndUnit());
                    delLatch.countDown();
                });

        if (!delLatch.await(TIMEOUT, SECONDS)) {
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
    @Test public void testUpdateStorageIngredient() throws InterruptedException {
        CountDownLatch addLatch = new CountDownLatch(1);
        CountDownLatch updateLatch = new CountDownLatch(1);

        storageIngredientDB.addStorageIngredient(mockStorageIngredient,
                (addedStorageIngredient, addSuccess) -> {
                    assertNotNull(addedStorageIngredient);
                    assertTrue(addSuccess);
                    addLatch.countDown();
                });

        if (!addLatch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        mockStorageIngredient.setCategory("Protein");
        mockStorageIngredient.setDescription("Chicken");
        mockStorageIngredient.setUnit("lb");
        mockStorageIngredient.setAmount(11.0);
        mockStorageIngredient.setLocation("Freezer");
        mockStorageIngredient.setBestBefore(new Date());

        storageIngredientDB.updateStorageIngredient(mockStorageIngredient,
                (updatedStorageIngredient, success) ->{
                    assertTrue(success);
                    assertNotNull(updatedStorageIngredient);

                    assertEquals(mockStorageIngredient.getId(),
                            updatedStorageIngredient.getId());
                    assertEquals(mockStorageIngredient.getStorageId(),
                            updatedStorageIngredient.getStorageId());
                    assertEquals(mockStorageIngredient.getDescription(),
                            updatedStorageIngredient.getDescription());
                    assertEquals(mockStorageIngredient.getLocation(),
                            updatedStorageIngredient.getLocation());
                    assertEquals(mockStorageIngredient.getAmount(),
                            updatedStorageIngredient.getAmount());
                    assertEquals(mockStorageIngredient.getCategory(),
                            updatedStorageIngredient.getCategory());
                    assertEquals(mockStorageIngredient.getBestBeforeDate(),
                            updatedStorageIngredient.getBestBeforeDate());
                    assertEquals(mockStorageIngredient.getUnit(),
                            updatedStorageIngredient.getUnit());
                    assertEquals(mockStorageIngredient.getAmountAndUnit(),
                            updatedStorageIngredient.getAmountAndUnit());

                    updateLatch.countDown();
                });

        if(!updateLatch.await(TIMEOUT, SECONDS)){
            throw new InterruptedException();
        }

        removeStorageIngredient(mockStorageIngredient);
    }
}
