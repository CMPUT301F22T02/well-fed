package com.example.wellfed.recipe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wellfed.MainActivity;

/**
 * Contract for launching new activities that return a Recipe
 * @version 1.0.0
 */
public class RecipeContract extends ActivityResultContract<Recipe, Pair<String, Recipe>> {

    /**
     * Creates an intent that contains recipe{@link Recipe}
     * @param context
     * @param recipe
     * @return
     */
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Recipe recipe) {
        Intent intent = new Intent(context, RecipeActivity.class);
        intent.putExtra("item", recipe);
        return intent;
    }

    /**
     * Parse the response from the activity as required
     * @param i
     * @param intent
     * @return null if activity was interrupted or failed to return anything
     */
    @Override
    public Pair<String, Recipe> parseResult(int i, @Nullable Intent intent) {
        if (i != Activity.RESULT_OK || intent == null) {
            return null;
        } else {
            String type = intent.getStringExtra("type");
            Recipe recipe = (Recipe) intent.getSerializableExtra("item");
            return new Pair<>(type,recipe);
        }
    }
}
