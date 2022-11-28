package com.xffffff.wellfed.recipe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.xffffff.wellfed.ActivityBase;
import com.xffffff.wellfed.R;

/**
 * Activity which allows user to search for an existing Recipe
 */
public class RecipeSearchActivity extends ActivityBase {
    /**
     * The onCreate method for the ingredient search screen.
     *
     * @param savedInstanceState Bundle object for the activity.
     */
    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_search);

        RecipeBookSelectFragment recipeBookFragment =
                new RecipeBookSelectFragment();
        recipeBookFragment.setListener(recipe -> {
            Intent intent = new Intent();
            intent.putExtra("type", "add");
            intent.putExtra("item", recipe);
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_recipe_book, recipeBookFragment).commit();
    }
}
