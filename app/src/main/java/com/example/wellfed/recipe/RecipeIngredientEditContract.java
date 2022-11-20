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

/**
 * Contract for launching an ingredient edit activity from within a recipe
 */
public class RecipeIngredientEditContract extends ActivityResultContract<Ingredient, Pair<String, Ingredient>> {
    /**
     * Creates an intent for the recipe ingredient edit activity to be launched
     *
     * @param context   context that launches the activity
     * @param ingredient ingredient to include in the intent, to be edited
     *
     * @return the Intent created for the recipe
     */
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Ingredient ingredient) {
        Intent intent = new Intent(context, RecipeIngredientEditActivity.class);
        intent.putExtra("ingredient", ingredient);
        return intent;
    }

    /**
     * Parse the result from the recipe ingredient edit activity
     *
     * @param i         result code of the activity
     * @param intent    intent returned in the result
     * @return          a pair identifying that it is an ingredient related activity
     *                  and the ingredient created by the edit activity
     */
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
