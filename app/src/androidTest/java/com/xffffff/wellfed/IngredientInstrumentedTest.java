package com.xffffff.wellfed;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import junit.framework.AssertionFailedError;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test for Ingredients page, which will execute on an Android
 * device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class) public class IngredientInstrumentedTest {
    /**
     * Holds the ActivityScenarioRule
     */
    @Rule public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Setup Ingredient test by navigating to IngredientFragment
     */
    @Before public void before() {
        closeSoftKeyboard();
        onView(withId(R.id.ingredient_storage_item)).perform(click());
    }

    /**
     * Performs all of the actions needed to type a complete ingredient.
     */
    private void typeMockIngredient(String description) {
        // typing description input
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.descriptionInputEditText)).perform(clearText());
        onView(withId(R.id.descriptionInputEditText)).perform(
                typeText(description));
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
        onView(withId(R.id.unitInput)).perform(click());
        onView(withText("g"))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(ViewActions.click());

        // typing location input
        onView(withId(R.id.locationInputEditText)).perform(clearText());
        onView(withId(R.id.locationInputEditText)).perform(typeText("Freezer"));
        closeSoftKeyboard();
    }

    /**
     * Checks whether the mock ingredient is present in the recyclerview.
     */
    private void checkIngredientPresent(String description) {
        // finding the ingredient in the RecyclerView
        onView(withText(description)).perform(click());

        // checking the correctness of the ingredient by seeing if all text
        // is visible
        onView(withText(description)).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("Meat")).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("5.0 g")).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("Freezer")).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        // deleting the ingredient
        onView(withId(R.id.ingredient_delete_button)).perform(click());
        onView(withText("Delete")).perform(click());
    }

    /**
     * Performs all of the actions needed to add an ingredient.
     */
    private void addMockIngredient() throws InterruptedException {
        typeMockIngredient("Ground Beef");

        // saving input
        onView(withId(R.id.ingredient_save_button)).perform(click());

        // todo: replace this with something else?
        // todo: this is here to ensure data actually is populated before
        //  testing
        Thread.sleep(2000);
    }

    /**
     * Adds mock ingredient w/ specified fields.
     */
    private void addMockIngredient(String description, String category,
                                   String amount,
                                   String location) {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.descriptionInputEditText)).perform(click());
        onView(withId(R.id.descriptionInputEditText)).perform(
                typeText(description));
        closeSoftKeyboard();

        onView(withId(R.id.bestBeforeInputEditText)).perform(clearText());
        onView(withId(R.id.bestBeforeInputEditText)).perform(click());
        onView(withText("OK")).perform(click());

        onView(withId(R.id.categoryInputEditText)).perform(click());
        onView(withId(R.id.categoryInputEditText)).perform(typeText(category));
        closeSoftKeyboard();

        onView(withId(R.id.amountInputEditText)).perform(click());
        onView(withId(R.id.amountInputEditText)).perform(typeText(amount));
        closeSoftKeyboard();

        onView(withId(R.id.unitInput)).perform(click());
        onView(withText("count"))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());

        onView(withId(R.id.locationInputEditText)).perform(click());
        onView(withId(R.id.locationInputEditText)).perform(typeText(location));
        closeSoftKeyboard();

        onView(withId(R.id.ingredient_save_button)).perform(click());
    }

    /**
     * Adds 5 ingredients to test sort with.
     * Clean up these ingredients with {@link #delete5Ingredients()}
     */
    private void add5Ingredients() {
        // adding apple
        addMockIngredient("Apple", "Fruit", "5", "Fruit bowl");

        addMockIngredient("Banana", "Fruit", "4", "Fruit bowl");

        addMockIngredient("Ground Beef", "Meat", "5", "Freezer");

        addMockIngredient("Milk", "Dairy", "4", "Fridge");

        addMockIngredient("Cheese", "Dairy", "10", "Pantry");
    }

    /**
     * Deletes the 5 ingredients to clean up the list.
     * Should be called after {@link #add5Ingredients()}
     */
    private void delete5Ingredients() {
        onView(withId(R.id.ingredient_storage_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.ingredient_delete_button)).perform(click());
        onView(withText("Delete")).perform(click());

        onView(withId(R.id.ingredient_storage_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.ingredient_delete_button)).perform(click());
        onView(withText("Delete")).perform(click());

        onView(withId(R.id.ingredient_storage_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.ingredient_delete_button)).perform(click());
        onView(withText("Delete")).perform(click());

        onView(withId(R.id.ingredient_storage_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.ingredient_delete_button)).perform(click());
        onView(withText("Delete")).perform(click());

        onView(withId(R.id.ingredient_storage_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.ingredient_delete_button)).perform(click());
        onView(withText("Delete")).perform(click());
    }

    /**
     * Clicks on the sort button (for ingredients)
     */
    private void clickSortButton() {
        ViewInteraction materialButton =
                onView(allOf(withId(R.id.image_filter_button),
                        ChildAtPositionMatcher.childAtPosition(
                                allOf(withId(R.id.fragment_sort_container),
                                        ChildAtPositionMatcher.childAtPosition(
                                                withId(R.id.fragment_ingredient_storage),
                                                1)), 1), isDisplayed()));
        materialButton.perform(click());
    }

    /**
     * Asserts that a view is not visible.
     */
    private void assertTextNotVisible(String text) throws Exception {
        try {
            onView(withText(text)).check(ViewAssertions.matches(
                    withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
            throw new Exception("The view with text " + text +
                    " was present when it shouldn't be.");
        } catch (NoMatchingViewException e) {
            return;
        }
    }

    /**
     * Tests adding a complete Ingredient.
     */
    @Test public void testAddIngredient() throws InterruptedException {
        // adding the mock ingredient
        addMockIngredient();

        // making sure it is visible
        checkIngredientPresent("Ground Beef");
    }

    /**
     * Tests adding and viewing a list of multiple Ingredients
     */
    @Test public void testViewMultipleIngredients()
            throws InterruptedException {
        // adding the mock ingredient
        String ingredient1 = "Ground Beef";
        String ingredient2 = "Ground Chicken";
        String ingredient3 = "Whole Turkey";

        typeMockIngredient(ingredient1);
        onView(withId(R.id.ingredient_save_button)).perform(click());

        typeMockIngredient(ingredient2);
        onView(withId(R.id.ingredient_save_button)).perform(click());

        typeMockIngredient(ingredient3);
        onView(withId(R.id.ingredient_save_button)).perform(click());

        // making sure it is visible
        Thread.sleep(2000);
        onView(withText(ingredient1)).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText(ingredient2)).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText(ingredient3)).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        checkIngredientPresent(ingredient1);
        checkIngredientPresent(ingredient2);
        checkIngredientPresent(ingredient3);
    }

    /**
     * Tests adding an incomplete Ingredient.
     */
    @Test public void testIncompleteMessages() {
        typeMockIngredient("Ground Beef");

        // testing the description error message
        onView(withId(R.id.descriptionInputEditText)).perform(clearText());
        onView(withText("Description is required")).check(
                ViewAssertions.matches(withEffectiveVisibility(
                        ViewMatchers.Visibility.VISIBLE)));

        onView(withId(R.id.bestBeforeInputEditText)).perform(clearText());
        onView(withText("Best Before Date is required")).check(
                ViewAssertions.matches(withEffectiveVisibility(
                        ViewMatchers.Visibility.VISIBLE)));

        onView(withId(R.id.categoryInputEditText)).perform(clearText());
        onView(withText("Category is required")).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withId(R.id.unitInputEditText)).perform(clearText());
        onView(withText("Unit is required")).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withId(R.id.locationInputEditText)).perform(clearText());
        onView(withText("Location is required")).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

    }

    /**
     * Tests deleting an ingredient
     */
    @Test public void testDeleteIngredient() throws InterruptedException {
        addMockIngredient();

        // finding the ingredient in the RecyclerView
        onView(withText("Ground Beef")).perform(click());

        // deleting the ingredient
        onView(withId(R.id.ingredient_delete_button)).perform(click());
        onView(withText("Delete")).perform(click());

        onView(withText("Ground Beef")).check(doesNotExist());
    }

    /**
     * Tests that the delete confirmation message shows up
     */
    @Test public void testDeleteConfirmationMessage()
            throws InterruptedException {
        addMockIngredient();

        onView(withText("Ground Beef")).perform(click());

        // deleting the ingredient
        onView(withId(R.id.ingredient_delete_button)).perform(click());

        onView(withText("Delete")).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("Cancel")).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withText("Delete")).perform(click());
    }

    /**
     * Tests editing an ingredient
     */
    @Test public void testEditIngredient() throws InterruptedException {
        addMockIngredient();

        onView(withText("Ground Beef")).perform(click());

        onView(withId(R.id.ingredient_edit_button)).perform(click());

        onView(withId(R.id.descriptionInputEditText)).perform(clearText());
        onView(withId(R.id.descriptionInputEditText)).perform(
                typeText("Ground Chicken"));
        closeSoftKeyboard();

        onView(withId(R.id.categoryInputEditText)).perform(clearText());
        onView(withId(R.id.categoryInputEditText)).perform(typeText("Poultry"));
        closeSoftKeyboard();

        // typing amount input
        onView(withId(R.id.amountInputEditText)).perform(clearText());
        onView(withId(R.id.amountInputEditText)).perform(typeText("6"));
        closeSoftKeyboard();

        // typing unit input
        onView(withId(R.id.unitInput)).perform(click());
        onView(withText("g"))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());

        // typing location input
        onView(withId(R.id.locationInputEditText)).perform(clearText());
        onView(withId(R.id.locationInputEditText)).perform(typeText("Fridge"));
        closeSoftKeyboard();

        onView(withId(R.id.ingredient_save_button)).perform(click());
        // todo: replace this with something else?
        Thread.sleep(2000);

        // finding the ingredient in the RecyclerView
        onView(withText("Ground Chicken")).perform(click());

        // checking the correctness of the ingredient by seeing if all text
        // is visible
        onView(withText("Ground Chicken")).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("Poultry")).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("6.0 g")).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("Fridge")).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        // deleting the ingredient
        onView(withId(R.id.ingredient_delete_button)).perform(click());
        onView(withText("Delete")).perform(click());

    }

    /**
     * Tests that the cancel confirmation message shows up when adding
     */
    @Test public void testCancelConfirmationMessageAdd() {
        // testing adding and then exiting
        typeMockIngredient("Ground Beef");
        pressBack();
        pressBack();

        // ensure the dialog shows up
        onView(withText("Quit")).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("Cancel")).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    /**
     * Tests exiting the add process + ensuring a confirmation message appears
     */
    @Test public void testExitAdd() throws InterruptedException {
        // testing adding and then exiting
        typeMockIngredient("Ground Beef");
        pressBack();
        pressBack();

        // quitting
        onView(withText("Quit")).perform(click());
        onView(withText("Ground Beef")).check(doesNotExist());
    }

    /**
     * Tests cancelling the exit of the add process
     */
    @Test public void testCancelExitAdd() throws InterruptedException {
        // cancelling and saving what we have
        typeMockIngredient("Ground Beef");
        pressBack();
        pressBack();
        onView(withText("Cancel")).perform(click());
        onView(withId(R.id.ingredient_save_button)).perform(click());

        Thread.sleep(2000);
        checkIngredientPresent("Ground Beef");
    }

    /**
     * Tests that the cancel confirmation message shows up when editing
     * This test currently fails due to our app crashing when back button is
     * pressed.
     */
    @Test public void testCancelConfirmationMessageEdit()
            throws InterruptedException {
        // testing adding and then exiting
        addMockIngredient();
        onView(withText("Ground Beef")).perform(click());
        onView(withId(R.id.ingredient_edit_button)).perform(click());
        onView(withId(R.id.descriptionInputEditText)).perform(clearText());
        onView(withId(R.id.descriptionInputEditText)).perform(
                typeText("Ground Chicken"));
        closeSoftKeyboard();

        pressBack();

        // ensure the dialog shows up
        onView(withText("Quit")).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("Cancel")).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withText("Cancel")).perform(click());
        onView(withId(R.id.ingredient_save_button)).perform(click());

        Thread.sleep(2000);

        // deleting the ingredient
        onView(withText("Ground Chicken")).perform(click());
        onView(withId(R.id.ingredient_delete_button)).perform(click());
        onView(withText("Delete")).perform(click());
    }

    /**
     * Tests exiting the edit process + ensuring a confirmation message appears
     * This test currently fails due to our app crashing when back button is
     * pressed.
     */
    @Test public void testExitEdit() throws InterruptedException {
        // ensuring quit works, by editing our item and exiting
        addMockIngredient();
        onView(withText("Ground Beef")).perform(click());

        onView(withId(R.id.ingredient_edit_button)).perform(click());
        onView(withId(R.id.descriptionInputEditText)).perform(clearText());
        onView(withId(R.id.descriptionInputEditText)).perform(
                typeText("Ground Chicken"));
        closeSoftKeyboard();

        pressBack();

        // quitting and ensuring ground beef is the same
        onView(withText("Quit")).perform(click());
        onView(withText("Ground Beef")).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        checkIngredientPresent("Ground Beef");

    }

    /**
     * Tests cancelling an exit of an edit.
     * This test currently fails due to our app crashing when back button is
     * pressed.
     */
    @Test public void testCancelExitEdit() throws InterruptedException {
        addMockIngredient();
        onView(withText("Ground Beef")).perform(click());

        onView(withId(R.id.ingredient_edit_button)).perform(click());
        onView(withId(R.id.descriptionInputEditText)).perform(clearText());
        onView(withId(R.id.descriptionInputEditText)).perform(
                typeText("Ground Chicken"));
        closeSoftKeyboard();

        pressBack();

        // quitting and ensuring ground beef is the same
        onView(withText("Cancel")).perform(click());
        onView(withId(R.id.ingredient_save_button)).perform(click());
        Thread.sleep(2000);

        // checking the correctness of the ingredient by seeing if all text
        // is visible
        onView(withText("Ground Chicken")).perform(click());
        onView(withText("Ground Chicken")).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("Meat")).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("5.0 g")).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("Freezer")).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        // deleting the ingredient
        onView(withId(R.id.ingredient_delete_button)).perform(click());
        onView(withText("Delete")).perform(click());
    }

    /**
     * Tests sorting Ingredients by description.
     */
    @Test public void testSortByDescription() {
        // should be Apple, Banana, Cheese, Ground Beef, Milk
        add5Ingredients();
        // sort by description
        clickSortButton();
        onView(withText("description")).inRoot(isPlatformPopup())
                .perform(click());

        // check that stuff is sorted
        onView(withId(R.id.ingredient_storage_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.ingredient_name)).check(matches(withText("Apple")));
        pressBack();

        onView(withId(R.id.ingredient_storage_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.ingredient_name)).check(matches(withText("Banana")));
        pressBack();

        onView(withId(R.id.ingredient_storage_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(2, click()));
        onView(withId(R.id.ingredient_name)).check(matches(withText("Cheese")));
        pressBack();

        onView(withId(R.id.ingredient_storage_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(3, click()));
        onView(withId(R.id.ingredient_name)).check(
                matches(withText("Ground Beef")));
        pressBack();

        onView(withId(R.id.ingredient_storage_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(4, click()));
        onView(withId(R.id.ingredient_name)).check(matches(withText("Milk")));
        pressBack();

        delete5Ingredients();
    }

    /**
     * Tests sorting Ingredients by category.
     */
    @Test public void testSortByCategory() {
        // should be Apple, Banana, Cheese, Ground Beef, Milk
        add5Ingredients();
        // sort by description
        clickSortButton();
        onView(withText("category")).inRoot(isPlatformPopup()).perform(click());

        // check that stuff is sorted
        onView(withId(R.id.ingredient_storage_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        // check for milk or cheese in either order
        try {
            onView(withId(R.id.ingredient_name)).check(
                    matches(withText("Milk")));
        } catch (AssertionFailedError e) {
            onView(withId(R.id.ingredient_name)).check(
                    matches(withText("Cheese")));
        }
        pressBack();

        onView(withId(R.id.ingredient_storage_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, click()));
        try {
            onView(withId(R.id.ingredient_name)).check(
                    matches(withText("Milk")));
        } catch (AssertionFailedError e) {
            onView(withId(R.id.ingredient_name)).check(
                    matches(withText("Cheese")));
        }
        pressBack();

        // check for banana or apple in either order
        onView(withId(R.id.ingredient_storage_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(2, click()));
        try {
            onView(withId(R.id.ingredient_name)).check(
                    matches(withText("Banana")));
        } catch (AssertionFailedError e) {
            onView(withId(R.id.ingredient_name)).check(
                    matches(withText("Apple")));
        }
        pressBack();

        onView(withId(R.id.ingredient_storage_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(3, click()));
        try {
            onView(withId(R.id.ingredient_name)).check(
                    matches(withText("Banana")));
        } catch (AssertionFailedError e) {
            onView(withId(R.id.ingredient_name)).check(
                    matches(withText("Apple")));
        }
        pressBack();

        onView(withId(R.id.ingredient_storage_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(4, click()));
        onView(withId(R.id.ingredient_name)).check(
                matches(withText("Ground Beef")));
        pressBack();

        delete5Ingredients();
    }

    /**
     * Tests sorting Ingredients by location.
     */
    @Test public void testSortByLocation() throws InterruptedException {
        // should be Apple, Banana, Cheese, Ground Beef, Milk
        add5Ingredients();
        // sort by description
        clickSortButton();
        onView(withText("location")).inRoot(isPlatformPopup()).perform(click());

        // check that stuff is sorted
        onView(withId(R.id.ingredient_storage_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.ingredient_name)).check(
                matches(withText("Ground Beef")));
        pressBack();

        onView(withId(R.id.ingredient_storage_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.ingredient_name)).check(matches(withText("Milk")));
        pressBack();

        // check for banana or apple in either order
        onView(withId(R.id.ingredient_storage_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(2, click()));
        try {
            onView(withId(R.id.ingredient_name)).check(
                    matches(withText("Banana")));
        } catch (AssertionFailedError e) {
            onView(withId(R.id.ingredient_name)).check(
                    matches(withText("Apple")));
        }
        pressBack();

        onView(withId(R.id.ingredient_storage_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(3, click()));
        try {
            onView(withId(R.id.ingredient_name)).check(
                    matches(withText("Banana")));
        } catch (AssertionFailedError e) {
            onView(withId(R.id.ingredient_name)).check(
                    matches(withText("Apple")));
        }
        pressBack();

        onView(withId(R.id.ingredient_storage_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(4, click()));
        onView(withId(R.id.ingredient_name)).check(matches(withText("Cheese")));
        pressBack();

        delete5Ingredients();
    }

    /**
     * Tests searching for a nonexisting Ingredient.
     */
    @Test public void testSearchNonexistent() throws Exception {
        add5Ingredients();

        // search for something not there
        onView(withId(R.id.search_text)).perform(click());
        onView(withId(R.id.search_text)).perform(typeText("X"));
        closeSoftKeyboard();
        Thread.sleep(1000);

        // assert none of the ingredients are visible
        assertTextNotVisible("Apple");
        assertTextNotVisible("Banana");
        assertTextNotVisible("Ground Beef");
        assertTextNotVisible("Milk");
        assertTextNotVisible("Cheese");

        onView(withId(R.id.search_text)).perform(clearText());
        delete5Ingredients();
    }

    /**
     * Tests searching and then clearing the search.
     */
    @Test public void testClearText() throws Exception {
        add5Ingredients();

        // search for something not there
        onView(withId(R.id.search_text)).perform(click());
        onView(withId(R.id.search_text)).perform(typeText("X"));
        closeSoftKeyboard();
        Thread.sleep(1000);

        // assert none of the ingredients are visible
        assertTextNotVisible("Apple");
        assertTextNotVisible("Banana");
        assertTextNotVisible("Ground Beef");
        assertTextNotVisible("Milk");
        assertTextNotVisible("Cheese");

        onView(withId(R.id.search_text)).perform(clearText());

        Thread.sleep(1000);
        onView(withText("Apple")).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("Banana")).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("Ground Beef")).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("Milk")).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("Cheese")).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        delete5Ingredients();
    }

    /**
     * Tests searching for the whole string in the description
     */
    @Test public void testWholeSearch() throws Exception {
        add5Ingredients();

        onView(withId(R.id.search_text)).perform(click());
        onView(withId(R.id.search_text)).perform(typeText("banana"));
        closeSoftKeyboard();
        Thread.sleep(1000);

        assertTextNotVisible("Apple");
        assertTextNotVisible("Ground Beef");
        assertTextNotVisible("Milk");
        assertTextNotVisible("Cheese");
        onView(withText("Banana")).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(withId(R.id.search_text)).perform(clearText());
        delete5Ingredients();
    }

    /**
     * Tests whether search is case-insensitive
     */
    @Test public void testCaseInsensitivity() throws Exception {
        add5Ingredients();

        onView(withId(R.id.search_text)).perform(click());
        onView(withId(R.id.search_text)).perform(typeText("b"));
        closeSoftKeyboard();
        Thread.sleep(1000);

        assertTextNotVisible("Apple");
        assertTextNotVisible("Milk");
        assertTextNotVisible("Cheese");
        onView(withText("Banana")).check(ViewAssertions.matches(
                withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        assertTextNotVisible("Ground Beef");

        onView(withId(R.id.search_text)).perform(clearText());
        delete5Ingredients();
    }
}
