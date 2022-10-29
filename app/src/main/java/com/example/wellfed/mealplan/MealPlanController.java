package com.example.wellfed.mealplan;

import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.recipe.Recipe;

import java.util.ArrayList;
import java.util.Date;

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
        mealPlan.setEatDate(new Date());
        mealPlan.setCategory("Breakfast");
        mealPlan.setServings(2);
        mealPlan.addRecipe(new Recipe("Cereal"));
        mealPlan.addIngredient(new Ingredient("Banana"));
        mealPlans.add(mealPlan);
        MealPlan mealPlan2 = new MealPlan("Butter Chicken");
        mealPlan2.setEatDate(new Date());
        mealPlan2.setCategory("Lunch");
        mealPlan2.setServings(6);
        mealPlans.add(mealPlan2);
        mealPlan.setServings(6);
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
}
