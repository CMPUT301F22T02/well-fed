package com.example.wellfed.mealplan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wellfed.recipe.Recipe;
import com.example.wellfed.recipe.RecipeActivity;

public class MealPlanContract extends ActivityResultContract<MealPlan, MealPlan> {
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, MealPlan mealPlan) {
        Intent intent = new Intent(context, MealPlanActivity.class);
        intent.putExtra("mealPlan", mealPlan);
        intent.putExtra("RequestCode", 101);
        return intent;
    }

    @Override
    public MealPlan parseResult(int i, @Nullable Intent intent) {
        if (i != Activity.RESULT_OK && intent.getStringExtra("Reason").equals("Delete")) {
            return null;
        } else {
            return (MealPlan) intent.getSerializableExtra("mealPlan");
        }
    }
}
