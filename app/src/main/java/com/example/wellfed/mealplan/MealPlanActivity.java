package com.example.wellfed.mealplan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.R;
import com.example.wellfed.common.DeleteButton;
import com.example.wellfed.common.OnDeleteListener;
import com.example.wellfed.recipe.Recipe;
import com.example.wellfed.recipe.RecipeAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

public class MealPlanActivity extends ActivityBase implements OnDeleteListener {
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
    private TextView mealPlanNumberOfServingsTextView;
    private DeleteButton deleteButton;
    private FloatingActionButton fab;
    private MealPlan mealPlan;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);
        getParent();
        Intent intent = getIntent();

        mealPlan = (MealPlan) intent.getSerializableExtra(ARG_MEAL_PLAN);

        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        this.dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        mealPlanTitleTextView = findViewById(R.id.mealPlanTitleTextView);
        mealPlanDateTextView = findViewById(R.id.mealPlanDateTextView);
        mealPlanNumberOfServingsTextView =
                findViewById(R.id.mealPlanNumberOfServingsTextView);

        mealPlanTitleTextView.setText(mealPlan.getTitle());
        if (mealPlan.getEatDate() != null) {
            mealPlanDateTextView.setText(
                    dateFormat.format(mealPlan.getEatDate()));
        }
        mealPlanNumberOfServingsTextView.setText(
                "Number of servings: " + mealPlan.getServings());
        ArrayList<Recipe> recipes = mealPlan.getRecipes();
        RecyclerView recipeRecyclerView = findViewById(R.id.recipeRecyclerView);
        RecipeAdapter recipeAdapter = new RecipeAdapter(this, recipes);
        recipeRecyclerView.setAdapter(recipeAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recipeRecyclerView.setLayoutManager(linearLayoutManager);

        deleteButton = new DeleteButton(this, findViewById(R.id.deleteButton),
                "Delete Meal Plan", this);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> launcher.launch(mealPlan));
    }

    public void onDelete() {
        Intent intent = new Intent();
        intent.putExtra("type", "delete");
        setResult(Activity.RESULT_OK, intent);
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

    @Override
    public void deleteIngredient() {
    }

    @Override
    public void delete() {
    }
}