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
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

import com.xffffff.wellfed.mealplan.MealPlanEditActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Instrumented test for MealPlan, which will execute on an Android device.
 *
 * Intended device: Pixel 6 Pro API 22
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MealPlanInstrumentedTest {

    /**
     * Holds the default timeout for Thread.sleep().
     */
    int timeout = 1000;

    /**
     * Holds the ActivityScenarioRule
     */
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Setup Recipe test by navigating to MealBookFragment
     */
    @Before
    public void before(){
        onView(withId(R.id.meal_book_item)).perform(click());

        Intents.init();
    }

    /**
     * Saves a meal plan, with an assertion.
     */
    private void testSaveMealPlan(){
        onView(withId(R.id.save_fab)).perform(click());
        intended(hasComponent(MealPlanEditActivity.class.getName()));
    }

    /**
     * Adds an ingredient to ingredient storage.
     * @param description the description of the ingredient to add
     * @throws InterruptedException when the thread sleeps are interrupted.
     */
    private void addIngredient(String description) throws InterruptedException {
        onView(withId(R.id.ingredient_storage_item)).perform(click());
        onView(withId(R.id.fab)).perform(click());
        Thread.sleep(timeout);

        onView(withId(R.id.descriptionInputEditText)).perform(typeText(description));
        closeSoftKeyboard();

        onView(withId(R.id.bestBeforeInput)).perform(click());
        onView(withText("OK")).perform(click());
        closeSoftKeyboard();

        onView(withId(R.id.categoryInputEditText)).perform(typeText("Bread"));
        closeSoftKeyboard();

        onView(withId(R.id.amountInputEditText)).perform(typeText("1"));
        closeSoftKeyboard();

        onView(withId(R.id.unitInputEditText)).perform(click());
        onView(withText("count")).inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        closeSoftKeyboard();

        onView(withId(R.id.locationInputEditText)).perform(typeText("Pantry"));
        closeSoftKeyboard();

        onView(withId(R.id.ingredient_save_button)).perform(click());

        onView(withId(R.id.meal_book_item)).perform(click());
    }

    /**
     * Adds a recipe to the recipe book.
     * @param title the title of the recipe to add
     * @throws InterruptedException when the thread.sleeps are interrupted
     */
    private void addRecipe(String title) throws InterruptedException {
        addRecipe(title, "1");
    }

    /**
     * Adds a recipe to the recipe book.
     * @param title the title of the recipe to add
     * @throws InterruptedException when the thread.sleeps are interrupted
     */
    private void addRecipe(String title, String servings) throws InterruptedException {
        onView(withId(R.id.recipe_book_item)).perform(click());
        onView(withId(R.id.fab)).perform(click());
        Thread.sleep(timeout);

        onView(withId(R.id.edit_recipe_title)).perform(typeText(title));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_prep_time_textEdit)).perform(typeText("10"));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_no_of_servings_textEdit)).perform(typeText(servings));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_category_textEdit)).perform(typeText("Breakfast"));
        closeSoftKeyboard();

        onView(withId(R.id.commentsEditText)).perform(typeText("A hearty breakfast meal"));
        closeSoftKeyboard();

        onView(withId(R.id.addButton)).perform(click());

        onView(withId(R.id.edit_descriptionInput)).perform(typeText("Eggs"));
        closeSoftKeyboard();

        onView(withId(R.id.edit_categoryInput)).perform(typeText("Protein"));
        closeSoftKeyboard();

        onView(withId(R.id.edit_amountInput)).perform(click());
        onView(withId(R.id.edit_amountInput)).perform(typeText("2"));
        closeSoftKeyboard();

        onView(withId(R.id.edit_unitInput)).perform(click());
        onView(withText("count")).inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        closeSoftKeyboard();

        onView(withId(R.id.ingredient_save_button)).perform(click());
        Thread.sleep(timeout);

        onView(withId(R.id.addButton)).perform(click());

        onView(withId(R.id.edit_descriptionInput)).perform(typeText("Bacon"));
        closeSoftKeyboard();

        onView(withId(R.id.edit_categoryInput)).perform(typeText("Protein"));
        closeSoftKeyboard();

        onView(withId(R.id.edit_amountInput)).perform(click());
        onView(withId(R.id.edit_amountInput)).perform(typeText("75"));
        closeSoftKeyboard();

        onView(withId(R.id.edit_unitInput)).perform(click());
        onView(withText("g")).inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        closeSoftKeyboard();

        onView(withId(R.id.ingredient_save_button)).perform(click());
        Thread.sleep(timeout);

        onView(withId(R.id.save_fab)).perform(click());
        Thread.sleep(2000);

        onView(withId(R.id.meal_book_item)).perform(click());
    }

    /**
     * Adds a meal plan to the meal planner.
     * @param mealPlan the name of the meal plan to add to the planner
     * @throws InterruptedException when the thread.sleeps are interrupted
     */
    private void addMealPlan(String mealPlan) throws InterruptedException {
        addMealPlan(mealPlan, "1");
    }

    /**
     * Adds a meal plan to the meal planner.
     * @param mealPlan the name of the meal plan to add to the planner
     * @throws InterruptedException when the thread.sleeps are interrupted
     */
    private void addMealPlan(String mealPlan, String servings) throws InterruptedException {
        String recipe = "Eggs and Bacon";
        String ingredient = "Sliced bread";

        onView(withId(R.id.meal_book_item)).perform(click());
        onView(withId(R.id.fab)).perform(click());

        Thread.sleep(timeout);

        onView(withId(R.id.MealPlan_TitleEditInput)).perform(typeText(mealPlan));

        onView(withId(R.id.dateTextInput)).perform(click());
        onView(withText("OK")).perform(click());

        onView(withId(R.id.MealPlan_CategoryEditInput)).perform(typeText("Breakfast"));
        closeSoftKeyboard();

        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(click());
        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(typeText(servings));
        closeSoftKeyboard();

        Thread.sleep(timeout); // for some reason closing soft keyboard has an animation.
        onView(allOf(withId(R.id.searchButton), isDescendantOfA(withId(R.id.recipeEditFragment)))).perform(click());

        closeSoftKeyboard();
        Thread.sleep(timeout);
        onView(withText(recipe)).perform(click());

        Thread.sleep(timeout);
        onView(allOf(withId(R.id.searchButton), isDescendantOfA(withId(R.id.ingredientEditFragment)))).perform(click());

        closeSoftKeyboard();
        Thread.sleep(timeout);
        onView(withText(ingredient)).perform(click());

        onView(withId(R.id.edit_amountInput)).perform(typeText("1"));
        closeSoftKeyboard();

        onView(withId(R.id.edit_unitInput)).perform(click());
        onView(withText("count")).inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        closeSoftKeyboard();

        onView(withId(R.id.ingredient_save_button)).perform(click());
        closeSoftKeyboard();

        onView(withId(R.id.save_fab)).perform(click());
    }

    /**
     * Adds a meal plan with ingredients to the meal planner.
     * @param mealPlan the name of the meal plan to add
     * @throws InterruptedException when the thread.sleeps are interrupted
     */
    private void addMealPlanAndIngredientAndRecipe(String mealPlan) throws InterruptedException {
        String recipe = "Eggs and Bacon";
        String ingredient = "Sliced bread";

        onView(withId(R.id.meal_book_item)).perform(click());

        addRecipe(recipe);
        addIngredient(ingredient);

        onView(withId(R.id.fab)).perform(click());

        Thread.sleep(timeout);

        onView(withId(R.id.MealPlan_TitleEditInput)).perform(typeText(mealPlan));

        onView(withId(R.id.dateTextInput)).perform(click());
        onView(withText("OK")).perform(click());

        onView(withId(R.id.MealPlan_CategoryEditInput)).perform(typeText("Breakfast"));
        closeSoftKeyboard();

        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(click());
        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(typeText("1"));
        closeSoftKeyboard();

        Thread.sleep(timeout); // for some reason closing soft keyboard has an animation.
        onView(allOf(withId(R.id.searchButton), isDescendantOfA(withId(R.id.recipeEditFragment)))).perform(click());

        closeSoftKeyboard();
        Thread.sleep(timeout);
        onView(withText(recipe)).perform(click());

        closeSoftKeyboard();
        Thread.sleep(timeout);
        onView(allOf(withId(R.id.searchButton), isDescendantOfA(withId(R.id.ingredientEditFragment)))).perform(click());

        closeSoftKeyboard();
        Thread.sleep(2000);
        onView(withText(ingredient)).perform(click());

        onView(withId(R.id.edit_amountInput)).perform(typeText("1"));
        closeSoftKeyboard();

        onView(withId(R.id.edit_unitInput)).perform(click());
        onView(withText("count")).inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        closeSoftKeyboard();

        onView(withId(R.id.ingredient_save_button)).perform(click());
        closeSoftKeyboard();

        onView(withId(R.id.save_fab)).perform(click());
    }

    /**
     * Cleans up an ingredient from the ingredient storage.
     * @param description the title of the ingredient to clean up
     * @throws InterruptedException when the thread.sleeps are interrupted
     */
    private void cleanUpIngredient(String description) throws InterruptedException {
        Thread.sleep(timeout);
        onView(withId(R.id.ingredient_storage_item)).perform(click());
        Thread.sleep(timeout);
        onView(withText(description)).perform(click());

        onView(withId(R.id.ingredient_delete_button)).perform(click());
        onView(withText("Delete")).perform(click());

        Thread.sleep(timeout);

        onView(withId(R.id.meal_book_item)).perform(click());
    }

    /**
     * Cleans up a recipe from the recipe book.
     * @param title the title of the recipe to clean up
     * @throws InterruptedException when the thread.sleeps are interrupted
     */
    private void cleanUpRecipe(String title) throws InterruptedException {
        Thread.sleep(timeout);
        onView(withId(R.id.recipe_book_item)).perform(click());
        Thread.sleep(timeout);
        onView(withText(title)).perform(click());

        onView(withId(R.id.recipe_delete_btn)).perform(click());

        onView(withText("Delete")).perform(click());

        Thread.sleep(timeout);

        onView(withId(R.id.meal_book_item)).perform(click());
    }

    /**
     * Cleans up a meal plan from the meal planner.
     * @param title the title of the meal plan to clean up
     * @throws InterruptedException when the thread.sleeps are interrupted
     */
    private void cleanUpMealPlan(String title) throws InterruptedException {
        Thread.sleep(timeout);
        onView(withId(R.id.meal_book_item)).perform(click());
        Thread.sleep(timeout);
        onView(withText(title)).perform(click());

        onView(withText("Delete")).perform(click());
        onView(withText("Delete")).perform(click());

        Thread.sleep(timeout);
    }

    /**
     * Add a MealPlan that only contains Recipes and then delete the MealPlan and Recipe
     * @throws InterruptedException Thrown if an interrupt from any thread is thrown while
     * Thread.sleep() is executing
     */
    @Test
    public void testAddMealPlanWithRecipes() throws InterruptedException {
        String mealPlan = "Hearty Breakfast";
        String recipe1 = "Eggs and Bacon";
        String recipe2 = "Toast";
        addRecipe(recipe1);

        onView(withId(R.id.fab)).perform(click());

        onView(withId(R.id.MealPlan_TitleEditInput)).perform(typeText(mealPlan));

        onView(withId(R.id.dateTextInput)).perform(click());
        onView(withText("OK")).perform(click());

        onView(withId(R.id.MealPlan_CategoryEditInput)).perform(typeText("Breakfast"));
        closeSoftKeyboard();

        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(click());
        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(typeText("2"));
        closeSoftKeyboard();

        Thread.sleep(timeout); // for some reason closing soft keyboard has an animation.
        onView(allOf(withId(R.id.searchButton), isDescendantOfA(withId(R.id.recipeEditFragment)))).perform(click());

        closeSoftKeyboard();
        Thread.sleep(timeout);
        onView(withText(recipe1)).perform(click());

        Thread.sleep(timeout);

        onView(allOf(withId(R.id.addButton), isDescendantOfA(withId(R.id.recipeEditFragment)))).perform(click());

        onView(withId(R.id.edit_recipe_title)).perform(typeText(recipe2));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_prep_time_textEdit)).perform(typeText("1"));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_no_of_servings_textEdit)).perform(typeText("1"));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_category_textEdit)).perform(typeText("Breakfast"));
        closeSoftKeyboard();

        onView(withId(R.id.commentsEditText)).perform(typeText("Just Toast"));
        closeSoftKeyboard();

        onView(withId(R.id.addButton)).perform(click());

        onView(withId(R.id.edit_descriptionInput)).perform(typeText("Bread"));
        closeSoftKeyboard();

        onView(withId(R.id.edit_categoryInput)).perform(typeText("Bread"));
        closeSoftKeyboard();

        onView(withId(R.id.edit_amountInput)).perform(click());
        onView(withId(R.id.edit_amountInput)).perform(typeText("2"));
        closeSoftKeyboard();

        onView(withId(R.id.edit_unitInput)).perform(click());
        onView(withText("count")).inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        closeSoftKeyboard();

        onView(withId(R.id.ingredient_save_button)).perform(click());
        Thread.sleep(timeout);

        onView(withId(R.id.save_fab)).perform(click());

        onView(withId(R.id.save_fab)).perform(click());
        Thread.sleep(2000);

        onView(withText(mealPlan)).check(matches(isDisplayed()));

        cleanUpMealPlan(mealPlan);
        cleanUpRecipe(recipe2);
        cleanUpRecipe(recipe1);
    }

    /**
     * Add a MealPlan that only contains Ingredients and then delete the MealPlan and Recipe
     * @throws InterruptedException Thrown if an interrupt from any thread is thrown while
     * Thread.sleep() is executing
     */
    @Test
    public void testAddMealPlanWithIngredients() throws InterruptedException {
        String mealPlan = "Snack";
        String ingredient1 = "Muffin";
        String ingredient2 = "Apple";

        addIngredient(ingredient1);

        onView(withId(R.id.fab)).perform(click());

        onView(withId(R.id.MealPlan_TitleEditInput)).perform(typeText(mealPlan));

        onView(withId(R.id.dateTextInput)).perform(click());
        onView(withText("OK")).perform(click());

        onView(withId(R.id.MealPlan_CategoryEditInput)).perform(typeText("Lunch"));
        closeSoftKeyboard();

        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(click());
        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(typeText("1"));
        closeSoftKeyboard();

        Thread.sleep(timeout);
        onView(allOf(withId(R.id.searchButton), isDescendantOfA(withId(R.id.ingredientEditFragment)))).perform(click());

        closeSoftKeyboard();
        Thread.sleep(timeout);
        onView(withText(ingredient1)).perform(click());

        onView(withId(R.id.edit_amountInput)).perform(typeText("1"));
        closeSoftKeyboard();

        onView(withId(R.id.edit_unitInput)).perform(click());
        onView(withText("count")).inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        closeSoftKeyboard();

        onView(withId(R.id.ingredient_save_button)).perform(click());
        closeSoftKeyboard();

        onView(allOf(withId(R.id.addButton), isDescendantOfA(withId(R.id.ingredientEditFragment)))).perform(click());

        onView(withId(R.id.edit_descriptionInput)).perform(typeText(ingredient2));
        closeSoftKeyboard();

        onView(withId(R.id.edit_categoryInput)).perform(typeText("Fruit"));
        closeSoftKeyboard();

        onView(withId(R.id.edit_amountInput)).perform(click());
        onView(withId(R.id.edit_amountInput)).perform(typeText("1"));
        closeSoftKeyboard();

        onView(withId(R.id.edit_unitInput)).perform(click());
        onView(withText("count")).inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        closeSoftKeyboard();

        Thread.sleep(timeout); // for some reason closing soft keyboard has an animation.
        onView(withId(R.id.ingredient_save_button)).perform(click());
        closeSoftKeyboard();

        Thread.sleep(timeout);

        onView(withId(R.id.save_fab)).perform(click());
        Thread.sleep(2000);

        onView(withText(mealPlan)).check(matches(isDisplayed()));

        cleanUpMealPlan(mealPlan);
        cleanUpIngredient(ingredient1);
    }

    /**
     * Add a MealPlan with Recipe and Ingredients and then delete the MealPlan, Recipe, and Ingredients
     * @throws InterruptedException Thrown if an interrupt from any thread is thrown while
     * Thread.sleep() is executing
     */
    @Test
    public void testAddMealPlanWithRecipeAndIngredient() throws InterruptedException {
        String mealPlan = "Hearty Breakfast";
        String recipe = "Eggs and Bacon";
        String ingredient = "Sliced bread";

        addRecipe(recipe);
        addIngredient(ingredient);

        onView(withId(R.id.fab)).perform(click());

        onView(withId(R.id.MealPlan_TitleEditInput)).perform(typeText(mealPlan));

        onView(withId(R.id.dateTextInput)).perform(click());
        onView(withText("OK")).perform(click());

        onView(withId(R.id.MealPlan_CategoryEditInput)).perform(typeText("Breakfast"));
        closeSoftKeyboard();

        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(click());
        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(typeText("1"));
        closeSoftKeyboard();

        Thread.sleep(timeout); // for some reason closing soft keyboard has an animation.
        onView(allOf(withId(R.id.searchButton), isDescendantOfA(withId(R.id.recipeEditFragment)))).perform(click());

        closeSoftKeyboard();
        Thread.sleep(timeout);
        onView(withText(recipe)).perform(click());

        Thread.sleep(timeout);
        onView(allOf(withId(R.id.searchButton), isDescendantOfA(withId(R.id.ingredientEditFragment)))).perform(click());

        closeSoftKeyboard();
        Thread.sleep(1000);

        onView(withText(ingredient)).perform(click());

        Thread.sleep(1000);
        closeSoftKeyboard();

        onView(withId(R.id.edit_amountInput)).perform(typeText("1"));
        closeSoftKeyboard();

        onView(withId(R.id.edit_unitInput)).perform(click());
        onView(withText("count")).inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        closeSoftKeyboard();

        onView(withId(R.id.ingredient_save_button)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.save_fab)).perform(click());
        Thread.sleep(2000);

        onView(withText(mealPlan)).check(matches(isDisplayed()));

        cleanUpMealPlan(mealPlan);
        cleanUpRecipe(recipe);
        cleanUpIngredient(ingredient);
    }

    /**
     * Add a MealPlan and then view the MealPlan making sure that everything a MealPlan should have
     * is there.
     * @throws InterruptedException Thrown if an interrupt from any thread is thrown while
     * Thread.sleep() is executing
     */
    @Test
    public void testViewMealPlan() throws InterruptedException {
        String mealPlan = "Hearty Breakfast";
        String recipe = "Eggs and Bacon";
        String ingredient = "Sliced bread";
        addRecipe(recipe);
        addIngredient(ingredient);

        onView(withId(R.id.fab)).perform(click());

        onView(withId(R.id.MealPlan_TitleEditInput)).perform(typeText(mealPlan));

        onView(withId(R.id.dateTextInput)).perform(click());
        onView(withText("OK")).perform(click());

        onView(withId(R.id.MealPlan_CategoryEditInput)).perform(typeText("Breakfast"));
        closeSoftKeyboard();

        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(click());
        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(typeText("1"));
        closeSoftKeyboard();

        Thread.sleep(timeout); // for some reason closing soft keyboard has an animation.
        onView(allOf(withId(R.id.searchButton), isDescendantOfA(withId(R.id.recipeEditFragment)))).perform(click());

        closeSoftKeyboard();
        Thread.sleep(timeout);
        onView(withText(recipe)).perform(click());

        Thread.sleep(timeout);
        onView(allOf(withId(R.id.searchButton), isDescendantOfA(withId(R.id.ingredientEditFragment)))).perform(click());

        closeSoftKeyboard();
        Thread.sleep(timeout);
        onView(withText(ingredient)).perform(click());

        onView(withId(R.id.edit_amountInput)).perform(typeText("1"));
        closeSoftKeyboard();

        onView(withId(R.id.edit_unitInput)).perform(click());
        onView(withText("count")).inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        closeSoftKeyboard();

        onView(withId(R.id.ingredient_save_button)).perform(click());
        closeSoftKeyboard();

        onView(withId(R.id.save_fab)).perform(click());
        Thread.sleep(2000);

        onView(withText(mealPlan)).check(matches(isDisplayed()));
        onView(withText(mealPlan)).perform(click());
        Thread.sleep(2000);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        String date = dateFormat.format(new Date());

        Thread.sleep(2000);
        onView(withId(R.id.mealPlanTitleTextView)).check(matches(withText(mealPlan)));
        onView(withId(R.id.mealPlanDateTextView)).check(matches(withText(containsString(date))));
        onView(withId(R.id.mealPlanCategoryTextView)).check(matches(withText(containsString("Breakfast"))));
        onView(withId(R.id.mealPlanNumberOfServingsTextView)).check(matches(withText(containsString("1"))));
        onView(withText(ingredient)).check(matches(isDisplayed()));
        onView(withText(recipe)).check(matches(isDisplayed()));

        pressBack();

        cleanUpMealPlan(mealPlan);
        cleanUpRecipe(recipe);
        cleanUpIngredient(ingredient);
    }

    /**
     * Attempt to add an invalid MealPlan and verify that an invalid MealPlan is never added.
     * @throws InterruptedException Thrown if an interrupt from any thread is thrown while
     * Thread.sleep() is executing
     */
    @Test
    public void testAddInvalidMealPlan() throws InterruptedException {
        String recipe = "Eggs and Bacon";
        String ingredient = "Sliced bread";
        addRecipe(recipe);
        addIngredient(ingredient);

        onView(withId(R.id.fab)).perform(click());

        testSaveMealPlan();

        onView(withId(R.id.MealPlan_TitleEditInput)).perform(typeText("Hearty Breakfast"));

        testSaveMealPlan();

        onView(withId(R.id.dateTextInput)).perform(click());
        onView(withText("OK")).perform(click());

        testSaveMealPlan();

        onView(withId(R.id.MealPlan_CategoryEditInput)).perform(typeText("Breakfast"));
        closeSoftKeyboard();

        testSaveMealPlan();

        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(click());
        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(typeText("1"));
        closeSoftKeyboard();

        testSaveMealPlan();

        closeSoftKeyboard();
        Thread.sleep(timeout); // for some reason closing soft keyboard has an animation.
        onView(allOf(withId(R.id.searchButton), isDescendantOfA(withId(R.id.recipeEditFragment)))).perform(click());

        closeSoftKeyboard();
        Thread.sleep(timeout);
        onView(withText(recipe)).perform(click());

        Thread.sleep(timeout);
        onView(allOf(withId(R.id.searchButton), isDescendantOfA(withId(R.id.ingredientEditFragment)))).perform(click());

        closeSoftKeyboard();
        Thread.sleep(timeout);
        onView(withText(ingredient)).perform(click());

        onView(withId(R.id.edit_amountInput)).perform(typeText("1"));
        closeSoftKeyboard();

        onView(withId(R.id.edit_unitInput)).perform(click());
        onView(withText("count")).inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        closeSoftKeyboard();

        onView(withId(R.id.ingredient_save_button)).perform(click());
        closeSoftKeyboard();


        onView(withId(R.id.MealPlan_TitleEditInput)).perform(clearText());

        testSaveMealPlan();

        onView(withId(R.id.MealPlan_CategoryEditInput)).perform(clearText());
        closeSoftKeyboard();

        testSaveMealPlan();

        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(click());
        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(clearText());
        closeSoftKeyboard();

        testSaveMealPlan();

        onView(allOf(withId(R.id.deleteButton), isDescendantOfA(withId(R.id.recipeEditFragment))))
                .perform(click());

        onView(withText("Delete")).perform(click());

        onView(allOf(withId(R.id.deleteButton), isDescendantOfA(withId(R.id.ingredientEditFragment))))
                .perform(click());

        onView(withText("Delete")).perform(click());

        testSaveMealPlan();

        pressBack();

        onView(withText("Quit")).perform(click());

        cleanUpRecipe(recipe);
        cleanUpIngredient(ingredient);
    }

    /**
     * Add a MealPlan and then delete the MealPlan.
     * @throws InterruptedException Thrown if an interrupt from any thread is thrown while
     * Thread.sleep() is executing
     */
    @Test
    public void testDeleteMealPlan() throws InterruptedException {
        addMealPlanAndIngredientAndRecipe("Hearty Breakfast");

        Thread.sleep(2000);

        onView(withText("Hearty Breakfast")).perform(click());

        onView(withText("Delete")).perform(click());
        onView(withText("Delete")).perform(click());
        Thread.sleep(timeout);

        onView(withText("Hearty Breakfast")).check(doesNotExist());
        cleanUpRecipe("Eggs and Bacon");
        cleanUpIngredient("Sliced bread");
    }

    /**
     * Add a Mealplan containing an Ingredient and a Recipe and attempt to delete the Ingredient and
     * Recipe first.
     * @throws InterruptedException Thrown if an interrupt from any thread is thrown while
     * Thread.sleep() is executing
     */
    @Test
    public void testDeleteRecipeAndIngredientBeforeMealPlan() throws InterruptedException {
        addMealPlanAndIngredientAndRecipe("Hearty Breakfast");

        Thread.sleep(3000);

        cleanUpRecipe("Eggs and Bacon");

        onView(withId(R.id.recipe_book_item)).perform(click());
        onView(withText("Eggs and Bacon")).check(matches(isDisplayed()));

        cleanUpMealPlan("Hearty Breakfast");
        cleanUpRecipe("Eggs and Bacon");
        cleanUpIngredient("Sliced bread");
    }

    /**
     * Test updating every field of the MealPlan after it is added.
     * @throws InterruptedException Thrown if an interrupt from any thread is thrown while
     * Thread.sleep() is executing
     */
    @Test
    public void testUpdateMealPlan() throws InterruptedException {
        addRecipe("Quinoa");
        addIngredient("Celery sticks");
        addMealPlanAndIngredientAndRecipe("Hearty Breakfast");

        String mealPlan = "Healthy Lunch";
        String recipe = "Quinoa";
        String ingredient = "Celery sticks";

        Thread.sleep(timeout);

        onView(withText("Hearty Breakfast")).perform(click());

        Thread.sleep(5000);
        onView(withId(R.id.save_fab)).perform(click());

        onView(withId(R.id.MealPlan_TitleEditInput)).perform(clearText(), typeText(mealPlan));


        onView(withId(R.id.dateTextInput)).perform(click());
        onView(withText("OK")).perform(click());

        onView(withId(R.id.MealPlan_CategoryEditInput)).perform(clearText(), typeText("Lunch"));
        closeSoftKeyboard();

        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(click());
        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(clearText(), typeText("2"));
        closeSoftKeyboard();

        onView(allOf(withId(R.id.deleteButton), isDescendantOfA(withId(R.id.recipeEditFragment))))
                .perform(click());

        onView(withText("Delete")).perform(click());

        onView(allOf(withId(R.id.deleteButton), isDescendantOfA(withId(R.id.ingredientEditFragment))))
                .perform(click());

        onView(withText("Delete")).perform(click());

        Thread.sleep(timeout);
        onView(allOf(withId(R.id.searchButton), isDescendantOfA(withId(R.id.recipeEditFragment)))).perform(click());

        closeSoftKeyboard();
        Thread.sleep(timeout);
        onView(withText(recipe)).perform(click());

        closeSoftKeyboard();
        Thread.sleep(timeout);
        onView(allOf(withId(R.id.searchButton), isDescendantOfA(withId(R.id.ingredientEditFragment)))).perform(click());

        closeSoftKeyboard();
        Thread.sleep(timeout);
        onView(withText(ingredient)).perform(click());

        onView(withId(R.id.edit_amountInput)).perform(typeText("5"));
        closeSoftKeyboard();

        onView(withId(R.id.edit_unitInput)).perform(click());
        onView(withText("count")).inRoot(RootMatchers.isPlatformPopup())
                .perform(click());

        onView(withId(R.id.ingredient_save_button)).perform(click());

        onView(withId(R.id.save_fab)).perform(click());
        Thread.sleep(5000);

        onView(withText(mealPlan)).check(matches(isDisplayed()));
        onView(withText(mealPlan)).perform(click());
        Thread.sleep(timeout);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        String date = dateFormat.format(new Date());

        onView(withId(R.id.mealPlanTitleTextView)).check(matches(withText(mealPlan)));
        onView(withId(R.id.mealPlanDateTextView)).check(matches(withText(containsString(date))));
        onView(withId(R.id.mealPlanCategoryTextView)).check(matches(withText(containsString("Lunch"))));
        onView(withId(R.id.mealPlanNumberOfServingsTextView)).check(matches(withText(containsString("2"))));
        onView(withText(ingredient)).check(matches(isDisplayed()));
        onView(withText(recipe)).check(matches(isDisplayed()));

        pressBack();

        cleanUpMealPlan(mealPlan);
        cleanUpRecipe(recipe);
        cleanUpRecipe("Eggs and Bacon");
        cleanUpIngredient(ingredient);
        cleanUpIngredient("Sliced bread");
    }

    /**
     * Add multiple MealPlans and check to make sure all of them are displayed.
     * @throws InterruptedException Thrown if an interrupt from any thread is thrown while
     * Thread.sleep() is executing
     */
    @Test
    public void testAddMultipleMealPlans() throws InterruptedException {
        String firstMeal = "First Hearty Breakfast";
        String secondMeal = "Second Hearty Breakfast";
        String thirdMeal = "Third Hearty Breakfast";
        String fourthMeal = "Fourth Hearty Breakfast";

        addMealPlanAndIngredientAndRecipe(firstMeal);
        addMealPlan(secondMeal);
        addMealPlan(thirdMeal);
        addMealPlan(fourthMeal);

        closeSoftKeyboard();
        Thread.sleep(timeout);
        onView(withText(firstMeal)).check(matches(isDisplayed()));
        onView(withText(secondMeal)).check(matches(isDisplayed()));
        onView(withText(thirdMeal)).check(matches(isDisplayed()));
        onView(withText(fourthMeal)).check(matches(isDisplayed()));

        cleanUpMealPlan(firstMeal);
        cleanUpMealPlan(secondMeal);
        cleanUpMealPlan(thirdMeal);
        cleanUpMealPlan(fourthMeal);
        cleanUpRecipe("Eggs and Bacon");
        cleanUpIngredient("Sliced bread");
    }

    /**
     * Tests recipe scaling of a MealPlan.
     * Does this by adding a Recipe with 5 servings to a MealPlan of 10 servings.
     * The Recipe's ingredients should be doubled.
     * @throws InterruptedException
     */
    @Test
    public void testRecipeScaling() throws InterruptedException {
        addIngredient("Sliced bread");
        addRecipe("Eggs and Bacon", "5");
        addMealPlan("Hearty Breakfast", "10");

        Thread.sleep(3000);

        onView(withText("Hearty Breakfast")).perform(click());

        Thread.sleep(3000);

        onView(withText("Eggs and Bacon")).perform(click());

        Thread.sleep(3000);

        onView(withText("Servings: 10")).check(matches(isDisplayed()));
        onView(withText("4.00 count")).check(matches(isDisplayed()));
        onView(withText("150.00 g")).check(matches(isDisplayed()));

        pressBack();
        pressBack();

        Thread.sleep(3000);

        cleanUpMealPlan("Hearty Breakfast");
        cleanUpIngredient("Sliced bread");
        cleanUpRecipe("Eggs and Bacon");
    }
}