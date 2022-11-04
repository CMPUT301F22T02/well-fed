package com.example.wellfed.ingredient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wellfed.R;
import com.example.wellfed.common.OnQuitListener;

import java.util.Date;

public class IngredientEditActivity extends AppCompatActivity implements OnQuitListener {
    private EditText name;
    private EditText amount;
    private EditText unit;
    private EditText location;
    private EditText bestBefore;
    private StorageIngredient ingredient;
    private IngredientController ingredientController;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_add_dialog);
        name = findViewById(R.id.ingredient_name);
        amount = findViewById(R.id.ingredient_quantity);
        unit = findViewById(R.id.ingredient_unit);
        location = findViewById(R.id.ingredient_location);
        bestBefore = findViewById(R.id.ingredient_expiration);
        ingredient = (StorageIngredient) getIntent().getSerializableExtra("ingredient");
        ingredientController = (IngredientController) getIntent().getSerializableExtra("controller");
        position = getIntent().getIntExtra("position", -1);
        if (ingredient != null) {
            name.setText(ingredient.getDescription());
            amount.setText(String.valueOf(ingredient.getAmount()));
            unit.setText(ingredient.getUnit());
            location.setText(ingredient.getLocation());
            bestBefore.setText(ingredient.getBestBefore());
        }
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
        if (ingredient == null) {
            ingredient = new StorageIngredient(name.getText().toString(),
                    (float) Double.parseDouble(amount.getText().toString()),
                    unit.getText().toString(),
                    location.getText().toString(),
                    new Date(bestBefore.getText().toString()));
            ingredientController.addIngredient(ingredient);
        } else {
            ingredient.setDescription(name.getText().toString());
            ingredient.setAmount((float) Double.parseDouble(amount.getText().toString()));
            ingredient.setUnit(unit.getText().toString());
            ingredient.setLocation(location.getText().toString());
            ingredient.setBestBefore(new Date(bestBefore.getText().toString()));
            ingredientController.updateIngredient(position, ingredient);
        }
        finish();
    }

    @Override
    public void onQuit() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
