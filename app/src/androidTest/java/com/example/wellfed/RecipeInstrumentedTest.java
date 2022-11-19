package com.example.wellfed;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressKey;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.times;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import androidx.test.espresso.contrib.RecyclerViewActions;
import static com.google.common.base.Predicates.instanceOf;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.junit.Assert.assertEquals;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.concurrent.TimeUnit;


import android.app.Activity;
import android.view.KeyEvent;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.wellfed.recipe.RecipeActivity;
import com.example.wellfed.recipe.RecipeEditActivity;
import com.example.wellfed.recipe.RecipeIngredientEditActivity;

import junit.framework.AssertionFailedError;

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

    private final Object lock = new Object();

    /**
     * Holds the ActivityScenarioRule
     */
    @Rule public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Setup Recipe test by navigating to RecipeBookFragment
     */
    @Before public void before() throws InterruptedException {
        // pre-add storage ingredient
        onView(withId(R.id.ingredient_storage_item)).perform(click());

        // typing description input
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.descriptionInputEditText)).perform(clearText());
        onView(withId(R.id.descriptionInputEditText)).perform(typeText("Tortilla"));
        closeSoftKeyboard();

        // typing best before input - this should get current day as best before
        onView(withId(R.id.bestBeforeInputEditText)).perform(clearText());
        onView(withId(R.id.bestBeforeInputEditText)).perform(click());
        onView(withText("OK")).perform(click());

        // typing category input
        onView(withId(R.id.categoryInputEditText)).perform(clearText());
        onView(withId(R.id.categoryInputEditText)).perform(typeText("Bread"));
        closeSoftKeyboard();

        // typing amount input
        onView(withId(R.id.amountInputEditText)).perform(clearText());
        onView(withId(R.id.amountInputEditText)).perform(typeText("5"));
        closeSoftKeyboard();

        // typing unit input
        onView(withId(R.id.unitInputEditText)).perform(clearText());
        onView(withId(R.id.unitInputEditText)).perform(typeText("count"));
        closeSoftKeyboard();

        // typing location input
        onView(withId(R.id.locationInputEditText)).perform(clearText());
        onView(withId(R.id.locationInputEditText)).perform(typeText("Breadbox"));
        closeSoftKeyboard();

        onView(withId(R.id.ingredient_save_button)).perform(click());

        onView(withId(R.id.recipe_book_item)).perform(click());

        Intents.init();
    }

    /**
     * Types out a mock recipe.
     * @throws InterruptedException
     */
    public void typeMockRecipe() throws InterruptedException {
        onView(withId(R.id.fab)).perform(click());

        Thread.sleep(2000);

        onView(withId(R.id.edit_recipe_title)).perform(typeText("Egg Wrap"));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_prep_time_textEdit)).perform(typeText("5"));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_no_of_servings_textEdit)).perform(typeText("1"));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_category)).perform(click());
        closeSoftKeyboard();

        onView(withText("Breakfast"))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        closeSoftKeyboard();

        onView(withId(R.id.commentsEditText)).perform(typeText("This breakfast is great for on the go."));
        closeSoftKeyboard();
    }

    /**
     * Adds a mock ingredient to a recipe.
     */
    public void addMockIngredient() {
        //add an ingredient
        onView(withId(R.id.ingredient_add_btn)).perform(click());
        onView(withId(R.id.edit_descriptionInput)).perform(typeText("Egg"));
        closeSoftKeyboard();

        onView(withId(R.id.categoryInput)).perform(click());
        onView(withText("Protein"))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        closeSoftKeyboard();

        onView(withId(R.id.edit_amountInput)).perform(typeText("1"));
        closeSoftKeyboard();

        onView(withId(R.id.edit_unitInput)).perform(click());
        onView(withId(R.id.edit_unitInput)).perform(typeText("count"));

        closeSoftKeyboard();
        onView(withId(R.id.ingredient_save_button)).perform(click());
    }

    /**
     * Test adding a full recipe with 2 ingredients (one searched, one added) and deleting a recipe
     */
    @Test public void testAddAndDeleteRecipe() throws InterruptedException {

        typeMockRecipe();

        onView(withId(R.id.ingredient_search_btn)).perform(click());
        //pick an ingredient check if recycler view is non empty
        onView(withText("Tortilla")).perform(click());

        onView(withId(R.id.edit_amountInput)).perform(typeText("1"));
        closeSoftKeyboard();
        onView(withId(R.id.edit_unitInput)).perform(click());
        onView(withId(R.id.edit_unitInput)).perform(typeText("count"));
        closeSoftKeyboard();

        onView(withId(R.id.ingredient_save_button)).perform(click());

        addMockIngredient();


        onView(withId(R.id.save_fab)).perform(click());


        Thread.sleep(2000);

        onView(withText("Egg Wrap")).perform(click());

        onView(withId(R.id.recipe_delete_btn)).perform(click());
        onView(withText("Delete")).perform(click());

        Thread.sleep(2000);
        
        onView(withText("Egg Wrap")).check(doesNotExist());
    }


    @Test public void testAddOnInvalidRecipe() throws InterruptedException {

        typeMockRecipe();

        //Try to add without ingredients
        onView(withId(R.id.save_fab)).perform(click());
        intended(hasComponent(RecipeEditActivity.class.getName()));

        onView(withId(R.id.ingredient_search_btn)).perform(click());
        //pick an ingredient check if recycler view is non empty
        onView(withId(R.id.ingredient_storage_list)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        intended(hasComponent(RecipeIngredientEditActivity.class.getName()));
        onView(withId(R.id.edit_amountInput)).perform(typeText("1"));

        closeSoftKeyboard();
        onView(withId(R.id.unitInput)).perform(click());
        onView(withText("lb"))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        closeSoftKeyboard();
        onView(withId(R.id.ingredient_save_button)).perform(click());

        addMockIngredient();

        //Try to add when title is removed
        onView(withId(R.id.edit_recipe_title)).perform(clearText());

        intended(hasComponent(RecipeEditActivity.class.getName()));

        //Test for no prep time
        onView(withId(R.id.recipe_prep_time_textEdit)).perform(clearText());
        closeSoftKeyboard();
        onView(withId(R.id.edit_recipe_title)).perform(typeText("Egg Wrap"));
        closeSoftKeyboard();

        onView(withId(R.id.save_fab)).perform(click());

        intended(hasComponent(RecipeEditActivity.class.getName()));

        onView(withId(R.id.recipe_no_of_servings_textEdit)).perform(clearText());
        closeSoftKeyboard();
        onView(withId(R.id.recipe_prep_time_textEdit)).perform(typeText("5"));
        closeSoftKeyboard();

        onView(withId(R.id.save_fab)).perform(click());

        intended(hasComponent(RecipeEditActivity.class.getName()));

        onView(withId(R.id.recipe_no_of_servings_textEdit)).perform(typeText("1"));
        closeSoftKeyboard();
        onView(withId(R.id.commentsEditText)).perform(clearText());

        intended(hasComponent(RecipeEditActivity.class.getName()));

        onView(withId(R.id.commentsEditText)).perform(typeText("This is perfect for on the go."));
    }
    /**
     * Test viewing a single recipe
     */
    @Test public void TestViewingRecipe() throws InterruptedException {

        typeMockRecipe();

        addMockIngredient();

        onView(withId(R.id.save_fab)).perform(click());

        Thread.sleep(2000);

        onView(withId(R.id.recipe_rv))
                .perform(RecyclerViewActions.actionOnItem(withText("Egg Wrap"), click()));

        intended(hasComponent(RecipeActivity.class.getName()));

        onView(withId(R.id.recipe_title_textView)).check(matches(withText("Egg Wrap")));
        onView(withId(R.id.recipe_prep_time_textView)).check(matches(withText("Prepartion time: 5")));
        onView(withId(R.id.recipe_no_of_servings_textView)).check(matches(withText("Servings: 1")));
        onView(withId(R.id.recipe_category)).check(matches(withText("Category: Breakfast")));
        onView(withId(R.id.recipe_description_textView)).check(matches(withText("This breakfast is great for on the go.")));
        onView(withId(R.id.recipe_ingredient_recycleViewer)).check(matches(hasDescendant(withText("1.0 count"))));
        onView(withId(R.id.recipe_ingredient_recycleViewer)).check(matches(hasDescendant(withText("Egg"))));

        onView(withId(R.id.recipe_delete_btn)).perform(click());
        onView(withText("Delete")).perform(click());

    }
    /**
     * Test deleting a Ingredient from recipe
     */
    @Test public void testDelIngredientFromRecipe(){

    }
    /**
     * Test editing a Recipe
     */
    @Test public void testEditingARecipe(){

    }
    /**
     * Test editing an ingredient in a Recipe
     */
    @Test public void testEditingIngredientOfARecipe(){

    }
}
