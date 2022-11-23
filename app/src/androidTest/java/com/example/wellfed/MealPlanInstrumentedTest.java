package com.example.wellfed;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withParentIndex;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import android.content.Context;

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class MealPlanInstrumentedTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule =
            new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void before(){
        onView(withId(R.id.meal_book_item)).perform(click());

        Intents.init();
    }

    private void addIngredient(String description){
        onView(withId(R.id.ingredient_storage_item)).perform(click());
        onView(withId(R.id.fab)).perform(click());

        onView(withId(R.id.descriptionInputEditText)).perform(typeText(description));
        closeSoftKeyboard();

        onView(withId(R.id.bestBeforeInput)).perform(click());
        onView(withText("OK")).perform(click());
        closeSoftKeyboard();

        onView(withId(R.id.categoryInputEditText)).perform(typeText("Bread"));
        closeSoftKeyboard();

        onView(withId(R.id.amountInputEditText)).perform(typeText("1"));
        closeSoftKeyboard();

        onView(withId(R.id.unitInputEditText)).perform(typeText("count"));
        closeSoftKeyboard();

        onView(withId(R.id.locationInputEditText)).perform(typeText("Pantry"));
        closeSoftKeyboard();

        onView(withId(R.id.ingredient_save_button)).perform(click());

        onView(withId(R.id.meal_book_item)).perform(click());
    }

    private void addRecipe(String title) throws InterruptedException {
        onView(withId(R.id.recipe_book_item)).perform(click());
        onView(withId(R.id.fab)).perform(click());

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

        onView(withId(R.id.edit_unitInput)).perform(typeText("count"));
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

        onView(withId(R.id.edit_unitInput)).perform(typeText("g"));
        closeSoftKeyboard();

        onView(withId(R.id.ingredient_save_button)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.save_fab)).perform(click());

        onView(withId(R.id.meal_book_item)).perform(click());
    }

    private void cleanUpIngredient(String description) throws InterruptedException {
        Thread.sleep(500);
        onView(withId(R.id.ingredient_storage_item)).perform(click());
        Thread.sleep(500);
        onView(withText(description)).perform(click());

        onView(withId(R.id.ingredient_delete_button)).perform(click());
        onView(withText("Delete")).perform(click());

        onView(withId(R.id.meal_book_item)).perform(click());
    }

    private void cleanUpRecipe(String title) throws InterruptedException {
        Thread.sleep(500);
        onView(withId(R.id.recipe_book_item)).perform(click());
        Thread.sleep(500);
        onView(withText(title)).perform(click());

        onView(withId(R.id.recipe_delete_btn)).perform(click());

        onView(withText("Delete")).perform(click());

        onView(withId(R.id.meal_book_item)).perform(click());
    }

    private void cleanUpMealPlan(String title) throws InterruptedException {
        Thread.sleep(500);
        onView(withId(R.id.meal_book_item)).perform(click());
        Thread.sleep(500);
        onView(withText(title)).perform(click());

        onView(withText("Delete")).perform(click());
        onView(withText("Delete")).perform(click());
    }


    @Test
    public void testAddMealPlanWithRecipes() throws InterruptedException {
        addRecipe("Eggs and Bacon");

        onView(withId(R.id.fab)).perform(click());

        onView(withId(R.id.MealPlan_TitleEditInput)).perform(typeText("Hearty Breakfast"));

        onView(withId(R.id.dateTextInput)).perform(click());
        onView(withText("OK")).perform(click());

        onView(withId(R.id.MealPlan_CategoryEditInput)).perform(typeText("Breakfast"));
        closeSoftKeyboard();

        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(click());
        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(typeText("2"));
        closeSoftKeyboard();

        onView(allOf(withId(R.id.searchButton), isDescendantOfA(withId(R.id.recipeEditFragment)))).perform(click());

        onView(withText("Eggs and Bacon")).perform(click());

        onView(allOf(withId(R.id.addButton), isDescendantOfA(withId(R.id.recipeEditFragment)))).perform(click());

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

        onView(withId(R.id.edit_descriptionInput)).perform(typeText("Bread"));
        closeSoftKeyboard();

        onView(withId(R.id.edit_categoryInput)).perform(typeText("Bread"));
        closeSoftKeyboard();

        onView(withId(R.id.edit_amountInput)).perform(click());
        onView(withId(R.id.edit_amountInput)).perform(typeText("2"));
        closeSoftKeyboard();

        onView(withId(R.id.edit_unitInput)).perform(typeText("count"));
        closeSoftKeyboard();

        onView(withId(R.id.ingredient_save_button)).perform(click());
        Thread.sleep(1000);

        onView(withId(R.id.save_fab)).perform(click());

        onView(withId(R.id.save_fab)).perform(click());
        Thread.sleep(1000);

        onView(withText("Hearty Breakfast")).check(matches(isDisplayed()));

        cleanUpMealPlan("Hearty Breakfast");
        cleanUpRecipe("Toast");
        cleanUpRecipe("Eggs and Bacon");
    }

    @Test
    public void testAddMealPlanWithIngredients() throws InterruptedException {
        addIngredient("Muffin");

        onView(withId(R.id.fab)).perform(click());

        onView(withId(R.id.MealPlan_TitleEditInput)).perform(typeText("Snack"));

        onView(withId(R.id.dateTextInput)).perform(click());
        onView(withText("OK")).perform(click());

        onView(withId(R.id.MealPlan_CategoryEditInput)).perform(typeText("Lunch"));
        closeSoftKeyboard();

        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(click());
        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(typeText("1"));
        closeSoftKeyboard();

        onView(allOf(withId(R.id.searchButton), isDescendantOfA(withId(R.id.MealPlan_IngredientEditFragment)))).perform(click());

        onView(withText("Muffin")).perform(click());

        onView(withId(R.id.edit_amountInput)).perform(typeText("1"));
        closeSoftKeyboard();

        onView(withId(R.id.edit_unitInput)).perform(typeText("count"));
        closeSoftKeyboard();

        onView(withId(R.id.ingredient_save_button)).perform(click());
        closeSoftKeyboard();

        onView(allOf(withId(R.id.addButton), isDescendantOfA(withId(R.id.MealPlan_IngredientEditFragment)))).perform(click());

        onView(withId(R.id.edit_descriptionInput)).perform(typeText("Apple"));
        closeSoftKeyboard();

        onView(withId(R.id.edit_categoryInput)).perform(typeText("Fruit"));
        closeSoftKeyboard();

        onView(withId(R.id.edit_amountInput)).perform(click());
        onView(withId(R.id.edit_amountInput)).perform(typeText("1"));
        closeSoftKeyboard();

        onView(withId(R.id.edit_unitInput)).perform(typeText("count"));
        closeSoftKeyboard();

        onView(withId(R.id.ingredient_save_button)).perform(click());
        closeSoftKeyboard();

        onView(withId(R.id.save_fab)).perform(click());
        Thread.sleep(1000);

        onView(withText("Snack")).check(matches(isDisplayed()));

        cleanUpMealPlan("Snack");
        cleanUpIngredient("Muffin");
    }

    @Test
    public void testAddMealPlanWithRecipeAndIngredient() throws InterruptedException {
        String recipe = "Eggs and Bacon";
        String ingredient = "Sliced Bread";
        addRecipe(recipe);
        addIngredient(ingredient);

        onView(withId(R.id.fab)).perform(click());

        onView(withId(R.id.MealPlan_TitleEditInput)).perform(typeText("Hearty Breakfast"));

        onView(withId(R.id.dateTextInput)).perform(click());
        onView(withText("OK")).perform(click());

        onView(withId(R.id.MealPlan_CategoryEditInput)).perform(typeText("Breakfast"));
        closeSoftKeyboard();

        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(click());
        onView(withId(R.id.MealPlan_NumberOfServingsEditInput)).perform(typeText("1"));
        closeSoftKeyboard();

        onView(allOf(withId(R.id.searchButton), isDescendantOfA(withId(R.id.recipeEditFragment)))).perform(click());

        onView(withText(recipe)).perform(click());

        onView(allOf(withId(R.id.searchButton), isDescendantOfA(withId(R.id.MealPlan_IngredientEditFragment)))).perform(click());

        onView(withText(ingredient)).perform(click());

        onView(withId(R.id.edit_amountInput)).perform(typeText("1"));
        closeSoftKeyboard();

        onView(withId(R.id.edit_unitInput)).perform(typeText("count"));
        closeSoftKeyboard();

        onView(withId(R.id.ingredient_save_button)).perform(click());
        closeSoftKeyboard();

        onView(withId(R.id.save_fab)).perform(click());
        Thread.sleep(1000);

        cleanUpMealPlan("Hearty Breakfast");
        cleanUpRecipe(recipe);
        cleanUpIngredient(ingredient);
    }

    @Test
    public void testViewMealPlan(){

    }

    @Test
    public void testAddInvalidMealPlan(){

    }

    @Test
    public void testDeleteMealPlan(){

    }

    @Test
    public void testUpdateMealPlanByUpdatingRecipes(){

    }

    @Test
    public void testUpdateMealPlanByUpdatingIngredients(){

    }

    @Test
    public void testUpdateMealPlanInvalid(){

    }
}
