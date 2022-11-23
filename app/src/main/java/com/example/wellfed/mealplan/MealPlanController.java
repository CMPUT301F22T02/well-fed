package com.example.wellfed.mealplan;

import android.app.Activity;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.common.DBConnection;
import com.example.wellfed.common.Launcher;

import java.util.ArrayList;
import java.util.Locale;

public class MealPlanController {
    private final ActivityBase activity;
    private MealPlanAdapter adapter;
    /**
     * The DB of stored ingredients
     */
    private final MealPlanDB db;

    public void setAdapter(MealPlanAdapter adapter) {
        this.adapter = adapter;
    }

    public MealPlanController(Activity activity) {
        this.activity = (ActivityBase) activity;
        DBConnection connection = new DBConnection(activity.getApplicationContext());
        db = new MealPlanDB(connection);
        adapter = new MealPlanAdapter(db);
    }

    public MealPlanAdapter getAdapter() {
        return adapter;
    }

    public void addMealPlan(MealPlan mealPlan) {
        db.addMealPlan(mealPlan, (addMealPlan, addSuccess) -> {
            if (!addSuccess) {
                this.activity.makeSnackbar("Failed to add " + addMealPlan.getTitle());
            } else {
                this.activity.makeSnackbar("Added " + addMealPlan.getTitle());
            }
        });
    }

    public void updateMealPlan(MealPlan mealPlan, Launcher<MealPlan> launcher) {
        db.updateMealPlan(mealPlan, (updateMealPlan, updateSuccess) -> {
            if (!updateSuccess) {
                this.activity.makeSnackbar("Failed to update " + updateMealPlan.getTitle());
            } else {
                this.activity.makeSnackbar("Updated " + updateMealPlan.getTitle());
                launcher.launch(updateMealPlan);
            }
        });
    }

    public void deleteMealPlan(MealPlan mealPlan) {
        this.db.delMealPlan(mealPlan, ((deleteMealPlan,
                                       deleteSuccess) -> {
            if (!deleteSuccess) {
                this.activity.makeSnackbar("Failed to delete" + deleteMealPlan.getTitle());
            } else {
                this.activity.makeSnackbar("Deleted " + deleteMealPlan.getTitle());
            }
        }));
    }
}
