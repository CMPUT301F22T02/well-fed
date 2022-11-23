package com.example.wellfed.mealplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MealPlanContract extends ActivityResultContract<MealPlan,
        Pair<String, MealPlan>> {
    /**
     * The createIntent method creates the intent to launch the meal plan
     * activity. It passes the meal plan to the activity.
     * @param context The context.
     * @param mealPlan The meal plan.
     * @return The intent to launch the meal plan activity.
     */
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, MealPlan mealPlan) {
        Intent intent = new Intent(context, MealPlanActivity.class);
        intent.putExtra("mealPlan", mealPlan);
        return intent;
    }

    /**
     * The parseResult method parses the result from the meal plan activity. It
     * returns the type of result and the meal plan.
     * @param i The result code.
     * @param intent The intent.
     * @return The type of result and the meal plan.
     */
    @Override
    public Pair<String, MealPlan> parseResult(int i, @Nullable Intent intent) {
        if (i != Activity.RESULT_OK || intent == null) {
            return null;
        }
        String type = intent.getStringExtra("type");
        MealPlan mealPlan = (MealPlan) intent.getSerializableExtra("mealPlan");
        return new Pair<>(type, mealPlan);
    }
}
