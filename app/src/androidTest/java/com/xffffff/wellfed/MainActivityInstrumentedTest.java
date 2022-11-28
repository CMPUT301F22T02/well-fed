package com.xffffff.wellfed;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * Intended device: Pixel 6 Pro API 22
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class) public class MainActivityInstrumentedTest {
    int timeout = 2000;

    /**
     * The activity rule for the device.
     */
    @Rule public ActivityScenarioRule<com.xffffff.wellfed.MainActivity>
            activityRule =
            new ActivityScenarioRule<>(com.xffffff.wellfed.MainActivity.class);

    /**
     * Tests the context of the application under the test.
     */
    @Test public void useAppContext() {
        // Context of the app under test.
        Context appContext =
                InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.xffffff.wellfed", appContext.getPackageName());
    }
    
    /**
     * Tests whether bottom app bar navigation is working as intended.
     */
    @Test public void testBottomAppBarNavigation() throws InterruptedException {
        onView(withId(R.id.fragment_meal_book)).check(matches(isDisplayed()));

        onView(withId(R.id.ingredient_storage_item)).perform(click());
        Thread.sleep(timeout); // animation
        onView(withId(R.id.fragment_ingredient_storage)).check(matches(isDisplayed()));
        onView(withId(R.id.fragment_meal_book)).check(matches(not(isDisplayed())));
        onView(withId(R.id.fragment_shopping_cart)).check(matches(not(isDisplayed())));
        onView(withId(R.id.fragment_recipe_book)).check(matches(not(isDisplayed())));


        onView(withId(R.id.meal_book_item)).perform(click());
        Thread.sleep(timeout); // animation
        onView(withId(R.id.fragment_meal_book)).check(matches(isDisplayed()));
        onView(withId(R.id.fragment_ingredient_storage)).check(matches(not(isDisplayed())));
        onView(withId(R.id.fragment_recipe_book)).check(matches(not(isDisplayed())));
        onView(withId(R.id.fragment_shopping_cart)).check(matches(not(isDisplayed())));

        onView(withId(R.id.recipe_book_item)).perform(click());
        Thread.sleep(timeout); // animation
        onView(withId(R.id.fragment_recipe_book)).check(matches(isDisplayed()));
        onView(withId(R.id.fragment_ingredient_storage)).check(matches(not(isDisplayed())));
        onView(withId(R.id.fragment_meal_book)).check(matches(not(isDisplayed())));
        onView(withId(R.id.fragment_shopping_cart)).check(matches(not(isDisplayed())));

        onView(withId(R.id.shopping_cart_item)).perform(click());
        Thread.sleep(timeout); // animation
        onView(withId(R.id.fragment_shopping_cart)).check(matches(isDisplayed()));
        onView(withId(R.id.fragment_ingredient_storage)).check(matches(not(isDisplayed())));
        onView(withId(R.id.fragment_recipe_book)).check(matches(not(isDisplayed())));
        onView(withId(R.id.fragment_meal_book)).check(matches(not(isDisplayed())));
    }
}
