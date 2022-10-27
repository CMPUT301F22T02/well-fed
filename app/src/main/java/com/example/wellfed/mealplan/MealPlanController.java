package com.example.wellfed.mealplan;

import com.example.wellfed.recipe.Recipe;
import com.example.wellfed.recipe.RecipeAdapter;

import java.util.List;

public class MealPlanController {
    private static MealPlanController instance;
    private List<MealPlan> mealPlans;
    private MealPlanAdapter mealPlanAdapter;



    public MealPlanController(){

    }

    public void setMealPlanAdapter(MealPlanAdapter mealPlanAdapter) {
        this.mealPlanAdapter = mealPlanAdapter;
    }

    public void deleteMealPlan(int position){
        if(position >= 0 && position < mealPlans.size()){
            mealPlans.remove(position);
            mealPlanAdapter.notifyItemRemoved(position);
        }
    }

    public void setRecipes(List<MealPlan> recipes){
        this.mealPlans = recipes;
    }

}
