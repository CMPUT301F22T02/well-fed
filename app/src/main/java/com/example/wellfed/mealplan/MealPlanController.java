package com.example.wellfed.mealplan;

import android.app.Activity;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.common.DBConnection;
import com.example.wellfed.common.Launcher;

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

    public void updateMealPlan(MealPlan mealPlan, Launcher launcher) {
        db.updateMealPlan(mealPlan, (updateMealPlan, updateSuccess) -> {
            if (!updateSuccess) {
                this.activity.makeSnackbar("Failed to update " + updateMealPlan.getTitle());
                launcher.launch(updateMealPlan);
            } else {
                this.activity.makeSnackbar("Updated " + updateMealPlan.getTitle());
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

    public MealPlan getNextMealPlan() {
//        Date today = new Date();
//        //        TODO: refactor move all date logic to its own class?
//        SimpleDateFormat hashDateFormat =
//                new SimpleDateFormat("yyyy-MM-dd", Locale.US);
//        hashDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
//        try {
//            today = hashDateFormat.parse(hashDateFormat.format(today));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        for (MealPlan mealPlan : mealPlans) {
//            if (mealPlan.getEatDate().after(today) ||
//                    mealPlan.getEatDate().equals(today)) {
//                return mealPlan;
//            }
//        }
        return null;
    }
}
