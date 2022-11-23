package com.xffffff.wellfed.ingredient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * A contract for creating intents to view an Ingredient, and results of
 * intents.
 */
public class IngredientContract extends
                                ActivityResultContract<StorageIngredient,
                                        Pair<String, StorageIngredient>> {
    /**
     * Creates an Intent for the IngredientEditActivity.
     *
     * @param context           Context object for the activity.
     * @param storageIngredient StorageIngredient object for the ingredient.
     * @return Intent object for the IngredientEditActivity.
     */
    @NonNull @Override public Intent createIntent(@NonNull Context context,
                                                  StorageIngredient storageIngredient) {
        Intent intent = new Intent(context, IngredientActivity.class);
        intent.putExtra("ingredient", storageIngredient);
        return intent;
    }

    /**
     * Pair object for the result of the IngredientEditActivity.
     *
     * @param i      index of the ingredient.
     * @param intent Intent object for the IngredientEditActivity.
     * @return Pair object for the result of the IngredientEditActivity.
     */
    @Override public Pair<String, StorageIngredient> parseResult(int i,
                                                                 @Nullable
                                                                         Intent intent) {
        if (i != Activity.RESULT_OK || intent == null) {
            return null;
        }
        String type = intent.getStringExtra("type");
        StorageIngredient ingredient =
                (StorageIngredient) intent.getSerializableExtra("ingredient");
        return new Pair<>(type, ingredient);
    }
}
