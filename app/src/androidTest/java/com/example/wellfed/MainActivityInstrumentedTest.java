package com.example.wellfed;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isSelected;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.internal.util.Checks;
import androidx.test.platform.app.InstrumentationRegistry;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityInstrumentedTest {

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.wellfed", appContext.getPackageName());
    }

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void testSwipeNavigation() {
        ViewAction[] swipes = {
                swipeLeft(),
                swipeLeft(),
                swipeRight(),
                swipeRight(),
                swipeRight(),
                swipeRight(),
        };
        int[] fragmentsIds = {
                R.id.fragment_shopping_cart,
                R.id.fragment_shopping_cart,
                R.id.fragment_meal_book,
                R.id.fragment_recipe_book,
                R.id.fragment_ingredient_storage,
                R.id.fragment_ingredient_storage,
        };
        onView(withId(R.id.fragment_meal_book)).check(matches(isDisplayed()));
        for (int i = 0; i < swipes.length; i++) {
            onView(withId(R.id.pager)).perform(swipes[i]);
            onView(withId(fragmentsIds[i])).check(matches(isDisplayed()));
            // TODO: test menu item tinting
        }
    }

    @Test
    public void testBottomAppBarNavigation() {
        Map<Integer, Integer> menuItemsToFragmentIds = new HashMap<>();
        Stack<Integer> history = new Stack<>();
        menuItemsToFragmentIds.put(R.id.ingredient_storage, R.id.fragment_ingredient_storage);
        menuItemsToFragmentIds.put(R.id.meal_book, R.id.fragment_meal_book);
        menuItemsToFragmentIds.put(R.id.recipe_book, R.id.fragment_recipe_book);
        menuItemsToFragmentIds.put(R.id.shopping_cart, R.id.fragment_shopping_cart);
        onView(withId(R.id.fragment_meal_book)).check(matches(isDisplayed()));
        for (Map.Entry<Integer, Integer> entry1 :
                menuItemsToFragmentIds.entrySet()) {
            onView(withId(entry1.getKey())).perform(click());
            onView(withId(entry1.getValue())).check(matches(isDisplayed()));
            for (Map.Entry<Integer, Integer> entry2 :
                    menuItemsToFragmentIds.entrySet()) {
                onView(withId(entry2.getKey())).perform(click());
                onView(withId(entry2.getValue())).check(matches(isDisplayed()));
            }
        }
        // TODO: test menu item tinting
    }

    @Test
    public void testBackButtonNavigation() {
        onView(withId(R.id.meal_book)).perform(click());
        onView(withId(R.id.shopping_cart)).perform(click());
        onView(withId(R.id.pager)).perform(swipeLeft());
        onView(withId(R.id.ingredient_storage)).perform(click());
        onView(withId(R.id.pager)).perform(swipeLeft());
        onView(withId(R.id.fragment_recipe_book)).check(matches(isDisplayed()));
        pressBack();
        onView(withId(R.id.fragment_ingredient_storage)).check(matches(isDisplayed()));
        pressBack();
        onView(withId(R.id.fragment_shopping_cart)).check(matches(isDisplayed()));
        pressBack();
        onView(withId(R.id.fragment_meal_book)).check(matches(isDisplayed()));
        pressBack();
        assertEquals(activityRule.getScenario().getState(),
                Lifecycle.State.DESTROYED);
    }
    // TODO: test menu item tinting
}
