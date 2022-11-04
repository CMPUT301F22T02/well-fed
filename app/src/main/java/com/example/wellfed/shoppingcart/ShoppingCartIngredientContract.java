package com.example.wellfed.shoppingcart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ShoppingCartIngredientContract extends ActivityResultContract<ShoppingCartIngredient,
        Pair<String, ShoppingCartIngredient>> {
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context,
                               @Nullable ShoppingCartIngredient shoppingCartIngredient) {
        Intent intent = new Intent(context, ShoppingCartEditActivity.class);
        intent.putExtra("shoppingCartIngredient", shoppingCartIngredient);
        return intent;
    }

    @Override
    public Pair<String, ShoppingCartIngredient> parseResult(int i, Intent intent) {
        switch(i) {
            case Activity.RESULT_OK:
                if (intent == null) {
                    return null;
                }
                ShoppingCartIngredient shoppingCartIngredient = (ShoppingCartIngredient) intent.getSerializableExtra(
                        "shoppingCartIngredient");
                String type = intent.getStringExtra("type");
                return new Pair<>(type, shoppingCartIngredient);
            case Activity.RESULT_CANCELED:
                return new Pair<>("quit", null);
            default:
                return null;
        }
    }
}
