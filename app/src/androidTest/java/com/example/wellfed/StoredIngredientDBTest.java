package com.example.wellfed;

import static org.junit.Assert.assertEquals;

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
     * This class tests the add functionality and get functionality of db.
     * @throws InterruptedException
     */
    @Test
    public void testAddFull() throws InterruptedException {
        StoredIngredient storedIngredient = new StoredIngredient("Broccoli");
        storedIngredient.setCategory("Vegetable");
        Date bestBefore = new Date(2022, 10, 1);
        storedIngredient.setBestBefore(bestBefore);
        storedIngredient.setLocation("Fridge");
        storedIngredient.setAmount(5);
        storedIngredient.setUnit("kg");

        // testing whether it was what was inserted into db
        String id = storedIngredientDB.addStoredIngredient(storedIngredient);
        StoredIngredient resultIngredient = storedIngredientDB.getStoredIngredient(id);
        assertEquals("Broccoli", resultIngredient.getDescription());
        assertEquals("Vegetable", resultIngredient.getCategory());
        assertEquals(bestBefore, resultIngredient.getBestBefore());
        assertEquals("Fridge", resultIngredient.getLocation());
        assertEquals(5, resultIngredient.getAmount());
        assertEquals("kg", resultIngredient.getUnit());


        // removing it afterward
        storedIngredientDB.removeFromIngredients(id);
    }

    
}
