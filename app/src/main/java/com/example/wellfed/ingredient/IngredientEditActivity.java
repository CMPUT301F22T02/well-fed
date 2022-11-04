package com.example.wellfed.ingredient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wellfed.R;
import com.example.wellfed.common.OnQuitListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.Objects;

public class IngredientEditActivity extends AppCompatActivity implements OnQuitListener {
    private EditText name;
    private EditText amount;
    private EditText unit;
    private EditText location;
    private EditText bestBefore;
    private StorageIngredient ingredient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_ingredient);
        name = findViewById(R.id.ingredient_name);
        amount = findViewById(R.id.ingredient_quantity);
        unit = findViewById(R.id.ingredient_unit_value);
        location = findViewById(R.id.ingredient_location);
        bestBefore = findViewById(R.id.ingredient_expiration);
        ingredient = (StorageIngredient) getIntent().getSerializableExtra("ingredient");

        if (ingredient != null) {
            name.setText(ingredient.getDescription());
            amount.setText(String.valueOf(ingredient.getAmount()));
            unit.setText(ingredient.getUnit());
            location.setText(ingredient.getLocation());
            bestBefore.setText(ingredient.getBestBefore());
        }

        // Enable back button in action bar to go back to previous activity
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Set up save button
        FloatingActionButton saveButton = findViewById(R.id.ingredient_save_button);
        saveButton.setOnClickListener(view -> {
            onSave();
        });
    }

    private Boolean hasUnsavedChanges() {
        if (ingredient == null) {
            return !name.getText().toString().isEmpty() ||
                    !amount.getText().toString().isEmpty() ||
                    !unit.getText().toString().isEmpty() ||
                    !location.getText().toString().isEmpty() ||
                    !bestBefore.getText().toString().isEmpty();
        } else {
            return !name.getText().toString().equals(ingredient.getDescription()) ||
                    !amount.getText().toString().equals(String.valueOf(ingredient.getAmount())) ||
                    !unit.getText().toString().equals(ingredient.getUnit()) ||
                    !location.getText().toString().equals(ingredient.getLocation()) ||
                    !bestBefore.getText().toString().equals(ingredient.getBestBefore());
        }
    }

    private void onSave() {
        // Verify that all fields are filled
        if (name.getText().toString().isEmpty()) {
            name.setError("Description is required");
            return;
        }

        if (amount.getText().toString().isEmpty()) {
            amount.setError("Amount is required");
            return;
        }

        if (unit.getText().toString().isEmpty()) {
            unit.setError("Unit is required");
            return;
        }

        if (location.getText().toString().isEmpty()) {
            location.setError("Location is required");
            return;
        }

        if (bestBefore.getText().toString().isEmpty()) {
            bestBefore.setError("Best before is required");
            return;
        }


        if (ingredient == null) {
            ingredient = new StorageIngredient(name.getText().toString(),
                    (float) Double.parseDouble(amount.getText().toString()),
                    unit.getText().toString(),
                    location.getText().toString(),
                    new Date(bestBefore.getText().toString()));
            Intent intent = new Intent();
            intent.putExtra("type", "add");
            intent.putExtra("ingredient", ingredient);
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else {
            ingredient.setDescription(name.getText().toString());
            ingredient.setAmount((float) Double.parseDouble(amount.getText().toString()));
            ingredient.setUnit(unit.getText().toString());
            ingredient.setLocation(location.getText().toString());
            ingredient.setBestBefore(new Date(2020, 1, 1));
            Intent intent = new Intent();
            intent.putExtra("type", "edit");
            intent.putExtra("ingredient", ingredient);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("type", "back");
        intent.putExtra("ingredient", ingredient);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onQuit() {
        Intent intent = new Intent();
        intent.putExtra("type", "quit");
        intent.putExtra("ingredient", ingredient);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
