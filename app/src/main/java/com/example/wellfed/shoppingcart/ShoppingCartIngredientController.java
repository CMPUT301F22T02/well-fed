package com.example.wellfed.shoppingcart;

import java.util.ArrayList;

public class ShoppingCartIngredientController {
    private ArrayList<ShoppingCartIngredient> ingredients;

    private ShoppingCartIngredientAdapter adapter;

    public ArrayList<ShoppingCartIngredient> getIngredients() {
        return ingredients;
    }

    public void setAdapter(ShoppingCartIngredientAdapter adapter) {
        this.adapter = adapter;
    }

    public ShoppingCartIngredientController() {
        ingredients = new ArrayList<>();
        // Demo data
        ShoppingCartIngredient ingredient1 = new ShoppingCartIngredient("Banana");
        ingredient1.setAmount((float)1.0);
        ingredient1.setUnit("lb(s)");
        ingredient1.setCategory("fruit");

        ShoppingCartIngredient ingredient2 = new ShoppingCartIngredient("Salt");
        ingredient2.setAmount((float)50.0);
        ingredient2.setUnit("gram(s)");
        ingredient2.setCategory("seasoning");

        ShoppingCartIngredient ingredient3 = new ShoppingCartIngredient("Milk");
        ingredient3.setAmount((float)4.5);
        ingredient3.setUnit("L(s)");
        ingredient3.setCategory("drinks");

        ingredients.add(ingredient1);
        ingredients.add(ingredient2);
        ingredients.add(ingredient3);
    }
}
