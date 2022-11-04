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
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, MealPlan mealPlan) {
        Intent intent = new Intent(context, MealPlanActivity.class);
        intent.putExtra("mealPlan", mealPlan);
        return intent;
    }

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
