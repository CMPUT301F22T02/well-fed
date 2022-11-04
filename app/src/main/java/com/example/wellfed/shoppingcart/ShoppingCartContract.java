package com.example.wellfed.shoppingcart;

import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;

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
    public Pair<String>
}
