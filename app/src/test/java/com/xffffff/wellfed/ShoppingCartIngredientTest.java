package com.xffffff.wellfed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.xffffff.wellfed.ingredient.Ingredient;
import com.xffffff.wellfed.shoppingcart.ShoppingCartIngredient;

import org.junit.Test;

/**
 * Tests ShoppingCartIngredient methods.
 * Since ShoppingCartIngredient extends Ingredient, I did not test those inherited methods.
 */
public class ShoppingCartIngredientTest {
    /**
     * Creates a mock ShoppingCartIngredient to use in the tests.
     */
    private ShoppingCartIngredient mockShoppingCartIngredient() {
        return new ShoppingCartIngredient("Pizza");
    }

    /**
     * Tests the first constructor.
     */
    @Test
    public void testShoppingCartIngredientConstructor1() {
        ShoppingCartIngredient mock = new ShoppingCartIngredient("Eggs");

        assertNull(mock.getUnit());
        assertNull(mock.getAmount());
        assertNull(mock.getCategory());
        assertNull(mock.getId());
        assertFalse(mock.isComplete());
        assertFalse(mock.isPickedUp());
        assertEquals("Eggs", mock.getDescription());
    }

    /**
     * Tests the second constructor.
     */
    @Test
    public void testShoppingCartIngredientConstructor2() {
        Ingredient mockIngredient = new Ingredient();
        ShoppingCartIngredient mock = new ShoppingCartIngredient(mockIngredient);

        assertNull(mock.getUnit());
        assertNull(mock.getAmount());
        assertNull(mock.getCategory());
        assertNull(mock.getId());
        assertFalse(mock.isComplete());
        assertFalse(mock.isPickedUp());
        assertNull(mock.getDescription());

        mockIngredient = new Ingredient("Eggs");
        mock = new ShoppingCartIngredient(mockIngredient);

        assertNull(mock.getUnit());
        assertNull(mock.getAmount());
        assertNull(mock.getCategory());
        assertNull(mock.getId());
        assertFalse(mock.isComplete());
        assertFalse(mock.isPickedUp());
        assertEquals("Eggs", mock.getDescription());
    }

    /**
     * Tests getting and setting picked up status
     */
    @Test
    public void testPickedUpStatus() {
        ShoppingCartIngredient mock = mockShoppingCartIngredient();

        mock.setPickedUp(false);
        assertFalse(mock.isPickedUp());

        mock.setPickedUp(true);
        assertTrue(mock.isPickedUp());
    }

    /**
     * Tests getting and setting completion status
     */
    @Test
    public void testCompletionStatus() {
        ShoppingCartIngredient mock = mockShoppingCartIngredient();

        mock.setComplete(false);
        assertFalse(mock.isComplete());

        mock.setComplete(true);
        assertTrue(mock.isComplete());
    }
}
