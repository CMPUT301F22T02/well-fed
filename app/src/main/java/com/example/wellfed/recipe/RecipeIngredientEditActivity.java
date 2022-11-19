package com.example.wellfed.recipe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.EditActivityBase;
import com.example.wellfed.R;
import com.example.wellfed.common.RequiredDropdownTextInputLayout;
import com.example.wellfed.common.RequiredNumberTextInputLayout;
import com.example.wellfed.common.RequiredTextInputLayout;
import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.ingredient.StorageIngredient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class RecipeIngredientEditActivity extends EditActivityBase {

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

    /**
     * OnCreate method for the activity.
     *
     * @param savedInstanceState Bundle object for the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe_ingredient_edit);
        descriptionInput = findViewById(R.id.descriptionInput);
        amountInput = findViewById(R.id.amountInput);
        unitInput = findViewById(R.id.unitInput);
        categoryInput = findViewById(R.id.categoryInput);

        this.categoryInput.setSimpleItems(new String[]{"Fruit", "Dairy",
                "Protein"});
        this.unitInput.setSimpleItems(new String[]{"oz", "lb", "g", "kg",
                "tsp", "tbsp", "cup", "qt", "gal", "ml", "l", "pt", "fl oz",
                "count"});

        // Get ingredient from intent
        ingredient = (Ingredient) getIntent().getSerializableExtra("ingredient");

        if (ingredient != null) {
            descriptionInput.setPlaceholderText(ingredient.getDescription());
            if (ingredient.getCategory() != null) {
                categoryInput.setPlaceholderText(ingredient.getCategory());
            }
            if (ingredient.getAmount() != null)
                amountInput.setPlaceholderText(ingredient.getAmount().toString());
            if (ingredient.getUnit() != null) unitInput.setPlaceholderText(ingredient.getUnit());
        } else {

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
        if (unitInput.hasChanges()) {
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
        String type = "edit";
        if (ingredient == null) {
            ingredient = new Ingredient(descriptionInput.getText());
            type = "add";
        }
        ingredient.setDescription(descriptionInput.getText());
        ingredient.setAmount(amountInput.getDouble());
        ingredient.setUnit(unitInput.getText());
        ingredient.setCategory(categoryInput.getText());
        Intent intent = new Intent();
        intent.putExtra("type", type);
        intent.putExtra("ingredient", ingredient);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }


}
