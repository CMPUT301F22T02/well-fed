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
        ingredient.setAmount((float) 3);
        ingredient.setCategory("Fruit");
        ingredient.setComplete(false);
        ingredient.setPickedUp(false);

        ShoppingCartIngredient ingredient2 = new ShoppingCartIngredient("Apple");
        ingredient2.setUnit("apple(s)");
        ingredient2.setAmount((float) 2);
        ingredient2.setCategory("Fruit");
        ingredient2.setComplete(false);
        ingredient2.setPickedUp(false);

        ShoppingCartIngredient ingredient3 = new ShoppingCartIngredient("Salt");
        ingredient3.setUnit("g");
        ingredient3.setAmount((float) 100.35);
        ingredient3.setCategory("seasoning");
        ingredient3.setComplete(false);
        ingredient3.setPickedUp(false);

        ShoppingCartIngredient ingredient4 = new ShoppingCartIngredient("Pie");
        ingredient4.setUnit("pie(s)");
        ingredient4.setAmount((float) 3.4);
        ingredient4.setCategory("Food");
        ingredient4.setComplete(false);
        ingredient4.setPickedUp(false);

        // add each ingredient to arraylist
        shoppingCartIngredients.add(ingredient);
        shoppingCartIngredients.add(ingredient2);
        shoppingCartIngredients.add(ingredient3);
        shoppingCartIngredients.add(ingredient4);
    }

    public void addShoppingCartIngredient(ShoppingCartIngredient shoppingCartIngredient) {
        this.shoppingCartIngredients.add(shoppingCartIngredient);
        this.adapter.notifyItemInserted(this.shoppingCartIngredients.size() - 1);
    }

    public void editShoppingCartIngredient(int index, ShoppingCartIngredient modifiedShoppingCartIngredient) {
        this.shoppingCartIngredients.set(index, modifiedShoppingCartIngredient);
        this.adapter.notifyItemChanged(index);
    }
}
