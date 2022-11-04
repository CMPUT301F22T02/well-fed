package com.example.wellfed.recipe;

import android.media.Image;

import com.example.wellfed.ingredient.Ingredient;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Nullable;

public class RecipeController {
    private static RecipeController instance;
    private List<Recipe> recipes;
    private RecipeAdapter recipeAdapter;
    private RecipeDB recipeDB;

    public RecipeController() {
        recipeDB = new RecipeDB();
    }

    public void setRecipeAdapter(RecipeAdapter recipeAdapter) {
        this.recipeAdapter = recipeAdapter;

    }

    public void deleteRecipe(int position) {
        if (position >= 0 && position < recipes.size()) {
            recipes.remove(position);
            recipeAdapter.notifyItemRemoved(position);
        }
    }

    public void addRecipe(Recipe recipe) {

            recipeDB.addRecipe(recipe);
            recipes.add(recipe);
            recipeAdapter.notifyItemInserted(recipes.size() - 1);

    }

    public void setRecipes(List<Recipe> recipes) {
        this.recipes = recipes;
    }

}
