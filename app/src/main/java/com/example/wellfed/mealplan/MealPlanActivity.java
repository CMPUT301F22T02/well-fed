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
import com.example.wellfed.common.DeleteButton;
import com.example.wellfed.common.UTCDate;
import com.example.wellfed.recipe.Recipe;
import com.example.wellfed.recipe.RecipeActivity;
import com.example.wellfed.recipe.RecipeContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

public class MealPlanActivity extends ActivityBase
        implements ConfirmDialog.OnConfirmListener,
                   MealPlanItemAdapter.OnItemClickListener<Recipe> {
    private static final String ARG_MEAL_PLAN = "mealPlan";
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
    private DateFormat dateFormat;
    private TextView mealPlanTitleTextView;
    private TextView mealPlanDateTextView;
    private TextView mealPLanCategoryTextView;
    private TextView mealPlanNumberOfServingsTextView;
    private DeleteButton deleteButton;
    private FloatingActionButton fab;
    private MealPlan mealPlan;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);
        Intent intent = getIntent();

        mealPlan = (MealPlan) intent.getSerializableExtra(ARG_MEAL_PLAN);

        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        this.dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        mealPlanTitleTextView = findViewById(R.id.mealPlanTitleTextView);
        mealPLanCategoryTextView = findViewById(R.id.mealPlanCategoryTextView);
        mealPlanDateTextView = findViewById(R.id.mealPlanDateTextView);
        mealPlanNumberOfServingsTextView =
                findViewById(R.id.mealPlanNumberOfServingsTextView);

        mealPlanTitleTextView.setText(mealPlan.getTitle());
        mealPlanDateTextView.setText(
                "Date: " + dateFormat.format(mealPlan.getEatDate()));
        mealPLanCategoryTextView.setText("Category: " + mealPlan.getCategory());
        mealPlanNumberOfServingsTextView.setText(
                "Number of servings: " + mealPlan.getServings());

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

        deleteButton = new DeleteButton(this, findViewById(R.id.deleteButton),
                "Delete Meal Plan", this);

        fab = findViewById(R.id.save_fab);
        fab.setOnClickListener(view -> launcher.launch(mealPlan));
    }

    public void onConfirm() {
        Intent intent = new Intent();
        intent.putExtra("type", "delete");
        setResult(Activity.RESULT_OK, intent);
        intent.putExtra("mealPlan", mealPlan);
        finish();
    }

    private void onEdit(MealPlan mealPlan) {
        Intent intent = new Intent();
        intent.putExtra("type", "edit");
        intent.putExtra("mealPlan", mealPlan);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void onQuitEdit() {
        Intent intent = new Intent();
        intent.putExtra("type", "launch");
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override public void onItemClick(Recipe recipe) {
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra("Recipe", recipe);
        startActivity(intent);
    }
}