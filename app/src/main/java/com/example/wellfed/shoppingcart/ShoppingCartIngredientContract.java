package com.example.wellfed.shoppingcart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ShoppingCartIngredientContract extends ActivityResultContract<ShoppingCartIngredient, Pair<String, ShoppingCartIngredient>> {
    /**
     * Creates an Intent for the ShoppingCartIngredientActivity.
     * @param context Context object for the activity.
     * @param shoppingCartIngredient ShoppingCartIngredient object for the ingredient.
     * @return Intent object for the ShoppingCartIngredientActivity.
     */
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, ShoppingCartIngredient shoppingCartIngredient) {
        Intent intent = new Intent(context, ShoppingCartIngredientActivity.class);
        intent.putExtra("ingredient", shoppingCartIngredient);
        return intent;
    }

    /**
     * Pair object for the result of the shoppingCartIngredientActivity.
     * @param i index of the ingredient.
     * @param intent Intent object for the ShoppingCartIngredientActivity.
     * @return Pair object for the result of the ShoppingCartIngredientActivity.
     */
    @Override
    public Pair<String, ShoppingCartIngredient> parseResult(int i, @Nullable Intent intent) {
        if (i != Activity.RESULT_OK || intent == null) {
            return null;
        }
        String type = intent.getStringExtra("type");
        ShoppingCartIngredient shoppingCartIngredient = (ShoppingCartIngredient) intent.getSerializableExtra("ingredient");
        return new Pair<>(type, shoppingCartIngredient);
    }
}
