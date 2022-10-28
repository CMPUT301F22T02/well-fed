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

    public RecipeController(){
        recipeDB = new RecipeDB();
    }

    public void setRecipeAdapter(RecipeAdapter recipeAdapter) {
        this.recipeAdapter = recipeAdapter;

        RecipeIngredient testIngredient = new RecipeIngredient();
        testIngredient.setDescription("Description");
        testIngredient.setAmount(1);
        testIngredient.setCategory("Test");
        testIngredient.setUnit("TestUnits");

        Recipe testRecipe = new Recipe("Test");
        testRecipe.setComments("Test");
        testRecipe.setServings(1);
        testRecipe.setPrepTimeMinutes(1);
        testRecipe.addIngredient(testIngredient);
        testRecipe.setCategory("Test");

        recipeDB.addRecipe(testRecipe);
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
