/**
 * Recipe
 * <p>
 * Version 1.0
 * <p>
 * 21 October, 2022
 * <p>
 * Copyright (c) 2022.  0xFFFFFF
 * All rights reserved.
 */

package com.xffffff.wellfed.recipe;

import androidx.annotation.Nullable;

import com.xffffff.wellfed.ingredient.Ingredient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Recipe, which contains ingredients and instructions on how to
 * prepare
 * a recipe.
 *
 * @version 1.0
 */
public class Recipe implements Serializable {
    /**
     * Holds the Ingredients needed for a Recipe.
     */
    private final List<Ingredient> ingredients;
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
    private String category;
    /**
     * Holds the user comments on a Recipe.
     */
    private String comments;

    /**
     * Holds the number of servings that a Recipe makes.
     */
    private Integer servings;

    /**
     * Holds the prep time in minutes for a Recipe.
     */
    private Integer prepTimeMinutes;

    /**
     * Holds a photograph of the result of a Recipe.
     */
    private String photograph;


    /**
     * Creates a Recipe object representing a recipe to be made.
     *
     * @param title A String representing the title of a recipe
     */
    public Recipe(String title) {
        this.title = title;
        this.ingredients = new ArrayList<>();
        this.id = null;
    }

    /**
     * Adds an Ingredient as part of a Recipe
     *
     * @param ingredient An Ingredient object to be added to the Recipe
     */
    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

    public void addIngredients(List<Ingredient> ingredients) {
        this.ingredients.addAll(ingredients);
    }

    /**
     * Removes an Ingredient from being part of a Recipe
     *
     * @param ingredient An Ingredient object to be removed from the Recipe
     */
    public void removeIngredient(Ingredient ingredient) {
        this.ingredients.remove(ingredient);
    }

    /**
     * Gets the Ingredient object at the specified index in a Recipe
     *
     * @param index The index of the desired Ingredient object
     * @return The Ingredient object that was at the index
     */
    public Ingredient getIngredient(int index) {
        return this.ingredients.get(index);
    }

    /**
     * Gets the entire ArrayList of Ingredients that make up a Recipe
     *
     * @return The ArrayList of all Ingredients
     */
    public List<Ingredient> getIngredients() {
        return this.ingredients;
    }

    /**
     * Gets the category (aka tag) of the Recipe
     *
     * @return A String representing the category indexed
     */
    @Nullable public String getCategory() {
        return this.category;
    }

    /**
     * Adds a category (aka tag) to a Recipe
     *
     * @param category The String representing category to add to the Recipe
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets the title of a Recipe
     *
     * @return A String representing the title of a Recipe
     */
    @Nullable public String getTitle() {
        return title;
    }

    /**
     * Sets the title of a Recipe
     *
     * @param title A String representing the title of a Recipe
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the comments associated with a Recipe
     *
     * @return A String representing the comments on a Recipe
     */
    @Nullable public String getComments() {
        return comments;
    }

    /**
     * Sets the comments associated with a Recipe
     *
     * @param comments A String representing the comments on a Recipe
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     * Gets the number of servings that a Recipe makes
     *
     * @return An integer representing the number of servings a Recipe makes
     */
    public Integer getServings() {
        return servings;
    }

    /**
     * Sets the number of servings that a Recipe makes
     *
     * @param servings An integer representing the number of servings a
     *                 Recipe makes
     */
    public void setServings(Integer servings) {
        this.servings = servings;
    }

    /**
     * Gets the time in minutes that it takes a Recipe to be prepared
     *
     * @return An integer representing the preparation time in minutes
     */
    public Integer getPrepTimeMinutes() {
        return prepTimeMinutes;
    }

    /**
     * Sets the prep time in minutes that it takes a Recipe to be prepared
     *
     * @param prepTimeMinutes An integer representing the prep time in minutes.
     */
    public void setPrepTimeMinutes(Integer prepTimeMinutes) {
        this.prepTimeMinutes = prepTimeMinutes;
    }

    /**
     * Gets the photograph associated with a Recipe.
     *
     * @return An Image object representing the photograph.
     */
    @Nullable public String getPhotograph() {
        return photograph;
    }

    /**
     * Sets the photograph associated with a Recipe.
     *
     * @param photograph An Image object representing the photograph to be set.
     */
    public void setPhotograph(String photograph) {
        this.photograph = photograph;
    }

    /**
     * Gets the id of a Recipe, the id of the document in the db the Recipe
     * is based off of
     *
     * @return A string representing the id of the document in the db
     */
    public String getId() {
        return this.id;
    }

    /**
     * Sets the id of a Recipe
     *
     * @param id A string representing the id of the document in the db
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Checks whether the recipe is equal to another recipe.
     * Note: Any order of ingredients is accepted.
     *
     * @param o the object to check equality with
     * @return true if the objects are equal, false otherwise
     */
    public boolean isEqual(Object o) {
        if (o.getClass() != Recipe.class) {
            return false;
        }

        ArrayList<Boolean> flags = new ArrayList<>();
        flags.add(Objects.equals(this.getId(), ((Recipe) o).getId()));
        flags.add(Objects.equals(this.getTitle(), ((Recipe) o).getTitle()));
        flags.add(
                Objects.equals(this.getCategory(), ((Recipe) o).getCategory()));
        flags.add(
                Objects.equals(this.getComments(), ((Recipe) o).getComments()));
        flags.add(Objects.equals(this.getPhotograph(),
                ((Recipe) o).getPhotograph()));
        flags.add(Objects.equals(this.getPrepTimeMinutes(),
                ((Recipe) o).getPrepTimeMinutes()));
        flags.add(
                Objects.equals(this.getServings(), ((Recipe) o).getServings()));

        // checking all ingredients (in any order!)
        flags.add(this.getIngredients().size() ==
                ((Recipe) o).getIngredients().size());

        // searching for ingredient
        for (int i = 0; i < this.getIngredients().size(); i++) {
            // search for the needed Ingredient
            boolean found = false;
            for (int j = 0; j < ((Recipe) o).getIngredients().size(); j++) {
                if (this.getIngredient(i)
                        .isEqual(((Recipe) o).getIngredient(j))) {
                    found = true;
                }
            }
            flags.add(found);
        }

        return !flags.contains(false);
    }
}