package com.example.wellfed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.wellfed.ingredient.StorageIngredient;
import com.example.wellfed.ingredient.StorageIngredientDB;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;


@RunWith(AndroidJUnit4.class)
public class StorageIngredientDBTest {
    StorageIngredientDB storageIngredientDB;
    @Before
    public void before() {
         storageIngredientDB = new StorageIngredientDB();
    }

    /**
     * Tests the add functionality and get functionality of db,
     * with a complete ingredient.
     * @throws InterruptedException
     */
    @Test
    public void testAddFull() throws InterruptedException {
        StorageIngredient storedIngredient = new StorageIngredient("Broccoli");
        storedIngredient.setCategory("Vegetable");
        Date bestBefore = new Date(2022, 10, 1);
        storedIngredient.setBestBefore(bestBefore);
        storedIngredient.setLocation("Fridge");
        storedIngredient.setAmount(5.0f);
        storedIngredient.setUnit("kg");

        // testing whether it was what was inserted into db
        String id = storageIngredientDB.addStoredIngredient(storedIngredient);
        StorageIngredient resultIngredient = storageIngredientDB.getStoredIngredient(id);
        assertEquals("Broccoli", resultIngredient.getDescription());
        assertEquals("Vegetable", resultIngredient.getCategory());
        assertEquals(bestBefore, resultIngredient.getBestBefore());
        assertEquals("Fridge", resultIngredient.getLocation());
        assertEquals(5.0f, resultIngredient.getAmount(), 0.01);
        assertEquals("kg", resultIngredient.getUnit());


        // removing it afterward
        storageIngredientDB.removeFromIngredients(id);
    }

    /**
     * Tests the add and get functionality, when fields are blank.
     * @throws InterruptedException
     */
    @Test
    public void testAddMissingFields() throws InterruptedException {
        StorageIngredient storedIngredient = new StorageIngredient("Broccoli");

        // testing whether it was what was inserted into db
        String id = storageIngredientDB.addStoredIngredient(storedIngredient);
        StorageIngredient resultIngredient = storageIngredientDB.getStoredIngredient(id);
        assertEquals("Broccoli", resultIngredient.getDescription());
        assertNull(resultIngredient.getCategory());
        assertNull(resultIngredient.getBestBefore());
        assertNull(resultIngredient.getLocation());
        assertNull(resultIngredient.getAmount());
        assertNull(resultIngredient.getUnit());

        // removing it afterward
        storageIngredientDB.removeFromIngredients(id);
    }

    /**
     * Tests deleting an ingredient from the database
     * @throws InterruptedException
     */
    @Test
    public void testDeleteIngredient() throws InterruptedException {
        StorageIngredient storedIngredient = new StorageIngredient("Broccoli");
        String id = storageIngredientDB.addStoredIngredient(storedIngredient);
        storageIngredientDB.removeFromIngredients(id);

        //TODO: Replace with assertThrows
        boolean present = true;
        try {
            storageIngredientDB.getStoredIngredient(id);
        } catch (IllegalArgumentException e) {
            present = false;
        }
        assertFalse(present);

    }

    /**
     * Tests deleting a non-existing ingredient.
     */
    @Test
    public void deleteNonExistingIngredient() throws InterruptedException {
        // attempting to remove non-existing ingredient from db
        // this test will succeed if no error is thrown
        storageIngredientDB.removeFromIngredients("-1");
    }

    /**
     * Tests whether an exception is thrown upon getting an invalid ingredient.
     * @throws InterruptedException
     */
    @Test
    public void getNonExistingIngredient() throws InterruptedException {
        // TODO: change this to a assertThrows()
        boolean valid = true;
        try {
            storageIngredientDB.getStoredIngredient("-1");
        } catch (IllegalArgumentException e) {
            valid = false;
        }
        assertFalse(valid);
    }

    /**
     * Tests whether all of the updated fields are reflected in the database.
     * @throws InterruptedException
     */
    @Test
    public void testUpdateIngredient() throws InterruptedException {
        StorageIngredient oldIngredient = new StorageIngredient("Broccoli");
        oldIngredient.setAmount(5.0f);
        oldIngredient.setUnit("kg");
        Date bestBefore = new Date(2022, 10, 1);
        oldIngredient.setBestBefore(bestBefore);
        oldIngredient.setCategory("Vegetable");
        oldIngredient.setLocation("Fridge");

        String id = storageIngredientDB.addStoredIngredient(oldIngredient);

        // change description to Test2
        StorageIngredient updatedIngredient = oldIngredient;
        updatedIngredient.setDescription("Steamed Broccoli");
        storageIngredientDB.updateStoredIngredient(id, updatedIngredient);
        StorageIngredient resultIngredient = storageIngredientDB.getStoredIngredient(id);
        assertEquals("Steamed Broccoli", updatedIngredient.getDescription());
        assertEquals(5.0f, resultIngredient.getAmount(), 0.01);
        assertEquals("kg", resultIngredient.getUnit());
        assertEquals(bestBefore, resultIngredient.getBestBefore());
        assertEquals("Vegetable", resultIngredient.getCategory());
        assertEquals("Fridge", resultIngredient.getLocation());

        // change amount to 4
        updatedIngredient.setAmount(4.0f);
        storageIngredientDB.updateStoredIngredient(id, updatedIngredient);
        resultIngredient = storageIngredientDB.getStoredIngredient(id);
        assertEquals("Steamed Broccoli", updatedIngredient.getDescription());
        assertEquals(4.0f, resultIngredient.getAmount(), 0.01);
        assertEquals("kg", resultIngredient.getUnit());
        assertEquals(bestBefore, resultIngredient.getBestBefore());
        assertEquals("Vegetable", resultIngredient.getCategory());
        assertEquals("Fridge", resultIngredient.getLocation());

        // change unit to Test2
        updatedIngredient.setUnit("lb");
        storageIngredientDB.updateStoredIngredient(id, updatedIngredient);
        resultIngredient = storageIngredientDB.getStoredIngredient(id);
        assertEquals("Steamed Broccoli", updatedIngredient.getDescription());
        assertEquals(4.0f, resultIngredient.getAmount(), 0.01);
        assertEquals("lb", resultIngredient.getUnit());
        assertEquals(bestBefore, resultIngredient.getBestBefore());
        assertEquals("Vegetable", resultIngredient.getCategory());
        assertEquals("Fridge", resultIngredient.getLocation());

        // change best-before to new date
        Date newBestBefore = new Date(2023, 11, 2);
        updatedIngredient.setBestBefore(newBestBefore);
        storageIngredientDB.updateStoredIngredient(id, updatedIngredient);
        resultIngredient = storageIngredientDB.getStoredIngredient(id);
        assertEquals("Steamed Broccoli", updatedIngredient.getDescription());
        assertEquals(4.0f, resultIngredient.getAmount(), 0.01);
        assertEquals("lb", resultIngredient.getUnit());
        assertEquals(newBestBefore, resultIngredient.getBestBefore());
        assertEquals("Vegetable", resultIngredient.getCategory());
        assertEquals("Fridge", resultIngredient.getLocation());

        // change category
        updatedIngredient.setCategory("Cooked Vegetable");
        storageIngredientDB.updateStoredIngredient(id, updatedIngredient);
        resultIngredient = storageIngredientDB.getStoredIngredient(id);
        assertEquals("Steamed Broccoli", updatedIngredient.getDescription());
        assertEquals(4.0f, resultIngredient.getAmount(), 0.01);
        assertEquals("lb", resultIngredient.getUnit());
        assertEquals(newBestBefore, resultIngredient.getBestBefore());
        assertEquals("Cooked Vegetable", resultIngredient.getCategory());
        assertEquals("Fridge", resultIngredient.getLocation());

        // change location
        updatedIngredient.setLocation("Freezer");
        storageIngredientDB.updateStoredIngredient(id, updatedIngredient);
        resultIngredient = storageIngredientDB.getStoredIngredient(id);
        assertEquals("Steamed Broccoli", updatedIngredient.getDescription());
        assertEquals(4.0f, resultIngredient.getAmount(), 0.01);
        assertEquals("lb", resultIngredient.getUnit());
        assertEquals(newBestBefore, resultIngredient.getBestBefore());
        assertEquals("Cooked Vegetable", resultIngredient.getCategory());
        assertEquals("Freezer", resultIngredient.getLocation());

        storageIngredientDB.removeFromIngredients(id);
    }
}
