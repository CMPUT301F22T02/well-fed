package com.example.wellfed;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Context;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MealPlanInstrumentedTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void before(){
        onView(withId(R.id.meal_book_item)).perform(click());

        Intents.init();
    }

    private void addIngredient(String description){

    }

    private void addRecipe(String title){

    }

    private void cleanUpIngredients(){

    }

    private void cleanUpRecipes(){

    }

    private void cleanUpMealPlan(){

    }


    @Test
    public void testAddMealPlanWithRecipes(){

    }

    @Test
    public void testAddMealPlanWithIngredients(){

    }

    @Test
    public void testAddMealPlanWithRecipeAndIngredients(){

    }

    @Test
    public void testViewMealPlan(){

    }

    @Test
    public void testAddInvalidMealPlan(){

    }

    @Test
    public void testDeleteMealPlan(){

    }

    @Test
    public void testUpdateMealPlanByUpdatingRecipes(){

    }

    @Test
    public void testUpdateMealPlanByUpdatingIngredients(){

    }

    @Test
    public void testUpdateMealPlanInvalid(){

    }
}
