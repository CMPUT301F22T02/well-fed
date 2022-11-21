package com.example.wellfed;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.ingredient.StorageIngredient;

import org.junit.Test;

import java.util.Date;


/**
 * Unit tests for the StorageIngredient class.
 */
public class StorageIngredientTest {
    /**
     * Creates a mock ingredient to be used in the tests, with no title.
     * @return the mock ingredient created
     */
    public StorageIngredient mockIngredient() {
        return new StorageIngredient("Apple");
    }

    /**
     * Tests the constructor of the StorageIngredient class.
     */
    @Test
    public void testConstructor() {
        StorageIngredient ingredient = new StorageIngredient("Apple");
        assertEquals("Apple", ingredient.getDescription());
        assertNull(ingredient.getAmount());
        assertNull(ingredient.getUnit());
        assertNull(ingredient.getLocation());
        assertNull(ingredient.getBestBeforeDate());
        assertNull(ingredient.getCategory());
    }

    /**
     * Tests the constructor of the StorageIngredient class.
     */
    @Test
    public void testConstructor2() {
        StorageIngredient ingredient = new StorageIngredient("Apple", 1.0,
                "g",
                "Pantry", new Date(2020, 1, 1));
        assertEquals("Apple", ingredient.getDescription());
        assertEquals((Double) 1.0, ingredient.getAmount());
        assertEquals("g", ingredient.getUnit());
        assertEquals("Pantry", ingredient.getLocation());
        assertEquals(new Date(2020, 1, 1), ingredient.getBestBeforeDate());
        Date bestBefore = new Date(2020, 1, 1);
        assertEquals(bestBefore, ingredient.getBestBeforeDate());
        assertNull(ingredient.getCategory());
    }

    /**
     * Tests the constructor of the StorageIngredient class.
     */
    @Test
    public void testConstructor3() {
        StorageIngredient ingredient = new StorageIngredient("Apple", 1.0,
                "g",
                "Pantry", new Date(2020, 1, 1), "Fruit");
        assertEquals("Apple", ingredient.getDescription());
        assertEquals((Double) 1.0, ingredient.getAmount());
        assertEquals("g", ingredient.getUnit());
        assertEquals("Pantry", ingredient.getLocation());
        assertEquals(new Date(2020, 1, 1), ingredient.getBestBeforeDate());
        Date bestBefore = new Date(2020, 1, 1);
        assertEquals(bestBefore, ingredient.getBestBeforeDate());
        assertEquals("Fruit", ingredient.getCategory());
    }

    /**
     * Test the getters of the StorageIngredient class.
     */
    @Test
    public void testGetters() {
        StorageIngredient ingredient = new StorageIngredient("Apple");
        assertEquals("Apple", ingredient.getDescription());
        assertNull(ingredient.getAmount());
        assertNull(ingredient.getUnit());
        assertNull(ingredient.getLocation());
        assertNull(ingredient.getBestBeforeDate());
        assertNull(ingredient.getCategory());

        StorageIngredient ingredient2 = new StorageIngredient("Apple", 1.0,
                "g",
                "Pantry", new Date(2020, 1, 1));
        assertEquals("Apple", ingredient2.getDescription());
        assertEquals((Double) 1.0, ingredient2.getAmount());
        assertEquals("g", ingredient2.getUnit());
        assertEquals("Pantry", ingredient2.getLocation());
        assertEquals(new Date(2020, 1, 1), ingredient2.getBestBeforeDate());
        Date bestBefore = new Date(2020, 1, 1);
        assertEquals(bestBefore, ingredient2.getBestBeforeDate());
        assertNull(ingredient2.getCategory());

        StorageIngredient ingredient3 = new StorageIngredient("Apple", 1.0,
                "g",
                "Pantry", new Date(2020, 1, 1), "Fruit");
        assertEquals("Apple", ingredient3.getDescription());
        assertEquals((Double) 1.0, ingredient3.getAmount());
        assertEquals("g", ingredient3.getUnit());
        assertEquals("Pantry", ingredient3.getLocation());

        assertEquals(new Date(2020, 1, 1), ingredient3.getBestBeforeDate());
        Date bestBefore2 = new Date(2020, 1, 1);
        assertEquals(bestBefore2, ingredient3.getBestBeforeDate());
        assertEquals("Fruit", ingredient3.getCategory());
    }

    /**
     * Test the setters of the StorageIngredient class.
     */
    @Test
    public void testSetters() {
        StorageIngredient ingredient = new StorageIngredient("Apple");
        ingredient.setDescription("Banana");
        assertEquals("Banana", ingredient.getDescription());
        ingredient.setAmount(1.0);
        assertEquals((Double) 1.0, ingredient.getAmount());
        ingredient.setUnit("g");
        assertEquals("g", ingredient.getUnit());
        ingredient.setLocation("Pantry");
        assertEquals("Pantry", ingredient.getLocation());
        ingredient.setBestBefore(new Date(2020, 1, 1));
        assertEquals(new Date(2020, 1, 1), ingredient.getBestBeforeDate());
        Date bestBefore = new Date(2020, 1, 1);
        assertEquals(bestBefore, ingredient.getBestBeforeDate());
        ingredient.setCategory("Fruit");
        assertEquals("Fruit", ingredient.getCategory());

        StorageIngredient ingredient2 = new StorageIngredient("Apple", 1.0,
                "g",
                "Pantry", new Date(2020, 1, 1));
        ingredient2.setDescription("Banana");
        assertEquals("Banana", ingredient2.getDescription());
        ingredient2.setAmount(1.0);
        assertEquals((Double) 1.0, ingredient2.getAmount());
        ingredient2.setUnit("g");
        assertEquals("g", ingredient2.getUnit());
        ingredient2.setLocation("Pantry");
        assertEquals("Pantry", ingredient2.getLocation());
        ingredient2.setBestBefore(new Date(2020, 1, 2));
        assertEquals(new Date(2020, 1, 2), ingredient2.getBestBeforeDate());
        Date bestBefore2 = new Date(2020, 1, 2);
        assertEquals(bestBefore2, ingredient2.getBestBeforeDate());
        ingredient2.setCategory("Fruit");
        assertEquals("Fruit", ingredient2.getCategory());

        StorageIngredient ingredient3 = new StorageIngredient("Apple", 1.0,
                "g",
                "Pantry", new Date(2020, 1, 1), "Fruit");
        ingredient3.setDescription("Banana");
        assertEquals("Banana", ingredient3.getDescription());
        ingredient3.setAmount(1.0);
        assertEquals((Double) 1.0, ingredient3.getAmount());
        ingredient3.setUnit("g");
        assertEquals("g", ingredient3.getUnit());
        ingredient3.setLocation("Pantry");
        assertEquals("Pantry", ingredient3.getLocation());
        ingredient3.setBestBefore(new Date(2020, 1, 2));
        assertEquals(new Date(2020, 1, 2), ingredient3.getBestBeforeDate());
        Date bestBefore3 = new Date(2020, 1, 2);
        assertEquals(bestBefore3, ingredient3.getBestBeforeDate());
        ingredient3.setCategory("Fruit2");
        assertEquals("Fruit2", ingredient3.getCategory());
    }

    /**
     * Tests that a ingredient is always equals if all of the fields are identical.
     * This is done by changing each field individually, and then invoking equals
     */
    @Test
    public void testEquals() {
        StorageIngredient mock = mockIngredient();
        StorageIngredient mock2 = mockIngredient();
        assertTrue(mock.equals(mock2));
        assertTrue(mock2.equals(mock));

        mock.setDescription("Orange");
        mock2.setDescription("Orange");
        assertTrue(mock.equals(mock2));
        assertTrue(mock2.equals(mock));

        mock.setId("ID");
        mock2.setId("ID");
        assertTrue(mock.equals(mock2));
        assertTrue(mock2.equals(mock));

        mock.setCategory("Fruit");
        mock2.setCategory("Fruit");
        assertTrue(mock.equals(mock2));
        assertTrue(mock2.equals(mock));

        mock.setUnit("count");
        mock2.setUnit("count");
        assertTrue(mock.equals(mock2));
        assertTrue(mock2.equals(mock));

        mock.setAmount(5.0);
        mock2.setAmount(5.0);
        assertTrue(mock.equals(mock2));
        assertTrue(mock2.equals(mock));

        mock.setLocation("Fruit bowl");
        mock2.setLocation("Fruit bowl");
        assertTrue(mock.equals(mock2));
        assertTrue(mock2.equals(mock));

        mock.setBestBefore(new Date(11, 12, 2005));
        mock2.setBestBefore(new Date(11, 12, 2005));
        assertTrue(mock.equals(mock2));
        assertTrue(mock2.equals(mock));

        mock.setStorageId("SID");
        mock2.setStorageId("SID");
        assertTrue(mock.equals(mock2));
        assertTrue(mock2.equals(mock));
    }

    /**
     * Tests that a ingredient is always equals if at least one of the fields is non-identical
     * This is done by changing each field individually, and then invoking equals
     */
    @Test
    public void testNotEquals() {
        StorageIngredient mock = mockIngredient();
        StorageIngredient mock2 = mockIngredient();
        assertTrue(mock.equals(mock2));
        assertTrue(mock2.equals(mock));

        mock.setDescription("Orange");
        mock2.setDescription("Apple");
        assertFalse(mock.equals(mock2));
        assertFalse(mock2.equals(mock));

        mock2.setDescription("Orange");
        mock.setId("ID");
        mock2.setId("ID2");
        assertFalse(mock.equals(mock2));
        assertFalse(mock2.equals(mock));

        mock2.setId("ID");
        mock.setCategory("Fruit");
        mock2.setCategory("Citrus");
        assertFalse(mock.equals(mock2));
        assertFalse(mock2.equals(mock));

        mock2.setCategory("Fruit");
        mock.setUnit("count");
        mock2.setUnit("fruits");
        assertFalse(mock.equals(mock2));
        assertFalse(mock2.equals(mock));

        mock.setUnit("count");
        mock.setAmount(5.0);
        mock2.setAmount(3.0);
        assertFalse(mock.equals(mock2));
        assertFalse(mock2.equals(mock));

        mock.setLocation("Fruit bowl");
        mock2.setLocation("Counter");
        assertFalse(mock.equals(mock2));
        assertFalse(mock2.equals(mock));

        mock2.setLocation("Fruit bowl");
        mock.setBestBefore(new Date(11, 12, 2005));
        mock2.setBestBefore(new Date(11, 12, 2006));
        assertFalse(mock.equals(mock2));
        assertFalse(mock2.equals(mock));

        mock.setBestBefore(new Date(11, 12, 2005));
        mock.setStorageId("SID");
        mock2.setStorageId("SID2");
        assertFalse(mock.equals(mock2));
        assertFalse(mock2.equals(mock));
    }
}
