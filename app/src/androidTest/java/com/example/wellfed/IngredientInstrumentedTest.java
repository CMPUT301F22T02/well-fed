package com.example.wellfed;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.contrib.PickerActions.setDate;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.widget.DatePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.material.datepicker.MaterialDatePicker;

import org.hamcrest.Matchers;
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
     * Tests adding an Ingredient
     */
    @Test
    public void testAddAndDeleteIngredient() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.descriptionInputEditText)).perform(clearText());
        onView(withId(R.id.descriptionInputEditText)).perform(typeText("Ground Beef"));

        // testing best before input - this should get current day as best before
        // TODO: find a way to pick from the material date picker?
        onView(withId(R.id.bestBeforeInputEditText)).perform(clearText());
        onView(withId(R.id.bestBeforeInputEditText)).perform(click());
        onView(withText("OK")).perform(click());

        


    }
}
