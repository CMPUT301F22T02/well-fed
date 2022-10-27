package com.example.wellfed.recipe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wellfed.MainActivity;

public class RecipeContract extends ActivityResultContract<Recipe, Recipe> {
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Recipe recipe) {
        Intent intent = new Intent(context, RecipeActivity.class);
        intent.putExtra("Recipe", recipe);
        intent.putExtra("RequestCode", 101);
        return intent;
    }

    @Override
    public Recipe parseResult(int i, @Nullable Intent intent) {
        if (i != Activity.RESULT_OK && intent.getIntExtra("RequestCode", - 1) != 101) {
            return null;
        } else {
            return (Recipe) intent.getSerializableExtra("Recipe");
        }
    }
}
