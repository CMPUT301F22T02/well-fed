package com.example.wellfed.recipe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Contract that launches a RecipeEditActivity
 */
public class RecipeEditContract extends ActivityResultContract<Recipe, Pair<String, Recipe>> {

    /**
     * creates an intent for the recipe
     * @param context context that launches the activity
     * @param recipe recipe to include in the intent
     * @return
     */
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Recipe recipe) {
        Intent intent = new Intent(context, RecipeEditActivity.class);
        intent.putExtra("Recipe", recipe);
        return intent;
    }


    /**
     * parses the result from the activity
     * @param i result code of the activity
     * @param intent intent returned in the result
     * @return
     */
    @Override
    public Pair<String, Recipe> parseResult(int i, @Nullable Intent intent) {
        if (i != Activity.RESULT_OK || intent == null) {
            return null;
        } else {
            String type = intent.getStringExtra("type");
            Recipe recipe = (Recipe) intent.getSerializableExtra("Recipe");
            return new Pair<>(type,recipe);
        }
    }
}