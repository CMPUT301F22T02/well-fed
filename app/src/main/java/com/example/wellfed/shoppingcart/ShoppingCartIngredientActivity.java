package com.example.wellfed.shoppingcart;

import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.R;
import com.example.wellfed.common.ConfirmDialog;

public class ShoppingCartIngredientActivity extends ActivityBase implements
        ConfirmDialog.OnConfirmListener {
    /**
     * The ShoppingCartIngredientContract is the contract for the ingredient.
     */
    private ShoppingCartIngredientContract contract;

    /**
     * The ShoppingCartIngredient object for the ingredient.
     */
    private ShoppingCartIngredient ingredient;

    /**
     * The ShoppingCartIngredientController is the controller for the ingredient.
     */
    private ShoppingCartIngredientController controller;

    /**
     * ActivityResultLauncher for the ShoppingCartIngredientEditActivity to edit an ingredient.
     */
    private final ActivityResultLauncher<ShoppingCartIngredient> editShoppingCartIngredient
            = registerForActivityResult(new ShoppingCartIngredientEditContract(),
            result -> {
                String type = result.first;
                ShoppingCartIngredient ingredient = result.second;
                switch(type) {

                }
            });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_shopping_cart_add);
    }
}
