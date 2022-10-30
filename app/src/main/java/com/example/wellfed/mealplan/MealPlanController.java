package com.example.wellfed.mealplan;

import com.example.wellfed.ingredient.Ingredient;
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

    public ArrayList<MealPlan> getMealPlans() {
        return mealPlans;
    }

    public void setAdapter(MealPlanAdapter adapter) {
        this.adapter = adapter;
    }

    public MealPlanController() {
        mealPlans = new ArrayList<>();
        // TODO: REMOVE THIS DEMO DATA
        MealPlan mealPlan = new MealPlan("Cereal and Banana");
        mealPlan.setEatDate(new Date(1665532800000L));
        mealPlan.setCategory("Breakfast");
        mealPlan.setServings(2);
        mealPlan.addRecipe(new Recipe("Cereal"));
        mealPlan.addIngredient(new Ingredient("Banana"));
        mealPlans.add(mealPlan);
        MealPlan mealPlan2 = new MealPlan("Butter Chicken");
        mealPlan2.setEatDate(new Date(1665532800000L));
        mealPlan2.setCategory("Lunch");
        mealPlan2.setServings(6);
        mealPlans.add(mealPlan2);
        MealPlan mealPlan3 = new MealPlan("Banana");
        mealPlan3.setEatDate(new Date(1667103854436L));
        mealPlan3.setCategory("Dinner");
        mealPlan3.setServings(100);
        mealPlans.add(mealPlan3);
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
