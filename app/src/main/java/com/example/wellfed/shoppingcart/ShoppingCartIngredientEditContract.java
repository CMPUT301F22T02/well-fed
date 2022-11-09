package com.example.wellfed.shoppingcart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wellfed.ingredient.IngredientActivity;
import com.example.wellfed.ingredient.IngredientEditActivity;

public class ShoppingCartIngredientEditContract extends ActivityResultContract<ShoppingCartIngredient, Pair<String, ShoppingCartIngredient>> {
    /**
     * Creates an Intent for the ShoppingCartIngredientEditActivity.
     * @param context Context object for the activity.
     * @param shoppingCartIngredient ShoppingCartIngredient object for the ingredient.
     * @return Intent object for the IngredientEditActivity.
     */
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, ShoppingCartIngredientContract shoppingCartIngredient) {
        Intent intent = new Intent(context, ShoppingCartIngredientEditActivity.class);
        intent.putExtra("ingredient", shoppingCartIngredient);
        return intent;
    }

    /**
     * Pair object for the result of the shoppingCartIngredientEditActivity.
     * @param i index of the ingredient.
     * @param intent Intent object for the ShoppingCartIngredientEditActivity.
     * @return Pair object for the result of the ShoppingCartIngredientEditActivity.
     */
    @Override
    public Pair<String, ShoppingCartIngredient> parseResult(int i, @Nullable Intent intent) {
        if (i != Activity.RESULT_OK || intent == null) {
            return null;
        }
        String type = intent.getStringExtra("type");
        ShoppingCartIngredient ingredient = (ShoppingCartIngredient) intent.getSerializableExtra("ingredient");
        return new Pair<>(type, ingredient);
    }
}
