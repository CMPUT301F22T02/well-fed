package com.example.wellfed.ingredient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.wellfed.EditActivityBase;
import com.example.wellfed.R;
import com.example.wellfed.common.ConfirmDialog;
import com.example.wellfed.common.DateUtil;
import com.example.wellfed.common.RequiredDateTextInputLayout;
import com.example.wellfed.common.RequiredDropdownTextInputLayout;
import com.example.wellfed.common.RequiredNumberTextInputLayout;
import com.example.wellfed.common.RequiredTextInputLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;
import java.util.Date;

/**
 * The activity that represents editing an Ingredient.
 */
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
	 * OnCreate method for the IngredientEdit activity. Is called when the activity is created.
	 *
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

		// Get ingredient from intent
		ingredient = (StorageIngredient) getIntent().getSerializableExtra("ingredient");


		if (ingredient != null) {
			descriptionInput.setPlaceholderText(ingredient.getDescription());
			amountInput.setPlaceholderText(String.valueOf(ingredient.getAmount()));
			unitInput.setPlaceholderText(ingredient.getUnit());
			if (ingredient.getLocation() != null) {
				locationInput.setPlaceholderText(ingredient.getLocation());
			}
			if (ingredient.getCategory() != null) {
				categoryInput.setPlaceholderText(ingredient.getCategory());
			}
			if (ingredient.getBestBefore() != null) {
				Date date = ingredient.getBestBefore();
				DateUtil dateUtil = new DateUtil();
				bestBeforeInput.setDate(date);
				bestBeforeInput.setPlaceholderText(dateUtil.format(date, "yyyy" +
					"-MM-dd"));
			}
		}


		// Enable back button in action bar to go back to previous activity
		Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

		// Set up save button
		FloatingActionButton saveButton = findViewById(R.id.ingredient_save_button);
		saveButton.setOnClickListener(view -> onSave());
	}

	/**
	 * Checks if there are any unsaved changes
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
		if (locationInput.hasChanges()) {
			return true;
		}
		return bestBeforeInput.hasChanges();
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
		ingredient.setDescription(descriptionInput.getText());
		ingredient.setAmount(amountInput.getDouble());
		ingredient.setUnit(unitInput.getText());
		ingredient.setLocation(locationInput.getText());
		ingredient.setCategory(categoryInput.getText());
		ingredient.setBestBefore(bestBeforeInput.getDate());
		Intent intent = new Intent();
		intent.putExtra("type", type);
		intent.putExtra("ingredient", ingredient);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}
}
