package com.example.wellfed.recipe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wellfed.ingredient.IngredientStorageFragment;
import com.example.wellfed.ingredient.StorageIngredient;

public class RecipeIngredientSearchContract extends ActivityResultContract<String, Pair<String, StorageIngredient>> {
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, String s) {
        Intent intent = new Intent(context, RecipeIngredientSearch.class);
        intent.putExtra("Recipe", s);
        return intent;
    }

    @Override
    public Pair<String, StorageIngredient> parseResult(int i, @Nullable Intent intent) {

        if (i != Activity.RESULT_OK || intent == null) {
            return null;
        } else {
            String type = intent.getStringExtra("type");
            StorageIngredient storageIngredient = (StorageIngredient) intent.getSerializableExtra("ingredient");
            return new Pair<>(type, storageIngredient);
        }
    }
}

