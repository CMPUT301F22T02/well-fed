package com.example.wellfed.shoppingcart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wellfed.R;
import com.example.wellfed.common.ConfirmDialog;
import com.example.wellfed.common.RequiredTextInputLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class ShoppingCartIngredientEditActivity extends AppCompatActivity implements ConfirmDialog.OnConfirmListener {
    /**
     * EditText for the ingredient's description.
     */
    private EditText description;

    /**
     * EditText for the ingredient's amount.
     */
    private EditText amount;

    /**
     * EditText for the ingredient's unit.
     */
    private EditText unit;

    /**
     * EditText for category of the ingredient.
     */
    private EditText category;

    /**
     * ShoppingCartIngredient object for the ingredient.
     */
    private ShoppingCartIngredient shoppingCartIngredient;

    /**
     * OnCreate method for the activity.
     * @param savedInstanceState Bundle object for the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_shopping_cart_ingredient);

        description = findViewById(R.id.shopping_cart_ingredient_description);
        amount = findViewById(R.id.shopping_cart_ingredient_amount);
        unit = findViewById(R.id.shopping_cart_ingredient_unit);
        category = findViewById(R.id.shopping_cart_ingredient_category);

        // Retrieve ingredient from intent
        shoppingCartIngredient = (ShoppingCartIngredient) getIntent().getSerializableExtra("ingredient");

        if (shoppingCartIngredient != null) {
            description.setText(shoppingCartIngredient.getDescription());
            amount.setText(String.valueOf(shoppingCartIngredient.getAmount()));
            unit.setText(shoppingCartIngredient.getUnit());

            if (shoppingCartIngredient.getCategory() != null) {
                category.setText(shoppingCartIngredient.getCategory());
            }
        }

        // Enable back button in action bar to go back to previous activity
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Set up save button
        FloatingActionButton saveButton = findViewById(R.id.shopping_cart_ingredient_save_button);
        saveButton.setOnClickListener(view -> {
            onSave();
        });
    }

    /**
     * Method to save the ingredient.
     */
    private void onSave() {
        if (description.getText().toString().isEmpty()) {
            description.setError("Description is required");
            return;
        }

        if (category.getText().toString().isEmpty()) {
            category.setError("Category is required");
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

        // Check if it's an add or edit op
        boolean flag = (shoppingCartIngredient == null);
        shoppingCartIngredient.setDescription(description.getText().toString());
        shoppingCartIngredient.setCategory(category.getText().toString());
        shoppingCartIngredient.setAmount(Float.parseFloat(amount.getText().toString()));
        shoppingCartIngredient.setUnit(unit.getText().toString());

        Intent intent = new Intent();
        // flag = true if it's add, else edit
        if (flag) {
            intent.putExtra("type", "add");
        } else {
            intent.putExtra("type", "edit");
        }
        intent.putExtra("ingredient", shoppingCartIngredient);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * Method to handle the back button.
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("type", "back");
        intent.putExtra("ingredient", shoppingCartIngredient);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    /**
     * onPointerCaptureChanged method for the activity.
     * @param hasCapture boolean for the activity.
     */
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    /**
     * onConfirm method for the activity to handle the back button.
     */
    @Override
    public void onConfirm() {
        Intent intent = new Intent();
        intent.putExtra("type", "quit");
        intent.putExtra("ingredient", shoppingCartIngredient);
        setResult(RESULT_OK, intent);
        finish();
    }
}
