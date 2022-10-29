package com.example.wellfed.ingredient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class IngredientContract extends ActivityResultContract<StorageIngredient, StorageIngredient> {
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, StorageIngredient input) {
        Intent intent = new Intent(context, IngredientActivity.class);
        intent.putExtra("ingredient", input);
        return intent;
    }

    @Override
    public StorageIngredient parseResult(int resultCode, @Nullable Intent intent) {
        if (resultCode == Activity.RESULT_OK && intent != null) {
            return intent.getParcelableExtra("ingredient");
        }
        return null;
    }
}
