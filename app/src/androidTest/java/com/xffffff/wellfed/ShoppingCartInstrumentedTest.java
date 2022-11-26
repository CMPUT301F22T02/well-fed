package com.xffffff.wellfed;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ShoppingCartInstrumentedTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void Before(){
        closeSoftKeyboard();
        onView(withId(R.id.shopping_cart_item)).perform(click());
    }

    @Test
    public void testAddingMealPlan(){

    }

    @Test
    public void testClickingOnShoppingCartItem(){

    }

    @Test
    public void testDeletingMealPlan(){

    }

    @Test
    public void testSortingWithItems(){

    }

    @Test
    public void testSortingNoItems(){

    }

    @Test
    public void testEditingMealPlanRecipe(){

    }

    @Test
    public void testEditingMealPlanIngredient(){

    }

    @Test
    public void testEditingMealPlanRecipeAndIngredient(){

    }

    @Test
    public void testEditingStoredIngredient(){

    }

    @Test
    public void testEditingRecipe(){

    }
}
