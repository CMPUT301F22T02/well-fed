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
import com.xffffff.wellfed.common.OnItemClickListener;
import com.xffffff.wellfed.recipe.Recipe;
import com.xffffff.wellfed.recipe.RecipeActivity;

/**
 * MealPlanActivity class. It is the activity that displays the meal plan. It
 * is launched by the meal plan fragment.
 */
public class MealPlanActivity extends ActivityBase
        implements ConfirmDialog.OnConfirmListener,
                   OnItemClickListener<Recipe> {
    /**
     * The ARG_MEAL_PLAN constant is the key for the meal plan extra.
     */
    private static final String ARG_MEAL_PLAN = "mealPlan";
    /**
     * The MealPlanController
     */
    private MealPlanController controller;

    /**
     * The titleTextView is the TextView that displays the title of the meal
     */
    private TextView titleTextView;
    /**
     * The categoryTextView is the TextView that displays the category of the
     * meal
     */
    private TextView categoryTextView;
    /**
     * The dateTextView is the TextView that displays the date of the meal
     */
    private TextView dateTextView;
    /**
     * The servingsTextView is the TextView that displays the number of
     * servings of the meal
     */
    private TextView servingsTextView;
    /**
     * dateUtil is the DateUtil object for the activity.
     */
    private DateUtil dateUtil;
    /**
     * recipeAdapter is the adapter for the RecyclerView that displays the
     * recipes.
     */
    private MealPlanRecipeItemAdapter recipeAdapter;
    /**
     * ingredientAdapter is the adapter for the RecyclerView that displays the
     * ingredients.
     */
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
     * The floating action button for the activity that allows the user to
     * edit the meal plan.
     */
    private FloatingActionButton fab;

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

        fab = findViewById(R.id.save_fab);
        titleTextView =
                findViewById(R.id.mealPlanTitleTextView);
        categoryTextView =
                findViewById(R.id.mealPlanCategoryTextView);
        dateTextView = findViewById(R.id.mealPlanDateTextView);
        servingsTextView =
                findViewById(R.id.mealPlanNumberOfServingsTextView);
        dateUtil = new DateUtil();

        controller = new MealPlanController(this);

        Intent intent = getIntent();
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


        fab.setOnClickListener(view -> launcher.launch(mealPlan));
    }

    /**
     * OnConfirm method. It is called when the user confirms the delete of
     * the meal plan.
     */
    public void onConfirm() {
        controller.stopListening();
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


    /**
     * updateUI method. It updates the UI with the meal plan information.
     */
    public void updateUI() {
        titleTextView.setText(mealPlan.getTitle());
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

        if (mealPlan instanceof MealPlanProxy) {
            fab.hide();
        } else {
            fab.show();
        }
    }

    /**
     * onPause method. It is called when the activity is paused. It stops
     * listening to the meal plan.
     */
    @Override
    protected void onPause() {
        super.onPause();
        controller.stopListening();
    }
}