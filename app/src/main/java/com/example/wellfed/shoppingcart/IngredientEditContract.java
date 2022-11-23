package com.example.wellfed.shoppingcart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;

import com.example.wellfed.ingredient.IngredientEditActivity;
import com.example.wellfed.ingredient.StorageIngredient;

public class IngredientEditContract extends ActivityResultContract<ShoppingCartIngredient, Pair<String, ShoppingCartIngredient>> {
	/**
	 * Creates an Intent for the IngredientEditActivity.
	 * @param context Context object for the activity.
	 * @param shoppingCartIngredient ShoppingCartIngredient object for the ingredient.
	 * @return
	 */
	@NonNull
	@Override
	public Intent createIntent(@NonNull Context context, ShoppingCartIngredient shoppingCartIngredient) {
		Intent intent = new Intent(context, IngredientEditActivity.class);
		intent.putExtra("ingredient", shoppingCartIngredient);
		return intent;
	}

	/**
	 * Pair object for the result of the IngredientEditActivity.
	 * @param i index of the ingredient.
	 * @param intent Intent object for the IngredientEditActivity.
	 * @return Pair object for the result of the IngredientEditActivity.
	 */
	@Override
	public Pair<String, ShoppingCartIngredient> parseResult(int i, Intent intent) {
		switch (i) {
			case Activity.RESULT_OK:
				if (intent == null) {
					return null;
				}
				StorageIngredient storageIngredient =
						(StorageIngredient) intent.getSerializableExtra(
						"ingredient");
				ShoppingCartIngredient ingredient = new ShoppingCartIngredient(
					storageIngredient);
				ingredient.setId(storageIngredient.getId());
				ingredient.setCategory(storageIngredient.getCategory());
				ingredient.setUnit(storageIngredient.getUnit());
				ingredient.setAmount(storageIngredient.getAmount());
				String type = intent.getStringExtra("type");
				return new Pair<>(type, ingredient);
			case Activity.RESULT_CANCELED:
				return new Pair<>("quit", null);
			default:
				return null;
		}
	}
}
