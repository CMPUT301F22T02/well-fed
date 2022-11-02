package com.example.wellfed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.example.wellfed.ingredient.Ingredient;

import org.junit.Test;

import java.util.Optional;

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
        return new Ingredient("string");
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
        String testString = "test";
        mock.setDescription(testString);
        assertEquals(testString, mock.getDescription());

        // does it work with string literals?
        mock.setDescription("test2");
        assertEquals("test2", mock.getDescription());

        // does it work when mockIngredient has a string already in it?
        Ingredient mock2 = mockIngredientTitled();
        assertNotNull(mock2.getDescription());

        // does it work with String objects?
        mock.setDescription(testString);
        assertEquals(testString, mock.getDescription());

        // does it work with string literals?
        mock.setDescription("test2");
        assertEquals("test2", mock.getDescription());
    }

    /**
     * Tests the setter and getter functionality of the categories.
     */
    @Test
    public void testCategory() {
        Ingredient mock = mockIngredient();
        assertNull(mock.getCategory());

        // does it work with String objects?
        String testString = "test";
        mock.setCategory(testString);
        assertEquals(testString, mock.getCategory());

        // does it work with string literals?
        mock.setCategory("test2");
        assertEquals("test2", mock.getCategory());
    }

    @Test
    public void testUnit() {
        Ingredient mock = mockIngredient();
        assertNull(mock.getUnit());

        // does it work with String objects?
        String testString = "test";
        mock.setUnit(testString);
        assertEquals(testString, mock.getUnit());

        // does it work with string literals?
        mock.setUnit("test2");
        assertEquals("test2", mock.getUnit());
    }

    @Test
    public void testAmount() {
        Ingredient mock = mockIngredient();
        assertNull(mock.getAmount());

        // does it work with Float objects?
        Float testFloat = 5.0f;
        mock.setAmount(testFloat);
        assertEquals(testFloat, mock.getAmount());
    }
}
