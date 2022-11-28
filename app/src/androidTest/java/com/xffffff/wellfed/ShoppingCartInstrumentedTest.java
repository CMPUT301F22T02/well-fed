package com.xffffff.wellfed;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeUp;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.supportsInputMethods;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withInputType;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withParentIndex;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;

import android.widget.DatePicker;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.android.material.datepicker.MaterialDatePicker;

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

    int timeout = 1000;

    private void addIngredient(String description) throws InterruptedException {
        onView(withId(R.id.ingredient_storage_item)).perform(click());
        onView(withId(R.id.fab)).perform(click());
        Thread.sleep(1000);

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

    private void addRecipe(String title) throws InterruptedException {
        onView(withId(R.id.recipe_book_item)).perform(click());
        onView(withId(R.id.fab)).perform(click());
        Thread.sleep(1000);

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
        Thread.sleep(1000);

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

    private void addMealPlan(String mealPlan) throws InterruptedException {
        String recipe = "Eggs and Bacon";
        String ingredient = "Sliced Bread";

        onView(withId(R.id.meal_book_item)).perform(click());
        onView(withId(R.id.fab)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.MealPlan_TitleEditInput)).perform(typeText(mealPlan));

        onView(withId(R.id.dateTextInput)).perform(click());
        onView(withText("OK")).perform(click());

        onView(withId(R.id.MealPlan_CategoryEditInput)).perform(typeText("Breakfast"));
        closeSoftKeyboard();

        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(click());
        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(typeText("1"));
        closeSoftKeyboard();

        Thread.sleep(1000); // for some reason closing soft keyboard has an animation.
        onView(Matchers.allOf(withId(R.id.searchButton), isDescendantOfA(withId(R.id.recipeEditFragment)))).perform(click());

        closeSoftKeyboard();
        Thread.sleep(1000);
        onView(withText(recipe)).perform(click());

        Thread.sleep(1000);
        onView(Matchers.allOf(withId(R.id.searchButton), isDescendantOfA(withId(R.id.ingredientEditFragment)))).perform(click());

        closeSoftKeyboard();
        Thread.sleep(1000);
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

    private void addMealPlanAndIngredients(String mealPlan) throws InterruptedException {
        String recipe = "Eggs and Bacon";
        String ingredient = "Sliced Bread";

        onView(withId(R.id.meal_book_item)).perform(click());

        addRecipe(recipe);
        addIngredient(ingredient);

        onView(withId(R.id.fab)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.MealPlan_TitleEditInput)).perform(typeText(mealPlan));

        onView(withId(R.id.dateTextInput)).perform(click());
        onView(withText("OK")).perform(click());

        onView(withId(R.id.MealPlan_CategoryEditInput)).perform(typeText("Breakfast"));
        closeSoftKeyboard();

        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(click());
        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(typeText("1"));
        closeSoftKeyboard();

        Thread.sleep(1000); // for some reason closing soft keyboard has an animation.
        onView(allOf(withId(R.id.searchButton), isDescendantOfA(withId(R.id.recipeEditFragment)))).perform(click());
        Thread.sleep(1000);

        closeSoftKeyboard();
        Thread.sleep(1000);
        onView(withText(recipe)).perform(click());

        onView(allOf(withId(R.id.addButton), isDescendantOfA(withId(R.id.recipeEditFragment)))).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.edit_recipe_title)).perform(typeText("Toast"));
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
        Thread.sleep(1000);
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
        Thread.sleep(1000);

        onView(withId(R.id.save_fab)).perform(click());
        Thread.sleep(2000);


        closeSoftKeyboard();
        Thread.sleep(1000);
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

        onView(allOf(withId(R.id.addButton), isDescendantOfA(withId(R.id.ingredientEditFragment)))).perform(click());

        onView(withId(R.id.edit_descriptionInput)).perform(typeText("Celery"));
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

        Thread.sleep(1000); // for some reason closing soft keyboard has an animation.
        onView(withId(R.id.ingredient_save_button)).perform(click());
        closeSoftKeyboard();

        Thread.sleep(1000);


        onView(withId(R.id.save_fab)).perform(click());
    }

    private void cleanUpIngredient(String description) throws InterruptedException {
        Thread.sleep(timeout);
        onView(withId(R.id.ingredient_storage_item)).perform(click());
        Thread.sleep(timeout);
        onView(withText(description)).perform(click());

        onView(withId(R.id.ingredient_delete_button)).perform(click());
        onView(withText("Delete")).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.meal_book_item)).perform(click());
    }

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

    private void cleanUpMealPlan(String title) throws InterruptedException {
        Thread.sleep(timeout);
        onView(withId(R.id.meal_book_item)).perform(click());
        Thread.sleep(timeout);
        onView(withText(title)).perform(click());

        onView(withText("Delete")).perform(click());
        onView(withText("Delete")).perform(click());

        Thread.sleep(1000);
    }

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void Before(){
        onView(withId(R.id.shopping_cart_item)).perform(click());
        Intents.init();
    }

    @Test
    public void testAddingMealPlan() throws InterruptedException {
        addMealPlanAndIngredientAndRecipe("Hearty Breakfast");

        Thread.sleep(1000);
        onView(withId(R.id.shopping_cart_item)).perform(click());
        Thread.sleep(10000);

        onView(withText("Eggs")).check(matches(isDisplayed()));
        onView(withText("2.00 count | Protein")).check(matches(isDisplayed()));
        onView(withText("Bacon")).check(matches(isDisplayed()));
        onView(withText("75.00 g | Protein")).check(matches(isDisplayed()));
        onView(Matchers.allOf(withText("Sliced Bread"), withId(R.id.shopping_cart_ingredient_description))).check(matches(isDisplayed()));
        onView(withText("1.00 count | Bread")).check(matches(isDisplayed()));

        Thread.sleep(3000);

        cleanUpMealPlan("Hearty Breakfast");
        cleanUpRecipe("Eggs and Bacon");
        cleanUpIngredient("Sliced Bread");
    }

    @Test
    public void testAddingMultipleMealPlan() throws InterruptedException {
        addMealPlanAndIngredientAndRecipe("Hearty Breakfast");
        addMealPlan("Another Hearty Breakfast");
        addMealPlan("Another Another Hearty Breakfast");

        Thread.sleep(1000);
        onView(withId(R.id.shopping_cart_item)).perform(click());
        Thread.sleep(10000);

        onView(withText("Eggs")).check(matches(isDisplayed()));
        onView(withText("6.00 count | Protein")).check(matches(isDisplayed()));
        onView(withText("Bacon")).check(matches(isDisplayed()));
        onView(withText("225.00 g | Protein")).check(matches(isDisplayed()));
        onView(Matchers.allOf(withText("Sliced Bread"), withId(R.id.shopping_cart_ingredient_description))).check(matches(isDisplayed()));
        onView(withText("3.00 count | Bread")).check(matches(isDisplayed()));
    }

    @Test
    public void testClickingOnShoppingCartItem() throws InterruptedException {
        String ingredient1 = "Bacon",
                ingredient2 = "Eggs",
                ingredient3 = "Sliced Bread" ;

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

    @Test
    public void testDeletingMealPlan() throws InterruptedException {
        String mealPlan1 = "Hearty Breakfast",
                mealPlan2 = "Another Hearty Breakfast";
        addMealPlanAndIngredientAndRecipe(mealPlan1);
        addMealPlan(mealPlan2);

        Thread.sleep(1000);
        onView(withId(R.id.shopping_cart_item)).perform(click());
        Thread.sleep(5000);

        onView(withText("Eggs")).check(matches(isDisplayed()));
        onView(withText("4.00 count | Protein")).check(matches(isDisplayed()));
        onView(withText("Bacon")).check(matches(isDisplayed()));
        onView(withText("150.00 g | Protein")).check(matches(isDisplayed()));
        onView(Matchers.allOf(withText("Sliced Bread"), withId(R.id.shopping_cart_ingredient_description))).check(matches(isDisplayed()));
        onView(withText("2.00 count | Bread")).check(matches(isDisplayed()));

        Thread.sleep(3000);

        cleanUpMealPlan(mealPlan1);

        Thread.sleep(1000);
        onView(withId(R.id.shopping_cart_item)).perform(click());
        Thread.sleep(5000);

        onView(withText("Eggs")).check(matches(isDisplayed()));
        onView(withText("2.00 count | Protein")).check(matches(isDisplayed()));
        onView(withText("Bacon")).check(matches(isDisplayed()));
        onView(withText("75.00 g | Protein")).check(matches(isDisplayed()));
        onView(Matchers.allOf(withText("Sliced Bread"), withId(R.id.shopping_cart_ingredient_description))).check(matches(isDisplayed()));
        onView(withText("1.00 count | Bread")).check(matches(isDisplayed()));

        cleanUpMealPlan(mealPlan2);
        cleanUpRecipe("Eggs and Bacon");
        cleanUpIngredient("Sliced Bread");
    }

    @Test
    public void testSortingWithItems() throws InterruptedException {
        addMealPlanAndIngredientAndRecipe("Hearty Breakfast");

        onView(withId(R.id.shopping_cart_item)).perform(click());
        Thread.sleep(1000);
        onView(withId(R.id.meal_book_item)).perform(click());
        Thread.sleep(1000);
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
                check(matches(ChildAtPositionMatcher.atPosition(2, hasDescendant(withText("Sliced Bread")))));

        onView(withId(R.id.fragment_sort_container3)).perform(click());
        Thread.sleep(3000);
        onView(withText("Category")).inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        Thread.sleep(3000);

        onView(withId(R.id.shopping_cart_list)).
                check(matches(ChildAtPositionMatcher.atPosition(0, hasDescendant(withText("Sliced Bread")))));

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

    @Test
    public void testSortingNoItems() throws InterruptedException {
        onView(withId(R.id.fragment_sort_container3)).perform(click());
        Thread.sleep(500);
        onView(withText("description")).inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        Thread.sleep(500);

        onView(withId(R.id.fragment_sort_container3)).perform(click());
        Thread.sleep(500);
        onView(withText("category")).inRoot(RootMatchers.isPlatformPopup())
                .perform(click());
        Thread.sleep(500);
    }

    @Test
    public void testEditingMealPlanRecipe() throws InterruptedException {
        addMealPlanAndIngredientAndRecipe("Hearty Breakfast");

        Thread.sleep(2000);
        onView(withId(R.id.shopping_cart_item)).perform(click());
        Thread.sleep(10000);

        onView(withText("Eggs")).check(matches(isDisplayed()));
        onView(withText("2.00 count | Protein")).check(matches(isDisplayed()));
        onView(withText("Bacon")).check(matches(isDisplayed()));
        onView(withText("75.00 g | Protein")).check(matches(isDisplayed()));
        onView(Matchers.allOf(withText("Sliced Bread"), withId(R.id.shopping_cart_ingredient_description))).check(matches(isDisplayed()));
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
        onView(Matchers.allOf(withText("Sliced Bread"), withId(R.id.shopping_cart_ingredient_description))).check(matches(isDisplayed()));
        onView(withText("1.00 count | Bread")).check(matches(isDisplayed()));

        cleanUpMealPlan("Hearty Breakfast");
        cleanUpRecipe("Eggs and Bacon");
        cleanUpIngredient("Sliced Bread");
    }

    @Test
    public void testEditingMealPlanIngredient() throws InterruptedException {
        addMealPlanAndIngredientAndRecipe("Hearty Breakfast");

        Thread.sleep(2000);
        onView(withId(R.id.shopping_cart_item)).perform(click());
        Thread.sleep(10000);

        onView(withText("Eggs")).check(matches(isDisplayed()));
        onView(withText("2.00 count | Protein")).check(matches(isDisplayed()));
        onView(withText("Bacon")).check(matches(isDisplayed()));
        onView(withText("75.00 g | Protein")).check(matches(isDisplayed()));
        onView(Matchers.allOf(withText("Sliced Bread"), withId(R.id.shopping_cart_ingredient_description))).check(matches(isDisplayed()));
        onView(withText("1.00 count | Bread")).check(matches(isDisplayed()));

        onView(withId(R.id.meal_book_item)).perform(click());
        Thread.sleep(1000);
        onView(withText("Hearty Breakfast")).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.save_fab)).perform(click());
        Thread.sleep(1000);

        onView(Matchers.allOf(withId(R.id.editButton), withParent(withChild(withText("Sliced Bread")))))
                .perform(click());

        Thread.sleep(1000);

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
        onView(Matchers.allOf(withText("Sliced Bread"), withId(R.id.shopping_cart_ingredient_description))).check(matches(isDisplayed()));
        onView(withText("3.00 count | Bread")).check(matches(isDisplayed()));

        cleanUpMealPlan("Hearty Breakfast");
        cleanUpRecipe("Eggs and Bacon");
        cleanUpIngredient("Sliced Bread");
    }

    @Test
    public void testEditingStoredIngredient() throws InterruptedException {
        addMealPlanAndIngredientAndRecipe("Hearty Breakfast");

        Thread.sleep(2000);
        onView(withId(R.id.shopping_cart_item)).perform(click());
        Thread.sleep(10000);

        onView(withText("Eggs")).check(matches(isDisplayed()));
        onView(withText("2.00 count | Protein")).check(matches(isDisplayed()));
        onView(withText("Bacon")).check(matches(isDisplayed()));
        onView(withText("75.00 g | Protein")).check(matches(isDisplayed()));
        onView(Matchers.allOf(withText("Sliced Bread"), withId(R.id.shopping_cart_ingredient_description))).check(matches(isDisplayed()));
        onView(withText("1.00 count | Bread")).check(matches(isDisplayed()));

        onView(withId(R.id.ingredient_storage_item)).perform(click());
        Thread.sleep(1000);
        onView(allOf(withText("Sliced Bread"), withId(R.id.textView))).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.ingredient_edit_button)).perform(click());
        Thread.sleep(1000);

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
        onView(Matchers.allOf(withText("Sliced Bread"), withId(R.id.shopping_cart_ingredient_description))).check(doesNotExist());
        onView(withText("3.00 count | Bread")).check(doesNotExist());

        cleanUpMealPlan("Hearty Breakfast");
        cleanUpRecipe("Eggs and Bacon");
        cleanUpIngredient("Sliced Bread");
    }

    @Test
    public void testEditingRecipe() throws InterruptedException {
        addIngredient("Sliced Bread");


        onView(withId(R.id.recipe_book_item)).perform(click());
        onView(withId(R.id.fab)).perform(click());
        Thread.sleep(1000);

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
        Thread.sleep(1000);

        onView(withId(R.id.save_fab)).perform(click());
        Thread.sleep(2000);

        onView(withId(R.id.meal_book_item)).perform(click());



        String recipe = "Eggs Over Easy";
        String ingredient = "Sliced Bread";

        onView(withId(R.id.meal_book_item)).perform(click());
        onView(withId(R.id.fab)).perform(click());

        Thread.sleep(1000);

        onView(withId(R.id.MealPlan_TitleEditInput)).perform(typeText("Hearty Breakfast"));

        onView(withId(R.id.dateTextInput)).perform(click());
        onView(withText("OK")).perform(click());

        onView(withId(R.id.MealPlan_CategoryEditInput)).perform(typeText("Breakfast"));
        closeSoftKeyboard();

        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(click());
        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(typeText("1"));
        closeSoftKeyboard();

        Thread.sleep(1000); // for some reason closing soft keyboard has an animation.
        onView(Matchers.allOf(withId(R.id.searchButton), isDescendantOfA(withId(R.id.recipeEditFragment)))).perform(click());

        closeSoftKeyboard();
        Thread.sleep(1000);
        onView(withText(recipe)).perform(click());

        Thread.sleep(1000);
        onView(Matchers.allOf(withId(R.id.searchButton), isDescendantOfA(withId(R.id.ingredientEditFragment)))).perform(click());

        closeSoftKeyboard();
        Thread.sleep(1000);
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
        onView(Matchers.allOf(withText("Sliced Bread"), withId(R.id.shopping_cart_ingredient_description))).check(matches(isDisplayed()));
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
        onView(Matchers.allOf(withText("Sliced Bread"), withId(R.id.shopping_cart_ingredient_description))).check(matches(isDisplayed()));
        onView(withText("1.00 count | Bread")).check(matches(isDisplayed()));

        cleanUpMealPlan("Hearty Breakfast");
        cleanUpRecipe("Eggs Over Easy");
        cleanUpIngredient("Sliced Bread");
    }
}
