package com.example.wellfed.recipe;

import java.io.Serializable;
import java.util.List;

public class RecipeController {
    private static RecipeController instance;
    private List<Recipe> recipes;
    private RecipeAdapter recipeAdapter;

    public RecipeController(){

    }

    public void setRecipeAdapter(RecipeAdapter recipeAdapter) {
        this.recipeAdapter = recipeAdapter;
    }

    public void deleteRecipe(int position){
        if(position >= 0 && position < recipes.size()){
            recipes.remove(position);
            recipeAdapter.notifyItemRemoved(position);
        }
    }

    public void setRecipes(List<Recipe> recipes){
        this.recipes = recipes;
    }

}