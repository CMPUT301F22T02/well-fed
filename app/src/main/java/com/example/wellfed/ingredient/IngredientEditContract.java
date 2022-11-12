package com.example.wellfed.ingredient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wellfed.mealplan.MealPlan;

public class IngredientEditContract extends ActivityResultContract<StorageIngredient, Pair<String, StorageIngredient>> {
    /**
     * Creates an Intent for the IngredientEditActivity.
     * @param context Context object for the activity.
     * @param storageIngredient StorageIngredient object for the ingredient.
     * @return
     */
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, StorageIngredient storageIngredient) {
        Intent intent = new Intent(context, IngredientEditActivity.class);
        intent.putExtra("ingredient", storageIngredient);
        return intent;
    }

    /**
     * Pair object for the result of the IngredientEditActivity.
     * @param i index of the ingredient.
     * @param intent Intent object for the IngredientEditActivity.
     * @return Pair object for the result of the IngredientEditActivity.
     */
    @Override
    public Pair<String, StorageIngredient> parseResult(int i, Intent intent) {
        switch (i) {
            case Activity.RESULT_OK:
                if (intent == null) {
                    return null;
                }
                StorageIngredient storageIngredient =
                        (StorageIngredient) intent.getSerializableExtra(
                        "ingredient");
                String type = intent.getStringExtra("type");
                return new Pair<>(type, storageIngredient);
            case Activity.RESULT_CANCELED:
                return new Pair<>("quit", null);
            default:
                return null;
        }
    }
}

