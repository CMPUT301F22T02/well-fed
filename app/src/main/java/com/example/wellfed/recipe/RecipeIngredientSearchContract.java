package com.example.wellfed.recipe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.ingredient.IngredientStorageFragment;
import com.example.wellfed.ingredient.StorageIngredient;

/**
 * Contract for launching the activity for searching for an ingredient to add to a recipe
 */
public class RecipeIngredientSearchContract extends ActivityResultContract<Ingredient, Pair<String, StorageIngredient>> {
    /**
     * Creates an intent for the recipe ingredient search activity to be launched
     *
     * @param context   context that launches the activity
     * @param ingredient ingredient to include in the intent
     *
     * @return the Intent created for the ingredient
     */
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, Ingredient ingredient) {
        Intent intent = new Intent(context, RecipeIngredientSearch.class);
        intent.putExtra("Ingredient", ingredient);
        return intent;
    }

    /**
     * Parse the result from the recipe ingredient search activity
     *
     * @param i         result code of the activity
     * @param intent    intent returned in the result
     * @return          a pair identifying that it is an ingredient related activity
     *                  and the ingredient found by the search activity
     */
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

