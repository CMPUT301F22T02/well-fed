package com.xffffff.wellfed.mealplan;

/**
 * ProxyMealPlan class. It is a proxy for the meal plan object, that does not
 * load the ingredient and recipe references.
 */
public class MealPlanProxy extends MealPlan {
    /**
     * Creates a new ProxyMealPlan object which represents a meal to be eaten
     * on a certain date.
     *
     * @param title The title of the MealPlan
     */
    public MealPlanProxy(String title) {
        super(title);
    }

    /**
     * Get MealPlan
     *
     * @return MealPlan object
     */
    public MealPlan getMealPlan() {
        MealPlan mealPlan = new MealPlan(this.getTitle());
        mealPlan.setId(this.getId());
        mealPlan.setCategory(this.getCategory());
        mealPlan.setEatDate(this.getEatDate());
        mealPlan.setServings(this.getServings());
        return mealPlan;
    }
}
