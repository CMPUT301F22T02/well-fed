package com.example.wellfed.mealplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MealPlanEditContract extends ActivityResultContract<MealPlan,
        Pair<String, MealPlan>> {
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context,
                               @Nullable MealPlan mealPlan) {
        Intent intent = new Intent(context, MealPlanEditActivity.class);
        intent.putExtra("mealPlan", mealPlan);
        return intent;
    }

    @Override
    public Pair<String, MealPlan> parseResult(int i, Intent intent) {
        switch (i) {
            case Activity.RESULT_OK:
                return new Pair<>("edit", (MealPlan) intent.getSerializableExtra(
                        "mealPlan"));
            case Activity.RESULT_CANCELED:
                return new Pair<>("quit", null);
            default:
                return null;
        }
    }
}
