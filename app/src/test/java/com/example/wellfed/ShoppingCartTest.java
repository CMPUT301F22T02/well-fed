package com.example.wellfed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.mealplan.MealPlan;
import com.example.wellfed.shoppingcart.ShoppingCart;
import com.example.wellfed.shoppingcart.ShoppingCartIngredient;

import org.junit.Test;

import java.util.List;

public class ShoppingCartTest {
    private ShoppingCart mockShoppingCart() {
        return new ShoppingCart();
    }
    private ShoppingCartIngredient mockShoppingCartIngredient(){return new ShoppingCartIngredient("test");}

    /**
     * Tests adding of an Ingredient to the ShoppingCart.
     */
    @Test
    public void testAddAndGetIngredient() {
        ShoppingCart shoppingCart = mockShoppingCart();
        ShoppingCartIngredient ingredient1 = mockShoppingCartIngredient();
        ShoppingCartIngredient ingredient2 = mockShoppingCartIngredient();
        ingredient1.setDescription("hello");
        ingredient2.setDescription("hello2");

        assertThrows(IndexOutOfBoundsException.class, () -> {
            shoppingCart.getIngredient(0);
        });
        shoppingCart.addIngredient(ingredient1);
        assertEquals(ingredient1, shoppingCart.getIngredient(0));

        shoppingCart.addIngredient(ingredient2);
        assertEquals(ingredient2, shoppingCart.getIngredient(1));
    }

    /**
     * Tests deleting of an Ingredient from the ShoppingCart.
     */
    @Test
    public void testRemoveIngredient() {
        ShoppingCart shoppingCart = mockShoppingCart();
        ShoppingCartIngredient ingredient1 = mockShoppingCartIngredient();
        ShoppingCartIngredient ingredient2 = mockShoppingCartIngredient();
        ingredient1.setDescription("hello");
        ingredient2.setDescription("hello2");

        shoppingCart.addIngredient(ingredient1);
        shoppingCart.addIngredient(ingredient2);

        shoppingCart.removeIngredient(ingredient1);
        assertEquals(ingredient2, shoppingCart.getIngredient(0));
        assertThrows(IndexOutOfBoundsException.class, () -> {
            shoppingCart.getIngredient(1);
        });

        shoppingCart.removeIngredient(ingredient2);
        assertThrows(IndexOutOfBoundsException.class, () -> {
            shoppingCart.getIngredient(0);
        });
    }

    /**
     * Tests the behaviour of removing an ingredient that is not in the ShoppingCart.
     */
    @Test
    public void testRemoveIngredientNotInMealPlan() {
        ShoppingCart shoppingCart = mockShoppingCart();
        ShoppingCartIngredient ingredient = mockShoppingCartIngredient();
        ShoppingCartIngredient ingredient2 = mockShoppingCartIngredient();
        // no error should be thrown, as deleting a non existing ingredient is fine
        shoppingCart.removeIngredient(ingredient);

        // a removed ingredient not in recipe should not affect other ingredients
        shoppingCart.addIngredient(ingredient);
        shoppingCart.removeIngredient(ingredient2);
        assertEquals(ingredient, shoppingCart.getIngredient(0));
    }

    /**
     * Tests getting the List of Ingredients from a Shopping Cart.
     */
    @Test
    public void testGetIngredients() {
        ShoppingCart shoppingCart = mockShoppingCart();
        ShoppingCartIngredient ingredient = mockShoppingCartIngredient();
        ShoppingCartIngredient ingredient2 = mockShoppingCartIngredient();

        shoppingCart.addIngredient(ingredient);
        shoppingCart.addIngredient(ingredient2);
        List<ShoppingCartIngredient> ingredientList = shoppingCart.getIngredients();

        assertEquals(ingredient, ingredientList.get(0));
        assertEquals(ingredient2, ingredientList.get(1));
    }
}
