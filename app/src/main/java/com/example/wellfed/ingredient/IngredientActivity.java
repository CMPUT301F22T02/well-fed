package com.example.wellfed.ingredient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.R;
import com.example.wellfed.common.DeleteButton;
import com.example.wellfed.common.OnDeleteListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class IngredientActivity extends ActivityBase implements OnDeleteListener {
    private IngredientContract contract;
    private StorageIngredient ingredient;
    private IngredientController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_activity);

        Intent intent = getIntent();
        this.ingredient = (StorageIngredient) intent.getSerializableExtra("Ingredient");

        // Set ingredient name
        TextView ingredientName = findViewById(R.id.ingredient_name);
        ingredientName.setText(ingredient.getDescription());

        // Set ingredient best before with id=ingredient_best_before_date_value
        TextView ingredientBestBefore = findViewById(R.id.ingredient_best_before_date_value);
        ingredientBestBefore.setText(ingredient.getBestBefore());

        // Set ingredient quantity with id=ingredient_quantity_value
        TextView ingredientQuantity = findViewById(R.id.ingredient_quantity_value);
        ingredientQuantity.setText(ingredient.getAmountAndUnit());

        // Set ingredient location with id=ingredient_location_value
        TextView ingredientLocation = findViewById(R.id.ingredient_location_value);
        ingredientLocation.setText(ingredient.getLocation());

        // Enable back button in action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Set delete button
        DeleteButton deleteButton = new DeleteButton(this,
                findViewById(R.id.ingredient_delete_button), "Delete " +
                "ingredient?", this);

        FloatingActionButton fab = findViewById(R.id.ingredient_edit_button);
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("Ingredient", ingredient);
        returnIntent.putExtra("Reason", "BackPressed");
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @Override
    public void onDelete() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("Ingredient", ingredient);
        returnIntent.putExtra("Reason", "Delete");
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    private void onQuitEdit() {
        Intent intent = new Intent();
        intent.putExtra("ingredient", ingredient);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void onEdit() {
        Intent intent = new Intent();
        intent.putExtra("type", "edit");
        intent.putExtra("mealPlan", ingredient);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            ingredient = (StorageIngredient) data.getSerializableExtra("Ingredient");
            controller.updateIngredient(ingredient);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
