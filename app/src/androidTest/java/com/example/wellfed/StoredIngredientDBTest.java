package com.example.wellfed;

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

    @Test
    public void testAddFull() throws InterruptedException {
        StoredIngredient storedIngredient = new StoredIngredient("Broccoli");
        storedIngredient.setCategory("Vegetable");
        Date bestBefore = new Date(2022, 10, 1);
        storedIngredient.setBestBefore(bestBefore);
        storedIngredient.setLocation("Fridge");
        storedIngredient.setAmount(5);
        storedIngredient.setUnit("kg");

        storedIngredientDB.addStoredIngredient(storedIngredient);

    }
}
