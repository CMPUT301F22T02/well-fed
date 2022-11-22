package com.example.wellfed.mealplan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.wellfed.EditActivityBase;
import com.example.wellfed.R;
import com.example.wellfed.common.EditItemAdapter;
import com.example.wellfed.common.ItemAdapter;
import com.example.wellfed.common.RequiredDateTextInputLayout;
import com.example.wellfed.common.RequiredDropdownTextInputLayout;
import com.example.wellfed.common.RequiredNumberTextInputLayout;
import com.example.wellfed.common.RequiredTextInputLayout;
import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.recipe.EditRecipeIngredientsFragment;
import com.example.wellfed.recipe.EditRecipesAdapter;
import com.example.wellfed.recipe.EditRecipesFragment;
import com.example.wellfed.recipe.Recipe;
import com.example.wellfed.recipe.RecipeIngredientAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class MealPlanEditActivity extends EditActivityBase {
    private RequiredTextInputLayout titleTextInput;
    private RequiredDateTextInputLayout dateTextInput;
    private RequiredDropdownTextInputLayout categoryTextInput;
    private RequiredNumberTextInputLayout numberOfServingsTextInput;
    private FloatingActionButton fab;
    private EditRecipeIngredientsFragment ingredientEditFragment;
    private RecipeIngredientAdapter ingredientEditAdapter;
    private EditRecipesFragment recipesEditFragment;
    private EditRecipesAdapter recipesEditAdapter;
    private MealPlan mealPlan;
    private String type;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan_edit);

        TextView titleTextView = findViewById(R.id.titleTextView);
        this.titleTextInput = findViewById(R.id.titleTextInput);
        this.dateTextInput = findViewById(R.id.dateTextInput);
        this.categoryTextInput = findViewById(R.id.categoryTextInput);
        this.numberOfServingsTextInput =
                findViewById(R.id.numberOfServingsTextInput);
        this.numberOfServingsTextInput.setRequireInteger();
        this.numberOfServingsTextInput.setRequirePositiveNumber(true);
        this.fab = findViewById(R.id.save_fab);
        this.fab.setOnClickListener(view -> onSave());
        Intent intent = this.getIntent();
        this.mealPlan = (MealPlan) intent.getSerializableExtra("mealPlan");
        //        TODO: don't hard code
        this.categoryTextInput.setSimpleItems(
                new String[]{"Breakfast", "Lunch", "Dinner"});

        this.recipesEditFragment = new EditRecipesFragment();
        this.recipesEditAdapter = new EditRecipesAdapter();
        this.recipesEditFragment.setAdapter(this.recipesEditAdapter);
        this.recipesEditFragment.setTitle("Recipes");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.recipeEditFragment, this.recipesEditFragment)
                .commit();

        this.ingredientEditFragment = new EditRecipeIngredientsFragment();
        this.ingredientEditAdapter = new RecipeIngredientAdapter();
        this.ingredientEditFragment.setAdapter(this.ingredientEditAdapter);
        this.ingredientEditFragment.setTitle("Ingredients");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.MealPlan_IngredientEditFragment, this.ingredientEditFragment)
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
        if (this.recipesEditAdapter.hasChanges()) {
            return true;
        }
        return false;
    }

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
        this.mealPlan.setServings(this.numberOfServingsTextInput.getInteger());
        Intent intent = new Intent();
        intent.putExtra("type", this.type);
        intent.putExtra("mealPlan", this.mealPlan);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}