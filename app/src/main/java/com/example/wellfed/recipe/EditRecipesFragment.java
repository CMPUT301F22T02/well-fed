package com.example.wellfed.recipe;

import android.content.Intent;
import android.util.Pair;

import com.example.wellfed.common.EditRecyclerViewFragment;

public class EditRecipesFragment extends EditRecyclerViewFragment<Recipe> {

    /**
     * createOnEditIntent creates an intent to edit a recipe.
     * @param item The recipe to edit.
     * @return The intent to edit the recipe.
     */
    public Intent createOnEditIntent(Recipe item) {
        Intent intent = new Intent(getContext(), RecipeEditActivity.class);
        intent.putExtra("item", item);
        return intent;
    }

    /**
     * createOnSearchIntent creates an intent to add a recipe.
     * @param item The recipe to add.
     * @return The intent to add a recipe.
     */
    public Intent createOnSearchIntent(Recipe item) {
        Intent intent = new Intent(getContext(), RecipesSearch.class);
        intent.putExtra("item", item);
        return intent;
    }

    /**
     * onSearchActivityResult is called when the user returns from the search
     * @param result The result of the search activity.
     */
    @Override
    public void onSearchActivityResult(Pair<String, Recipe> result) {
        if (result == null) {
            return;
        }
        String type = result.first;
        Recipe item = result.second;
        switch (type) {
            case "add":
                add(item);
            case "quit":
                break;
            default:
                throw new IllegalArgumentException();
        }
    }
}
