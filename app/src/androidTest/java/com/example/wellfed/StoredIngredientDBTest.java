package com.example.wellfed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.wellfed.ingredient.StoredIngredient;
import com.example.wellfed.ingredient.StoredIngredientDB;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

@RunWith(AndroidJUnit4.class)
public class StoredIngredientDBTest {
    StoredIngredientDB storedIngredientDB;
    @Before
    public void before() {
         storedIngredientDB = new StoredIngredientDB();
    }

    /**
     * This method tests the add functionality and get functionality of db,
     * with a complete ingredient.
     * @throws InterruptedException
     */
    @Test
    public void testAddFull() throws InterruptedException {
        StoredIngredient storedIngredient = new StoredIngredient("Broccoli");
        storedIngredient.setCategory("Vegetable");
        Date bestBefore = new Date(2022, 10, 1);
        storedIngredient.setBestBefore(bestBefore);
        storedIngredient.setLocation("Fridge");
        storedIngredient.setAmount(5.0f);
        storedIngredient.setUnit("kg");

        // testing whether it was what was inserted into db
        String id = storedIngredientDB.addStoredIngredient(storedIngredient);
        StoredIngredient resultIngredient = storedIngredientDB.getStoredIngredient(id);
        assertEquals("Broccoli", resultIngredient.getDescription());
        assertEquals("Vegetable", resultIngredient.getCategory());
        assertEquals(bestBefore, resultIngredient.getBestBefore());
        assertEquals("Fridge", resultIngredient.getLocation());
        assertEquals(5.0f, resultIngredient.getAmount(), 0.01);
        assertEquals("kg", resultIngredient.getUnit());


        // removing it afterward
        storedIngredientDB.removeFromIngredients(id);
    }

    /**
     * This method tests the add and get functionality, when fields are blank.
     * @throws InterruptedException
     */
    @Test
    public void testAddMissingFields() throws InterruptedException {
        StoredIngredient storedIngredient = new StoredIngredient("Broccoli");

        // testing whether it was what was inserted into db
        String id = storedIngredientDB.addStoredIngredient(storedIngredient);
        StoredIngredient resultIngredient = storedIngredientDB.getStoredIngredient(id);
        assertEquals("Broccoli", resultIngredient.getDescription());
        assertNull(resultIngredient.getCategory());
        assertNull(resultIngredient.getBestBefore());
        assertNull(resultIngredient.getLocation());
        assertEquals(0.0f, resultIngredient.getAmount(), 0.01);
        assertNull(resultIngredient.getUnit());

        // removing it afterward
        storedIngredientDB.removeFromIngredients(id);
    }

    /**
     * This method tests deleting a non-existing ingredient.
     */
    @Test
    public void deleteNonExistingIngredient() {
        // attempting to remove non-existing ingredient from db
        // this test will succeed if no error is thrown
        storedIngredientDB.removeFromIngredients("-1");
    }

    /**
     * This method tests whether all of the fields are null if an ingredient does not exist.
     * @throws InterruptedException
     */
    @Test
    public void getNonExistingIngredient() throws InterruptedException {
        StoredIngredient resultIngredient = storedIngredientDB.getStoredIngredient("-1");
        assertNull(resultIngredient.getDescription());
        assertNull(resultIngredient.getCategory());
        assertNull(resultIngredient.getBestBefore());
        assertNull(resultIngredient.getLocation());
        assertEquals(0.0f, resultIngredient.getAmount(), 0.01);
        assertNull(resultIngredient.getUnit());
    }

    /**
     * This method tests whether all of the updated fields are reflected in the database.
     * @throws InterruptedException
     */
    @Test
    public void testUpdateIngredient() throws InterruptedException {
        StoredIngredient oldIngredient = new StoredIngredient("Test");
        oldIngredient.setAmount(5.0f);
        oldIngredient.setUnit("Test");
        Date bestBefore = new Date(2022, 10, 1);
        oldIngredient.setBestBefore(bestBefore);
        oldIngredient.setCategory("Test");
        oldIngredient.setLocation("Test");

        String id = storedIngredientDB.addStoredIngredient(oldIngredient);

        // change description to Test2
        StoredIngredient updatedIngredient = oldIngredient;
        updatedIngredient.setDescription("Test2");
        storedIngredientDB.updateStoredIngredient(id, updatedIngredient);
        StoredIngredient resultIngredient = storedIngredientDB.getStoredIngredient(id);
        assertEquals("Test2", updatedIngredient.getDescription());
        assertEquals(5.0f, resultIngredient.getAmount(), 0.01);
        assertEquals("Test", resultIngredient.getUnit());
        assertEquals(bestBefore, resultIngredient.getBestBefore());
        assertEquals("Test", resultIngredient.getCategory());
        assertEquals("Test", resultIngredient.getLocation());

        // change amount to 4
        updatedIngredient.setAmount(4.0f);
        storedIngredientDB.updateStoredIngredient(id, updatedIngredient);
        resultIngredient = storedIngredientDB.getStoredIngredient(id);
        assertEquals("Test2", updatedIngredient.getDescription());
        assertEquals(4.0f, resultIngredient.getAmount(), 0.01);
        assertEquals("Test", resultIngredient.getUnit());
        assertEquals(bestBefore, resultIngredient.getBestBefore());
        assertEquals("Test", resultIngredient.getCategory());
        assertEquals("Test", resultIngredient.getLocation());

        // change unit to Test2
        updatedIngredient.setUnit("Test2");
        storedIngredientDB.updateStoredIngredient(id, updatedIngredient);
        resultIngredient = storedIngredientDB.getStoredIngredient(id);
        assertEquals("Test2", updatedIngredient.getDescription());
        assertEquals(4.0f, resultIngredient.getAmount(), 0.01);
        assertEquals("Test2", resultIngredient.getUnit());
        assertEquals(bestBefore, resultIngredient.getBestBefore());
        assertEquals("Test", resultIngredient.getCategory());
        assertEquals("Test", resultIngredient.getLocation());

        // change best-before to new date
        Date newBestBefore = new Date(2023, 11, 2);
        updatedIngredient.setBestBefore(newBestBefore);
        storedIngredientDB.updateStoredIngredient(id, updatedIngredient);
        resultIngredient = storedIngredientDB.getStoredIngredient(id);
        assertEquals("Test2", updatedIngredient.getDescription());
        assertEquals(4.0f, resultIngredient.getAmount(), 0.01);
        assertEquals("Test2", resultIngredient.getUnit());
        assertEquals(newBestBefore, resultIngredient.getBestBefore());
        assertEquals("Test", resultIngredient.getCategory());
        assertEquals("Test", resultIngredient.getLocation());

        // change category
        updatedIngredient.setCategory("Test2");
        storedIngredientDB.updateStoredIngredient(id, updatedIngredient);
        resultIngredient = storedIngredientDB.getStoredIngredient(id);
        assertEquals("Test2", updatedIngredient.getDescription());
        assertEquals(4.0f, resultIngredient.getAmount(), 0.01);
        assertEquals("Test2", resultIngredient.getUnit());
        assertEquals(newBestBefore, resultIngredient.getBestBefore());
        assertEquals("Test2", resultIngredient.getCategory());
        assertEquals("Test", resultIngredient.getLocation());

        // change location
        updatedIngredient.setLocation("Test2");
        storedIngredientDB.updateStoredIngredient(id, updatedIngredient);
        resultIngredient = storedIngredientDB.getStoredIngredient(id);
        assertEquals("Test2", updatedIngredient.getDescription());
        assertEquals(4.0f, resultIngredient.getAmount(), 0.01);
        assertEquals("Test2", resultIngredient.getUnit());
        assertEquals(newBestBefore, resultIngredient.getBestBefore());
        assertEquals("Test2", resultIngredient.getCategory());
        assertEquals("Test2", resultIngredient.getLocation());

        storedIngredientDB.removeFromIngredients(id);
    }
}
