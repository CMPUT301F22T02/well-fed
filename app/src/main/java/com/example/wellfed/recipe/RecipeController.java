package com.example.wellfed.recipe;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.animation.AccelerateInterpolator;

import androidx.fragment.app.FragmentActivity;

import com.example.wellfed.ActivityBase;
import com.example.wellfed.common.DBConnection;
import com.example.wellfed.ingredient.Ingredient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

/**
 * Handles the business logic for the Recipes
 *
 * @version 1.0.0
 */
public class RecipeController {

    private ActivityBase activity;

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
    public RecipeController(FragmentActivity activity) {
        this.activity = (ActivityBase) activity;
        DBConnection connection = new DBConnection(activity.getApplicationContext());
        recipeDB = new RecipeDB(connection);
        this.recipeAdapter = new RecipeAdapter(recipeDB);
    }

    public void editRecipe(Recipe recipe) {
        recipeDB.updateRecipe(recipe, (updated, success)->{
            boolean a = success;
            if (!success) {
                this.activity.makeSnackbar("Failed to edit recipe");
            }
        });
    }

    /**
     * handles the logic for deleting the recipe
     *
     * @param id of the recipe to delete
     */
    public void deleteRecipe(String id) {
        recipeDB.delRecipe(id, (deleted, success) -> {
            if (!success) {
                this.activity.makeSnackbar("Failed to delete recipe");
            }
        });
    }

    /**
     * adds the recipe to db and notifies the adapter
     *
     * @param recipe
     */
    public void addRecipe(Recipe recipe) {
        recipeDB.addRecipe(recipe, (added, success) -> {
            if (!success) {
                this.activity.makeSnackbar("Failed to add " + recipe.getTitle());
            }
        });
    }

    public RecipeAdapter getRecipeAdapter() {
        return recipeAdapter;
    }

}
