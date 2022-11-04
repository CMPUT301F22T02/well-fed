package com.example.wellfed;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.example.wellfed.ingredient.StorageIngredient;

import org.junit.Test;

import java.util.Date;


/**
 * Unit tests for the StorageIngredient class.
 */
public class StorageIngredientTest {
    /**
     * Tests the constructor of the StorageIngredient class.
     */
    @Test
    public void testConstructor() {
        StorageIngredient ingredient = new StorageIngredient("Test");
        assertEquals("Test", ingredient.getDescription());
        assertNull(ingredient.getAmount());
        assertNull(ingredient.getUnit());
        assertNull(ingredient.getLocation());
        assertNull(ingredient.getBestBefore());
        assertNull(ingredient.getCategory());
    }

    /**
     * Tests the constructor of the StorageIngredient class.
     */
    @Test
    public void testConstructor2() {
        StorageIngredient ingredient = new StorageIngredient("Test", 1.0f, "g", "A1", new Date(2020, 1, 1));
        assertEquals("Test", ingredient.getDescription());
        assertEquals((Float) 1.0f, ingredient.getAmount());
        assertEquals("g", ingredient.getUnit());
        assertEquals("A1", ingredient.getLocation());
        assertEquals(new Date(2020, 1, 1), ingredient.getBestBeforeDate());
        Date bestBefore = new Date(2020, 1, 1);
        String bestBeforeString = bestBefore.toString();
        String[] bestBeforeSplit = bestBeforeString.split(" ");
        bestBeforeString =
                bestBeforeSplit[0] + ", " + bestBeforeSplit[1] + " " + bestBeforeSplit[2];
        assertEquals(bestBeforeString, ingredient.getBestBefore());
        assertNull(ingredient.getCategory());
    }

    /**
     * Tests the constructor of the StorageIngredient class.
     */
    @Test
    public void testConstructor3() {
        StorageIngredient ingredient = new StorageIngredient("Test", 1.0f, "g", "A1", new Date(2020, 1, 1), "Fruit");
        assertEquals("Test", ingredient.getDescription());
        assertEquals((Float) 1.0f, ingredient.getAmount());
        assertEquals("g", ingredient.getUnit());
        assertEquals("A1", ingredient.getLocation());
        assertEquals(new Date(2020, 1, 1), ingredient.getBestBeforeDate());
        Date bestBefore = new Date(2020, 1, 1);
        String bestBeforeString = bestBefore.toString();
        String[] bestBeforeSplit = bestBeforeString.split(" ");
        bestBeforeString =
                bestBeforeSplit[0] + ", " + bestBeforeSplit[1] + " " + bestBeforeSplit[2];
        assertEquals(bestBeforeString, ingredient.getBestBefore());
        assertEquals("Fruit", ingredient.getCategory());
    }

    /**
     * Test the getters of the StorageIngredient class.
     */
    @Test
    public void testGetters() {
        StorageIngredient ingredient = new StorageIngredient("Test");
        assertEquals("Test", ingredient.getDescription());
        assertNull(ingredient.getAmount());
        assertNull(ingredient.getUnit());
        assertNull(ingredient.getLocation());
        assertNull(ingredient.getBestBefore());
        assertNull(ingredient.getCategory());

        StorageIngredient ingredient2 = new StorageIngredient("Test", 1.0f, "g", "A1", new Date(2020, 1, 1));
        assertEquals("Test", ingredient2.getDescription());
        assertEquals((Float) 1.0f, ingredient2.getAmount());
        assertEquals("g", ingredient2.getUnit());
        assertEquals("A1", ingredient2.getLocation());
        assertEquals(new Date(2020, 1, 1), ingredient2.getBestBeforeDate());
        Date bestBefore = new Date(2020, 1, 1);
        String bestBeforeString = bestBefore.toString();
        String[] bestBeforeSplit = bestBeforeString.split(" ");
        bestBeforeString =
                bestBeforeSplit[0] + ", " + bestBeforeSplit[1] + " " + bestBeforeSplit[2];
        assertEquals(bestBeforeString, ingredient2.getBestBefore());
        assertNull(ingredient2.getCategory());

        StorageIngredient ingredient3 = new StorageIngredient("Test", 1.0f, "g", "A1", new Date(2020, 1, 1), "Fruit");
        assertEquals("Test", ingredient3.getDescription());
        assertEquals((Float) 1.0f, ingredient3.getAmount());
        assertEquals("g", ingredient3.getUnit());
        assertEquals("A1", ingredient3.getLocation());

        assertEquals(new Date(2020, 1, 1), ingredient3.getBestBeforeDate());
        Date bestBefore2 = new Date(2020, 1, 1);
        String bestBeforeString2 = bestBefore2.toString();
        String[] bestBeforeSplit2 = bestBeforeString2.split(" ");
        bestBeforeString2 =
                bestBeforeSplit2[0] + ", " + bestBeforeSplit2[1] + " " + bestBeforeSplit2[2];
        assertEquals(bestBeforeString2, ingredient3.getBestBefore());
        assertEquals("Fruit", ingredient3.getCategory());
    }

    /**
     * Test the setters of the StorageIngredient class.
     */
    @Test
    public void testSetters() {
        StorageIngredient ingredient = new StorageIngredient("Test");
        ingredient.setDescription("Test2");
        assertEquals("Test2", ingredient.getDescription());
        ingredient.setAmount(1.0f);
        assertEquals((Float) 1.0f, ingredient.getAmount());
        ingredient.setUnit("g");
        assertEquals("g", ingredient.getUnit());
        ingredient.setLocation("A1");
        assertEquals("A1", ingredient.getLocation());
        ingredient.setBestBefore(new Date(2020, 1, 1));
        assertEquals(new Date(2020, 1, 1), ingredient.getBestBeforeDate());
        Date bestBefore = new Date(2020, 1, 1);
        String bestBeforeString = bestBefore.toString();
        String[] bestBeforeSplit = bestBeforeString.split(" ");
        bestBeforeString =
                bestBeforeSplit[0] + ", " + bestBeforeSplit[1] + " " + bestBeforeSplit[2];
        assertEquals(bestBeforeString, ingredient.getBestBefore());
        ingredient.setCategory("Fruit");
        assertEquals("Fruit", ingredient.getCategory());

        StorageIngredient ingredient2 = new StorageIngredient("Test", 1.0f, "g", "A1", new Date(2020, 1, 1));
        ingredient2.setDescription("Test2");
        assertEquals("Test2", ingredient2.getDescription());
        ingredient2.setAmount(1.0f);
        assertEquals((Float) 1.0f, ingredient2.getAmount());
        ingredient2.setUnit("g");
        assertEquals("g", ingredient2.getUnit());
        ingredient2.setLocation("A1");
        assertEquals("A1", ingredient2.getLocation());
        ingredient2.setBestBefore(new Date(2020, 1, 2));
        assertEquals(new Date(2020, 1, 2), ingredient2.getBestBeforeDate());
        Date bestBefore2 = new Date(2020, 1, 2);
        String bestBeforeString2 = bestBefore2.toString();
        String[] bestBeforeSplit2 = bestBeforeString2.split(" ");
        bestBeforeString2 =
                bestBeforeSplit2[0] + ", " + bestBeforeSplit2[1] + " " + bestBeforeSplit2[2];
        assertEquals(bestBeforeString2, ingredient2.getBestBefore());
        ingredient2.setCategory("Fruit");
        assertEquals("Fruit", ingredient2.getCategory());

        StorageIngredient ingredient3 = new StorageIngredient("Test", 1.0f, "g", "A1", new Date(2020, 1, 1), "Fruit");
        ingredient3.setDescription("Test2");
        assertEquals("Test2", ingredient3.getDescription());
        ingredient3.setAmount(1.0f);
        assertEquals((Float) 1.0f, ingredient3.getAmount());
        ingredient3.setUnit("g");
        assertEquals("g", ingredient3.getUnit());
        ingredient3.setLocation("A1");
        assertEquals("A1", ingredient3.getLocation());
        ingredient3.setBestBefore(new Date(2020, 1, 2));
        assertEquals(new Date(2020, 1, 2), ingredient3.getBestBeforeDate());
        Date bestBefore3 = new Date(2020, 1, 2);
        String bestBeforeString3 = bestBefore3.toString();
        String[] bestBeforeSplit3 = bestBeforeString3.split(" ");
        bestBeforeString3 =
                bestBeforeSplit3[0] + ", " + bestBeforeSplit3[1] + " " + bestBeforeSplit3[2];
        assertEquals(bestBeforeString3, ingredient3.getBestBefore());
        ingredient3.setCategory("Fruit2");
        assertEquals("Fruit2", ingredient3.getCategory());
    }

    /**
     * Test the equals method of the StorageIngredient class.
     */
    @Test
    public void testEquals() {
        StorageIngredient ingredient = new StorageIngredient("Test");
        StorageIngredient ingredient2 = new StorageIngredient("Test");
        StorageIngredient ingredient3 = new StorageIngredient("Test2");
        StorageIngredient ingredient4 = new StorageIngredient("Test", 1.0f, "g", "A1", new Date(2020, 1, 1));
        StorageIngredient ingredient5 = new StorageIngredient("Test", 1.0f, "g", "A1", new Date(2020, 1, 1));
        StorageIngredient ingredient6 = new StorageIngredient("Test", 1.0f, "g", "A1", new Date(2020, 1, 2));
        StorageIngredient ingredient7 = new StorageIngredient("Test4", 1.0f,
                "g", "A1", new Date(2020, 1, 1), "Fruit");
        StorageIngredient ingredient8 = new StorageIngredient("Test5", 1.0f,
                "g", "A1", new Date(2020, 1, 1), "Fruit");
        StorageIngredient ingredient9 = new StorageIngredient("Test7", 1.0f,
                "g", "A1", new Date(2020, 1, 1), "Fruit2");
        assertTrue(ingredient.equals(ingredient2));
        assertFalse(ingredient.equals(ingredient3));
        assertTrue(ingredient4.equals(ingredient5));
        assertTrue(ingredient4.equals(ingredient6));
        assertFalse(ingredient7.equals(ingredient8));
        assertFalse(ingredient7.equals(ingredient9));
    }
}
