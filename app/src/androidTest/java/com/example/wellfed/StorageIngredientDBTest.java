package com.example.wellfed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import static java.util.concurrent.TimeUnit.SECONDS;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.wellfed.ingredient.StorageIngredient;
import com.example.wellfed.ingredient.StorageIngredientDB;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.concurrent.CountDownLatch;

// TODO: javadoc
@RunWith(AndroidJUnit4.class) public class StorageIngredientDBTest {
    /**
     * Dictates how long until a database function timeout and throws an error
     */
    private static final long TIMEOUT = 5;
    /**
     * Holds an instance of the StorageIngredientDB.
     */
    StorageIngredientDB storageIngredientDB;
    /**
     * An instance of StorageIngredient we use for testing the StorageIngredientDB
     */
    StorageIngredient mockStorageIngredient;
    /**
     * An instance of StorageIngredient that we use for testing the StorageIngredientDB on
     * ingredients we do not add to the StorageIngredientDB.
     */
    StorageIngredient mockNonExistentStorageIngredient;

    /**
     * For removing a StorageIngredient we have added to the database during testing.
     *
     * @param mockStorageIngredient The StorageIngredient that is to be removed
     * @throws InterruptedException Thrown when deleting from StorageIngredientDB fails.
     */
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
        mockNonExistentStorageIngredient =
                new StorageIngredient("Broccoli", 5.0, "kg", "Fridge",
                        new Date(), "Vegetable");
        mockNonExistentStorageIngredient.setStorageId("-1");
    }

    /**
     * Tests the add functionality of the StorageIngredientDB. The mockStorageIngredient
     * object is used to test addStorageIngredient method in StorageIngredientDB. The call to
     * addStorageIngredient is expected to succeed for this test to pass.
     *
     * @throws InterruptedException Thrown when adding to StorageIngredientDB
     * or deleting from StorageIngredientDB fails.
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

        if (!latch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        removeStorageIngredient(mockStorageIngredient);
    }

    /**
     * Tests the get functionality of the StorageIngredientDB. The mockStorageIngredient object is
     * used to test getStorageIngredient method in StorageIngredientDB. The call to
     * getStorageIngredient is expected to succeed for this test to pass.
     *
     * @throws InterruptedException Thrown when adding to, getting from
     * or deleting from StorageIngredientDB fail. Deleting is done via removeStorageIngredient method
     */
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

    /**
     * Tests the get functionality of the StorageIngredientDB. The mockNonExistentStorageIngredient
     * object is used to test getStorageIngredient method in StorageIngredientDB. The call to
     * getStorageIngredient is expected to fail for this test to succeed.
     *
     * @throws InterruptedException Thrown when getting from StorageIngredientDB fails.
     */
    @Test
    public void testGetNonExistenceStorageIngredient() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        storageIngredientDB.getStorageIngredient(mockNonExistentStorageIngredient.getStorageId(),
                (foundStorageIngredient, success) ->{
                    assertFalse(success);
                    latch.countDown();
                });

        if(!latch.await(TIMEOUT, SECONDS)){
            throw new InterruptedException();
        }
    }

    /**
     * Tests the delete functionality of the StorageIngredientDB. The mockStorageIngredient object
     * is used to test the deleteStorageIngredient method in StorageIngredientDB. The call to
     * deleteStorageIngredient is expected to succeed for this test to pass.
     *
     * @throws InterruptedException Thrown when adding to, or deleting from StorageIngredientDB
     * fails
     */
    @Test public void testDeleteStorageIngredient() throws InterruptedException {
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

    /**
     * Tests the update functionality of the StorageIngredientDB. The mockStorageIngredient object
     * is used to test the updateStorageIngredient method in StorageIngredientDB. The call to
     * updateStorageIngredient is expected to succeed for this test to pass.
     *
     * @throws InterruptedException Thrown when adding to, updating in, or deleting from
     * StorageIngredientDB fails
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

    /**
     * Tests the update functionality of the StorageIngredientDB. The
     * mockNonExistentStorageIngredient object is used to test the updateStorageIngredient method in
     * StorageIngredientDB. The call to updateStorageIngredient is expect to fail for this test to
     * succeed.
     *
     * @throws InterruptedException Thrown when updating in StorageIngredientDB fails
     */
    @Test
    public void testUpdateNonExistentStorageIngredient() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        storageIngredientDB.updateStorageIngredient(mockNonExistentStorageIngredient,
                (updatedStorageIngredient, success) ->{
                    assertFalse(success);
                    latch.countDown();
                });

        if(!latch.await(TIMEOUT, SECONDS)){
            throw new InterruptedException();
        }
    }
}
