package com.xffffff.wellfed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.xffffff.wellfed.ingredient.Ingredient;

import org.junit.Test;

/**
 * Unit tests for the Ingredient model class.
 */
public class IngredientTest {
    /**
     * Creates a mock ingredient to be used in the tests, with no title.
     * @return the mock ingredient created
     */
    public Ingredient mockIngredient() {
        return new Ingredient();
    }

    /**
     * Creates a mock ingredient to be used in the tests, with a title.
     * @return the mock ingredient created
     */
    public Ingredient mockIngredientTitled() {
        return new Ingredient("Apple");
    }

    /**
     * Tests whether a new Ingredient has the correctly initialized fields.
     */
    @Test
    public void testNullFields() {
        Ingredient mock = mockIngredient();
        assertNull(mock.getAmount());
        assertNull(mock.getCategory());
        assertNull(mock.getDescription());
        assertNull(mock.getUnit());
    }

    /**
     * Tests the set and get functionality of the description.
     */
    @Test
    public void testDescription() {
        Ingredient mock = mockIngredient();
        assertNull(mock.getDescription());

        // does it work with String objects?
        String testString = "Orange";
        mock.setDescription(testString);
        assertEquals(testString, mock.getDescription());

        // does it work with string literals?
        mock.setDescription("Pear");
        assertEquals("Pear", mock.getDescription());

        // does it work when mockIngredient has a string already in it?
        Ingredient mock2 = mockIngredientTitled();
        assertNotNull(mock2.getDescription());

        // does it work with String objects?
        mock.setDescription(testString);
        assertEquals(testString, mock.getDescription());

        // does it work with string literals?
        mock.setDescription("Pear");
        assertEquals("Pear", mock.getDescription());
    }

    /**
     * Tests the set and get functionality of the category.
     */
    @Test
    public void testCategory() {
        Ingredient mock = mockIngredient();
        assertNull(mock.getCategory());

        // does it work with String objects?
        String testString = "Fruit";
        mock.setCategory(testString);
        assertEquals(testString, mock.getCategory());

        // does it work with string literals?
        mock.setCategory("Vegetable");
        assertEquals("Vegetable", mock.getCategory());
    }

    /**
     * Tests the set and get functionality of the unit.
     */
    @Test
    public void testUnit() {
        Ingredient mock = mockIngredient();
        assertNull(mock.getUnit());

        // does it work with String objects?
        String testString = "lb";
        mock.setUnit(testString);
        assertEquals(testString, mock.getUnit());

        // does it work with string literals?
        mock.setUnit("kg");
        assertEquals("kg", mock.getUnit());
    }

    /**
     * Tests the set and get functionality of the description.
     */
    @Test
    public void testAmount() {
        Ingredient mock = mockIngredient();
        assertNull(mock.getAmount());

        // does it work with Float objects?
        Double testFloat = 5.0;
        mock.setAmount(testFloat);
        assertEquals(testFloat, mock.getAmount());
    }

    /**
     * Tests that a ingredient is always equals if all of the fields are identical.
     * This is done by changing each field individually, and then invoking equals
     */
    @Test
    public void testEquals() {
        Ingredient mock = mockIngredient();
        Ingredient mock2 = mockIngredient();
        assertTrue(mock.isEqual(mock2));
        assertTrue(mock2.isEqual(mock));

        mock.setDescription("Orange");
        mock2.setDescription("Orange");
        assertTrue(mock.isEqual(mock2));
        assertTrue(mock2.isEqual(mock));

        mock.setId("ID");
        mock2.setId("ID");
        assertTrue(mock.isEqual(mock2));
        assertTrue(mock2.isEqual(mock));

        mock.setCategory("Fruit");
        mock2.setCategory("Fruit");
        assertTrue(mock.isEqual(mock2));
        assertTrue(mock2.isEqual(mock));

        mock.setUnit("count");
        mock2.setUnit("count");
        assertTrue(mock.isEqual(mock2));
        assertTrue(mock2.isEqual(mock));

        mock.setAmount(5.0);
        mock2.setAmount(5.0);
        assertTrue(mock.isEqual(mock2));
        assertTrue(mock2.isEqual(mock));
    }

    /**
     * Tests that a ingredient is always equals if at least one of the fields is non-identical
     * This is done by changing each field individually, and then invoking equals
     */
    @Test
    public void testNotEquals() {
        Ingredient mock = mockIngredient();
        Ingredient mock2 = mockIngredient();
        assertTrue(mock.isEqual(mock2));
        assertTrue(mock2.isEqual(mock));

        mock.setDescription("Orange");
        mock2.setDescription("Apple");
        assertFalse(mock.isEqual(mock2));
        assertFalse(mock2.isEqual(mock));

        mock2.setDescription("Orange");
        mock.setId("ID");
        mock2.setId("ID2");
        assertFalse(mock.isEqual(mock2));
        assertFalse(mock2.isEqual(mock));

        mock2.setId("ID");
        mock.setCategory("Fruit");
        mock2.setCategory("Citrus");
        assertFalse(mock.isEqual(mock2));
        assertFalse(mock2.isEqual(mock));

        mock2.setCategory("Fruit");
        mock.setUnit("count");
        mock2.setUnit("fruits");
        assertFalse(mock.isEqual(mock2));
        assertFalse(mock2.isEqual(mock));

        mock.setUnit("count");
        mock.setAmount(5.0);
        mock2.setAmount(3.0);
        assertFalse(mock.isEqual(mock2));
        assertFalse(mock2.isEqual(mock));
    }
}
