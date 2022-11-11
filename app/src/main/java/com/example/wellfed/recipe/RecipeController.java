package com.example.wellfed.recipe;

import android.media.Image;
import android.util.Log;

import com.example.wellfed.ingredient.Ingredient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Handles the business logic for the Recipes
 * @version 1.0.0
 */
public class RecipeController {
    /**
     * Stores the reference to the list of {@link Recipe}
     */
    private ArrayList<Recipe> recipes;

    /**
     * Adapter for the list of recipes
     */
    private RecipeAdapter recipeAdapter;

    /**
     * Stores the instance of {@link RecipeDB}
     */
    private RecipeDB recipeDB;

    /**
     * Constructor that initializes the db
     */
    public RecipeController() {
        recipeDB = new RecipeDB();
    }

    /**
     * sets the recipeAdapter
     * @param recipeAdapter
     */
    public void setRecipeAdapter(RecipeAdapter recipeAdapter) {
        this.recipeAdapter = recipeAdapter;

    }

    /**
     * handles the logic for deleting the recipe
     * @param id of the recipe to delete
     */
    public void deleteRecipe(String id) {
//        try {
//            recipeDB.delRecipe(id);
//        } catch (Exception e) {
//
//        }
    }

    /**
     * adds the recipe to db and notifies the adapter
     * @param recipe
     */
    public void addRecipe(Recipe recipe) {
//        try {
//            recipeDB.addRecipe(recipe);
//            recipes.add(recipe);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    /**
     * gets the list of recipes from the db
     * @return list of recipes
     * @throws InterruptedException when db fails to execute
     */
    public ArrayList<Recipe> getRecipes() throws InterruptedException {
        Log.d("RecipeController", "getRecipes: ");
        return null;
    }

    /**
     * set the recipes
     * @param recipes
     */
    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

}
