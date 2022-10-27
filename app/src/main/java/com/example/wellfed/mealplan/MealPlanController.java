package com.example.wellfed.mealplan;

import com.example.wellfed.recipe.Recipe;

import java.util.ArrayList;
import java.util.List;

public class MealPlanController {
    private List<MealPlan> mealPlans;

    private MealPlanAdapter adapter;

    public List<MealPlan> getMealPlans() {
        return mealPlans;
    }

    public void setAdapter(MealPlanAdapter adapter) {
        this.adapter = adapter;
    }

    public MealPlanController() {
        mealPlans = new ArrayList<>();
        // TODO: REMOVE THIS DEMO DATA
        MealPlan mealPlan = new MealPlan("Cereal and Banana");
        mealPlan.setCategory("Breakfast");
        mealPlan.setServings(2);
        mealPlan.addRecipe(new Recipe("Cereal"));
        mealPlans.add(mealPlan);
        MealPlan mealPlan2 = new MealPlan("Butter Chicken");
        mealPlan2.setCategory("Lunch");
        mealPlans.add(mealPlan2);
        mealPlan.setServings(6);
    }

    public void deleteMealPlan(int index) {
        if (0 <= index && index < this.mealPlans.size()) {
            this.mealPlans.remove(index);
            this.adapter.notifyItemRemoved(index);
        }
    }
}
