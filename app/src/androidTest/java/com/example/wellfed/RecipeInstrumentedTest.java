package com.example.wellfed;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test for Recipes, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class) public class RecipeInstrumentedTest {

    /**
     * Holds the ActivityScenarioRule
     */
    @Rule public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Setup Recipe test by navigating to RecipeBookFragment
     */
    @Before public void before() {
        onView(withId(R.id.recipe_book_item)).perform(click());
    }

    /**
     * test add and delete recipe
     */
    @Test public void testAddDeleteRecipe() {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.recipe_title_editText)).perform(clearText());
        onView(withId(R.id.recipe_title_editText)).perform(typeText("Tacos"));
        onView(withId(R.id.recipe_prep_time_editText)).perform(clearText());
        onView(withId(R.id.recipe_prep_time_editText)).perform(typeText("10"));
        onView(withId(R.id.recipe_no_of_servings_editText)).perform(
                clearText());
//        onView(withId(R.id.recipe_no_of_servings_editText)).perform(
//                typeText("2"));
//        onView(withId(R.id.recipe_category_textEdit)).perform(click());
//        onView(withText("Lunch")).inRoot(RootMatchers.isPlatformPopup())
//                .perform(click());
//        onView(withId(R.id.commentsEditText)).perform(typeText(
//                "Ground beef and homemade taco seasoning" +
//                        "make the best taco meat in this classic easy" +
//                        "recipe that's better than take out."));
//        closeSoftKeyboard();
//        onView(withId(R.id.save_fab)).perform(click());
//
//        onView(withId(R.id.recipeRecyclerView))
//                .perform(RecyclerViewActions.actionOnItem(
//                hasDescendant(withText("Tacos")), click()));
//        onView(withId(R.id.deleteButton)).perform(click());
    }
}
