package com.example.wellfed.shoppingcart;

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
    }
}
