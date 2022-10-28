package com.example.wellfed.mealplan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.R;
import com.example.wellfed.common.ConfirmQuitDialog;
import com.example.wellfed.common.OnQuitListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MealPlanEditActivity extends ActivityBase implements
                                                            OnQuitListener {
    private FloatingActionButton fab;
    private MealPlan mealPlan;

    @Override
    public void onBackPressed() {
        new ConfirmQuitDialog(this, this).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            new ConfirmQuitDialog(this, this).show();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan_edit);
        this.fab = findViewById(R.id.fab);
        this.fab.setOnClickListener(view -> onSave());
    }

    private void onSave() {
        Intent intent = new Intent();
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
}