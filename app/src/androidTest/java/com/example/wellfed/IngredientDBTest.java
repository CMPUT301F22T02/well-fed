package com.example.wellfed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import static java.util.concurrent.TimeUnit.SECONDS;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.ingredient.IngredientDB;
import com.google.firebase.firestore.DocumentReference;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

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
    Ingredient mockNonExistingIngredient;

    private void assertIngredientsEqual(Ingredient resultIngredient, Ingredient expectedIngredient){
        assertNotNull(resultIngredient);
        assertEquals(resultIngredient.getId(), expectedIngredient.getId());
        assertEquals(resultIngredient.getDescription(), expectedIngredient.getDescription());
        assertEquals(resultIngredient.getCategory(), expectedIngredient.getCategory());
    }

    private void removeIngredient(Ingredient mockIngredient) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        AtomicReference<Boolean> successAtomic = new AtomicReference<>();
        ingredientDB.deleteIngredient(mockIngredient,
                (deletedIngredient, success) -> {
            successAtomic.set(success);
            latch.countDown();
        });

        if(!latch.await(TIMEOUT, SECONDS)){
            throw new InterruptedException();
        }

        assertTrue(successAtomic.get());
    }

    /**
     * Creates a new IngredientDB object and mock ingredients.
     */
    @Before
    public void before() {
        MockDBConnection connection = new MockDBConnection();
        ingredientDB = new IngredientDB(connection);
        mockIngredient = new Ingredient("Broccoli");
        mockIngredient.setCategory("Vegetable");
        mockIngredient.setUnit("kg");
        mockIngredient.setAmount(5.0);
        mockNonExistingIngredient = new Ingredient("Broccoli");
        mockNonExistingIngredient.setCategory("Vegetable");
        mockNonExistingIngredient.setUnit("kg");
        mockNonExistingIngredient.setAmount(5.0);
        mockNonExistingIngredient.setId("-1");
    }

    /**
     * Tests addIngredient and getIngredient on DB with a complete
     * ingredient, checks if the ingredient is added to DB and retrieved from
     * DB.
     *
     * @throws InterruptedException if the test times out
     */
    @Test
    public void testAddIngredient() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        AtomicReference<Boolean> successAtomic = new AtomicReference<>();
        AtomicReference<Ingredient> addedIngredientAtomic = new AtomicReference<>();
        ingredientDB.addIngredient(mockIngredient, (addedIngredient, success) -> {
            successAtomic.set(success);
            addedIngredientAtomic.set(addedIngredient);
            latch.countDown();
        });

        if (!latch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        assertTrue(successAtomic.get());
        assertIngredientsEqual(addedIngredientAtomic.get(), mockIngredient);

        removeIngredient(mockIngredient);
    }

    /**
     * Tests deleteIngredient on DB, checks if the ingredient is deleted.
     *
     * @throws InterruptedException if the test times out
     */
    @Test
    public void testDeleteIngredient() throws InterruptedException {
        CountDownLatch addLatch = new CountDownLatch(1);
        CountDownLatch deleteLatch = new CountDownLatch(1);

        AtomicReference<Boolean> successAtomic = new AtomicReference<>();
        ingredientDB.addIngredient(mockIngredient,
                (addIngredient, success) -> {
            successAtomic.set(success);
            addLatch.countDown();
        });

        if (!addLatch.await(TIMEOUT, SECONDS)) {
            throw new InterruptedException();
        }

        assertTrue(successAtomic.get());

        AtomicReference<Ingredient> deletedIngredientAtomic = new AtomicReference<>();
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
        assertIngredientsEqual(deletedIngredientAtomic.get(), mockIngredient);
    }

    @Test
    public void testGetIngredient() throws InterruptedException {
        CountDownLatch addLatch = new CountDownLatch(1);
        CountDownLatch getLatch = new CountDownLatch(1);

        AtomicReference<Boolean> successAtomic = new AtomicReference<>();
        ingredientDB.addIngredient(mockIngredient,
                (addedIngredient, success) ->{
            successAtomic.set(success);
            addLatch.countDown();
        });

        if(!addLatch.await(TIMEOUT, SECONDS)){
            throw new InterruptedException();
        }

        assertTrue(successAtomic.get());

        AtomicReference<Ingredient> foundIngredientAtomic = new AtomicReference<>();
        ingredientDB.getIngredient(mockIngredient.getId(),
                (foundIngredient, success) ->{
            successAtomic.set(success);
            foundIngredientAtomic.set(foundIngredient);
            getLatch.countDown();
        });

        if(!getLatch.await(TIMEOUT, SECONDS)){
            throw new InterruptedException();
        }

        assertTrue(successAtomic.get());
        assertIngredientsEqual(foundIngredientAtomic.get(), mockIngredient);

        removeIngredient(mockIngredient);
    }

    /**
     * Tests whether null is returned upon getting an invalid
     * ingredient.
     *
     * @throws InterruptedException if the test times out
     */
    @Test
    public void testGetNonExistentIngredient() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        AtomicReference<Boolean> successAtomic = new AtomicReference<>();
        AtomicReference<Ingredient> foundIngredientAtomic = new AtomicReference<>();
        ingredientDB.getIngredient(mockNonExistingIngredient, (foundIngredient, success) -> {
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

    @Test
    public void testGetIngredientByDocumentReference() throws InterruptedException {
        CountDownLatch addLatch = new CountDownLatch(1);
        CountDownLatch getLatch = new CountDownLatch(1);

        //add
        AtomicReference<Boolean> successAtomic = new AtomicReference<>();
        ingredientDB.addIngredient(mockIngredient,
                (addedIngredient, success) -> {
            successAtomic.set(success);
            addLatch.countDown();
        });

        if(!addLatch.await(TIMEOUT, SECONDS)){
            throw new InterruptedException();
        }

        assertTrue(successAtomic.get());
        //get document reference
        DocumentReference mockIngredientDocumentReference =  ingredientDB.getDocumentReference(mockIngredient);
        //get ingredient

        AtomicReference<Ingredient> foundIngredientAtomic = new AtomicReference<>();
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
        assertIngredientsEqual(foundIngredientAtomic.get(), mockIngredient);

        //deleteIngredient
        removeIngredient(mockIngredient);
    }

    @Test
    public void testGetNonExistentIngredientByDocumentReference() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        DocumentReference mockNonExistentIngredientDocumentReference =
                ingredientDB.getDocumentReference(mockNonExistingIngredient);

        AtomicReference<Boolean> successAtomic = new AtomicReference<>();
        AtomicReference<Ingredient> foundIngredientAtomic = new AtomicReference<>();
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



