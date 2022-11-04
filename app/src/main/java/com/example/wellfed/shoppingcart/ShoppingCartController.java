package com.example.wellfed.shoppingcart;

import java.util.List;

public class ShoppingCartController {
    private static ShoppingCartController instance;
    private List<ShoppingCartIngredient> shoppingCartIngredients;
    private ShoppingCartIngredientAdapter shoppingCartIngredientAdapter;
    private ShoppingCartDB shoppingCartDB;

    public ShoppingCartController() {shoppingCartDB = new ShoppingCartDB();};

    public void setShoppingCartIngredientAdapter(ShoppingCartIngredientAdapter shoppingCartIngredientAdapter) {
        this.shoppingCartIngredientAdapter = shoppingCartIngredientAdapter;
    }

    public void setShoppingCartIngredients(List<ShoppingCartIngredient> shoppingCartIngredients) {
        this.shoppingCartIngredients = shoppingCartIngredients;
    }
}
