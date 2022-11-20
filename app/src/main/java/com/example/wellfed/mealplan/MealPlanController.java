package com.example.wellfed.mealplan;

import android.app.Activity;

import androidx.fragment.app.FragmentActivity;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.common.DBConnection;
import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.ingredient.StorageIngredientAdapter;
import com.example.wellfed.ingredient.StorageIngredientDB;
import com.example.wellfed.recipe.Recipe;
import com.google.common.collect.Iterables;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.function.Predicate;

public class MealPlanController {
    private ArrayList<MealPlan> mealPlans;

    private MealPlanAdapter adapter;

    private final ActivityBase activity;

    public ArrayList<MealPlan> getMealPlans() {
        return mealPlans;
    }

    /**
     * The DB of stored ingredients
     */
    private final MealPlanDB db;

    public void setAdapter(MealPlanAdapter adapter) {
        this.adapter = adapter;
    }

    public MealPlanController(Activity activity) {
        mealPlans = new ArrayList<>();
        this.activity = (ActivityBase) activity;
        DBConnection connection = new DBConnection(activity.getApplicationContext());
        db = new MealPlanDB(connection);
        adapter = new MealPlanAdapter(db);
    }

    public MealPlanAdapter getAdapter() {
        return adapter;
    }

    public void addMealPlan(MealPlan mealPlan) {
        this.mealPlans.add(mealPlan);
        this.adapter.notifyItemInserted(this.mealPlans.size() - 1);
    }

    public void editMealPlan(int index, MealPlan modifiedMealPlan) {
        this.mealPlans.set(index, modifiedMealPlan);
        this.adapter.notifyItemChanged(index);
    }

    public void deleteMealPlan(int index) {
        if (0 <= index && index < this.mealPlans.size()) {
            this.mealPlans.remove(index);
            this.adapter.notifyItemRemoved(index);
        }
    }

    public MealPlan getNextMealPlan() {
        Date today = new Date();
        //        TODO: refactor move all date logic to its own class?
        SimpleDateFormat hashDateFormat =
                new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        hashDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            today = hashDateFormat.parse(hashDateFormat.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (MealPlan mealPlan : mealPlans) {
            if (mealPlan.getEatDate().after(today) ||
                    mealPlan.getEatDate().equals(today)) {
                return mealPlan;
            }
        }
        return null;
    }
}
