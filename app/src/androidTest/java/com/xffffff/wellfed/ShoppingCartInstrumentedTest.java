package com.xffffff.wellfed;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.supportsInputMethods;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import junit.framework.AssertionFailedError;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@RunWith(JUnit4.class)
public class ShoppingCartInstrumentedTest {

    /**
     * Holds the default timeout for Thread.sleep().
     */
    int timeout = 1000;

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
        onView(withId(R.id.recipe_book_item)).perform(click());
        onView(withId(R.id.fab)).perform(click());
        Thread.sleep(timeout);

        onView(withId(R.id.edit_recipe_title)).perform(typeText(title));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_prep_time_textEdit)).perform(typeText("10"));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_no_of_servings_textEdit)).perform(typeText("1"));
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
        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(typeText("1"));
        closeSoftKeyboard();

        Thread.sleep(timeout); // for some reason closing soft keyboard has an animation.
        onView(Matchers.allOf(withId(R.id.searchButton), isDescendantOfA(withId(R.id.recipeEditFragment)))).perform(click());

        closeSoftKeyboard();
        Thread.sleep(timeout);
        onView(withText(recipe)).perform(click());

        Thread.sleep(timeout);
        onView(Matchers.allOf(withId(R.id.searchButton), isDescendantOfA(withId(R.id.ingredientEditFragment)))).perform(click());

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
     * Adds a meal plan with an ingredient and a recipe to the meal planner.
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
        onView(Matchers.allOf(withId(R.id.searchButton), isDescendantOfA(withId(R.id.recipeEditFragment)))).perform(click());

        closeSoftKeyboard();
        Thread.sleep(timeout);
        onView(withText(recipe)).perform(click());

        closeSoftKeyboard();
        Thread.sleep(timeout);
        onView(Matchers.allOf(withId(R.id.searchButton), isDescendantOfA(withId(R.id.ingredientEditFragment)))).perform(click());

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
        onView(allOf(withText(description), withId(R.id.textView))).perform(click());

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
     * Holds the ActivityScenarioRule
     */
    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    /**
     * Setup Recipe test by navigating to MealBookFragment
     */
    @Before
    public void Before(){
        onView(withId(R.id.shopping_cart_item)).perform(click());
        Intents.init();
    }

    /**
     * Add a MealPlan and verify the shopping cart is updated as well
     * @throws InterruptedException Thrown if an interrupt from any thread is thrown while
     * Thread.sleep() is executing
     */
    @Test
    public void testAddingMealPlan() throws InterruptedException {
        String ingredient = "Sliced bread";
        addMealPlanAndIngredientAndRecipe("Hearty Breakfast");

        Thread.sleep(timeout);
        onView(withId(R.id.shopping_cart_item)).perform(click());
        Thread.sleep(10000);

        onView(withText("Eggs")).check(matches(isDisplayed()));
        onView(withText("2.00 count | Protein")).check(matches(isDisplayed()));
        onView(withText("Bacon")).check(matches(isDisplayed()));
        onView(withText("75.00 g | Protein")).check(matches(isDisplayed()));
        onView(Matchers.allOf(withText(ingredient), withId(R.id.shopping_cart_ingredient_description))).check(matches(isDisplayed()));
        onView(withText("1.00 count | Bread")).check(matches(isDisplayed()));

        Thread.sleep(3000);

        cleanUpMealPlan("Hearty Breakfast");
        cleanUpRecipe("Eggs and Bacon");
        cleanUpIngredient(ingredient);
    }

    /**
     * Add multiple MealPlan and verify the shopping cart is updated as well
     * @throws InterruptedException Thrown if an interrupt from any thread is thrown while
     * Thread.sleep() is executing
     */
    @Test
    public void testAddingMultipleMealPlan() throws InterruptedException {
        addMealPlanAndIngredientAndRecipe("Hearty Breakfast");
        addMealPlan("Another Hearty Breakfast");
        addMealPlan("Another Another Hearty Breakfast");

        Thread.sleep(timeout);
        onView(withId(R.id.shopping_cart_item)).perform(click());
        Thread.sleep(10000);

        onView(withText("Eggs")).check(matches(isDisplayed()));
        onView(withText("6.00 count | Protein")).check(matches(isDisplayed()));
        onView(withText("Bacon")).check(matches(isDisplayed()));
        onView(withText("225.00 g | Protein")).check(matches(isDisplayed()));
        onView(Matchers.allOf(withText("Sliced bread"), withId(R.id.shopping_cart_ingredient_description))).check(matches(isDisplayed()));
        onView(withText("3.00 count | Bread")).check(matches(isDisplayed()));
    }

    /**
     * Test is clicking on a Shopping Cart item is working
     * @throws InterruptedException Thrown if an interrupt from any thread is thrown while
     * Thread.sleep() is executing
     */
    @Test
    public void testClickingOnShoppingCartItem() throws InterruptedException {
        String ingredient1 = "Bacon",
                ingredient2 = "Eggs",
                ingredient3 = "Sliced bread" ;

        addMealPlanAndIngredientAndRecipe("Hearty Breakfast");

        Thread.sleep(5000);
        onView(withId(R.id.shopping_cart_item)).perform(click());
        Thread.sleep(5000);

        onView(allOf(withId(R.id.checkBox), withParent(withChild(withText(ingredient1))))).perform(click());
        onView(allOf(withId(R.id.checkBox), withParent(withChild(withText(ingredient1))))).check(matches(isChecked()));
        onView(allOf(withId(R.id.checkBox), withParent(withChild(withText(ingredient1))))).perform(click());
        onView(allOf(withId(R.id.checkBox), withParent(withChild(withText(ingredient1))))).check(matches(not(isChecked())));

        onView(allOf(withId(R.id.checkBox), withParent(withChild(withText(ingredient2))))).perform(click());
        onView(allOf(withId(R.id.checkBox), withParent(withChild(withText(ingredient2))))).check(matches(isChecked()));
        onView(allOf(withId(R.id.checkBox), withParent(withChild(withText(ingredient2))))).perform(click());
        onView(allOf(withId(R.id.checkBox), withParent(withChild(withText(ingredient2))))).check(matches(not(isChecked())));

        onView(allOf(withId(R.id.checkBox), withParent(withChild(withText(ingredient3))))).perform(click());
        onView(allOf(withId(R.id.checkBox), withParent(withChild(withText(ingredient3))))).check(matches(isChecked()));
        onView(allOf(withId(R.id.checkBox), withParent(withChild(withText(ingredient3))))).perform(click());
        onView(allOf(withId(R.id.checkBox), withParent(withChild(withText(ingredient3))))).check(matches(not(isChecked())));
        Thread.sleep(5000);
    }

    /**
     * Add two MealPlan and verify the shopping cart is updated. Then delete both MealPlans and
     * verify the changes are reflected in the shopping cart.
     * @throws InterruptedException Thrown if an interrupt from any thread is thrown while
     * Thread.sleep() is executing
     */
    @Test
    public void testDeletingMealPlan() throws InterruptedException {
        String ingredient = "Sliced bread";
        String mealPlan1 = "Hearty Breakfast",
                mealPlan2 = "Another Hearty Breakfast";
        addMealPlanAndIngredientAndRecipe(mealPlan1);
        addMealPlan(mealPlan2);

        Thread.sleep(timeout);
        onView(withId(R.id.shopping_cart_item)).perform(click());
        Thread.sleep(5000);

        onView(withText("Eggs")).check(matches(isDisplayed()));
        onView(withText("4.00 count | Protein")).check(matches(isDisplayed()));
        onView(withText("Bacon")).check(matches(isDisplayed()));
        onView(withText("150.00 g | Protein")).check(matches(isDisplayed()));
        onView(Matchers.allOf(withText(ingredient), withId(R.id.shopping_cart_ingredient_description))).check(matches(isDisplayed()));
        onView(withText("2.00 count | Bread")).check(matches(isDisplayed()));

        Thread.sleep(3000);

        cleanUpMealPlan(mealPlan1);

        Thread.sleep(timeout);
        onView(withId(R.id.shopping_cart_item)).perform(click());
        Thread.sleep(5000);

        onView(withText("Eggs")).check(matches(isDisplayed()));
        onView(withText("2.00 count | Protein")).check(matches(isDisplayed()));
        onView(withText("Bacon")).check(matches(isDisplayed()));
        onView(withText("75.00 g | Protein")).check(matches(isDisplayed()));
        onView(Matchers.allOf(withText(ingredient), withId(R.id.shopping_cart_ingredient_description))).check(matches(isDisplayed()));
        onView(withText("1.00 count | Bread")).check(matches(isDisplayed()));

        cleanUpMealPlan(mealPlan2);
        cleanUpRecipe("Eggs and Bacon");
        cleanUpIngredient(ingredient);
    }

    /**
     * Test the sorting functionality of Shopping cart with Items present
     * @throws InterruptedException Thrown if an interrupt from any thread is thrown while
     * Thread.sleep() is executing
     */
    @Test
    public void testSortingWithItems() throws InterruptedException {
        String ingredient = "Sliced bread";
        addMealPlanAndIngredientAndRecipe("Hearty Breakfast");

        onView(withId(R.id.shopping_cart_item)).perform(click());
        Thread.sleep(timeout);
        onView(withId(R.id.meal_book_item)).perform(click());
        Thread.sleep(timeout);
        onView(withId(R.id.shopping_cart_item)).perform(click());

        onView(withId(R.id.fragment_sort_container3)).perform(click());
        Thread.sleep(3000);
        onView(withText("Description")).inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        Thread.sleep(3000);

        onView(withId(R.id.shopping_cart_list)).
                check(matches(ChildAtPositionMatcher.atPosition(0, hasDescendant(withText("Bacon")))));

        onView(withId(R.id.shopping_cart_list)).
                check(matches(ChildAtPositionMatcher.atPosition(1, hasDescendant(withText("Eggs")))));

        onView(withId(R.id.shopping_cart_list)).
                check(matches(ChildAtPositionMatcher.atPosition(2, hasDescendant(withText(ingredient)))));

        onView(withId(R.id.fragment_sort_container3)).perform(click());
        Thread.sleep(3000);
        onView(withText("Category")).inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        Thread.sleep(3000);

        onView(withId(R.id.shopping_cart_list)).
                check(matches(ChildAtPositionMatcher.atPosition(0, hasDescendant(withText(ingredient)))));

        try{
            onView(withId(R.id.shopping_cart_list)).
                    check(matches(ChildAtPositionMatcher.atPosition(1, hasDescendant(withText("Bacon")))));
        }
        catch (AssertionFailedError assertError){
            onView(withId(R.id.shopping_cart_list)).
                    check(matches(ChildAtPositionMatcher.atPosition(1, hasDescendant(withText("Eggs")))));
        }

        try{
            onView(withId(R.id.shopping_cart_list)).
                    check(matches(ChildAtPositionMatcher.atPosition(2, hasDescendant(withText("Bacon")))));
        }
        catch (AssertionFailedError assertError){
            onView(withId(R.id.shopping_cart_list)).
                    check(matches(ChildAtPositionMatcher.atPosition(2, hasDescendant(withText("Eggs")))));
        }
    }

    /**
     * Test Sorting when no items are present in the shopping cart
     * @throws InterruptedException Thrown if an interrupt from any thread is thrown while
     * Thread.sleep() is executing
     */
    @Test
    public void testSortingNoItems() throws InterruptedException {
        onView(withId(R.id.fragment_sort_container3)).perform(click());
        Thread.sleep(500);
        onView(withText("Description")).inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        Thread.sleep(500);

        onView(withId(R.id.fragment_sort_container3)).perform(click());
        Thread.sleep(500);
        onView(withText("Category")).inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        Thread.sleep(500);
    }

    /**
     * Add a MealPlan and verify the shopping cart is updated as well
     * @throws InterruptedException Thrown if an interrupt from any thread is thrown while
     * Thread.sleep() is executing
     */
    @Test
    public void testEditingMealPlanRecipe() throws InterruptedException {
        String ingredient = "Sliced bread";
        addMealPlanAndIngredientAndRecipe("Hearty Breakfast");

        Thread.sleep(2000);
        onView(withId(R.id.shopping_cart_item)).perform(click());
        Thread.sleep(10000);

        onView(withText("Eggs")).check(matches(isDisplayed()));
        onView(withText("2.00 count | Protein")).check(matches(isDisplayed()));
        onView(withText("Bacon")).check(matches(isDisplayed()));
        onView(withText("75.00 g | Protein")).check(matches(isDisplayed()));
        onView(Matchers.allOf(withText(ingredient), withId(R.id.shopping_cart_ingredient_description))).check(matches(isDisplayed()));
        onView(withText("1.00 count | Bread")).check(matches(isDisplayed()));

        onView(withId(R.id.meal_book_item)).perform(click());
        Thread.sleep(5000);
        onView(withText("Hearty Breakfast")).perform(click());
        Thread.sleep(5000);

        onView(withId(R.id.save_fab)).perform(click());
        Thread.sleep(5000);

        onView(Matchers.allOf(withId(R.id.editButton), withParent(withChild(withText("Eggs and Bacon")))))
                .perform(click());

        Thread.sleep(5000);

        onView(Matchers.allOf(withId(R.id.editButton), withParent(withChild(withText("Eggs")))))
                .perform(click());

        Thread.sleep(2000);

        onView(withId(R.id.edit_amountInput)).perform(clearText(), typeText("5"));
        Thread.sleep(2000);
        closeSoftKeyboard();

        Thread.sleep(2000);

        onView(withId(R.id.ingredient_save_button)).perform(click());

        Thread.sleep(2000);

        onView(withId(R.id.save_fab)).perform(click());

        Thread.sleep(2000);

        onView(withId(R.id.save_fab)).perform(click());

        Thread.sleep(5000);

        onView(withId(R.id.shopping_cart_item)).perform(click());

        Thread.sleep(5000);

        onView(withText("Eggs")).check(matches(isDisplayed()));
        onView(withText("5.00 count | Protein")).check(matches(isDisplayed()));
        onView(withText("Bacon")).check(matches(isDisplayed()));
        onView(withText("75.00 g | Protein")).check(matches(isDisplayed()));
        onView(Matchers.allOf(withText(ingredient), withId(R.id.shopping_cart_ingredient_description))).check(matches(isDisplayed()));
        onView(withText("1.00 count | Bread")).check(matches(isDisplayed()));

        cleanUpMealPlan("Hearty Breakfast");
        cleanUpRecipe("Eggs and Bacon");
        cleanUpIngredient(ingredient);
    }

    /**
     * Test Shopping cart updates when a MealPlan Ingredient is updated
     * @throws InterruptedException Thrown if an interrupt from any thread is thrown while
     * Thread.sleep() is executing
     */
    @Test
    public void testEditingMealPlanIngredient() throws InterruptedException {
        String ingredient = "Sliced bread";
        addMealPlanAndIngredientAndRecipe("Hearty Breakfast");

        Thread.sleep(2000);
        onView(withId(R.id.shopping_cart_item)).perform(click());
        Thread.sleep(10000);

        onView(withText("Eggs")).check(matches(isDisplayed()));
        onView(withText("2.00 count | Protein")).check(matches(isDisplayed()));
        onView(withText("Bacon")).check(matches(isDisplayed()));
        onView(withText("75.00 g | Protein")).check(matches(isDisplayed()));
        onView(Matchers.allOf(withText(ingredient), withId(R.id.shopping_cart_ingredient_description))).check(matches(isDisplayed()));
        onView(withText("1.00 count | Bread")).check(matches(isDisplayed()));

        onView(withId(R.id.meal_book_item)).perform(click());
        Thread.sleep(timeout);
        onView(withText("Hearty Breakfast")).perform(click());
        Thread.sleep(timeout);

        onView(withId(R.id.save_fab)).perform(click());
        Thread.sleep(timeout);

        onView(Matchers.allOf(withId(R.id.editButton), withParent(withChild(withText(ingredient)))))
                .perform(click());

        Thread.sleep(timeout);

        onView(withId(R.id.edit_amountInput)).perform(clearText(), typeText("3"));
        Thread.sleep(2000);
        closeSoftKeyboard();

        onView(withId(R.id.ingredient_save_button)).perform(click());

        onView(withId(R.id.save_fab)).perform(click());

        Thread.sleep(5000);

        onView(withId(R.id.shopping_cart_item)).perform(click());

        Thread.sleep(5000);

        onView(withText("Eggs")).check(matches(isDisplayed()));
        onView(withText("2.00 count | Protein")).check(matches(isDisplayed()));
        onView(withText("Bacon")).check(matches(isDisplayed()));
        onView(withText("75.00 g | Protein")).check(matches(isDisplayed()));
        onView(Matchers.allOf(withText("Sliced bread"), withId(R.id.shopping_cart_ingredient_description))).check(matches(isDisplayed()));
        onView(withText("3.00 count | Bread")).check(matches(isDisplayed()));

        cleanUpMealPlan("Hearty Breakfast");
        cleanUpRecipe("Eggs and Bacon");
        cleanUpIngredient(ingredient);
    }

    /**
     * Test Shopping cart updates when a stored ingredient is updated
     * @throws InterruptedException Thrown if an interrupt from any thread is thrown while
     * Thread.sleep() is executing
     */
    @Test
    public void testEditingStoredIngredient() throws InterruptedException {
        String ingredient = "Sliced bread";
        addMealPlanAndIngredientAndRecipe("Hearty Breakfast");

        Thread.sleep(2000);
        onView(withId(R.id.shopping_cart_item)).perform(click());
        Thread.sleep(10000);

        onView(withText("Eggs")).check(matches(isDisplayed()));
        onView(withText("2.00 count | Protein")).check(matches(isDisplayed()));
        onView(withText("Bacon")).check(matches(isDisplayed()));
        onView(withText("75.00 g | Protein")).check(matches(isDisplayed()));
        onView(Matchers.allOf(withText(ingredient), withId(R.id.shopping_cart_ingredient_description))).check(matches(isDisplayed()));
        onView(withText("1.00 count | Bread")).check(matches(isDisplayed()));

        onView(withId(R.id.ingredient_storage_item)).perform(click());
        Thread.sleep(timeout);
        onView(allOf(withText(ingredient), withId(R.id.textView))).perform(click());
        Thread.sleep(timeout);

        onView(withId(R.id.ingredient_edit_button)).perform(click());
        Thread.sleep(timeout);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);

        String date = dateFormat.format(new Date());

        onView(withId(R.id.bestBeforeInput)).perform(click());
        onView(withId(com.google.android.material.R.id.mtrl_picker_header_toggle)).perform(click());

        onView(allOf(supportsInputMethods(), isDescendantOfA(withId(com.google.android.material.R.id.mtrl_picker_text_input_date)))).perform(clearText(), typeText("11/27/30"));

        onView(withText("OK")).perform(click());

        onView(withId(R.id.amountInputEditText)).perform(clearText(), typeText("3"));
        closeSoftKeyboard();

        onView(withId(R.id.ingredient_save_button)).perform(click());

        Thread.sleep(5000);

        onView(withId(R.id.shopping_cart_item)).perform(click());

        Thread.sleep(5000);

        onView(withText("Eggs")).check(matches(isDisplayed()));
        onView(withText("2.00 count | Protein")).check(matches(isDisplayed()));
        onView(withText("Bacon")).check(matches(isDisplayed()));
        onView(withText("75.00 g | Protein")).check(matches(isDisplayed()));
        onView(Matchers.allOf(withText(ingredient), withId(R.id.shopping_cart_ingredient_description))).check(doesNotExist());
        onView(withText("3.00 count | Bread")).check(doesNotExist());

        cleanUpMealPlan("Hearty Breakfast");
        cleanUpRecipe("Eggs and Bacon");
        cleanUpIngredient(ingredient);
    }

    /**
     * Test Shopping cart updates when a recipe is updated
     * @throws InterruptedException Thrown if an interrupt from any thread is thrown while
     * Thread.sleep() is executing
     */
    @Test
    public void testEditingRecipe() throws InterruptedException {
        String ingredient = "Sliced bread";
        addIngredient(ingredient);


        onView(withId(R.id.recipe_book_item)).perform(click());
        onView(withId(R.id.fab)).perform(click());
        Thread.sleep(timeout);

        onView(withId(R.id.edit_recipe_title)).perform(typeText("Eggs Over Easy"));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_prep_time_textEdit)).perform(typeText("10"));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_no_of_servings_textEdit)).perform(typeText("1"));
        closeSoftKeyboard();

        onView(withId(R.id.recipe_category_textEdit)).perform(typeText("Breakfast"));
        closeSoftKeyboard();

        onView(withId(R.id.commentsEditText)).perform(typeText("A hearty breakfast"));
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

        onView(withId(R.id.save_fab)).perform(click());
        Thread.sleep(2000);

        onView(withId(R.id.meal_book_item)).perform(click());



        String recipe = "Eggs Over Easy";

        onView(withId(R.id.meal_book_item)).perform(click());
        onView(withId(R.id.fab)).perform(click());

        Thread.sleep(timeout);

        onView(withId(R.id.MealPlan_TitleEditInput)).perform(typeText("Hearty Breakfast"));

        onView(withId(R.id.dateTextInput)).perform(click());
        onView(withText("OK")).perform(click());

        onView(withId(R.id.MealPlan_CategoryEditInput)).perform(typeText("Breakfast"));
        closeSoftKeyboard();

        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(click());
        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(typeText("1"));
        closeSoftKeyboard();

        Thread.sleep(timeout); // for some reason closing soft keyboard has an animation.
        onView(Matchers.allOf(withId(R.id.searchButton), isDescendantOfA(withId(R.id.recipeEditFragment)))).perform(click());

        closeSoftKeyboard();
        Thread.sleep(timeout);
        onView(withText(recipe)).perform(click());

        Thread.sleep(timeout);
        onView(Matchers.allOf(withId(R.id.searchButton), isDescendantOfA(withId(R.id.ingredientEditFragment)))).perform(click());

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
        onView(withId(R.id.shopping_cart_item)).perform(click());
        Thread.sleep(10000);

        onView(withText("Eggs")).check(matches(isDisplayed()));
        onView(withText("2.00 count | Protein")).check(matches(isDisplayed()));
        onView(Matchers.allOf(withText(ingredient), withId(R.id.shopping_cart_ingredient_description))).check(matches(isDisplayed()));
        onView(withText("1.00 count | Bread")).check(matches(isDisplayed()));

        onView(withId(R.id.recipe_book_item)).perform(click());
        Thread.sleep(2000);
        onView(withText("Eggs Over Easy")).perform(click());
        Thread.sleep(2000);

        onView(withId(R.id.save_fab)).perform(click());
        Thread.sleep(5000);

        onView(withId(R.id.recipe_no_of_servings_textEdit)).perform(clearText(), typeText("1"));
        closeSoftKeyboard();

        onView(allOf(withId(R.id.editButton), withParent(withChild(withText("Eggs"))))).perform(click());

        Thread.sleep(2000);

        onView(withId(R.id.edit_amountInput)).perform(clearText(), typeText("5"));
        closeSoftKeyboard();

        onView(withId(R.id.ingredient_save_button)).perform(click());

        Thread.sleep(2000);

        onView(withId(R.id.save_fab)).perform(click());

        Thread.sleep(2000);

        onView(withId(R.id.shopping_cart_item)).perform(click());

        Thread.sleep(5000);

        onView(withText("Eggs")).check(matches(isDisplayed()));
        onView(withText("5.00 count | Protein")).check(matches(isDisplayed()));
        onView(Matchers.allOf(withText(ingredient), withId(R.id.shopping_cart_ingredient_description))).check(matches(isDisplayed()));
        onView(withText("1.00 count | Bread")).check(matches(isDisplayed()));

        cleanUpMealPlan("Hearty Breakfast");
        cleanUpRecipe("Eggs Over Easy");
        cleanUpIngredient(ingredient);
    }
}
