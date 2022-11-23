package com.xffffff.wellfed.mealplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * The MealPlanContract class is the contract for the meal plan activity. It
 * launches the meal plan activity and parses the result.
 */
public class MealPlanEditContract
        extends ActivityResultContract<MealPlan, Pair<String, MealPlan>> {
    /**
     * The createIntent method creates the intent to launch the meal plan
     *
     * @param context  The context.
     * @param mealPlan The meal plan.
     * @return The intent to launch the meal plan activity.
     */
    @NonNull @Override public Intent createIntent(@NonNull Context context,
                                                  @Nullable MealPlan mealPlan) {
        Intent intent = new Intent(context, MealPlanEditActivity.class);
        intent.putExtra("mealPlan", mealPlan);
        return intent;
    }

    /**
     * The parseResult method parses the result from the meal plan activity. It
     * returns the type of result and the meal plan.
     *
     * @param i      The result code.
     * @param intent The intent.
     * @return The type of result and the meal plan.
     */
    @Override public Pair<String, MealPlan> parseResult(int i, Intent intent) {
        switch (i) {
            case Activity.RESULT_OK:
                if (intent == null) {
                    return null;
                }
                MealPlan mealPlan =
                        (MealPlan) intent.getSerializableExtra("mealPlan");
                String type = intent.getStringExtra("type");
                return new Pair<>(type, mealPlan);
            case Activity.RESULT_CANCELED:
                return new Pair<>("quit", null);
            default:
                return null;
        }
    }
}
