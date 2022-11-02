package com.example.wellfed.ingredient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class IngredientContract extends ActivityResultContract<Ingredient,
        Ingredient> {

    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Ingredient ingredient) {
        Intent intent = new Intent(context, IngredientActivity.class);
        intent.putExtra("Ingredient", ingredient);
        intent.putExtra("RequestCode", 101);
        return intent;
    }

    @Override
    public Ingredient parseResult(int i, @Nullable Intent intent) {
        if (i != Activity.RESULT_OK && Objects.requireNonNull(intent).getStringExtra("Reason").equals("Delete")) {
            return null;
        } else {
            assert intent != null;
            return (Ingredient) intent.getSerializableExtra("Ingredient");
        }
    }
}
