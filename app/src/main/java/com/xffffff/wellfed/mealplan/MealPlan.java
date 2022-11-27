/**
 * MealPlan
 * <p>
 * Version 1.0
 * <p>
 * 21 October, 2022
 * <p>
 * Copyright (c) 2022.  0xFFFFFF
 * All rights reserved.
 */

package com.xffffff.wellfed.mealplan;

import androidx.annotation.Nullable;

import com.xffffff.wellfed.ingredient.Ingredient;
import com.xffffff.wellfed.recipe.Recipe;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

/**
 * This class represents a MealPlan, which contains recipes and ingredients
 * to be eaten on a
 * specific date.
 *
 * @version 1.0
 */
public class MealPlan implements Serializable {
    /**
     * Holds a list of all Ingredients in a MealPlan.
     */
    private final ArrayList<Ingredient> ingredients;
    /**
     * Holds a list of all Recipes in a MealPlan.
     */
    private final ArrayList<Recipe> recipes;
    /**
     * Holds the title of a MealPlan.
     */
    private String title;
    /**
     * Holds the category of a MealPlan. Note that a MealPlan has only one
     * category, unlike
     * Recipe and Ingredient.
     */
    private String category;
    /**
     * Holds the date that a MealPlan is planned to be eaten on.
     */
    private Date eatDate;
    /**
     * Holds the number of servings that a MealPlan serves. Any recipes
     * should be scaled up to fit this number.
     */
    private Long servings;
    /**
     * Holds the Database id of a Recipe
     */
    private String id;

    /**
     * Creates a new MealPlan object which represents a meal to be eaten on a
     * certain date.
     *
     * @param title The title of the MealPlan
     */
    public MealPlan(String title) {
        this.title = title;
        this.ingredients = new ArrayList<>();
        this.recipes = new ArrayList<>();
    }

    /**
     * Adds an Ingredient to the MealPlan object.
     *
     * @param ingredient The Ingredient to be added.
     */
    public void addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
    }

    /**
     * Removes an Ingredient from the MealPlan object.
     *
     * @param ingredient The Ingredient to be removed.
     */
    public void removeIngredient(Ingredient ingredient) {
        this.ingredients.remove(ingredient);
    }

    /**
     * Gets an Ingredient in the MealPlan object, given the index.
     *
     * @param index The index to get the Ingredient of
     * @return The Ingredient that was indexed
     */
    public Ingredient getIngredient(int index) {
        return this.ingredients.get(index);
    }

    /**
     * Gets the entire ArrayList of Ingredients in the MealPlan object.
     *
     * @return The ArrayList containing all Ingredients within a MealPlan.
     */
    public ArrayList<Ingredient> getIngredients() {
        return this.ingredients;
    }

    /**
     * Sets the entire ArrayList of Ingredients in the MealPlan object.
     */
    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients.clear();
        this.ingredients.addAll(ingredients);
    }

    /**
     * Adds a Recipe to the MealPlan object.
     *
     * @param recipe The Recipe to be added.
     */
    public void addRecipe(Recipe recipe) {
        this.recipes.add(recipe);
    }

    /**
     * Removes a Recipe from the MealPlan object.
     *
     * @param recipe The Recipe to be removed.
     */
    public void removeRecipe(Recipe recipe) {
        this.recipes.remove(recipe);
    }

    /**
     * Gets a Recipe in the MealPlan object, given the index.
     *
     * @param index The index to get the Recipe of
     * @return The Recipe that was indexed
     */
    public Recipe getRecipe(int index) {
        return this.recipes.get(index);
    }

    /**
     * Gets the entire ArrayList of Recipes in the MealPlan object.
     *
     * @return The ArrayList containing all Recipes within a MealPlan.
     */
    public ArrayList<Recipe> getRecipes() {
        return this.recipes;
    }

    /**
     * Sets the entire ArrayList of Recipes in the MealPlan object.
     */
    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes.clear();
        this.recipes.addAll(recipes);
    }

    /**
     * Gets the title of a MealPlan object.
     *
     * @return A String representing the title of a MealPlan.
     */
    @Nullable public String getTitle() {
        return title;
    }

    /**
     * Sets the title of a MealPlan object.
     *
     * @param title The String to set as the title of the MealPlan.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the category of a MealPlan object.
     *
     * @return The String representing the category of a MealPlan object.
     */
    @Nullable public String getCategory() {
        return category;
    }

    /**
     * Sets the category of a MealPlan object.
     *
     * @param category The String to set as the category of the MealPlan object.
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Gets the eat date that a MealPlan is to be eaten on.
     *
     * @return a Date representing the day that a MealPlan is to be eaten on
     */
    @Nullable public Date getEatDate() {
        return eatDate;
    }

    /**
     * Sets the eat date that a MealPlan is to be eaten on
     *
     * @param eatDate a Date representing the day that a MealPlan is to be
     *                eaten on
     */
    public void setEatDate(Date eatDate) {
        this.eatDate = eatDate;
    }

    /**
     * Gets the number of servings in a MealPlan
     *
     * @return an int representing the number of servings in a MealPlan
     */
    public Long getServings() {
        return servings;
    }

    /**
     * Sets the number of servings in a MealPlan
     *
     * @param servings an int representing the number of servings in a MealPlan
     */
    public void setServings(Long servings) {
        this.servings = servings;
    }

    /**
     * Gets the id of a MealPlan, the id of the document in the db the
     * MealPlan is based off of
     *
     * @return A string representing the id of the document in the db
     */
    public String getId() {
        return this.id;
    }

    /**
     * Sets the id of a MealPlan
     *
     * @param id A string representing the id of the document in the db
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Checks whether the meal plan is equal to another meal plan.
     * Note: Any order of ingredients/recipes is accepted.
     *
     * @param o the object to check equality with
     * @return true if the objects are equal, false otherwise
     */
    public boolean isEqual(Object o) {
        if (o.getClass() != MealPlan.class) {
            return false;
        }

        ArrayList<Boolean> flags = new ArrayList<>();
        flags.add(Objects.equals(this.getId(), ((MealPlan) o).getId()));
        flags.add(Objects.equals(this.getTitle(), ((MealPlan) o).getTitle()));
        flags.add(Objects.equals(this.getCategory(),
                ((MealPlan) o).getCategory()));
        flags.add(
                Objects.equals(this.getEatDate(), ((MealPlan) o).getEatDate()));
        flags.add(Objects.equals(this.getServings(),
                ((MealPlan) o).getServings()));

        // checking all ingredients in the MealPlan (in any order!)
        flags.add(this.getIngredients().size() ==
                ((MealPlan) o).getIngredients().size());

        // searching for ingredient
        for (int i = 0; i < this.getIngredients().size(); i++) {
            // search for the needed Ingredient
            boolean found = false;
            for (int j = 0; j < ((MealPlan) o).getIngredients().size(); j++) {
                if (this.getIngredient(i)
                        .isEqual(((MealPlan) o).getIngredient(j))) {
                    found = true;
                    break;
                }
            }
            flags.add(found);
        }

        // checking all recipes by matching them up
        flags.add(
                this.getRecipes().size() == ((MealPlan) o).getRecipes().size());
        for (int i = 0; i < this.getRecipes().size(); i++) {
            // search for the needed Recipe
            boolean found = false;
            for (int j = 0; j < ((MealPlan) o).getRecipes().size(); j++) {
                if (this.getRecipe(i).isEqual(((MealPlan) o).getRecipe(j))) {
                    found = true;
                    break;
                }
            }
            flags.add(found);
        }

        return !flags.contains(false);
    }
}
