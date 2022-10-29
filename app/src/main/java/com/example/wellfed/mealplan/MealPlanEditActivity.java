package com.example.wellfed.mealplan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.R;
import com.example.wellfed.common.ConfirmQuitDialog;
import com.example.wellfed.common.OnQuitListener;
import com.example.wellfed.common.RequiredDateTextInputLayout;
import com.example.wellfed.common.RequiredTextInputLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.Locale;
import java.util.Objects;

public class MealPlanEditActivity extends ActivityBase
        implements OnQuitListener {
    private TextView titleTextView;
    private RequiredTextInputLayout titleTextInput;
    private RequiredDateTextInputLayout dateTextInput;
    // TODO: menu for category
    private RequiredTextInputLayout numberOfServingsTextInput;
    private FloatingActionButton fab;
    private MealPlan mealPlan;
    private String mode;

    @Override public void onBackPressed() {
//        if edits else just exit
        new ConfirmQuitDialog(this, this).show();
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            new ConfirmQuitDialog(this, this).show();
        }
        return true;
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan_edit);
        this.titleTextView = findViewById(R.id.titleTextView);
        this.titleTextInput = findViewById(R.id.titleTextInput);
        this.dateTextInput = findViewById(R.id.dateTextInput);
        this.numberOfServingsTextInput =
                findViewById(R.id.numberOfServingsTextInput);
        this.fab = findViewById(R.id.fab);
        this.fab.setOnClickListener(view -> onSave());

        Intent intent = this.getIntent();
        this.mealPlan = (MealPlan) intent.getSerializableExtra("mealPlan");
        if (this.mealPlan != null) {
            this.mode = "edit";
            this.titleTextView.setText(R.string.edit_meal_plan);
            Objects.requireNonNull(this.titleTextInput.getEditText())
                    .setText(this.mealPlan.getTitle());
            this.dateTextInput.setDate(this.mealPlan.getEatDate());
            // TODO: menu for category
            Objects.requireNonNull(this.numberOfServingsTextInput.getEditText())
                    .setText(String.format(Locale.US, "%d",
                            mealPlan.getServings()));
        } else {
            this.mode = "add";
            this.titleTextView.setText(R.string.add_meal_plan);
        }
    }

    private void onSave() {

        Intent intent = new Intent();
        intent.putExtra("mealPlan", this.mealPlan);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override public void onQuit() {
        Intent intent = new Intent();
        setResult(Activity.RESULT_CANCELED, intent);
        finish();
    }
}