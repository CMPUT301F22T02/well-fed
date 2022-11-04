package com.example.wellfed.shoppingcart;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCartController {
    private ArrayList<ShoppingCartIngredient> shoppingCartIngredients;

    private ShoppingCartIngredientAdapter adapter;

    public ArrayList<ShoppingCartIngredient> getShoppingCartIngredients() { return shoppingCartIngredients; }

    public void setAdapter(ShoppingCartIngredientAdapter adapter) { this.adapter = adapter; }

    public ShoppingCartController() {
        shoppingCartIngredients = new ArrayList<>();
        // TODO: REMOVE THIS DEMO DATA
        // Initialize mock data
        ShoppingCartIngredient ingredient = new ShoppingCartIngredient("Banana");
        ingredient.setUnit("banana(s)");
        ingredient.setAmount((float)1);
        ingredient.setCategory("Fruit");
        ingredient.setComplete(false);
        ingredient.setPickedUp(false);

        ShoppingCartIngredient ingredient2 = new ShoppingCartIngredient("Apple");
        ingredient.setUnit("apple(s)");
        ingredient.setAmount((float)2);
        ingredient.setCategory("Fruit");
        ingredient.setComplete(false);
        ingredient.setPickedUp(false);

        ShoppingCartIngredient ingredient3 = new ShoppingCartIngredient("Salt");
        ingredient.setUnit("g");
        ingredient.setAmount((float)100.35);
        ingredient.setCategory("seasoning");
        ingredient.setComplete(false);
        ingredient.setPickedUp(false);

        ShoppingCartIngredient ingredient4 = new ShoppingCartIngredient("Pie");
        ingredient.setUnit("pie(s)");
        ingredient.setAmount((float)3);
        ingredient.setCategory("Food");
        ingredient.setComplete(false);
        ingredient.setPickedUp(false);

        // add each ingredient to arraylist
        shoppingCartIngredients.add(ingredient);
        shoppingCartIngredients.add(ingredient2);
        shoppingCartIngredients.add(ingredient3);
        shoppingCartIngredients.add(ingredient4);
    }
}
