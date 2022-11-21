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


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

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

    private void assertStorageIngredientsEqual(StorageIngredient result, StorageIngredient expected){
        assertNotNull(result);
        assertEquals(expected.getId(),
                result.getId());
        assertEquals(expected.getStorageId(),
                result.getStorageId());
        assertEquals(expected.getDescription(),
                result.getDescription());
        assertEquals(expected.getLocation(),
                result.getLocation());
        assertEquals(expected.getAmount(),
                result.getAmount());
        assertEquals(expected.getCategory(),
                result.getCategory());
        assertEquals(expected.getBestBefore(),
                result.getBestBefore());
        assertEquals(expected.getUnit(),
                result.getUnit());
        assertEquals(expected.getAmountAndUnit(),
                result.getAmountAndUnit());
    }

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
        AtomicReference<Boolean> successAtomic = new AtomicReference<>();
        AtomicReference<StorageIngredient> addedStorageIngredientAtomic = new AtomicReference<>();
        storageIngredientDB.addStorageIngredient(mockStorageIngredient,
                (addedStorageIngredient, success) -> {
                    successAtomic.set(success);
                    addedStorageIngredientAtomic.set(addedStorageIngredient);

                    latch.countDown();
                });

        if (!latch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        assertStorageIngredientsEqual(addedStorageIngredientAtomic.get(), mockStorageIngredient);

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

        AtomicReference<Boolean> successAtomic = new AtomicReference<>();
        storageIngredientDB.addStorageIngredient(mockStorageIngredient,
                (addedStorageIngredient, success) -> {
                    successAtomic.set(success);
                    addLatch.countDown();
                });

        if(!addLatch.await(TIMEOUT, SECONDS)){
            throw new InterruptedException();
        }

        assertTrue(successAtomic.get());

        AtomicReference<StorageIngredient> deletedStorageIngredientAtomic = new AtomicReference<>();
        storageIngredientDB.getStorageIngredient(mockStorageIngredient.getStorageId(),
                (foundStorageIngredient, success) -> {
                    successAtomic.set(success);
                    deletedStorageIngredientAtomic.set(foundStorageIngredient);

                    getLatch.countDown();
                });


        if (!getLatch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        assertTrue(successAtomic.get());
        assertStorageIngredientsEqual(deletedStorageIngredientAtomic.get(), mockStorageIngredient);

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

        AtomicReference<Boolean> successAtomic = new AtomicReference<>();
        AtomicReference<StorageIngredient> foundStorageIngredientAtomic = new AtomicReference<>();
        storageIngredientDB.getStorageIngredient(mockNonExistentStorageIngredient.getStorageId(),
                (foundStorageIngredient, success) ->{
                    successAtomic.set(success);
                    foundStorageIngredientAtomic.set(foundStorageIngredient);
                    latch.countDown();
                });

        if(!latch.await(TIMEOUT, SECONDS)){
            throw new InterruptedException();
        }

        assertFalse(successAtomic.get());
        assertNull(foundStorageIngredientAtomic.get());
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

        AtomicReference<Boolean> successAtomic = new AtomicReference<>();
        storageIngredientDB.addStorageIngredient(mockStorageIngredient,
                (addedStorageIngredient, success)->{
                    successAtomic.set(success);
                    addLatch.countDown();
                });

        if (!addLatch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        assertTrue(successAtomic.get());

        AtomicReference<StorageIngredient> deletedStorageIngredientAtomic = new AtomicReference<>();
        storageIngredientDB.deleteStorageIngredient(mockStorageIngredient,
                (deletedStorageIngredient, success)-> {
                    successAtomic.set(success);
                    deletedStorageIngredientAtomic.set(deletedStorageIngredient);
                    delLatch.countDown();
                });

        if (!delLatch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        assertStorageIngredientsEqual(deletedStorageIngredientAtomic.get(), mockStorageIngredient);
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

        AtomicReference<Boolean> successAtomic = new AtomicReference<>();
        storageIngredientDB.addStorageIngredient(mockStorageIngredient,
                (addedStorageIngredient, success) -> {
                    successAtomic.set(success);
                    addLatch.countDown();
                });

        if (!addLatch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        assertTrue(successAtomic.get());

        mockStorageIngredient.setCategory("Protein");
        mockStorageIngredient.setDescription("Chicken");
        mockStorageIngredient.setUnit("lb");
        mockStorageIngredient.setAmount(11.0);
        mockStorageIngredient.setLocation("Freezer");
        mockStorageIngredient.setBestBefore(new Date());

        AtomicReference<StorageIngredient> updatedStorageIngredientAtomic = new AtomicReference<>();
        storageIngredientDB.updateStorageIngredient(mockStorageIngredient,
                (updatedStorageIngredient, success) ->{
                    successAtomic.set(success);
                    updatedStorageIngredientAtomic.set(updatedStorageIngredient);
                    updateLatch.countDown();
                });

        if(!updateLatch.await(TIMEOUT, SECONDS)){
            throw new InterruptedException();
        }

        assertTrue(successAtomic.get());
        assertStorageIngredientsEqual(updatedStorageIngredientAtomic.get(), mockStorageIngredient);

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

        AtomicReference<Boolean> successAtomic = new AtomicReference<>();
        storageIngredientDB.updateStorageIngredient(mockNonExistentStorageIngredient,
                (updatedStorageIngredient, success) ->{
                    successAtomic.set(success);
                    latch.countDown();
                });

        if(!latch.await(TIMEOUT, SECONDS)){
            throw new InterruptedException();
        }

        assertFalse(successAtomic.get());
    }
}
