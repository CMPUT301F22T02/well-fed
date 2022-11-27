package com.xffffff.wellfed;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.xffffff.wellfed.recipe.RecipeActivity;
import com.xffffff.wellfed.recipe.RecipeEditActivity;
import com.xffffff.wellfed.recipe.RecipeIngredientEditActivity;

import junit.framework.AssertionFailedError;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test for Recipes, which will execute on an Android device.
 *
 * Intended device: Pixel 6 Pro API 22
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class) public class RecipeInstrumentedTest {
    /**
     * Holds the ActivityScenarioRule
     */
    @Rule public ActivityScenarioRule<com.xffffff.wellfed.MainActivity>
            activityRule =
            new ActivityScenarioRule<>(com.xffffff.wellfed.MainActivity.class);
    /**
     * Holds the max timeout for Thread.sleep
     */
    int timeout = 2000;

    /**
     * Setup Recipe test by navigating to RecipeBookFragment
     */
    @Before public void before() {
        onView(withId(R.id.recipe_book_item)).perform(click());

        Intents.init();
    }

    /**
     * Adds an ingredient to the ingredient storage.
     * This is used to test adding existing ingredients to recipe.
     */
    public void addPreexistingIngredient(String description) throws InterruptedException {
        // pre-add storage ingredient
        onView(withId(R.id.ingredient_storage_item)).perform(click());

        // typing description input
        onView(withId(R.id.fab)).perform(click());
        Thread.sleep(timeout);
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
        onView(withId(R.id.categoryInputEditText)).perform(typeText("Bread"));
        closeSoftKeyboard();

        // typing amount input
        onView(withId(R.id.amountInputEditText)).perform(clearText());
        onView(withId(R.id.amountInputEditText)).perform(typeText("5"));
        closeSoftKeyboard();

        // typing unit input
        onView(withId(R.id.unitInput)).perform(click());
        onView(withText("count"))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());

        // typing location input
        onView(withId(R.id.locationInputEditText)).perform(clearText());
        onView(withId(R.id.locationInputEditText)).perform(
                typeText("Breadbox"));
        closeSoftKeyboard();

        onView(withId(R.id.ingredient_save_button)).perform(click());

        onView(withId(R.id.recipe_book_item)).perform(click());
    }

    /**
     * Clicks on the sort button (for recipes.
     */
    private void clickSortButton() {
        ViewInteraction materialButton =
                onView(allOf(withId(R.id.image_filter_button),
                        ChildAtPositionMatcher.childAtPosition(
                                allOf(withId(R.id.fragment_sort_container2),
                                        ChildAtPositionMatcher.childAtPosition(
                                                withId(R.id.fragment_recipe_book),
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
     * Types out a mock recipe.
     *
     * @throws InterruptedException
     */
    public void typeMockRecipe(String description) throws InterruptedException {
        onView(withId(R.id.fab)).perform(click());
        Thread.sleep(timeout);

        onView(withId(R.id.edit_recipe_title)).perform(typeText(description));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_prep_time_textEdit)).perform(typeText("5"));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_no_of_servings_textEdit)).perform(
                typeText("1"));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_category)).perform(click());
        closeSoftKeyboard();

        onView(withText("Breakfast")).inRoot(isPlatformPopup())
                .perform(click());
        closeSoftKeyboard();

        onView(withId(R.id.commentsEditText)).perform(
                typeText("This breakfast is great for on the go."));
        closeSoftKeyboard();
    }

    /**
     * Adds a mock ingredient to a recipe.
     */
    public void addMockIngredient(String description) throws InterruptedException {
        //add an ingredient
        onView(withId(R.id.addButton)).perform(click());
        Thread.sleep(timeout);

        onView(withId(R.id.edit_descriptionInput)).perform(
                typeText(description));
        closeSoftKeyboard();

        onView(withId(R.id.categoryInput)).perform(click());
        onView(withText("Protein")).inRoot(isPlatformPopup()).perform(click());
        closeSoftKeyboard();

        onView(withId(R.id.edit_amountInput)).perform(typeText("1"));
        closeSoftKeyboard();

        onView(withId(R.id.unitInput)).perform(click());
        onView(withText("count"))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());

        closeSoftKeyboard();
        onView(withId(R.id.ingredient_save_button)).perform(click());
    }

    /**
     * Adds 5 recipes to the recipe list.
     * Clean them up after with {@link #cleanup5Recipes()}
     */
    private void add5Recipes() throws InterruptedException {
        // adding a ton of recipes
        onView(withId(R.id.fab)).perform(click());
        Thread.sleep(timeout);
        onView(withId(R.id.edit_recipe_title)).perform(typeText("Apple pie"));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_prep_time_textEdit)).perform(typeText("45"));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_no_of_servings_textEdit)).perform(
                typeText("5"));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_category_textEdit)).perform(
                typeText("Dessert"));
        closeSoftKeyboard();

        onView(withId(R.id.commentsEditText)).perform(
                typeText("Basic apple pie."));
        closeSoftKeyboard();

        addMockIngredient("Apple");
        onView(withId(R.id.save_fab)).perform(click());

        // adding recipe 2
        onView(withId(R.id.fab)).perform(click());
        Thread.sleep(timeout);
        onView(withId(R.id.edit_recipe_title)).perform(typeText("Apple puree"));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_prep_time_textEdit)).perform(typeText("10"));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_no_of_servings_textEdit)).perform(
                typeText("3"));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_category_textEdit)).perform(typeText("Side"));
        closeSoftKeyboard();

        onView(withId(R.id.commentsEditText)).perform(
                typeText("Puree as a side dish."));
        closeSoftKeyboard();

        addMockIngredient("Apple");

        onView(withId(R.id.save_fab)).perform(click());

        // adding recipe 3
        onView(withId(R.id.fab)).perform(click());
        Thread.sleep(timeout);
        onView(withId(R.id.edit_recipe_title)).perform(
                typeText("Bananas foster"));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_prep_time_textEdit)).perform(typeText("30"));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_no_of_servings_textEdit)).perform(
                typeText("4"));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_category_textEdit)).perform(
                typeText("Dessert"));
        closeSoftKeyboard();

        onView(withId(R.id.commentsEditText)).perform(
                typeText("Delicious dessert."));
        closeSoftKeyboard();

        addMockIngredient("Banana");

        onView(withId(R.id.save_fab)).perform(click());

        // adding recipe 4
        onView(withId(R.id.fab)).perform(click());
        Thread.sleep(timeout);
        onView(withId(R.id.edit_recipe_title)).perform(typeText("Ice cream"));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_prep_time_textEdit)).perform(typeText("120"));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_no_of_servings_textEdit)).perform(
                typeText("3"));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_category_textEdit)).perform(
                typeText("Small dessert"));
        closeSoftKeyboard();

        onView(withId(R.id.commentsEditText)).perform(
                typeText("Cold dessert."));
        closeSoftKeyboard();

        addMockIngredient("Milk");

        onView(withId(R.id.save_fab)).perform(click());

        // adding recipe 5
        onView(withId(R.id.fab)).perform(click());
        Thread.sleep(timeout);
        onView(withId(R.id.edit_recipe_title)).perform(typeText("Cereal"));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_prep_time_textEdit)).perform(typeText("5"));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_no_of_servings_textEdit)).perform(
                typeText("1"));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_category_textEdit)).perform(
                typeText("Breakfast"));
        closeSoftKeyboard();

        onView(withId(R.id.commentsEditText)).perform(
                typeText("Quick and easy."));
        closeSoftKeyboard();

        addMockIngredient("Milk");

        onView(withId(R.id.save_fab)).perform(click());
    }

    /**
     * Cleans up the 5 recipes made in {@link #add5Recipes()}
     */
    private void cleanup5Recipes() throws InterruptedException {

        for (int i = 0; i < 5; i++) {
            Thread.sleep(timeout);
            onView(withId(R.id.recipe_rv)).perform(
                    RecyclerViewActions.actionOnItemAtPosition(0, click()));
            onView(withId(R.id.recipe_delete_btn)).perform(click());
            onView(withText("Delete")).perform(click());
        }
    }

    /**
     * Test adding a full recipe with 2 ingredients (one searched, one added)
     * and deleting a recipe
     */
    @Test public void testAddAndDeleteRecipe() throws InterruptedException {
        addPreexistingIngredient("Tortilla");
        typeMockRecipe("Egg Wrap");

        onView(withId(R.id.searchButton)).perform(click());
        //pick an ingredient check if recycler view is non empty
        Thread.sleep(timeout);
        onView(withId(R.id.ingredient_storage_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(withId(R.id.edit_amountInput)).perform(typeText("1"));
        closeSoftKeyboard();

        onView(withId(R.id.unitInput)).perform(click());
        onView(withText("count"))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());

        onView(withId(R.id.ingredient_save_button)).perform(click());

        addMockIngredient("Egg");


        onView(withId(R.id.save_fab)).perform(click());


        Thread.sleep(timeout);

        onView(withText("Egg Wrap")).perform(click());

        onView(withId(R.id.recipe_delete_btn)).perform(click());
        onView(withText("Delete")).perform(click());



        // checking deleted recipe does not exist
        Thread.sleep(timeout);
        onView(withText("Egg Wrap")).check(doesNotExist());
    }


    @Test public void testAddOnInvalidRecipe() throws InterruptedException {
        addPreexistingIngredient("Tortilla");

        typeMockRecipe("Egg Wrap");

        //Try to add without ingredients
        onView(withId(R.id.save_fab)).perform(click());
        intended(hasComponent(RecipeEditActivity.class.getName()));

        onView(withId(R.id.searchButton)).perform(click());
        Thread.sleep(timeout);
        //pick an ingredient check if recycler view is non empty
        onView(withId(R.id.ingredient_storage_list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        intended(hasComponent(RecipeIngredientEditActivity.class.getName()));
        onView(withId(R.id.edit_amountInput)).perform(typeText("1"));

        closeSoftKeyboard();
        onView(withId(R.id.unitInput)).perform(click());
        onView(withText("g"))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        closeSoftKeyboard();
        onView(withId(R.id.ingredient_save_button)).perform(click());

        addMockIngredient("Egg");

        //Try to add when title is removed
        onView(withId(R.id.edit_recipe_title)).perform(clearText());

        intended(hasComponent(RecipeEditActivity.class.getName()));

        //Test for no prep time
        onView(withId(R.id.recipe_prep_time_textEdit)).perform(clearText());
        closeSoftKeyboard();
        onView(withId(R.id.edit_recipe_title)).perform(typeText("Egg Wrap"));
        closeSoftKeyboard();

        onView(withId(R.id.save_fab)).perform(click());
        Thread.sleep(timeout);
        intended(hasComponent(RecipeEditActivity.class.getName()));

        onView(withId(R.id.recipe_no_of_servings_textEdit)).perform(
                clearText());
        closeSoftKeyboard();
        onView(withId(R.id.recipe_prep_time_textEdit)).perform(typeText("5"));
        closeSoftKeyboard();

        onView(withId(R.id.save_fab)).perform(click());
        Thread.sleep(timeout);
        intended(hasComponent(RecipeEditActivity.class.getName()));

        onView(withId(R.id.recipe_no_of_servings_textEdit)).perform(
                typeText("1"));
        closeSoftKeyboard();
        onView(withId(R.id.commentsEditText)).perform(clearText());

        intended(hasComponent(RecipeEditActivity.class.getName()));

        onView(withId(R.id.commentsEditText)).perform(
                typeText("This is perfect for on the go."));
    }

    /**
     * Test viewing a single recipe
     */
    @Test public void TestViewingRecipe() throws InterruptedException {

        typeMockRecipe("Egg Wrap");

        addMockIngredient("Egg");

        onView(withId(R.id.save_fab)).perform(click());

        Thread.sleep(timeout);

        onView(withText("Egg Wrap")).perform(click());


        intended(hasComponent(RecipeActivity.class.getName()));

        Thread.sleep(timeout);
        onView(withId(R.id.recipe_title_textView)).check(
                matches(withText("Egg Wrap")));
        onView(withId(R.id.recipe_prep_time_textView)).check(
                matches(withText("5 mins")));
        onView(withId(R.id.recipe_no_of_servings_textView)).check(
                matches(withText("Servings: 1")));
        onView(withId(R.id.recipe_category)).check(
                matches(withText("Breakfast")));
        onView(withId(R.id.recipe_description_textView)).check(
                matches(withText("This breakfast is great for on the go.")));
        onView(withId(R.id.recipe_ingredient_recycleViewer)).check(
                matches(hasDescendant(withText("1.00 count"))));
        onView(withId(R.id.recipe_ingredient_recycleViewer)).check(
                matches(hasDescendant(withText("Egg | Protein"))));

        onView(withId(R.id.recipe_delete_btn)).perform(click());
        onView(withText("Delete")).perform(click());

    }

    /**
     * Test deleting an added Ingredient from recipe
     */
    @Test public void testDelIngredientFromRecipe()
            throws InterruptedException {
        typeMockRecipe("Egg Wrap");

        addMockIngredient("Egg");

        //add separate ingredient
        onView(withId(R.id.addButton)).perform(click());
        Thread.sleep(timeout);
        onView(withId(R.id.edit_descriptionInput)).perform(
                typeText("Chicken Breast"));
        closeSoftKeyboard();

        onView(withId(R.id.categoryInput)).perform(click());
        onView(withText("Protein")).inRoot(isPlatformPopup()).perform(click());
        closeSoftKeyboard();

        onView(withId(R.id.edit_amountInput)).perform(typeText("2"));
        closeSoftKeyboard();

        onView(withId(R.id.unitInput)).perform(click());
        onView(withText("g"))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());

        closeSoftKeyboard();
        onView(withId(R.id.ingredient_save_button)).perform(click());

        // deleting egg
        onView(allOf(isDisplayed(), withId(R.id.recyclerView))).perform(
                RecyclerViewActions.actionOnItemAtPosition(0,
                        RecyclerViewClickViewAction.clickChildViewWithId(
                                R.id.deleteButton)));

        // confirming delete
        onView(withText("Delete")).perform(click());

        // asserting that chicken breast is still there with correct details
        onView(allOf(isDisplayed(), withId(R.id.recyclerView))).check(
                matches(hasDescendant(withText("Chicken Breast"))));
        onView(allOf(isDisplayed(), withId(R.id.recyclerView))).check(
                matches(hasDescendant(withText("2.00 g"))));

        // asserting that egg is NOT there
        Thread.sleep(timeout);
        onView(withText("Egg")).check(ViewAssertions.doesNotExist());
        onView(withText("1.00 count")).check(ViewAssertions.doesNotExist());
    }

    /**
     * Test editing the details of a Recipe
     * TODO: deal with thread.sleep?
     */
    @Test public void testEditingARecipe() throws InterruptedException {
        typeMockRecipe("Egg Wrap");

        addMockIngredient("Egg");

        onView(withId(R.id.save_fab)).perform(click());

        Thread.sleep(timeout);
        onView(withId(R.id.recipe_rv)).perform(RecyclerViewActions.actionOnItem(
                hasDescendant(withText("Egg Wrap")), click()));

        // press edit button
        onView(withId(R.id.save_fab)).perform(click());
        Thread.sleep(timeout);
        // test editing each field individually
        onView(withId(R.id.edit_recipe_title)).perform(clearText());
        onView(withId(R.id.edit_recipe_title)).perform(
                typeText("Egg Sandwich"));
        closeSoftKeyboard();

        onView(withId(R.id.save_fab)).perform(click());

        Thread.sleep(timeout);
        // check edit
        onView(withId(R.id.recipe_rv)).perform(RecyclerViewActions.actionOnItem(
                hasDescendant(withText("Egg Sandwich")), click()));

        Thread.sleep(timeout);
        onView(withId(R.id.recipe_title_textView)).check(
                matches(withText("Egg Sandwich")));
        onView(withId(R.id.recipe_prep_time_textView)).check(
                matches(withText("5 mins")));
        onView(withId(R.id.recipe_no_of_servings_textView)).check(
                matches(withText("Servings: 1")));
        onView(withId(R.id.recipe_category)).check(
                matches(withText("Breakfast")));
        onView(withId(R.id.recipe_description_textView)).check(
                matches(withText("This breakfast is great for on the go.")));
        onView(withId(R.id.recipe_ingredient_recycleViewer)).check(
                matches(hasDescendant(withText("1.00 count"))));
        onView(withId(R.id.recipe_ingredient_recycleViewer)).check(
                matches(hasDescendant(withText("Egg | Protein"))));

        // press edit button
        onView(withId(R.id.save_fab)).perform(click());
        Thread.sleep(timeout);

        // test editing each field individually
        onView(withId(R.id.recipe_prep_time_textEdit)).perform(clearText());
        onView(withId(R.id.recipe_prep_time_textEdit)).perform(typeText("7"));
        closeSoftKeyboard();

        onView(withId(R.id.save_fab)).perform(click());

        Thread.sleep(timeout);
        // check edit
        onView(withId(R.id.recipe_rv)).perform(RecyclerViewActions.actionOnItem(
                hasDescendant(withText("Egg Sandwich")), click()));

        Thread.sleep(timeout);
        onView(withId(R.id.recipe_title_textView)).check(
                matches(withText("Egg Sandwich")));
        onView(withId(R.id.recipe_prep_time_textView)).check(
                matches(withText("7 mins")));
        onView(withId(R.id.recipe_no_of_servings_textView)).check(
                matches(withText("Servings: 1")));
        onView(withId(R.id.recipe_category)).check(
                matches(withText("Breakfast")));
        onView(withId(R.id.recipe_description_textView)).check(
                matches(withText("This breakfast is great for on the go.")));
        onView(withId(R.id.recipe_ingredient_recycleViewer)).check(
                matches(hasDescendant(withText("1.00 count"))));
        onView(withId(R.id.recipe_ingredient_recycleViewer)).check(
                matches(hasDescendant(withText("Egg | Protein"))));

        // press edit button
        onView(withId(R.id.save_fab)).perform(click());
        Thread.sleep(timeout);

        // test editing each field individually
        onView(withId(R.id.recipe_no_of_servings_textEdit)).perform(
                clearText());
        onView(withId(R.id.recipe_no_of_servings_textEdit)).perform(
                typeText("2"));
        closeSoftKeyboard();

        onView(withId(R.id.save_fab)).perform(click());

        // check edit
        Thread.sleep(timeout);
        onView(withId(R.id.recipe_rv)).perform(RecyclerViewActions.actionOnItem(
                hasDescendant(withText("Egg Sandwich")), click()));

        Thread.sleep(timeout);
        onView(withId(R.id.recipe_title_textView)).check(
                matches(withText("Egg Sandwich")));
        onView(withId(R.id.recipe_prep_time_textView)).check(
                matches(withText("7 mins")));
        onView(withId(R.id.recipe_no_of_servings_textView)).check(
                matches(withText("Servings: 2")));
        onView(withId(R.id.recipe_category)).check(
                matches(withText("Breakfast")));
        onView(withId(R.id.recipe_description_textView)).check(
                matches(withText("This breakfast is great for on the go.")));
        onView(withId(R.id.recipe_ingredient_recycleViewer)).check(
                matches(hasDescendant(withText("1.00 count"))));
        onView(withId(R.id.recipe_ingredient_recycleViewer)).check(
                matches(hasDescendant(withText("Egg | Protein"))));

        // press edit button
        onView(withId(R.id.save_fab)).perform(click());
        Thread.sleep(timeout);

        // test editing each field individually
        onView(withId(R.id.recipe_category_textEdit)).perform(clearText());
        onView(withId(R.id.recipe_category_textEdit)).perform(
                typeText("Lunch"));
        closeSoftKeyboard();

        onView(withId(R.id.save_fab)).perform(click());

        // check edit
        Thread.sleep(timeout);
        onView(withId(R.id.recipe_rv)).perform(RecyclerViewActions.actionOnItem(
                hasDescendant(withText("Egg Sandwich")), click()));

        Thread.sleep(timeout);
        onView(withId(R.id.recipe_title_textView)).check(
                matches(withText("Egg Sandwich")));
        onView(withId(R.id.recipe_prep_time_textView)).check(
                matches(withText("7 mins")));
        onView(withId(R.id.recipe_no_of_servings_textView)).check(
                matches(withText("Servings: 2")));
        onView(withId(R.id.recipe_category)).check(matches(withText("Lunch")));
        onView(withId(R.id.recipe_description_textView)).check(
                matches(withText("This breakfast is great for on the go.")));
        onView(withId(R.id.recipe_ingredient_recycleViewer)).check(
                matches(hasDescendant(withText("1.00 count"))));
        onView(withId(R.id.recipe_ingredient_recycleViewer)).check(
                matches(hasDescendant(withText("Egg | Protein"))));

        // press edit button
        onView(withId(R.id.save_fab)).perform(click());
        Thread.sleep(timeout);

        // test editing each field individually
        onView(withId(R.id.commentsEditText)).perform(clearText());
        onView(withId(R.id.commentsEditText)).perform(
                typeText("This lunch is filling and delicious."));
        closeSoftKeyboard();

        onView(withId(R.id.save_fab)).perform(click());

        // check edit
        Thread.sleep(timeout);
        onView(withId(R.id.recipe_rv)).perform(RecyclerViewActions.actionOnItem(
                hasDescendant(withText("Egg Sandwich")), click()));

        Thread.sleep(timeout);
        onView(withId(R.id.recipe_title_textView)).check(
                matches(withText("Egg Sandwich")));
        onView(withId(R.id.recipe_prep_time_textView)).check(
                matches(withText("7 mins")));
        onView(withId(R.id.recipe_no_of_servings_textView)).check(
                matches(withText("Servings: 2")));
        onView(withId(R.id.recipe_category)).check(matches(withText("Lunch")));
        onView(withId(R.id.recipe_description_textView)).check(
                matches(withText("This lunch is filling and delicious.")));
        onView(withId(R.id.recipe_ingredient_recycleViewer)).check(
                matches(hasDescendant(withText("1.00 count"))));
        onView(withId(R.id.recipe_ingredient_recycleViewer)).check(
                matches(hasDescendant(withText("Egg | Protein"))));

        onView(withId(R.id.recipe_delete_btn)).perform(click());
        onView(withText("Delete")).perform(click());
    }

    /**
     * Test editing the details of an ingredient in a Recipe
     * is added
     */
    @Test public void testEditingIngredientOfARecipe()
            throws InterruptedException {
        typeMockRecipe("Egg Wrap");
        addMockIngredient("Egg");

        // editing the ingredient
        onView(withId(R.id.recyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0,
                        RecyclerViewClickViewAction.clickChildViewWithId(
                                R.id.editButton)));

        // change all of the fields
        onView(withId(R.id.edit_descriptionInput)).perform(clearText());
        onView(withId(R.id.edit_descriptionInput)).perform(
                typeText("Duck Egg"));
        closeSoftKeyboard();

        onView(withId(R.id.edit_categoryInput)).perform(clearText());
        onView(withId(R.id.edit_categoryInput)).perform(typeText("Vegetarian"));
        closeSoftKeyboard();

        onView(withId(R.id.edit_amountInput)).perform(clearText());
        onView(withId(R.id.edit_amountInput)).perform(typeText("3"));
        closeSoftKeyboard();

        onView(withId(R.id.unitInput)).perform(click());
        onView(withText("count"))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());

        closeSoftKeyboard();
        onView(withId(R.id.ingredient_save_button)).perform(click());

        // check the ingredient is changed
        Thread.sleep(timeout);
        onView(withId(R.id.recyclerView)).check(
                matches(hasDescendant(withText("3.00 count"))));
        onView(withId(R.id.recyclerView)).check(
                matches(hasDescendant(withText("Duck Egg"))));

    }

    /**
     * Test adding a handful of recipes, and viewing the list of all of them
     */
    @Test public void viewListOfRecipes() throws InterruptedException {
        typeMockRecipe("Egg Wrap");
        addMockIngredient("Egg");
        onView(withId(R.id.save_fab)).perform(click());
        typeMockRecipe("Egg Salad");
        addMockIngredient("Egg");
        onView(withId(R.id.save_fab)).perform(click());
        typeMockRecipe("Tacos");
        addMockIngredient("Ground beef");
        onView(withId(R.id.save_fab)).perform(click());
        typeMockRecipe("Chicken pot pie");
        addMockIngredient("Chicken");
        onView(withId(R.id.save_fab)).perform(click());
        typeMockRecipe("Greek Salad");
        addMockIngredient("Tomato");
        onView(withId(R.id.save_fab)).perform(click());


        // check whether all of these recipes are present (and in order!)
        onView(withId(R.id.recipe_rv)).perform(RecyclerViewActions.actionOnItem(
                hasDescendant(withText("Egg Wrap")), click()));
        Thread.sleep(timeout);
        onView(withId(R.id.recipe_title_textView)).check(
                matches(withText("Egg Wrap")));
        onView(withId(R.id.recipe_delete_btn)).perform(click());
        onView(withText("Delete")).perform(click());

        onView(withId(R.id.recipe_rv)).perform(RecyclerViewActions.actionOnItem(
                hasDescendant(withText("Egg Salad")), click()));
        Thread.sleep(timeout);
        onView(withId(R.id.recipe_title_textView)).check(
                matches(withText("Egg Salad")));
        onView(withId(R.id.recipe_delete_btn)).perform(click());
        onView(withText("Delete")).perform(click());

        onView(withId(R.id.recipe_rv)).perform(RecyclerViewActions.actionOnItem(
                hasDescendant(withText("Tacos")), click()));
        Thread.sleep(timeout);
        onView(withId(R.id.recipe_title_textView)).check(
                matches(withText("Tacos")));
        onView(withId(R.id.recipe_delete_btn)).perform(click());
        onView(withText("Delete")).perform(click());

        onView(withId(R.id.recipe_rv)).perform(RecyclerViewActions.actionOnItem(
                hasDescendant(withText("Chicken pot pie")), click()));
        Thread.sleep(timeout);
        onView(withId(R.id.recipe_title_textView)).check(
                matches(withText("Chicken pot pie")));
        onView(withId(R.id.recipe_delete_btn)).perform(click());
        onView(withText("Delete")).perform(click());

        onView(withId(R.id.recipe_rv)).perform(RecyclerViewActions.actionOnItem(
                hasDescendant(withText("Greek Salad")), click()));
        Thread.sleep(timeout);
        onView(withId(R.id.recipe_title_textView)).check(
                matches(withText("Greek Salad")));
        onView(withId(R.id.recipe_delete_btn)).perform(click());
        onView(withText("Delete")).perform(click());
    }

    /**
     * Tests sorting recipes by title, prep time, category, and servings.
     * Note: These were refactored into one test, instead of many tests, one for each category.
     * The reason for this was because adding and deleting the data took a very long time.
     */
    @Test public void testSort() throws InterruptedException {
        add5Recipes();
        /*
        Sort by title.
         */
        clickSortButton();
        onView(withText("Title")).inRoot(isPlatformPopup()).perform(click());

        // check order of items
        onView(withId(R.id.recipe_rv)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Thread.sleep(timeout);
        onView(withId(R.id.recipe_title_textView)).check(
                matches(withText("Apple pie")));
        pressBack();

        onView(withId(R.id.recipe_rv)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, click()));
        Thread.sleep(timeout);
        onView(withId(R.id.recipe_title_textView)).check(
                matches(withText("Apple puree")));
        pressBack();

        onView(withId(R.id.recipe_rv)).perform(
                RecyclerViewActions.actionOnItemAtPosition(2, click()));
        Thread.sleep(timeout);
        onView(withId(R.id.recipe_title_textView)).check(
                matches(withText("Bananas foster")));
        pressBack();

        onView(withId(R.id.recipe_rv)).perform(
                RecyclerViewActions.actionOnItemAtPosition(3, click()));
        Thread.sleep(timeout);
        onView(withId(R.id.recipe_title_textView)).check(
                matches(withText("Cereal")));
        pressBack();

        onView(withId(R.id.recipe_rv)).perform(
                RecyclerViewActions.actionOnItemAtPosition(4, click()));
        Thread.sleep(timeout);
        onView(withId(R.id.recipe_title_textView)).check(
                matches(withText("Ice cream")));
        pressBack();

        /*
        Sort by prep time.
         */
        clickSortButton();
        onView(withText("Preparation Time")).inRoot(isPlatformPopup())
                .perform(click());

        // check order of items
        onView(withId(R.id.recipe_rv)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Thread.sleep(timeout);
        onView(withId(R.id.recipe_title_textView)).check(
                matches(withText("Cereal")));
        pressBack();

        onView(withId(R.id.recipe_rv)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, click()));
        Thread.sleep(timeout);
        onView(withId(R.id.recipe_title_textView)).check(
                matches(withText("Apple puree")));
        pressBack();

        onView(withId(R.id.recipe_rv)).perform(
                RecyclerViewActions.actionOnItemAtPosition(2, click()));
        Thread.sleep(timeout);
        onView(withId(R.id.recipe_title_textView)).check(
                matches(withText("Bananas foster")));
        pressBack();

        onView(withId(R.id.recipe_rv)).perform(
                RecyclerViewActions.actionOnItemAtPosition(3, click()));
        Thread.sleep(timeout);
        onView(withId(R.id.recipe_title_textView)).check(
                matches(withText("Apple pie")));
        pressBack();

        onView(withId(R.id.recipe_rv)).perform(
                RecyclerViewActions.actionOnItemAtPosition(4, click()));
        Thread.sleep(timeout);
        onView(withId(R.id.recipe_title_textView)).check(
                matches(withText("Ice cream")));
        pressBack();

        /*
        Sort by servings.
         */
        clickSortButton();
        onView(withText("Servings")).inRoot(isPlatformPopup()).perform(click());

        // check order of items
        onView(withId(R.id.recipe_rv)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Thread.sleep(timeout);
        onView(withId(R.id.recipe_title_textView)).check(
                matches(withText("Cereal")));
        pressBack();

        // ice cream OR apple puree can be shown here!
        onView(withId(R.id.recipe_rv)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, click()));
        Thread.sleep(timeout);
        try {
            onView(withId(R.id.recipe_title_textView)).check(
                    matches(withText("Apple puree")));
        } catch (AssertionFailedError e) {
            onView(withId(R.id.recipe_title_textView)).check(
                    matches(withText("Ice cream")));
        } finally {
            pressBack();
        }

        // ice cream OR apple puree can be shown here!
        onView(withId(R.id.recipe_rv)).perform(
                RecyclerViewActions.actionOnItemAtPosition(2, click()));
        Thread.sleep(timeout);
        try {
            onView(withId(R.id.recipe_title_textView)).check(
                    matches(withText("Ice cream")));
        } catch (AssertionFailedError e) {
            onView(withId(R.id.recipe_title_textView)).check(
                    matches(withText("Apple puree")));
        } finally {
            pressBack();
        }

        onView(withId(R.id.recipe_rv)).perform(
                RecyclerViewActions.actionOnItemAtPosition(3, click()));
        Thread.sleep(timeout);
        onView(withId(R.id.recipe_title_textView)).check(
                matches(withText("Bananas foster")));
        pressBack();

        onView(withId(R.id.recipe_rv)).perform(
                RecyclerViewActions.actionOnItemAtPosition(4, click()));
        Thread.sleep(timeout);
        onView(withId(R.id.recipe_title_textView)).check(
                matches(withText("Apple pie")));
        pressBack();

        /*
        Sort by category.
         */
        clickSortButton();
        onView(withText("Category")).inRoot(isPlatformPopup()).perform(click());

        // check order of items
        Thread.sleep(timeout);
        onView(withId(R.id.recipe_rv)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Thread.sleep(timeout);
        onView(withId(R.id.recipe_title_textView)).check(
                matches(withText("Cereal")));
        pressBack();

        onView(withId(R.id.recipe_rv)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, click()));
        Thread.sleep(timeout);
        try {
            onView(withId(R.id.recipe_title_textView)).check(
                    matches(withText("Apple pie")));
        } catch (AssertionFailedError e) {
            onView(withId(R.id.recipe_title_textView)).check(
                    matches(withText("Bananas foster")));
        }
        pressBack();

        onView(withId(R.id.recipe_rv)).perform(
                RecyclerViewActions.actionOnItemAtPosition(2, click()));
        Thread.sleep(timeout);
        try {
            onView(withId(R.id.recipe_title_textView)).check(
                    matches(withText("Apple pie")));
        } catch (AssertionFailedError e) {
            onView(withId(R.id.recipe_title_textView)).check(
                    matches(withText("Bananas foster")));
        }
        pressBack();

        onView(withId(R.id.recipe_rv)).perform(
                RecyclerViewActions.actionOnItemAtPosition(3, click()));
        Thread.sleep(timeout);
        onView(withId(R.id.recipe_title_textView)).check(
                matches(withText("Apple puree")));
        pressBack();

        onView(withId(R.id.recipe_rv)).perform(
                RecyclerViewActions.actionOnItemAtPosition(4, click()));
        Thread.sleep(timeout);
        onView(withId(R.id.recipe_title_textView)).check(
                matches(withText("Ice cream")));
        pressBack();

        cleanup5Recipes();
    }

    /**
     * Tests deleting an ingredient when it is in a Recipe.
     * This should not crash the app, and the ingredient should remain in the
     * recipe but be removed from storage.
     */
    @Test public void testDeleteIngredientInStorageWhileInRecipe()
            throws InterruptedException {
        // add the pre-existing ingredient
        addPreexistingIngredient("English muffin");

        // add recipe
        typeMockRecipe("Breakfast sandwich");

        onView(withId(R.id.searchButton)).perform(click());
        //pick an ingredient check if recycler view is non empty
        Thread.sleep(timeout);
        onView(withId(R.id.ingredient_storage_list)).perform(
                RecyclerViewActions.actionOnItem(
                        hasDescendant(withText("English muffin")), click()));
        intended(hasComponent(RecipeIngredientEditActivity.class.getName()));
        onView(withId(R.id.edit_amountInput)).perform(typeText("1"));

        closeSoftKeyboard();
        onView(withId(R.id.unitInput)).perform(click());
        onView(withText("count"))
                .inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        closeSoftKeyboard();
        onView(withId(R.id.ingredient_save_button)).perform(click());
        onView(withId(R.id.save_fab)).perform(click());

        // deleting the ingredient from storage
        onView(withId(R.id.ingredient_storage_item)).perform(click());
        onView(withText("English muffin")).perform(click());
        onView(withId(R.id.ingredient_delete_button)).perform(click());
        onView(withText("Delete")).perform(click());

        // returning to the recipe
        onView(withId(R.id.recipe_book_item)).perform(click());
        onView(withId(R.id.recipe_rv)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
        Thread.sleep(timeout);
        onView(withId(R.id.recipe_title_textView)).check(
                matches(withText("Breakfast sandwich")));

        // checking the ingredient is still there
        onView(withId(R.id.recipe_ingredient_recycleViewer)).check(
                matches(hasDescendant(withText("1.00 count"))));
        onView(withId(R.id.recipe_ingredient_recycleViewer)).check(
                matches(hasDescendant(withText("English muffin | Bread"))));
    }

    /**
     * Tests searching for recipes.
     * Searches done:
     *      - tests searching for something not there, and asserts nothing is seen
     *      - tests that after we clear a search, all of the stuff is visible
     *      - tests the whole search functionality (if we search "Apple pie" can we see it?)
     *      - tests the prefix search functionality (if we search "Apple" do we get "Apple pie" and
     *          "Apple puree"?)
     *      - tests case insensitivity for search
     *
     * Note: This test was refactored from multiple separate tests into one.
     * The reason for this was because when each test stood on its own, the process of adding
     * and deleting extended the length each test took to run.
     */
    @Test public void testSearch() throws Exception {
        // add Apple pie, Apple puree, Bananas foster, Ice cream
        add5Recipes();

        /*
        Searches for something not there.
         */
        onView(allOf(isDisplayed(), withId(R.id.search_text))).perform(click());
        onView(allOf(isDisplayed(), withId(R.id.search_text))).perform(typeText("X"));
        closeSoftKeyboard();
        Thread.sleep(1000);

        // assert none of the recipes are visible
        assertTextNotVisible("Apple pie");
        assertTextNotVisible("Apple puree");
        assertTextNotVisible("Bananas foster");
        assertTextNotVisible("Ice cream");
        assertTextNotVisible("Cereal");

        onView(allOf(isDisplayed(), withId(R.id.search_text))).perform(clearText());
        closeSoftKeyboard();

        // assert all of the recipes are now visible after clearing the box
        Thread.sleep(1000);

        onView(withText("Apple pie"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("Apple puree"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("Bananas foster"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("Ice cream"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("Cereal"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        /*
        Searches for the whole text of a Recipe.
         */
        onView(allOf(isDisplayed(), withId(R.id.search_text))).perform(click());
        onView(allOf(isDisplayed(), withId(R.id.search_text))).perform(typeText("bananas foster"));
        closeSoftKeyboard();
        Thread.sleep(1000);

        assertTextNotVisible("Apple pie");
        assertTextNotVisible("Apple puree");
        assertTextNotVisible("Milk");
        assertTextNotVisible("Cheese");
        onView(withText("Bananas foster"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(allOf(isDisplayed(), withId(R.id.search_text))).perform(clearText());

        /*
        Tests the prefix search of a Recipe. (eg. if you enter "Apple", will it get "Apple pie"?
         */
        onView(allOf(isDisplayed(), withId(R.id.search_text))).perform(click());
        onView(allOf(isDisplayed(), withId(R.id.search_text))).perform(typeText("Apple"));
        closeSoftKeyboard();
        Thread.sleep(1000);

        assertTextNotVisible("Bananas foster");
        assertTextNotVisible("Milk");
        assertTextNotVisible("Cheese");
        onView(withText("Apple pie"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("Apple puree"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(allOf(isDisplayed(), withId(R.id.search_text))).perform(clearText());

        /*
        Tests the case insensitivity of searching for a Recipe.
         */
        onView(allOf(isDisplayed(), withId(R.id.search_text))).perform(click());
        onView(allOf(isDisplayed(), withId(R.id.search_text))).perform(typeText("apple"));
        closeSoftKeyboard();
        Thread.sleep(1000);

        assertTextNotVisible("Bananas foster");
        assertTextNotVisible("Milk");
        assertTextNotVisible("Cheese");
        onView(withText("Apple pie"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withText("Apple puree"))
                .check(ViewAssertions.matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        onView(allOf(isDisplayed(), withId(R.id.search_text))).perform(clearText());

        cleanup5Recipes();
    }
}
