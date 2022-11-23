package com.example.wellfed.mealplan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.R;
import com.example.wellfed.common.ConfirmDialog;
import com.example.wellfed.common.DateUtil;
import com.example.wellfed.common.DeleteButton;
import com.example.wellfed.recipe.Recipe;
import com.example.wellfed.recipe.RecipeActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * MealPlanActivity class. It is the activity that displays the meal plan. It
 * is launched by the meal plan fragment.
 */
public class MealPlanActivity extends ActivityBase
        implements ConfirmDialog.OnConfirmListener,
                   MealPlanItemAdapter.OnItemClickListener<Recipe> {
    /**
     * The ARG_MEAL_PLAN constant is the key for the meal plan extra.
     */
    private static final String ARG_MEAL_PLAN = "mealPlan";
    /**
     * The ActivityResultLauncher for the recipe activity to launch the
     * recipe so that the user can edit the recipes in the meal plan.
     */
    private final ActivityResultLauncher<MealPlan> launcher =
            registerForActivityResult(new MealPlanEditContract(), result -> {
                String type = result.first;
                MealPlan mealPlan = result.second;
                switch (type) {
                    case "quit":
                        onQuitEdit();
                        break;
                    case "edit":
                        onEdit(mealPlan);
                        break;
                    default:
                        break;
                }
            });
    /**
     * The meal plan object.
     */
    private MealPlan mealPlan;

    /**
     * OnCreate method. It is called when the activity is created. It sets up
     * the activity and displays the meal plan.
     * @param savedInstanceState The saved instance state.
     */
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);
        Intent intent = getIntent();

        mealPlan = (MealPlan) intent.getSerializableExtra(ARG_MEAL_PLAN);

        TextView mealPlanTitleTextView = findViewById(R.id.mealPlanTitleTextView);
        TextView mealPLanCategoryTextView = findViewById(R.id.mealPlanCategoryTextView);
        TextView mealPlanDateTextView = findViewById(R.id.mealPlanDateTextView);
        TextView mealPlanNumberOfServingsTextView = findViewById(R.id.mealPlanNumberOfServingsTextView);

        mealPlanTitleTextView.setText(mealPlan.getTitle());
        DateUtil dateUtil = new DateUtil();
        String mealPlanDateText =
            "Date: " + dateUtil.format(mealPlan.getEatDate(), "yyyy-MM-dd");
        mealPlanDateTextView.setText(mealPlanDateText);
        String mealPlanCategoryText = "Category: " + mealPlan.getCategory();
        mealPLanCategoryTextView.setText(mealPlanCategoryText);
        String mealPlanNumberOfServingsText =
            "Number of servings: " + mealPlan.getServings();
        mealPlanNumberOfServingsTextView.setText(mealPlanNumberOfServingsText);

        RecyclerView recipeRecyclerView = findViewById(R.id.recipeRecyclerView);
        MealPlanRecipeItemAdapter recipeAdapter =
                new MealPlanRecipeItemAdapter();
        recipeAdapter.setItems(mealPlan.getRecipes());
        recipeAdapter.setOnItemClickListener(this);
        recipeRecyclerView.setAdapter(recipeAdapter);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView ingredientRecyclerView =
                findViewById(R.id.ingredientRecyclerView);
        MealPlanIngredientItemAdapter ingredientAdapter =
                new MealPlanIngredientItemAdapter();
        ingredientAdapter.setItems(mealPlan.getIngredients());
        ingredientRecyclerView.setAdapter(ingredientAdapter);
        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        DeleteButton deleteButton = new DeleteButton(this, findViewById(R.id.deleteButton),
            "Delete Meal Plan", this);

        FloatingActionButton fab = findViewById(R.id.save_fab);
        fab.setOnClickListener(view -> launcher.launch(mealPlan));
    }

    /**
     * OnConfirm method. It is called when the user confirms the delete of
     * the meal plan.
     */
    public void onConfirm() {
        Intent intent = new Intent();
        intent.putExtra("type", "delete");
        setResult(Activity.RESULT_OK, intent);
        intent.putExtra("mealPlan", mealPlan);
        finish();
    }

    /**
     * onEdit method. It is called when the user edits the meal plan. It
     * confirms the edit and returns the meal plan to the meal plan fragment.
     * @param mealPlan The meal plan.
     */
    private void onEdit(MealPlan mealPlan) {
        Intent intent = new Intent();
        intent.putExtra("type", "edit");
        intent.putExtra("mealPlan", mealPlan);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * onQuitEdit method. It is called when the user quits the edit of the
     * meal plan edit activity. It does nothing.
     */
    private void onQuitEdit() {
        Intent intent = new Intent();
        intent.putExtra("type", "launch");
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * OnItemClick method. It is called when the user clicks on a recipe in
     * the meal plan. It launches the recipe activity so that the user can
     * edit the recipe.
     * @param recipe The recipe that was clicked.
     */
    @Override public void onItemClick(Recipe recipe) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra("item", recipe);
        intent.putExtra("viewonly", true);
        startActivity(intent);
    }
}