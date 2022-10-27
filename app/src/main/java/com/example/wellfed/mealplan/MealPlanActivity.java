package com.example.wellfed.mealplan;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.R;

public class MealPlanActivity extends ActivityBase {
    private static final String ARG_MEAL_PLAN = "mealPlan";
    private TextView mealPlanTitleTextView;
    private TextView mealPlanNumberOfServingsTextView;
    private Button deleteButton;
    private int position;
    private MealPlan mealPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);
        getParent();
        Intent intent = getIntent();
        position = intent.getIntExtra("Position", -1);
        mealPlan = (MealPlan) intent.getSerializableExtra(ARG_MEAL_PLAN);

        mealPlanTitleTextView = findViewById(R.id.mealPlanTitleTextView);
        mealPlanNumberOfServingsTextView = findViewById(
                R.id.mealPlanNumberOfServingsTextView
        );
        deleteButton = findViewById(R.id.deleteButton);

        mealPlanTitleTextView.setText(mealPlan.getTitle());
        mealPlanNumberOfServingsTextView.setText(
                "Number of servings: " + mealPlan.getServings()
        );
        deleteButton.setOnClickListener(v -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("Position", position);
            setResult(RESULT_OK, returnIntent);
            finish();
        });

    }
}