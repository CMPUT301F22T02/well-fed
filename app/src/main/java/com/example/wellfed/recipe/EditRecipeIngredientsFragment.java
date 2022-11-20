package com.example.wellfed.recipe;

import android.content.Intent;
import android.util.Pair;

import com.example.wellfed.common.EditRecyclerViewFragment;
import com.example.wellfed.ingredient.Ingredient;

public class EditRecipeIngredientsFragment extends EditRecyclerViewFragment<Ingredient> {

    public Intent createOnEditIntent(Ingredient item) {
        Intent intent = new Intent(getContext(), RecipeIngredientEditActivity.class);
        intent.putExtra("item", item);
        return intent;
    }

    public Intent createOnSearchIntent(Ingredient item) {
        Intent intent = new Intent(getContext(), RecipeIngredientSearch.class);
        intent.putExtra("item", item);
        return intent;
    }

    @Override
    public void onSearchActivityResult(Pair<String, Ingredient> result) {
                if (result == null) {
            return;
        }
        String type = result.first;
        Ingredient item = result.second;
        switch (type) {
            case "add":
                onEdit(item);
            case "quit":
                break;
            default:
                throw new IllegalArgumentException();
        }
    }



}
