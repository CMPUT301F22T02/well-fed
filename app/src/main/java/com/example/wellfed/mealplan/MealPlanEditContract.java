package com.example.wellfed.mealplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;

public class MealPlanEditContract extends ActivityResultContract<MealPlan, MealPlan> {
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, MealPlan mealPlan) {
        Intent intent = new Intent(context, MealPlanEditContract.class);
        intent.putExtra("mealPlan", mealPlan);
        return intent;
    }

    @Override
    public MealPlan parseResult(int i, Intent intent) {
        if (i != Activity.RESULT_OK) {
            return null;
        }
        return (MealPlan) intent.getSerializableExtra("mealPlan");
    }
}
