package com.example.wellfed.recipe;

import android.content.Intent;
import android.util.Pair;

import com.example.wellfed.common.EditRecyclerViewFragment;
import com.example.wellfed.ingredient.Ingredient;

public class EditRecipeIngredientsFragment extends EditRecyclerViewFragment<Ingredient> {

    /**
     * createOnEditIntent creates an intent to edit an ingredient.
     * @param item The ingredient to edit.
     * @return The intent to edit the ingredient.
     */
    public Intent createOnEditIntent(Ingredient item) {
        Intent intent = new Intent(getContext(), RecipeIngredientEditActivity.class);
        intent.putExtra("item", item);
        return intent;
    }

    /**
     * createOnAddIntent creates an intent to add an ingredient.
     * @return The intent to add an ingredient.
     */
    public Intent createOnSearchIntent(Ingredient item) {
        Intent intent = new Intent(getContext(), RecipeIngredientSearch.class);
        intent.putExtra("item", item);
        return intent;
    }

    /**
     * onSearchActivityResult is called when the user returns from the search
     * activity. It allows the user to add an ingredient to the recipe or
     * quit the activity.
     * @param result The result of the search activity.
     */
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
