package com.example.wellfed;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test for Ingredients page, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class IngredientInstrumentedTest {
    /**
     * Holds the ActivityScenarioRule
     */
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Setup Recipe test by navigating to RecipeBookFragment
     */
    @Before
    public void before() {
        closeSoftKeyboard();
        onView(withId(R.id.ingredient_storage_item)).perform(click());

    }

    /**
     * Tests adding a complete Ingredient.
     */
    @Test
    public void testAddIngredient() {
        // testing description input
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.descriptionInputEditText)).perform(clearText());
        onView(withId(R.id.descriptionInputEditText)).perform(typeText("Ground Beef"));

        // testing best before input - this should get current day as best before
        onView(withId(R.id.bestBeforeInputEditText)).perform(clearText());
        onView(withId(R.id.bestBeforeInputEditText)).perform(click());
        onView(withText("OK")).perform(click());

        // testing category input
        onView(withId(R.id.categoryInputEditText)).perform(clearText());
        onView(withId(R.id.categoryInputEditText)).perform(typeText("Meat"));

        // testing amount input
        onView(withId(R.id.amountInputEditText)).perform(clearText());
        onView(withId(R.id.amountInputEditText)).perform(typeText("5"));

        // testing unit input
        onView(withId(R.id.unitInputEditText)).perform(clearText());
        onView(withId(R.id.unitInputEditText)).perform(typeText("lb"));

        // testing location input
        onView(withId(R.id.locationInputEditText)).perform(clearText());
        onView(withId(R.id.locationInputEditText)).perform(typeText("Freezer"));

        // saving input
        onView(withId(R.id.ingredient_save_button)).perform(click());

        // finding the ingredient in the RecyclerView
        onView(withId(R.id.ingredient_storage_list))
                .perform(RecyclerViewActions.actionOnItem(
                        hasDescendant(withText("Ground Beef")), click()));

        // checking the correctness of the ingredient by seeing if all text is visible
        onView(withText("Ground Beef")).check(ViewAssertions
                        .matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("Meat")).check(ViewAssertions
                .matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("5.0")).check(ViewAssertions
                .matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("lb")).check(ViewAssertions
                .matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("Freezer")).check(ViewAssertions
                .matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        // deleting the ingredient
        onView(withId(R.id.ingredient_delete_button)).perform(click());
        onView(withText("OK")).perform(click());
    }


}
