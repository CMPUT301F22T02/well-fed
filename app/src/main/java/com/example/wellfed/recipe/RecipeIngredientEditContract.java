package com.example.wellfed.recipe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.ingredient.StorageIngredient;

public class RecipeIngredientEditContract extends ActivityResultContract<Ingredient, Pair<String, Ingredient>> {
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Ingredient ingredient) {
        Intent intent = new Intent(context, RecipeIngredientEditActivity.class);
        intent.putExtra("ingredient", ingredient);
        return intent;
    }

    @Override
    public Pair<String, Ingredient> parseResult(int i, @Nullable Intent intent) {
        if (i != Activity.RESULT_OK || intent == null) {
            return null;
        } else {
            String type = intent.getStringExtra("type");
            Ingredient ingredient = (Ingredient) intent.getSerializableExtra("ingredient");
            return new Pair<>(type, ingredient);
        }
    }
}
