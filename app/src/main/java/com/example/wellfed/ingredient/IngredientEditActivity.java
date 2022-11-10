package com.example.wellfed.ingredient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.wellfed.EditActivityBase;
import com.example.wellfed.R;
import com.example.wellfed.common.ConfirmDialog;
import com.example.wellfed.common.RequiredDateTextInputLayout;
import com.example.wellfed.common.RequiredDropdownTextInputLayout;
import com.example.wellfed.common.RequiredNumberTextInputLayout;
import com.example.wellfed.common.RequiredTextInputLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;
import java.util.Objects;

public class IngredientEditActivity extends EditActivityBase
        implements ConfirmDialog.OnConfirmListener {
    /**
     * RequiredTextInputLayout for the StorageIngredient's description.
     */
    private RequiredTextInputLayout descriptionInput;
    /**
     * RequiredNumberTextInputLayout for the StorageIngredient's amount.
     */
    private RequiredNumberTextInputLayout amountInput;
    /**
     * RequiredTextInputLayout for the StorageIngredient's unit.
     */
    private RequiredDropdownTextInputLayout unitInput;
    /**
     * RequiredDropdownTextInputLayout for the StorageIngredient's location.
     */
    private RequiredDropdownTextInputLayout locationInput;
    /**
     * RequiredDateTextInputLayout for the StorageIngredient's expiration date.
     */
    private RequiredDateTextInputLayout bestBeforeInput;
    /**
     * RequiredDropdownTextInputLayout for the StorageIngredient's category.
     */
    private RequiredDropdownTextInputLayout categoryInput;
    /**
     * StorageIngredient object for the ingredient.
     */
    private StorageIngredient ingredient;

    /**
     * OnCreate method for the activity.
     * @param savedInstanceState Bundle object for the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_ingredient);
        descriptionInput = findViewById(R.id.descriptionInput);
        amountInput = findViewById(R.id.amountInput);
        unitInput = findViewById(R.id.unitInput);
        locationInput = findViewById(R.id.locationInput);
        bestBeforeInput = findViewById(R.id.bestBeforeInput);
        categoryInput = findViewById(R.id.categoryInput);

        this.categoryInput.setSimpleItems(new String[]{"Fruit", "Dairy",
                "Protein"});
        this.locationInput.setSimpleItems(new String[]{"Fridge", "Freezer",
                "Pantry"});
        this.unitInput.setSimpleItems(new String[]{"oz", "lb", "g", "kg",
                "tsp", "tbsp", "cup", "qt", "gal", "ml", "l", "pt", "fl oz",
                "count"});
        RequiredDateTextInputLayout bestBeforeLayout = findViewById(R.id.bestBeforeInput);

        // Get ingredient from intent
        ingredient = (StorageIngredient) getIntent().getSerializableExtra("ingredient");

        if (ingredient != null) {
            descriptionInput.setPlaceholderText(ingredient.getDescription());
            amountInput.setPlaceholderText(String.valueOf(ingredient.getAmount()));
            unitInput.setPlaceholderText(ingredient.getUnit());
            locationInput.setPlaceholderText(ingredient.getLocation());
            if (ingredient.getCategory() != null) {
                categoryInput.setPlaceholderText(ingredient.getCategory());
            }
            // Set date in yyyy-MM-dd format
            bestBeforeLayout.setPlaceholderDate(ingredient.getBestBeforeDate());
        }

        // Enable back button in action bar to go back to previous activity
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Set up save button
        FloatingActionButton saveButton = findViewById(R.id.ingredient_save_button);
        saveButton.setOnClickListener(view -> {
            onSave();
        });
    }

    /**
     * checks if there are unsaved changes
     * @return true if there are unsaved changes, false otherwise
     */
    public Boolean hasUnsavedChanges() {
        if (descriptionInput.hasChanges()) {
            return true;
        }
        if (amountInput.hasChanges()) {
            return true;
        }
        if (unitInput.hasChanges()) {
            return true;
        }
        if (locationInput.hasChanges()) {
            return true;
        }
        if (bestBeforeInput.hasChanges()) {
            return true;
        }
        return false;
    }

    /**
     * Method to save the ingredient.
     */
    private void onSave() {
        // Verify that all fields are filled
        if (!descriptionInput.isValid()) {
            return;
        }
        if (!amountInput.isValid()) {
            return;
        }
        if (!unitInput.isValid()) {
            return;
        }
        if (!categoryInput.isValid()) {
            return;
        }
        if (!locationInput.isValid()) {
            return;
        }
        if (!bestBeforeInput.isValid()) {
            return;
        }
        String type = "edit";
        if (ingredient == null) {
            ingredient = new StorageIngredient(descriptionInput.getText());
            type = "add";
        }
        Date date = bestBeforeInput.getDate();
        ingredient = new StorageIngredient(descriptionInput.getText(),
                amountInput.getDouble(),
                unitInput.getText(), locationInput.getText(),
                date);
        Intent intent = new Intent();
        intent.putExtra("type", type);
        intent.putExtra("ingredient", ingredient);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
