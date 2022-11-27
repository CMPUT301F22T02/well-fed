package com.xffffff.wellfed;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static java.util.concurrent.TimeUnit.SECONDS;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.firestore.DocumentReference;
import com.xffffff.wellfed.ingredient.Ingredient;
import com.xffffff.wellfed.ingredient.IngredientDB;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The IngredientDBTest class is used to test the IngredientDB class.
 *
 * Intended device: Pixel 6 Pro API 22
 */
@RunWith(AndroidJUnit4.class) public class IngredientDBTest {
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
    Ingredient mockNonExistingIngredient;

    /**
     * Remove an Ingredient we have added to the database during testing.
     *
     * @param mockIngredient The Ingredient that is to be removed
     * @throws InterruptedException Thrown when deleting from IngredientDB
     *                              fails.
     */
    private void removeIngredient(Ingredient mockIngredient)
            throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        AtomicReference<Boolean> successAtomic = new AtomicReference<>();
        ingredientDB.deleteIngredient(mockIngredient,
                (deletedIngredient, success) -> {
                    successAtomic.set(success);
                    latch.countDown();
                });

        if (!latch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        assertTrue(successAtomic.get());
    }

    /**
     * Creates a new IngredientDB object and mock ingredients.
     */
    @Before public void before() {
        MockDBConnection connection = new MockDBConnection();
        ingredientDB = new IngredientDB(connection);
        mockIngredient = new Ingredient("Broccoli");
        mockIngredient.setCategory("Vegetable");
        mockNonExistingIngredient = new Ingredient("Broccoli");
        mockNonExistingIngredient.setCategory("Vegetable");
        mockNonExistingIngredient.setUnit("kg");
        mockNonExistingIngredient.setId("-1");
    }

    /**
     * Tests the add functionality of the IngredientDB. The mockIngredient
     * object is used to test
     * addIngredient method in IngredientDB. The call to addIngredient is
     * expected to succeed for
     * this test to pass.
     *
     * @throws InterruptedException Thrown when adding to IngredientDB
     *                              or deleting from IngredientDB fails.
     */
    @Test public void testAddIngredient() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        AtomicReference<Boolean> successAtomic = new AtomicReference<>();
        AtomicReference<Ingredient> addedIngredientAtomic =
                new AtomicReference<>();
        ingredientDB.addIngredient(mockIngredient,
                (addedIngredient, success) -> {
                    successAtomic.set(success);
                    addedIngredientAtomic.set(addedIngredient);
                    latch.countDown();
                });

        if (!latch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        assertTrue(successAtomic.get());
        assertTrue(addedIngredientAtomic.get().isEqual(mockIngredient));

        removeIngredient(mockIngredient);
    }

    /**
     * Tests the delete functionality of the IngredientDB. The mockIngredient
     * object
     * is used to test the deleteIngredient method in IngredientDB. The call to
     * deleteIngredient is expected to succeed for this test to pass.
     *
     * @throws InterruptedException Thrown when adding to, or deleting from
     *                              IngredientDB
     *                              fails
     */
    @Test public void testDeleteIngredient() throws InterruptedException {
        CountDownLatch addLatch = new CountDownLatch(1);
        CountDownLatch deleteLatch = new CountDownLatch(1);

        AtomicReference<Boolean> successAtomic = new AtomicReference<>();
        ingredientDB.addIngredient(mockIngredient, (addIngredient, success) -> {
            successAtomic.set(success);
            addLatch.countDown();
        });

        if (!addLatch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        assertTrue(successAtomic.get());

        AtomicReference<Ingredient> deletedIngredientAtomic =
                new AtomicReference<>();
        ingredientDB.deleteIngredient(mockIngredient,
                (deletedIngredient, success) -> {
                    successAtomic.set(success);
                    deletedIngredientAtomic.set(deletedIngredient);
                    deleteLatch.countDown();
                });

        if (!deleteLatch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        assertTrue(successAtomic.get());
        assertTrue(deletedIngredientAtomic.get().isEqual(mockIngredient));
    }

    /**
     * Tests the get functionality of the IngredientDB. The mockIngredient
     * object is used to test
     * getIngredient method in IngredientDB. The call to getIngredient is
     * expected to succeed for
     * this test to pass.
     *
     * @throws InterruptedException Thrown when adding to, getting from
     *                              or deleting from IngredientDB fail.
     *                              Deleting is done via removeIngredient method
     */
    @Test public void testGetIngredient() throws InterruptedException {
        CountDownLatch addLatch = new CountDownLatch(1);
        CountDownLatch getLatch = new CountDownLatch(1);

        AtomicReference<Boolean> successAtomic = new AtomicReference<>();
        ingredientDB.addIngredient(mockIngredient,
                (addedIngredient, success) -> {
                    successAtomic.set(success);
                    addLatch.countDown();
                });

        if (!addLatch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        assertTrue(successAtomic.get());

        AtomicReference<Ingredient> foundIngredientAtomic =
                new AtomicReference<>();
        ingredientDB.getIngredient(mockIngredient.getId(),
                (foundIngredient, success) -> {
                    successAtomic.set(success);
                    foundIngredientAtomic.set(foundIngredient);
                    getLatch.countDown();
                });

        if (!getLatch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        assertTrue(successAtomic.get());
        assertTrue(foundIngredientAtomic.get().isEqual(mockIngredient));

        removeIngredient(mockIngredient);
    }

    /**
     * Tests the get functionality of the IngredientDB. The
     * mockNonExistentIngredient
     * object is used to test getIngredient method in IngredientDB. The call to
     * getIngredient is expected to fail for this test to succeed.
     *
     * @throws InterruptedException Thrown when getting from IngredientDB fails.
     */
    @Test public void testGetNonExistentIngredient()
            throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        AtomicReference<Boolean> successAtomic = new AtomicReference<>();
        AtomicReference<Ingredient> foundIngredientAtomic =
                new AtomicReference<>();
        ingredientDB.getIngredient(mockNonExistingIngredient,
                (foundIngredient, success) -> {
                    successAtomic.set(success);
                    foundIngredientAtomic.set(foundIngredient);
                    latch.countDown();
                });

        if (!latch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        assertFalse(successAtomic.get());
        assertNull(foundIngredientAtomic.get());
    }

    /**
     * Tests the get functionality of the IngredientDB. The mockIngredient
     * object is used to test
     * getIngredient method in IngredientDB. The call to getIngredient is
     * expected to succeed for
     * this test to pass. This Test differs from testGetIngredient as it
     * tests the getIngredient
     * method that requires a document reference
     *
     * @throws InterruptedException Thrown when adding to, getting from
     *                              or deleting from IngredientDB fail.
     *                              Deleting is done via removeIngredient method
     */
    @Test public void testGetIngredientByDocumentReference()
            throws InterruptedException {
        CountDownLatch addLatch = new CountDownLatch(1);
        CountDownLatch getLatch = new CountDownLatch(1);

        //add
        AtomicReference<Boolean> successAtomic = new AtomicReference<>();
        ingredientDB.addIngredient(mockIngredient,
                (addedIngredient, success) -> {
                    successAtomic.set(success);
                    addLatch.countDown();
                });

        if (!addLatch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        assertTrue(successAtomic.get());
        //get document reference
        DocumentReference mockIngredientDocumentReference =
                ingredientDB.getDocumentReference(mockIngredient);
        //get ingredient

        AtomicReference<Ingredient> foundIngredientAtomic =
                new AtomicReference<>();
        ingredientDB.getIngredient(mockIngredientDocumentReference,
                (foundIngredient, success) -> {
                    successAtomic.set(success);
                    foundIngredientAtomic.set(foundIngredient);
                    getLatch.countDown();
                });

        if (!getLatch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        assertTrue(successAtomic.get());
        assertTrue(foundIngredientAtomic.get().isEqual(mockIngredient));

        //deleteIngredient
        removeIngredient(mockIngredient);
    }

    /**
     * Tests the get functionality of the IngredientDB. The
     * mockNonExistentIngredient
     * object is used to test getIngredient method in IngredientDB. The call to
     * getIngredient is expected to fail for this test to succeed. This Test
     * differs from
     * testGetIngredient as it tests the getIngredient method that requires a
     * document reference
     *
     * @throws InterruptedException Thrown when getting from IngredientDB fails.
     */
    @Test public void testGetNonExistentIngredientByDocumentReference()
            throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        DocumentReference mockNonExistentIngredientDocumentReference =
                ingredientDB.getDocumentReference(mockNonExistingIngredient);

        AtomicReference<Boolean> successAtomic = new AtomicReference<>();
        AtomicReference<Ingredient> foundIngredientAtomic =
                new AtomicReference<>();
        ingredientDB.getIngredient(mockNonExistentIngredientDocumentReference,
                (foundIngredient, success) -> {
                    successAtomic.set(success);
                    foundIngredientAtomic.set(foundIngredient);
                    latch.countDown();
                });

        if (!latch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        assertFalse(successAtomic.get());
        assertNull(foundIngredientAtomic.get());
    }
}



