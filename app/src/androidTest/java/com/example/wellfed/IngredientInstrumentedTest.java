package com.example.wellfed;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.idling.CountingIdlingResource;
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
     * Setup Ingredient test by navigating to IngredientFragment
     */
    @Before
    public void before() {
        closeSoftKeyboard();
        onView(withId(R.id.ingredient_storage_item)).perform(click());
    }

    /**
     * Performs all of the actions needed to type a complete ingredient.
     */
    private void typeMockIngredient() {
        // typing description input
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.descriptionInputEditText)).perform(clearText());
        onView(withId(R.id.descriptionInputEditText)).perform(typeText("Ground Beef"));
        closeSoftKeyboard();

        // typing best before input - this should get current day as best before
        onView(withId(R.id.bestBeforeInputEditText)).perform(clearText());
        onView(withId(R.id.bestBeforeInputEditText)).perform(click());
        onView(withText("OK")).perform(click());

        // typing category input
        onView(withId(R.id.categoryInputEditText)).perform(clearText());
        onView(withId(R.id.categoryInputEditText)).perform(typeText("Meat"));
        closeSoftKeyboard();

        // typing amount input
        onView(withId(R.id.amountInputEditText)).perform(clearText());
        onView(withId(R.id.amountInputEditText)).perform(typeText("5"));
        closeSoftKeyboard();

        // typing unit input
        onView(withId(R.id.unitInputEditText)).perform(clearText());
        onView(withId(R.id.unitInputEditText)).perform(typeText("lb"));
        closeSoftKeyboard();

        // typing location input
        onView(withId(R.id.locationInputEditText)).perform(clearText());
        onView(withId(R.id.locationInputEditText)).perform(typeText("Freezer"));
        closeSoftKeyboard();
    }

    /**
     * Checks whether the mock ingredient is present in the recyclerview.
     */
    private void checkIngredientPresent() throws InterruptedException {
        // finding the ingredient in the RecyclerView
        onView(withText("Ground Beef")).perform(click());

        // checking the correctness of the ingredient by seeing if all text is visible
        onView(withText("Ground Beef")).check(ViewAssertions
                .matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("Meat")).check(ViewAssertions
                .matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("5.0 lb")).check(ViewAssertions
                .matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("Freezer")).check(ViewAssertions
                .matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    /**
     * Performs all of the actions needed to add an ingredient.
     */
    private void addMockIngredient() throws InterruptedException {
        typeMockIngredient();

        // saving input
        onView(withId(R.id.ingredient_save_button)).perform(click());

        // todo: replace this with something else?
        // todo: this is here to ensure data actually is populated before testing
        Thread.sleep(2000);
    }

    /**
     * Tests adding a complete Ingredient.
     */
    @Test
    public void testAddIngredient() throws InterruptedException {
        // adding the mock ingredient
        addMockIngredient();

        // making sure it is visible
        checkIngredientPresent();

        // deleting the ingredient
        onView(withId(R.id.ingredient_delete_button)).perform(click());
        onView(withText("Delete")).perform(click());
    }

    /**
     * Tests adding an incomplete Ingredient.
     */
    @Test
    public void testIncompleteMessages() {
        typeMockIngredient();

        // testing the description error message
        onView(withId(R.id.descriptionInputEditText)).perform(clearText());
        onView(withText("Description is required")).check(ViewAssertions
                .matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withId(R.id.bestBeforeInputEditText)).perform(clearText());
        onView(withText("Best Before Date is required")).check(ViewAssertions
                .matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withId(R.id.categoryInputEditText)).perform(clearText());
        onView(withText("Category is required")).check(ViewAssertions
                .matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withId(R.id.unitInputEditText)).perform(clearText());
        onView(withText("Unit is required")).check(ViewAssertions
                .matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withId(R.id.locationInputEditText)).perform(clearText());
        onView(withText("Location is required")).check(ViewAssertions
                .matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

    }

    /**
     * Tests deleting an ingredient
     */
    @Test
    public void testDeleteIngredient() throws InterruptedException {
        addMockIngredient();

        // finding the ingredient in the RecyclerView
        onView(withText("Ground Beef")).perform(click());

        // deleting the ingredient
        onView(withId(R.id.ingredient_delete_button)).perform(click());
        onView(withText("Delete")).perform(click());

        onView(withText("Ground Beef")).check(doesNotExist());
    }

    /**
     * Tests editing an ingredient
     */
    @Test
    public void testEditIngredient() throws InterruptedException {
        addMockIngredient();

        onView(withText("Ground Beef")).perform(click());

        onView(withId(R.id.ingredient_edit_button)).perform(click());

        onView(withId(R.id.descriptionInputEditText)).perform(clearText());
        onView(withId(R.id.descriptionInputEditText)).perform(typeText("Ground Chicken"));
        closeSoftKeyboard();

        onView(withId(R.id.categoryInputEditText)).perform(clearText());
        onView(withId(R.id.categoryInputEditText)).perform(typeText("Poultry"));
        closeSoftKeyboard();

        // typing amount input
        onView(withId(R.id.amountInputEditText)).perform(clearText());
        onView(withId(R.id.amountInputEditText)).perform(typeText("6"));
        closeSoftKeyboard();

        // typing unit input
        onView(withId(R.id.unitInputEditText)).perform(clearText());
        onView(withId(R.id.unitInputEditText)).perform(typeText("kg"));
        closeSoftKeyboard();

        // typing location input
        onView(withId(R.id.locationInputEditText)).perform(clearText());
        onView(withId(R.id.locationInputEditText)).perform(typeText("Fridge"));
        closeSoftKeyboard();

        onView(withId(R.id.ingredient_save_button)).perform(click());
        // todo: replace this with something else?
        Thread.sleep(2000);

        // finding the ingredient in the RecyclerView
        onView(withText("Ground Chicken")).perform(click());

        // checking the correctness of the ingredient by seeing if all text is visible
        onView(withText("Ground Chicken")).check(ViewAssertions
                .matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("Poultry")).check(ViewAssertions
                .matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("6.0 kg")).check(ViewAssertions
                .matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("Fridge")).check(ViewAssertions
                .matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        // deleting the ingredient
        onView(withId(R.id.ingredient_delete_button)).perform(click());
        onView(withText("Delete")).perform(click());

    }

    /**
     * Tests exiting the edit process + ensuring a confirmation message appears
     */
    @Test
    public void testExitAddAndEdit() throws InterruptedException {
        // testing editing and then exiting
        typeMockIngredient();
        closeSoftKeyboard();
        pressBack();
        pressBack();

        // ensure the dialog shows up
        onView(withText("Quit")).check(ViewAssertions
                .matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("Cancel")).check(ViewAssertions
                .matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        // quitting
        onView(withText("Quit")).perform(click());
        onView(withText("Ground Beef")).check(doesNotExist());

        // cancelling and saving what we have
        typeMockIngredient();
        pressBack();
        pressBack();
        onView(withText("Cancel")).perform(click());
        onView(withId(R.id.ingredient_save_button)).perform(click());

        Thread.sleep(2000);
        checkIngredientPresent();

        // deleting the ingredient
        onView(withId(R.id.ingredient_delete_button)).perform(click());
        onView(withText("Delete")).perform(click());
    }
}
