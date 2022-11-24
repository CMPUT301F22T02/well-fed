package com.xffffff.wellfed.mealplan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xffffff.wellfed.ActivityBase;
import com.xffffff.wellfed.R;
import com.xffffff.wellfed.common.ConfirmDialog;
import com.xffffff.wellfed.common.DateUtil;
import com.xffffff.wellfed.common.DeleteButton;
import com.xffffff.wellfed.recipe.Recipe;
import com.xffffff.wellfed.recipe.RecipeActivity;

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
     * The MealPlanController
     */
    private MealPlanController controller;

    private TextView titleTextView;
    private TextView categoryTextView;
    private TextView dateTextView;
    private TextView servingsTextView;
    private DateUtil dateUtil;
    private MealPlanRecipeItemAdapter recipeAdapter;
    private MealPlanIngredientItemAdapter ingredientAdapter;


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
     *
     * @param savedInstanceState The saved instance state.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);
        Intent intent = getIntent();

        controller = new MealPlanController(this);

        mealPlan = (MealPlan) intent.getSerializableExtra(ARG_MEAL_PLAN);
        controller.startListening(mealPlan.getId());
        controller.setOnDataChanged(updatedMealPlan -> {
            mealPlan = updatedMealPlan;
            updateUI();
        });

        RecyclerView recipeRecyclerView = findViewById(R.id.recipeRecyclerView);
        recipeAdapter =
                new MealPlanRecipeItemAdapter();
        recipeAdapter.setItems(mealPlan.getRecipes());
        recipeAdapter.setOnItemClickListener(this);
        recipeRecyclerView.setAdapter(recipeAdapter);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView ingredientRecyclerView =
                findViewById(R.id.ingredientRecyclerView);
        ingredientAdapter = new MealPlanIngredientItemAdapter();
        ingredientAdapter.setItems(mealPlan.getIngredients());
        ingredientRecyclerView.setAdapter(ingredientAdapter);
        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        updateUI();
        DeleteButton deleteButton =
                new DeleteButton(this, findViewById(R.id.deleteButton),
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
     *
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
     *
     * @param recipe The recipe that was clicked.
     */
    @Override
    public void onItemClick(Recipe recipe) {
        Intent intent = new Intent(this, RecipeActivity.class);
        Recipe scaledRecipe = controller.scaleRecipe(recipe,
                mealPlan.getServings());
        intent.putExtra("item", scaledRecipe);
        intent.putExtra("viewonly", true);
        startActivity(intent);
    }


    public void updateUI() {
        titleTextView =
                findViewById(R.id.mealPlanTitleTextView);
        categoryTextView =
                findViewById(R.id.mealPlanCategoryTextView);
        dateTextView = findViewById(R.id.mealPlanDateTextView);
        servingsTextView =
                findViewById(R.id.mealPlanNumberOfServingsTextView);

        titleTextView.setText(mealPlan.getTitle());
        dateUtil = new DateUtil();
        String mealPlanDateText =
                "Date: " + dateUtil.format(mealPlan.getEatDate(), "yyyy-MM-dd");
        dateTextView.setText(mealPlanDateText);
        String mealPlanCategoryText = "Category: " + mealPlan.getCategory();
        categoryTextView.setText(mealPlanCategoryText);
        String mealPlanNumberOfServingsText =
                "Number of servings: " + mealPlan.getServings();
        servingsTextView.setText(mealPlanNumberOfServingsText);

        recipeAdapter.setItems(mealPlan.getRecipes());
        recipeAdapter.notifyDataSetChanged();
        ingredientAdapter.setItems(mealPlan.getIngredients());
        ingredientAdapter.notifyDataSetChanged();
    }

}