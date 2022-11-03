package com.example.wellfed.mealplan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.R;
import com.example.wellfed.common.ConfirmQuitDialog;
import com.example.wellfed.common.OnQuitListener;
import com.example.wellfed.common.RequiredDateTextInputLayout;
import com.example.wellfed.common.RequiredDropdownTextInputLayout;
import com.example.wellfed.common.RequiredNumberTextInputLayout;
import com.example.wellfed.common.RequiredTextInputLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class MealPlanEditActivity extends ActivityBase
        implements OnQuitListener {
    private RequiredTextInputLayout titleTextInput;
    private RequiredDateTextInputLayout dateTextInput;
    private RequiredDropdownTextInputLayout categoryTextInput;
    private RequiredNumberTextInputLayout numberOfServingsTextInput;
    private FloatingActionButton fab;
    private MealPlan mealPlan;
    private String type;

    @Override public void onBackPressed() {
        if (this.hasUnsavedChanges()) {
            new ConfirmQuitDialog(this, this).show();
        } else {
            super.onBackPressed();
        }
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (this.hasUnsavedChanges()) {
            if (item.getItemId() == android.R.id.home) {
                new ConfirmQuitDialog(this, this).show();
            }
        } else {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan_edit);

        TextView titleTextView = findViewById(R.id.titleTextView);
        this.titleTextInput = findViewById(R.id.titleTextInput);
        this.dateTextInput = findViewById(R.id.dateTextInput);
        this.categoryTextInput = findViewById(R.id.categoryTextInput);
        this.numberOfServingsTextInput =
                findViewById(R.id.numberOfServingsTextInput);
        this.numberOfServingsTextInput.requireInteger();
        this.numberOfServingsTextInput.setRequirePositiveNumber(true);
        this.fab = findViewById(R.id.fab);
        this.fab.setOnClickListener(view -> onSave());

        Intent intent = this.getIntent();
        this.mealPlan = (MealPlan) intent.getSerializableExtra("mealPlan");
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
        }
    }

    private Boolean hasUnsavedChanges() {
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
        String title = this.titleTextInput.getText();
        if (this.type.equals("edit")) {
            this.mealPlan.setTitle(title);
        } else {
            this.mealPlan = new MealPlan(title);
        }
        this.mealPlan.setCategory(this.categoryTextInput.getText());
        this.mealPlan.setEatDate(this.dateTextInput.getDate());
        this.mealPlan.setServings(this.numberOfServingsTextInput.getInteger());
        Intent intent = new Intent();
        intent.putExtra("type", this.type);
        intent.putExtra("mealPlan", this.mealPlan);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onQuit() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}