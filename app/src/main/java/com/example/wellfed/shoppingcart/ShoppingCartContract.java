package com.example.wellfed.shoppingcart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ShoppingCartContract extends ActivityResultContract<ShoppingCartIngredient,
        Pair<String, ShoppingCartIngredient>> {
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, ShoppingCartIngredient shoppingCartIngredient) {
        Intent intent = new Intent(context, ShoppingCartActivity.class);
        intent.putExtra("shoppingCartIngredient", shoppingCartIngredient);
        return intent;
    }

    @Override
    public Pair<String, ShoppingCartIngredient> parseResult(int i, @Nullable Intent intent) {
        if (i != Activity.RESULT_OK || intent == null) {
            return null;
        }
        String type = intent.getStringExtra("type");
        ShoppingCartIngredient shoppingCartIngredient = (ShoppingCartIngredient) intent.getSerializableExtra("shoppingCartIngredient");
        return new Pair<>(type, shoppingCartIngredient);
    }
}
