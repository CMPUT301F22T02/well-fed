package com.example.wellfed.recipe;

import android.content.Intent;

import com.example.wellfed.common.EditRecyclerViewFragment;
import com.example.wellfed.ingredient.Ingredient;

public class EditRecipeIngredientsFragment extends EditRecyclerViewFragment<Ingredient> {
    public Intent createOnEditIntent(Ingredient item)  {
        Intent intent = new Intent(getContext(), RecipeIngredientEditActivity.class);
        intent.putExtra("item", item);
        return intent;
    }
}
