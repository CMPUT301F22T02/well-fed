package com.xffffff.wellfed.ingredient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xffffff.wellfed.EditActivityBase;
import com.xffffff.wellfed.R;
import com.xffffff.wellfed.common.RequiredDropdownTextInputLayout;
import com.xffffff.wellfed.common.RequiredNumberTextInputLayout;
import com.xffffff.wellfed.common.RequiredTextInputLayout;
import com.xffffff.wellfed.unit.UnitConverter;

import java.util.Objects;

/**
 * Activity which allows user to edit/add an existing recipe's ingredient
 */
public class IngredientEditActivity extends EditActivityBase {

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
     * RequiredDropdownTextInputLayout for the StorageIngredient's category.
     */
    private RequiredDropdownTextInputLayout categoryInput;
    /**
     * StorageIngredient object for the ingredient.
     */
    private Ingredient ingredient;

    private boolean isEdit;

    /**
     * OnCreate method for the activity.
     *
     * @param savedInstanceState Bundle object for the activity.
     */
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_ingredient_edit);
        descriptionInput = findViewById(R.id.descriptionInput);
        amountInput = findViewById(R.id.amountInput);
        unitInput = findViewById(R.id.unitInput);
        categoryInput = findViewById(R.id.categoryInput);

        this.amountInput.setRequirePositiveNumber(true);

        UnitConverter converter = new UnitConverter(getApplicationContext());

        this.categoryInput.setSimpleItems(
                new String[]{"Fruit", "Dairy", "Protein"});
        this.unitInput.setSimpleItems(converter.getUnits().toArray(new String[0]));

        // Get ingredient from intent
        ingredient = (Ingredient) getIntent().getSerializableExtra("item");

        if (ingredient != null) {
            descriptionInput.setPlaceholderText(ingredient.getDescription());
            if (ingredient.getCategory() != null) {
                categoryInput.setPlaceholderText(ingredient.getCategory());
            }
            if (ingredient.getAmount() != null) {

                amountInput.setPlaceholderText(
                        ingredient.getAmount().toString());
            } else {
                isEdit = true;
            }
            if (ingredient.getUnit() != null) {
                unitInput.setPlaceholderText(ingredient.getUnit());
            }
        }

        // Enable back button in action bar to go back to previous activity
        Objects.requireNonNull(getSupportActionBar())
                .setDisplayHomeAsUpEnabled(true);

        // Set up save button
        FloatingActionButton saveButton =
                findViewById(R.id.ingredient_save_button);
        saveButton.setOnClickListener(view -> onSave());
    }

    /**
     * Checks if there are unsaved changes while editing the ingredient.
     *
     * @return true if there are unsaved changes, false otherwise
     */
    public Boolean hasUnsavedChanges() {
        if (descriptionInput.hasChanges()) {
            return true;
        }
        if (amountInput.hasChanges()) {
            return true;
        }
        return unitInput.hasChanges();
    }

    /**
     * Saves the ingredient created in the activity, and sets the activity
     * result to the new Ingredient
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
        String type = "edit";
        if (ingredient == null || isEdit) {
            ingredient = new Ingredient(descriptionInput.getText());
            type = "add";
        }
        ingredient.setDescription(descriptionInput.getText());
        ingredient.setAmount(amountInput.getDouble());
        ingredient.setUnit(unitInput.getText());
        ingredient.setCategory(categoryInput.getText());
        Intent intent = new Intent();
        intent.putExtra("type", type);
        intent.putExtra("item", ingredient);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
