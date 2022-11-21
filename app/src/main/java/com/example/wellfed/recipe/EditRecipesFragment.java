package com.example.wellfed.recipe;

import android.content.Intent;
import android.util.Pair;

import com.example.wellfed.common.EditRecyclerViewFragment;
import com.example.wellfed.ingredient.Ingredient;

public class EditRecipesFragment extends EditRecyclerViewFragment<Recipe> {

    public Intent createOnEditIntent(Recipe item) {
        Intent intent = new Intent(getContext(), RecipeEditActivity.class);
        intent.putExtra("item", item);
        return intent;
    }

    public Intent createOnSearchIntent(Recipe item) {
        Intent intent = new Intent(getContext(), RecipeBookFragment.class);
        intent.putExtra("item", item);
        return intent;
    }

    @Override
    public void onSearchActivityResult(Pair<String, Recipe> result) {
        if (result == null) {
            return;
        }
        String type = result.first;
        Recipe item = result.second;
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
