/**
 * MealPlan
 *
 * Version 1.0
 *
 * 21 October, 2022
 *
 * Copyright (c) 2022.  0xFFFFFF
 * All rights reserved.
 */

package com.example.wellfed.mealplan;

import androidx.annotation.Nullable;

import java.io.Serializable;
import com.example.wellfed.recipe.Recipe;
import com.example.wellfed.ingredient.Ingredient;

import java.util.ArrayList;
import java.util.Date;

/**
 * This class represents a MealPlan, which contains recipes and ingredients to be eaten on a
 * specific date.
 *
 * @version 1.0
 */
public class MealPlan implements Serializable {
    /**
     * Holds the title of a MealPlan.
     */
    private String title;

    /**
     * Holds the category of a MealPlan. Note that a MealPlan has only one category, unlike
     * Recipe and Ingredient.
     */
    private String category;

    /**
     * Holds the date that a MealPlan is planned to be eaten on.
     */
    private Date eatDate;

    /**
     * Holds the number of servings that a MealPlan serves. Any recipes should be scaled up to fit
     * this number.
     */
    private int servings;

    /**
     * Holds a list of all Ingredients in a MealPlan.
     */
    private final ArrayList<Ingredient> ingredients;

    /**
     * Holds a list of all Recipes in a MealPlan.
     */
    private final ArrayList<Recipe> recipes;

    /**
     * Creates a new MealPlan object which represents a meal to be eaten on a certain date.
     * @param title The title of the MealPlan
     */
    public MealPlan(String title) {
        this.title = title;
        this.ingredients = new ArrayList<>();
        this.recipes = new ArrayList<>();
    }

    /**
     * Adds an Ingredient to the MealPlan object.
     * @param ingredient The Ingredient to be added.
     */
    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

    /**
     * Removes an Ingredient from the MealPlan object.
     * @param ingredient The Ingredient to be removed.
     */
    public void removeIngredient(Ingredient ingredient) {
        this.ingredients.remove(ingredient);
    }

    /**
     * Gets an Ingredient in the MealPlan object, given the index.
     * @param index The index to get the Ingredient of
     * @return The Ingredient that was indexed
     */
    public Ingredient getIngredient(int index) {
        return this.ingredients.get(index);
    }

    /**
     * Gets the entire ArrayList of Ingredients in the MealPlan object.
     * @return The ArrayList containing all Ingredients within a MealPlan.
     */
    public ArrayList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    /**
     * Adds a Recipe to the MealPlan object.
     * @param recipe The Recipe to be added.
     */
    public void addRecipe(Recipe recipe) {
        this.recipes.add(recipe);
    }

    /**
     * Removes a Recipe from the MealPlan object.
     * @param recipe The Recipe to be removed.
     */
    public void removeRecipe(Recipe recipe) {
        this.recipes.remove(recipe);
    }

    /**
     * Gets a Recipe in the MealPlan object, given the index.
     * @param index The index to get the Recipe of
     * @return The Recipe that was indexed
     */
    public Recipe getRecipe(int index) {
        return this.recipes.get(index);
    }

    /**
     * Gets the entire ArrayList of Recipes in the MealPlan object.
     * @return The ArrayList containing all Recipes within a MealPlan.
     */
    public ArrayList<Recipe> getRecipes() {
        return this.recipes;
    }

    /**
     * Gets the title of a MealPlan object.
     * @return A String representing the title of a MealPlan.
     */
    @Nullable
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of a MealPlan object.
     * @param title The String to set as the title of the MealPlan.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the category of a MealPlan object.
     * @return The String representing the category of a MealPlan object.
     */
    @Nullable
    public String getCategory() {
        return category;
    }

    /**
     * Sets the category of a MealPlan object.
     * @param category The String to set as the category of the MealPlan object.
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets the eat date that a MealPlan is to be eaten on.
     * @return a Date representing the day that a MealPlan is to be eaten on
     */
    @Nullable
    public Date getEatDate() {
        return eatDate;
    }

    /**
     * Sets the eat date that a MealPlan is to be eaten on
     * @param eatDate a Date representing the day that a MealPlan is to be eaten on
     */
    public void setEatDate(Date eatDate) {
        this.eatDate = eatDate;
    }

    /**
     * Gets the number of servings in a MealPlan
     * @return an int representing the number of servings in a MealPlan
     */
    public int getServings() {
        return servings;
    }

    /**
     * Sets the number of servings in a MealPlan
     * @param servings an int representing the number of servings in a MealPlan
     */
    public void setServings(int servings) {
        this.servings = servings;
    }
}
