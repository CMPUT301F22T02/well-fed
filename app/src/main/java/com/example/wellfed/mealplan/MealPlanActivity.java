package com.example.wellfed.mealplan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.R;
import com.example.wellfed.common.DeleteButton;
import com.example.wellfed.common.OnDeleteListener;
import com.example.wellfed.recipe.Recipe;
import com.example.wellfed.recipe.RecipeActivity;
import com.example.wellfed.recipe.RecipeAdapter;

import java.util.ArrayList;

public class MealPlanActivity extends ActivityBase implements
                                                   OnDeleteListener {
    private static final String ARG_MEAL_PLAN = "mealPlan";
    private TextView mealPlanTitleTextView;
    private TextView mealPlanNumberOfServingsTextView;
    private DeleteButton deleteButton;
    private MealPlan mealPlan;

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("mealPlan", mealPlan);
        returnIntent.putExtra("Reason", "BackPressed");
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);
        getParent();
        Intent intent = getIntent();
        mealPlan = (MealPlan) intent.getSerializableExtra(ARG_MEAL_PLAN);

        mealPlanTitleTextView = findViewById(R.id.mealPlanTitleTextView);
        mealPlanNumberOfServingsTextView = findViewById(
                R.id.mealPlanNumberOfServingsTextView
        );

        mealPlanTitleTextView.setText(mealPlan.getTitle());
        mealPlanNumberOfServingsTextView.setText(
                "Number of servings: " + mealPlan.getServings()
        );
        ArrayList<Recipe> recipes = mealPlan.getRecipes();
        RecyclerView recipeRecyclerView = findViewById(R.id.recipeRecyclerView);
        RecipeAdapter recipeAdapter = new RecipeAdapter(
                this, recipes
        );
        recipeRecyclerView.setAdapter(recipeAdapter);
        recipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        deleteButton = new DeleteButton(
                this,
                findViewById(R.id.deleteButton),
                "Delete Meal Plan",
                this);
    }

    public void onDelete() {
        Intent intent = new Intent();
        intent.putExtra("Reason", "Delete");
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}