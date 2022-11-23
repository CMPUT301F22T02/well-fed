package com.example.wellfed.recipe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Contract for launching a RecipeEditActivity
 */
public class RecipeEditContract extends ActivityResultContract<Recipe, Pair<String, Recipe>> {

    /**
     * Creates an intent for the recipe edit activity to be launched
     *
     * @param context context that launches the activity
     * @param recipe recipe to include in the intent
     *
     * @return the Intent created for the recipe
     */
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Recipe recipe) {
        Intent intent = new Intent(context, RecipeEditActivity.class);
        intent.putExtra("item", recipe);
        return intent;
    }


    /**
     * Parses the result from the recipe edit activity
     *
     * @param i         result code of the activity
     * @param intent    intent returned in the result
     * @return          a pair identifying that it is a recipe related activity
     *                  and the recipe created by the edit activity
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