package com.xffffff.wellfed.shoppingcart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.xffffff.wellfed.ingredient.IngredientActivity;

public class IngredientContract extends ActivityResultContract<ShoppingCartIngredient, Pair<String, ShoppingCartIngredient>> {
	/**
	 * Creates an Intent for the IngredientEditActivity.
	 * @param context Context object for the activity.
	 * @param shoppingCartIngredient ShoppingCartIngredient object for the ingredient.
	 * @return Intent object for the IngredientEditActivity.
	 */
	@NonNull
	@Override
	public Intent createIntent(@NonNull Context context, ShoppingCartIngredient shoppingCartIngredient) {
		Intent intent = new Intent(context, IngredientActivity.class);
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
	public Pair<String, ShoppingCartIngredient> parseResult(int i, @Nullable Intent intent) {
		if (i != Activity.RESULT_OK || intent == null) {
			return null;
		}
		String type = intent.getStringExtra("type");
		ShoppingCartIngredient ingredient = (ShoppingCartIngredient) intent.getSerializableExtra("ingredient");
		return new Pair<>(type, ingredient);
	}
}
