package com.example.wellfed.recipe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.R;

public class RecipesSearch extends ActivityBase {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_search);

        RecipeBookSelectFragment recipeBookFragment = new RecipeBookSelectFragment();
        recipeBookFragment.setListener(recipe -> {
            Intent intent = new Intent();
            intent.putExtra("type", "add");
            intent.putExtra("item", recipe);
            setResult(Activity.RESULT_OK, intent);
            finish();
        });
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_recipe_book, recipeBookFragment)
                .commit();
    }
}
