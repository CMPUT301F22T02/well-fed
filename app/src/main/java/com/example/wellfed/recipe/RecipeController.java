package com.example.wellfed.recipe;

import android.media.Image;
import android.util.Log;

import com.example.wellfed.ingredient.Ingredient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class RecipeController {
    private ArrayList<Recipe> recipes;
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
            try {
                recipeDB.addRecipe(recipe);
            } catch (Exception e) {
                e.printStackTrace();
            }
            recipes.add(recipe);
            recipeAdapter.notifyItemInserted(recipes.size() - 1);

    }

    public ArrayList<Recipe> getRecipes() throws InterruptedException {
        Log.d("RecipeController", "getRecipes: ");
        recipes = recipeDB.getRecipes();
        return recipes;
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

}
