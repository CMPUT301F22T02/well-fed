/**
 * Recipe
 *
 * Version 1.0
 *
 * 21 October, 2022
 *
 * Copyright (c) 2022.  0xFFFFFF
 * All rights reserved.
 */

package com.example.wellfed.recipe;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

import android.media.Image;

import androidx.annotation.Nullable;

import com.example.wellfed.ingredient.Ingredient;
import com.example.wellfed.recipe.category.BreakfastCategory;
import com.example.wellfed.recipe.category.Category;
import com.example.wellfed.recipe.category.DinnerCategory;
import com.example.wellfed.recipe.category.LunchCategory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a Recipe, which contains ingredients and instructions on how to prepare
 * a recipe.
 *
 * @version 1.0
 */
public class Recipe implements Serializable {
    /**
     * Holds the Database id of a Recipe
     */
    private String id;

    /**
     * Holds the title of a Recipe.
     */
    private String title;

    /**
     * Holds the category (aka tag) of a Recipe.
     */
    private Category category;

    /**
     * Holds the Ingredients needed for a Recipe.
     */
    private final List<RecipeIngredient> ingredients;

    /**
     * Holds the user comments on a Recipe.
     */
    private String comments;

    /**
     * Holds the number of servings that a Recipe makes.
     */
    private int servings;

    /**
     * Holds the prep time in minutes for a Recipe.
     */
    private int prepTimeMinutes;

    /**
     * Holds a photograph of the result of a Recipe.
     */
    private Image photograph;


    /**
     * Creates a Recipe object representing a recipe to be made.
     * @param title A String representing the title of a recipe
     */
    public Recipe(String title) {
        this.title = title;
        this.ingredients = new ArrayList<>();
        this.id = NULL;
        this.category = null;
    }

    /**
     * Adds an Ingredient as part of a Recipe
     * @param ingredient An Ingredient object to be added to the Recipe
     */
    public void addIngredient(RecipeIngredient ingredient) {
        this.ingredients.add(ingredient);
    }

    /**
     * Removes an Ingredient from being part of a Recipe
     * @param ingredient An Ingredient object to be removed from the Recipe
     */
    public void removeIngredient(Ingredient ingredient) {
        this.ingredients.remove(ingredient);
    }

    /**
     * Gets the Ingredient object at the specified index in a Recipe
     * @param index The index of the desired Ingredient object
     * @return The Ingredient object that was at the index
     */
    public Ingredient getIngredient(int index) {
        return this.ingredients.get(index);
    }

    /**
     * Gets the entire ArrayList of Ingredients that make up a Recipe
     * @return The ArrayList of all Ingredients
     */
    public List<RecipeIngredient> getIngredients() {
        return this.ingredients;
    }

    /**
     * Adds a category (aka tag) to a Recipe
     * @param category The String representing category to add to the Recipe
     */
    public void setCategory(String category) {
        switch (category) {
            case "Breakfast":
                this.category = new BreakfastCategory();
                break;
            case "Lunch":
                this.category = new LunchCategory();
                break;
            case "Dinner":
                this.category = new DinnerCategory();
                break;
        }
    }

    /**
     * Gets the category (aka tag) of the Recipe
     * @return A String representing the category indexed
     */
    @Nullable
    public String getCategory() {
        if(category != null)
            return category.getCategory();
        else
            return "";
    }


    /** Gets the title of a Recipe
     * @return A String representing the title of a Recipe
     */
    @Nullable
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of a Recipe
     * @param title A String representing the title of a Recipe
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the comments associated with a Recipe
     * @return A String representing the comments on a Recipe
     */
    @Nullable
    public String getComments() {
        return comments;
    }

    /**
     * Sets the comments associated with a Recipe
     * @param comments A String representing the comments on a Recipe
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * Gets the number of servings that a Recipe makes
     * @return An integer representing the number of servings a Recipe makes
     */
    public int getServings() {
        return servings;
    }

    /**
     * Sets the number of servings that a Recipe makes
     * @param servings An integer representing the number of servings a Recipe makes
     */
    public void setServings(int servings) {
        this.servings = servings;
    }

    /**
     * Gets the time in minutes that it takes a Recipe to be prepared
     * @return An integer representing the preparation time in minutes
     */
    public int getPrepTimeMinutes() {
        return prepTimeMinutes;
    }

    /**
     * Sets the prep time in minutes that it takes a Recipe to be prepared
     * @param prepTimeMinutes An integer representing the prep time in minutes.
     */
    public void setPrepTimeMinutes(int prepTimeMinutes) {
        this.prepTimeMinutes = prepTimeMinutes;
    }

    /**
     * Gets the photograph associated with a Recipe.
     * @return An Image object representing the photograph.
     */
    @Nullable
    public Image getPhotograph() {
        return photograph;
    }

    /**
     * Sets the photograph associated with a Recipe.
     * @param photograph An Image object representing the photograph to be set.
     */
    public void setPhotograph(Image photograph) {
        this.photograph = photograph;
    }

    /**
     * Sets the id of a Recipe
     * @param id A string representing the id of the document in the db
     */
    public void setId(String id){
        this.id = id;
    }

    /**
     * Gets the id of a Recipe, the id of the document in the db the Recipe is based off of
     * @return A string representing the id of the document in the db
     */
    public String getId(){
        return this.id;
    }
}
