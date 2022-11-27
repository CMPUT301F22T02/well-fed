package com.xffffff.wellfed.mealplan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xffffff.wellfed.EditActivityBase;
import com.xffffff.wellfed.R;
import com.xffffff.wellfed.common.RequiredDateTextInputLayout;
import com.xffffff.wellfed.common.RequiredDropdownTextInputLayout;
import com.xffffff.wellfed.common.RequiredNumberTextInputLayout;
import com.xffffff.wellfed.common.RequiredTextInputLayout;
import com.xffffff.wellfed.ingredient.EditIngredientsFragment;
import com.xffffff.wellfed.ingredient.IngredientAdapter;
import com.xffffff.wellfed.recipe.EditRecipesAdapter;
import com.xffffff.wellfed.recipe.EditRecipesFragment;

import java.util.Locale;

/**
 * MealPlanEditActivity class. It is the activity that allows the user to edit
 * the meal plan.
 */
public class MealPlanEditActivity extends EditActivityBase {
    /**
     * titleTextInput is the text input for the title.
     */
    private RequiredTextInputLayout titleTextInput;
    /**
     * dateTextInput is the text input for the date.
     */
    private RequiredDateTextInputLayout dateTextInput;
    /**
     * categoryTextInput is the text input for the category.
     */
    private RequiredDropdownTextInputLayout categoryTextInput;
    /**
     * numberOfServingsTextInput is the text input for the number of servings.
     */
    private RequiredNumberTextInputLayout numberOfServingsTextInput;
    /**
     * ingredientEditAdapter is the adapter to edit the ingredients.
     */
    private IngredientAdapter ingredientEditAdapter;
    /**
     * recipesEditAdapter is the adapter to edit the recipes.
     */
    private EditRecipesAdapter recipesEditAdapter;
    /**
     * mealPlan is the meal plan object.
     */
    private MealPlan mealPlan;
    /**
     * type is either "add" or "edit" or "delete". It is used to determine what
     * to do when the activity is finished.
     */
    private String type;

    /**
     * OnCreate method. It is called when the activity is created. It sets up
     * the activity and displays the meal plan.
     *
     * @param savedInstanceState The saved instance state.
     */
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan_edit);

        TextView titleTextView = findViewById(R.id.titleTextView);
        this.titleTextInput = findViewById(R.id.titleTextInput);
        this.dateTextInput = findViewById(R.id.dateTextInput);
        this.categoryTextInput = findViewById(R.id.categoryTextInput);
        this.numberOfServingsTextInput =
                findViewById(R.id.numberOfServingsTextInput);
        this.numberOfServingsTextInput.setRequireLong();
        this.numberOfServingsTextInput.setRequirePositiveNumber(true);
        FloatingActionButton fab = findViewById(R.id.save_fab);
        fab.setOnClickListener(view -> onSave());
        Intent intent = this.getIntent();
        this.mealPlan = (MealPlan) intent.getSerializableExtra("mealPlan");
        //        TODO: don't hard code
        this.categoryTextInput.setSimpleItems(
                new String[]{"Breakfast", "Lunch", "Dinner"});

        EditRecipesFragment recipesEditFragment = new EditRecipesFragment();
        this.recipesEditAdapter = new EditRecipesAdapter();
        recipesEditFragment.setAdapter(this.recipesEditAdapter);
        recipesEditFragment.setTitle("Recipes");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.recipeEditFragment, recipesEditFragment).commit();

        EditIngredientsFragment ingredientEditFragment =
                new EditIngredientsFragment();
        this.ingredientEditAdapter = new IngredientAdapter();
        ingredientEditFragment.setAdapter(this.ingredientEditAdapter);
        ingredientEditFragment.setTitle("Ingredients");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.ingredientEditFragment, ingredientEditFragment)
                .commit();


        if (this.mealPlan != null) {
            this.type = "edit";
            titleTextView.setText(R.string.edit_meal_plan);
            this.titleTextInput.setPlaceholderText(this.mealPlan.getTitle());
            this.dateTextInput.setPlaceholderDate(this.mealPlan.getEatDate());
            this.categoryTextInput.setPlaceholderText(
                    this.mealPlan.getCategory());
            this.numberOfServingsTextInput.setPlaceholderText(
                    String.format(Locale.US, "%d", mealPlan.getServings()));
        } else {
            this.type = "add";
            titleTextView.setText(R.string.add_meal_plan);
            this.mealPlan = new MealPlan(null);
        }
        ingredientEditAdapter.setItems(this.mealPlan.getIngredients());
        recipesEditAdapter.setItems(this.mealPlan.getRecipes());
    }

    /**
     * hasUnsavedChanges returns true if there are unsaved changes.
     *
     * @return true if there are unsaved changes.
     */
    public Boolean hasUnsavedChanges() {
        if (this.titleTextInput.hasChanges()) {
            return true;
        }
        if (this.dateTextInput.hasChanges()) {
            return true;
        }
        if (this.categoryTextInput.hasChanges()) {
            return true;
        }
        if (this.numberOfServingsTextInput.hasChanges()) {
            return true;
        }
        if (this.ingredientEditAdapter.hasChanges()) {
            return true;
        }
        return this.recipesEditAdapter.hasChanges();
    }

    /**
     * onSave is called when the save button is clicked. It saves the meal plan.
     */
    private void onSave() {
        if (!this.titleTextInput.isValid()) {
            return;
        }
        if (!this.dateTextInput.isValid()) {
            return;
        }
        if (!this.categoryTextInput.isValid()) {
            return;
        }
        if (!this.numberOfServingsTextInput.isValid()) {
            return;
        }
        if (this.mealPlan.getIngredients().size() +
                this.mealPlan.getRecipes().size() < 1) {
            this.makeSnackbar("Please add at least one ingredient or recipe");
            return;
        }
        String title = this.titleTextInput.getText();
        this.mealPlan.setTitle(title);
        this.mealPlan.setCategory(this.categoryTextInput.getText());
        this.mealPlan.setEatDate(this.dateTextInput.getDate());
        this.mealPlan.setServings(this.numberOfServingsTextInput.getLong());
        Intent intent = new Intent();
        intent.putExtra("type", this.type);
        intent.putExtra("mealPlan", this.mealPlan);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * onPointerCaptureChanged is called when the pointer capture changes.
     *
     * @param hasCapture true if the pointer has capture.
     */
    @Override public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}